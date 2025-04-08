package com.zoomly.controllers;

import com.zoomly.model.User;
import com.zoomly.service.UserService;
import com.zoomly.service.VehicleService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

/**
 * LoginController.java
 * Controller class for managing user login.
 * Handles user authentication and scene transitions for the application.
 */
public class LoginController {
    @FXML
    private Button loginButton;
    @FXML
    private TextField emailTextField;
    @FXML
    private PasswordField passwordPasswordField;
    @FXML
    private Label statusLabel;

    private UserService userService;
    private VehicleService vehicleService;

    /**
     * Constructor for initializing the LoginController.
     * Initializes the UserService and VehicleService instances.
     */
    public LoginController() {
        this.userService = UserService.getInstance();
        this.vehicleService = VehicleService.getInstance();
    }

    /**
     * Loads the login scene into the primary stage.
     *
     * @param primaryStage The main stage for the application.
     */
    public void loadLoginScene(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Zoomly Login");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Error loading login scene.");
        }
    }

    /**
     * Handles the login action. Validates user credentials and navigates
     * to the corresponding scene based on account type (admin or user).
     */
    @FXML
    public void handleLogin() {
        String email = emailTextField.getText();
        String password = passwordPasswordField.getText();

        Optional<User> userOptional = userService.login(email, password);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            userService.setCurrentUser(user);

            boolean isAdmin = "admin".equalsIgnoreCase(user.getAccountType());

            loadScene(isAdmin ? "/fxml/Administrator.fxml" : "/fxml/User.fxml", isAdmin ? "Administrator" : "User");
        } else {
            statusLabel.setText("Invalid email or password.");
        }
    }

    /**
     * Loads the specified FXML scene and sets the window title.
     *
     * @param fxmlPath The path to the FXML file to load.
     * @param title The title to set for the window.
     */
    private void loadScene(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) loginButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(title);
        } catch (Exception e) {
            statusLabel.setText("Error loading scene: " + e.getMessage());
        }
    }

    /**
     * Handles the action of navigating to the registration screen.
     * Loads the registration FXML file and sets the scene to the registration page.
     */
    @FXML
    public void handleSignUp() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Register.fxml"));
            Parent registerPane = loader.load();

            Stage stage = (Stage) emailTextField.getScene().getWindow();
            Scene scene = new Scene(registerPane);
            stage.setScene(scene);
            stage.setTitle("Register");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Error loading registration page.");
        }
    }
}
