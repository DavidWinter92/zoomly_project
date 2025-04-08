package com.zoomly.dao;

import com.zoomly.model.Vehicle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * VehicleDao.java
 * Data Access Object (DAO) for interacting with the vehicles table in the database.
 * Provides methods for adding, retrieving, updating, and deleting vehicles.
 */
public class VehicleDao {

    /**
     * Adds a new vehicle to the database.
     *
     * @param vin vehicle identification number
     * @param make vehicle make
     * @param model vehicle model
     * @param year vehicle year
     * @param mileage vehicle mileage
     * @param pricePerDay daily rental price
     * @param imagePath file path of vehicle image
     * @param description vehicle description
     * @throws SQLException if insertion fails
     */
    public void addVehicle(String vin, String make, String model, int year, double mileage, double pricePerDay, String imagePath, String description) throws SQLException {
        String sql = "INSERT INTO vehicles (vin, make, model, year, mileage, price_per_day, image_path, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, vin.trim());
            statement.setString(2, make);
            statement.setString(3, model);
            statement.setInt(4, year);
            statement.setDouble(5, mileage);
            statement.setDouble(6, pricePerDay);
            statement.setString(7, imagePath);
            statement.setString(8, description);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            throw e;
        }
    }

    /**
     * Retrieves a vehicle by its ID.
     *
     * @param id vehicle ID
     * @return optional vehicle if found
     */
    public Optional<Vehicle> getVehicleById(int id) {
        String sql = "SELECT * FROM vehicles WHERE id = ?";
        Vehicle vehicle = null;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                vehicle = new Vehicle(
                        resultSet.getInt("id"),
                        resultSet.getString("vin"),
                        resultSet.getString("make"),
                        resultSet.getString("model"),
                        resultSet.getInt("year"),
                        resultSet.getDouble("mileage"),
                        resultSet.getDouble("price_per_day"),
                        resultSet.getString("image_path"),
                        resultSet.getString("description")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(vehicle);
    }

    /**
     * Retrieves all vehicles from the database.
     *
     * @return list of all vehicles
     * @throws SQLException if retrieval fails
     */
    public List<Vehicle> findAll() throws SQLException {
        String sql = "SELECT * FROM vehicles";
        List<Vehicle> vehicles = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Vehicle vehicle = new Vehicle(
                        resultSet.getInt("id"),
                        resultSet.getString("vin"),
                        resultSet.getString("make"),
                        resultSet.getString("model"),
                        resultSet.getInt("year"),
                        resultSet.getDouble("mileage"),
                        resultSet.getDouble("price_per_day"),
                        resultSet.getString("image_path"),
                        resultSet.getString("description")
                );
                vehicles.add(vehicle);
            }
        }

        return vehicles;
    }

    /**
     * Updates an existing vehicle in the database.
     *
     * @param id vehicle ID
     * @param vin updated VIN
     * @param make updated make
     * @param model updated model
     * @param year updated year
     * @param mileage updated mileage
     * @param pricePerDay updated daily rental price
     * @param imagePath updated image path
     * @param description updated description
     * @throws SQLException if update fails
     */
    public void updateVehicle(int id, String vin, String make, String model, int year, double mileage, double pricePerDay, String imagePath, String description) throws SQLException {
        String sql = "UPDATE vehicles SET vin = ?, make = ?, model = ?, year = ?, mileage = ?, price_per_day = ?, image_path = ?, description = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, vin.trim());
            statement.setString(2, make);
            statement.setString(3, model);
            statement.setInt(4, year);
            statement.setDouble(5, mileage);
            statement.setDouble(6, pricePerDay);
            statement.setString(7, imagePath);
            statement.setString(8, description);
            statement.setInt(9, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            throw e;
        }
    }

    /**
     * Deletes a vehicle from the database by its ID.
     *
     * @param id vehicle ID
     */
    public void deleteVehicle(int id) {
        String sql = "DELETE FROM vehicles WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves a vehicle by its VIN.
     *
     * @param vin vehicle identification number
     * @return optional vehicle if found
     */
    public Optional<Vehicle> getVehicleByVin(String vin) {
        String sql = "SELECT * FROM vehicles WHERE vin = ?";
        Vehicle vehicle = null;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, vin.trim());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                vehicle = new Vehicle(
                        resultSet.getInt("id"),
                        resultSet.getString("vin"),
                        resultSet.getString("make"),
                        resultSet.getString("model"),
                        resultSet.getInt("year"),
                        resultSet.getDouble("mileage"),
                        resultSet.getDouble("price_per_day"),
                        resultSet.getString("image_path"),
                        resultSet.getString("description")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(vehicle);
    }
}
