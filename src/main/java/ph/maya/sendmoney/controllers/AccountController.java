package ph.maya.sendmoney.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ph.maya.sendmoney.dto.AccountDTO;
import ph.maya.sendmoney.services.AccountService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<AccountDTO>> getAccounts(Principal principal) {
        String userId = principal.getName();
        List<AccountDTO> accounts = accountService.getAccountsByUserId(Long.parseLong(userId));
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(accounts);
    }
}
