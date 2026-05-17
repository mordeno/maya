package ph.maya.sendmoney.repositories;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ph.maya.sendmoney.dto.AccountBalanceDTO;
import ph.maya.sendmoney.models.Account;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("""
        SELECT new ph.maya.sendmoney.dto.AccountBalanceDTO(
            a.id,
            a.balance,
            a.dailyLimit,
            a.currency
        )
        FROM Account a
        WHERE a.user.id = :userId
        """)
    Optional<AccountBalanceDTO> findBalanceByUserId(@Param("userId") Long userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT a
        FROM Account a
        WHERE a.user.id = :userId
        """)
    Optional<Account> findByUserIdWithLock(@Param("userId") Long userId);
}
