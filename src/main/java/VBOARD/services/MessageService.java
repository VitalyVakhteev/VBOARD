package VBOARD.services;

import VBOARD.repositories.MessageRepository;
import VBOARD.vboard.Message;
import VBOARD.vboard.Reply;
import VBOARD.vboard.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
     * This method retrieves all top-level messages from the message repository.
     * A top-level message is defined as a message that does not have a parent message.
     * This is typically used to fetch all the main topics in a discussion board.
     *
     * @return A list of all top-level messages.
     */
    public List<Message> getAllTopLevelMessages() {
        return messageRepository.findAll().stream()
                .filter(message -> message.getParentMessage() == null)
                .collect(Collectors.toList());
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
        Topic topic = new Topic(author, subject, body);
        topic.setImageUrl(imageUrl);
        return messageRepository.save(topic);
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
        Optional<Message> parentOpt = messageRepository.findById(parentId);
        if (parentOpt.isPresent()) {
            Message parent = parentOpt.get();
            Reply reply = new Reply(author, "Re: " + parent.getSubject(), body);
            reply.setParentMessage(parent);
            reply.setImageUrl(imageUrl);
            return messageRepository.save(reply);
        }
        return null;
    }
}
