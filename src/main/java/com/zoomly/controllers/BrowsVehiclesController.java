package com.zoomly.controllers;

import com.zoomly.model.Reservation;
import com.zoomly.model.User;
import com.zoomly.model.Vehicle;
import com.zoomly.service.ReservationService;
import com.zoomly.service.UserService;
import com.zoomly.service.VehicleService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * BrowsVehiclesController.java
 * Controller class for displaying and interacting with vehicles in the browsing view.
 * Handles filtering, vehicle selection, and reservations.
 */

public class BrowsVehiclesController extends BaseMenuController {
    @FXML private Button userProfileButton;
    @FXML private Button reservationsButton;
    @FXML private Button editAccountButton;
    @FXML private Button logoutButton;
    @FXML private Button reserveButton;
    @FXML private Button filterButton;

    @FXML private Label selectedVehicleLabel;
    @FXML private Label reservationErrorLabel;

    @FXML private VBox vehicleCardContainer;
    @FXML private ScrollPane vehicleScrollPane;

    @FXML private DatePicker pickupDatePicker;
    @FXML private DatePicker dropOffDatePicker;

    @FXML private ChoiceBox<String> makeChoiceBox;
    @FXML private ChoiceBox<String> modelChoiceBox;

    @FXML private TextField mileageTextField;
    @FXML private TextField pricePerDayTextField;
    @FXML private TextField yearTextField;

    private final UserService userService = UserService.getInstance();
    private final VehicleService vehicleService = VehicleService.getInstance();

    private VBox selectedCard = null;
    private Vehicle selectedVehicle = null;

    @FXML
    private void initialize() {
        loadCurrentUserData();

        vehicleScrollPane.setFitToWidth(true);
        vehicleScrollPane.setStyle("-fx-background: #101010; -fx-border-color: #101010;");

        pickupDatePicker.setValue(LocalDate.now());
        pickupDatePicker.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(empty || item.isBefore(LocalDate.now()));
            }
        });

        dropOffDatePicker.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(empty || item.isBefore(pickupDatePicker.getValue()));
            }
        });

        makeChoiceBox.setOnAction(e -> handleMakeSelection());
        modelChoiceBox.setOnAction(e -> handleModelSelection());

        populateMakeChoiceBox();

        userProfileButton.setOnAction(this::handleUserProfile);
        reservationsButton.setOnAction(this::handleReservations);
        editAccountButton.setOnAction(this::handleEditAccount);
        logoutButton.setOnAction(this::handleLogout);
        reserveButton.setOnAction(this::handleReservation);
        filterButton.setOnAction(this::handleFilter);

        loadVehicles();
    }

    private void loadCurrentUserData() {
        User currentUser = userService.getCurrentUser();
    }

    private void populateMakeChoiceBox() {
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        Set<String> makes = vehicles.stream().map(Vehicle::getMake).collect(Collectors.toSet());

        makes.add("None");

        makeChoiceBox.setItems(FXCollections.observableArrayList(makes));
        makeChoiceBox.setValue("None");
    }

    private void handleMakeSelection() {
        String selectedMake = makeChoiceBox.getValue();

        if (selectedMake == null || selectedMake.equals("None")) {
            modelChoiceBox.setItems(FXCollections.observableArrayList());
            modelChoiceBox.setValue(null);
            loadVehiclesByMake(selectedMake);
        } else {
            List<String> models = vehicleService.getAllVehicles().stream()
                    .filter(v -> v.getMake().equals(selectedMake))
                    .map(Vehicle::getModel)
                    .distinct()
                    .collect(Collectors.toList());

            models.add("None");
            modelChoiceBox.setItems(FXCollections.observableArrayList(models));
            modelChoiceBox.setValue("None");

            loadVehiclesByMake(selectedMake);
        }
    }


    private void loadVehiclesByMake(String make) {
        List<Vehicle> vehicles;

        if (make == null || make.equals("None")) {
            vehicles = vehicleService.getAllVehicles();
        } else {
            vehicles = vehicleService.getAllVehicles().stream()
                    .filter(v -> v.getMake().equals(make))
                    .collect(Collectors.toList());
        }

        vehicleCardContainer.getChildren().clear();

        for (Vehicle vehicle : vehicles) {
            VBox card = createVehicleCard(vehicle);
            vehicleCardContainer.getChildren().add(card);
        }
    }

    private void handleModelSelection() {

        String selectedModel = modelChoiceBox.getValue();
        if (selectedModel != null && selectedModel.equals("None")) {
        }
    }

    private void loadVehicles() {
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        vehicleCardContainer.getChildren().clear();

        for (Vehicle vehicle : vehicles) {
            VBox card = createVehicleCard(vehicle);
            vehicleCardContainer.getChildren().add(card);
        }
    }

    /**
     * method: createVehicleCard
     * parameters: Vehicle vehicle - The vehicle object containing the details to be displayed on the card.
     * return: VBox - A VBox containing the vehicle card with image, details, and interaction logic.
     * purpose: Creates and returns a VBox representing a vehicle card, displaying its image, make, model, year, mileage, price per day, and description.
     *          The card also supports hover and click events for selecting a vehicle and updating the selected vehicle label.
     */
    private VBox createVehicleCard(Vehicle vehicle) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(12));
        card.setStyle("-fx-background-color: #2a2a2a; -fx-background-radius: 8;");
        card.setPrefWidth(Region.USE_COMPUTED_SIZE);

        card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: #3a3a3a; -fx-background-radius: 8;"));
        card.setOnMouseExited(e -> card.setStyle("-fx-background-color: #2a2a2a; -fx-background-radius: 8;"));

        HBox topRow = new HBox(15);
        ImageView imageView = new ImageView();
        imageView.setFitWidth(160);
        imageView.setFitHeight(100);
        imageView.setPreserveRatio(true);

        File file = new File(vehicle.getImagePath());
        if (file.exists()) {
            imageView.setImage(new Image(file.toURI().toString()));
        } else {
            imageView.setImage(new Image("/vehicle_imgs/default_car.png"));
        }

        VBox infoBox = new VBox(5);
        Label titleLabel = new Label(vehicle.getMake() + " " + vehicle.getModel());
        titleLabel.setFont(Font.font("System Bold", 14));
        titleLabel.setTextFill(Color.web("#6fbf4e"));

        Label yearLabel = new Label("Year: " + vehicle.getYear());
        Label mileageLabel = new Label(String.format("Mileage: %.0f km", vehicle.getMileage()));
        Label priceLabel = new Label(String.format("Price/Day: $%.2f", vehicle.getPricePerDay()));
        yearLabel.setTextFill(Color.WHITE);
        mileageLabel.setTextFill(Color.WHITE);
        priceLabel.setTextFill(Color.WHITE);

        infoBox.getChildren().addAll(titleLabel, yearLabel, mileageLabel, priceLabel);
        topRow.getChildren().addAll(imageView, infoBox);

        Label descLabel = new Label(vehicle.getDescription());
        descLabel.setWrapText(true);
        descLabel.setTextFill(Color.LIGHTGRAY);
        descLabel.setFont(Font.font(11));

        card.getChildren().addAll(topRow, descLabel);

        card.setOnMouseClicked(e -> {
            if (selectedCard != null) {
                selectedCard.setStyle("-fx-background-color: #2a2a2a; -fx-background-radius: 8;");
            }

            selectedCard = card;
            selectedVehicle = vehicle;

            if (selectedVehicleLabel != null) {
                selectedVehicleLabel.setText("Selected: " + vehicle.getMake() + " " + vehicle.getModel());
            }

            card.setStyle("-fx-background-color: #3d3d3d; -fx-background-radius: 8;");
        });

        return card;
    }

    @FXML
    private void handleReservation(ActionEvent event) {
        if (selectedVehicle == null) {
            reservationErrorLabel.setText("Please select a vehicle to reserve.");
            reservationErrorLabel.setTextFill(Color.ORANGE);
            return;
        }

        LocalDate pickup = pickupDatePicker.getValue();
        LocalDate dropOff = dropOffDatePicker.getValue();

        if (pickup == null || dropOff == null || !dropOff.isAfter(pickup)) {
            reservationErrorLabel.setText("Invalid date range. Drop-off must be after pickup.");
            reservationErrorLabel.setTextFill(Color.ORANGE);
            return;
        }

        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            reservationErrorLabel.setText("User session expired. Please log in again.");
            reservationErrorLabel.setTextFill(Color.RED);
            return;
        }

        try {
            Reservation reservation = ReservationService.getInstance().createReservation(
                    currentUser.getId(),
                    selectedVehicle.getId(),
                    java.sql.Date.valueOf(pickup),
                    java.sql.Date.valueOf(dropOff)
            );

            selectedVehicleLabel.setText("Vehicle Reserved");
            selectedVehicleLabel.setTextFill(Color.LIGHTGREEN);
            reservationErrorLabel.setText("");

        } catch (IllegalArgumentException e) {
            String message = e.getMessage();
            if (message != null && message.toLowerCase().contains("not available")) {
                String conflictInfo = "";
                int index = message.indexOf(":");
                if (index != -1 && index + 1 < message.length()) {
                    conflictInfo = message.substring(index + 1).trim();
                }
                reservationErrorLabel.setText("This vehicle is already reserved for the dates: " + conflictInfo);
            } else {
                reservationErrorLabel.setText("Reservation failed: " + message);
            }
            reservationErrorLabel.setTextFill(Color.RED);
        }
    }






    @FXML
    private void handleFilter(ActionEvent event) {
        String selectedMake = makeChoiceBox.getValue();
        String selectedModel = modelChoiceBox.getValue();
        String mileage = mileageTextField.getText();
        String price = pricePerDayTextField.getText();
        String year = yearTextField.getText();

        List<Vehicle> filteredVehicles = vehicleService.getAllVehicles().stream()
                .filter(v -> (selectedMake == null || v.getMake().equals(selectedMake)) &&
                        (selectedModel == null || v.getModel().equals(selectedModel)) &&
                        (mileage.isEmpty() || v.getMileage() <= Long.parseLong(mileage)) &&
                        (price.isEmpty() || String.valueOf(v.getPricePerDay()).contains(price)) &&
                        (year.isEmpty() || String.valueOf(v.getYear()).equals(year)))
                .collect(Collectors.toList());

        vehicleCardContainer.getChildren().clear();

        for (Vehicle vehicle : filteredVehicles) {
            VBox card = createVehicleCard(vehicle);
            vehicleCardContainer.getChildren().add(card);
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
