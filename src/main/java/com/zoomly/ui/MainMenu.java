package com.zoomly.ui;

import com.zoomly.model.User;
import com.zoomly.model.Vehicle;
import com.zoomly.repository.UserRepository;
import com.zoomly.repository.VehicleRepository;
import com.zoomly.util.FileLoader;

import java.io.IOException;
import java.util.List;

/**
 * MainMenu.java
 * This class, like ConsoleUI, handles user interface for the application by pointing
 * to ConsoleUI for external functions.
 */

public class MainMenu {
    private final ConsoleUI consoleUI;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private User currentUser;

    public MainMenu(ConsoleUI consoleUI, UserRepository userRepository, VehicleRepository vehicleRepository) {
        this.consoleUI = consoleUI;
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
    }

    public void start() {
        boolean running = true;

        while (running) {
            consoleUI.displayMainMenu();
            int choice = consoleUI.getUserChoice(); // Delegate to ConsoleUI

            switch (choice) {
                case 1:
                    handleLogin();
                    break;
                case 2:
                    handleRegistration();
                    break;
                case 3:
                    uploadUsersFromFile(); // New option to upload users
                    break;
                case 4:
                    uploadVehiclesFromFile(); // New option to upload vehicles
                    break;
                case 5:
                    running = false;
                    System.out.println("Thank you for using Zoomly. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void handleLogin() {
        System.out.println("\n--- Login ---");
        System.out.print("Email: ");
        String email = consoleUI.getScanner().nextLine();
        System.out.print("Password: ");
        String password = consoleUI.getScanner().nextLine();

        currentUser = consoleUI.getUserService().login(email, password).orElse(null);
        if (currentUser != null) {
            System.out.println("Login successful! Welcome, " + currentUser.getFirstName() + "!");
            if (currentUser.getAccountType().equals("administrator")) {
                handleAdminMenu();
            } else {
                handleUserMenu();
            }
        } else {
            System.out.println("Invalid email or password. Please try again.");
        }
    }

    private void handleRegistration() {
        consoleUI.handleRegistration();
    }

    private void handleAdminMenu() {
        AdminMenu adminMenu = new AdminMenu(consoleUI, currentUser, consoleUI.getUserService(),
                consoleUI.getVehicleService(), consoleUI.getReservationService());
        adminMenu.show();
    }

    private void handleUserMenu() {
        UserMenu userMenu = new UserMenu(consoleUI, currentUser, consoleUI.getUserService(),
                consoleUI.getVehicleService(), consoleUI.getReservationService());
        userMenu.show();
    }

    private void uploadUsersFromFile() {
        AdminMenu adminMenu = new AdminMenu(consoleUI, currentUser, consoleUI.getUserService(),
                consoleUI.getVehicleService(), consoleUI.getReservationService());
        adminMenu.handleUserFileUpload(); // Call the user upload method
    }

    private void uploadVehiclesFromFile() {
        AdminMenu adminMenu = new AdminMenu(consoleUI, currentUser, consoleUI.getUserService(),
                consoleUI.getVehicleService(), consoleUI.getReservationService());
        adminMenu.handleVehicleFileUpload(); // Call the vehicle upload method
    }
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
}