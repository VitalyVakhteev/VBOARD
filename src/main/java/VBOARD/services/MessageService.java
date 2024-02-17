package VBOARD.services;

import VBOARD.vboard.Message;
import VBOARD.vboard.Reply;
import VBOARD.vboard.Topic;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class MessageService {
    private final List<Message> messages = new ArrayList<>();
    private final AtomicInteger currentId = new AtomicInteger(0);

    private final Path dataFilePath;

    public MessageService(@Value("${vboard.datafile.path}") String filePath) {
        this.dataFilePath = Paths.get(filePath.replace("file:", ""));
    }

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
        if (!Files.exists(dataFilePath))
            return;

        String content = new String(Files.readAllBytes(dataFilePath));
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, List<Map<String, Object>>>>() {}.getType();
        Map<String, List<Map<String, Object>>> data = gson.fromJson(content, type);
        List<Map<String, Object>> topics = data.get("topics");

        topics.forEach(topicData -> {
            Topic topic = parseTopic(topicData);
            messages.add(topic);
            parseChildren(topic, topicData);
        });
    }

    private LocalDateTime parseTimestamp(String timestampString) {
        try {
            return LocalDateTime.ofInstant(Instant.parse(timestampString), ZoneId.systemDefault());
        } catch (DateTimeParseException e) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS");
                return LocalDateTime.parse(timestampString, formatter);
            } catch (DateTimeParseException ex) {
                throw new RuntimeException("Failed to parse timestamp: " + timestampString, ex);
            }
        }
    }

    private Topic parseTopic(Map<String, Object> data) {
        String author = (String) data.get("from");
        String subject = (String) data.get("subject");
        String body = (String) data.get("body");
        int id = ((Double) data.get("id")).intValue();
        String timestampString = (String) data.get("timestamp");
        LocalDateTime timestamp = parseTimestamp(timestampString);
        Topic topic = new Topic(author, subject, body, id, timestamp);
        updateCurrentId(id);
        return topic;
    }

    private Reply parseReply(Map<String, Object> data) {
        String author = (String) data.get("from");
        String subject = (String) data.get("subject");
        String body = (String) data.get("body");
        int id = ((Double) data.get("id")).intValue();
        String timestampString = (String) data.get("timestamp");
        LocalDateTime timestamp = parseTimestamp(timestampString);
        Reply reply = new Reply(author, subject, body, id, timestamp);
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
     * Save history to local file.
     */
    private void saveHistory() throws IOException {
        Map<String, List<Map<String, Object>>> structuredData = new HashMap<>();
        List<Map<String, Object>> topicsList = new ArrayList<>();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        messages.stream()
                .filter(message -> message instanceof Topic)
                .forEach(topic -> {
                    Map<String, Object> topicData = convertToMap(topic);
                    List<Map<String, Object>> childrenData = new ArrayList<>();
                    addChildrenData(topic, childrenData);
                    topicData.put("children", childrenData);
                    topicsList.add(topicData);
                });

        structuredData.put("topics", topicsList);
        String json = gson.toJson(structuredData);
        Files.write(dataFilePath, json.getBytes());
    }

    /**
     * Recursively adds children replies to the provided list.
     */
    private void addChildrenData(Message parent, List<Map<String, Object>> childrenData) {
        for (Message child : parent.getChildren()) {
            Map<String, Object> childData = convertToMap(child);
            if (!child.getChildren().isEmpty()) {
                List<Map<String, Object>> grandChildrenData = new ArrayList<>();
                addChildrenData(child, grandChildrenData);
                childData.put("children", grandChildrenData);
            }
            childrenData.add(childData);
        }
    }

    /**
     * Converts a Message (or Topic/Reply) to a Map representation suitable for JSON serialization.
     */
    private Map<String, Object> convertToMap(Message message) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", message.getId());
        map.put("from", message.getAuthor());
        map.put("subject", message.getSubject());
        map.put("body", message.getBody());
        map.put("timestamp", message.getTimestamp().toString());
        return map;
    }

    /**
     * Add a new topic to the message board.
     * @param author The author of the message.
     * @param subject The subject of the message.
     * @param body The body of the message.
     * @return The added topic.
     */
    public Message addTopic(String author, String subject, String body) {
        LocalDateTime timestamp = LocalDateTime.now();
        Message topic = new Topic(author, subject, body, currentId.incrementAndGet(), timestamp);
        messages.add(topic);

        try {
            saveHistory();
        } catch (IOException e) {
            System.err.println("Error: Unable to save message history - " + e.getMessage());
        }

        return topic;
    }

    /**
     * Add a reply to an existing message.
     * @param parentId The ID of the message to reply to.
     * @param author The author of the reply.
     * @param subject The subject of the reply. If null or empty, default to "Re: parent's subject".
     * @param body The body of the reply.
     * @return The added reply, or null if the parent message does not exist.
     */
    public Message addReply(int parentId, String author, String subject, String body) {
        Optional<Message> parentMessageOpt = messages.stream()
                .filter(message -> message.getId() == parentId)
                .findFirst();

        if (parentMessageOpt.isPresent()) {
            Message parentMessage = parentMessageOpt.get();
            String finalSubject = (subject == null || subject.isEmpty()) ? "Re: " + parentMessage.getSubject() : subject;
            LocalDateTime timestamp = LocalDateTime.now();
            Reply reply = new Reply(author, finalSubject, body, currentId.incrementAndGet(), timestamp);
            parentMessage.addChild(reply);
            messages.add(reply);

            try {
                saveHistory();
            } catch (IOException e) {
                System.err.println("Error: Unable to save message history - " + e.getMessage());
            }

            return reply;
        }

        return null;
    }
}
