// Todo: Write full docs
package VBOARD.services;

import VBOARD.repositories.UserRepository;
import VBOARD.vboard.User;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
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
        User user = userRepository.findByUsername(username);
        return user != null && BCrypt.checkpw(password, user.getHashedPwd());
    }

    /**
     * Adds a new user to the system.
     *
     * @param username The username of the new user.
     * @param password The password of the new user.
     * @return true if the user is added successfully, false if the user already exists.
     */
    public boolean addUser(String username, String password) {
        if (userRepository.findByUsername(username) != null) {
            return false;   // User already exists
        }
        User newUser = new User(username, password, false);
        userRepository.save(newUser);
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
        User user = userRepository.findByUsername(username);
        if (user != null && BCrypt.checkpw(oldPassword, user.getHashedPwd())) {
            user.setHashedPwd(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
            userRepository.save(user);
            return true;
        }
        return false;
    }
}
