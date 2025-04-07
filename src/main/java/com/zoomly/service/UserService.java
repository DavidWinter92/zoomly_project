package com.zoomly.service;

import com.zoomly.dao.UserDao;
import com.zoomly.model.User;
import com.zoomly.util.UserValidator;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * UserService.java
 * This class provides the business logic for user-related operations including
 * user registration, authentication, and account management.
 */

public class UserService {
    private static UserService instance;
    private final UserDao userDao;
    private User currentUser;

    private UserService() {
        this.userDao = new UserDao();
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public Optional<User> login(String email, String password) {
        Optional<User> userOptional = userDao.getUserByEmail(email);
        if (userOptional.isPresent() && userOptional.get().getPassword().equals(password)) {
            setCurrentUser(userOptional.get());
            return userOptional;
        }
        return Optional.empty();
    }

    public User registerUser(String firstName, String lastName, String email, String password, String accountType) {
        User user = new User(firstName, lastName, email, password, accountType);
        UserValidator.validate(user);
        userDao.addUser(firstName, lastName, email, password, accountType);
        return user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void updateEmail(int userId, String newEmail) {
        try {
            userDao.updateEmail(userId, newEmail);
            refreshCurrentUser(userId);
        } catch (SQLException e) {
            throw new UserServiceException("Error updating email for user ID " + userId, e);
        }
    }

    public void updateFirstName(int userId, String newFirstName) {
        try {
            userDao.updateFirstName(userId, newFirstName);
            refreshCurrentUser(userId);
        } catch (SQLException e) {
            throw new UserServiceException("Error updating first name for user ID " + userId, e);
        }
    }

    public void updateLastName(int userId, String newLastName) {
        try {
            userDao.updateLastName(userId, newLastName);
            refreshCurrentUser(userId);
        } catch (SQLException e) {
            throw new UserServiceException("Error updating last name for user ID " + userId, e);
        }
    }

    public void updatePassword(int userId, String newPassword) {
        try {
            userDao.updatePassword(userId, newPassword);
            refreshCurrentUser(userId);
        } catch (SQLException e) {
            throw new UserServiceException("Error updating password for user ID " + userId, e);
        }
    }

    // New method to update a user
    public void updateUser(User user) {
        try {
            userDao.updateEmail(user.getId(), user.getEmail());
            userDao.updateFirstName(user.getId(), user.getFirstName());
            userDao.updateLastName(user.getId(), user.getLastName());
            userDao.updatePassword(user.getId(), user.getPassword());
            userDao.updateAccountType(user.getId(), user.getAccountType());

        } catch (SQLException e) {
            throw new UserServiceException("Error updating user ID " + user.getId(), e);
        }
    }

    public void deleteUser(int userId) {
        try {
            userDao.deleteUser(userId);
            if (currentUser != null && currentUser.getId() == userId) {
                currentUser = null;
            }
        } catch (SQLException e) {
            throw new UserServiceException("Error deleting user ID " + userId, e);
        }
    }

    public boolean isEmailRegistered(String email) {
        List<User> users = getAllUsers();
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    private void refreshCurrentUser(int userId) {
        currentUser = userDao.getUserById(userId).orElse(null);
    }
}