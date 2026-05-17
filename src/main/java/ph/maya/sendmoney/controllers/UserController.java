package ph.maya.sendmoney.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ph.maya.sendmoney.dto.*;
import ph.maya.sendmoney.dto.api.BalanceResponse;
import ph.maya.sendmoney.dto.api.ContactResponse;
import ph.maya.sendmoney.dto.api.TransactionDetailsResponse;
import ph.maya.sendmoney.dto.api.TransactionResponse;
import ph.maya.sendmoney.models.User;
import ph.maya.sendmoney.services.UserService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Get the balance of user's account
     * @param principal Authenticated user
     * @return User account details
     */
    @GetMapping("/balance")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<BalanceResponse> getBalance(Principal principal) {
        String userId = principal.getName();
        BalanceResponse account = userService.getBalance(Long.parseLong(userId));
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(account);
    }

    /**
     *
     * @param principal
     * @return
     */
    @GetMapping("/contacts")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ContactResponse>> getContacts(Principal principal) {
        String userId = principal.getName();
        List<ContactResponse> contacts = userService.getContacts(Long.parseLong(userId));
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(contacts);
    }

    @GetMapping("/transactions")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<TransactionResponse>> getTransactions(Principal principal) {
        String userId = principal.getName();
        List<TransactionResponse> transactions = userService.getTransactions(Long.parseLong(userId));
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(transactions);
    }

    @GetMapping("/transactions/{txnId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TransactionDetailsResponse> getTransactionDetails(@PathVariable Long txnId, Principal principal) {
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
        @Valid @RequestBody TransferRequestDTO transferRequest,
        Principal principal) {

        String userId = principal.getName();
        TransactionDetailsResponse transactionDetails = userService.transferMoney(Long.parseLong(userId), transferRequest);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(transactionDetails);
    }
}
