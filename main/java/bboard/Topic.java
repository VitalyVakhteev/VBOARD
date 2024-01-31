/*
 *	Author: Vitaly Vakhteev
 *  Date: 01/22/24
 */

package main.java.bboard;

/**
 * Represents a Topic in a message board. A Topic is a specific type of Message that
 * is not a reply to other messages.
 */
public class Topic extends Message {

	/**
	 * Constructs a new Topic with specified author, subject, body, and id.
	 *
	 * @param author  The author of the topic.
	 * @param subject The subject of the topic.
	 * @param body    The body of the topic.
	 * @param id      The unique identifier of the topic.
	 */
	public Topic(String author, String subject, String body, int id) {
		super(author, subject, body, id);
	}

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
