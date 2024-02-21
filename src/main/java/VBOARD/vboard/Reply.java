/*
 *	Author: Vitaly Vakhteev
 *  Date: 01/22/24
 */

package VBOARD.vboard;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDateTime;

/**
 * Represents a Reply in a message board. A Reply is a specific type of Message
 * that is a response to other messages.
 */
@Entity
@DiscriminatorValue("REPLY")
public class Reply extends Message {
	/**
	 * Default constructor for the Reply class.
	 * It creates a new Reply with empty author, subject, and body.
	 * This constructor is typically used when the specific details of the Reply are not yet available.
	 */
	public Reply() {
		super();
	}

	/**
	 * Constructs a new Reply with specified author, subject, body, and id.
	 *
	 * @param author  The author of the reply.
	 * @param subject The subject of the reply.
	 * @param body    The body of the reply.
	 */
	public Reply(String author, String subject, String body) {
		super(author, subject, body);
	}

	/**
	 * Overrides the toString method for the Reply class.
	 *
	 * @return A string representation of the Reply object, including the author, subject, body, and ID.
	 */
	@Override
	public String toString() {
		return "Reply{" +
				"Author='" + getAuthor() + '\'' +
				", Subject='" + getSubject() + '\'' +
				", Body='" + getBody() + '\'' +
				", ID=" + getId() +
				'}';
	}
}
