package com.zoomly.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.time.Year;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Validator.java
 * Provides validation methods for various input fields in the system.
 */
public class Validator {
    private static final Set<String> VALID_ACCOUNT_TYPES = new HashSet<>(Arrays.asList("user", "administrator"));
    private static final Set<String> VALID_CAR_TYPES = new HashSet<>(Arrays.asList("SUV", "Sedan", "Truck"));
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)\\.com$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[!@#$%^&*])[A-Za-z0-9!@#$%^&*]{6,}$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z]{1,10}$");

    // Date pattern for MM/dd/yyyy format
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public static boolean isValidAccountType(String accountType) {
        return VALID_ACCOUNT_TYPES.contains(accountType.toLowerCase());
    }

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        return password != null && PASSWORD_PATTERN.matcher(password).matches();
    }

    public static boolean isValidName(String name) {
        return name != null && NAME_PATTERN.matcher(name).matches();
    }

    public static boolean isValidCarType(String carType) {
        return VALID_CAR_TYPES.contains(carType);
    }

    public static boolean isValidYear(int year) {
        int currentYear = Year.now().getValue();
        return year >= 1800 && year <= currentYear;
    }

    // New method to validate dates
    public static boolean isValidDate(String dateStr) {
        try {
            LocalDate.parse(dateStr, DATE_FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    // New method to validate date ranges
    public static boolean isValidDateRange(String startDateStr, String endDateStr) {
        try {
            LocalDate startDate = LocalDate.parse(startDateStr, DATE_FORMATTER);
            LocalDate endDate = LocalDate.parse(endDateStr, DATE_FORMATTER);

            return !endDate.isBefore(startDate);
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}