package com.zoomly.dao;

import com.zoomly.model.Reservation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * ReservationDao.java
 * Data Access Object (DAO) for interacting with the reservations table in the database.
 * Provides methods for adding, retrieving, updating, and deleting reservations.
 */

public class ReservationDao {

    public void addReservation(int userId, int vehicleId, Date pickupDate, Date dropOffDate, double totalCharge) throws SQLException {
        String sql = "INSERT INTO reservations (user_id, vehicle_id, pickup_date, dropoff_date, total_charge) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setInt(2, vehicleId);
            statement.setDate(3, pickupDate);
            statement.setDate(4, dropOffDate);
            statement.setDouble(5, totalCharge);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public Optional<Reservation> getReservationById(int id) {
        String sql = "SELECT * FROM reservations WHERE id = ?";
        Reservation reservation = null;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                reservation = new Reservation(
                        resultSet.getInt("id"),
                        resultSet.getInt("user_id"),
                        resultSet.getInt("vehicle_id"),
                        resultSet.getDate("pickup_date"),
                        resultSet.getDate("dropoff_date"),
                        resultSet.getDouble("total_charge")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(reservation);
    }

    public List<Reservation> getAllReservations() {
        String sql = "SELECT * FROM reservations";
        List<Reservation> reservations = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Reservation reservation = new Reservation(
                        resultSet.getInt("id"),
                        resultSet.getInt("user_id"),
                        resultSet.getInt("vehicle_id"),
                        resultSet.getDate("pickup_date"),
                        resultSet.getDate("dropoff_date"),
                        resultSet.getDouble("total_charge")
                );
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservations;
    }

    public List<Reservation> getReservationsByUserId(int userId) {
        String sql = "SELECT * FROM reservations WHERE user_id = ?";
        List<Reservation> reservations = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Reservation reservation = new Reservation(
                        resultSet.getInt("id"),
                        resultSet.getInt("user_id"),
                        resultSet.getInt("vehicle_id"),
                        resultSet.getDate("pickup_date"),
                        resultSet.getDate("dropoff_date"),
                        resultSet.getDouble("total_charge")
                );
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservations;
    }

    public List<Reservation> getReservationsByVehicleId(int vehicleId) {
        String sql = "SELECT * FROM reservations WHERE vehicle_id = ?";
        List<Reservation> reservations = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, vehicleId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Reservation reservation = new Reservation(
                        resultSet.getInt("id"),
                        resultSet.getInt("user_id"),
                        resultSet.getInt("vehicle_id"),
                        resultSet.getDate("pickup_date"),
                        resultSet.getDate("dropoff_date"),
                        resultSet.getDouble("total_charge")
                );
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservations;
    }

    public List<Reservation> getConflictingReservations(int reservationId, Date pickupDate, Date dropOffDate) {
        String sql = "SELECT * FROM reservations WHERE vehicle_id = ? AND id != ? AND " +
                "(pickup_date < ? AND dropoff_date > ?)";
        List<Reservation> conflictingReservations = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, reservationId);
            statement.setInt(2, reservationId);
            statement.setDate(3, dropOffDate);
            statement.setDate(4, pickupDate);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Reservation reservation = new Reservation(
                        resultSet.getInt("id"),
                        resultSet.getInt("user_id"),
                        resultSet.getInt("vehicle_id"),
                        resultSet.getDate("pickup_date"),
                        resultSet.getDate("dropoff_date"),
                        resultSet.getDouble("total_charge")
                );
                conflictingReservations.add(reservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conflictingReservations;
    }

    public void updateReservation(int id, Date pickupDate, Date dropOffDate, double totalCharge) throws SQLException {
        String updateSQL = "UPDATE reservations SET pickup_date = ?, dropoff_date = ?, total_charge = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(updateSQL)) {

            statement.setDate(1, pickupDate);
            statement.setDate(2, dropOffDate);
            statement.setDouble(3, totalCharge);
            statement.setInt(4, id);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Updating reservation failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void updateTotalCharge(int reservationId, double totalCharge) throws SQLException {
        String updateSQL = "UPDATE reservations SET total_charge = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(updateSQL)) {

            statement.setDouble(1, totalCharge);
            statement.setInt(2, reservationId);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Updating total charge failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }


    public void updateReservationDates(int reservationId, Date pickupDate, Date dropOffDate) throws SQLException {
        String updateSQL = "UPDATE reservations SET pickup_date = ?, dropoff_date = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(updateSQL)) {

            statement.setDate(1, pickupDate);
            statement.setDate(2, dropOffDate);
            statement.setInt(3, reservationId);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Updating reservation dates failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void deleteReservation(int id) throws SQLException {
        String sql = "DELETE FROM reservations WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
