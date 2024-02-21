/*
 *	Author: Vitaly Vakhteev
 *  Date: 01/22/24
 */

package VBOARD.vboard;

import jakarta.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a message in a message board system. This class serves as a base for both topic posts and replies.
 */
@Entity
@Getter
@Setter
@Table(name = "messages")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "message_type", discriminatorType = DiscriminatorType.STRING)
@NoArgsConstructor(force = true)
public abstract class Message {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private final String author;
	private final String subject;

	@Column(nullable = false)
	private final String body;

	@CreationTimestamp
	private LocalDateTime timestamp;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	private Message parentMessage;

	@OneToMany(mappedBy = "parentMessage", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Message> replies = new ArrayList<>();

    public Message(String author, String subject, String body) {
        this.author = author;
        this.subject = subject;
        this.body = body;
    }

	@Override
	public String toString() {
		return "Message{" +
				"Author='" + author + '\'' +
				", Subject='" + subject + '\'' +
				", Body='" + body + '\'' +
				", ID=" + id +
				'}';
	}
}
