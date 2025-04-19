package com.zoomly.controllers;

import com.zoomly.model.User;
import com.zoomly.service.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

/**
 * UserController.java
 * Controller class for managing user profile information and navigation.
 * Provides functionalities to view user details and navigate to other scenes.
 */
public class UserController extends BaseMenuController {

    @FXML
    private Button browsVehicleButton;
    @FXML
    private Button reservationsButton;
    @FXML
    private Button editAccountButton;
    @FXML
    private Button logoutButton;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField emailTextField;

    private UserService userService;

    /**
     * Initializes the UserController by setting up the UserService instance.
     */
    public UserController() {
        this.userService = UserService.getInstance();
    }

    /**
     * Initializes the controller by setting up event handlers for buttons and refreshing user data.
     */
    @FXML
    private void initialize() {
        refreshData();
        browsVehicleButton.setOnAction(this::handleBrowsVehicles);
        reservationsButton.setOnAction(this::handleReservations);
        editAccountButton.setOnAction(this::handleEditAccount);
        logoutButton.setOnAction(this::handleLogout);
    }

    /**
     * Loads the current user data and populates the text fields.
     * If no user is logged in, it outputs a message or could navigate to the login scene.
     */
    @Override
    protected void refreshData() {
        User currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            firstNameTextField.setText(currentUser.getFirstName());
            lastNameTextField.setText(currentUser.getLastName());
            emailTextField.setText(currentUser.getEmail());
        } else {
            System.out.println("No user is currently logged in.");
        }
    }

    /**
     * Handles the action to navigate to the Browse Vehicles view.
     *
     * @param event The event triggered by the user action
     */
    @FXML
    private void handleBrowsVehicles(ActionEvent event) {
        loadScene("/fxml/BrowsVehicles.fxml", "Browse Vehicles", event);
    }

    /**
     * Handles the action for navigating to the Reservations.fxml scene.
     *
     * @param event the ActionEvent triggered by the button press
     */
    @FXML
    private void handleReservations(ActionEvent event) {
        loadScene("/fxml/Reservations.fxml", "Reservations", event);
    }
    /**
     * Handles the action for navigating to the Edit Account scene.
     *
     * @param event the ActionEvent triggered by the button press
     */
    @FXML
    private void handleEditAccount(ActionEvent event) {
        loadScene("/fxml/EditAccountUser.fxml", "Edit Account", event);
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
