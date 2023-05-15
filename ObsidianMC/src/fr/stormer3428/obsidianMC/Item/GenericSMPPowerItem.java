package fr.stormer3428.obsidianMC.Item;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.stormer3428.obsidianMC.Manager.OMCItemManager;
import fr.stormer3428.obsidianMC.Manager.OMCPowerManager;
import fr.stormer3428.obsidianMC.Power.OMCPower;

/**
 * An implementation of {@link SMPPowerItem}, it does not handle the casting of the power. <br>
 * @implNote
 * Should be registered to an {@link OMCItemManager} that should listen to events on it's own and determine by itself the trigger mechanism for calling {@link #getPower()} {@link OMCPower#tryCast(ItemStack, Player)}
 * @author stormer3428
 * @see OMCPower
 * @see OMCPowerManager
 *
 */
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
