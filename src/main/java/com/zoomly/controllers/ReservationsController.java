package com.zoomly.controllers;

import com.zoomly.model.Reservation;
import com.zoomly.model.User;
import com.zoomly.model.Vehicle;
import com.zoomly.service.ReservationService;
import com.zoomly.service.UserService;
import com.zoomly.service.VehicleService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.File;
import java.util.List;

/**
 * ReservationsController.java
 * Controller class for managing user reservations in the application.
 * Provides functionalities to list, cancel, and navigate to different user-related views.
 */
public class ReservationsController extends BaseMenuController {
    @FXML
    private VBox reservationsVBox;
    @FXML
    private ScrollPane reservationScrollPane;
    @FXML
    private Button browsVehicleButton;
    @FXML
    private Button userProfileButton;
    @FXML
    private Button editAccountButton;
    @FXML
    private Button logoutButton;
    @FXML
    private Button cancelReservationButton;

    private UserService userService;
    private ReservationService reservationService;
    private VehicleService vehicleService;

    /**
     * Constructor for ReservationsController
     * Initializes service instances for user, reservation, and vehicle services.
     */
    public ReservationsController() {
        this.userService = UserService.getInstance();
        this.reservationService = ReservationService.getInstance();
        this.vehicleService = VehicleService.getInstance();
    }

    /**
     * Initializes the controller by setting up event handlers and loading the user data.
     * Also sets styles for the reservation scroll pane.
     */
    @FXML
    private void initialize() {
        loadCurrentUserData();

        reservationScrollPane.setFitToWidth(true);
        reservationScrollPane.setStyle("-fx-background: #101010; -fx-border-color: #101010;");

        browsVehicleButton.setOnAction(this::handleBrowsVehicle);
        userProfileButton.setOnAction(this::handleUserProfile);
        editAccountButton.setOnAction(this::handleEditAccount);
        logoutButton.setOnAction(this::handleLogout);
        createReservationCard(null);
    }

    /**
     * Loads the current user data.
     * Retrieves the current logged-in user using the UserService.
     */
    private void loadCurrentUserData() {
        User currentUser = userService.getCurrentUser();
    }

    /**
     * Creates and displays reservation cards for the current user's reservations.
     * Each card displays vehicle details, reservation information, and a cancel button.
     *
     * @param event The event triggered by the user action (not used in this case)
     */
    @FXML
    private void createReservationCard(ActionEvent event) {
        List<Reservation> reservations = reservationService.getUserReservations(userService.getCurrentUser().getId());

        reservationsVBox.getChildren().clear();

        for (Reservation reservation : reservations) {
            Vehicle vehicle = vehicleService.getVehicleById(reservation.getVehicleId())
                    .orElse(new Vehicle(0, "N/A", "Unknown", "Unknown", 0, 0.0, 0.0, "", ""));

            VBox reservationCard = new VBox(10);
            reservationCard.setPadding(new Insets(12));
            reservationCard.setStyle("-fx-background-color: #2a2a2a; -fx-background-radius: 8;");
            reservationCard.setPrefWidth(Region.USE_COMPUTED_SIZE);

            reservationCard.setOnMouseEntered(e -> reservationCard.setStyle("-fx-background-color: #3a3a3a; -fx-background-radius: 8;"));
            reservationCard.setOnMouseExited(e -> reservationCard.setStyle("-fx-background-color: #2a2a2a; -fx-background-radius: 8;"));

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

            HBox titleAndCancelBox = new HBox();
            titleAndCancelBox.setSpacing(10);
            titleAndCancelBox.setStyle("-fx-alignment: space-between;");

            Label titleLabel = new Label(vehicle.getMake() + " " + vehicle.getModel());
            titleLabel.setFont(Font.font("System Bold", 14));
            titleLabel.setTextFill(Color.web("#6fbf4e"));
            HBox.setHgrow(titleLabel, Priority.ALWAYS);

            Button cancelButton = new Button("Cancel");
            cancelButton.setStyle("-fx-background-color: #ff224a; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 4 10; -fx-background-radius: 6;");
            cancelButton.setOnAction(e -> handleCancelReservation(reservation.getId()));

            titleAndCancelBox.getChildren().addAll(titleLabel, cancelButton);

            Label yearLabel = new Label("Year: " + vehicle.getYear());
            Label mileageLabel = new Label(String.format("Mileage: %.0f km", vehicle.getMileage()));
            Label priceLabel = new Label(String.format("Price/Day: $%.2f", vehicle.getPricePerDay()));
            yearLabel.setTextFill(Color.WHITE);
            mileageLabel.setTextFill(Color.WHITE);
            priceLabel.setTextFill(Color.WHITE);

            infoBox.getChildren().addAll(titleAndCancelBox, yearLabel, mileageLabel, priceLabel);
            topRow.getChildren().addAll(imageView, infoBox);

            Label descLabel = new Label(vehicle.getDescription());
            descLabel.setWrapText(true);
            descLabel.setTextFill(Color.LIGHTGRAY);
            descLabel.setFont(Font.font(11));

            reservationCard.getChildren().addAll(topRow, descLabel);
            reservationsVBox.getChildren().add(reservationCard);
        }
    }

    /**
     * Handles the cancellation of a reservation.
     * Removes the reservation from the list and updates the displayed reservation cards.
     *
     * @param reservationId The ID of the reservation to cancel
     */
    @FXML
    private void handleCancelReservation(int reservationId) {
        try {
            reservationService.cancelReservation(reservationId);

            reservationsVBox.getChildren().clear();
            createReservationCard(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the action to navigate to the Browse Vehicles view.
     *
     * @param event The event triggered by the user action
     */
    @FXML
    private void handleBrowsVehicle(ActionEvent event) {
        loadScene("/fxml/BrowsVehicles.fxml", "Browse Vehicles", event);
    }

    /**
     * Handles the action to navigate to the User Profile view.
     *
     * @param event The event triggered by the user action
     */
    @FXML
    private void handleUserProfile(ActionEvent event) {
        loadScene("/fxml/User.fxml", "User Profile", event);
    }

    /**
     * Handles the action to navigate to the Edit Account view.
     *
     * @param event The event triggered by the user action
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
