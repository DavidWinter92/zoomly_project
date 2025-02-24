package com.zoomly.service;

import com.zoomly.model.User;
import com.zoomly.repository.UserRepository;
import com.zoomly.util.Validator;
import java.util.List;
import java.util.Optional;

/**
 * UserService.java
 * This class provides the business logic for user-related operations including
 * user registration, authentication, and account management.
 */

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(String firstName, String lastName, String email,
                             String password, String accountType) {
        // Validate input
        if (!Validator.isValidName(firstName)) {
            throw new IllegalArgumentException("Invalid first name");
        }
        if (!Validator.isValidName(lastName)) {
            throw new IllegalArgumentException("Invalid last name");
        }
        if (!Validator.isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (!Validator.isValidPassword(password)) {
            throw new IllegalArgumentException("Invalid password format");
        }
        if (!Validator.isValidAccountType(accountType)) {
            throw new IllegalArgumentException("Invalid account type");
        }

        // Check if email already exists
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }

        User user = new User(firstName, lastName, email, password, accountType);

        User savedUser = userRepository.save(user);

        return savedUser;


    }

    /**
     * method: login
     * parameters:
     *   String email - User's email address
     *   String password - User's password
     * return: Optional<User> - The user if credentials are valid, empty otherwise
     * purpose: Authenticates a user based on email and password
     */
    public Optional<User> login(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            System.out.println("Attempting login for email: " + email + " with password: " + password);
            if (user.getPassword().equals(password)) {
                return Optional.of(user);
            } else {
                System.out.println("Password mismatch for email: " + email);
            }
        } else {
            System.out.println("No user found with email: " + email);
        }
        return Optional.empty();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(int id) {
        return userRepository.findById(id);
    }

    public void deleteUser(int id) {
        userRepository.delete(id);
    }

    public User updateUser(User user) {
        if (!userRepository.findById(user.getId()).isPresent()) {
            throw new IllegalArgumentException("User not found");
        }
        return userRepository.save(user);
    }


    public void updateEmail(int userId, String newEmail) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (!Validator.isValidEmail(newEmail)) {
            throw new IllegalArgumentException("Invalid email format");
        }
        user.setEmail(newEmail);
        userRepository.save(user);
    }

    public void updateFirstName(int userId, String newFirstName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (!Validator.isValidName(newFirstName)) {
            throw new IllegalArgumentException("Invalid first name");
        }
        user.setFirstName(newFirstName);
        userRepository.save(user);
    }

    public void updateLastName(int userId, String newLastName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (!Validator.isValidName(newLastName)) {
            throw new IllegalArgumentException("Invalid last name");
        }
        user.setLastName(newLastName);
        userRepository.save(user);
    }

    public void updatePassword(int userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (!Validator.isValidPassword(newPassword)) {
            throw new IllegalArgumentException("Invalid password format");
        }
        user.setPassword(newPassword);
        userRepository.save(user);
    }

    public void updateAccountType(int userId, String newAccountType) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (!Validator.isValidAccountType(newAccountType)) {
            throw new IllegalArgumentException("Invalid account type");
        }
        user.setAccountType(newAccountType);
        userRepository.save(user);
    }
}