/*
 *	Author: Vitaly Vakhteev
 *  Date: 01/22/24
 */

package main.java.bboard;

import java.util.ArrayList;

/**
 * Represents a message in a message board system. This class serves as a base for both topic posts and replies.
 */
public class Message {
	private final String author;
	private final String subject;
	private final String body;
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

	public String getAuthor() {
		return author;
	}
	public String getBody() {
		return body;
	}
	public String getSubject() {
		return subject;
	}
	public int getId() {
		return id;
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
}
