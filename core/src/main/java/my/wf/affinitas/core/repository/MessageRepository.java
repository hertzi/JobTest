package my.wf.affinitas.core.repository;

import my.wf.affinitas.core.model.Message;
import my.wf.affinitas.core.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("select m from Message m where m.recipient=?1")
    List<Message> getReceivedMessages(User recipient);
    @Query("select m from Message m where m.sender=?1")
    List<Message> getSentMessages(User sender);
}
