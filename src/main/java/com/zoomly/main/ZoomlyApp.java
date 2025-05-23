package com.zoomly.main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * David Winter
 * CEN-3024C
 * Software Development 1
 * 04/07/2025
 *
 * ZoomlyApp.java
 * This class serves as the main entry point for the Zoomly Vehicle Rental System.
 * It initializes the data from text files and starts the application with a graphical interface.
 */

public class ZoomlyApp extends Application {

    /**
     * Starts the JavaFX application by loading the ServerSetup.fxml file.
     *
     * @param primaryStage the main stage for this application
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ServerSetup.fxml"));
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.setTitle("Zoomly Vehicle Rental");
            primaryStage.show();
            primaryStage.setOnCloseRequest(event -> Platform.exit());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Main method that launches the JavaFX application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}