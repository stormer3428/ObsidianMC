package fr.stormer3428.obsidianMC.Item;

import fr.stormer3428.obsidianMC.Manager.OMCItemManager;
import fr.stormer3428.obsidianMC.Power.OMCPower;

public abstract class SMPPowerItem extends SMPItem{

	public SMPPowerItem(String registryName, OMCItemManager itemManager) {
		super(registryName, itemManager);
	}
	
	public abstract OMCPower getPower();

	
}
