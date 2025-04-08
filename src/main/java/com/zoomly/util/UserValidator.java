package com.zoomly.util;

import com.zoomly.model.User;

/**
 * UserValidator.java
 * Utility class for validating user input.
 * Provides methods to validate user attributes like name, email, password, and account type.
 * Ensures that user data meets the required format and standards before processing.
 */
public class UserValidator {

    /**
     * Validates the attributes of the given user object.
     * Checks if the user's first name, last name, email, password, and account type are valid.
     *
     * @param user The user object to validate.
     * @throws IllegalArgumentException If any of the user's attributes are invalid.
     */
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

    /**
     * Validates the account type.
     * The account type must be either "user" or "admin" (case-insensitive).
     *
     * @param accountType The account type to validate.
     * @return true if the account type is valid, false otherwise.
     */
    public static boolean isValidAccountType(String accountType) {
        return accountType != null && (accountType.equalsIgnoreCase("user") || accountType.equalsIgnoreCase("admin"));
    }

    /**
     * Validates the email format.
     * Checks if the email contains "@" and ".", and follows basic email structure.
     *
     * @param email The email to validate.
     * @return A string describing the validation errors (if any).
     */
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

    /**
     * Validates the password format.
     * The password must be at least 8 characters long, and contain at least one numerical and one special character.
     *
     * @param password The password to validate.
     * @return true if the password is valid, false otherwise.
     */
    public static boolean isValidPassword(String password) {
        return password != null && password.matches("^(?=.*[0-9])(?=.*[!@#$%^&*])[A-Za-z0-9!@#$%^&*]{8,}$");
    }

    /**
     * Validates the name format.
     * The name must consist of only alphabetic characters (A-Z, a-z) and be between 1 to 10 characters long.
     *
     * @param name The name to validate.
     * @return true if the name is valid, false otherwise.
     */
    public static boolean isValidName(String name) {
        return name != null && name.matches("^[A-Za-z]{1,10}$");
    }

    /**
     * Returns a string of invalid characters found in the given input.
     * Invalid characters are those that are not alphabetic letters.
     *
     * @param input The string to check for invalid characters.
     * @return A string of invalid characters.
     */
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
