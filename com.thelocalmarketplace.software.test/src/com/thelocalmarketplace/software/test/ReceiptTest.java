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

import java.math.BigDecimal;

import org.junit.*;

import com.jjjwelectronics.EmptyDevice;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.Numeral;
import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodedItem;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.ISelfCheckoutStation;
import com.thelocalmarketplace.hardware.PLUCodedItem;
import com.thelocalmarketplace.hardware.PLUCodedProduct;
import com.thelocalmarketplace.hardware.PriceLookUpCode;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.external.ProductDatabases;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.funds.*;

public class ReceiptTest {

    private Receipt receipt;
    private Funds funds;
    private SelfCheckoutStationSoftware station;
    private BarcodedItem barcodedItem;
    private BarcodedProduct barcodedProduct;
    private PLUCodedItem pluCodeItem;
    private PLUCodedProduct pluProduct;

    @Before
    public void setUp() {
        this.station = new SelfCheckoutStationSoftware(new SelfCheckoutStationBronze());
        this.funds = new Funds(this.station);
        this.receipt = new Receipt(this.station.getStationHardware().getPrinter(), funds, station);

    }

    @Test
    public void testReceiptPrinterWithBarcodedProduct() throws EmptyDevice, OverloadedDevice {

        Numeral[] barcodeDigits = {Numeral.one, Numeral.two, Numeral.three, Numeral.four, Numeral.five};
        Barcode barcode = new Barcode(barcodeDigits);
        Mass itemMass = new Mass(1000000000); // 1kg in micrograms
        barcodedItem = new BarcodedItem(barcode, itemMass);

        // Initializing mock product (using same barcode as the barcoded item)
        String barcodeProductDescription = "banana";
        long barcodeProductPrice = 5;
        double barcodeProductWeightInGrams = 1000;
        barcodedProduct = new BarcodedProduct(barcode, barcodeProductDescription, barcodeProductPrice, barcodeProductWeightInGrams);

        // Adding mock product into product database
        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode, barcodedProduct);

        this.station.addItemToOrder(barcodedItem);
        this.station.addTotalOrderPrice(barcodeProductPrice);
        this.funds.addToTotalPaid(new BigDecimal(5));

        this.receipt.printReceipt();

    }

    @Test
    public void testReceiptPrinterWithPLUProduct() throws EmptyDevice, OverloadedDevice {

        String pluDigits = "0001";
        PriceLookUpCode pluCode = new PriceLookUpCode(pluDigits);
        Mass mass = new Mass(1000000000); // Converts the weight of the product to a mass
        pluCodeItem = new PLUCodedItem(pluCode, mass);


        String pluCodeProductDescription = "orange";
        long pluCodeProductPrice = 10;

        pluProduct = new PLUCodedProduct(pluCode, pluCodeProductDescription, pluCodeProductPrice);

        ProductDatabases.PLU_PRODUCT_DATABASE.put(pluCode, pluProduct);

        this.station.addItemToOrder(pluCodeItem);
        this.station.addTotalOrderPrice(pluCodeProductPrice);
        this.funds.addToTotalPaid(new BigDecimal(5));

        this.receipt.printReceipt();


    }

    @Test (expected = NullPointerException.class)
    public void testReceiptPrinterWithUnsupportedProduct() throws EmptyDevice, OverloadedDevice {
        this.station.addItemToOrder(null);

        this.station.addTotalOrderPrice(5);
        this.funds.addToTotalPaid(new BigDecimal(5));

        this.receipt.printReceipt();
    }


    @Test
    public void testReceiptPrinterWithLowPaper() throws EmptyDevice, OverloadedDevice {
        String pluDigits = "0001";
        PriceLookUpCode pluCode = new PriceLookUpCode(pluDigits);
        Mass mass = new Mass(1000000000); // Converts the weight of the product to a mass
        pluCodeItem = new PLUCodedItem(pluCode, mass);


        String pluCodeProductDescription = "orange";
        long pluCodeProductPrice = 10;

        pluProduct = new PLUCodedProduct(pluCode, pluCodeProductDescription, pluCodeProductPrice);

        ProductDatabases.PLU_PRODUCT_DATABASE.put(pluCode, pluProduct);


        int paperRemaining = this.station.getStationHardware().getPrinter().paperRemaining();
        System.out.println(paperRemaining);




        for (int i = 0; i < this.station.getStationHardware().getPrinter().MAXIMUM_PAPER - 1; i++) {

            this.station.addItemToOrder(pluCodeItem);

        }


        this.receipt.printReceipt();



    }


    @Test
    public void testReceiptPrinterOutOfPaper() throws EmptyDevice, OverloadedDevice {
        String pluDigits = "0001";
        PriceLookUpCode pluCode = new PriceLookUpCode(pluDigits);
        Mass mass = new Mass(1000000000); // Converts the weight of the product to a mass
        pluCodeItem = new PLUCodedItem(pluCode, mass);


        String pluCodeProductDescription = "orange";
        long pluCodeProductPrice = 10;

        pluProduct = new PLUCodedProduct(pluCode, pluCodeProductDescription, pluCodeProductPrice);

        ProductDatabases.PLU_PRODUCT_DATABASE.put(pluCode, pluProduct);


        int paperRemaining = this.station.getStationHardware().getPrinter().paperRemaining();
        System.out.println(paperRemaining);




        for (int i = 0; i < this.station.getStationHardware().getPrinter().MAXIMUM_PAPER + 1; i++) {

            this.station.addItemToOrder(pluCodeItem);

        }


        this.receipt.printReceipt();



    }


    @Test
    public void testReceiptPrinterWithLowInk() throws EmptyDevice, OverloadedDevice {
        String pluDigits = "0001";
        PriceLookUpCode pluCode = new PriceLookUpCode(pluDigits);
        Mass mass = new Mass(1000000000); // Converts the weight of the product to a mass
        pluCodeItem = new PLUCodedItem(pluCode, mass);


        String pluCodeProductDescription = "orange";
        long pluCodeProductPrice = 10;

        pluProduct = new PLUCodedProduct(pluCode, pluCodeProductDescription, pluCodeProductPrice);

        ProductDatabases.PLU_PRODUCT_DATABASE.put(pluCode, pluProduct);


        int paperRemaining = this.station.getStationHardware().getPrinter().inkRemaining();
        System.out.println(paperRemaining);




        for (int i = 0; i < this.station.getStationHardware().getPrinter().MAXIMUM_INK - 1; i++) {

            this.station.addItemToOrder(pluCodeItem);

        }


        this.receipt.printReceipt();



    }



    @Test
    public void testReceiptPrinterOutOfInk() throws EmptyDevice, OverloadedDevice {
        String pluDigits = "0001";
        PriceLookUpCode pluCode = new PriceLookUpCode(pluDigits);
        Mass mass = new Mass(1000000000); // Converts the weight of the product to a mass
        pluCodeItem = new PLUCodedItem(pluCode, mass);


        String pluCodeProductDescription = "orange";
        long pluCodeProductPrice = 10;

        pluProduct = new PLUCodedProduct(pluCode, pluCodeProductDescription, pluCodeProductPrice);

        ProductDatabases.PLU_PRODUCT_DATABASE.put(pluCode, pluProduct);


        int paperRemaining = this.station.getStationHardware().getPrinter().inkRemaining();
        System.out.println(paperRemaining);




        for (int i = 0; i < this.station.getStationHardware().getPrinter().MAXIMUM_INK + 1; i++) {

            this.station.addItemToOrder(pluCodeItem);

        }


        this.receipt.printReceipt();



    }






}