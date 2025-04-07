package com.zoomly.util;

import com.zoomly.model.User;

/**
 * UserValidator.java
 * Utility class for validating user input.
 * Provides methods to validate user attributes like name, email, password, and account type.
 * Ensures that user data meets the required format and standards before processing.
 */

public class UserValidator {
    public static void validate(User user) {
        StringBuilder errorMessage = new StringBuilder();

        if (!isValidName(user.getFirstName())) {
            errorMessage.append("Invalid first name. Cannot use special or numerical characters. ");
            errorMessage.append("This is invalid: ").append(getInvalidCharacters(user.getFirstName())).append("\n");
        }
        if (!isValidName(user.getLastName())) {
            errorMessage.append("Invalid last name. Cannot use special or numerical characters. ");
            errorMessage.append("This is invalid: ").append(getInvalidCharacters(user.getLastName())).append("\n");
        }
        String emailError = validateEmail(user.getEmail());
        if (!emailError.isEmpty()) {
            errorMessage.append(emailError);
        }
        if (!isValidPassword(user.getPassword())) {
            errorMessage.append("Invalid password format. Password must be at least 8 characters long and include 1 special character and 1 numerical character. \n");
        }
        if (!isValidAccountType(user.getAccountType())) {
            errorMessage.append("Invalid account type. \n");
        }

        if (errorMessage.length() > 0) {
            throw new IllegalArgumentException(errorMessage.toString());
        }
    }

    public static boolean isValidAccountType(String accountType) {
        return accountType != null && (accountType.equalsIgnoreCase("user") || accountType.equalsIgnoreCase("admin"));
    }

    public static String validateEmail(String email) {
        StringBuilder error = new StringBuilder();
        if (email == null || email.isEmpty()) {
            error.append("Email cannot be empty. Proper format: example@example.com\n");
            return error.toString();
        }

        if (!email.contains("@")) {
            error.append("Email must contain '@'. ");
        }
        if (email.indexOf('@') == 0) {
            error.append("Missing email name before '@'. ");
        }
        if (!email.contains(".")) {
            error.append("Email must contain a '.' character. ");
        }
        if (email.indexOf('.') < email.indexOf('@') + 2) {
            error.append("Missing domain after '@'. ");
        }
        if (email.indexOf('.') == email.length() - 1) {
            error.append("Missing extension after '.'. ");
        }

        if (error.length() > 0) {
            error.append("Proper format: example@example.com\n");
        }
        return error.toString();
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.matches("^(?=.*[0-9])(?=.*[!@#$%^&*])[A-Za-z0-9!@#$%^&*]{8,}$");
    }

    public static boolean isValidName(String name) {
        return name != null && name.matches("^[A-Za-z]{1,10}$");
    }

    private static String getInvalidCharacters(String input) {
        StringBuilder invalidChars = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (!Character.isLetter(c)) {
                invalidChars.append(c);
            }
        }
        return invalidChars.toString();
    }
}