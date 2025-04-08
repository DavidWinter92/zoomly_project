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

    /**
     * Private constructor to initialize the VehicleService with a VehicleDao.
     */
    private VehicleService() {
        this.vehicleDao = new VehicleDao();
    }

    /**
     * Returns the singleton instance of the VehicleService.
     *
     * @return the singleton instance of the VehicleService.
     */
    public static VehicleService getInstance() {
        if (instance == null) {
            instance = new VehicleService();
        }
        return instance;
    }

    /**
     * Adds a new vehicle to the system after validating its data.
     *
     * @param vin The VIN of the vehicle.
     * @param make The make of the vehicle.
     * @param model The model of the vehicle.
     * @param year The year of the vehicle.
     * @param mileage The mileage of the vehicle.
     * @param pricePerDay The price per day for renting the vehicle.
     * @param imagePath The image path for the vehicle.
     * @param description A description of the vehicle.
     * @return The newly added vehicle.
     * @throws IllegalArgumentException If a vehicle with the same VIN already exists.
     * @throws RuntimeException If there is an error adding the vehicle to the database.
     */
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

    /**
     * Retrieves a vehicle by its VIN.
     *
     * @param vin The VIN of the vehicle to be retrieved.
     * @return An Optional containing the vehicle if found, or an empty Optional if not found.
     */
    public Optional<Vehicle> getVehicleByVin(String vin) {
        return vehicleDao.getVehicleByVin(vin);
    }

    /**
     * Retrieves all vehicles in the system.
     *
     * @return A list of all vehicles.
     * @throws RuntimeException If there is an error retrieving vehicles from the database.
     */
    public List<Vehicle> getAllVehicles() {
        try {
            return vehicleDao.findAll();
        } catch (SQLException e) {
            System.err.println("Error retrieving vehicles: " + e.getMessage());
            throw new RuntimeException("Failed to retrieve vehicles from the database.", e);
        }
    }

    /**
     * Retrieves a vehicle by its ID.
     *
     * @param vehicleId The ID of the vehicle to be retrieved.
     * @return An Optional containing the vehicle if found, or an empty Optional if not found.
     */
    public Optional<Vehicle> getVehicleById(int vehicleId) {
        try {
            return vehicleDao.getVehicleById(vehicleId);
        } catch (Exception e) {
            System.err.println("Error fetching vehicle by ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Updates the details of an existing vehicle.
     *
     * @param id The ID of the vehicle to be updated.
     * @param vin The VIN of the vehicle.
     * @param make The make of the vehicle.
     * @param model The model of the vehicle.
     * @param year The year of the vehicle.
     * @param mileage The mileage of the vehicle.
     * @param pricePerDay The price per day for renting the vehicle.
     * @param imagePath The image path for the vehicle.
     * @param description A description of the vehicle.
     * @throws IllegalArgumentException If there is an error updating the vehicle.
     */
    public void updateVehicle(int id, String vin, String make, String model, int year, double mileage, double pricePerDay, String imagePath, String description) {
        try {
            vehicleDao.updateVehicle(id, vin, make, model, year, mileage, pricePerDay, imagePath, description);
        } catch (SQLException e) {
            throw new IllegalArgumentException("Error updating vehicle: " + e.getMessage());
        }
    }

    /**
     * Deletes a vehicle from the system.
     *
     * @param id The ID of the vehicle to be deleted.
     */
    public void deleteVehicle(int id) {
        vehicleDao.deleteVehicle(id);
    }

    /**
     * Returns the VehicleDao instance used by the service.
     *
     * @return The VehicleDao instance.
     */
    public VehicleDao getVehicleDao() {
        return vehicleDao;
    }
}
