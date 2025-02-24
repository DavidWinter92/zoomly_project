package com.zoomly.model;

import java.util.Date;

/**
 * Represents a reservation in the system.
 * Links users with vehicles for specific date ranges.
 */

public class Reservation {
    private static int currentId = 1;
    private final int id;
    private int userId;
    private int vehicleId;
    private Date pickupDate;
    private Date dropOffDate;
    private double totalCharge;

    public Reservation(int id, int userId, int vehicleId, Date pickupDate, Date dropOffDate, double totalCharge) {
        this.id = getNextId();
        this.userId = userId;
        this.vehicleId = vehicleId;
        this.pickupDate = pickupDate;
        this.dropOffDate = dropOffDate;
        this.totalCharge = totalCharge;
    }
    private static int getNextId() {
        return currentId++;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public int getVehicleId() { return vehicleId; }
    public Date getPickupDate() { return pickupDate; }
    public void setPickupDate(Date pickupDate) { this.pickupDate = pickupDate; }
    public Date getDropOffDate() { return dropOffDate; }
    public void setDropOffDate(Date dropOffDate) { this.dropOffDate = dropOffDate; }
    public double getTotalCharge() { return totalCharge; }
    public void setTotalCharge(double totalCharge) { this.totalCharge = totalCharge; }

    /**
     * method: toString
     * parameters: none
     * return: String - String representation of the reservation
     * purpose: Creates a formatted string containing all reservation information
     */
    @Override
    public String toString() {
        return String.format("Reservation{id=%d, userId=%d, vehicleId=%d, pickup=%s, dropoff=%s, charge=%.2f}",
                id, userId, vehicleId, pickupDate, dropOffDate, totalCharge);
    }
}