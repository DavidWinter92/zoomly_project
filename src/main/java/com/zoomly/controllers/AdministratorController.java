package com.zoomly.controllers;

import com.zoomly.model.User;
import com.zoomly.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * AdministratorController.java
 * Controller class for the administrator's main menu.
 * Manages user interactions for navigating to different functionalities.
 */
public class AdministratorController extends BaseMenuController {
    @FXML
    public Button adminMenuButton;
    @FXML
    private Button manageUsersButton;
    @FXML
    private Button manageVehiclesButton;
    @FXML
    private Button manageReservationsButton;
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
     * Constructor for initializing the AdministratorController.
     * Initializes the UserService instance.
     */
    public AdministratorController() {
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
        editAccountButton.setOnAction(this::handleEditAccount);
        logoutButton.setOnAction(this::handleLogout);
    }

    /**
     * Refreshes the administrator's data by populating the text fields
     * with the current user's details.
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
     * Handles the action for navigating to the Edit Account Admin scene.
     *
     * @param event the ActionEvent triggered by the button press
     */
    @FXML
    private void handleEditAccount(ActionEvent event) {
        loadScene("/fxml/EditAccountAdmin.fxml", "Edit Account", event);
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
