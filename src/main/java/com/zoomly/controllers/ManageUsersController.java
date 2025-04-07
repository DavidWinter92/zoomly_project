package com.zoomly.controllers;

import com.zoomly.model.User;
import com.zoomly.service.UserService;
import com.zoomly.util.FileLoader;
import com.zoomly.util.UserValidator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

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
    private Button addUserButton;
    @FXML
    private Button uploadFileButton;
    @FXML
    private Button deleteButton;

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
    private TableView<User> usersTableView;

    @FXML
    private TableColumn<User, Integer> idColumn;
    @FXML
    private TableColumn<User, String> firstNameColumn;
    @FXML
    private TableColumn<User, String> lastNameColumn;
    @FXML
    private TableColumn<User, String> emailColumn;
    @FXML
    private TableColumn<User, String> passwordColumn;
    @FXML
    private TableColumn<User, String> accountTypeColumn;
    @FXML
    private TextField errorTextField;

    private UserService userService;
    private ObservableList<User> userList;

    public ManageUsersController() {
        this.userService = UserService.getInstance();
    }

    @FXML
    private void initialize() {
        setupTableView();
        loadUsersIntoTable();
    }

    private void setupTableView() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        accountTypeColumn.setCellValueFactory(new PropertyValueFactory<>("accountType"));

        firstNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        lastNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        emailColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        passwordColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        accountTypeColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        firstNameColumn.setOnEditCommit(event -> {
            User user = event.getRowValue();
            String originalValue = user.getFirstName();
            String newValue = event.getNewValue();

            user.setFirstName(newValue);
            try {
                UserValidator.validate(user);
                userService.updateUser(user);
            } catch (IllegalArgumentException e) {
                errorTextField.setText(e.getMessage());
                user.setFirstName(originalValue);
                usersTableView.refresh();
                event.consume();
            }
        });

        lastNameColumn.setOnEditCommit(event -> {
            User user = event.getRowValue();
            String originalValue = user.getLastName();
            String newValue = event.getNewValue();

            user.setLastName(newValue);
            try {
                UserValidator.validate(user);
                userService.updateUser(user);
            } catch (IllegalArgumentException e) {
                errorTextField.setText(e.getMessage());
                user.setLastName(originalValue);
                usersTableView.refresh();
                event.consume();
            }
        });

        emailColumn.setOnEditCommit(event -> {
            User user = event.getRowValue();
            String originalValue = user.getEmail();
            String newValue = event.getNewValue();

            user.setEmail(newValue);
            try {
                UserValidator.validate(user);
                userService.updateUser(user);
            } catch (IllegalArgumentException e) {
                errorTextField.setText(e.getMessage());
                user.setEmail(originalValue);
                usersTableView.refresh();
                event.consume();
            }
        });

        passwordColumn.setOnEditCommit(event -> {
            User user = event.getRowValue();
            String originalValue = user.getPassword();
            String newValue = event.getNewValue();

            user.setPassword(newValue);
            try {
                UserValidator.validate(user);
                userService.updateUser(user);
            } catch (IllegalArgumentException e) {
                errorTextField.setText(e.getMessage());
                user.setPassword(originalValue);
                usersTableView.refresh();
                event.consume();
            }
        });

        accountTypeColumn.setOnEditCommit(event -> {
            User user = event.getRowValue();
            String originalValue = user.getAccountType();
            String newValue = event.getNewValue();

            user.setAccountType(newValue);
            try {
                UserValidator.validate(user);
                userService.updateUser(user);
            } catch (IllegalArgumentException e) {
                errorTextField.setText(e.getMessage());
                user.setAccountType(originalValue);
                usersTableView.refresh();
                event.consume();
            }
        });


        userList = FXCollections.observableArrayList();
        usersTableView.setItems(userList);
        usersTableView.setEditable(true);
    }

    private void loadUsersIntoTable() {
        List<User> users = userService.getAllUsers();
        userList.setAll(users);
    }

    @FXML
    private void handleAddUser(ActionEvent event) {
        try {
            String firstName = firstNameTextField.getText();
            String lastName = lastNameTextField.getText();
            String email = emailTextField.getText();
            String password = passwordTextField.getText();
            String accountType = accountTypeTextField.getText();

            if (userService.isEmailRegistered(email)) {
                errorTextField.setText("Email is already registered.");
                return;
            }

            User newUser = new User(0, firstName, lastName, email, password, accountType);
            UserValidator.validate(newUser);

            userService.registerUser(firstName, lastName, email, password, accountType);
            loadUsersIntoTable();
            errorTextField.setText("User added successfully.");
        } catch (IllegalArgumentException e) {
            errorTextField.setText(e.getMessage());
        }
    }

    @FXML
    private void handleDeleteUser(ActionEvent event) {
        User selectedUser = usersTableView.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            errorTextField.setText("Please select a user to delete.");
            return;
        }

        try {
            userService.deleteUser(selectedUser.getId());
            loadUsersIntoTable();
            errorTextField.setText("User deleted successfully.");
        } catch (Exception e) {
            errorTextField.setText("Error deleting user: " + e.getMessage());
        }
    }

    @FXML
    private void handleUserFileUpload(ActionEvent event) {
        String filePath = uploadUsersTextField.getText();
        FileLoader fileLoader = new FileLoader(userService.getUserDao(), null);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        PrintStream originalOut = System.out;
        System.setOut(printStream);

        try {
            fileLoader.loadUsers(filePath);
            loadUsersIntoTable();
        } catch (Exception e) {
            System.out.println("Failed to load users: " + e.getMessage());
        } finally {
            System.setOut(originalOut);
        }

        String output = outputStream.toString();
        errorTextField.setText(output);
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