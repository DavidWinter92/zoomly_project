package com.zoomly.util;

import java.time.Year;
import java.util.Arrays;
import java.util.List;

/**
 * VehicleValidator.java
 * Utility class for validating vehicle details.
 * Provides methods to validate VIN, make, model, year, mileage, and price per day.
 * Ensures that vehicle data meets the required format and standards before adding it to the system.
 */

public class VehicleValidator {
    private static final List<String> VALID_MAKES = Arrays.asList("Toyota", "Honda", "Tesla", "Ford");
    private static final List<String> TOYOTA_MODELS = Arrays.asList("Corolla", "Camry");
    private static final List<String> HONDA_MODELS = Arrays.asList("Civic", "Accord");
    private static final List<String> TESLA_MODELS = Arrays.asList("Model X", "Model Y", "Model S");
    private static final List<String> FORD_MODELS = Arrays.asList("Mustang", "F-150");

    public static void validate(String vin, String make, String model, int year, double mileage, double pricePerDay) {
        // Validate VIN
        if (vin.length() != 8 || !vin.matches("[A-Za-z0-9]+")) {
            throw new IllegalArgumentException("VIN must be exactly 8 alphanumeric characters.");
        }

        if (!VALID_MAKES.contains(make)) {
            throw new IllegalArgumentException("Make must be one of: " + String.join(", ", VALID_MAKES));
        }

        if (!isValidModel(make, model)) {
            throw new IllegalArgumentException("Invalid model for " + make + ": " + model + ". Valid models are: " + getValidModels(make));
        }

        if (year < 1800 || year > Year.now().getValue()) {
            throw new IllegalArgumentException("Year must be between 1800 and the current year: " + Year.now().getValue());
        }

        validateMileage(mileage);

        if (pricePerDay <= 0) {
            throw new IllegalArgumentException("Price per day must be a positive number.");
        }

        pricePerDay = Math.round(pricePerDay * 100.0) / 100.0;

        if (pricePerDay != Math.round(pricePerDay * 100.0) / 100.0) {
            throw new IllegalArgumentException("Price per day must have at most two decimal places.");
        }
    }

    private static boolean isValidModel(String make, String model) {
        switch (make) {
            case "Toyota":
                return TOYOTA_MODELS.contains(model);
            case "Honda":
                return HONDA_MODELS.contains(model);
            case "Tesla":
                return TESLA_MODELS.contains(model);
            case "Ford":
                return FORD_MODELS.contains(model);
            default:
                return false;
        }
    }

    private static String getValidModels(String make) {
        switch (make) {
            case "Toyota":
                return String.join(", ", TOYOTA_MODELS);
            case "Honda":
                return String.join(", ", HONDA_MODELS);
            case "Tesla":
                return String.join(", ", TESLA_MODELS);
            case "Ford":
                return String.join(", ", FORD_MODELS);
            default:
                return "None";
        }
    }

    private static void validateMileage(double mileage) {
        if (mileage < 0) {
            throw new IllegalArgumentException("Mileage must be a positive number.");
        }
    }
}
