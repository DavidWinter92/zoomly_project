package com.zoomly.repository;

import com.zoomly.model.Reservation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * ReservationRepository.java
 * Repository class for Reservation data access.
 * Currently implements in-memory storage, can be extended for database implementation.
 */

public class ReservationRepository {
    private final Map<Integer, Reservation> reservations = new HashMap<>();
    private int nextId = 1;

    public Reservation save(Reservation reservation) {
        if (reservation.getId() == 0) {
            reservation = new Reservation(nextId++, reservation.getUserId(),
                    reservation.getVehicleId(), reservation.getPickupDate(),
                    reservation.getDropOffDate(), reservation.getTotalCharge());
        }
        reservations.put(reservation.getId(), reservation);
        return reservation;
    }

    public Optional<Reservation> findById(int id) {
        return Optional.ofNullable(reservations.get(id));
    }

    public List<Reservation> findAll() {
        return new ArrayList<>(reservations.values());
    }

    public List<Reservation> findByUserId(int userId) {
        return reservations.values().stream()
                .filter(reservation -> reservation.getUserId() == userId)
                .toList();
    }

    public List<Reservation> findByVehicleId(int vehicleId) {
        return reservations.values().stream()
                .filter(reservation -> reservation.getVehicleId() == vehicleId)
                .toList();
    }

    public void delete(int id) {
        reservations.remove(id);
    }
}