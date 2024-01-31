/*
 *	Author: Vitaly Vakhteev
 *  Date: 01/22/24
 */

package main.java.bboard;

/**
 * Represents a Reply in a message board. A Reply is a specific type of Message
 * that is a response to other messages.
 */
public class Reply extends Message {

	/**
	 * Constructs a new Reply with specified author, subject, body, and id.
	 *
	 * @param author  The author of the reply.
	 * @param subject The subject of the reply.
	 * @param body    The body of the reply.
	 * @param id      The unique identifier of the reply.
	 */
	public Reply(String author, String subject, String body, int id) {
		super(author, subject, body, id);
	}

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
