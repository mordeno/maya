package ph.maya.sendmoney.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ph.maya.sendmoney.dto.api.*;
import ph.maya.sendmoney.services.UserService;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @GetMapping("/balance")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<BalanceResponse> getBalance(Principal principal) {
        log.info("Received request to get balance for user: {}", principal.getName());
        String userId = principal.getName();
        BalanceResponse account = userService.getBalance(Long.parseLong(userId));
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(account);
    }

    @GetMapping("/contacts")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ContactResponse>> getContacts(Principal principal) {
        log.info("Received request to get contacts for user: {}", principal.getName());
        String userId = principal.getName();
        List<ContactResponse> contacts = userService.getContacts(Long.parseLong(userId));
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(contacts);
    }

    @GetMapping("/transactions")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<TransactionResponse>> getTransactions(Principal principal) {
        log.info("Received request to get transactions for user: {}", principal.getName());
        String userId = principal.getName();
        List<TransactionResponse> transactions = userService.getTransactions(Long.parseLong(userId));
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(transactions);
    }

    @GetMapping("/transactions/{txnId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TransactionDetailsResponse> getTransactionDetails(
        @PathVariable Long txnId, Principal principal) {

        log.info("Received request to get transaction details for transaction ID: {} and user: {}", txnId, principal.getName());
        String userId = principal.getName();
        TransactionDetailsResponse transaction = userService.getTransactionDetails(Long.parseLong(userId), txnId);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(transaction);
    }

    @PostMapping("/transfer")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TransactionDetailsResponse> sendMoney(
        @RequestHeader(value = "Idempotency-Key") String idempotencyKey,
        @Valid @RequestBody TransactionRequest transactionRequest,
        Principal principal) {

        String userId = principal.getName();

        TransactionDetailsResponse transaction = userService.transferMoney(
            Long.parseLong(userId), transactionRequest, idempotencyKey);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(transaction);
    }
}
