package com.zoomly.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * User.java
 * Model class representing a user in the Zoomly application.
 * Contains user attributes and methods for managing user data.
 */

public class User {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty firstName;
    private final SimpleStringProperty lastName;
    private final SimpleStringProperty email;
    private final SimpleStringProperty password;
    private final SimpleStringProperty accountType;
    private List<Reservation> reservations;

    public User(String firstName, String lastName, String email, String password, String accountType) {
        this(-1, firstName, lastName, email, password, accountType);
    }

    public User(int id, String firstName, String lastName, String email, String password, String accountType) {
        this.id = new SimpleIntegerProperty(id);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.email = new SimpleStringProperty(email);
        this.password = new SimpleStringProperty(password);
        this.accountType = new SimpleStringProperty(accountType);
        this.reservations = new ArrayList<>();
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public String getFirstName() {
        return firstName.get();
    }

    public SimpleStringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public SimpleStringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getPassword() {
        return password.get();
    }

    public SimpleStringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public String getAccountType() {
        return accountType.get();
    }

    public SimpleStringProperty accountTypeProperty() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType.set(accountType);
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public boolean isAdmin() {
        return "admin".equals(this.accountType.get().toLowerCase());
    }

    @Override
    public String toString() {
        return String.format("User{id=%d, firstName='%s', lastName='%s', email='%s', accountType='%s'}",
                id.get(), firstName.get(), lastName.get(), email.get(), accountType.get());
    }
}