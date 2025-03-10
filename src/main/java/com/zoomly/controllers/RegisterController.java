package com.zoomly.controllers;

import com.zoomly.model.User;
import com.zoomly.service.UserService;
import com.zoomly.util.Validator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * RegisterController.java
 * Controller class for handling user registration.
 * Provides functionalities to register a new user and navigate to the login screen.
 */

public class RegisterController {
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private PasswordField passwordPasswordField;
    @FXML
    private Label statusLabel;
    @FXML
    private Button registerButton;
    @FXML
    private Button signInButton;

    private UserService userService;

    public RegisterController() {
        this.userService = UserService.getInstance();
    }

    @FXML
    public void handleRegister() {
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String email = emailTextField.getText();
        String password = passwordPasswordField.getText();

        String accountType = "user";

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Please fill in all fields.");
            return;
        }

        if (!Validator.isValidEmail(email)) {
            statusLabel.setText("Invalid email format.");
            return;
        }

        try {
            User newUser = userService.registerUser(firstName, lastName, email, password, accountType);
            statusLabel.setText("Registration successful!");
            clearFields();
        } catch (Exception e) {
            statusLabel.setText("Error registering user: " + e.getMessage());
        }
    }

    private void clearFields() {
        lastNameTextField.clear();
        firstNameTextField.clear();
        emailTextField.clear();
        passwordPasswordField.clear();
    }

    @FXML
    public void handleSignIn() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
            Parent loginRoot = loader.load();

            Stage stage = (Stage) signInButton.getScene().getWindow();
            Scene loginScene = new Scene(loginRoot);

            stage.setScene(loginScene);
            stage.setTitle("Login");
        } catch (Exception e) {
            statusLabel.setText("Error loading login screen: " + e.getMessage());
        }
    }
}