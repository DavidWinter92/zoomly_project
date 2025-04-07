package com.zoomly.service;

import com.zoomly.dao.ReservationDao;
import com.zoomly.dao.VehicleDao;
import com.zoomly.model.Reservation;
import com.zoomly.model.Vehicle;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * ReservationService.java
 * Service class for managing reservations.
 * Provides methods to create, update, cancel, and validate reservations,
 * as well as checking for conflicts with existing reservations.
 */

public class ReservationService {
    private static ReservationService instance;
    private final ReservationDao reservationDao;
    private final VehicleDao vehicleDao;

    private ReservationService() {
        this.reservationDao = new ReservationDao();
        this.vehicleDao = new VehicleDao();
    }

    public static ReservationService getInstance() {
        if (instance == null) {
            instance = new ReservationService();
        }
        return instance;
    }

    public List<Reservation> getAllReservations() {
        return reservationDao.getAllReservations();
    }

    public List<Reservation> getUserReservations(int userId) {
        return reservationDao.getReservationsByUserId(userId);
    }

    public Optional<Reservation> getReservationById(int id) {
        return reservationDao.getReservationById(id);
    }

    public void cancelReservation(int id) {
        try {
            reservationDao.deleteReservation(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private double roundToTwoDecimals(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public Reservation createReservation(int userId, int vehicleId, java.util.Date pickupDate, java.util.Date dropOffDate) {
        if (pickupDate.after(dropOffDate)) {
            throw new IllegalArgumentException("Pickup date must be before drop-off date");
        }

        Vehicle vehicle = vehicleDao.getVehicleById(vehicleId)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found"));

        List<Reservation> conflictingReservations = reservationDao.getConflictingReservations(vehicleId, new Date(pickupDate.getTime()), new Date(dropOffDate.getTime()));

        if (!conflictingReservations.isEmpty()) {
            StringBuilder conflictInfo = new StringBuilder("This vehicle is already reserved for the following dates:\n");
            for (Reservation conflictingReservation : conflictingReservations) {
                conflictInfo.append("• From ")
                        .append(conflictingReservation.getPickupDate().toString())
                        .append(" to ")
                        .append(conflictingReservation.getDropOffDate().toString())
                        .append("\n");
            }
            throw new IllegalArgumentException(conflictInfo.toString().trim());
        }

        long days = (dropOffDate.getTime() - pickupDate.getTime()) / (1000 * 60 * 60 * 24);
        double totalCharge = days * vehicle.getPricePerDay();

        totalCharge = roundToTwoDecimals(totalCharge);

        Date sqlPickupDate = new Date(pickupDate.getTime());
        Date sqlDropOffDate = new Date(dropOffDate.getTime());

        Reservation reservation = new Reservation(0, userId, vehicleId, sqlPickupDate, sqlDropOffDate, totalCharge);

        try {
            reservationDao.addReservation(userId, vehicleId, sqlPickupDate, sqlDropOffDate, totalCharge);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Failed to add reservation due to database error");
        }

        return reservation;
    }

    public Reservation updateReservation(Reservation reservation) {
        Optional<Reservation> existingReservation = reservationDao.getReservationById(reservation.getId());
        if (!existingReservation.isPresent()) {
            throw new IllegalArgumentException("Reservation not found");
        }

        List<Reservation> vehicleReservations = reservationDao.getReservationsByVehicleId(reservation.getVehicleId());
        boolean hasConflict = vehicleReservations.stream()
                .filter(r -> r.getId() != reservation.getId())
                .anyMatch(r -> !(reservation.getDropOffDate().before(r.getPickupDate()) ||
                        reservation.getPickupDate().after(r.getDropOffDate())));

        if (hasConflict) {
            throw new IllegalArgumentException("This vehicle is already reserved for the selected dates.");
        }

        try {
            reservationDao.updateReservation(reservation.getId(), reservation.getPickupDate(),
                    reservation.getDropOffDate(), reservation.getTotalCharge());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservation;
    }

    public void updateTotalCharge(int reservationId, double totalCharge) throws SQLException {
        reservationDao.updateTotalCharge(reservationId, totalCharge);
    }

    public void updateReservationDates(int reservationId, Date newPickupDate, Date newDropOffDate) {
        Optional<Reservation> reservationOptional = reservationDao.getReservationById(reservationId);
        if (!reservationOptional.isPresent()) {
            throw new IllegalArgumentException("Reservation not found.");
        }

        Reservation reservation = reservationOptional.get();
        int vehicleId = reservation.getVehicleId();

        List<Reservation> conflictingReservations = reservationDao.getReservationsByVehicleId(vehicleId);
        for (Reservation conflictingReservation : conflictingReservations) {
            if (conflictingReservation.getId() != reservationId &&
                    !(newDropOffDate.before(conflictingReservation.getPickupDate()) || newPickupDate.after(conflictingReservation.getDropOffDate()))) {

                String conflictingDateRange = "Pickup: " + conflictingReservation.getPickupDate() + " - Drop-off: " + conflictingReservation.getDropOffDate();
                throw new IllegalArgumentException("This vehicle is already reserved for the selected dates. Conflicting reservation: " + conflictingDateRange);
            }
        }

        try {
            reservationDao.updateReservation(reservationId, newPickupDate, newDropOffDate, reservation.getTotalCharge());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating reservation dates.", e);
        }
    }

    public void validateReservationConflict(int vehicleId, Date pickupDate, Date dropOffDate, int excludeReservationId) {
        List<Reservation> vehicleReservations = reservationDao.getReservationsByVehicleId(vehicleId);
        boolean hasConflict = vehicleReservations.stream()
                .filter(r -> r.getId() != excludeReservationId)
                .anyMatch(r -> !(dropOffDate.before(r.getPickupDate()) || pickupDate.after(r.getDropOffDate())));

        if (hasConflict) {
            StringBuilder conflictInfo = new StringBuilder();
            for (Reservation r : vehicleReservations) {
                if (r.getId() != excludeReservationId &&
                        !(dropOffDate.before(r.getPickupDate()) || pickupDate.after(r.getDropOffDate()))) {
                    conflictInfo.append("• From ")
                            .append(r.getPickupDate())
                            .append(" to ")
                            .append(r.getDropOffDate())
                            .append("\n");
                }
            }
            throw new IllegalArgumentException("This vehicle is already reserved for the dates:\n" + conflictInfo.toString().trim());
        }
    }
}
