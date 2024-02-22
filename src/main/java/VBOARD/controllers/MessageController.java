package VBOARD.controllers;

import VBOARD.services.MessageService;
import VBOARD.vboard.Message;
import VBOARD.vboard.Reply;
import VBOARD.vboard.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    // Get all messages
    @GetMapping
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllTopLevelMessages();
        return ResponseEntity.ok().body(messages);
    }

    // Add a new topic
    @PostMapping("/topic")
    public ResponseEntity<Message> addTopic(@RequestBody Topic topic) {
        System.out.println("Received topic: \n" + topic);
        Message newTopic = messageService.addTopic(topic.getAuthor(), topic.getSubject(), topic.getBody());
        return ResponseEntity.status(HttpStatus.CREATED).body(newTopic);
    }

    // Add a reply
    @PostMapping("/reply")
    public ResponseEntity<?> addReply(@RequestParam("parentId") long parentId, @RequestBody Reply reply) {
        String subject = reply.getSubject();
        Message newReply = messageService.addReply(parentId, reply.getAuthor(), reply.getBody());
        if (newReply == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Parent message not found");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(newReply);
    }
}
