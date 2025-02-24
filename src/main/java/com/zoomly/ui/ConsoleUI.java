package com.zoomly.ui;

import com.zoomly.repository.*;
import com.zoomly.service.*;
import com.zoomly.model.User;
import java.util.Scanner;

/**
 * Main console user interface for the application.
 * Handles all user interaction and menu navigation.
 */

public class ConsoleUI {
    private final Scanner scanner;
    private final UserService userService;
    private final VehicleService vehicleService;
    private final ReservationService reservationService;
    private User currentUser;

    public ConsoleUI() {
        this.scanner = new Scanner(System.in);

        // Initialize repositories
        UserRepository userRepository = new UserRepository();
        VehicleRepository vehicleRepository = new VehicleRepository();
        ReservationRepository reservationRepository = new ReservationRepository();

        // Initialize services
        this.userService = new UserService(userRepository);
        this.vehicleService = new VehicleService(vehicleRepository);
        this.reservationService = new ReservationService(reservationRepository, vehicleRepository);
    }

    public UserService getUserService() {
        return userService;
    }

    public VehicleService getVehicleService() {
        return vehicleService;
    }

    public ReservationService getReservationService() {
        return reservationService;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void displayMainMenu() {
        System.out.println("\n--- Zoomly Vehicle Rental System ---");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
    }

    public int getUserChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public void handleRegistration() {
        System.out.println("\n--- Registration ---");
        try {
            System.out.print("First Name: ");
            String firstName = scanner.nextLine();
            System.out.print("Last Name: ");
            String lastName = scanner.nextLine();
            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();
            System.out.print("Account Type (user/administrator): ");
            String accountType = scanner.nextLine();

            userService.registerUser(firstName, lastName, email, password, accountType);
            System.out.println("Registration successful!");
        } catch (IllegalArgumentException e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }
}