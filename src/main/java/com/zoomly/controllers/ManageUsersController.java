package com.zoomly.controllers;

import com.zoomly.model.User;
import com.zoomly.service.UserService;
import com.zoomly.util.FileLoader;
import com.zoomly.util.Validator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Optional;

/**
 * ManageUsersController.java
 * Controller class for managing user operations in the application.
 * Provides functionalities to add, update, delete, and list users.
 */

public class ManageUsersController extends BaseMenuController {
    @FXML
    private Button logoutButton;
    @FXML
    private Button manageVehiclesButton;
    @FXML
    private Button manageReservationsButton;
    @FXML
    private Button editAccountButton;
    @FXML
    private Button adminMenuButton;
    @FXML
    private Button listUsersButton;
    @FXML
    private Button updateUserButton;
    @FXML
    private Button addUserButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button uploadFileButton;

    @FXML
    private TextField userIdTextField;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private TextField accountTypeTextField;
    @FXML
    private TextField uploadUsersTextField;
    @FXML
    private TextField deleteUserIdTextField;
    @FXML
    private TextArea usersListTextArea;

    private UserService userService;

    public ManageUsersController() {
        this.userService = UserService.getInstance();
    }

    @FXML
    private void initialize() {
        loadCurrentUserData();
        manageVehiclesButton.setOnAction(this::handleManageVehicles);
        manageReservationsButton.setOnAction(this::handleManageReservations);
        editAccountButton.setOnAction(this::handleEditAccount);
        adminMenuButton.setOnAction(this::handleAdminMenu);
        logoutButton.setOnAction(this::handleLogout);
    }

    private void loadCurrentUserData() {
        User currentUser = userService.getCurrentUser();
    }

    @FXML
    private void handleListUsers(ActionEvent event) {
        List<User> users = userService.getAllUsers();
        StringBuilder userList = new StringBuilder("Users:\n");
        for (User user : users) {
            userList.append(user.toString()).append("\n");
        }
        usersListTextArea.setText(userList.toString());
    }

    @FXML
    private void handleUpdateUser(ActionEvent event) {
        String idText = userIdTextField.getText();
        if (idText.isEmpty()) {
            usersListTextArea.setText("Please enter a user ID to update.");
            return;
        }

        try {
            int userId = Integer.parseInt(idText);
            Optional<User> optionalUser = userService.getUserById(userId);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();

                StringBuilder errorMessages = new StringBuilder();

                String firstName = firstNameTextField.getText();
                if (firstName.isEmpty()) {
                    errorMessages.append("First name cannot be empty.\n");
                } else if (!firstName.matches("[a-zA-Z]+")) {
                    errorMessages.append("Invalid first name: '").append(firstName).append("' cannot use numbers or special characters.\n");
                } else {
                    user.setFirstName(firstName);
                }

                String lastName = lastNameTextField.getText();
                if (lastName.isEmpty()) {
                    errorMessages.append("Last name cannot be empty.\n");
                } else if (!lastName.matches("[a-zA-Z]+")) {
                    errorMessages.append("Invalid last name: '").append(lastName).append("' cannot use numbers or special characters.\n");
                } else {
                    user.setLastName(lastName);
                }

                String email = emailTextField.getText();
                if (email.isEmpty()) {
                    errorMessages.append("Email cannot be empty.\n");
                } else if (!Validator.isValidEmail(email)) {
                    errorMessages.append("Invalid email format. Valid format: example@domain.com\n");
                } else {
                    user.setEmail(email);
                }

                String password = passwordTextField.getText();
                if (password.isEmpty()) {
                    errorMessages.append("Password cannot be empty.\n");
                } else if (password.length() < 8 || !password.matches(".*[0-9].*") || !password.matches(".*[!@#$%^&*].*")) {
                    errorMessages.append("Password must be at least 8 characters long and contain 1 number and 1 special character.\n");
                } else {
                    user.setPassword(password);
                }

                String accountType = accountTypeTextField.getText();
                if (accountType.isEmpty()) {
                    errorMessages.append("Account type cannot be empty.\n");
                } else if (!Validator.isValidAccountType(accountType)) {
                    errorMessages.append("Account type must be 'user' or 'administrator'.\n");
                } else {
                    user.setAccountType(accountType);
                }

                if (errorMessages.length() > 0) {
                    usersListTextArea.setText(errorMessages.toString());
                    return;
                }

                userService.updateUser(user);
                usersListTextArea.setText("User updated: " + user);
            } else {
                usersListTextArea.setText("User not found.");
            }
        } catch (NumberFormatException e) {
            usersListTextArea.setText("Invalid ID format.");
        }
    }

    @FXML
    private void handleAddUser(ActionEvent event) {
        String idText = userIdTextField.getText();
        if (!idText.isEmpty()) {
            usersListTextArea.setText("ID field must be empty to add a user.");
            return;
        }

        try {
            String firstName = firstNameTextField.getText();
            String lastName = lastNameTextField.getText();
            String email = emailTextField.getText();
            String password = passwordTextField.getText();
            String accountType = accountTypeTextField.getText();

            StringBuilder errorMessages = new StringBuilder();

            if (firstName.isEmpty()) {
                errorMessages.append("First name cannot be empty.\n");
            } else if (!firstName.matches("[a-zA-Z]+")) {
                errorMessages.append("Invalid first name: '").append(firstName).append("' cannot use numbers or special characters.\n");
            }

            if (lastName.isEmpty()) {
                errorMessages.append("Last name cannot be empty.\n");
            } else if (!lastName.matches("[a-zA-Z]+")) {
                errorMessages.append("Invalid last name: '").append(lastName).append("' cannot use numbers or special characters.\n");
            }

            if (email.isEmpty()) {
                errorMessages.append("Email cannot be empty.\n");
            } else if (!Validator.isValidEmail(email)) {
                errorMessages.append("Invalid email format. Valid format: example@domain.com\n");
            }

            if (password.isEmpty()) {
                errorMessages.append("Password cannot be empty.\n");
            } else if (password.length() < 8 || !password.matches(".*[0-9].*") || !password.matches(".*[!@#$%^&*].*")) {
                errorMessages.append("Password must be at least 8 characters long and contain 1 number and 1 special character.\n");
            }

            if (accountType.isEmpty()) {
                errorMessages.append("Account type cannot be empty.\n");
            } else if (!Validator.isValidAccountType(accountType)) {
                errorMessages.append("Account type must be 'user' or 'administrator'.\n");
            }

            if (errorMessages.length() > 0) {
                usersListTextArea.setText(errorMessages.toString());
                return;
            }

            User user = userService.registerUser(firstName, lastName, email, password, accountType);
            usersListTextArea.setText("User added: " + user);
        } catch (IllegalArgumentException e) {
            usersListTextArea.setText(e.getMessage());
        }
    }

    @FXML
    private void handleDeleteUser(ActionEvent event) {
        String idText = deleteUserIdTextField.getText();
        if (idText.isEmpty()) {
            usersListTextArea.setText("Please enter a user ID to delete.");
            return;
        }

        try {
            int userId = Integer.parseInt(idText);

            Optional<User> optionalUser = userService.getUserById(userId);
            if (optionalUser.isEmpty()) {
                usersListTextArea.setText("User " + userId + " does not exist.");
                return;
            }

            User currentUser = userService.getCurrentUser();
            if (currentUser != null && currentUser.getId() == userId) {
                usersListTextArea.setText("Cannot delete the currently logged-in user.");
                return;
            }

            userService.deleteUser(userId);
            usersListTextArea.setText("User deleted: ID " + userId);
        } catch (NumberFormatException e) {
            usersListTextArea.setText("Invalid ID format.");
        } catch (Exception e) {
            usersListTextArea.setText("Error deleting user: " + e.getMessage());
        }
    }

    @FXML
    private void handleUserFileUpload(ActionEvent event) {
        String filePath = uploadUsersTextField.getText();
        FileLoader fileLoader = new FileLoader(userService.getUserRepository(), null);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        PrintStream originalOut = System.out;
        System.setOut(printStream);

        try {
            fileLoader.loadUsers(filePath);
        } catch (Exception e) {
            System.out.println("Failed to load users: " + e.getMessage());
        } finally {
            System.setOut(originalOut);
        }

        String output = outputStream.toString();
        usersListTextArea.setText(output);
    }

    @FXML
    private void handleManageVehicles(ActionEvent event) {
        loadScene("/fxml/ManageVehicles.fxml", "Manage Vehicles", event);
    }

    @FXML
    private void handleManageReservations(ActionEvent event) {
        loadScene("/fxml/ManageReservations.fxml", "Manage Reservations", event);
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