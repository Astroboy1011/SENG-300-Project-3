package com.thelocalmarketplace.software.communication.GUI.CustomerStationSoftware;
import javax.swing.*;

import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsPanel extends JPanel {
    private JComboBox<String> languageDropdown;
    private JComboBox<String> accessibilityDropdown;

    private String[] languages = {"English"};
    private String[] accessibilitySettings = {"Default"};
    
    private StatusOfStation status;

    public SettingsPanel(SelfCheckoutStationSoftware software) {
    	if(software != null) 
    		this.status = new StatusOfStation(software);
        setLayout(new FlowLayout(FlowLayout.LEFT));

        // Language settings
        JPanel languagePanel = new JPanel(new BorderLayout());
        languageDropdown = new JComboBox<>(languages);
        languagePanel.add(new JLabel("Language: "), BorderLayout.NORTH);
        languagePanel.add(languageDropdown, BorderLayout.CENTER);

        // Accessibility settings
        JPanel accessibilityPanel = new JPanel(new BorderLayout());
        accessibilityDropdown = new JComboBox<>(accessibilitySettings);
        accessibilityPanel.add(new JLabel("Accessibility: "), BorderLayout.NORTH);
        accessibilityPanel.add(accessibilityDropdown, BorderLayout.CENTER);

        add(languagePanel);
        add(accessibilityPanel);

        if(software != null) {
	        // Button to open StatusOfStation pop-up
	        JButton statusButton = new JButton("Status of Station");
	        statusButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	status.setVisible(true);
	            }
	        });
	        add(statusButton);
        }
        
        setVisible(true);
    }

    public void updateStatus() {
    	status.updateLabels();
    }
}