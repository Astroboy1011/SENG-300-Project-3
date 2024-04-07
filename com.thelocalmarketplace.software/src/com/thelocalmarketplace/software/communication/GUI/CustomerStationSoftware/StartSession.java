package com.thelocalmarketplace.software.communication.GUI.CustomerStationSoftware;

import javax.swing.*;

import com.jjjwelectronics.scale.AbstractElectronicScale;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.communication.GUI.AttendantStation.AttendantPageGUI;

import java.awt.*;
import java.awt.event.*;

public class StartSession extends JFrame {
    private final int SCREEN_WIDTH = 900; // Width of the window
    private final int SCREEN_HEIGHT = 700; // Height of the window
    private int selectedStation; // Variable to hold the selected station number
    private CustomerStation customerStation; // Reference to the CustomerStation GUI
    private AttendantPageGUI attendantPageGUI;
    private SelfCheckoutStationSoftware stationSoftwareInstance;
    private AbstractElectronicScale scale;
    private boolean mouseListenerEnabled = true;


    // Method to start a customer session (this might be called based on some user interaction within StartSession)
    public void startCustomerSession(int stationNumber, SelfCheckoutStationSoftware stationSoftwareInstance, AbstractElectronicScale scale) {
        if (customerStation == null) {
            this.customerStation = new CustomerStation(stationNumber,stationSoftwareInstance,scale, attendantPageGUI);
            this.customerStation.setVisible(true);
            // Update the reference in AttendantPageGUI
            if (attendantPageGUI != null) {
                attendantPageGUI.updateCustomerStation(stationNumber, customerStation);
            }
        }
    }
    
    public void setAttendantPageGUI(AttendantPageGUI attendantPageGUI) {
        this.attendantPageGUI = attendantPageGUI;
    }

    public StartSession(int stationNumber, SelfCheckoutStationSoftware stationSoftwareInstance, AbstractElectronicScale scale) {
    	this.stationSoftwareInstance = stationSoftwareInstance;
    	this.stationSoftwareInstance.setStationActive(false);
    	this.scale = scale;
        this.selectedStation = stationNumber; // Set the selected station number
        // Frame initialization
        setTitle("Welcome to the Market - Station " + selectedStation);
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT); // Set the size of the window
        setLocationRelativeTo(null); // Center the window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Content panel for the main content
        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER; // End row after this component
        gbc.fill = GridBagConstraints.HORIZONTAL; // Stretch component horizontally
        gbc.anchor = GridBagConstraints.CENTER; // Center component

        // Make the panel clickable
        contentPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (mouseListenerEnabled) {
                    // Implement session start logic here
                    stationSoftwareInstance.setStationActive(true);
                    // Once the session is started, dispose the current frame and open the CustomerStation GUI
                    StartSession.this.dispose(); // Close StartSession window
                    startCustomerSession(stationNumber, stationSoftwareInstance, scale); // Create and display a new instance of CustomerStation (using station 1 for this example)
                }
            }
        });

        // Welcome label
        JLabel welcomeLabel = new JLabel("Welcome to the Self-Checkout Station!");
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Set the font size of the welcome label
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));

        // Start session label
        JLabel startSessionLabel = new JLabel("Touch Anywhere to Start");
        startSessionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // set the font size of the start session label
        startSessionLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        // Add components to the panel with constraints
        contentPanel.add(Box.createVerticalStrut(50)); // Vertical spacer
        contentPanel.add(welcomeLabel, gbc);
        contentPanel.add(Box.createVerticalStrut(50)); // Vertical spacer
        contentPanel.add(startSessionLabel, gbc);

        add(contentPanel, BorderLayout.CENTER);
        SettingsPanel settingsPanel = new SettingsPanel(null);
        settingsPanel.setVisible(true);
        add(settingsPanel, BorderLayout.NORTH);

        // Make the GUI visible
        setVisible(true);
    }
    
    public void sessionPopUp(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void enableMouseListener() {
        mouseListenerEnabled = true;
    }

    // Method to disable the mouse listener
    public void disableMouseListener() {
        mouseListenerEnabled = false;
    }
}

