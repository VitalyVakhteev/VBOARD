package VBOARD.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import VBOARD.vboard.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
