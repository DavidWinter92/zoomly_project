package com.zoomly.controllers;

import com.zoomly.model.Reservation;
import com.zoomly.service.ReservationService;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DoubleStringConverter;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

/**
 * ManageReservationsController.java
 * Controller class for managing vehicle reservations.
 * Handles displaying, updating, and canceling reservations, as well as managing date pickers and charge updates.
 */

public class ManageReservationsController extends BaseMenuController {

    @FXML private TableView<Reservation> reservationsTableView;
    @FXML private TableColumn<Reservation, Integer> idColumn;
    @FXML private TableColumn<Reservation, Integer> userIdColumn;
    @FXML private TableColumn<Reservation, Integer> vehicleIdColumn;
    @FXML private TableColumn<Reservation, Date> pickupDateColumn;
    @FXML private TableColumn<Reservation, Date> dropOffDate;
    @FXML private TableColumn<Reservation, Double> totalChargeColumn;

    @FXML private DatePicker pickupDatePicker;
    @FXML private DatePicker dropOffDatePicker;
    @FXML private Label errorLabel;
    @FXML private Button updateButton;
    @FXML private Button cancelButton;

    @FXML private Button manageUsersButton;
    @FXML private Button manageVehiclesButton;
    @FXML private Button manageReservationsButton;
    @FXML private Button editAccountButton;
    @FXML private Button logoutButton;
    @FXML private Button adminMenuButton;

    private final ReservationService reservationService = ReservationService.getInstance();

    @FXML
    public void initialize() {
        setupTable();
        loadReservations();
        setupDatePickers();

        reservationsTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                pickupDatePicker.setValue(newVal.getPickupDate().toLocalDate());
                dropOffDatePicker.setValue(newVal.getDropOffDate().toLocalDate());
                errorLabel.setText("");
            }
        });
    }

    private void setupTable() {
        idColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getId()));
        userIdColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getUserId()));
        vehicleIdColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getVehicleId()));
        pickupDateColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getPickupDate()));
        dropOffDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getDropOffDate()));

        totalChargeColumn.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getTotalCharge()).asObject());
        totalChargeColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        totalChargeColumn.setOnEditCommit(event -> {
            Reservation reservation = event.getRowValue();
            double newCharge = event.getNewValue();
            reservation.setTotalCharge(newCharge);
            reservationService.updateReservation(reservation);
            loadReservations();
        });

        reservationsTableView.setEditable(true);
    }

    private void setupDatePickers() {
        pickupDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(empty || item.isBefore(LocalDate.now()));
            }
        });

        dropOffDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(empty || item.isBefore(LocalDate.now()));
            }
        });
    }

    private void loadReservations() {
        List<Reservation> reservationList = reservationService.getAllReservations();
        ObservableList<Reservation> observableList = FXCollections.observableArrayList(reservationList);
        reservationsTableView.setItems(observableList);
    }

    @FXML
    private void handleUpdateReservation(ActionEvent event) {
        Reservation selected = reservationsTableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            errorLabel.setText("Please select a reservation to update.");
            return;
        }

        LocalDate newPickup = pickupDatePicker.getValue();
        LocalDate newDropOff = dropOffDatePicker.getValue();

        if (newPickup == null || newDropOff == null) {
            errorLabel.setText("Please select both pickup and drop-off dates.");
            return;
        }

        if (!newDropOff.isAfter(newPickup)) {
            errorLabel.setText("Drop-off date must be after pickup date.");
            return;
        }

        selected.setPickupDate(Date.valueOf(newPickup));
        selected.setDropOffDate(Date.valueOf(newDropOff));

        try {
            reservationService.updateReservationDates(selected.getId(), selected.getPickupDate(), selected.getDropOffDate());

            errorLabel.setText("Reservation dates updated successfully.");
            loadReservations();

        } catch (IllegalArgumentException e) {
            errorLabel.setText(e.getMessage());
        } catch (Exception e) {
            errorLabel.setText("Error updating reservation: " + e.getMessage());
        }
    }


    @FXML
    private void handleTotalChargeUpdate(ActionEvent event) {
        Reservation selected = reservationsTableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            errorLabel.setText("Please select a reservation to update.");
            return;
        }

        double newTotalCharge = selected.getTotalCharge();

        try {
            reservationService.updateTotalCharge(selected.getId(), newTotalCharge);

            errorLabel.setText("Total charge updated successfully.");
            loadReservations();

        } catch (Exception e) {
            errorLabel.setText("Error updating total charge: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancelReservation(ActionEvent event) {
        Reservation selected = reservationsTableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            errorLabel.setText("Please select a reservation to cancel.");
            return;
        }

        reservationService.cancelReservation(selected.getId());
        errorLabel.setText("Reservation canceled.");
        loadReservations();
    }

    // --- Navigation Menu ---
    @FXML private void handleManageUsers(ActionEvent event) {
        loadScene("/fxml/ManageUsers.fxml", "Manage Users", event);
    }

    @FXML private void handleManageVehicles(ActionEvent event) {
        loadScene("/fxml/ManageVehicles.fxml", "Manage Vehicles", event);
    }

    @FXML private void handleManageReservations(ActionEvent event) {
        loadScene("/fxml/ManageReservations.fxml", "Manage Reservations", event);
    }

    @FXML private void handleEditAccount(ActionEvent event) {
        loadScene("/fxml/EditAccountAdmin.fxml", "Edit Account", event);
    }

    @FXML private void handleLogout(ActionEvent event) {
        loadScene("/fxml/Login.fxml", "Login", event);
    }

    @FXML private void handleAdminMenu(ActionEvent event) {
        loadScene("/fxml/Administrator.fxml", "Admin Menu", event);
    }
}
