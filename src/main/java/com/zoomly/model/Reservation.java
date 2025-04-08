package com.zoomly.model;

import java.sql.Date;

/**
 * Reservation.java
 * Represents a reservation in the system.
 * Links users with vehicles for specific date ranges.
 */
public class Reservation {
    private int id;
    private int userId;
    private int vehicleId;
    private Date pickupDate;
    private Date dropOffDate;
    private double totalCharge;

    /**
     * Constructs a Reservation object with all fields.
     *
     * @param id the reservation ID
     * @param userId the ID of the user who made the reservation
     * @param vehicleId the ID of the reserved vehicle
     * @param pickupDate the start date of the reservation
     * @param dropOffDate the end date of the reservation
     * @param totalCharge the total charge for the reservation
     */
    public Reservation(int id, int userId, int vehicleId, Date pickupDate, Date dropOffDate, double totalCharge) {
        this.id = id;
        this.userId = userId;
        this.vehicleId = vehicleId;
        this.pickupDate = pickupDate;
        this.dropOffDate = dropOffDate;
        this.totalCharge = totalCharge;
    }

    /**
     * Returns the reservation ID.
     *
     * @return the reservation ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the reservation ID.
     *
     * @param id the reservation ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the user ID.
     *
     * @return the user ID
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Returns the vehicle ID.
     *
     * @return the vehicle ID
     */
    public int getVehicleId() {
        return vehicleId;
    }

    /**
     * Returns the pickup date of the reservation.
     *
     * @return the pickup date
     */
    public Date getPickupDate() {
        return pickupDate;
    }

    /**
     * Sets the pickup date of the reservation.
     *
     * @param pickupDate the pickup date
     */
    public void setPickupDate(Date pickupDate) {
        this.pickupDate = pickupDate;
    }

    /**
     * Returns the drop-off date of the reservation.
     *
     * @return the drop-off date
     */
    public Date getDropOffDate() {
        return dropOffDate;
    }

    /**
     * Sets the drop-off date of the reservation.
     *
     * @param dropOffDate the drop-off date
     */
    public void setDropOffDate(Date dropOffDate) {
        this.dropOffDate = dropOffDate;
    }

    /**
     * Returns the total charge of the reservation.
     *
     * @return the total charge
     */
    public double getTotalCharge() {
        return totalCharge;
    }

    /**
     * Sets the total charge of the reservation.
     *
     * @param totalCharge the total charge
     */
    public void setTotalCharge(double totalCharge) {
        this.totalCharge = totalCharge;
    }

    /**
     * Returns a string representation of the reservation.
     *
     * @return a string describing the reservation
     */
    @Override
    public String toString() {
        return String.format("Reservation{id=%d, userId=%d, vehicleId=%d, pickup=%s, dropoff=%s, charge=%.2f}",
                id, userId, vehicleId, pickupDate, dropOffDate, totalCharge);
    }
}
