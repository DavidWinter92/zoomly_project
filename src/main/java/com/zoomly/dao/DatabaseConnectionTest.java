package com.zoomly.dao;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * A test class for verifying the database connection.
 *
 * This class attempts to establish a connection using DatabaseConnection.getConnection()
 * and prints the result to the console. It is used during development or setup
 * to confirm that the database is properly configured and accessible.
 */
public class DatabaseConnectionTest {

    /**
     * Entry point for running the database connection test.
     *
     * @param args Command-line arguments (not used)
     */
    public static void main(String[] args) {
        testDatabaseConnection();
    }

    /**
     * Attempts to establish a connection to the database and prints the result.
     * Prints a success message if the connection is successful,
     * otherwise prints error messages.
     */
    public static void testDatabaseConnection() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            if (connection != null) {
                System.out.println("Connection to the database was successful!");
            } else {
                System.out.println("Failed to make connection!");
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception caught: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }
}
