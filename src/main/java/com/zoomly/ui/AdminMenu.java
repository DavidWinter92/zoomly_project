package com.zoomly.ui;

import com.zoomly.model.User;
import com.zoomly.model.Vehicle;
import com.zoomly.model.Reservation;
import com.zoomly.service.UserService;
import com.zoomly.service.VehicleService;
import com.zoomly.service.ReservationService;
import com.zoomly.util.FileLoader;
import com.zoomly.repository.UserRepository;
import java.util.Scanner;
import java.util.List;
import java.text.SimpleDateFormat;

/**
 * AdminMenu.java
 * This class handles the administrator menu interface, providing options for
 * managing vehicles, users, and reservations.
 */

public class AdminMenu {
    private final Scanner scanner;
    private final User currentUser;
    private final UserService userService;
    private final VehicleService vehicleService;
    private final ReservationService reservationService;
    private final SimpleDateFormat dateFormat;

    public AdminMenu(User currentUser, UserService userService,
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
            System.out.println("\n--- Administrator Menu ---");
            System.out.println("1. Vehicle Management");
            System.out.println("2. User Management");
            System.out.println("3. Reservation Management");
            System.out.println("4. Account Settings");
            System.out.println("5. Logout");
            System.out.print("Enter your choice: ");

            int choice = getUserChoice();
            switch (choice) {
                case 1:
                    handleVehicleManagement();
                    break;
                case 2:
                    handleUserManagement();
                    break;
                case 3:
                    handleReservationManagement();
                    break;
                case 4:
                    handleAccountSettings();
                    break;
                case 5:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void handleVehicleManagement() {
        while (true) {
            System.out.println("\n--- Vehicle Management ---");
            System.out.println("1. View all vehicles");
            System.out.println("2. Add vehicle");
            System.out.println("3. Update vehicle");
            System.out.println("4. Delete vehicle");
            System.out.println("5. Return to previous menu");
            System.out.print("Enter your choice: ");

            int choice = getUserChoice();
            switch (choice) {
                case 1:
                    viewAllVehicles();
                    break;
                case 2:
                    handleAddVehicle();
                    break;
                case 3:
                    handleUpdateVehicle();
                    break;
                case 4:
                    handleDeleteVehicle();
                    return;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void viewAllVehicles() {
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        System.out.println("\n--- All Vehicles ---");
        for (Vehicle vehicle : vehicles) {
            System.out.printf("ID: %d | %d %s %s | Mileage: %.1f | Price/Day: $%.2f%n",
                    vehicle.getId(), vehicle.getYear(), vehicle.getCarType(),
                    vehicle.getModel(), vehicle.getMileage(), vehicle.getPricePerDay());
        }
    }

    private void handleAddVehicle() {
        System.out.println("\n--- Add Vehicle ---");
        System.out.println("1. Upload from file");
        System.out.println("2. Manual input");
        System.out.println("3. Return to previous menu");
        System.out.print("Enter your choice: ");

        int choice = getUserChoice();
        switch (choice) {
            case 1:
                handleVehicleFileUpload();
                break;
            case 2:
                addVehicleManually();
                break;
            case 3:
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private void handleVehicleFileUpload() {
        System.out.print("Enter the path to the vehicle file: ");
        String filePath = scanner.nextLine();
        try {
            List<Vehicle> vehicles = FileLoader.loadVehicles(filePath);
            for (Vehicle vehicle : vehicles) {
                vehicleService.addVehicle(vehicle.getCarType(), vehicle.getModel(),
                        vehicle.getYear(), vehicle.getMileage(), vehicle.getPricePerDay());
            }
            System.out.println("Vehicles added successfully from file.");
        } catch (Exception e) {
            System.out.println("Error loading vehicles: " + e.getMessage());
        }
    }

    private void addVehicleManually() {
        try {
            System.out.print("Enter car type (SUV/Sedan/Truck): ");
            String carType = scanner.nextLine();

            System.out.print("Enter model: ");
            String model = scanner.nextLine();

            System.out.print("Enter year: ");
            int year = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter mileage: ");
            double mileage = Double.parseDouble(scanner.nextLine());

            System.out.print("Enter price per day: ");
            double pricePerDay = Double.parseDouble(scanner.nextLine());

            vehicleService.addVehicle(carType, model, year, mileage, pricePerDay);
            System.out.println("Vehicle added successfully.");
        } catch (Exception e) {
            System.out.println("Error adding vehicle: " + e.getMessage());
        }
    }

    private void handleUpdateVehicle() {
        viewAllVehicles();
        System.out.print("\nEnter vehicle ID to update (0 to cancel): ");
        int vehicleId = getUserChoice();
        if (vehicleId == 0) return;

        vehicleService.getVehicleById(vehicleId).ifPresentOrElse(
                vehicle -> updateVehicleFields(vehicle),
                () -> System.out.println("Vehicle not found.")
        );
    }
    private void handleDeleteVehicle() {
        viewAllVehicles();
        System.out.print("\nEnter vehicle ID to delete (0 to cancel): ");
        int vehicleId = getUserChoice();
        if (vehicleId == 0) return;

        try {
            vehicleService.deleteVehicle(vehicleId);
            System.out.println("Vehicle deleted successfully.");
        } catch (Exception e) {
            System.out.println("Error deleting vehicle: " + e.getMessage());
        }
    }

    private void updateVehicleFields(Vehicle vehicle) {
        while (true) {
            System.out.println("\n--- Update Vehicle ---");
            System.out.println("1. Update car type");
            System.out.println("2. Update model");
            System.out.println("3. Update year");
            System.out.println("4. Update mileage");
            System.out.println("5. Update price per day");
            System.out.println("6. Return to previous menu");
            System.out.print("Enter your choice: ");

            int choice = getUserChoice();
            try {
                switch (choice) {
                    case 1:
                        System.out.print("Enter new car type: ");
                        vehicle.setCarType(scanner.nextLine());
                        break;
                    case 2:
                        System.out.print("Enter new model: ");
                        vehicle.setModel(scanner.nextLine());
                        break;
                    case 3:
                        System.out.print("Enter new year: ");
                        vehicle.setYear(Integer.parseInt(scanner.nextLine()));
                        break;
                    case 4:
                        System.out.print("Enter new mileage: ");
                        vehicle.setMileage(Double.parseDouble(scanner.nextLine()));
                        break;
                    case 5:
                        System.out.print("Enter new price per day: ");
                        vehicle.setPricePerDay(Double.parseDouble(scanner.nextLine()));
                        break;
                    case 6:
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        continue;
                }
                vehicleService.updateVehicle(vehicle);
                System.out.println("Vehicle updated successfully.");
            } catch (Exception e) {
                System.out.println("Error updating vehicle: " + e.getMessage());
            }
        }
    }

    private void handleUserManagement() {
        while (true) {
            System.out.println("\n--- User Management ---");
            System.out.println("1. View all users");
            System.out.println("2. Add users");
            System.out.println("3. Update user");
            System.out.println("4. Delete user");
            System.out.println("5. Return to previous menu");
            System.out.print("Enter your choice: ");

            int choice = getUserChoice();
            switch (choice) {
                case 1:
                    viewAllUsers();
                    break;
                case 2:
                    handleAddUsers();
                    break;
                case 3:
                    handleUpdateUser();
                    break;
                case 4:
                    handleDeleteUser();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void viewAllUsers() {
        List<User> users = userService.getAllUsers();
        System.out.println("\n--- All Users ---");
        for (User user : users) {
            System.out.printf("ID: %d | %s %s | Email: %s | Account Type: %s%n",
                    user.getId(), user.getFirstName(), user.getLastName(),
                    user.getEmail(), user.getAccountType());
        }
    }

    private void handleAddUsers() {
        System.out.println("\n--- Add Users ---");
        System.out.println("1. Upload from file");
        System.out.println("2. Manual input");
        System.out.println("3. Return to previous menu");
        System.out.print("Enter your choice: ");

        int choice = getUserChoice();
        switch (choice) {
            case 1:
                handleUserFileUpload();
                break;
            case 2:
                addUserManually();
                break;
            case 3:
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private void handleUserFileUpload() {
        System.out.print("Enter the path to the user file: ");
        String filePath = scanner.nextLine();

        UserRepository userRepository = new UserRepository();
        FileLoader fileLoader = new FileLoader(userRepository);

        try {
            List<User> users = fileLoader.loadUsers(filePath);
            for (User user : users) {
                userService.registerUser(user.getFirstName(), user.getLastName(),
                        user.getEmail(), user.getPassword(), user.getAccountType());
            }
            System.out.println("Users added successfully from file.");
        } catch (Exception e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
    }

    private void addUserManually() {
        try {
            System.out.print("Enter first name: ");
            String firstName = scanner.nextLine();

            System.out.print("Enter last name: ");
            String lastName = scanner.nextLine();

            System.out.print("Enter email: ");
            String email = scanner.nextLine();

            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            System.out.print("Enter account type (user/administrator): ");
            String accountType = scanner.nextLine();

            userService.registerUser(firstName, lastName, email, password, accountType);
            System.out.println("User added successfully.");
        } catch (Exception e) {
            System.out.println("Error adding user: " + e.getMessage());
        }
    }

    private void handleUpdateUser() {
        viewAllUsers();
        System.out.print("\nEnter user ID to update (0 to cancel): ");
        int userId = getUserChoice();
        if (userId == 0) return;

        userService.getUserById(userId).ifPresentOrElse(
                user -> updateUserFields(user),
                () -> System.out.println("User not found.")
        );
    }

    private void handleDeleteUser() {
        viewAllUsers();
        System.out.print("\nEnter user ID to delete (0 to cancel): ");
        int userId = getUserChoice();
        if (userId == 0) return;

        try {
            userService.deleteUser(userId);
            System.out.println("User deleted successfully.");
        } catch (Exception e) {
            System.out.println("Error deleting user: " + e.getMessage());
        }
    }

    private void updateUserFields(User user) {
        while (true) {
            System.out.println("\n--- Update User ---");
            System.out.println("1. Update email");
            System.out.println("2. Update first name");
            System.out.println("3. Update last name");
            System.out.println("4. Update password");
            System.out.println("5. Update account type");
            System.out.println("6. Return to previous menu");
            System.out.print("Enter your choice: ");

            int choice = getUserChoice();
            try {
                switch (choice) {
                    case 1:
                        System.out.print("Enter new email: ");
                        userService.updateEmail(user.getId(), scanner.nextLine());
                        break;
                    case 2:
                        System.out.print("Enter new first name: ");
                        userService.updateFirstName(user.getId(), scanner.nextLine());
                        break;
                    case 3:
                        System.out.print("Enter new last name: ");
                        userService.updateLastName(user.getId(), scanner.nextLine());
                        break;
                    case 4:
                        System.out.print("Enter new password: ");
                        userService.updatePassword(user.getId(), scanner.nextLine());
                        break;
                    case 5:
                        System.out.print("Enter new account type (user/administrator): ");
                        userService.updateAccountType(user.getId(), scanner.nextLine());
                        break;
                    case 6:
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        continue;
                }
                System.out.println("User updated successfully.");
            } catch (Exception e) {
                System.out.println("Error updating user: " + e.getMessage());
            }
        }
    }

    private void handleReservationManagement() {
        while (true) {
            System.out.println("\n--- Reservation Management ---");
            System.out.println("1. View all reservations");
            System.out.println("2. Update reservation");
            System.out.println("3. Delete reservation");
            System.out.println("4. Return to previous menu");
            System.out.print("Enter your choice: ");

            int choice = getUserChoice();
            switch (choice) {
                case 1:
                    viewAllReservations();
                    break;
                case 2:
                    handleUpdateReservation();
                    break;
                case 3:
                    handleDeleteReservation();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void viewAllReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        System.out.println("\n=== All Reservations ===");
        for (Reservation reservation : reservations) {
            Vehicle vehicle = vehicleService.getVehicleById(reservation.getVehicleId())
                    .orElseThrow(() -> new RuntimeException("Vehicle not found"));
            User user = userService.getUserById(reservation.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            System.out.printf("ID: %d | User: %s %s | Vehicle: %d %s %s | Pickup: %s | Drop-off: %s | Total: $%.2f%n",
                    reservation.getId(), user.getFirstName(), user.getLastName(),
                    vehicle.getYear(), vehicle.getCarType(), vehicle.getModel(),
                    dateFormat.format(reservation.getPickupDate()),
                    dateFormat.format(reservation.getDropOffDate()),
                    reservation.getTotalCharge());
        }
    }

    private void handleUpdateReservation() {
        viewAllReservations();
        System.out.print("\nEnter reservation ID to update (0 to cancel): ");
        int reservationId = getUserChoice();
        if (reservationId == 0) return;

        reservationService.getReservationById(reservationId).ifPresentOrElse(
                reservation -> updateReservationFields(reservation),
                () -> System.out.println("Reservation not found.")
        );
    }

    private void updateReservationFields(Reservation reservation) {
        while (true) {
            System.out.println("\n--- Update Reservation ---");
            System.out.println("1. Update pickup date");
            System.out.println("2. Update drop-off date");
            System.out.println("3. Return to previous menu");
            System.out.print("Enter your choice: ");

            int choice = getUserChoice();
            try {
                switch (choice) {
                    case 1:
                        System.out.print("Enter new pickup date (MM/dd/yyyy): ");
                        reservation.setPickupDate(dateFormat.parse(scanner.nextLine()));
                        break;
                    case 2:
                        System.out.print("Enter new drop-off date (MM/dd/yyyy): ");
                        reservation.setDropOffDate(dateFormat.parse(scanner.nextLine()));
                        break;
                    case 3:
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        continue;
                }

                Vehicle vehicle = vehicleService.getVehicleById(reservation.getVehicleId())
                        .orElseThrow(() -> new RuntimeException("Vehicle not found"));
                long days = (reservation.getDropOffDate().getTime() -
                        reservation.getPickupDate().getTime()) / (1000 * 60 * 60 * 24);
                reservation.setTotalCharge(days * vehicle.getPricePerDay());

                System.out.println("Reservation updated successfully.");
            } catch (Exception e) {
                System.out.println("Error updating reservation: " + e.getMessage());
            }
        }
    }

    private void handleDeleteReservation() {
        viewAllReservations();
        System.out.print("\nEnter reservation ID to delete (0 to cancel): ");
        int reservationId = getUserChoice();
        if (reservationId == 0) return;

        try {
            reservationService.cancelReservation(reservationId);
            System.out.println("Reservation deleted successfully.");
        } catch (Exception e) {
            System.out.println("Error deleting reservation: " + e.getMessage());
        }
    }

    private void handleAccountSettings() {
        UserMenu userMenu = new UserMenu(currentUser, userService, vehicleService, reservationService);
        userMenu.handleAccountSettings();
    }

    private int getUserChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}