package com.zoomly.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * BaseMenuController.java
 * Base controller class for managing scene transitions in the application.
 * Provides utility methods for loading FXML scenes and displaying error messages.
 */

public class BaseMenuController {

    protected void loadScene(String fxmlPath, String title, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(title);

            if (loader.getController() instanceof BaseMenuController) {
                ((BaseMenuController) loader.getController()).refreshData();
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error loading scene: " + e.getMessage());
        }
    }

    protected void refreshData() {
        // Default implementation does nothing
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}