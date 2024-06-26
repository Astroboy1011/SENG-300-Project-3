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

package com.thelocalmarketplace.software.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.jjjwelectronics.EmptyDevice;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.Numeral;
import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.printer.IReceiptPrinter;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodedItem;

import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.PLUCodedItem;
import com.thelocalmarketplace.hardware.PLUCodedProduct;
import com.thelocalmarketplace.hardware.PriceLookUpCode;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.external.ProductDatabases;

import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;

import com.jjjwelectronics.printer.ReceiptPrinterBronze;

import com.thelocalmarketplace.software.funds.*;

import powerutility.PowerGrid;

public class ReceiptTest {
    private Receipt receipt;

    private Funds funds;
    private SelfCheckoutStationSoftware station;
    private IReceiptPrinter printer;

    @Before
    public void setUp() {
        this.station = new SelfCheckoutStationSoftware(new SelfCheckoutStationBronze());
        this.funds = new Funds(this.station);
        this.printer = this.station.getStationHardware().getPrinter();
        this.receipt = new Receipt(printer, funds);
        PowerGrid.engageUninterruptiblePowerSource();
        this.station.station.plugIn(PowerGrid.instance());
        this.station.station.turnOn();
    }

    @Test
    public void testReceiptPrinterWithBarcodedProduct() throws EmptyDevice, OverloadedDevice {
        Numeral[] barcodeDigits = {Numeral.one, Numeral.two, Numeral.three, Numeral.four, Numeral.five};
        Barcode barcode = new Barcode(barcodeDigits);
        Mass itemMass = new Mass(1000000000); // 1kg in micrograms
        BarcodedItem barcodedItem = new BarcodedItem(barcode, itemMass);

        // Initializing mock product (using same barcode as the barcoded item)
        String barcodeProductDescription = "banana";
        long barcodeProductPrice = 5;
        double barcodeProductWeightInGrams = 1000;
        BarcodedProduct barcodedProduct = new BarcodedProduct(barcode, barcodeProductDescription, barcodeProductPrice, barcodeProductWeightInGrams);

        // Adding mock product into product database
        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode, barcodedProduct);

        this.station.addItemToOrder(barcodedItem);
        this.station.addTotalOrderPrice(barcodeProductPrice);
        this.funds.addToTotalPaid(new BigDecimal(5));

        this.receipt.receiptPrinter.addInk(ReceiptPrinterBronze.MAXIMUM_INK);
        this.receipt.receiptPrinter.addPaper(ReceiptPrinterBronze.MAXIMUM_PAPER);

        this.receipt.printReceipt();
    }

    @Test
    public void testReceiptPrinterWithPLUProduct() throws EmptyDevice, OverloadedDevice {
        String pluDigits = "0001";
        PriceLookUpCode pluCode = new PriceLookUpCode(pluDigits);
        Mass mass = new Mass(1000000000); // Converts the weight of the product to a mass
        PLUCodedItem pluCodeItem = new PLUCodedItem(pluCode, mass);


        String pluCodeProductDescription = "orange";
        long pluCodeProductPrice = 10;

        PLUCodedProduct pluProduct = new PLUCodedProduct(pluCode, pluCodeProductDescription, pluCodeProductPrice);

        ProductDatabases.PLU_PRODUCT_DATABASE.put(pluCode, pluProduct);

        this.station.addItemToOrder(pluCodeItem);
        this.station.addTotalOrderPrice(pluCodeProductPrice);
        this.funds.addToTotalPaid(new BigDecimal(5));

        this.receipt.receiptPrinter.addInk(ReceiptPrinterBronze.MAXIMUM_INK);
        this.receipt.receiptPrinter.addPaper(ReceiptPrinterBronze.MAXIMUM_PAPER);

        this.receipt.printReceipt();
    }

    @Test (expected = IllegalArgumentException.class)
    public void testReceiptPrinterWithUnsupportedProduct() throws EmptyDevice, OverloadedDevice {
        this.station.addItemToOrder(null);

        this.station.addTotalOrderPrice(5);
        this.funds.addToTotalPaid(new BigDecimal(5));

        this.receipt.printReceipt();
    }

    @Test
    public void testRegisterAndNotifyReceiptPrinted() {
        mockReceiptObserver observer = new mockReceiptObserver();

        this.receipt.register(observer);
        this.receipt.notifyReceiptPrinted(new ArrayList<>());

        assertTrue(observer.receiptPrintedCalled);
    }

    @Test
    public void testDeregister() {
        mockReceiptObserver observer = new mockReceiptObserver();

        this.receipt.register(observer);
        this.receipt.deregister(observer);
        this.receipt.notifyReceiptPrinted(new ArrayList<>());

        assertFalse(observer.receiptPrintedCalled);
    }

    @Test
    public void testNotifyInkEmpty() {
        mockReceiptObserver observer = new mockReceiptObserver();

        this.receipt.register(observer);
        this.receipt.notifyInkEmpty(printer);

        assertTrue(observer.noInkCalled);
    }

    @Test
    public void testNotifyPaperEmpty() {
        mockReceiptObserver observer = new mockReceiptObserver();

        this.receipt.register(observer);
        this.receipt.notifyPaperEmpty(printer);

        assertTrue(observer.noPaperCalled);
    }

    @Test
    public void testNotifyPaperLow() {
        mockReceiptObserver observer = new mockReceiptObserver();

        this.receipt.register(observer);
        this.receipt.notifyPaperLow(printer);

        assertTrue(observer.lowPaperCalled);
    }

    @Test
    public void testNotifyInkLow() {
        mockReceiptObserver observer = new mockReceiptObserver();

        this.receipt.register(observer);
        this.receipt.notifyInkLow(printer);

        assertTrue(observer.lowInkCalled);
    }

    @Test
    public void testNotifyInkAdded() {
        mockReceiptObserver observer = new mockReceiptObserver();

        this.receipt.register(observer);
        this.receipt.notifyInkAdded(printer);

        assertTrue(observer.inkAddedCalled);
    }

    @Test
    public void testNotifyPaperAdded() {
        mockReceiptObserver observer = new mockReceiptObserver();

        this.receipt.register(observer);
        this.receipt.notifyPaperAdded(printer);

        assertTrue(observer.paperAddedCalled);
    }
}