/*
 *	Author: Vitaly Vakhteev
 *  Date: 01/22/24
 */

package VBOARD.vboard;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * Represents a Topic in a message board. A Topic is a specific type of Message that
 * is not a reply to other messages.
 */
@Entity
@DiscriminatorValue("TOPIC")
public class Topic extends Message {
	/**
	 * Default constructor for the Topic class.
	 * It creates a new Topic with empty author, subject, and body.
	 * This constructor is typically used when the specific details of the Topic are not yet available.
	 */
	public Topic() {
		super("", "", "");
	}

	/**
	 * Constructs a new Topic with specified author, subject, body, and id.
	 *
	 * @param author  The author of the topic.
	 * @param subject The subject of the topic.
	 * @param body    The body of the topic.
	 */
	public Topic(String author, String subject, String body) {
		super(author, subject, body);
	}

	/**
	 * Overrides the toString method for the Topic class.
	 *
	 * @return A string representation of the Topic object, including the author, subject, body, and ID.
	 */
	@Override
	public String toString() {
		return "Topic{" +
				"Author='" + getAuthor() + '\'' +
				", Subject='" + getSubject() + '\'' +
				", Body='" + getBody() + '\'' +
				", ID=" + getId() +
				'}';
	}
}
