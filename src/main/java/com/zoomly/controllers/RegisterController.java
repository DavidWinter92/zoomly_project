package com.zoomly.controllers;

import com.zoomly.model.User;
import com.zoomly.service.UserService;
import com.zoomly.util.UserValidator;
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

    /**
     * Initializes the RegisterController by setting up the UserService instance.
     */
    public RegisterController() {
        this.userService = UserService.getInstance();
    }

    /**
     * Handles the user registration process.
     * Validates the input fields, checks if the email is already registered,
     * and registers a new user if all checks pass.
     * Displays appropriate status messages based on the registration outcome.
     */
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

        try {
            if (userService.isEmailRegistered(email)) {
                statusLabel.setText("Error: Email is already registered.");
                return;
            }

            User newUser = new User(0, firstName, lastName, email, password, accountType);

            UserValidator.validate(newUser);

            userService.registerUser(firstName, lastName, email, password, accountType);
            statusLabel.setText("Registration successful!");
            clearFields();
        } catch (IllegalArgumentException e) {
            statusLabel.setText(e.getMessage());
        } catch (Exception e) {
            statusLabel.setText("Error registering user: " + e.getMessage());
        }
    }

    /**
     * Clears all input fields in the registration form.
     */
    private void clearFields() {
        lastNameTextField.clear();
        firstNameTextField.clear();
        emailTextField.clear();
        passwordPasswordField.clear();
    }

    /**
     * Handles the action for navigating to the login screen.
     * Loads the login FXML file and sets the scene to the login screen.
     */
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
