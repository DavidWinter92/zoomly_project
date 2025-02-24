package com.zoomly.main;

import com.zoomly.model.User;
import com.zoomly.model.Vehicle;
import com.zoomly.repository.UserRepository;
import com.zoomly.repository.VehicleRepository;
import com.zoomly.ui.MainMenu;
import com.zoomly.ui.ConsoleUI;
import com.zoomly.util.FileLoader;

import java.io.IOException;
import java.util.List;

/**
 * David Winter
 * CEN-3024C
 * Software Development 1
 * 02/21/2025
 *
 * ZoomlyApp.java
 * This class serves as the main entry point for the Zoomly Vehicle Rental System.
 * It initializes the data from text files and starts the application.
 */

public class ZoomlyApp {

    // Class variables to hold loaded users and vehicles
    private static List<User> loadedUsers;
    private static List<Vehicle> loadedVehicles;

    public static void main(String[] args) {
        try {
            // Initialize repositories
            UserRepository userRepository = new UserRepository();
            VehicleRepository vehicleRepository = new VehicleRepository();

            ConsoleUI consoleUI = new ConsoleUI();

            // Create an instance of FileLoader with the user repository
            FileLoader fileLoader = new FileLoader(userRepository);

            // Load data from text files
            loadedUsers = fileLoader.loadUsers("users.txt");
            loadedUsers.forEach(userRepository::save);

            loadedVehicles = FileLoader.loadVehicles("vehicles.txt");
            loadedVehicles.forEach(vehicleRepository::save);

            System.out.println("Loaded " + loadedVehicles.size() + " vehicles.");

            // Add test user
            User testUser = new User("Dave", "Wint", "admin2@gmail.com", "password1!", "administrator");
            userRepository.save(testUser);
            System.out.println("Saved user: " + testUser);

            System.out.println("\n*** Ignore above logs. FileLoader does not function correctly ***\nMain purpose was to save time with testing, however I was unable to get this feature to work. Instead proceed by registering a new admin user before using the user account:\nFirsName\nLastName\nadmin1@_____.com\npassword1!\nadministrator\n\n  Test should begin by registering and using an admin account to upload files from root directory. When the account has been registered you may begin testing features before setting up for Phase 3 in Phase 2.");


            // Start the application with the main menu
            MainMenu mainMenu = new MainMenu(consoleUI, userRepository, vehicleRepository);
            mainMenu.start();

        } catch (IOException e) {
            System.out.println("Error loading data files: " + e.getMessage());
            System.out.println("Please ensure 'users.txt' and 'vehicles.txt' exist in the application directory.");
            System.exit(1);
        }
    }
}