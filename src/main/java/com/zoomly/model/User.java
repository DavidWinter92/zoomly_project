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

    /**
     * Constructs a new User with default ID.
     *
     * @param firstName user's first name
     * @param lastName user's last name
     * @param email user's email address
     * @param password user's password
     * @param accountType user's account type (e.g., admin, user)
     */
    public User(String firstName, String lastName, String email, String password, String accountType) {
        this(-1, firstName, lastName, email, password, accountType);
    }

    /**
     * Constructs a new User with specified ID.
     *
     * @param id user's ID
     * @param firstName user's first name
     * @param lastName user's last name
     * @param email user's email address
     * @param password user's password
     * @param accountType user's account type (e.g., admin, user)
     */
    public User(int id, String firstName, String lastName, String email, String password, String accountType) {
        this.id = new SimpleIntegerProperty(id);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.email = new SimpleStringProperty(email);
        this.password = new SimpleStringProperty(password);
        this.accountType = new SimpleStringProperty(accountType);
        this.reservations = new ArrayList<>();
    }

    /**
     * Returns the user ID.
     *
     * @return user ID
     */
    public int getId() {
        return id.get();
    }

    /**
     * Returns the property for user ID.
     *
     * @return user ID property
     */
    public SimpleIntegerProperty idProperty() {
        return id;
    }

    /**
     * Returns the user's first name.
     *
     * @return first name
     */
    public String getFirstName() {
        return firstName.get();
    }

    /**
     * Returns the property for first name.
     *
     * @return first name property
     */
    public SimpleStringProperty firstNameProperty() {
        return firstName;
    }

    /**
     * Sets the user's first name.
     *
     * @param firstName first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    /**
     * Returns the user's last name.
     *
     * @return last name
     */
    public String getLastName() {
        return lastName.get();
    }

    /**
     * Returns the property for last name.
     *
     * @return last name property
     */
    public SimpleStringProperty lastNameProperty() {
        return lastName;
    }

    /**
     * Sets the user's last name.
     *
     * @param lastName last name to set
     */
    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    /**
     * Returns the user's email.
     *
     * @return email address
     */
    public String getEmail() {
        return email.get();
    }

    /**
     * Returns the property for email.
     *
     * @return email property
     */
    public SimpleStringProperty emailProperty() {
        return email;
    }

    /**
     * Sets the user's email.
     *
     * @param email email to set
     */
    public void setEmail(String email) {
        this.email.set(email);
    }

    /**
     * Returns the user's password.
     *
     * @return password
     */
    public String getPassword() {
        return password.get();
    }

    /**
     * Returns the property for password.
     *
     * @return password property
     */
    public SimpleStringProperty passwordProperty() {
        return password;
    }

    /**
     * Sets the user's password.
     *
     * @param password password to set
     */
    public void setPassword(String password) {
        this.password.set(password);
    }

    /**
     * Returns the user's account type.
     *
     * @return account type
     */
    public String getAccountType() {
        return accountType.get();
    }

    /**
     * Returns the property for account type.
     *
     * @return account type property
     */
    public SimpleStringProperty accountTypeProperty() {
        return accountType;
    }

    /**
     * Sets the user's account type.
     *
     * @param accountType account type to set
     */
    public void setAccountType(String accountType) {
        this.accountType.set(accountType);
    }

    /**
     * Returns the list of reservations associated with the user.
     *
     * @return list of reservations
     */
    public List<Reservation> getReservations() {
        return reservations;
    }

    /**
     * Checks if the user has an admin account type.
     *
     * @return true if the user is an admin, false otherwise
     */
    public boolean isAdmin() {
        return "admin".equals(this.accountType.get().toLowerCase());
    }

    /**
     * Returns a string representation of the user.
     *
     * @return string describing the user
     */
    @Override
    public String toString() {
        return String.format("User{id=%d, firstName='%s', lastName='%s', email='%s', accountType='%s'}",
                id.get(), firstName.get(), lastName.get(), email.get(), accountType.get());
    }
}
