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

    /**
     * Private constructor to initialize UserService.
     * Initializes the UserDao instance.
     */
    private UserService() {
        this.userDao = new UserDao();
    }

    /**
     * Returns the singleton instance of UserService.
     * If the instance doesn't exist, creates it.
     *
     * @return The singleton instance of UserService.
     */
    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    /**
     * Attempts to log in a user with the provided email and password.
     * If login is successful, sets the currentUser and returns the user.
     *
     * @param email The email address of the user.
     * @param password The password of the user.
     * @return An Optional containing the user if login is successful, or an empty Optional if login fails.
     */
    public Optional<User> login(String email, String password) {
        Optional<User> userOptional = userDao.getUserByEmail(email);
        if (userOptional.isPresent() && userOptional.get().getPassword().equals(password)) {
            setCurrentUser(userOptional.get());
            return userOptional;
        }
        return Optional.empty();
    }

    /**
     * Registers a new user by validating the user details and adding the user to the database.
     *
     * @param firstName The first name of the user.
     * @param lastName The last name of the user.
     * @param email The email address of the user.
     * @param password The password of the user.
     * @param accountType The type of account the user is creating.
     * @return The created User object.
     */
    public User registerUser(String firstName, String lastName, String email, String password, String accountType) {
        User user = new User(firstName, lastName, email, password, accountType);
        UserValidator.validate(user);
        userDao.addUser(firstName, lastName, email, password, accountType);
        return user;
    }

    /**
     * Returns the current logged-in user.
     *
     * @return The current user.
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Sets the current user.
     *
     * @param user The user to set as the current user.
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    /**
     * Returns the UserDao instance used to interact with the database.
     *
     * @return The UserDao instance.
     */
    public UserDao getUserDao() {
        return userDao;
    }

    /**
     * Updates the email address of a user in the database.
     *
     * @param userId The ID of the user to update.
     * @param newEmail The new email address for the user.
     */
    public void updateEmail(int userId, String newEmail) {
        try {
            userDao.updateEmail(userId, newEmail);
            refreshCurrentUser(userId);
        } catch (SQLException e) {
            throw new UserServiceException("Error updating email for user ID " + userId, e);
        }
    }

    /**
     * Updates the first name of a user in the database.
     *
     * @param userId The ID of the user to update.
     * @param newFirstName The new first name for the user.
     */
    public void updateFirstName(int userId, String newFirstName) {
        try {
            userDao.updateFirstName(userId, newFirstName);
            refreshCurrentUser(userId);
        } catch (SQLException e) {
            throw new UserServiceException("Error updating first name for user ID " + userId, e);
        }
    }

    /**
     * Updates the last name of a user in the database.
     *
     * @param userId The ID of the user to update.
     * @param newLastName The new last name for the user.
     */
    public void updateLastName(int userId, String newLastName) {
        try {
            userDao.updateLastName(userId, newLastName);
            refreshCurrentUser(userId);
        } catch (SQLException e) {
            throw new UserServiceException("Error updating last name for user ID " + userId, e);
        }
    }

    /**
     * Updates the password of a user in the database.
     *
     * @param userId The ID of the user to update.
     * @param newPassword The new password for the user.
     */
    public void updatePassword(int userId, String newPassword) {
        try {
            userDao.updatePassword(userId, newPassword);
            refreshCurrentUser(userId);
        } catch (SQLException e) {
            throw new UserServiceException("Error updating password for user ID " + userId, e);
        }
    }

    /**
     * Updates all user details (email, first name, last name, password, account type) in the database.
     *
     * @param user The User object containing the updated details.
     */
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

    /**
     * Deletes a user from the database.
     * If the deleted user is the current user, it clears the current user.
     *
     * @param userId The ID of the user to delete.
     */
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

    /**
     * Checks if a given email is already registered in the database.
     *
     * @param email The email address to check.
     * @return True if the email is registered, false otherwise.
     */
    public boolean isEmailRegistered(String email) {
        List<User> users = getAllUsers();
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a list of all users in the database.
     *
     * @return A list of all users.
     */
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    /**
     * Refreshes the currentUser by fetching the user from the database.
     *
     * @param userId The ID of the user to fetch.
     */
    private void refreshCurrentUser(int userId) {
        currentUser = userDao.getUserById(userId).orElse(null);
    }
}
