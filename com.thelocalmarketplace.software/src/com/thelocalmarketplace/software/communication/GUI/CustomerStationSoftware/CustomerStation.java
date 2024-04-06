package com.thelocalmarketplace.software.communication.GUI.CustomerStationSoftware;

import javax.swing.*;

import com.jjjwelectronics.Mass;
import com.jjjwelectronics.scale.AbstractElectronicScale;
import com.jjjwelectronics.scale.IElectronicScale;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodedItem;
import com.jjjwelectronics.scanner.IBarcodeScanner;
import com.thelocalmarketplace.hardware.PLUCodedProduct;
import com.thelocalmarketplace.hardware.external.ProductDatabases;
import com.thelocalmarketplace.software.AddownBag;
import com.thelocalmarketplace.software.communication.GUI.AttendantStation.AttendantLoginPage;
import com.thelocalmarketplace.software.communication.GUI.AttendantStation.AttendantPageGUI;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.product.Products;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

public class CustomerStation extends JFrame {

    private JTextArea cartTextArea;
    private JLabel totalPriceLabel;
    private JPanel menuPanel;
    private JPanel mainPanel;
    private JPanel cartPanel;
    private JPanel PLUPanel;
    private JPanel payButtonPanel;
    private JTextField screenTextField;
    private JPanel addItemPanel;
    private AddownBag addOwnBag;
    private SelfCheckoutStationSoftware stationSoftwareInstance;
    private AbstractElectronicScale scale;
    private Products products;
    
    public CustomerStation(int selectedStation, SelfCheckoutStationSoftware stationSoftwareInstance, AbstractElectronicScale scale) {
    	this.scale = scale;
    	this.stationSoftwareInstance = stationSoftwareInstance;
    	
    	stationSoftwareInstance.setGUI(this);
    	
        setTitle("Self-Checkout Station " + selectedStation);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);


        
        // Main panel
        mainPanel = new JPanel(new BorderLayout());

        // Menu panel
        menuPanel = new JPanel(new GridLayout(3, 3, 10, 10));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Buttons
        // the following nulls inside the buttons should be replaced by the corresponding callback functions 
        //associated with the popups
        JButton useOwnBagsBtn = createButton("Use Own Bags", null);
        
        JButton scanBarcodeBtn = createButton("Scan Barcode", e -> {
        	// Get a random barcode from the available barcoded products
        	ArrayList<Barcode> barcodes = AttendantLoginPage.getBarcodes();    	
        	Random random = new Random();   	
            int randomIndex = random.nextInt(barcodes.size());
            Barcode barcode = barcodes.get(randomIndex);
            
            // Get the weight of that product
            double productWeight = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode).getExpectedWeight();
            Mass mass = new Mass(productWeight);
            BarcodedItem barcodedItem = new BarcodedItem(barcode, mass);
        	
        	IBarcodeScanner scanner = stationSoftwareInstance.getStationHardware().getMainScanner();
        	scanner.scan(barcodedItem);
        	
        	IElectronicScale baggingArea = stationSoftwareInstance.getStationHardware().getBaggingArea();
        	baggingArea.addAnItem(barcodedItem);
        });
        
        JButton enterPLUBtn = createButton("Enter PLU Code", null);
        JButton searchProductBtn = createButton("Search Product", null);
        JButton removeItemBtn = createButton("Remove Item", null);
        JButton doNotBagBtn = createButton("Do Not Bag", null);
        JButton viewBaggingAreaBtn = createButton("View Bagging Area", null);
        //Signal for attendant button 
        JButton helpButton = new JButton("Help");
        helpButton.setFont(new Font("Arial", Font.PLAIN, 16));
        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notifyAttendant(selectedStation);
            }
        });
        
        JButton addItem = createButton("Add Item", null);
        JButton back = createButton("Back", null);
        
        
     

        // Add ActionListener to the "Use Own Bags" button
        useOwnBagsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Call the method to handle using own bags
                handleUseOwnBags();
            }
        });
        
        // Add buttons to menu panel
        menuPanel.add(useOwnBagsBtn);
        //menuPanel.add(scanBarcodeBtn);
        //menuPanel.add(enterPLUBtn);
        //menuPanel.add(searchProductBtn);
        menuPanel.add(addItem);
        menuPanel.add(removeItemBtn);
        menuPanel.add(doNotBagBtn);
        menuPanel.add(viewBaggingAreaBtn);
        menuPanel.add(helpButton);
        
        
         addItemPanel = new JPanel(new GridLayout(2, 2,10,10));
         addItemPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
         
         addItemPanel.add(scanBarcodeBtn);
         addItemPanel.add(enterPLUBtn);
         addItemPanel.add(searchProductBtn);
         addItemPanel.add(back);
         
         addItem.addActionListener(e -> {
        	 replaceGrids();
         });

         back.addActionListener(e ->{
        	 replaceGrids();
         });
        // Cart panel
        cartPanel = new JPanel(new BorderLayout());
        cartPanel.setBorder(BorderFactory.createTitledBorder("Cart"));

        // Cart contents
        cartTextArea = new JTextArea(20, 30);
        cartTextArea.setEditable(false);
        JScrollPane cartScrollPane = new JScrollPane(cartTextArea);

        // Total price label
        totalPriceLabel = new JLabel("Total Price: $0.00");
        totalPriceLabel.setFont(totalPriceLabel.getFont().deriveFont(Font.BOLD, 16));
        JPanel totalPricePanel = new JPanel(new BorderLayout());
        totalPricePanel.add(totalPriceLabel, BorderLayout.WEST);

        // Pay button
        JButton payButton = new JButton("Pay");
        payButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Perform payment action here
                JOptionPane.showMessageDialog(CustomerStation.this, "Payment processed successfully!");
                // Clear cart after payment
                cartTextArea.setText("");
                totalPriceLabel.setText("Total Price: $0.00");
            }
        });
        
        payButton.setFont(payButton.getFont().deriveFont(Font.BOLD, 16));
        payButtonPanel = new JPanel(new BorderLayout());
        payButtonPanel.add(payButton, BorderLayout.CENTER);

        // Add components to cart panel
        cartPanel.add(cartScrollPane, BorderLayout.CENTER);
        cartPanel.add(totalPricePanel, BorderLayout.NORTH);
        cartPanel.add(payButtonPanel, BorderLayout.SOUTH);

        // Add panels to main panel
        mainPanel.add(menuPanel, BorderLayout.CENTER);
        mainPanel.add(cartPanel, BorderLayout.EAST);
        
        //PLU panel
        PLUPanel = new JPanel(new BorderLayout());
        
        // PLU code keypad
        JPanel keypadPanel = createKeypadPanel();
        
        // PLU code screen
        JPanel screenPanel = createScreenPanel();
        
        PLUPanel.add(keypadPanel);
        PLUPanel.add(screenPanel, BorderLayout.NORTH);
        
        enterPLUBtn.addActionListener(e -> {
        	replaceCartPanelWithKeypadPanel();
        });
        
        // Add main panel to frame
        add(mainPanel);

        // Add sample products to the cart
        addProductToCart("Apple", 1.99);
        addProductToCart("Banana", 0.99);

        // Set frame visible
        setVisible(true);
    }
    
    private void handleUseOwnBags() {
    	
        // Call the method in the AddownBag instance to handle bagging
    	   // Initialize the AddownBag instance
         addOwnBag = new AddownBag(stationSoftwareInstance, scale);
    }
    
    private void notifyAttendant(int selectedStation) {
        // Notify the AttendantPageGUI that assistance is required at this station
        AttendantPageGUI.notifyAssistanceRequired(selectedStation-1);
    }
    
    // I MADE THIS PUBLIC IDK IF IM RIGHT BUT IM USING THIS IN COORDINATION!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public void addProductToCart(String productName, double price) {
        cartTextArea.append(productName + " - $" + price + "\n");
        double currentTotal = Double.parseDouble(totalPriceLabel.getText().replace("Total Price: $", ""));
        currentTotal += price;
        totalPriceLabel.setText("Total Price: $" + String.format("%.2f", currentTotal));
    }

    private JButton createButton(String text, ActionListener actionListener) {
        JButton button = new JButton("<html><center>" + text + "</center></html>");
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        if (actionListener != null) {
            button.addActionListener(actionListener);
        }
        return button;
    }

    public void askForMembershipNumber() {
    	MembershipNumberInput dialog = new MembershipNumberInput(this);
        dialog.frameInit();
    }
    
    public JPanel createKeypadPanel() {
    	JPanel keypadPanel = new JPanel();
    	keypadPanel.setLayout(new GridLayout(4,4,10,10));
    	keypadPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    	
    	for (int i = 1; i <= 9; i++) {
            JButton button = new JButton(Integer.toString(i));
            button.setFont(new Font("Arial", Font.PLAIN, 16));
            keypadPanel.add(button);
            button.addActionListener(addNum);
            
        }
    	
    	JButton enter = new JButton("Enter");
    	enter.setFont(new Font("Arial", Font.PLAIN, 16));
    	enter.setBackground(new Color(214, 255, 217));
        enter.setForeground(new Color(42, 120, 48));
    	keypadPanel.add(enter);
    	
    	JButton button0 = new JButton("0");
    	button0.setFont(new Font("Arial", Font.PLAIN, 16));
    	keypadPanel.add(button0);
    	button0.addActionListener(addNum);
    	
    	JButton delete = new JButton("Delete");
    	delete.setFont(new Font("Arial", Font.PLAIN, 16));
        delete.setBackground(new Color(227, 188, 188));
        delete.setForeground(Color.RED);
    	keypadPanel.add(delete);
    	
    	delete.addActionListener(e -> {
        	String currentText = screenTextField.getText();
        	if (!currentText.isEmpty()) {
                 // Remove the last character from the text
                 String newText = currentText.substring(0, currentText.length() - 1);
                 screenTextField.setText(newText);
        	}
        });
    	
    	
    	enter.addActionListener(e -> {
    		
    		String userInput = screenTextField.getText();
    		
    		// Convert string > plu code using method in Products.java
    		PLUCodedProduct product = stationSoftwareInstance.matchCodeAndPLUProduct(userInput);
        	
    		String description = product.getDescription();
    		
    		
    		AddtoBagging popup  = new AddtoBagging(description);
        	popup.setVisible(true);
        });
    	
    	// Return from popup:
    	 return keypadPanel;
    }
   
    ActionListener addNum = e -> {
        JButton button = (JButton) e.getSource();
        String digit = button.getText();
        String current = screenTextField.getText(); // Append the digit to the screen
        String newText = current + digit;
        
        Font currentFont = screenTextField.getFont();
        Font newFont = new Font(currentFont.getName(), currentFont.getStyle(), 24); // Adjust the font size as needed
        screenTextField.setFont(newFont);
        
        screenTextField.setText(newText);
    };
    
    public JPanel createScreenPanel() {
    	JPanel screenPanel = new JPanel(new BorderLayout());
        screenTextField = new JTextField(); // Initialize the screen text field
        screenTextField.setEditable(false); // Make it read-only
        screenTextField.setBackground(Color.WHITE);
        screenTextField.setHorizontalAlignment(JTextField.CENTER);
        screenPanel.add(screenTextField, BorderLayout.CENTER);
        screenPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        screenTextField.setPreferredSize(new Dimension(100, 50));
        
        return screenPanel;
    }
    
    private void replaceCartPanelWithKeypadPanel() {
        // Remove cart panel
    	if (cartPanel.isVisible()) {
    		cartPanel.setVisible(false);
    		getContentPane().add(PLUPanel, BorderLayout.EAST);
    		PLUPanel.setVisible(true);
            // To refresh 
            revalidate();
            repaint();
    	} else {
    		if (!PLUPanel.isVisible()) {
    			cartPanel.setVisible(true);
    		}
    	}
    }
    private void replaceGrids() {
        // Remove current panels from mainPanel
        mainPanel.remove(menuPanel);
        mainPanel.remove(addItemPanel);

        if (menuPanel.isVisible()) {
            // Hide menuPanel and show addItemPanel
            menuPanel.setVisible(false);
            addItemPanel.setVisible(true);
            
            PLUPanel.setVisible(false);

        } else {
            // Hide addItemPanel and show menuPanel
            addItemPanel.setVisible(false);
            menuPanel.setVisible(true);
            PLUPanel.setVisible(false);
            cartPanel.setVisible(true);
        }

        // Add the panels back to mainPanel with the same constraints
        mainPanel.add(menuPanel, BorderLayout.CENTER);
        mainPanel.add(addItemPanel, BorderLayout.CENTER);

        // To refresh 
        revalidate();
        repaint();
    }
    
    public void freezeGUI() {
        for (Component component : getContentPane().getComponents()) {
            component.setEnabled(false);
        }
        // Disable buttons in the menuPanel
        for (Component component : menuPanel.getComponents()) {
            if (component instanceof JButton) {
                component.setEnabled(false);
            }
        }
        // Disable pay button
        Component[] cartComponents = payButtonPanel.getComponents();
        for (Component component : cartComponents) {
            if (component instanceof JButton && ((JButton) component).getText().equals("Pay")) {
                component.setEnabled(false);
            }
        }
        
    }

    public void customerPopUp(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
    
    public void unfreezeGUI() {
        for (Component component : getContentPane().getComponents()) {
            component.setEnabled(true);
        }
        // Enable buttons in the menuPanel
        if (menuPanel != null) {
            for (Component component : menuPanel.getComponents()) {
                if (component instanceof JButton) {
                    component.setEnabled(true);
                }
            }
            // Enable pay button
            Component[] cartComponents = payButtonPanel.getComponents();
            for (Component component : cartComponents) {
                if (component instanceof JButton && ((JButton) component).getText().equals("Pay")) {
                    component.setEnabled(true);
                }
            }
        }
    } 

}
