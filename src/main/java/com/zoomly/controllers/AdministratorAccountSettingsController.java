package com.zoomly.controllers;

import com.zoomly.model.User;
import com.zoomly.service.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

/**
 * AdministratorAccountSettingsController.java
 * Controller class for managing administrator account settings.
 * Handles user interface interactions for updating user details.
 */

public class AdministratorAccountSettingsController extends BaseMenuController {
    @FXML
    private Button logoutButton;
    @FXML
    private Button manageUsersButton;
    @FXML
    private Button manageVehiclesButton;
    @FXML
    private Button manageReservationsButton;
    @FXML
    private Button adminMenuButton;
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

    public AdministratorAccountSettingsController() {
        this.userService = UserService.getInstance();
    }

    @FXML
    private void initialize() {
        loadCurrentUserData();
        manageUsersButton.setOnAction(this::handleManageUsers);
        manageVehiclesButton.setOnAction(this::handleManageVehicles);
        manageReservationsButton.setOnAction(this::handleManageReservations);
        adminMenuButton.setOnAction(this::handleAdminMenu);
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
    private void handleUpdateEmail(ActionEvent event) {
        String newEmail = emailTextField.getText();
        User currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            try {
                userService.updateEmail(currentUser.getId(), newEmail);
                errorTextField.setText("");
                System.out.println("Email updated successfully.");
                loadCurrentUserData();
            } catch (Exception e) {
                System.out.println("Caught an exception: " + e.getMessage());
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
                userService.updateFirstName(currentUser.getId(), newFirstName);
                errorTextField.setText("");
                System.out.println("First name updated successfully.");
                loadCurrentUserData();
            } catch (Exception e) {
                System.out.println("Caught an exception: " + e.getMessage());
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
                userService.updateLastName(currentUser.getId(), newLastName);
                errorTextField.setText("");
                System.out.println("Last name updated successfully.");
                loadCurrentUserData();
            } catch (Exception e) {
                System.out.println("Caught an exception: " + e.getMessage());
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
                userService.updatePassword(currentUser.getId(), newPassword);
                errorTextField.setText("");
                System.out.println("Password updated successfully.");
                loadCurrentUserData();
            } catch (Exception e) {
                System.out.println("Caught an exception: " + e.getMessage());
                errorTextField.setText("Error updating password: " + e.getMessage());
            }
        } else {
            errorTextField.setText("No user is currently logged in.");
        }
    }

    @FXML
    private void handleManageUsers(ActionEvent event) {
        loadScene("/fxml/ManageUsers.fxml", "Manage Users", event);
    }

    @FXML
    private void handleManageVehicles(ActionEvent event) {
        loadScene("/fxml/ManageVehicles.fxml", "Manage Vehicles", event);
    }

    @FXML
    private void handleManageReservations(ActionEvent event) {
        loadScene("/fxml/ManageReservations.fxml", "Manage Reservations", event);
    }

    @FXML
    private void handleAdminMenu(ActionEvent event) {
        loadScene("/fxml/Administrator.fxml", "Admin Menu", event);
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        loadScene("/fxml/Login.fxml", "Login", event);
    }
}