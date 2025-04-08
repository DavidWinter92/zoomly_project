package com.zoomly.controllers;

import com.zoomly.model.User;
import com.zoomly.service.UserService;
import com.zoomly.util.UserValidator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

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

    /**
     * Constructor for initializing the AdministratorAccountSettingsController.
     * Initializes the UserService instance.
     */
    public AdministratorAccountSettingsController() {
        this.userService = UserService.getInstance();
    }

    /**
     * Initializes the controller by setting up the button actions
     * and refreshing the administrator's account data.
     */
    @FXML
    private void initialize() {
        refreshData();
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

    /**
     * Refreshes the administrator's account data by loading the current user details.
     */
    @Override
    protected void refreshData() {
        User currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            firstNameTextField.setText(currentUser.getFirstName());
            lastNameTextField.setText(currentUser.getLastName());
            emailTextField.setText(currentUser.getEmail());
        }
    }

    /**
     * Handles the action of updating the email address of the administrator.
     * Validates the new email and updates it if valid.
     *
     * @param event The action event that triggered this method.
     */
    @FXML
    private void handleUpdateEmail(ActionEvent event) {
        String newEmail = emailTextField.getText();
        User currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            try {
                UserValidator.validate(new User(currentUser.getId(), currentUser.getFirstName(), currentUser.getLastName(), newEmail, currentUser.getPassword(), currentUser.getAccountType()));
                userService.updateEmail(currentUser.getId(), newEmail);
                errorTextField.setText("");
                System.out.println("Email updated successfully to: " + newEmail);
                errorTextField.setText("Email changed to: " + newEmail);
                refreshData();
            } catch (IllegalArgumentException e) {
                errorTextField.setText(e.getMessage());
            } catch (Exception e) {
                System.out.println("Caught an exception: " + e.getMessage());
                errorTextField.setText("Error updating email: " + e.getMessage());
            }
        } else {
            errorTextField.setText("No user is currently logged in.");
        }
    }

    /**
     * Handles the action of updating the first name of the administrator.
     * Validates the new first name and updates it if valid.
     *
     * @param event The action event that triggered this method.
     */
    @FXML
    private void handleUpdateFirstName(ActionEvent event) {
        String newFirstName = firstNameTextField.getText();
        User currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            try {
                UserValidator.validate(new User(currentUser.getId(), newFirstName, currentUser.getLastName(), currentUser.getEmail(), currentUser.getPassword(), currentUser.getAccountType()));
                userService.updateFirstName(currentUser.getId(), newFirstName);
                errorTextField.setText("");
                System.out.println("First name updated successfully to: " + newFirstName);
                errorTextField.setText("First name changed to: " + newFirstName);
                refreshData();
            } catch (IllegalArgumentException e) {
                errorTextField.setText(e.getMessage());
            } catch (Exception e) {
                System.out.println("Caught an exception: " + e.getMessage());
                errorTextField.setText("Error updating first name: " + e.getMessage());
            }
        } else {
            errorTextField.setText("No user is currently logged in.");
        }
    }

    /**
     * Handles the action of updating the last name of the administrator.
     * Validates the new last name and updates it if valid.
     *
     * @param event The action event that triggered this method.
     */
    @FXML
    private void handleUpdateLastName(ActionEvent event) {
        String newLastName = lastNameTextField.getText();
        User currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            try {
                UserValidator.validate(new User(currentUser.getId(), currentUser.getFirstName(), newLastName, currentUser.getEmail(), currentUser.getPassword(), currentUser.getAccountType()));
                userService.updateLastName(currentUser.getId(), newLastName);
                errorTextField.setText("");
                System.out.println("Last name updated successfully to: " + newLastName);
                errorTextField.setText("Last name changed to: " + newLastName);
                refreshData();
            } catch (IllegalArgumentException e) {
                errorTextField.setText(e.getMessage());
            } catch (Exception e) {
                System.out.println("Caught an exception: " + e.getMessage());
                errorTextField.setText("Error updating last name: " + e.getMessage());
            }
        } else {
            errorTextField.setText("No user is currently logged in.");
        }
    }

    /**
     * Handles the action of updating the password of the administrator.
     * Validates the new password and updates it if valid.
     *
     * @param event The action event that triggered this method.
     */
    @FXML
    private void handleUpdatePassword(ActionEvent event) {
        String newPassword = passwordTextField.getText();
        User currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            try {
                UserValidator.validate(new User(currentUser.getId(), currentUser.getFirstName(), currentUser.getLastName(), currentUser.getEmail(), newPassword, currentUser.getAccountType()));
                userService.updatePassword(currentUser.getId(), newPassword);
                errorTextField.setText("");
                System.out.println("Password updated successfully.");
                errorTextField.setText("Password updated successfully.");
                refreshData();
            } catch (IllegalArgumentException e) {
                errorTextField.setText(e.getMessage());
            } catch (Exception e) {
                System.out.println("Caught an exception: " + e.getMessage());
                errorTextField.setText("Error updating password: " + e.getMessage());
            }
        } else {
            errorTextField.setText("No user is currently logged in.");
        }
    }

    /**
     * Handles the action for navigating to the Manage Users scene.
     *
     * @param event the ActionEvent triggered by the button press
     */
    @FXML
    private void handleManageUsers(ActionEvent event) {
        loadScene("/fxml/ManageUsers.fxml", "Manage Users", event);
    }

    /**
     * Handles the action for navigating to the Manage Vehicles screen.
     *
     * @param event the ActionEvent triggered by the button press
     */
    @FXML
    private void handleManageVehicles(ActionEvent event) {
        loadScene("/fxml/ManageVehicles.fxml", "Manage Vehicles", event);
    }

    /**
     * Handles the action for navigating to the Manage Reservations scene.
     *
     * @param event the ActionEvent triggered by the button press
     */
    @FXML
    private void handleManageReservations(ActionEvent event) {
        loadScene("/fxml/ManageReservations.fxml", "Manage Reservations", event);
    }

    /**
     * Handles the action for navigating to the Admin Menu scene.
     *
     * @param event the ActionEvent triggered by the button press
     */
    @FXML
    private void handleAdminMenu(ActionEvent event) {
        loadScene("/fxml/Administrator.fxml", "Admin Menu", event);
    }

    /**
     * Handles the action for logging out and navigating to the Login scene.
     *
     * @param event the ActionEvent triggered by the button press
     */
    @FXML
    private void handleLogout(ActionEvent event) {
        loadScene("/fxml/Login.fxml", "Login", event);
    }
}
