package com.zoomly.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user in the system.
 * Users can be either regular users or administrators.
 */

public class User {
    private static int currentId = 1;
    private final int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String accountType;
    private List<Reservation> reservations;

    public User(String firstName, String lastName, String email, String password, String accountType) {
        this.id = getNextId();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.accountType = accountType;
        this.reservations = new ArrayList<>();
    }
    private static int getNextId() {
        return currentId++;
    }

    public int getId() { return id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }
    public List<Reservation> getReservations() { return reservations; }


    /**
     * method: toString
     * parameters: none
     * return: String - String representation of the user
     * purpose: Creates a formatted string containing all user information
     */
    @Override
    public String toString() {
        return String.format("User{id=%d, firstName='%s', lastName='%s', email='%s', accountType='%s'}",
                id, firstName, lastName, email, accountType);
    }
}