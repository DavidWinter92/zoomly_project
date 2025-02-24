package com.zoomly.service;

import com.zoomly.model.Vehicle;
import com.zoomly.repository.VehicleRepository;
import com.zoomly.util.Validator;
import java.util.List;
import java.util.Optional;

/**
 * VehicleService.java
 * This class provides the business logic for vehicle-related operations including
 * adding, updating, and managing vehicles in the rental system.
 */

public class VehicleService {
    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public Vehicle addVehicle(String carType, String model, int year,
                              double mileage, double pricePerDay) {
        if (!Validator.isValidCarType(carType)) {
            throw new IllegalArgumentException("Invalid car type");
        }
        if (!Validator.isValidYear(year)) {
            throw new IllegalArgumentException("Invalid year");
        }
        if (mileage < 0) {
            throw new IllegalArgumentException("Mileage cannot be negative");
        }
        if (pricePerDay <= 0) {
            throw new IllegalArgumentException("Price per day must be positive");
        }

        Vehicle vehicle = new Vehicle(0, carType, model, year, mileage, pricePerDay);
        return vehicleRepository.save(vehicle);
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public Optional<Vehicle> getVehicleById(int id) {
        return vehicleRepository.findById(id);
    }

    public List<Vehicle> getVehiclesByType(String carType) {
        return vehicleRepository.findByType(carType);
    }

    public void deleteVehicle(int id) {
        vehicleRepository.delete(id);
    }

    public Vehicle updateVehicle(Vehicle vehicle) {
        if (!vehicleRepository.findById(vehicle.getId()).isPresent()) {
            throw new IllegalArgumentException("Vehicle not found");
        }
        return vehicleRepository.save(vehicle);
    }
}