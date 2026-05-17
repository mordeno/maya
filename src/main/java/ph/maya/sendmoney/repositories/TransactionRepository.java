package ph.maya.sendmoney.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ph.maya.sendmoney.dto.TransactionDetailsDTO;
import ph.maya.sendmoney.models.Transaction;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("""
        SELECT new ph.maya.sendmoney.dto.TransactionDetailsDTO(
            t.id,
            t.amount,
            sender.id,
            sender.name,
            receiver.id,
            receiver.name,
            t.status,
            t.date,
            t.currency,
            t.idempotencyKey
        )
        FROM Transaction t
        JOIN t.sender s
        JOIN s.user sender
        JOIN t.receiver r
        JOIN r.user receiver
        WHERE sender.id = :userId OR receiver.id = :userId
        ORDER BY t.date DESC
    """)
    List<TransactionDetailsDTO> findTransactionsByUserId(@Param("userId") Long userId);

    @Query("""
        SELECT new ph.maya.sendmoney.dto.TransactionDetailsDTO(
            t.id,
            t.amount,
            sender.id,
            sender.name,
            receiver.id,
            receiver.name,
            t.status,
            t.date,
            t.currency,
            t.idempotencyKey
        )
        FROM Transaction t
        JOIN t.sender s
        JOIN s.user sender
        JOIN t.receiver r
        JOIN r.user receiver
        WHERE t.id = :transactionId AND (sender.id = :userId OR receiver.id = :userId)
    """)
    Optional<TransactionDetailsDTO> findTransactionDetailsByIdAndUserId(@Param("transactionId") Long transactionId, @Param("userId") Long userId);

    @Query("""
        SELECT new ph.maya.sendmoney.dto.TransactionDetailsDTO(
            t.id,
            t.amount,
            sender.id,
            sender.name,
            receiver.id,
            receiver.name,
            t.status,
            t.date,
            t.currency,
            t.idempotencyKey
        )
        FROM Transaction t
        JOIN t.sender s
        JOIN s.user sender
        JOIN t.receiver r
        JOIN r.user receiver
        WHERE t.idempotencyKey = :idempotencyKey AND sender.id = :userId
    """)
    Optional<TransactionDetailsDTO> findTransactionDetailsByIdempotencyKey(@Param("idempotencyKey") String idempotencyKey, @Param("userId") Long userId);
}
