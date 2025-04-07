package com.zoomly.dao;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * DatabaseConnectionTest.java
 * Test class for verifying the database connection.
 * Attempts to establish a connection to the database and prints the result.
 */

public class DatabaseConnectionTest {
    public static void main(String[] args) {
        testDatabaseConnection();
    }

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