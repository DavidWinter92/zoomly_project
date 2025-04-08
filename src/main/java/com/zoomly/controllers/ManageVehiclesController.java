package com.zoomly.controllers;

import com.zoomly.model.Vehicle;
import com.zoomly.service.VehicleService;
import com.zoomly.util.FileLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.util.List;

/**
 * ManageVehiclesController.java
 * Controller class responsible for managing vehicle-related actions in the application.
 * Handles adding, deleting, updating vehicle details, uploading vehicle files, and navigating between scenes.
 */
public class ManageVehiclesController extends BaseMenuController {

    @FXML
    private Button addVehicleButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button uploadButton;
    @FXML
    private Button editVehicleButton;
    @FXML
    private TextField vinTextField;
    @FXML
    private TextField makeTextField;
    @FXML
    private TextField modelTextField;
    @FXML
    private TextField yearTextField;
    @FXML
    private TextField mileageTextField;
    @FXML
    private TextField priceTextField;
    @FXML
    private TextField imagePathTextField;
    @FXML
    private TextField descriptionTextField;
    @FXML
    private TextField uploadVehiclesTextField;
    @FXML
    private TableView<Vehicle> vehiclesTableView;

    @FXML
    private TableColumn<Vehicle, Integer> idColumn;
    @FXML
    private TableColumn<Vehicle, String> makeColumn;
    @FXML
    private TableColumn<Vehicle, String> modelColumn;
    @FXML
    private TableColumn<Vehicle, Integer> yearColumn;
    @FXML
    private TableColumn<Vehicle, Double> mileageColumn;
    @FXML
    private TableColumn<Vehicle, String> vinColumn;
    @FXML
    private TableColumn<Vehicle, String> imagePathColumn;
    @FXML
    private TableColumn<Vehicle, String> descriptionColumn;
    @FXML
    private TableColumn<Vehicle, Double> pricePerDayColumn;
    @FXML
    private Label errorLabel;

    private VehicleService vehicleService;

    /**
     * Constructor for ManageVehiclesController. Initializes the VehicleService.
     */
    public ManageVehiclesController() {
        this.vehicleService = VehicleService.getInstance();
    }

    /**
     * Initializes the controller. Sets up the TableView and loads vehicles into the table.
     */
    @FXML
    private void initialize() {
        setupTableView();
        loadVehicles();
    }

    /**
     * Configures the TableView by setting the cell factories and event listeners for cell edits.
     */
    private void setupTableView() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        makeColumn.setCellValueFactory(new PropertyValueFactory<>("make"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        mileageColumn.setCellValueFactory(new PropertyValueFactory<>("mileage"));
        vinColumn.setCellValueFactory(new PropertyValueFactory<>("vin"));
        imagePathColumn.setCellValueFactory(new PropertyValueFactory<>("imagePath"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        pricePerDayColumn.setCellValueFactory(new PropertyValueFactory<>("pricePerDay"));

        makeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        modelColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        yearColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        mileageColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        pricePerDayColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));

        descriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        imagePathColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        makeColumn.setOnEditCommit(event -> updateVehicleField(event, "make"));
        modelColumn.setOnEditCommit(event -> updateVehicleField(event, "model"));
        yearColumn.setOnEditCommit(event -> updateVehicleField(event, "year"));
        mileageColumn.setOnEditCommit(event -> updateVehicleField(event, "mileage"));
        pricePerDayColumn.setOnEditCommit(event -> updateVehicleField(event, "pricePerDay"));
        descriptionColumn.setOnEditCommit(event -> updateVehicleField(event, "description"));
        imagePathColumn.setOnEditCommit(event -> updateVehicleField(event, "imagePath"));

        vehiclesTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        vehiclesTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                System.out.println("Selected vehicle: " + newValue.getMake());
            }
        });
    }

    /**
     * Updates the specified vehicle field when an edit is committed in the table.
     *
     * @param event The edit event.
     * @param fieldName The field name being updated.
     */
    private void updateVehicleField(TableColumn.CellEditEvent<Vehicle, ?> event, String fieldName) {
        Vehicle selectedVehicle = event.getRowValue();
        String originalValue = event.getOldValue().toString();
        String newValue = event.getNewValue().toString();

        try {
            switch (fieldName) {
                case "make":
                    selectedVehicle.setMake(newValue);
                    break;
                case "model":
                    selectedVehicle.setModel(newValue);
                    break;
                case "year":
                    selectedVehicle.setYear(Integer.parseInt(newValue));
                    break;
                case "mileage":
                    selectedVehicle.setMileage(Double.parseDouble(newValue));
                    break;
                case "pricePerDay":
                    selectedVehicle.setPricePerDay(Double.parseDouble(newValue));
                    break;
                case "description":
                    selectedVehicle.setDescription(newValue);
                    break;
                case "imagePath":
                    selectedVehicle.setImagePath(newValue);
                    break;
            }

            vehicleService.updateVehicle(
                    selectedVehicle.getId(),
                    selectedVehicle.getVin(),
                    selectedVehicle.getMake(),
                    selectedVehicle.getModel(),
                    selectedVehicle.getYear(),
                    selectedVehicle.getMileage(),
                    selectedVehicle.getPricePerDay(),
                    selectedVehicle.getImagePath(),
                    selectedVehicle.getDescription()
            );

            loadVehicles();
        } catch (Exception e) {
            errorLabel.setText("Error updating vehicle: " + e.getMessage());
            revertVehicleField(event, originalValue);
        }
    }

    /**
     * Reverts the vehicle field to its original value in case of an error during update.
     *
     * @param event The edit event.
     * @param originalValue The original value of the field.
     */
    private void revertVehicleField(TableColumn.CellEditEvent<Vehicle, ?> event, String originalValue) {
        Vehicle selectedVehicle = event.getRowValue();

        String fieldName = event.getTableColumn().getText();

        try {
            switch (fieldName) {
                case "Make":
                    selectedVehicle.setMake(originalValue);
                    break;
                case "Model":
                    selectedVehicle.setModel(originalValue);
                    break;
                case "Year":
                    selectedVehicle.setYear(Integer.parseInt(originalValue));
                    break;
                case "Mileage":
                    selectedVehicle.setMileage(Double.parseDouble(originalValue));
                    break;
                case "Price per Day":
                    selectedVehicle.setPricePerDay(Double.parseDouble(originalValue));
                    break;
                case "Description":
                    selectedVehicle.setDescription(originalValue);
                    break;
                case "Image Path":
                    selectedVehicle.setImagePath(originalValue);
                    break;
            }
        } catch (Exception e) {
            errorLabel.setText("Error reverting changes: " + e.getMessage());
        }

        vehiclesTableView.refresh();
        event.consume();
    }

    /**
     * Loads the list of vehicles from the vehicle service and updates the TableView.
     */
    private void loadVehicles() {
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        vehiclesTableView.getItems().clear();
        vehiclesTableView.getItems().addAll(vehicles);
    }

    /**
     * Handles the event of adding a new vehicle.
     *
     * @param event The action event triggered when the add button is clicked.
     */
    @FXML
    private void handleAddVehicle(ActionEvent event) {
        String vin = vinTextField.getText();
        String make = makeTextField.getText();
        String model = modelTextField.getText();
        int year = Integer.parseInt(yearTextField.getText());
        double mileage = Double.parseDouble(mileageTextField.getText());
        double pricePerDay = Double.parseDouble(priceTextField.getText());
        String imagePath = imagePathTextField.getText();
        String description = descriptionTextField.getText();

        try {
            Vehicle vehicle = vehicleService.addVehicle(vin, make, model, year, mileage, pricePerDay, imagePath, description);
            loadVehicles();
            errorLabel.setText("Vehicle added successfully: " + vehicle);
        } catch (IllegalArgumentException e) {
            errorLabel.setText("Error: " + e.getMessage());
        } catch (Exception e) {
            errorLabel.setText("An unexpected error occurred: " + e.getMessage());
        }
    }

    /**
     * Handles the event of deleting a selected vehicle.
     *
     * @param event The action event triggered when the delete button is clicked.
     */
    @FXML
    private void handleDeleteVehicle(ActionEvent event) {
        Vehicle selectedVehicle = vehiclesTableView.getSelectionModel().getSelectedItem();
        if (selectedVehicle != null) {
            vehicleService.deleteVehicle(selectedVehicle.getId());
            loadVehicles();
            vehiclesTableView.getSelectionModel().clearSelection();
            errorLabel.setText("Vehicle deleted: " + selectedVehicle.getVin());
        } else {
            errorLabel.setText("Please select a vehicle to delete.");
        }
    }

    /**
     * Handles the event of uploading vehicle data from a file.
     *
     * @param event The action event triggered when the upload button is clicked.
     */
    @FXML
    private void handleVehicleFileUpload(ActionEvent event) {
        String filePath = uploadVehiclesTextField.getText();
        FileLoader fileLoader = new FileLoader(null, vehicleService.getVehicleDao());

        try {
            fileLoader.loadVehicles(filePath);
            loadVehicles();
            errorLabel.setText("Vehicles loaded successfully.");
        } catch (IOException e) {
            errorLabel.setText("Failed to load vehicles: " + e.getMessage());
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
