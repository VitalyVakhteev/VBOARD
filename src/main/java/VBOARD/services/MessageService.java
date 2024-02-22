package VBOARD.services;

import VBOARD.repositories.MessageRepository;
import VBOARD.vboard.Message;
import VBOARD.vboard.Reply;
import VBOARD.vboard.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for handling message-related operations.
 */
@Service
public class MessageService {
    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);

    @Autowired
    private MessageRepository messageRepository;

    /**
     * Retrieve all messages, including topics and their replies.
     * @return A list of all messages.
     */
    public List<Message> getAllMessages() {
        logger.info("Retrieving all messages @ timestamp: " + System.currentTimeMillis());
        List<Message> messages = messageRepository.findAll();
        logger.info("Retrieved " + messages.size() + " messages @ timestamp: " + System.currentTimeMillis());
        return messages;
    }

    /**
     * This method retrieves all top-level messages from the message repository.
     * A top-level message is defined as a message that does not have a parent message.
     * This is typically used to fetch all the main topics in a discussion board.
     *
     * @return A list of all top-level messages.
     */
    public List<Message> getAllTopLevelMessages() {
        logger.info("Retrieving all top-level messages @ timestamp: " + System.currentTimeMillis());
        List<Message> messages = messageRepository.findAll().stream()
                .filter(message -> message.getParentMessage() == null)
                .collect(Collectors.toList());
        logger.info("Retrieved " + messages.size() + " top-level messages @ timestamp: " + System.currentTimeMillis());
        return messages;
    }

    /**
     * Add a new topic to the message board with an optional image.
     * @param author The author of the message.
     * @param subject The subject of the message.
     * @param body The body of the message.
     * @param imageUrl The URL of the image. Can be null or empty if there's no image.
     * @return The added message.
     */
    public Message addTopic(String author, String subject, String body, String imageUrl) {
        logger.info("Adding new topic by " + author + " @ timestamp: " + System.currentTimeMillis());
        Topic topic = new Topic(author, subject, body);
        topic.setImageUrl(imageUrl);
        Message addedTopic = messageRepository.save(topic);
        logger.info("Added new topic with ID: " + addedTopic.getId() + " @ timestamp: " + System.currentTimeMillis());
        return addedTopic;
    }

    /**
     * Add a reply to an existing message.
     * @param parentId The ID of the message to reply to.
     * @param author The author of the reply.
     * @param body The body of the reply.
     * @param imageUrl The URL of the image. Can be null or empty if there's no image.
     * @return The added reply, or null if the parent message does not exist.
     */
    public Message addReply(long parentId, String author, String body, String imageUrl) {
        logger.info("Adding reply to message with ID: " + parentId + " by " + author + " @ timestamp: " + System.currentTimeMillis());
        Optional<Message> parentOpt = messageRepository.findById(parentId);
        if (parentOpt.isPresent()) {
            Message parent = parentOpt.get();
            Reply reply = new Reply(author, "Re: " + parent.getSubject(), body);
            reply.setParentMessage(parent);
            reply.setImageUrl(imageUrl);
            Message addedReply = messageRepository.save(reply);
            logger.info("Added reply with ID: " + addedReply.getId() + " to message with ID: " + parentId + " @ timestamp: " + System.currentTimeMillis());
            return addedReply;
        }
        logger.error("Failed to add reply as parent message with ID: " + parentId + " does not exist @ timestamp: " + System.currentTimeMillis());
        return null;
    }
}
