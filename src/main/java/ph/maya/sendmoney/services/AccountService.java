package ph.maya.sendmoney.services;

import org.springframework.stereotype.Service;
import ph.maya.sendmoney.dto.AccountDTO;
import ph.maya.sendmoney.dto.ContactDTO;
import ph.maya.sendmoney.dto.TransactionDTO;
import ph.maya.sendmoney.repositories.AccountRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public AccountDTO getBalance(Long userId) {
        return null;
    }

    public List<ContactDTO> getContacts(Long userId) {
        return null;
    }



    public List<AccountDTO> getAccountsByUserId(Long userId) {

        return new ArrayList<>();
    }

    public AccountDTO getAccountById(Long accountId) {
        return null;
    }

    public TransactionDTO transferMoney(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        return null;
    }


}
