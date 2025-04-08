package com.zoomly.dao;

import com.zoomly.model.Reservation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object (DAO) for interacting with the reservations table in the database.
 *
 * Provides methods for creating, retrieving, updating, and deleting reservation records.
 * This class connects to the database using the DatabaseConnection utility.
 *
 * Used by controller and service classes to manage reservation data.
 */
public class ReservationDao {

    /**
     * Adds a new reservation to the database.
     *
     * @param userId the ID of the user making the reservation
     * @param vehicleId the ID of the vehicle being reserved
     * @param pickupDate the start date of the reservation
     * @param dropOffDate the end date of the reservation
     * @param totalCharge the total cost of the reservation
     * @throws SQLException if a database access error occurs
     */
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

    /**
     * Retrieves a reservation by its ID.
     *
     * @param id the ID of the reservation
     * @return an Optional containing the reservation if found, or empty if not found
     */
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

    /**
     * Retrieves all reservations from the database.
     *
     * @return a list of all reservations
     */
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

    /**
     * Retrieves all reservations made by a specific user.
     *
     * @param userId the ID of the user
     * @return a list of reservations made by the user
     */
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

    /**
     * Retrieves all reservations for a specific vehicle.
     *
     * @param vehicleId the ID of the vehicle
     * @return a list of reservations for the vehicle
     */
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

    /**
     * Retrieves reservations that conflict with the given date range for a reservation.
     *
     * @param reservationId the ID of the reservation being checked
     * @param pickupDate the desired pickup date
     * @param dropOffDate the desired drop-off date
     * @return a list of conflicting reservations
     */
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

    /**
     * Updates an existing reservation's dates and total charge.
     *
     * @param id the ID of the reservation to update
     * @param pickupDate the new pickup date
     * @param dropOffDate the new drop-off date
     * @param totalCharge the new total charge
     * @throws SQLException if a database access error occurs
     */
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

    /**
     * Updates only the total charge of an existing reservation.
     *
     * @param reservationId the ID of the reservation
     * @param totalCharge the new total charge
     * @throws SQLException if a database access error occurs
     */
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

    /**
     * Updates only the pickup and drop-off dates of a reservation.
     *
     * @param reservationId the ID of the reservation
     * @param pickupDate the new pickup date
     * @param dropOffDate the new drop-off date
     * @throws SQLException if a database access error occurs
     */
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

    /**
     * Deletes a reservation from the database.
     *
     * @param id the ID of the reservation to delete
     * @throws SQLException if a database access error occurs
     */
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
