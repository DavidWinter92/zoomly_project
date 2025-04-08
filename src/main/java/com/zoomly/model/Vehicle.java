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

    /**
     * Constructs a new Vehicle object.
     *
     * @param id unique vehicle ID
     * @param vin vehicle identification number
     * @param make vehicle make
     * @param model vehicle model
     * @param year vehicle manufacture year
     * @param mileage current mileage
     * @param pricePerDay rental price per day
     * @param imagePath image file path
     * @param description vehicle description
     */
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

    /**
     * Returns the vehicle ID.
     *
     * @return vehicle ID
     */
    public int getId() { return id; }

    /**
     * Returns the vehicle VIN.
     *
     * @return VIN
     */
    public String getVin() { return vin; }

    /**
     * Sets the vehicle VIN.
     *
     * @param vin VIN to set
     */
    public void setVin(String vin) { this.vin = vin; }

    /**
     * Returns the vehicle make.
     *
     * @return make
     */
    public String getMake() { return make; }

    /**
     * Sets the vehicle make.
     *
     * @param make make to set
     */
    public void setMake(String make) { this.make = make; }

    /**
     * Returns the vehicle model.
     *
     * @return model
     */
    public String getModel() { return model; }

    /**
     * Sets the vehicle model.
     *
     * @param model model to set
     */
    public void setModel(String model) { this.model = model; }

    /**
     * Returns the vehicle year.
     *
     * @return year
     */
    public int getYear() { return year; }

    /**
     * Sets the vehicle year.
     *
     * @param year year to set
     */
    public void setYear(int year) { this.year = year; }

    /**
     * Returns the vehicle mileage.
     *
     * @return mileage
     */
    public double getMileage() { return mileage; }

    /**
     * Sets the vehicle mileage.
     *
     * @param mileage mileage to set
     */
    public void setMileage(double mileage) { this.mileage = mileage; }

    /**
     * Returns the rental price per day.
     *
     * @return price per day
     */
    public double getPricePerDay() { return pricePerDay; }

    /**
     * Sets the rental price per day.
     *
     * @param pricePerDay price to set
     */
    public void setPricePerDay(double pricePerDay) { this.pricePerDay = pricePerDay; }

    /**
     * Returns the image path of the vehicle.
     *
     * @return image path
     */
    public String getImagePath() { return imagePath; }

    /**
     * Sets the image path of the vehicle.
     *
     * @param imagePath image path to set
     */
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    /**
     * Returns the description of the vehicle.
     *
     * @return description
     */
    public String getDescription() { return description; }

    /**
     * Sets the description of the vehicle.
     *
     * @param description description to set
     */
    public void setDescription(String description) { this.description = description; }

    /**
     * Returns a string representation of the vehicle.
     *
     * @return string describing the vehicle
     */
    @Override
    public String toString() {
        return String.format("Vehicle{id=%d, vin='%s', make='%s', model='%s', year=%d, mileage=%.2f, pricePerDay=%.2f, imagePath='%s', description='%s'}",
                id, vin, make, model, year, mileage, pricePerDay, imagePath, description);
    }
}
