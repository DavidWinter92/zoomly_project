//package com.zoomly.unitTests;
//
//import com.zoomly.model.Vehicle;
//import com.zoomly.repository.VehicleRepository;
//import com.zoomly.service.VehicleService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import java.util.List;
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * VehicleServiceUnitTest.java
// * This class tests the CRUD operation for vehicle management.
// */
//
//public class VehicleServiceUnitTest {
//    private VehicleService vehicleService;
//    private VehicleRepository vehicleRepository;
//
//    @BeforeEach
//    void setUp() {
//        vehicleRepository = new VehicleRepository();
//        vehicleService = new VehicleService(vehicleRepository);
//    }
//
//    @Test
//    void testAddVehicle() {
//        // Arrange
//        String carType = "SUV";
//        String model = "Toyota RAV4";
//        int year = 2024;
//        double mileage = 25000.0;
//        double pricePerDay = 75.0;
//
//        // Act
//        Vehicle vehicle = vehicleService.addVehicle(carType, model, year, mileage, pricePerDay);
//
//        // Assert
//        assertNotNull(vehicle);
//        assertEquals(carType, vehicle.getCarType());
//        assertEquals(model, vehicle.getModel());
//        assertEquals(year, vehicle.getYear());
//        assertEquals(mileage, vehicle.getMileage(), 0.01);
//        assertEquals(pricePerDay, vehicle.getPricePerDay(), 0.01);
//    }
//
//    @Test
//    void testGetVehicleById() {
//        // Arrange
//        Vehicle vehicle = vehicleService.addVehicle("SUV", "Toyota RAV4", 2024, 25000.0, 75.0);
//        int vehicleId = vehicle.getId();
//
//        // Act
//        Vehicle retrievedVehicle = vehicleService.getVehicleById(vehicleId)
//                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
//
//        // Assert
//        assertEquals(vehicle, retrievedVehicle);
//    }
//
//    @Test
//    void testGetAllVehicles() {
//        // Arrange
//        vehicleService.addVehicle("SUV", "Toyota RAV4", 2024, 25000.0, 75.0);
//        vehicleService.addVehicle("Sedan", "Honda Civic", 2023, 30000.0, 50.0);
//
//        // Act
//        List<Vehicle> vehicles = vehicleService.getAllVehicles();
//
//        // Assert
//        assertNotNull(vehicles);
//        assertEquals(2, vehicles.size());
//    }
//
//
//    @Test
//    void testUpdateVehicle() {
//        // Arrange
//        Vehicle originalVehicle = new Vehicle(0, "SUV", "Toyota RAV4", 2024, 25000.0, 75.0);
//        vehicleRepository.save(originalVehicle);
//
//        Vehicle updatedVehicle = new Vehicle(
//                originalVehicle.getId(),
//                "Truck",
//                "Ford F-150",
//                2025,
//                20000.0,
//                90.0
//        );
//
//        // Act
//        Vehicle result = vehicleService.updateVehicle(updatedVehicle);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(updatedVehicle.getCarType(), result.getCarType());
//        assertEquals(updatedVehicle.getModel(), result.getModel());
//        assertEquals(updatedVehicle.getYear(), result.getYear());
//        assertEquals(updatedVehicle.getMileage(), result.getMileage(), 0.01);
//        assertEquals(updatedVehicle.getPricePerDay(), result.getPricePerDay(), 0.01);
//    }
//
//    @Test
//    void testDeleteVehicle() {
//        // Arrange
//        Vehicle vehicle = vehicleService.addVehicle("SUV", "Toyota RAV4", 2024, 25000.0, 75.0);
//        int vehicleId = vehicle.getId();
//
//        // Act
//        vehicleService.deleteVehicle(vehicleId);
//
//        // Assert
//        assertFalse(vehicleService.getVehicleById(vehicleId).isPresent());
//    }
//
//    @Test
//    void testUpdateNonExistentVehicle() {
//        // Arrange
//        Vehicle nonExistentVehicle = new Vehicle(999, "Nonexistent", "Unknown", 2020, 100000.0, 500.0);
//
//        // Act & Assert
//        assertThrows(IllegalArgumentException.class, () ->
//                vehicleService.updateVehicle(nonExistentVehicle)
//        );
//    }
//}