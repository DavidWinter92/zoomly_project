package com.zoomly.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DatabaseConnection.java
 * Utility class for establishing a connection to the MySQL database.
 * Provides a method to retrieve a database connection using JDBC.
 */

// jdbc:mysql://localhost:3300 (For my reference)

public class DatabaseConnection {
    private static String URL = "";
    private static String USER = "";
    private static String PASSWORD = "";

    public static void setUrl(String url) {
        URL = url;
    }

    public static String getUrl() {
        return URL;
    }

    public static void setUser(String user) {
        USER = user;
    }

    public static void setPassword(String password) {
        PASSWORD = password;
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}