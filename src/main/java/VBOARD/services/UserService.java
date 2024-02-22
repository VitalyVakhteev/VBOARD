package VBOARD.services;

import VBOARD.repositories.UserRepository;
import VBOARD.vboard.User;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for handling user-related operations.
 */
@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    /**
     * Authenticate a user with the provided username and password.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @return true if authentication is successful, false otherwise.
     */
    public boolean authenticate(String username, String password) {
        logger.info("Attempting to authenticate user: " + username + " @ timestamp: " + System.currentTimeMillis());
        User user = userRepository.findByUsername(username);
        boolean isAuthenticated = user != null && BCrypt.checkpw(password, user.getHashedPwd());
        if (isAuthenticated) {
            logger.info("User: " + username + " authenticated successfully @ timestamp: " + System.currentTimeMillis());
        } else {
            logger.info("Failed to authenticate user: " + username + " @ timestamp: " + System.currentTimeMillis());
        }
        return isAuthenticated;
    }

    /**
     * Adds a new user to the system.
     *
     * @param username The username of the new user.
     * @param password The password of the new user.
     * @return true if the user is added successfully, false if the user already exists.
     */
    public boolean addUser(String username, String password) {
        logger.info("Attempting to add user: " + username + " @ timestamp: " + System.currentTimeMillis());
        if (userRepository.findByUsername(username) != null) {
            logger.info("User: " + username + " already exists @ timestamp: " + System.currentTimeMillis());
            return false;
        }
        User newUser = new User(username, password, false);
        userRepository.save(newUser);
        logger.info("User: " + username + " added successfully @ timestamp: " + System.currentTimeMillis());
        return true;
    }

    /**
     * Changes the password for an existing user.
     * Currently unused in prod.
     *
     * @param username The username of the user.
     * @param oldPassword The current password of the user.
     * @param newPassword The new password to set.
     * @return true if the password was changed successfully, false otherwise.
     */
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        logger.info("Attempting to change password for user: " + username + " @ timestamp: " + System.currentTimeMillis());
        User user = userRepository.findByUsername(username);
        if (user != null && BCrypt.checkpw(oldPassword, user.getHashedPwd())) {
            user.setHashedPwd(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
            userRepository.save(user);
            logger.info("Password changed successfully for user: " + username + " @ timestamp: " + System.currentTimeMillis());
            return true;
        }
        logger.info("Failed to change password for user: " + username + " @ timestamp: " + System.currentTimeMillis());
        return false;
    }
}
