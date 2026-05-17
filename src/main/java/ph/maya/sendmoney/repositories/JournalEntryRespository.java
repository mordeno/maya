package ph.maya.sendmoney.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ph.maya.sendmoney.models.JournalEntry;

@Repository
public interface JournalEntryRespository extends JpaRepository<JournalEntry, Long> {
}
