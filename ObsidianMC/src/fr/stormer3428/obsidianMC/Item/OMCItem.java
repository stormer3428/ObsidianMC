package fr.stormer3428.obsidianMC.Item;

import org.bukkit.inventory.ItemStack;

public interface OMCItem {

	public ItemStack createItemsStack(int amount);
	public boolean matches(ItemStack other);
	public boolean matches(OMCItem other);
	public String getRegistryName();
}
