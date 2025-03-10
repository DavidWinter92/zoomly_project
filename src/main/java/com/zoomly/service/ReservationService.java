package com.zoomly.service;

import com.zoomly.model.Reservation;
import com.zoomly.model.Vehicle;
import com.zoomly.repository.ReservationRepository;
import com.zoomly.repository.VehicleRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * ReservationService.java
 * Service class for reservation-related business logic.
 */

public class ReservationService {
    private static ReservationService instance;
    private final ReservationRepository reservationRepository;
    private final VehicleRepository vehicleRepository;

    private ReservationService() {
        this.reservationRepository = ReservationRepository.getInstance();
        this.vehicleRepository = VehicleRepository.getInstance();
    }

    public static ReservationService getInstance() {
        if (instance == null) {
            instance = new ReservationService();
        }
        return instance;
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public List<Reservation> getUserReservations(int userId) {
        return reservationRepository.findByUserId(userId);
    }

    public Optional<Reservation> getReservationById(int id) {
        return reservationRepository.findById(id);
    }

    public void cancelReservation(int id) {
        reservationRepository.delete(id);
    }

    private double roundToTwoDecimals(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public Reservation createReservation(int userId, int vehicleId,
                                         Date pickupDate, Date dropOffDate) {
        if (pickupDate.after(dropOffDate)) {
            throw new IllegalArgumentException("Pickup date must be before drop-off date");
        }

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found"));

        List<Reservation> vehicleReservations = reservationRepository.findByVehicleId(vehicleId);
        boolean hasOverlap = vehicleReservations.stream()
                .anyMatch(r -> !(dropOffDate.before(r.getPickupDate()) ||
                        pickupDate.after(r.getDropOffDate())));

        if (hasOverlap) {
            throw new IllegalArgumentException("Vehicle is not available for the selected dates");
        }

        long days = (dropOffDate.getTime() - pickupDate.getTime()) / (1000 * 60 * 60 * 24);
        double totalCharge = days * vehicle.getPricePerDay();

        totalCharge = roundToTwoDecimals(totalCharge);

        Reservation reservation = new Reservation(0, userId, vehicleId,
                pickupDate, dropOffDate, totalCharge);

        return reservationRepository.save(reservation);
    }

    public Reservation updateReservation(Reservation reservation) {
        Optional<Reservation> existingReservation = reservationRepository.findById(reservation.getId());
        if (!existingReservation.isPresent()) {
            throw new IllegalArgumentException("Reservation not found");
        }

        List<Reservation> vehicleReservations = reservationRepository.findByVehicleId(reservation.getVehicleId());
        boolean hasConflict = vehicleReservations.stream()
                .filter(r -> r.getId() != reservation.getId())
                .anyMatch(r -> !(reservation.getDropOffDate().before(r.getPickupDate()) ||
                        reservation.getPickupDate().after(r.getDropOffDate())));

        if (hasConflict) {
            throw new IllegalArgumentException("This vehicle is already reserved for the selected dates.");
        }

        return reservationRepository.save(reservation);
    }

}