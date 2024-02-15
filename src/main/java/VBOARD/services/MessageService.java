package VBOARD.services;

import VBOARD.vboard.Message;
import VBOARD.vboard.Reply;
import VBOARD.vboard.Topic;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class MessageService {
    private final List<Message> messages = new ArrayList<>();
    private final AtomicInteger currentId = new AtomicInteger(0);
    @Value("classpath:data.json")
    private Resource dataFile;

    @PostConstruct
    public void init() {
        try {
            loadHistory();
        } catch (FileNotFoundException e) {
            System.err.println("Error: Unable to load message history - " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error: IO Exception while loading message history - " + e.getMessage());
        }
    }

    private void loadHistory() throws IOException {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, List<Map<String, Object>>>>() {}.getType();
        try (FileReader reader = new FileReader(dataFile.getFile())) {
            Map<String, List<Map<String, Object>>> data = gson.fromJson(reader, type);
            List<Map<String, Object>> topics = data.get("topics");

            topics.forEach(topicData -> {
                Topic topic = parseTopic(topicData);
                messages.add(topic);
                parseChildren(topic, topicData);
            });
        }
    }

    private Topic parseTopic(Map<String, Object> data) {
        String author = (String) data.get("from");
        String subject = (String) data.get("subject");
        String body = (String) data.get("body");
        int id = ((Double) data.get("id")).intValue();
        Topic topic = new Topic(author, subject, body, id);
        updateCurrentId(id);
        return topic;
    }

    private Reply parseReply(Map<String, Object> data) {
        String author = (String) data.get("from");
        String subject = (String) data.get("subject");
        String body = (String) data.get("body");
        int id = ((Double) data.get("id")).intValue();
        Reply reply = new Reply(author, subject, body, id);
        updateCurrentId(id);
        return reply;
    }

    private void parseChildren(Message parent, Map<String, Object> parentData) {
        Object childrenObject = parentData.get("children");
        if (childrenObject instanceof List<?>) {
            List<Map<String, Object>> children = (List<Map<String, Object>>) childrenObject;
            children.forEach(childData -> {
                Reply reply = parseReply(childData);
                parent.addChild(reply);
                messages.add(reply);
                parseChildren(reply, childData); // Recursively add children's replies
            });
        }
    }

    private void updateCurrentId(int id) {
        currentId.set(Math.max(currentId.get(), id));
    }

    /**
     * Retrieve all messages, including topics and their replies.
     * @return A list of all messages.
     */
    public List<Message> getAllMessages() {
        return messages;
    }

    /**
     * Add a new topic to the message board.
     * @param author The author of the message.
     * @param subject The subject of the message.
     * @param body The body of the message.
     * @return The added topic.
     */
    public Message addTopic(String author, String subject, String body) {
        Message topic = new Message(author, subject, body, currentId.incrementAndGet());
        messages.add(topic);
        return topic;
    }

    /**
     * Add a reply to an existing message.
     * @param parentId The ID of the message to reply to.
     * @param author The author of the reply.
     * @param body The body of the reply.
     * @return The added reply, or null if the parent message does not exist.
     */
    public Message addReply(int parentId, String author, String body) {
        Optional<Message> parentMessageOpt = messages.stream()
                .filter(message -> message.getId() == parentId)
                .findFirst();

        if (parentMessageOpt.isPresent()) {
            Message parentMessage = parentMessageOpt.get();
            Message reply = new Message(author, "Re: " + parentMessage.getSubject(), body, currentId.incrementAndGet());
            parentMessage.addChild(reply);
            messages.add(reply);
            return reply;
        }

        return null;    // Parent message not found
    }
}
