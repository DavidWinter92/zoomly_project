package com.zoomly.service;

import com.zoomly.dao.VehicleDao;
import com.zoomly.model.Vehicle;
import com.zoomly.util.VehicleValidator;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * VehicleService.java
 * Service class for managing vehicle-related operations.
 * Provides methods for adding, retrieving, updating, and deleting vehicles.
 * Ensures data validation and handles exceptions for vehicle data processing.
 */

public class VehicleService {
    private static VehicleService instance;
    private final VehicleDao vehicleDao;

    private VehicleService() {
        this.vehicleDao = new VehicleDao();
    }

    public static VehicleService getInstance() {
        if (instance == null) {
            instance = new VehicleService();
        }
        return instance;
    }

    public Vehicle addVehicle(String vin, String make, String model, int year, double mileage, double pricePerDay, String imagePath, String description) {
        VehicleValidator.validate(vin, make, model, year, mileage, pricePerDay);

        if (getVehicleByVin(vin).isPresent()) {
            throw new IllegalArgumentException("A vehicle with VIN " + vin + " already exists.");
        }

        Vehicle vehicle = new Vehicle(0, vin, make, model, year, mileage, pricePerDay, imagePath, description);

        try {
            vehicleDao.addVehicle(vin, make, model, year, mileage, pricePerDay, imagePath, description);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add vehicle to the database: " + e.getMessage());
        }

        return vehicle;
    }

    public Optional<Vehicle> getVehicleByVin(String vin) {
        return vehicleDao.getVehicleByVin(vin);
    }

    public List<Vehicle> getAllVehicles() {
        try {
            return vehicleDao.findAll();
        } catch (SQLException e) {
            System.err.println("Error retrieving vehicles: " + e.getMessage());
            throw new RuntimeException("Failed to retrieve vehicles from the database.", e);
        }
    }

    public Optional<Vehicle> getVehicleById(int vehicleId) {
        try {
            return vehicleDao.getVehicleById(vehicleId);
        } catch (Exception e) {
            System.err.println("Error fetching vehicle by ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    public void updateVehicle(int id, String vin, String make, String model, int year, double mileage, double pricePerDay, String imagePath, String description) {
        try {
            vehicleDao.updateVehicle(id, vin, make, model, year, mileage, pricePerDay, imagePath, description);
        } catch (SQLException e) {
            throw new IllegalArgumentException("Error updating vehicle: " + e.getMessage());
        }
    }

    public void deleteVehicle(int id) {
        vehicleDao.deleteVehicle(id);
    }

    public VehicleDao getVehicleDao() {
        return vehicleDao;
    }
}
