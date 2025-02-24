package com.zoomly.repository;

import com.zoomly.model.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Repository class for User data access.
 * Currently implements in-memory storage, can be extended for database implementation.
 */

public class UserRepository {
    private final Map<Integer, User> users = new HashMap<>();
    //private int nextId = 1;

    public User save(User user) {
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
}