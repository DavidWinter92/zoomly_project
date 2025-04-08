package com.zoomly.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * A utility class for managing the database connection in the Zoomly application.
 *
 * Provides static methods for setting and retrieving database connection parameters,
 * and for obtaining a Connection object using JDBC.
 *
 * This class is typically configured during application startup and used wherever
 * a database connection is needed.
 */
public class DatabaseConnection {
    private static String URL = "";
    private static String USER = "";
    private static String PASSWORD = "";

    /**
     * Sets the database connection URL.
     *
     * @param url the JDBC URL to connect to the database
     */
    public static void setUrl(String url) {
        URL = url;
    }

    /**
     * Retrieves the current database connection URL.
     *
     * @return the current JDBC URL
     */
    public static String getUrl() {
        return URL;
    }

    /**
     * Sets the database username.
     *
     * @param user the username used to connect to the database
     */
    public static void setUser(String user) {
        USER = user;
    }

    /**
     * Sets the database password.
     *
     * @param password the password used to connect to the database
     */
    public static void setPassword(String password) {
        PASSWORD = password;
    }

    /**
     * Establishes and returns a connection to the database using the configured URL, username, and password.
     *
     * @return a Connection object to the database
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
