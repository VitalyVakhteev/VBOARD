package VBOARD.repositories;

import VBOARD.vboard.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for handling operations related to the User entity.
 * This interface extends JpaRepository, allowing for the use of JpaRepository methods in addition to custom ones.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Method to find a user by their username.
     *
     * @param username The username of the user to find.
     * @return The User entity if found, null otherwise.
     */
    User findByUsername(String username);
}