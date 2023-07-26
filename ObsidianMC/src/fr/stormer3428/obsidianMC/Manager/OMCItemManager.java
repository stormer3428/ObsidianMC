package fr.stormer3428.obsidianMC.Manager;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import fr.stormer3428.obsidianMC.OMCPlugin;
import fr.stormer3428.obsidianMC.Command.OMCCommand;
import fr.stormer3428.obsidianMC.Command.OMCVariable;
import fr.stormer3428.obsidianMC.Config.ConfigHolder;
import fr.stormer3428.obsidianMC.Item.OMCItem;
import fr.stormer3428.obsidianMC.Util.OMCLang;
import fr.stormer3428.obsidianMC.Util.OMCLogger;

public abstract class OMCItemManager extends ConfigHolder implements Listener{

	private final ArrayList<OMCItem> registeredItems = new ArrayList<>();

	protected abstract void registerItems();
	
	public OMCItemManager(String configName) {
		super(new File(OMCPlugin.i.getDataFolder(), configName));
	}
	
	@Override
	public void onPluginEnable() {
		super.onPluginEnable();
		OMCPlugin.i.getServer().getPluginManager().registerEvents(this, OMCPlugin.i);
		registerItems();
	}

	/**
	 * Creates a {@link OMCVariable} with the given signature that completes for registered {@link OMCItem}
	 * 
	 * @param variableSignature
	 * the signature of the variable
	 * @return the variable
	 * @see OMCCommand
	 * @see #registerItem(OMCItem)
	 */
	public OMCVariable getItemVariable(String variableSignature) {
		return new OMCVariable(variableSignature) {

			@Override
			protected ArrayList<String> complete(CommandSender sender, String incomplete) {
				String lowercase = incomplete.toLowerCase();
				ArrayList<String> list = new ArrayList<>();
				for(OMCItem item : registeredItems) {
					String uppercase = item.getRegistryName();
					String name = uppercase.toLowerCase();
					if(name.startsWith(lowercase) || name.contains(lowercase)) list.add(uppercase);
				}
				return list;
			}
		};
	}

	/**
	 * 
	 * Registers an {@link OMCItem} to the {@link OMCItemManager}. An item registered to the itemManager will show up in the {@link #getItemVariable(String)} completion list
	 * 
	 * @param item 
	 * the item to register
	 * @see #getItems()
	 */
	public void registerItem(OMCItem item) {
		if(item == null) {
			OMCLogger.systemError(OMCLang.ERROR_ITEM_MANAGER_REGISTER_NULL.toString());
			return;
		}
		if(item.getRegistryName() == null || item.getRegistryName().isBlank()) {
			OMCLogger.systemError(OMCLang.ERROR_ITEM_MANAGER_REGISTER_NULL_NAME.toString());
			return;
		}
		registeredItems.add(item);
	}

	/**
	 * 
	 * @return a copy of the registered item list
	 * @see #registerItem(OMCItem)
	 */
	public ArrayList<OMCItem> getItems() {
		return new ArrayList<>(registeredItems);
	}

	/**
	 * Returns whether or not the manager recognizes this {@link ItemStack} as a registered {@link OMCItem}
	 * </br>
	 * 
	 * @param item
	 * The item to test for
	 * @return whether it is recognized
	 */
	public boolean contains(ItemStack item) {
		return fromItemStack(item) != null;
	}

	/**
	 * Will return the corresponding {@link OMCItem} registered in this manager, or null if it is unrecognized.
	 * @implNote
	 * Uses {@link OMCItem#matches(ItemStack)} to find the correlating {@link ItemStack}
	 * 
	 * @param item
	 * The {@link ItemStack} to correlate to
	 * @return The corresponding {@link OMCItem}
	 * @see #fromName(String)
	 */
	public OMCItem fromItemStack(ItemStack item) {
		if(item == null) return null;
		for(OMCItem omcItem : registeredItems) if(omcItem.matches(item)) return omcItem;
		return null;
	}

	/**
	 * Will return the corresponding {@link OMCItem} registered in this manager, or null if it is unrecognized
	 * @implNote
	 * This method uses {@link String#equalsIgnoreCase(String)} to find the correlating {@link OMCItem}
	 * 
	 * @param item
	 * The item name to search for
	 * @return The corresponding {@link OMCItem}
	 * @see #fromItemStack(ItemStack)
	 */
	public OMCItem fromName(String item) {
		for(OMCItem omcItem : registeredItems) if(omcItem.getRegistryName().equalsIgnoreCase(item)) return omcItem;
		return null;
	}	
}












