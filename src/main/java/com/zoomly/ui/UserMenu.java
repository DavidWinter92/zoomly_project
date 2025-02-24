package com.zoomly.ui;

import com.zoomly.model.User;
import com.zoomly.model.Vehicle;
import com.zoomly.model.Reservation;
import com.zoomly.service.UserService;
import com.zoomly.service.VehicleService;
import com.zoomly.service.ReservationService;
import java.util.Scanner;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * UserMenu.java
 * This class handles the user menu interface, providing options for
 * browsing vehicles, managing reservations, and account settings.
 */

public class UserMenu {
    private final Scanner scanner;
    private final User currentUser;
    private final UserService userService;
    private final VehicleService vehicleService;
    private final ReservationService reservationService;
    private final SimpleDateFormat dateFormat;

    public UserMenu(User currentUser, UserService userService,
                    VehicleService vehicleService, ReservationService reservationService) {
        this.scanner = new Scanner(System.in);
        this.currentUser = currentUser;
        this.userService = userService;
        this.vehicleService = vehicleService;
        this.reservationService = reservationService;
        this.dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    }


    public void show() {
        boolean running = true;
        while (running) {
            System.out.println("\n--- User Menu ---");
            System.out.println("1. Browse Vehicles");
            System.out.println("2. My Reservations");
            System.out.println("3. Account Settings");
            System.out.println("4. Logout");
            System.out.print("Enter your choice: ");

            int choice = getUserChoice();
            switch (choice) {
                case 1:
                    handleBrowseVehicles();
                    break;
                case 2:
                    handleReservations();
                    break;
                case 3:
                    handleAccountSettings();
                    break;
                case 4:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void handleBrowseVehicles() {
        while (true) {
            System.out.println("\n--- Browse Vehicles ---");
            System.out.println("1. List all vehicles");
            System.out.println("2. Reserve vehicle");
            System.out.println("3. Return to previous menu");
            System.out.print("Enter your choice: ");

            int choice = getUserChoice();
            switch (choice) {
                case 1:
                    listAllVehicles();
                    break;
                case 2:
                    handleReservation();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void listAllVehicles() {
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        System.out.println("\n--- Available Vehicles ---");
        for (Vehicle vehicle : vehicles) {
            System.out.printf("ID: %d | %d %s %s | Mileage: %.1f | Price/Day: $%.2f%n",
                    vehicle.getId(), vehicle.getYear(), vehicle.getCarType(),
                    vehicle.getModel(), vehicle.getMileage(), vehicle.getPricePerDay());
        }
    }

    private void handleReservation() {
        listAllVehicles();
        System.out.print("\nEnter vehicle ID to reserve (0 to cancel): ");
        int vehicleId = getUserChoice();
        if (vehicleId == 0) return;

        try {
            System.out.print("Enter pickup date (MM/dd/yyyy): ");
            Date pickupDate = dateFormat.parse(scanner.nextLine());

            System.out.print("Enter drop-off date (MM/dd/yyyy): ");
            Date dropOffDate = dateFormat.parse(scanner.nextLine());

            System.out.print("Confirm reservation (Y/N)? ");
            String confirm = scanner.nextLine();

            if (confirm.equalsIgnoreCase("Y")) {
                Reservation reservation = reservationService.createReservation(
                        currentUser.getId(), vehicleId, pickupDate, dropOffDate);
                System.out.println("Reservation confirmed! Reservation ID: " + reservation.getId());
            } else {
                System.out.println("Reservation cancelled.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleReservations() {
        while (true) {
            System.out.println("\n--- My Reservations ---");
            System.out.println("1. View reservations");
            System.out.println("2. Cancel reservation");
            System.out.println("3. Return to previous menu");
            System.out.print("Enter your choice: ");

            int choice = getUserChoice();
            switch (choice) {
                case 1:
                    viewReservations();
                    break;
                case 2:
                    cancelReservation();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void viewReservations() {
        List<Reservation> reservations = reservationService.getUserReservations(currentUser.getId());
        if (reservations.isEmpty()) {
            System.out.println("You have no reservations.");
            return;
        }

        System.out.println("\n=== Your Reservations ===");
        for (Reservation reservation : reservations) {
            Vehicle vehicle = vehicleService.getVehicleById(reservation.getVehicleId())
                    .orElseThrow(() -> new RuntimeException("Vehicle not found"));

            System.out.printf("Reservation ID: %d | Vehicle: %d %s %s | Pickup: %s | Drop-off: %s | Total: $%.2f%n",
                    reservation.getId(), vehicle.getYear(), vehicle.getCarType(), vehicle.getModel(),
                    dateFormat.format(reservation.getPickupDate()),
                    dateFormat.format(reservation.getDropOffDate()),
                    reservation.getTotalCharge());
        }
    }

    private void cancelReservation() {
        viewReservations();
        System.out.print("\nEnter reservation ID to cancel (0 to cancel): ");
        int reservationId = getUserChoice();
        if (reservationId == 0) return;

        try {
            reservationService.cancelReservation(reservationId);
            System.out.println("Reservation cancelled successfully.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    void handleAccountSettings() {
        while (true) {
            System.out.println("\n--- Account Settings ---");
            System.out.println("1. View account details");
            System.out.println("2. Edit account information");
            System.out.println("3. Delete account");
            System.out.println("4. Return to previous menu");
            System.out.print("Enter your choice: ");

            int choice = getUserChoice();
            switch (choice) {
                case 1:
                    viewAccountDetails();
                    break;
                case 2:
                    editAccountInformation();
                    break;
                case 3:
                    if (deleteAccount()) return;
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void viewAccountDetails() {
        System.out.println("\n--- Account Details ---");
        System.out.println("First Name: " + currentUser.getFirstName());
        System.out.println("Last Name: " + currentUser.getLastName());
        System.out.println("Email: " + currentUser.getEmail());
        System.out.println("Account Type: " + currentUser.getAccountType());
    }

    private void editAccountInformation() {
        while (true) {
            System.out.println("\n--- Edit Account Information ---");
            System.out.println("1. Change email");
            System.out.println("2. Change first name");
            System.out.println("3. Change last name");
            System.out.println("4. Change password");
            System.out.println("5. Return to previous menu");
            System.out.print("Enter your choice: ");

            int choice = getUserChoice();
            switch (choice) {
                case 1:
                    changeEmail();
                    break;
                case 2:
                    changeFirstName();
                    break;
                case 3:
                    changeLastName();
                    break;
                case 4:
                    changePassword();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void changeEmail() {
        System.out.print("Enter new email: ");
        String newEmail = scanner.nextLine();
        try {
            userService.updateEmail(currentUser.getId(), newEmail);
            System.out.println("Email updated successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void changeFirstName() {
        System.out.print("Enter new first name: ");
        String newFirstName = scanner.nextLine();
        try {
            userService.updateFirstName(currentUser.getId(), newFirstName);
            System.out.println("First name updated successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void changeLastName() {
        System.out.print("Enter new last name: ");
        String newLastName = scanner.nextLine();
        try {
            userService.updateLastName(currentUser.getId(), newLastName);
            System.out.println("Last name updated successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void changePassword() {
        System.out.print("Enter new password: ");
        String newPassword = scanner.nextLine();
        try {
            userService.updatePassword(currentUser.getId(), newPassword);
            System.out.println("Password updated successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private boolean deleteAccount() {
        System.out.print("Are you sure you want to delete your account? (Y/N): ");
        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("Y")) {
            try {
                userService.deleteUser(currentUser.getId());
                System.out.println("Account deleted successfully.");
                return true;
            } catch (Exception e) {
                System.out.println("Error deleting account: " + e.getMessage());
            }
        }
        return false;
    }

    private int getUserChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}