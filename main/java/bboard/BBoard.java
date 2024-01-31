/*
 *	Author: Vitaly Vakhteev
 *  Date: 01/22/24
 */

package main.java.bboard;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Class containing most of the utility methods and logic of the main bulletin board in the message board system.
 */
public class BBoard {
	private static final int QUIT_ID = -1;
	private final Scanner scanner;
	private User currentUser;
	private ArrayList<Message> messageList;
	private ArrayList<User> userList;

	public BBoard() {
		this.currentUser = null;
		this.messageList = new ArrayList<>();
		this.userList = new ArrayList<>();
		this.scanner = new Scanner(System.in);
	}

	/**
	 * Loads users from a specified file.
	 * @param inputFile The path to the file containing user data.
	 */
	public void loadUsers(String inputFile) throws FileNotFoundException {
		try {
			File file = new File(inputFile);
			Scanner fileScanner = new Scanner(file);

			while (fileScanner.hasNextLine()) {
				String[] credentials = fileScanner.nextLine().split(" ", 2);
				if (credentials.length == 2) {
					userList.add(new User(credentials[0], credentials[1], true));
				}
			}
		} catch (FileNotFoundException e) {
			// File was deleted or moved
			System.err.println("Error: Unable to load users - " + e.getMessage());
		}
	}

	/**
	 * Loads chat history from a specified file.
	 * @param inputFile The path to the file containing chat history data.
	 */
	public void loadHistory(String inputFile) throws FileNotFoundException {
		Gson gson = new Gson();
		Type type = new TypeToken<Map<String, List<Map<String, Object>>>>() {}.getType();

		try (FileReader reader = new FileReader(inputFile)) {
			Map<String, List<Map<String, Object>>> data = gson.fromJson(reader, type);
			List<Map<String, Object>> topics = data.get("topics");

			for (Map<String, Object> topicData : topics) {
				Topic topic = parseTopic(topicData);
				messageList.add(topic);
				parseChildren(topic, topicData);
			}
		} catch (IOException e) {
			// File was deleted or moved
			System.err.println("Error: Unable to load chat messages - " + e.getMessage());
		}
	}

	/**
	 * Parses children of topics from JSON.
	 * @param parent The parent message.
	 * @param parentData A map with key string and object value.
	 */
	private void parseChildren(Message parent, Map<String, Object> parentData) {
		Object childrenObject = parentData.get("children");
		if (childrenObject instanceof List<?>) {
			@SuppressWarnings("unchecked") // Stops compiler from being annoying
			List<Map<String, Object>> children = (List<Map<String, Object>>) childrenObject;
			for (Map<String, Object> childData : children) {
				Reply reply = parseReply(childData);
				parent.addChild(reply);
				messageList.add(reply);
				parseChildren(reply, childData);
			}
		}
	}

	/**
	 * Parses topic data from JSON.
	 * @param data A map with key String and value Object.
	 */
	private Topic parseTopic(Map<String, Object> data) {
		String author = (String) data.get("from");
		String subject = (String) data.get("subject");
		String body = (String) data.get("body");
		int id = ((Double) data.get("id")).intValue();

		return new Topic(author, subject, body, id);
	}

	/**
	 * Parses reply data from JSON.
	 * @param data A map with key String and value Object.
	 */
	private Reply parseReply(Map<String, Object> data) {
		String author = (String) data.get("from");
		String subject = (String) data.get("subject");
		String body = (String) data.get("body");
		int id = ((Double) data.get("id")).intValue();

		return new Reply(author, subject, body, id);
	}

	/**
	 * Handles the login process for the bulletin board.
	 */
	private void login() {
		while (true) {
			System.out.print("Enter a username (Q to quit): ");
			String username = scanner.nextLine();
			if (username.equalsIgnoreCase("Q")) {
				return;
			}

			System.out.print("Enter a password: ");
			String password = scanner.nextLine();

			for (User user : userList) {
				if (user.check(username, password)) {
					this.currentUser = user;
					System.out.println("Logged in!");
					return;
				}
			}

			System.out.println("Invalid username or password. Try again.");
		}
	}

	/**
	 * The main method to run the bulletin board, handling user inputs and actions.
	 */
	public void run() {
		login();
		if (currentUser != null) {
			boolean isRunning = true;
			while (isRunning) {
				showMenu();
				String input = scanner.nextLine();
				switch (input.toLowerCase()) {
					case "q":
						System.out.println("Bye!");
						System.out.println("--------------------------------------------");
						isRunning = false;
						break;
					case "d":
						System.out.println("--------------------------------------------");
						displayMessages();
						break;
					case "n":
						System.out.println("--------------------------------------------");
						addTopic();
						break;
					case "r":
						System.out.println("--------------------------------------------");
						addReply();
						break;
					case "p":
						System.out.println("--------------------------------------------");
						changePassword();
						System.out.println("--------------------------------------------");
						break;
					default:
						System.out.println("Invalid input. Try again.");
				}
			}
		}
	}

	/**
	 * Displays the main menu options to the user.
	 */
	private void showMenu() {
		System.out.println("--------------------------------------------");
		System.out.println("MENU");
		System.out.println("- Display Messages (D|d)");
		System.out.println("- Add New Topic (N|n)");
		System.out.println("- Add Reply (R|r)");
		System.out.println("- Change Password (P|p)");
		System.out.println("- Quit (Q|q)");
	}

	/**
	 * Displays all messages on the bulletin board.
	 */
	private void displayMessages() {
		if (messageList.isEmpty()) {
			System.out.println("No messages to display.");
			return;
		}
		for (Message msg : messageList) {
			if (msg instanceof Topic) {
				msg.print(0);
				System.out.println();
			}
		}
	}

	/**
	 * Allows the current user to add a new topic to the bulletin board.
	 */
	private void addTopic() {
		System.out.print("Enter a subject line: ");
		String subject = scanner.nextLine();
		if (subject.isEmpty()) {
			System.out.println("Subject cannot be empty.");
			return;
		}

		System.out.print("Enter the body: ");
		String body = scanner.nextLine();
		if (body.isEmpty()) {
			System.out.println("Body cannot be empty.");
			return;
		}

		String author = currentUser.getUsername();
		int id = messageList.size() + 1;
		messageList.add(new Topic(author, subject, body, id));
		System.out.println("Message added.");
	}

	/**
	 * Allows the current user to add a reply to an existing message on the bulletin board.
	 */
	private void addReply() {
		System.out.print("Enter the id of the message (-1 to quit): ");
		int msgId = readIntFromUser();
		if (msgId == QUIT_ID) {
			System.out.println("Quitting...");
			return;
		}
		if (msgId <= 0 || msgId > messageList.size()) {
			System.out.println("Invalid id for message. Try again.");
			return;
		}

		Message parentMsg = messageList.get(msgId - 1);
		System.out.print("Enter the body of the message: ");
		String body = scanner.nextLine();
		String author = currentUser.getUsername();
		String subject = parentMsg.getSubject();
		Reply reply = new Reply(author, subject, body, messageList.size() + 1);
		parentMsg.addChild(reply);
		messageList.add(reply);
		System.out.println("Reply added.");
	}

	/**
	 * Allows the current user to change their password.
	 */
	private void changePassword() {
		System.out.println("--------------------------------------------");
		System.out.print("Enter your old password (C to cancel): ");
		String oldPass = scanner.nextLine();
		if (oldPass.equalsIgnoreCase("C")) {
			System.out.println("Cancelled setting password.");
			return;
		}

		if (!currentUser.check(currentUser.getUsername(), oldPass)) {
			System.out.println("Invalid old password.");
			return;
		}

		System.out.print("Enter a new password: ");
		String newPass = scanner.nextLine();
		if (newPass.equalsIgnoreCase("C")) {
			System.out.println("Cancelled setting password.");
			return;
		}

		if (validateNewPassword(newPass) && currentUser.setPassword(oldPass, newPass)) {
			System.out.println("Password successfully changed.");
		} else {
			System.out.println("Failed to set new password.");
		}
	}

	/**
	 * Validates the new password against certain criteria.
	 * @param newPass The new password to be validated.
	 * @return true if the password meets the criteria, false otherwise.
	 */
	private boolean validateNewPassword(String newPass) {
		// Todo: Implement validation logic (e.g., check length, complexity)
		return true;
	}

	/**
	 * Reads an integer value from the user input.
	 * @return The integer entered by the user, or QUIT_ID if the user chooses to quit.
	 */
	private int readIntFromUser() {
		while (true) {
			String input = scanner.nextLine();
			try {
				return Integer.parseInt(input);
			} catch (NumberFormatException e) {
				if (input.equalsIgnoreCase(String.valueOf(QUIT_ID))) {
					return QUIT_ID;
				}
				System.out.print("Invalid input. Please enter a number (-1 to quit): ");
			}
		}
	}
}