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

import com.jjjwelectronics.Item;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.scale.AbstractElectronicScale;

import powerutility.PowerGrid;

public class MockScale extends AbstractElectronicScale {
	
	//mockScale constructor 
	protected Mass sensitivityLimit1;
	public MockScale(Mass limit, Mass sensitivityLimit) {
		super(limit, sensitivityLimit);
		sensitivityLimit1 = limit; 
		massLimit = limit; 
	}

	@Override 
	public Mass getMassLimit() {
		return massLimit;
	}
	
	@Override
	public Mass getSensitivityLimit() {
		return sensitivityLimit1; 
	}
	
	@Override 
	public synchronized void addAnItem(Item item) {
		currentMass = currentMass.sum(item.getMass());
		items.add(item);
	}
	
	@Override
	public Mass getCurrentMassOnTheScale() throws OverloadedDevice {
		return currentMass; 
	}
	
	public void plugIn(PowerGrid grid) {}
	public void turnOn() {}
	public void enable() {}
}