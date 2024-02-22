package VBOARD.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import VBOARD.vboard.Message;

import java.util.List;

/**
 * Repository interface for handling operations related to the Message entity.
 * This interface extends JpaRepository, allowing for the use of JpaRepository methods in addition to custom ones.
 */
public interface MessageRepository extends JpaRepository<Message, Long> {
    /**
     * Method to find all top-level messages (i.e., messages that have no parent message).
     *
     * @return A list of all top-level messages.
     */
    List<Message> findByParentMessageIsNull();
}