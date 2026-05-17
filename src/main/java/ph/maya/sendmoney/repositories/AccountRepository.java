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

    // Eager fetch User to use in @Transactional service
    // Lock only applicable with concurrent access
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT a
        FROM Account a
        JOIN FETCH a.user u
        WHERE u.id = :userId
    """)
    Optional<Account> findSenderAccountWithLock(@Param("userId") Long userId);

    // Eager fetch User to use in @Transactional service
    // Lock only applicable with concurrent access
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT a
        FROM Account a
        JOIN FETCH a.user u
        WHERE u.id = :receiverUserId
        AND EXISTS (
            SELECT c FROM Contact c
            WHERE c.user.id = :senderUserId
            AND c.contact.id = :receiverUserId
        )
    """)
    Optional<Account> findReceiverAccountWithLock(@Param("senderUserId") Long senderUserId, @Param("receiverUserId") Long receiverUserId);
}
