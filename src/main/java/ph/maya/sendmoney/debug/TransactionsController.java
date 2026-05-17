package ph.maya.sendmoney.debug;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ph.maya.sendmoney.models.JournalEntry;
import ph.maya.sendmoney.models.Transaction;
import ph.maya.sendmoney.models.TransactionEvent;
import ph.maya.sendmoney.repositories.JournalEntryRespository;
import ph.maya.sendmoney.repositories.TransactionEventRepository;
import ph.maya.sendmoney.repositories.TransactionRepository;

import java.util.List;
import java.util.stream.Collectors;

// ==============================================
// This is just for DEBUGGING to see lists
// ==============================================
@RequiredArgsConstructor
@RestController
@RequestMapping("/debug")
public class TransactionsController {

    private final JournalEntryRespository journalEntryRespository;
    private final TransactionEventRepository transactionEventRepository;
    private final TransactionRepository transactionRepository;

    @GetMapping("/ledger")
    public ResponseEntity<List<LedgerList>> getLedgerEntries() {
        List<JournalEntry> entries = journalEntryRespository.findAll();
        List<LedgerList> response = entries.stream()
            .map(entry -> {
                LedgerList dto = new LedgerList();
                dto.setId(entry.getId());
                dto.setTransactionId(entry.getTransaction().getId());
                dto.setAccountId(entry.getAccount().getId());
                dto.setType(entry.getType());
                dto.setDescription(entry.getDescription());
                dto.setAmount(entry.getAmount().toPlainString());
                dto.setDate(entry.getDate().toString());
                return dto;
            }).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionList>> getTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        List<TransactionList> response = transactions.stream()
            .map(tx -> {
                TransactionList dto = new TransactionList();
                dto.setId(tx.getId());
                dto.setSenderName(tx.getSender().getUser().getName());
                dto.setReceiverName(tx.getReceiver().getUser().getName());
                dto.setAmount(tx.getAmount().toPlainString());
                dto.setStatus(tx.getStatus());
                dto.setDate(tx.getDate().toString());
                dto.setCurrency(tx.getCurrency());
                return dto;
            }).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/events")
    public ResponseEntity<List<TransactionEventsList>> getTransactionEvents() {
        List<TransactionEvent> events = transactionEventRepository.findAll();
        List<TransactionEventsList> response = events.stream()
            .map(event -> {
                TransactionEventsList dto = new TransactionEventsList();
                dto.setId(event.getId());
                dto.setTransactionId(event.getTransaction().getId());
                dto.setEventType(event.getEventType());
                dto.setDate(event.getDate().toString());
                return dto;
            }).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
