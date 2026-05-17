package ph.maya.sendmoney.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ph.maya.sendmoney.models.TransactionEvent;

@Repository
public interface TransactionEventRepository extends JpaRepository<TransactionEvent, Long> {
}
