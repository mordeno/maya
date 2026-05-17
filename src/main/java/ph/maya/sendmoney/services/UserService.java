package ph.maya.sendmoney.services;

import org.springframework.stereotype.Service;
import ph.maya.sendmoney.client.ContactsClient;
import ph.maya.sendmoney.client.JsonPlaceholderUsersResponse;
import ph.maya.sendmoney.dto.*;
import ph.maya.sendmoney.dto.api.BalanceResponse;
import ph.maya.sendmoney.dto.api.ContactResponse;
import ph.maya.sendmoney.dto.api.TransactionDetailsResponse;
import ph.maya.sendmoney.dto.api.TransactionResponse;
import ph.maya.sendmoney.exceptions.AccountNotFoundException;
import ph.maya.sendmoney.models.User;
import ph.maya.sendmoney.repositories.AccountRepository;
import ph.maya.sendmoney.repositories.UserRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final ContactsClient contactsClient;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    public UserService(ContactsClient contactsClient, UserRepository userRepository, AccountRepository accountRepository) {
        this.contactsClient = contactsClient;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

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

        // Fetch mocked contact names from the external API
        List<JsonPlaceholderUsersResponse> contactNames = contactsClient.getContacts();
        List<ContactDTO> contactsDto = userRepository.findContactsByUserId(userId);

        // If mocked names are available, override the contact names with the mocked names
        if (!contactNames.isEmpty()) {
            Map<Long, String> nameById = contactNames.stream()
                .collect(Collectors.toMap(JsonPlaceholderUsersResponse::id, JsonPlaceholderUsersResponse::name));

            contactsDto.forEach(user -> {
                String name = nameById.get(user.id()) != null ? nameById.get(user.id()) : user.name();
                contacts.add(new ContactResponse(user.id(), name));
            });
        }

        return contacts;
    }

    public List<TransactionResponse> getTransactions(Long userId) {
        return null;
    }

    public TransactionDetailsResponse getTransactionDetails(Long userId, Long txnId) {
        return null;
    }

    public TransactionDetailsResponse transferMoney(Long userId, TransferRequestDTO transferRequest) {
        return null;
    }
}
