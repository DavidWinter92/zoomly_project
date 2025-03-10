package com.zoomly.model;

import java.util.ArrayList;
import java.util.List;

/**
 * User.java
 * Model class representing a user in the Zoomly application.
 * Contains user attributes and methods for managing user data.
 */

public class User {
    private final int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String accountType;
    private List<Reservation> reservations;

    public User(String firstName, String lastName, String email, String password, String accountType, int id) {
        this.id = id; // Use the passed ID
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.accountType = accountType;
        this.reservations = new ArrayList<>();
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

    public boolean isAdmin() {
        return "administrator".equals(this.accountType.toLowerCase());
    }

    @Override
    public String toString() {
        return String.format("User{id=%d, firstName='%s', lastName='%s', email='%s', accountType='%s'}",
                id, firstName, lastName, email, accountType);
    }
}