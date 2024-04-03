package com.thelocalmarketplace.software;

import java.math.BigDecimal;

import com.thelocalmarketplace.hardware.Product;
import com.thelocalmarketplace.software.funds.Funds;
import com.thelocalmarketplace.software.funds.FundsObserver;
import com.thelocalmarketplace.software.funds.PaymentKind.Kind;
import com.thelocalmarketplace.software.product.ProductHandler;

public class Coordination implements FundsObserver {
    Funds funds;
    ProductHandler products;

    public Coordination(Funds funds, ProductHandler products) {
        this.funds = funds;
        this.products = products;
    }

    @Override
    public void fundsAdded(Funds fundsFacade, BigDecimal funds) {
        
    }

    @Override
    public void fundsRemoved(Funds fundsFacade, BigDecimal funds) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fundsRemoved'");
    }

    @Override
    public void fundsStored(Funds fundsFacade, BigDecimal funds) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fundsStored'");
    }

    @Override
    public void fundsInvalid(Funds fundsFacade, Kind kind) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fundsInvalid'");
    }

    @Override
    public void fundsPaidInFull(Funds fundsFacade, BigDecimal changeReturned) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fundsPaidInFull'");
    }

    @Override
    public void fundsStationBlocked(Funds fundsFacade, boolean blockedStatus) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fundsStationBlocked'");
    }
}