/**

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

package com.thelocalmarketplace.software.funds;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import com.jjjwelectronics.EmptyDevice;
import com.jjjwelectronics.OverloadedDevice;
import com.tdc.CashOverloadException;
import com.tdc.DisabledException;
import com.tdc.IComponent;
import com.tdc.IComponentObserver;
import com.tdc.NoCashAvailableException;
import com.tdc.coin.Coin;
import com.tdc.coin.CoinDispenserObserver;
import com.tdc.coin.CoinValidator;
import com.tdc.coin.CoinValidatorObserver;
import com.tdc.coin.ICoinDispenser;

/**
 * Handles coin payment events, implementing observer interfaces for CoinValidator and CoinDispenser.
 */
public class CoinHandler implements CoinValidatorObserver, CoinDispenserObserver {
    
    private Funds fundController = null;
    
    /**
     * Constructs a CoinHandler with a given fund controller.
     * 
     * @param fundController The fund controller to associate with this handler.
     */
    public CoinHandler(Funds fundController) {
        this.fundController = fundController;
    }
    
    /**
     * Handles the event when a valid coin is detected by the CoinValidator.
     * 
     * @param validator The CoinValidator detecting the coin.
     * @param value The value of the detected coin.
     */
    @Override
    public void validCoinDetected(CoinValidator validator, BigDecimal value)  {
        this.fundController.totalPaid.add(value);
        this.fundController.notifyFundsAdded(value);
        BigDecimal amountDue = new BigDecimal(this.fundController.checkoutStationSoftware.getTotalOrderPrice()).subtract(this.fundController.totalPaid);
        if (amountDue.compareTo(BigDecimal.ZERO) <= 0) {
            amountDue = amountDue.abs();
            
            boolean missed = false;
            try {
                missed = this.fundController.dispenseAccurateChange(amountDue);
            } catch (DisabledException | CashOverloadException  | EmptyDevice | NoCashAvailableException
                    | OverloadedDevice e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            if (missed) {                
                this.fundController.notifyPaidFunds(amountDue);
            }
        }
    }

    /**
     * Handles the event when an invalid coin is detected by the CoinValidator.
     * 
     * @param validator The CoinValidator detecting the invalid coin.
     */
    @Override
    public void invalidCoinDetected(CoinValidator validator) {
        this.fundController.notifyInvalidFunds(PaymentKind.Kind.CASH);
    }
    
    /**
     * Adds a coin to the list of available coins when a coin is added to the dispenser.
     * 
     * @param dispenser The coin dispenser where the coin is added.
     * @param coin The coin added to the dispenser.
     */
    @Override
	public void coinAdded(ICoinDispenser dispenser, Coin coin) {
        this.fundController.coinsAvailable.put(coin.getValue(), (int)this.fundController.coinsAvailable.get(coin.getValue()) + 1);
    }

    /**
     * Removes a coin from the list of available coins when a coin is removed from the dispenser.
     * 
     * @param dispenser The coin dispenser where the coin is removed.
     * @param coin The coin removed from the dispenser.
     * @throws NullPointerException If the coin being removed is not available.
     */
    @Override
    public void coinRemoved(ICoinDispenser dispenser, Coin coin) {
        if((int)this.fundController.coinsAvailable.get(coin.getValue()) > 0) {
            this.fundController.coinsAvailable.put(coin.getValue(), (int)this.fundController.coinsAvailable.get(coin.getValue()) - 1);
        }
        else {
            throw new NullPointerException();
        }
    }
    
    /**
     * Loads coins into the dispenser and updates the list of available coins.
     * 
     * @param dispenser The coin dispenser where coins are loaded.
     * @param coins The coins loaded into the dispenser.
     */
    @Override
    public void coinsLoaded(ICoinDispenser dispenser, Coin... coins) {
        for (Coin c : coins) {
            this.fundController.coinsAvailable.put(c.getValue(), (int)this.fundController.coinsAvailable.get(c.getValue()) + 1);
        }
    }

    /**
     * Unloads coins from the dispenser and updates the list of available coins.
     * 
     * @param dispenser The coin dispenser where coins are unloaded.
     * @param coins The coins unloaded from the dispenser.
     * @throws NullPointerException If any unloaded coin is not available.
     */
    @Override
    public void coinsUnloaded(ICoinDispenser dispenser, Coin... coins) {
        for (Coin c : coins) {
            if((int)this.fundController.coinsAvailable.get(c.getValue()) > 0) {
                this.fundController.coinsAvailable.put(c.getValue(), (int)this.fundController.coinsAvailable.get(c.getValue()) - 1);
            }
            else {
                throw new NullPointerException();
            }
        }
    }

    // not useful to us
    
    @Override
    public void enabled(IComponent<? extends IComponentObserver> component) {
        // TODO Auto-generated method stub
    }

    @Override
    public void disabled(IComponent<? extends IComponentObserver> component) {
        // TODO Auto-generated method stub
    }

    @Override
    public void turnedOn(IComponent<? extends IComponentObserver> component) {
        // TODO Auto-generated method stub
    }

    @Override
    public void turnedOff(IComponent<? extends IComponentObserver> component) {
        // TODO Auto-generated method stub    
    }

    @Override
    public void coinsFull(ICoinDispenser dispenser) {
        // TODO Auto-generated method stub
    }
    
    /**
     * Clears the list of available coins when the coin dispenser is empty.
     * 
     * @param dispenser The coin dispenser that is empty.
     */
    @Override
    public void coinsEmpty(ICoinDispenser dispenser) {
    }


}