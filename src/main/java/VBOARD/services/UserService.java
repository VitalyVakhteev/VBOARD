package VBOARD.services;

import jakarta.annotation.PostConstruct;
import VBOARD.vboard.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Service
public class UserService {
    private final List<User> users = new ArrayList<>();
    private final Path usersFilePath;

    public UserService(@Value("${vboard.userfile.path}") String usersFilePath) {
        this.usersFilePath = Paths.get(usersFilePath.replace("file:", ""));
    }

    @PostConstruct
    public void init() {
        try {
            loadUsers();
        } catch (IOException e) {
            System.err.println("Error: Unable to load users - " + e.getMessage());
        }
    }

    private void loadUsers() throws IOException {
        if (!Files.exists(usersFilePath))
            return;
        try {
            List<String> lines = Files.readAllLines(usersFilePath);
            lines.forEach(line -> {
                String[] credentials = line.split(" ", 2);
                if (credentials.length == 2) {
                    users.add(new User(credentials[0], credentials[1], true));
                }
            });
        } catch (IOException e) {
            System.err.println("Error: Unable to load users - " + e.getMessage());
        }
    }

    /**
     * Authenticate a user with the provided username and password.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @return true if authentication is successful, false otherwise.
     */
    public boolean authenticate(String username, String password) {
        Optional<User> foundUser = users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();

        return foundUser.isPresent() && foundUser.get().check(username, password);
    }

    /**
     * Adds a new user to the system.
     *
     * @param username The username of the new user.
     * @param password The password of the new user.
     * @return true if the user is added successfully, false if the user already exists.
     */
    public boolean addUser(String username, String password) {
        if(users.stream().anyMatch(user -> user.getUsername().equals(username))) {
            return false; // User already exists
        }
        users.add(new User(username, password, false));
        return true;
    }

    /**
     * Changes the password for an existing user.
     *
     * @param username The username of the user.
     * @param oldPassword The current password of the user.
     * @param newPassword The new password to set.
     * @return true if the password was changed successfully, false otherwise.
     */
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        Optional<User> foundUser = users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();

        return foundUser.map(user -> user.setPassword(oldPassword, newPassword)).orElse(false);
    }
}
