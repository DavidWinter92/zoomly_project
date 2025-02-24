package com.zoomly.service;

import com.zoomly.model.Reservation;
import com.zoomly.model.Vehicle;
import com.zoomly.repository.ReservationRepository;
import com.zoomly.repository.VehicleRepository;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * ReservationService.java
 * Service class for reservation-related business logic.
 */

public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final VehicleRepository vehicleRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              VehicleRepository vehicleRepository) {
        this.reservationRepository = reservationRepository;
        this.vehicleRepository = vehicleRepository;
    }

    public Reservation createReservation(int userId, int vehicleId,
                                         Date pickupDate, Date dropOffDate) {
        // Validate dates
        if (pickupDate.after(dropOffDate)) {
            throw new IllegalArgumentException("Pickup date must be before drop-off date");
        }

        // Check if vehicle exists
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found"));

        // Check for overlapping reservations
        List<Reservation> vehicleReservations = reservationRepository.findByVehicleId(vehicleId);
        boolean hasOverlap = vehicleReservations.stream()
                .anyMatch(r -> !(dropOffDate.before(r.getPickupDate()) ||
                        pickupDate.after(r.getDropOffDate())));

        if (hasOverlap) {
            throw new IllegalArgumentException("Vehicle is not available for the selected dates");
        }

        // Calculate total charge
        long days = (dropOffDate.getTime() - pickupDate.getTime()) / (1000 * 60 * 60 * 24);
        double totalCharge = days * vehicle.getPricePerDay();

        Reservation reservation = new Reservation(0, userId, vehicleId,
                pickupDate, dropOffDate, totalCharge);
        return reservationRepository.save(reservation);
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
}