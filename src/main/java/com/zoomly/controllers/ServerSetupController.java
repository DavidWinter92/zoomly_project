package com.zoomly.controllers;

import com.zoomly.dao.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ServerSetupController.java
 * Controller for setting up and connecting to the database server.
 * Handles user input for database server address, username, password,
 * and uploads the database schema from a file.
 *
 * This class interacts with the DatabaseConnection utility to establish a
 * connection and handle any SQL script executions required for database setup.
 * It also includes functionality for switching the scene to the login screen
 * once the database setup is successful.
 */

public class ServerSetupController {

    @FXML
    private TextField serverAddressTextField;

    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordPasswordField;

    @FXML
    private Label connectionStatusLabel;

    @FXML
    private Button connectButton;

    @FXML
    private TextField filepathTextField;

    @FXML
    private Button uploadButton;

    private Connection connection;

    @FXML
    public void initialize() {
        filepathTextField.setDisable(true);
        uploadButton.setDisable(true);
    }

    @FXML
    private void handleConnect() {
        String serverAddress = serverAddressTextField.getText();
        String username = usernameTextField.getText();
        String password = passwordPasswordField.getText();

        DatabaseConnection.setUrl(serverAddress);
        DatabaseConnection.setUser(username);
        DatabaseConnection.setPassword(password);

        try {
            connection = DatabaseConnection.getConnection();
            connectionStatusLabel.setText("Connection Successful");

            filepathTextField.setDisable(false);
            uploadButton.setDisable(false);
        } catch (SQLException e) {
            connectionStatusLabel.setText("Connection Failed: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpload() {
        if (connection != null) {
            File scriptFile = new File(filepathTextField.getText());
            try (FileInputStream fileInputStream = new FileInputStream(scriptFile)) {
                Statement stmt = connection.createStatement();
                String script = new String(fileInputStream.readAllBytes());

                String[] sqlStatements = script.split(";");

                String databaseName = extractDatabaseName(script);

                if (databaseName == null || databaseName.isEmpty()) {
                    connectionStatusLabel.setText("Database name not found in the script.");
                    return;
                }

                for (String sqlStatement : sqlStatements) {
                    sqlStatement = sqlStatement.trim();

                    if (!sqlStatement.isEmpty()) {
                        try {
                            stmt.executeUpdate(sqlStatement);
                        } catch (SQLException e) {
                            connectionStatusLabel.setText("Failed to execute SQL: " + e.getMessage());
                            return;
                        }
                    }
                }

                String newUrl = DatabaseConnection.getUrl() + "/" + databaseName;
                DatabaseConnection.setUrl(newUrl);
                connection = DatabaseConnection.getConnection();

                connectionStatusLabel.setText("Database Created and Data Loaded Successfully");

                switchToLogin();

            } catch (IOException | SQLException e) {
                connectionStatusLabel.setText("Upload Failed: " + e.getMessage());
            }
        }
    }

    private String extractDatabaseName(String script) {
        Pattern pattern = Pattern.compile("USE `([^`]+)`;", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(script);

        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private void switchToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
            Stage stage = (Stage) connectionStatusLabel.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Zoomly Vehicle Rental - Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
