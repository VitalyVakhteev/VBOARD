package VBOARD.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import VBOARD.vboard.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByParentMessageIsNull();
}
