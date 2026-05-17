package ph.maya.sendmoney.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ph.maya.sendmoney.client.ContactsClient;
import ph.maya.sendmoney.client.JsonPlaceholderUsersResponse;
import ph.maya.sendmoney.dto.*;
import ph.maya.sendmoney.dto.api.*;
import ph.maya.sendmoney.exceptions.AccountNotFoundException;
import ph.maya.sendmoney.exceptions.ContactsProviderException;
import ph.maya.sendmoney.exceptions.TransactionNotFoundException;
import ph.maya.sendmoney.models.Account;
import ph.maya.sendmoney.models.JournalEntry;
import ph.maya.sendmoney.models.Transaction;
import ph.maya.sendmoney.models.TransactionEvent;
import ph.maya.sendmoney.repositories.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private final ContactsClient contactsClient;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final JournalEntryRespository journalEntryRespository;
    private final TransactionEventRepository transactionEventRepository;

    public BalanceResponse getBalance(Long userId) {
        AccountBalanceDTO accountBalance = accountRepository
            .findBalanceByUserId(userId)
            .orElseThrow(() -> new AccountNotFoundException("Account not found for user id: " + userId));

        return new BalanceResponse(
            accountBalance.id(),
            accountBalance.balance().toPlainString(),
            accountBalance.dailyLimit().toPlainString(),
            accountBalance.currency());
    }

    public List<ContactResponse> getContacts(Long userId) {
        List<ContactResponse> contacts = new ArrayList<>();
        List<ContactDTO> contactsDto = userRepository.findContactsByUserId(userId);
        Map<Long, String> mockedEmails = getMockedEmails();
        if (mockedEmails != null) {
            contactsDto.forEach(user -> {
                String email = mockedEmails.get(user.id());
                contacts.add(new ContactResponse(user.id(), user.name(), email));
            });
            return contacts;
        } else {
            contactsDto.forEach(user -> {
                contacts.add(new ContactResponse(user.id(), user.name(), "some@email.com"));
            });
        }
        return contacts;
    }

    public List<TransactionResponse> getTransactions(Long userId) {
        List<TransactionDetailsDTO> transactions = transactionRepository.findTransactionsByUserId(userId);
        return transactions.stream()
            .map(txn -> {
                Long transactionId = txn.id();
                String name = txn.senderId().equals(userId) ? txn.receiverName() : txn.senderName();
                String amount = txn.amount().toPlainString();
                String type = txn.senderId().equals(userId) ? "SENT" : "RECEIVED";
                String date = txn.date().toString();
                String currency = txn.currency();
                return new TransactionResponse(transactionId, name, amount, type, date, currency);
            }).collect(Collectors.toList());
    }

    public TransactionDetailsResponse getTransactionDetails(Long userId, Long txnId) {
        TransactionDetailsDTO txn = transactionRepository
            .findTransactionDetailsByIdAndUserId(txnId, userId)
            .orElseThrow(() -> new TransactionNotFoundException("Transaction not found for user: " + userId));

        return new TransactionDetailsResponse(
            txn.id(),
            txn.senderName(),
            txn.receiverName(),
            txn.amount().toPlainString(),
            txn.senderId().equals(userId) ? "SENT" : "RECEIVED",
            txn.status(),
            txn.date().toString(),
            txn.currency()
        );
    }

    @Transactional
    public TransactionDetailsResponse transferMoney(Long userId, TransactionRequest transactionRequest,
                                                    String idempotencyKey) {

        // 1. Check if a transaction with the same idempotency key already exists for the user
        Optional<TransactionDetailsDTO> existingTransaction = transactionRepository
            .findTransactionDetailsByIdempotencyKey(idempotencyKey, userId);
        if (existingTransaction.isPresent()) {
            TransactionDetailsDTO txn = existingTransaction.get();
            return new TransactionDetailsResponse(
                txn.id(),
                txn.senderName(),
                txn.receiverName(),
                txn.amount().toPlainString(),
                txn.senderId().equals(userId) ? "SENT" : "RECEIVED",
                txn.status(),
                txn.date().toString(),
                txn.currency()
            );
        }

        // 2. Get accounts
        Account sender = accountRepository
            .findSenderAccountWithLock(userId)
            .orElseThrow(() -> new AccountNotFoundException("Sender not found"));

        Account receiver = accountRepository
            .findReceiverAccountWithLock(userId, transactionRequest.getRecipientId())
            .orElseThrow(() -> new AccountNotFoundException("Recipient not found"));

        // 3. Validate sender has enough balance and daily limit
        if (sender.getBalance().compareTo(transactionRequest.getAmount()) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        if (sender.getDailyLimit().compareTo(transactionRequest.getAmount()) < 0) {
            throw new IllegalArgumentException("Daily limit exceeded");
        }

        // =========================================
        // Happy Path Simulation Only CREATED -> COMPLETED
        // =========================================
        // 4. Create transaction in COMPLETED status
        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setCurrency(sender.getCurrency());
        transaction.setAmount(transactionRequest.getAmount());
        transaction.setStatus("COMPLETED");
        transaction.setIdempotencyKey(idempotencyKey);
        transaction.setDate(LocalDateTime.now());
        transactionRepository.save(transaction);

        // 5.1 Debit sender balance and subtract from daily limit
        sender.setBalance(sender.getBalance().subtract(transactionRequest.getAmount()));
        sender.setDailyLimit(sender.getDailyLimit().subtract(transactionRequest.getAmount()));
        accountRepository.save(sender);

        // 5.2 Credit receiver balance
        receiver.setBalance(receiver.getBalance().add(transactionRequest.getAmount()));
        accountRepository.save(receiver);

        // 6.1 Record DEBIT in ledger for sender
        JournalEntry debitSender = new JournalEntry();
        debitSender.setTransaction(transaction);
        debitSender.setAccount(sender);
        debitSender.setType("DEBIT");
        debitSender.setDescription("AVAILABLE");
        debitSender.setAmount(transactionRequest.getAmount());
        debitSender.setDate(LocalDateTime.now());
        journalEntryRespository.save(debitSender);

        // 6.2 Record CREDIT in ledger for sender's onhold
        JournalEntry creditOnHoldEntry = new JournalEntry();
        creditOnHoldEntry.setTransaction(transaction);
        creditOnHoldEntry.setAccount(sender);
        creditOnHoldEntry.setType("CREDIT");
        creditOnHoldEntry.setDescription("ON_HOLD");
        creditOnHoldEntry.setAmount(transactionRequest.getAmount());
        creditOnHoldEntry.setDate(LocalDateTime.now());
        journalEntryRespository.save(creditOnHoldEntry);

        // 6.3 Record DEBIT in ledger for sender's onhold
        JournalEntry debitOnHoldEntry = new JournalEntry();
        debitOnHoldEntry.setTransaction(transaction);
        debitOnHoldEntry.setAccount(sender);
        debitOnHoldEntry.setType("DEBIT");
        debitOnHoldEntry.setDescription("ON_HOLD");
        debitOnHoldEntry.setAmount(transactionRequest.getAmount());
        debitOnHoldEntry.setDate(LocalDateTime.now());
        journalEntryRespository.save(debitOnHoldEntry);

        // 6.4 Record CREDIT in ledger for receiver
        JournalEntry creditReceiver = new JournalEntry();
        creditReceiver.setTransaction(transaction);
        creditReceiver.setAccount(receiver);
        creditReceiver.setType("CREDIT");
        creditReceiver.setDescription("AVAILABLE");
        creditReceiver.setAmount(transactionRequest.getAmount());
        creditReceiver.setDate(LocalDateTime.now());
        journalEntryRespository.save(creditReceiver);

        // 7.1 Record Transaction event CREATED
        TransactionEvent createdEvent = new TransactionEvent();
        createdEvent.setTransaction(transaction);
        createdEvent.setEventType("CREATED");
        createdEvent.setDate(LocalDateTime.now());
        transactionEventRepository.save(createdEvent);

        // 7.2 Record Transaction event COMPLETED
        TransactionEvent completedEvent = new TransactionEvent();
        completedEvent.setTransaction(transaction);
        completedEvent.setEventType("COMPLETED");
        completedEvent.setDate(LocalDateTime.now());
        transactionEventRepository.save(completedEvent);

        // 8. Return transaction details response
        return new TransactionDetailsResponse(
            transaction.getId(),
            sender.getUser().getName(),
            receiver.getUser().getName(),
            transaction.getAmount().toPlainString(),
            "SENT",
            transaction.getStatus(),
            transaction.getDate().toString(),
            transaction.getCurrency()
        );
    }

    public Map<Long, String> getMockedEmails() {
        try {
            List<JsonPlaceholderUsersResponse> contactEmails = contactsClient.getContacts();
            return contactEmails.stream()
                .collect(Collectors.toMap(JsonPlaceholderUsersResponse::id, JsonPlaceholderUsersResponse::email));
        } catch (Exception e) {
            return null;
        }
    }
}
