package ph.maya.sendmoney.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ph.maya.sendmoney.models.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
}
