package com.zoomly.controllers;

import com.zoomly.model.Vehicle;
import com.zoomly.service.VehicleService;
import com.zoomly.util.FileLoader;
import com.zoomly.util.Validator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.time.Year;
import java.util.List;
import java.util.Optional;

/**
 * ManageVehiclesController.java
 * Controller class for managing vehicle operations in the application.
 * Provides functionalities to add, update, delete, and list vehicles.
 */

public class ManageVehiclesController extends BaseMenuController {
    @FXML
    public Button manageUsersButton;
    @FXML
    public Button adminMenuButton;
    @FXML
    public Button logoutButton;
    @FXML
    public Button editAccountButton;
    @FXML
    public Button manageReservationsButton;
    @FXML
    public Button manageVehiclesButton;
    @FXML
    private Button listVehiclesButton;
    @FXML
    private Button updateVehicleButton;
    @FXML
    private Button addVehicleButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button uploadButton;
    @FXML
    private TextField vehicleIdTextField;
    @FXML
    private TextField typeTextField;
    @FXML
    private TextField modelTextField;
    @FXML
    private TextField yearTextField;
    @FXML
    private TextField mileageTextField;
    @FXML
    private TextField priceTextField;
    @FXML
    private TextField deleteVehicleIdTextField;
    @FXML
    private TextField uploadVehiclesTextField;
    @FXML
    private TextArea vehiclesListTextArea;

    private VehicleService vehicleService;

    public ManageVehiclesController() {
        this.vehicleService = VehicleService.getInstance();
    }

    @FXML
    private void initialize() {
        // Placeholder for logic initialization (Phase 4)
    }

    @FXML
    private void handleListVehicles(ActionEvent event) {
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        StringBuilder vehicleList = new StringBuilder("Vehicles:\n");
        for (Vehicle vehicle : vehicles) {
            vehicleList.append(vehicle.toString()).append("\n");
        }
        vehiclesListTextArea.setText(vehicleList.toString());
    }

    @FXML
    private void handleUpdateVehicle(ActionEvent event) {
        String idText = vehicleIdTextField.getText();
        if (idText.isEmpty()) {
            vehiclesListTextArea.setText("Please enter a vehicle ID to update.");
            return;
        }

        try {
            int vehicleId = Integer.parseInt(idText);
            Optional<Vehicle> optionalVehicle = vehicleService.getVehicleById(vehicleId);
            if (optionalVehicle.isPresent()) {
                Vehicle vehicle = optionalVehicle.get();
                validateAndUpdateVehicle(vehicle);
            } else {
                vehiclesListTextArea.setText("Vehicle not found.");
            }
        } catch (NumberFormatException e) {
            vehiclesListTextArea.setText("Invalid ID format.");
        }
    }

    @FXML
    private void handleAddVehicle(ActionEvent event) {
        String idText = vehicleIdTextField.getText();
        if (!idText.isEmpty()) {
            vehiclesListTextArea.setText("ID field must be empty to add a vehicle.");
            return;
        }

        String carType = typeTextField.getText();
        String model = modelTextField.getText();
        String yearText = yearTextField.getText();
        String mileageText = mileageTextField.getText();
        String priceText = priceTextField.getText();

        StringBuilder errorMessages = new StringBuilder();

        if (!Validator.isValidCarType(carType)) {
            errorMessages.append("Car type '").append(carType).append("' is not valid: Valid car types are SUV, Sedan, or Truck.\n");
        }

        int year = 0;
        if (yearText.isEmpty() || !yearText.matches("\\d+") || (year = Integer.parseInt(yearText)) < 1800 || year > Year.now().getValue()) {
            errorMessages.append("Year must be a number between 1800 and ").append(Year.now().getValue()).append(".\n");
        }

        double mileage = 0;
        if (mileageText.isEmpty() || !mileageText.matches("\\d+(\\.\\d+)?")) {
            errorMessages.append("Mileage must be a valid number.\n");
        } else {
            mileage = Double.parseDouble(mileageText);
        }

        double pricePerDay = 0;
        if (priceText.isEmpty() || !priceText.matches("\\d+(\\.\\d+)?")) {
            errorMessages.append("Price per day must be a valid number.\n");
        } else {
            pricePerDay = Double.parseDouble(priceText);
        }

        if (errorMessages.length() > 0) {
            vehiclesListTextArea.setText(errorMessages.toString());
            return;
        }

        Vehicle vehicle = vehicleService.addVehicle(carType, model, year, mileage, pricePerDay);
        vehiclesListTextArea.setText("Vehicle added: " + vehicle);
    }

    /**
     * method: validateAndUpdateVehicle
     * parameters: Vehicle vehicle - The vehicle object to update.
     * return: void
     * purpose: Validates and updates the details of the specified vehicle.
     */
    private void validateAndUpdateVehicle(Vehicle vehicle) {
        boolean anyFieldUpdated = false;

        if (!typeTextField.getText().isEmpty()) {
            String carType = typeTextField.getText();
            if (!Validator.isValidCarType(carType)) {
                vehiclesListTextArea.setText("Car type '" + carType + "' is not valid: Valid car types are SUV, Sedan, or Truck.");
                return;
            }
            vehicle.setCarType(carType);
            anyFieldUpdated = true;
        }

        if (!modelTextField.getText().isEmpty()) {
            vehicle.setModel(modelTextField.getText());
            anyFieldUpdated = true;
        }

        if (!yearTextField.getText().isEmpty()) {
            int year;
            try {
                year = Integer.parseInt(yearTextField.getText());
                if (!Validator.isValidYear(year)) {
                    vehiclesListTextArea.setText("Year must be between 1800 and " + Year.now().getValue() + ".");
                    return;
                }
                vehicle.setYear(year);
                anyFieldUpdated = true;
            } catch (NumberFormatException e) {
                vehiclesListTextArea.setText("Year must be a valid number.");
                return;
            }
        }

        if (!mileageTextField.getText().isEmpty()) {
            double mileage;
            try {
                mileage = Double.parseDouble(mileageTextField.getText());
                vehicle.setMileage(mileage);
                anyFieldUpdated = true;
            } catch (NumberFormatException e) {
                vehiclesListTextArea.setText("Mileage must be a valid number.");
                return;
            }
        }

        if (!priceTextField.getText().isEmpty()) {
            double pricePerDay;
            try {
                pricePerDay = Double.parseDouble(priceTextField.getText());
                vehicle.setPricePerDay(pricePerDay);
                anyFieldUpdated = true;
            } catch (NumberFormatException e) {
                vehiclesListTextArea.setText("Price per day must be a valid number.");
                return;
            }
        }

        if (!anyFieldUpdated) {
            vehiclesListTextArea.setText("No updates were made: All fields are empty.");
            return;
        }

        vehicleService.updateVehicle(vehicle);
        vehiclesListTextArea.setText("Vehicle updated: " + vehicle);
    }

    @FXML
    private void handleDeleteVehicle(ActionEvent event) {
        String idText = deleteVehicleIdTextField.getText();
        if (idText.isEmpty()) {
            vehiclesListTextArea.setText("Please enter a vehicle ID to delete.");
            return;
        }
        try {
            int vehicleId = Integer.parseInt(idText);
            vehicleService.deleteVehicle(vehicleId);
            vehiclesListTextArea.setText("Vehicle deleted: ID " + vehicleId);
        } catch (NumberFormatException e) {
            vehiclesListTextArea.setText("Invalid ID format.");
        } catch (Exception e) {
            vehiclesListTextArea.setText("Error deleting vehicle: " + e.getMessage());
        }
    }

    @FXML
    private void handleVehicleFileUpload(ActionEvent event) {
        String filePath = uploadVehiclesTextField.getText();
        FileLoader fileLoader = new FileLoader(null, vehicleService.getVehicleRepository());
        try {
            List<Vehicle> loadedVehicles = fileLoader.loadVehicles(filePath);
            vehiclesListTextArea.setText("Loaded vehicles: " + loadedVehicles.size());
        } catch (Exception e) {
            vehiclesListTextArea.setText("Failed to load vehicles: " + e.getMessage());
        }
    }

    @FXML
    private void handleManageUsers(ActionEvent event) {
        loadScene("/fxml/ManageUsers.fxml", "Manage Users", event);
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
    private void handleEditAccount(ActionEvent event) {
        loadScene("/fxml/EditAccountAdmin.fxml", "Edit Account", event);
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        loadScene("/fxml/Login.fxml", "Login", event);
    }
}