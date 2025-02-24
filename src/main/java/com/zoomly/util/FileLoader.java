package com.zoomly.util;

import com.zoomly.model.User;
import com.zoomly.model.Vehicle;
import com.zoomly.repository.UserRepository;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * FileLoader.java
 * This class handles loading and validating data from text files for users and vehicles.
 */

public class FileLoader {
    private final UserRepository userRepository;

    public FileLoader(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * method: loadUsers
     * parameters: String filePath - Path to the users text file
     * return: List<User> - List of valid users from the file
     * purpose: Loads and validates user data from a text file
     */
    public List<User> loadUsers(String filePath) throws IOException {
        List<User> users = new ArrayList<>();
        int lineNumber = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                try {
                    String[] fields = line.split(",");
                    if (fields.length != 5) {
                        System.out.println("Warning: Invalid number of fields at line " + lineNumber);
                        continue;
                    }

                    String firstName = fields[0].trim();
                    String lastName = fields[1].trim();
                    String email = fields[2].trim();
                    String password = fields[3].trim(); // Read and trim the password
                    String accountType = fields[4].trim();

                    // Create the User object
                    User user = new User(firstName, lastName, email, password, accountType);

                    // Save the user to the repository
                    userRepository.save(user);
                    System.out.println("Created and saved user: " + user);

                    users.add(user);
                } catch (Exception e) {
                    System.out.println("Warning: Error processing line " + lineNumber + ": " + e.getMessage());
                }
            }
        }
        return users;
    }

    /**
     * method: loadVehicles
     * parameters: String filePath - Path to the vehicles text file
     * return: List<Vehicle> - List of valid vehicles from the file
     * purpose: Loads and validates vehicle data from a text file
     */
    public static List<Vehicle> loadVehicles(String filePath) throws IOException {
        List<Vehicle> vehicles = new ArrayList<>();
        int lineNumber = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                try {
                    String[] fields = line.split(",");
                    if (fields.length != 5) {
                        System.out.println("Warning: Invalid number of fields at line " + lineNumber);
                        continue;
                    }

                    String carType = fields[0].trim();
                    String model = fields[1].trim();
                    int year = Integer.parseInt(fields[2].trim());
                    double mileage = Double.parseDouble(fields[3].trim());
                    double pricePerDay = Double.parseDouble(fields[4].trim());

                    // Validate all fields
                    if (!Validator.isValidCarType(carType)) {
                        System.out.println("Warning: Invalid car type at line " + lineNumber);
                        continue;
                    }
                    if (!Validator.isValidYear(year)) {
                        System.out.println("Warning: Invalid year at line " + lineNumber);
                        continue;
                    }
                    if (mileage < 0) {
                        System.out.println("Warning: Invalid mileage at line " + lineNumber);
                        continue;
                    }
                    if (pricePerDay <= 0) {
                        System.out.println("Warning: Invalid price per day at line " + lineNumber);
                        continue;
                    }

                    Vehicle vehicle = new Vehicle(0, carType, model, year, mileage, pricePerDay);
                    vehicles.add(vehicle);
                } catch (NumberFormatException e) {
                    System.out.println("Warning: Invalid number format at line " + lineNumber);
                } catch (Exception e) {
                    System.out.println("Warning: Error processing line " + lineNumber + ": " + e.getMessage());
                }
            }
        }
        return vehicles;
    }
}