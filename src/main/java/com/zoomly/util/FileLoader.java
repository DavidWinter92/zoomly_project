package com.zoomly.util;

import com.zoomly.dao.UserDao;
import com.zoomly.dao.VehicleDao;
import com.zoomly.model.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * FileLoader.java
 * This class handles loading and validating data from text files for users and vehicles.
 */

public class FileLoader {
    private final UserDao userDao;
    private final VehicleDao vehicleDao;

    public FileLoader(UserDao userDao, VehicleDao vehicleDao) {
        this.userDao = userDao;
        this.vehicleDao = vehicleDao;
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

                try {
                    User newUser = new User(0, firstName, lastName, email, password, accountType);
                    UserValidator.validate(newUser);

                    userDao.addUser(firstName, lastName, email, password, accountType);
                    System.out.println("User registered: " + firstName + " " + lastName);
                } catch (IllegalArgumentException e) {
                    System.out.println("Warning: " + e.getMessage() + " for " + email + ". Skipping user.");
                } catch (Exception e) {
                    System.out.println("Warning: " + e.getMessage());
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
     * return: void
     * purpose: Loads and validates vehicle data from a text file.
     */
    public void loadVehicles(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length == 8) {
                String vin = parts[0].trim();
                String make = parts[1].trim();
                String model = parts[2].trim();
                int year = Integer.parseInt(parts[3].trim());
                double mileage = Double.parseDouble(parts[4].trim());
                double pricePerDay = Double.parseDouble(parts[5].trim());
                String imagePath = parts[6].trim();
                String description = parts[7].trim();

                try {
                    vehicleDao.addVehicle(vin, make, model, year, mileage, pricePerDay, imagePath, description);
                    System.out.println("Vehicle registered: " + make + " " + model + " (VIN: " + vin + ")");
                } catch (Exception e) {
                    System.out.println("Warning: " + e.getMessage());
                }
            } else {
                System.out.println("Warning: Invalid line format: " + line);
            }
        }
        reader.close();
    }
}