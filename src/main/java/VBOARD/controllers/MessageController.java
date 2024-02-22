package VBOARD.controllers;

import VBOARD.services.MessageService;
import VBOARD.vboard.Message;
import VBOARD.vboard.Reply;
import VBOARD.vboard.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for handling message-related HTTP requests.
 */
@RestController
@RequestMapping("/api/messages")
public class MessageController {
    // Logger instance for this class
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    // MessageService instance, injected by Spring
    @Autowired
    private MessageService messageService;

    /**
     * Endpoint to get all top-level messages.
     *
     * @return A ResponseEntity containing a list of all top-level messages.
     */
    @GetMapping
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllTopLevelMessages();
        logger.info("Returning all messages @ timestamp: " + System.currentTimeMillis());
        return ResponseEntity.ok().body(messages);
    }

    /**
     * Endpoint to add a new topic.
     *
     * @param topic The topic to be added.
     * @return A ResponseEntity containing the added topic, or a bad request status if the topic could not be created.
     */
    @PostMapping("/topic")
    public ResponseEntity<Message> addTopic(@RequestBody Topic topic) {
        Message newTopic = messageService.addTopic(topic.getAuthor(), topic.getSubject(), topic.getBody(), topic.getImageUrl());
        if (newTopic == null) {
            logger.error("Error creating topic");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        logger.info("New topic created with id: " + newTopic.getId() + " @ timestamp: " + System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.CREATED).body(newTopic);
    }

    /**
     * Endpoint to add a reply to a parent message.
     *
     * @param parentId The ID of the parent message.
     * @param reply The reply to be added.
     * @return A ResponseEntity containing the added reply, or a bad request status if the parent message could not be found.
     */
    @PostMapping("/reply")
    public ResponseEntity<?> addReply(@RequestParam("parentId") long parentId, @RequestBody Reply reply) {
        String subject = reply.getSubject();
        Message newReply = messageService.addReply(parentId, reply.getAuthor(), reply.getBody(), reply.getImageUrl());
        if (newReply == null) {
            logger.error("Parent message not found");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Parent message not found");
        }
        logger.info("New reply created with id: " + newReply.getId() + " @ timestamp: " + System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.CREATED).body(newReply);
    }
}