package com.zoomly.model;

/**
 * Vehicle.java
 * Model class representing a vehicle.
 * Stores information related to a vehicle, such as VIN, make, model, year, mileage, price per day, image path, and description.
 * Used for interacting with the vehicle data in the database.
 */

public class Vehicle {
    private final int id;
    private String vin;
    private String make;
    private String model;
    private int year;
    private double mileage;
    private double pricePerDay;
    private String imagePath;
    private String description;

    public Vehicle(int id, String vin, String make, String model, int year, double mileage, double pricePerDay, String imagePath, String description) {
        this.id = id;
        this.vin = vin;
        this.make = make;
        this.model = model;
        this.year = year;
        this.mileage = mileage;
        this.pricePerDay = pricePerDay;
        this.imagePath = imagePath;
        this.description = description;
    }

    public int getId() { return id; }
    public String getVin() { return vin; }
    public void setVin(String vin) { this.vin = vin; }
    public String getMake() { return make; }
    public void setMake(String make) { this.make = make; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    public double getMileage() { return mileage; }
    public void setMileage(double mileage) { this.mileage = mileage; }
    public double getPricePerDay() { return pricePerDay; }
    public void setPricePerDay(double pricePerDay) { this.pricePerDay = pricePerDay; }
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return String.format("Vehicle{id=%d, vin='%s', make='%s', model='%s', year=%d, mileage=%.2f, pricePerDay=%.2f, imagePath='%s', description='%s'}",
                id, vin, make, model, year, mileage, pricePerDay, imagePath, description);
    }
}