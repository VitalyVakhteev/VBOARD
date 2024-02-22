package VBOARD.controllers;

import VBOARD.services.UserService;
import VBOARD.vboard.LoginRequest;
import VBOARD.vboard.RegisterRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * Controller class for handling user-related HTTP requests.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {
    // Logger instance for this class
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    // UserService instance, injected by Spring
    @Autowired
    private UserService userService;

    /**
     * Endpoint to authenticate a user.
     *
     * @param loginRequest The login request containing the username and password.
     * @return A ResponseEntity containing a success message if authentication is successful, or an unauthorized status if it fails.
     */
    @PostMapping("/login")
    @CrossOrigin
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        boolean isAuthenticated = userService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        if (isAuthenticated) {
            logger.info("User authenticated: " + loginRequest.getUsername() + " @ timestamp: " + System.currentTimeMillis());
            return ResponseEntity.ok().body("User authenticated");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    /**
     * Endpoint to register a new user.
     *
     * @param registerRequest The register request containing the username, password, and password confirmation.
     * @return A ResponseEntity containing a success message if registration is successful, or a bad request status if it fails.
     * @throws IOException if an I/O error occurs.
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) throws IOException {
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("Passwords do not match");
        }
        boolean userAdded = userService.addUser(registerRequest.getUsername(), registerRequest.getPassword());
        if (userAdded) {
            logger.info("User registered: " + registerRequest.getUsername() + " @ timestamp: " + System.currentTimeMillis());
            return ResponseEntity.ok().body("User registered successfully");
        } else {
            return ResponseEntity.badRequest().body("Username already exists");
        }
    }
}