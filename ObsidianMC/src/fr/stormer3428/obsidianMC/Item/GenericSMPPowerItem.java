package fr.stormer3428.obsidianMC.Item;

import fr.stormer3428.obsidianMC.Manager.OMCItemManager;
import fr.stormer3428.obsidianMC.Power.OMCPower;

public class GenericSMPPowerItem extends SMPPowerItem{

	public GenericSMPPowerItem(String registryName, OMCItemManager itemManager, OMCPower omcPower) {
		super(registryName, itemManager);
		this.omcPower = omcPower;
	}

	private final OMCPower omcPower;
	
	@Override
	public OMCPower getPower() {
		return omcPower;
	}

}
