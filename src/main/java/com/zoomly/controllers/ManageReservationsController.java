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
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * ManageReservationsController.java
 * Controller class for managing reservations in the application.
 * Provides functionalities to list, update, and cancel reservations.
 */

public class ManageReservationsController extends BaseMenuController {
    @FXML
    private Button manageReservationsButton;
    @FXML
    private Button logoutButton;
    @FXML
    private Button manageUsersButton;
    @FXML
    private Button manageVehiclesButton;
    @FXML
    private Button editAccountButton;
    @FXML
    private Button adminMenuButton;
    @FXML
    private Button listReservationsButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button cancelButton;
    @FXML
    private TextField updateReservationIdTextField;
    @FXML
    private TextField cancelReservationIdTextField;
    @FXML
    private DatePicker pickupDatePicker;
    @FXML
    private DatePicker dropOffDatePicker;
    @FXML
    private TextArea reservationsListTextArea;

    private ReservationService reservationService;
    private UserService userService;
    private VehicleService vehicleService;

    public ManageReservationsController() {
        this.reservationService = ReservationService.getInstance();
        this.userService = UserService.getInstance();
        this.vehicleService = VehicleService.getInstance();
    }

    /**
     * method: initialize
     * parameters: void
     * return: void
     * purpose: Initializes the controller and sets up date pickers.
     */
    @FXML
    private void initialize() {
        loadCurrentUserData();

        pickupDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(empty || item.isBefore(LocalDate.now().minusDays(1))); // Allow today
            }
        });

        dropOffDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(empty || item.isBefore(LocalDate.now().minusDays(1)));
            }
        });
    }

    private void loadCurrentUserData() {
        // Placeholder for potential future functionality (Phase 4)
    }

    @FXML
    private void handleListReservations(ActionEvent event) {
        List<Reservation> reservations = reservationService.getAllReservations();
        StringBuilder reservationList = new StringBuilder("Reservations:\n");
        for (Reservation reservation : reservations) {
            Optional<User> user = userService.getUserById(reservation.getUserId());
            String userEmail = user.map(User::getEmail).orElse("Unknown User");

            Optional<Vehicle> vehicle = vehicleService.getVehicleById(reservation.getVehicleId());
            String vehicleDetails = vehicle.map(v -> String.format("Vehicle ID: %d, Type: %s, Mileage: %.2f",
                    v.getId(), v.getCarType(), v.getMileage())).orElse("Unknown Vehicle");

            reservationList.append(String.format("Reservation ID: %d, User: %s\n%s\nPickup Date: %s, Drop-off Date: %s, Total Charge: %.2f\n\n",
                    reservation.getId(), userEmail, vehicleDetails,
                    reservation.getPickupDate(), reservation.getDropOffDate(), reservation.getTotalCharge()));
        }
        reservationsListTextArea.setText(reservationList.toString());
    }

    @FXML
    private void handleUpdateReservation(ActionEvent event) {
        String idText = updateReservationIdTextField.getText();
        if (idText.isEmpty()) {
            reservationsListTextArea.setText("Please enter a reservation ID.");
            return;
        }

        try {
            int reservationId = Integer.parseInt(idText);
            Optional<Reservation> optionalReservation = reservationService.getReservationById(reservationId);
            if (optionalReservation.isPresent()) {
                Reservation reservation = optionalReservation.get();

                Date newPickupDate = java.sql.Date.valueOf(pickupDatePicker.getValue());
                Date newDropOffDate = java.sql.Date.valueOf(dropOffDatePicker.getValue());

                List<Reservation> allReservations = reservationService.getAllReservations();
                boolean hasConflict = allReservations.stream()
                        .filter(r -> r.getId() != reservationId)
                        .anyMatch(r -> !(newDropOffDate.before(r.getPickupDate()) ||
                                newPickupDate.after(r.getDropOffDate())));

                if (hasConflict) {
                    reservationsListTextArea.setText("Conflict detected with another reservation.");
                    return;
                }

                reservation.setPickupDate(newPickupDate);
                reservation.setDropOffDate(newDropOffDate);
                reservationService.updateReservation(reservation);
                reservationsListTextArea.setText("Reservation updated: " + reservation);
            } else {
                reservationsListTextArea.setText("Reservation not found.");
            }
        } catch (IllegalArgumentException e) {
            reservationsListTextArea.setText("Invalid ID format. Please enter a numerical ID.");
        }
    }

    @FXML
    private void handleCancelReservation(ActionEvent event) {
        String idText = cancelReservationIdTextField.getText();
        if (idText.isEmpty()) {
            reservationsListTextArea.setText("Please enter a reservation ID to cancel.");
            return;
        }

        try {
            int reservationId = Integer.parseInt(idText);
            Optional<Reservation> optionalReservation = reservationService.getReservationById(reservationId);
            if (optionalReservation.isPresent()) {
                reservationService.cancelReservation(reservationId);
                reservationsListTextArea.setText("Reservation canceled: ID " + reservationId);
            } else {
                reservationsListTextArea.setText("There is no reservation for ID " + reservationId + ".");
            }
        } catch (NumberFormatException e) {
            reservationsListTextArea.setText("Invalid ID format. Please enter a numerical ID.");
        } catch (Exception e) {
            reservationsListTextArea.setText("Error canceling reservation: " + e.getMessage());
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
    private void handleEditAccount(ActionEvent event) {
        loadScene("/fxml/EditAccountAdmin.fxml", "Edit Account", event);
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