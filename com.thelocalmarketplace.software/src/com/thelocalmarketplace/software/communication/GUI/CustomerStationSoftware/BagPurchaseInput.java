/**

 SENG 300 - ITERATION 3
 GROUP GOLD {8}

 Name                      UCID

 Yotam Rojnov             30173949
 Duncan McKay             30177857
 Mahfuz Alam              30142265
 Luis Trigueros Granillo  30167989
 Lilia Skumatova          30187339
 Abdelrahman Abbas        30110374
 Talaal Irtija            30169780
 Alejandro Cardona        30178941
 Alexandre Duteau         30192082
 Grace Johnson            30149693
 Abil Momin               30154771
 Tara Ghasemi M. Rad      30171212
 Izabella Mawani          30179738
 Binish Khalid            30061367
 Fatima Khalid            30140757
 Lucas Kasdorf            30173922
 Emily Garcia-Volk        30140791
 Yuinikoru Futamata       30173228
 Joseph Tandyo            30182561
 Syed Haider              30143096
 Nami Marwah              30178528

 */

package com.thelocalmarketplace.software.communication.GUI.CustomerStationSoftware;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


/**
 * A dialog window for purchasing bags.
 */
public class BagPurchaseInput extends JFrame {
    private int numOfBags = 0;
    public JPanel keypadPanel;
    public JPanel confirmPanel;
    
    /**
     * Constructs a dialog window for purchasing bags.
     * 
     * @param owner 
     * 			The Frame from which the dialog is displayed.
     */
    public BagPurchaseInput(ActionListener callback) {
        setTitle("Purchase Bags");
        initializeUI(callback);
    }
    
    /**
     * Initializes the user interface components.
     */
    private void initializeUI(ActionListener callback) {
        setSize(300, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JTextField inputField = new JTextField();
        inputField.setEditable(false);
        inputField.setHorizontalAlignment(JTextField.CENTER);
        add(inputField, BorderLayout.NORTH);

        keypadPanel = new JPanel(new GridLayout(4, 3));
        ActionListener numPadActionListener = e -> { inputField.setText(inputField.getText() + ((AbstractButton) e.getSource()).getText()); };

        for (int i = 1; i <= 9; i++) {
            JButton button = new JButton(Integer.toString(i));
            button.setFont(new Font("Arial", Font.PLAIN, 16));
            keypadPanel.add(button);
            button.addActionListener(numPadActionListener);
        }

        keypadPanel.add(new JLabel(""));
        JButton zeroButton = new JButton("0");
        zeroButton.addActionListener(numPadActionListener);
        keypadPanel.add(zeroButton);
        keypadPanel.add(new JLabel(""));

        add(keypadPanel, BorderLayout.CENTER);

        confirmPanel = new JPanel(new FlowLayout());
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            String input = inputField.getText();
            if (!input.isEmpty()) {
                try {
                    numOfBags = Integer.parseInt(input);
                    callback.actionPerformed(e);
                    getDefaultCloseOperation();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid number. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());

        confirmPanel.add(okButton);
        confirmPanel.add(cancelButton);

        add(confirmPanel, BorderLayout.SOUTH);
    }
    
    
    /**
     * Retrieves the number of bags entered by the user.
     * 
     * @return The number of bags.
     */
    public int getNumOfBags() {
        return numOfBags;
    }
}
