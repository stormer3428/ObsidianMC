package fr.stormer3428.obsidianMC.Item;

import fr.stormer3428.obsidianMC.Manager.OMCItemManager;
import fr.stormer3428.obsidianMC.Manager.OMCPowerManager;
import fr.stormer3428.obsidianMC.Power.OMCPower;

/**
 * Represents an {@link SMPItem} that is linked to an {@link OMCPower}
 * @author stormer3428
 * @see GenericSMPPowerItem
 * @see OMCPower
 * @see OMCPowerManager
 */
public abstract class SMPPowerItem extends SMPItem{

	public SMPPowerItem(String registryName, OMCItemManager itemManager) {
		super(registryName, itemManager);
	}
	
	public abstract OMCPower getPower();

	
}
