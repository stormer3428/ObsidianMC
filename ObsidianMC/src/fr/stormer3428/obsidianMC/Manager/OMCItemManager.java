package fr.stormer3428.obsidianMC.Manager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import fr.stormer3428.obsidianMC.OMCPlugin;
import fr.stormer3428.obsidianMC.Command.OMCCommand;
import fr.stormer3428.obsidianMC.Command.OMCVariable;
import fr.stormer3428.obsidianMC.Config.ConfigHolder;
import fr.stormer3428.obsidianMC.Item.OMCItem;
import fr.stormer3428.obsidianMC.Item.SMPPowerItem;
import fr.stormer3428.obsidianMC.Power.OMCPassivePower;
import fr.stormer3428.obsidianMC.Util.OMCLang;
import fr.stormer3428.obsidianMC.Util.OMCUtil;

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
		registerRecipes();

		for(Player p : Bukkit.getOnlinePlayers()) {
			OMCPassivePower mainPassive = getPassive(fromItemStack(p.getInventory().getItemInMainHand()));
			if(mainPassive != null) mainPassive.onStartHolding(p, false);
			OMCPassivePower offPassive = getPassive(fromItemStack(p.getInventory().getItemInOffHand()));
			if(offPassive != null) offPassive.onStartHolding(p, true);
			MAIN_HAND_MAP.put(p.getUniqueId(), mainPassive);
			OFF_HAND_MAP.put(p.getUniqueId(), offPassive);
		}
	}

	@Override
	public void onPluginDisable() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			OMCPassivePower mainPassive = getPassive(fromItemStack(p.getInventory().getItemInMainHand()));
			if(mainPassive != null) mainPassive.onStopHolding(p, false);
			OMCPassivePower offPassive = getPassive(fromItemStack(p.getInventory().getItemInOffHand()));
			if(offPassive != null) offPassive.onStopHolding(p, true);
			MAIN_HAND_MAP.put(p.getUniqueId(), mainPassive);
			OFF_HAND_MAP.put(p.getUniqueId(), offPassive);
		}
	}

	public void registerRecipes() {
		for(OMCItem item : registeredItems) for(Recipe recipe : item.getRecipes()){
			Bukkit.removeRecipe(OMCUtil.getNameSpacedKeyFromRecipe(recipe));
			Bukkit.addRecipe(recipe);
		}		
	}

	@Override
	public void onPluginReload() {
		super.onPluginReload();
		registerRecipes();
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
	 * Creates a {@link OMCVariable} with the default signature "%ITEM%" that completes for registered {@link OMCItem}
	 * 
	 * @param variableSignature
	 * the signature of the variable
	 * @return the variable
	 * @see OMCCommand
	 * @see #registerItem(OMCItem)
	 */
	public OMCVariable getItemVariable() {
		return getItemVariable("%ITEM%");
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


	//	@EventHandler
	//	public void onClick(PlayerInteractEvent e) {
	//		if(!Arrays.asList(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK).contains(e.getAction())) return;
	//		ItemStack it = e.getItem();
	//		if(it == null) return;
	//		Player p = e.getPlayer();
	//		OMCItem omcItem = fromItemStack(it);
	//		if(omcItem == null) return;
	//		if(!(omcItem instanceof SMPPowerItem powerItem)) return;
	//		OMCPower power = powerItem.getPower();
	//		if(power == null) return;
	//		power.tryCast(it, p);
	//	}

	private final HashMap<UUID, OMCPassivePower> MAIN_HAND_MAP = new HashMap<>();
	private final HashMap<UUID, OMCPassivePower> OFF_HAND_MAP = new HashMap<>();


	public void checkHoldingMaps(Player p) {
		Bukkit.getScheduler().runTaskLater(OMCPlugin.i, () -> {

			OMCItem itemMain = fromItemStack(p.getInventory().getItemInMainHand());
			OMCItem itemOff = fromItemStack(p.getInventory().getItemInOffHand());

			OMCPassivePower oldMain = MAIN_HAND_MAP.get(p.getUniqueId());
			OMCPassivePower currentMain = getPassive(itemMain);
			OMCPassivePower oldOff = OFF_HAND_MAP.get(p.getUniqueId());
			OMCPassivePower currentOff = getPassive(itemOff);

			MAIN_HAND_MAP.put(p.getUniqueId(), currentMain);
			OFF_HAND_MAP.put(p.getUniqueId(), currentOff);

			HashMap<OMCPassivePower, Boolean> toStop = new HashMap<>();
			HashMap<OMCPassivePower, Boolean> toStart = new HashMap<>();
			HashMap<OMCPassivePower, Boolean> toSwap = new HashMap<>();

			if(oldMain != currentMain) {			
				if(oldMain != null) {
					if(oldMain == currentOff) toSwap.put(oldMain, true);
					else toStop.put(oldMain, false);
				}
				if(currentMain != null) toStart.put(currentMain, false);
			}
			
			if(oldOff != currentOff) {
				if(oldOff != null) {
					if(oldOff == currentMain) toSwap.put(oldOff, false);
					else toStop.put(oldOff, true);
				}
				if(currentOff != null) toStart.put(currentOff, true);
			}

			for(Entry<OMCPassivePower, Boolean> entry : toStop.entrySet()) if(!toStart.containsKey(entry.getKey()) && !toSwap.containsKey(entry.getKey())) entry.getKey().onStopHolding(p, entry.getValue());
			for(Entry<OMCPassivePower, Boolean> entry : toStart.entrySet()) if(!toStop.containsKey(entry.getKey()) && !toSwap.containsKey(entry.getKey())) entry.getKey().onStartHolding(p, entry.getValue());
			for(Entry<OMCPassivePower, Boolean> entry : toSwap.entrySet()) entry.getKey().onSwap(p, entry.getValue());
		}, 1);
	}

	public static OMCPassivePower getPassive(OMCItem item) {
		if(!(item != null && item instanceof SMPPowerItem powerItem && powerItem.getPower() != null && powerItem.getPower() instanceof OMCPassivePower power)) return null;
		return power;
	}

	@EventHandler
	public void onSlotChange(PlayerItemHeldEvent e) {
		Player p = e.getPlayer();
		checkHoldingMaps(p);
	}

	@EventHandler
	public void onSwap(PlayerSwapHandItemsEvent e) {
		Player p = e.getPlayer();
		checkHoldingMaps(p);
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if(!(e.getWhoClicked() instanceof Player p)) return;
		checkHoldingMaps(p);
	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		checkHoldingMaps(e.getPlayer());
	}

	@EventHandler
	public void onPickup(EntityPickupItemEvent e) {
		if(!(e.getEntity() instanceof Player p)) return;
		checkHoldingMaps(p);
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		OMCPassivePower mainPassive = getPassive(fromItemStack(p.getInventory().getItemInMainHand()));
		if(mainPassive != null) mainPassive.onStartHolding(p, false);
		OMCPassivePower offPassive = getPassive(fromItemStack(p.getInventory().getItemInOffHand()));
		if(offPassive != null) offPassive.onStartHolding(p, true);
		MAIN_HAND_MAP.put(p.getUniqueId(), mainPassive);
		OFF_HAND_MAP.put(p.getUniqueId(), offPassive);
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		OMCPassivePower mainPassive = getPassive(fromItemStack(p.getInventory().getItemInMainHand()));
		if(mainPassive != null) mainPassive.onStopHolding(p, false);
		OMCPassivePower offPassive = getPassive(fromItemStack(p.getInventory().getItemInOffHand()));
		if(offPassive != null) offPassive.onStopHolding(p, true);
		MAIN_HAND_MAP.put(p.getUniqueId(), mainPassive);
		OFF_HAND_MAP.put(p.getUniqueId(), offPassive);
	}
}









