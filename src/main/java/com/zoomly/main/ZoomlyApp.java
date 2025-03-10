package com.zoomly.main;

import com.zoomly.controllers.LoginController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * David Winter
 * CEN-3024C
 * Software Development 1
 * 03/09/2025
 *
 * ZoomlyApp.java
 * This class serves as the main entry point for the Zoomly Vehicle Rental System.
 * It initializes the data from text files and starts the application with a graphical interface.
 */

public class ZoomlyApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            LoginController loginController = new LoginController();
            loginController.loadLoginScene(primaryStage);
            primaryStage.setOnCloseRequest(event -> Platform.exit());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}