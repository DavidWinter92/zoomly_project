package com.zoomly.controllers;

import com.zoomly.model.User;
import com.zoomly.repository.VehicleRepository;
import com.zoomly.service.UserService;
import com.zoomly.service.VehicleService;
import com.zoomly.util.FileLoader;
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
 * Controller class for managing user login and file uploads.
 * Handles user authentication and scene transitions for the application.
 */

public class LoginController {
    @FXML
    private TextField uploadUsersTextField;
    @FXML
    private TextField uploadVehiclesTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private Button loginButton;
    @FXML
    private Button uploadUsersButton;
    @FXML
    private TextField emailTextField;
    @FXML
    private PasswordField passwordPasswordField;
    @FXML
    private Label statusLabel;

    private UserService userService;
    private VehicleService vehicleService;

    public LoginController() {
        this.userService = UserService.getInstance();
        this.vehicleService = VehicleService.getInstance();
    }

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

    @FXML
    public void handleLogin() {
        String email = emailTextField.getText();
        String password = passwordPasswordField.getText();

        Optional<User> userOptional = userService.login(email, password);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            userService.setCurrentUser(user);

            boolean isAdmin = user.isAdmin();
            if (isAdmin) {
                loadScene("/fxml/Administrator.fxml", "Administrator", isAdmin);
            } else {
                loadScene("/fxml/User.fxml", "User", isAdmin);
            }
        } else {
            statusLabel.setText("Invalid email or password.");
        }
    }

    private void loadScene(String fxmlPath, String title, boolean isAdmin) {
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

    @FXML
    private void handleUserFileUpload() {
        String filePath = uploadUsersTextField.getText().trim();

        if (filePath.isEmpty()) {
            statusLabel.setText("Please enter a file path.");
            return;
        }

        FileLoader fileLoader = new FileLoader(userService.getUserRepository(), VehicleRepository.getInstance());

        try {
            fileLoader.loadUsers(filePath);
            statusLabel.setText("Users uploaded successfully.");
        } catch (IOException e) {
            statusLabel.setText("Error uploading users: " + e.getMessage());
        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleVehicleFileUpload() {
        String filePath = uploadVehiclesTextField.getText().trim();

        if (filePath.isEmpty()) {
            statusLabel.setText("Please enter a file path for vehicles.");
            return;
        }

        FileLoader fileLoader = new FileLoader(userService.getUserRepository(), VehicleRepository.getInstance());

        try {
            fileLoader.loadVehicles(filePath);
            statusLabel.setText("Vehicles uploaded successfully.");
        } catch (IOException e) {
            statusLabel.setText("Error uploading vehicles: " + e.getMessage());
        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
        }
    }
}