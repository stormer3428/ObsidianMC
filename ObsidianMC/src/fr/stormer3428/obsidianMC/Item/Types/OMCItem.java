package fr.stormer3428.obsidianMC.Item.Types;

import java.util.List;

import org.bukkit.inventory.ItemStack;

import fr.stormer3428.obsidianMC.Item.OMCItemManager;
import fr.stormer3428.obsidianMC.Power.Types.OMCPower;

/**
 * Represents a custom {@link ItemStack}
 * @author stormer3428
 * @see SMPItem
 * @see OMCItemManager
 *
 */
public interface OMCItem {

	public List<OMCPower> getPowers();
	public ItemStack createItemsStack(int amount);
	public boolean equals(ItemStack other);
	public String getRegistryName();
	
}
