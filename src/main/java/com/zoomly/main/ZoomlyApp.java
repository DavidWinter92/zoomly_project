package com.zoomly.main;

import com.zoomly.repository.UserRepository;
import com.zoomly.repository.VehicleRepository;
import com.zoomly.ui.ConsoleUI;
import com.zoomly.ui.MainMenu;

/**
 * David Winter
 * CEN-3024C
 * Software Development 1
 * 02/21/2025
 *
 * ZoomlyApp.java
 * This class serves as the main entry point for the Zoomly Vehicle Rental System.
 * It initializes the data from text files and starts the application.
 */

public class ZoomlyApp {
    public static void main(String[] args) {
        UserRepository userRepository = new UserRepository();
        VehicleRepository vehicleRepository = new VehicleRepository();
        ConsoleUI consoleUI = new ConsoleUI();

        // Start the application with the main menu
        MainMenu mainMenu = new MainMenu(consoleUI, userRepository, vehicleRepository);
        mainMenu.start();
    }
}