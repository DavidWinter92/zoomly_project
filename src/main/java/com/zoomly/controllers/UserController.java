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

    public UserController() {
        this.userService = UserService.getInstance();
    }

    @FXML
    private void initialize() {
        refreshData();
        browsVehicleButton.setOnAction(this::handleBrowsVehicle);
        reservationsButton.setOnAction(this::handleReservations);
        editAccountButton.setOnAction(this::handleEditAccount);
        logoutButton.setOnAction(this::handleLogout);
    }

    /**
     * method: loadCurrentUserData
     * parameters: void
     * return: void
     * purpose: Loads the current user data and populates the text fields.
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

    @FXML
    private void handleBrowsVehicle(ActionEvent event) {
        loadScene("/fxml/BrowsVehicles.fxml", "Browse Vehicles", event);
    }

    @FXML
    private void handleReservations(ActionEvent event) {
        loadScene("/fxml/Reservations.fxml", "Reservations", event);
    }

    @FXML
    private void handleEditAccount(ActionEvent event) {
        loadScene("/fxml/EditAccountUser.fxml", "Edit Account", event);
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        loadScene("/fxml/Login.fxml", "Login", event);
    }
}