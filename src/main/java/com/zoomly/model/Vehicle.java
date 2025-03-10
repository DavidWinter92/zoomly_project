package com.zoomly.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Vehicle.java
 * This class represents a vehicle entity in the rental system, containing
 * all relevant vehicle information and reservation history.
 */

public class Vehicle {
    private int id;
    private String carType;
    private String model;
    private int year;
    private double mileage;
    private double pricePerDay;
    private List<Reservation> reservations;

    public Vehicle(int id, String carType, String model, int year, double mileage, double pricePerDay) {
        this.id = id;
        this.carType = carType;
        this.model = model;
        this.year = year;
        this.mileage = mileage;
        this.pricePerDay = pricePerDay;
        this.reservations = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getMileage() {
        return mileage;
    }

    public void setMileage(double mileage) {
        this.mileage = mileage;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    @Override
    public String toString() {
        return String.format("Vehicle{id=%d, type='%s', model='%s', year=%d, mileage=%.2f, pricePerDay=%.2f}",
                id, carType, model, year, mileage, pricePerDay);
    }
}