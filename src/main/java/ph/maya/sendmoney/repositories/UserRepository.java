package ph.maya.sendmoney.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ph.maya.sendmoney.dto.ContactDTO;
import ph.maya.sendmoney.models.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("""
        SELECT new ph.maya.sendmoney.dto.ContactDTO(
            cu.id,
            cu.name
        )
        FROM Contact c
        JOIN c.contact cu
        WHERE c.user.id = :userId
    """)
    List<ContactDTO> findContactsByUserId(@Param("userId") Long userId);
}
