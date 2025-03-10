package com.zoomly.controllers;

import com.zoomly.model.Reservation;
import com.zoomly.model.User;
import com.zoomly.model.Vehicle;
import com.zoomly.service.ReservationService;
import com.zoomly.service.UserService;
import com.zoomly.service.VehicleService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.util.List;
import java.util.Optional;

/**
 * ReservationsController.java
 * Controller class for managing user reservations in the application.
 * Provides functionalities to list, cancel, and navigate to different user-related views.
 */

public class ReservationsController extends BaseMenuController {
    @FXML
    private Button browsVehicleButton;
    @FXML
    private Button userProfileButton;
    @FXML
    private Button editAccountButton;
    @FXML
    private Button logoutButton;
    @FXML
    private Button listReservationsButton;
    @FXML
    private Button cancelReservationButton;

    @FXML
    private TextField cancelReservationIdTextField;
    @FXML
    private TextArea reservationsListTextArea;

    private UserService userService;
    private ReservationService reservationService;
    private VehicleService vehicleService;

    public ReservationsController() {
        this.userService = UserService.getInstance();
        this.reservationService = ReservationService.getInstance();
        this.vehicleService = VehicleService.getInstance();
    }

    @FXML
    private void initialize() {
        loadCurrentUserData();
        browsVehicleButton.setOnAction(this::handleBrowsVehicle);
        userProfileButton.setOnAction(this::handleUserProfile);
        editAccountButton.setOnAction(this::handleEditAccount);
        logoutButton.setOnAction(this::handleLogout);
        listReservationsButton.setOnAction(this::handleListReservations);
        cancelReservationButton.setOnAction(this::handleCancelReservation);
    }

    private void loadCurrentUserData() {
        User currentUser = userService.getCurrentUser();
    }

    @FXML
    private void handleListReservations(ActionEvent event) {
        List<Reservation> reservations = reservationService.getUserReservations(userService.getCurrentUser().getId());
        StringBuilder reservationList = new StringBuilder("Your Reservations:\n");

        for (Reservation reservation : reservations) {
            Vehicle vehicle = vehicleService.getVehicleById(reservation.getVehicleId())
                    .orElse(new Vehicle(0, "Unknown", "Unknown", 0, 0.0, 0.0));

            reservationList.append("ID: ").append(reservation.getId())
                    .append("\n")
                    .append("Model: ").append(vehicle.getModel())
                    .append(", Type: ").append(vehicle.getCarType())
                    .append(", Year: ").append(vehicle.getYear())
                    .append(", Mileage: ").append(vehicle.getMileage())
                    .append("\n")
                    .append("Pickup Date: ").append(reservation.getPickupDate())
                    .append("\n")
                    .append("Drop-off Date: ").append(reservation.getDropOffDate())
                    .append("\n")
                    .append("Total Charge: $").append(reservation.getTotalCharge())
                    .append("\n")
                    .append("\n");
        }

        reservationsListTextArea.setText(reservationList.toString());
    }

    @FXML
    private void handleCancelReservation(ActionEvent event) {
        String idText = cancelReservationIdTextField.getText();
        if (idText.isEmpty()) {
            reservationsListTextArea.setText("Please enter a reservation ID.");
            return;
        }

        try {
            int reservationId = Integer.parseInt(idText);
            Optional<Reservation> optionalReservation = reservationService.getReservationById(reservationId);

            if (optionalReservation.isPresent()) {
                reservationService.cancelReservation(reservationId);
                reservationsListTextArea.setText("Reservation " + reservationId + " canceled successfully.");
            } else {
                reservationsListTextArea.setText("There's no reservation for ID: " + reservationId);
            }
        } catch (NumberFormatException e) {
            reservationsListTextArea.setText("Invalid reservation ID.");
        } catch (Exception e) {
            reservationsListTextArea.setText("Failed to cancel reservation: " + e.getMessage());
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
    private void handleEditAccount(ActionEvent event) {
        loadScene("/fxml/EditAccountUser.fxml", "Edit Account", event);
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        loadScene("/fxml/Login.fxml", "Login", event);
    }
}