package com.zoomly.repository;

import com.zoomly.model.Vehicle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * VehicleRepository.java
 * Repository class for Vehicle data access.
 * Currently implements in-memory storage, can be extended for database implementation.
 */

public class VehicleRepository {
    private final Map<Integer, Vehicle> vehicles = new HashMap<>();
    private int nextId = 1;

    public Vehicle save(Vehicle vehicle) {
        if (vehicle.getId() == 0) {
            vehicle.setId(nextId++);
        }
        vehicles.put(vehicle.getId(), vehicle);
        return vehicle;
    }

    public Optional<Vehicle> findById(int id) {
        return Optional.ofNullable(vehicles.get(id));
    }

    public List<Vehicle> findAll() {
        return new ArrayList<>(vehicles.values());
    }

    public List<Vehicle> findByType(String carType) {
        return vehicles.values().stream()
                .filter(vehicle -> vehicle.getCarType().equals(carType))
                .toList();
    }

    public void delete(int id) {
        vehicles.remove(id);
    }
}