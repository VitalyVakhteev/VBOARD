/*
 *	Author: Vitaly Vakhteev
 *  Date: 01/22/24
 */

package VBOARD.vboard;

import lombok.Getter;

import java.util.ArrayList;

/**
 * Represents a message in a message board system. This class serves as a base for both topic posts and replies.
 */
public class Message {
	@Getter
	private final String author;
	@Getter
	private final String subject;
	@Getter
	private final String body;
	@Getter
	private final int id;
	private ArrayList<Message> childList;

	public Message(String author, String subject, String body, int id) {
		this.author = author;
		this.subject = subject;
		this.body = body;
		this.id = id;
		childList = new ArrayList<>();
	}

	public void print(int indentation) {
		if (author.isEmpty() && subject.isEmpty() && body.isEmpty()) {
			throw new IllegalStateException("Message is empty.");
		}

		StringBuilder indentString = new StringBuilder(" ".repeat(indentation * 2));
		System.out.println(indentString + "Message #" + id + ": \"" + subject + "\"");
		System.out.println(indentString + "From " + author + ":");
		String[] bodyLines = body.split("\n");
		for (String line : bodyLines) {
			System.out.println(indentString + "    " + line);
		}

		if (!childList.isEmpty()) {
			System.out.println(indentString + "  Replies:");
			for (int i = 0; i < childList.size(); i++) {
				childList.get(i).print(indentation + 2);

				if (i < childList.size() - 1) {
					System.out.println();
				}
			}
		}
	}

	public void addChild(Message child){
		childList.add(child);
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

	public ArrayList<Message> getChildren() {
		return childList;
	}
}
