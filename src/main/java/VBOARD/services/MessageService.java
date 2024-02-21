package VBOARD.services;

import VBOARD.repositories.MessageRepository;
import VBOARD.vboard.Message;
import VBOARD.vboard.Reply;
import VBOARD.vboard.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    /**
     * Retrieve all messages, including topics and their replies.
     * @return A list of all messages.
     */
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    /**
     * Add a new topic to the message board.
     * @param author The author of the message.
     * @param subject The subject of the message.
     * @param body The body of the message.
     * @return The added message.
     */
    public Message addTopic(String author, String subject, String body) {
        Message topic = new Topic(author, subject, body);
        return messageRepository.save(topic);
    }

    /**
     * Add a reply to an existing message.
     * @param parentId The ID of the message to reply to.
     * @param author The author of the reply.
     * @param subject The subject of the reply. If null or empty, default to "Re: parent's subject".
     * @param body The body of the reply.
     * @return The added reply, or null if the parent message does not exist.
     */
    public Message addReply(long parentId, String author, String subject, String body) {
        Optional<Message> parent = messageRepository.findById(parentId);
        if (parent.isPresent()) {
            Reply reply = new Reply(author, subject.isEmpty() ? "Re: " + parent.get().getSubject() : subject, body);
            reply.setParentMessage(parent.get());
            return messageRepository.save(reply);
        }
        return null;
    }
}
