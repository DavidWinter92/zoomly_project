package com.zoomly.dao;

import com.zoomly.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * UserDao.java
 * Data Access Object (DAO) for interacting with the users table in the database.
 * Provides methods for adding, retrieving, updating, and deleting users.
 */
public class UserDao {

    /**
     * Adds a new user to the database.
     *
     * @param firstName user's first name
     * @param lastName user's last name
     * @param email user's email
     * @param password user's password
     * @param accountType user's account type
     */
    public void addUser(String firstName, String lastName, String email, String password, String accountType) {
        String sql = "INSERT INTO users (first_name, last_name, email, password, account_type) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, email);
            statement.setString(4, password);
            statement.setString(5, accountType);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves a user by email.
     *
     * @param email email address to search
     * @return optional user if found
     */
    public Optional<User> getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        User user = null;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getString("account_type")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(user);
    }

    /**
     * Updates the email of a user.
     *
     * @param userId user's ID
     * @param newEmail new email to set
     * @throws SQLException if update fails
     */
    public void updateEmail(int userId, String newEmail) throws SQLException {
        String sql = "UPDATE users SET email = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, newEmail);
            statement.setInt(2, userId);
            statement.executeUpdate();
        }
    }

    /**
     * Updates the first name of a user.
     *
     * @param userId user's ID
     * @param newFirstName new first name to set
     * @throws SQLException if update fails
     */
    public void updateFirstName(int userId, String newFirstName) throws SQLException {
        String sql = "UPDATE users SET first_name = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, newFirstName);
            statement.setInt(2, userId);
            statement.executeUpdate();
        }
    }

    /**
     * Updates the last name of a user.
     *
     * @param userId user's ID
     * @param newLastName new last name to set
     * @throws SQLException if update fails
     */
    public void updateLastName(int userId, String newLastName) throws SQLException {
        String sql = "UPDATE users SET last_name = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, newLastName);
            statement.setInt(2, userId);
            statement.executeUpdate();
        }
    }

    /**
     * Updates the password of a user.
     *
     * @param userId user's ID
     * @param newPassword new password to set
     * @throws SQLException if update fails
     */
    public void updatePassword(int userId, String newPassword) throws SQLException {
        String sql = "UPDATE users SET password = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, newPassword);
            statement.setInt(2, userId);
            statement.executeUpdate();
        }
    }

    /**
     * Updates the account type of a user.
     *
     * @param userId user's ID
     * @param accountType new account type to set
     * @throws SQLException if update fails
     */
    public void updateAccountType(int userId, String accountType) throws SQLException {
        String sql = "UPDATE users SET account_type = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, accountType);
            statement.setInt(2, userId);
            statement.executeUpdate();
        }
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param userId user's ID
     * @return optional user if found
     */
    public Optional<User> getUserById(int userId) {
        String sql = "SELECT * FROM users WHERE id = ?";
        User user = null;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getString("account_type")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(user);
    }

    /**
     * Retrieves all users from the database.
     *
     * @return list of all users
     */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                User user = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getString("account_type")
                );
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    /**
     * Deletes a user from the database.
     *
     * @param userId user's ID
     * @throws SQLException if deletion fails
     */
    public void deleteUser(int userId) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.executeUpdate();
        }
    }
}
