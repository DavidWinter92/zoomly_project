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

    /**
     * Loads a new scene specified by the provided FXML path.
     * This method also sets the scene title and attempts to refresh data
     * in the new scene if the controller implements {@link BaseMenuController}.
     *
     * @param fxmlPath The path to the FXML file representing the new scene.
     * @param title The title to set for the new scene's window.
     * @param event The event that triggered the scene transition (used for determining the current stage).
     */
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

    /**
     * Refreshes the data in the current scene.
     * This method can be overridden in subclasses to perform custom actions when a scene is loaded.
     * The default implementation does nothing.
     */
    protected void refreshData() {
        // Default implementation does nothing
    }

    /**
     * Displays an error message in an alert dialog.
     *
     * @param message The error message to be displayed in the alert dialog.
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
