package com.zoomly.repository;

import com.zoomly.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * UserRepository.java
 * Singleton class for managing user data storage and retrieval.
 * Provides methods for saving, finding, deleting, and retrieving users.
 */

public class UserRepository {
    private static UserRepository instance;
    private final Map<Integer, User> users = new HashMap<>();
    private static int currentId = 1;

    private UserRepository() {}

    /**
     * method: getInstance
     * return: UserRepository
     * purpose: Returns the singleton instance of UserRepository.
     */
    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    public User save(User user) {
        Optional<User> existingUser = findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            System.out.println("Warning: User with email " + user.getEmail() + " already exists.");
            return existingUser.get();
        }

        users.put(user.getId(), user);
        return user;
    }

    public Optional<User> findById(int id) {
        return Optional.ofNullable(users.get(id));
    }

    public Optional<User> findByEmail(String email) {
        return users.values().stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public void delete(int id) {
        users.remove(id);
    }

    public void reset() {
        users.clear();
        currentId = 1;
    }

    public int getNextId() {
        return currentId++;
    }
}