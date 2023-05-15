package fr.stormer3428.obsidianMC.Item;

import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import fr.stormer3428.obsidianMC.Manager.OMCItemManager;

/**
 * Represents a custom {@link ItemStack}
 * @author stormer3428
 * @see SMPItem
 * @see OMCItemManager
 *
 */
public interface OMCItem {

	public ItemStack createItemsStack(int amount);
	public boolean matches(ItemStack other);
	public boolean matches(OMCItem other);
	public String getRegistryName();
	
	public List<Recipe> getRecipes();
}
