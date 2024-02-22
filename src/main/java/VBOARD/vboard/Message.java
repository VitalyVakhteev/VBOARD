package VBOARD.vboard;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a message. This class serves as a base for both topic posts and replies.
 */
@Entity
@Getter
@Setter
@Table(name = "messages")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "message_type", discriminatorType = DiscriminatorType.STRING)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
		@JsonSubTypes.Type(value = Topic.class, name = "topic"),
		@JsonSubTypes.Type(value = Reply.class, name = "reply")
})
@NoArgsConstructor(force = true)
public abstract class Message {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// The author of the message
	private final String author;
	// The subject of the message
	private final String subject;
	// The URL of the image associated with the message
	private String imageUrl;

	// The body of the message
	@Column(nullable = false)
	private final String body;

	// The timestamp of when the message was created
	@CreationTimestamp
	private LocalDateTime timestamp;

	// The parent message of this message, if it is a reply
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	private Message parentMessage;

	// The replies to this message, if it is a topic
	@OneToMany(mappedBy = "parentMessage", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Message> replies = new ArrayList<>();

	/**
	 * Constructor for Message.
	 *
	 * @param author The author of the message.
	 * @param subject The subject of the message.
	 * @param body The body of the message.
	 */
	public Message(String author, String subject, String body) {
		this.author = author;
		this.subject = subject;
		this.body = body;
	}

	/**
	 * Returns a string representation of the Message object.
	 *
	 * @return A string representation of the Message object.
	 */
	@Override
	public String toString() {
		return "Message{" +
				"Author='" + author + '\'' +
				", Subject='" + subject + '\'' +
				", Body='" + body + '\'' +
				", ImageUrl='" + imageUrl + '\'' +
				", ID=" + id +
				'}';
	}
}