package com.zoomly.util;

import com.zoomly.model.User;
import com.zoomly.model.Vehicle;
import com.zoomly.repository.UserRepository;
import com.zoomly.repository.VehicleRepository;
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
    private final VehicleRepository vehicleRepository;

    public FileLoader(UserRepository userRepository, VehicleRepository vehicleRepository) {
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
    }

    /**
     * method: loadUsers
     * parameters: String filePath - Path to the users text file
     * return: void
     * purpose: Loads and validates user data from a text file.
     */
    public void loadUsers(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length == 5) {
                String firstName = parts[0].trim();
                String lastName = parts[1].trim();
                String email = parts[2].trim();
                String password = parts[3].trim();
                String accountType = parts[4].trim();

                if (!Validator.isValidEmail(email)) {
                    System.out.println("Warning: Invalid email format for " + email + ". Skipping user.");
                    continue;
                }

                int userId = userRepository.getNextId();
                User user = new User(firstName, lastName, email, password, accountType, userId);
                User savedUser = userRepository.save(user);

                if (savedUser == null) {
                    System.out.println("Warning: Email " + email + " already exists. Skipping user.");
                } else {
                    System.out.println("User registered: " + savedUser);
                }
            } else {
                System.out.println("Warning: Invalid line format: " + line);
            }
        }
        reader.close();
    }

    /**
     * method: loadVehicles
     * parameters: String filePath - Path to the vehicles text file
     * return: List<Vehicle> - List of vehicles loaded from the file
     * purpose: Loads and validates vehicle data from a text file.
     */
    public List<Vehicle> loadVehicles(String filePath) throws IOException {
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
                    vehicleRepository.save(vehicle); // Save to the repository
                    vehicles.add(vehicle);
                    System.out.println("Created and saved vehicle: " + vehicle);
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