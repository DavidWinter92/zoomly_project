package com.zoomly.controllers;

import com.zoomly.model.Reservation;
import com.zoomly.model.User;
import com.zoomly.model.Vehicle;
import com.zoomly.service.ReservationService;
import com.zoomly.service.UserService;
import com.zoomly.service.VehicleService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;
import java.util.List;

/**
 * BrowsVehiclesController.java
 * Controller class for browsing available vehicles and making reservations.
 * Manages user interactions for vehicle listing and reservation.
 */

public class BrowsVehiclesController extends BaseMenuController {
    @FXML
    private Button userProfileButton;
    @FXML
    private Button reservationsButton;
    @FXML
    private Button editAccountButton;
    @FXML
    private Button logoutButton;
    @FXML
    private Button listVehiclesButton;
    @FXML
    private Button reserveButton;
    @FXML
    private TextField reserveIdTextField;
    @FXML
    private DatePicker pickupDatePicker;
    @FXML
    private DatePicker dropOffDatePicker;
    @FXML
    private TextArea vehiclesListTextArea;

    private UserService userService;
    private VehicleService vehicleService;
    private ReservationService reservationService;

    public BrowsVehiclesController() {
        this.userService = UserService.getInstance();
        this.vehicleService = VehicleService.getInstance();
        this.reservationService = ReservationService.getInstance();
    }

    /**
     * method: initialize
     * parameters: void
     * return: void
     * purpose: Initializes the controller and sets up event handlers for buttons and date pickers.
     */
    @FXML
    private void initialize() {
        loadCurrentUserData();
        userProfileButton.setOnAction(this::handleUserProfile);
        reservationsButton.setOnAction(this::handleReservations);
        editAccountButton.setOnAction(this::handleEditAccount);
        logoutButton.setOnAction(this::handleLogout);
        listVehiclesButton.setOnAction(this::handleListVehicles);
        reserveButton.setOnAction(this::handleReserveVehicleButton);

        pickupDatePicker.setValue(LocalDate.now());
        pickupDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(empty || item.isBefore(LocalDate.now()));
            }
        });

        dropOffDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (pickupDatePicker.getValue() != null) {
                    setDisable(empty || item.isBefore(pickupDatePicker.getValue()));
                } else {
                    setDisable(empty || item.isBefore(LocalDate.now()));
                }
            }
        });
    }

    private void loadCurrentUserData() {
        User currentUser = userService.getCurrentUser();
    }

    @FXML
    private void handleListVehicles(ActionEvent event) {
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        StringBuilder vehicleList = new StringBuilder("Available Vehicles:\n");

        for (Vehicle vehicle : vehicles) {
            vehicleList.append("ID: ").append(vehicle.getId())
                    .append(", Type: ").append(vehicle.getCarType())
                    .append(", Model: ").append(vehicle.getModel())
                    .append(", Year: ").append(vehicle.getYear())
                    .append(", Price per Day: ").append(vehicle.getPricePerDay())
                    .append("\n");
        }

        vehiclesListTextArea.setText(vehicleList.toString());
    }

    @FXML
    private void handleReserveVehicleButton(ActionEvent event) {
        int vehicleId;
        try {
            vehicleId = Integer.parseInt(reserveIdTextField.getText());
        } catch (NumberFormatException e) {
            vehiclesListTextArea.setText("Invalid vehicle ID.");
            return;
        }

        if (pickupDatePicker.getValue() == null || dropOffDatePicker.getValue() == null) {
            vehiclesListTextArea.setText("Please select both pickup and drop-off dates.");
            return;
        }

        Reservation reservation;
        try {
            reservation = reservationService.createReservation(
                    userService.getCurrentUser().getId(),
                    vehicleId,
                    java.sql.Date.valueOf(pickupDatePicker.getValue()),
                    java.sql.Date.valueOf(dropOffDatePicker.getValue())
            );
            vehiclesListTextArea.setText("Reservation successful! ID: " + reservation.getId());
        } catch (IllegalArgumentException e) {
            vehiclesListTextArea.setText(e.getMessage());
        }
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
    private void handleEditAccount(ActionEvent event) {
        loadScene("/fxml/EditAccountUser.fxml", "Edit Account", event);
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        loadScene("/fxml/Login.fxml", "Login", event);
    }
}