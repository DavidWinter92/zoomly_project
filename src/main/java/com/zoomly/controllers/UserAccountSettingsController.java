package com.zoomly.controllers;

import com.zoomly.model.User;
import com.zoomly.service.UserService;
import com.zoomly.util.UserValidator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * UserAccountSettingsController.java
 * Controller class for managing user account settings.
 * Provides functionalities to update user details and navigate to different user-related views.
 */

public class UserAccountSettingsController extends BaseMenuController {
    @FXML
    private Button editAccountButton;
    @FXML
    private Button logoutButton;
    @FXML
    private Button browsVehicleButton;
    @FXML
    private Button userProfileButton;
    @FXML
    private Button reservationsButton;
    @FXML
    private Button updateEmailButton;
    @FXML
    private Button updateFirstNameButton;
    @FXML
    private Button updateLastNameButton;
    @FXML
    private Button updatePasswordButton;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private TextField errorTextField;

    private UserService userService;

    public UserAccountSettingsController() {
        this.userService = UserService.getInstance();
    }

    @FXML
    private void initialize() {
        loadCurrentUserData();
        editAccountButton.setOnAction(this::handleEditAccount);
        browsVehicleButton.setOnAction(this::handleBrowsVehicle);
        userProfileButton.setOnAction(this::handleUserProfile);
        reservationsButton.setOnAction(this::handleReservations);
        logoutButton.setOnAction(this::handleLogout);

        updateEmailButton.setOnAction(this::handleUpdateEmail);
        updateFirstNameButton.setOnAction(this::handleUpdateFirstName);
        updateLastNameButton.setOnAction(this::handleUpdateLastName);
        updatePasswordButton.setOnAction(this::handleUpdatePassword);
    }

    private void loadCurrentUserData() {
        User currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            firstNameTextField.setText(currentUser.getFirstName());
            lastNameTextField.setText(currentUser.getLastName());
            emailTextField.setText(currentUser.getEmail());
        }
    }

    @FXML
    private void handleEditAccount(ActionEvent event) {
        loadScene("/fxml/EditAccount.fxml", "Edit Account", event);
    }

    @FXML
    private void handleUpdateEmail(ActionEvent event) {
        String newEmail = emailTextField.getText();
        User currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            try {
                UserValidator.validate(new User(currentUser.getId(), currentUser.getFirstName(), currentUser.getLastName(), newEmail, currentUser.getPassword(), currentUser.getAccountType()));
                userService.updateEmail(currentUser.getId(), newEmail);
                errorTextField.setText("Email changed to: " + newEmail);
                loadCurrentUserData();
            } catch (IllegalArgumentException e) {
                errorTextField.setText(e.getMessage());
            } catch (Exception e) {
                errorTextField.setText("Error updating email: " + e.getMessage());
            }
        } else {
            errorTextField.setText("No user is currently logged in.");
        }
    }

    @FXML
    private void handleUpdateFirstName(ActionEvent event) {
        String newFirstName = firstNameTextField.getText();
        User currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            try {
                UserValidator.validate(new User(currentUser.getId(), newFirstName, currentUser.getLastName(), currentUser.getEmail(), currentUser.getPassword(), currentUser.getAccountType()));
                userService.updateFirstName(currentUser.getId(), newFirstName);
                errorTextField.setText("First name changed to: " + newFirstName);
                loadCurrentUserData();
            } catch (IllegalArgumentException e) {
                errorTextField.setText(e.getMessage());
            } catch (Exception e) {
                errorTextField.setText("Error updating first name: " + e.getMessage());
            }
        } else {
            errorTextField.setText("No user is currently logged in.");
        }
    }

    @FXML
    private void handleUpdateLastName(ActionEvent event) {
        String newLastName = lastNameTextField.getText();
        User currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            try {
                UserValidator.validate(new User(currentUser.getId(), currentUser.getFirstName(), newLastName, currentUser.getEmail(), currentUser.getPassword(), currentUser.getAccountType()));
                userService.updateLastName(currentUser.getId(), newLastName);
                errorTextField.setText("Last name changed to: " + newLastName);
                loadCurrentUserData();
            } catch (IllegalArgumentException e) {
                errorTextField.setText(e.getMessage());
            } catch (Exception e) {
                errorTextField.setText("Error updating last name: " + e.getMessage());
            }
        } else {
            errorTextField.setText("No user is currently logged in.");
        }
    }

    @FXML
    private void handleUpdatePassword(ActionEvent event) {
        String newPassword = passwordTextField.getText();
        User currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            try {
                UserValidator.validate(new User(currentUser.getId(), currentUser.getFirstName(), currentUser.getLastName(), currentUser.getEmail(), newPassword, currentUser.getAccountType()));
                userService.updatePassword(currentUser.getId(), newPassword);
                errorTextField.setText("Password updated successfully.");
                loadCurrentUserData();
            } catch (IllegalArgumentException e) {
                errorTextField.setText(e.getMessage());
            } catch (Exception e) {
                errorTextField.setText("Error updating password: " + e.getMessage());
            }
        } else {
            errorTextField.setText("No user is currently logged in.");
        }
    }

    @FXML
    private void handleBrowsVehicle(ActionEvent event) {
        loadScene("/fxml/BrowsVehicles.fxml", "Browse Vehicles", event);
    }

    @FXML
    private void handleUserProfile(ActionEvent event) {
        loadScene("/fxml/User.fxml", "User Profile", event);
    }

    @FXML
    private void handleReservations(ActionEvent event) {
        loadScene("/fxml/Reservations.fxml", "Reservations", event);
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        loadScene("/fxml/Login.fxml", "Login", event);
    }
}