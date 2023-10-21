package fr.stormer3428.obsidianMC.Manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.stormer3428.obsidianMC.OMCPlugin;
import fr.stormer3428.obsidianMC.Config.PluginTied;

@Deprecated
public abstract class OMCSoulbindingManagerLegacy implements PluginTied, Listener{

	protected abstract boolean isSoulboundItem(ItemStack it);
	protected abstract boolean isSuitable(ArrayList<ItemStack> soulboundItems);
	protected abstract void fixInventory(ArrayList<ItemStack> soulboundItems, Player p);

	@Override
	public void onPluginEnable() {
		OMCPlugin.i.getServer().getPluginManager().registerEvents(this, OMCPlugin.i);
	}

	@Override
	public void onPluginDisable() {}

	@Override
	public void onPluginReload() {}

	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		if(!isSoulboundItem(e.getItemDrop().getItemStack())) return;
		e.setCancelled(true);
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		validate((Player) e.getPlayer());
	}

	@EventHandler
	public void onInventoryOpen(InventoryOpenEvent e) {
		validate((Player) e.getPlayer());
	}

	@EventHandler
	public void onDrag(InventoryDragEvent e) {
		Inventory top = e.getView().getTopInventory();
		if(top == null) return;
		if(!(top instanceof CraftingInventory ci && ci.getSize() == 5)) return;
		for(Entry<Integer, ItemStack> entry : e.getNewItems().entrySet()) {
			int slot = entry.getKey();
			ItemStack it = entry.getValue();
			if(slot > 5) continue;
			if(!isSoulboundItem(it)) continue;
			e.setCancelled(true);
			return;
		}
	}

	@EventHandler 
	public void inventoryClick(InventoryClickEvent e) {
		Inventory top = e.getView().getTopInventory();
		if(top == null) return;
		if(top instanceof CraftingInventory ci && ci.getSize() == 5 && !top.equals(e.getClickedInventory())) return;
		if(
				isSoulboundItem(e.getCurrentItem()) ||
				isSoulboundItem(e.getCursor()) ||
				(e.getHotbarButton() == -1 ? false : isSoulboundItem(e.getView().getBottomInventory().getItem(e.getHotbarButton()))) ||
				(e.getClick().equals(ClickType.SWAP_OFFHAND) && isSoulboundItem(e.getWhoClicked().getInventory().getItemInOffHand()))
				) {
			e.setCancelled(true);
			return;
		}
	}

	private final HashMap<UUID, ArrayList<ItemStack>> restituteMap = new HashMap<>();

	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
		ArrayList<ItemStack> toRemove = new ArrayList<>();
		List<ItemStack> drops = e.getDrops();
		for(ItemStack item : drops) if(isSoulboundItem(item)) toRemove.add(item);
		for(ItemStack item : toRemove) {
			drops.remove(item);
			if(!restituteMap.containsKey(p.getUniqueId())) restituteMap.put(p.getUniqueId(), new ArrayList<ItemStack>());
			restituteMap.get(p.getUniqueId()).add(item);
		}
	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		Player p = e.getPlayer();
		if(!restituteMap.containsKey(p.getUniqueId())) restituteMap.put(p.getUniqueId(), new ArrayList<ItemStack>());
		ArrayList<ItemStack> toRestitute = restituteMap.get(p.getUniqueId());
		for(ItemStack it : toRestitute) p.getInventory().addItem(it);
		toRestitute.clear();
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		validate(p);
	}

	@EventHandler
	public void onInteract(PlayerInteractAtEntityEvent e) {
		Player p = e.getPlayer();
		ItemStack it = p.getInventory().getItem(e.getHand());
		if(it == null || !isSoulboundItem(it)) return;
		e.setCancelled(true);
	}

	@EventHandler
	public void onInteract(PlayerInteractEntityEvent e) {
		Player p = e.getPlayer();
		ItemStack it = p.getInventory().getItem(e.getHand());
		if(it == null || !isSoulboundItem(it)) return;
		e.setCancelled(true);
	}

	public ArrayList<ItemStack> getSoulboundItems(Player p){
		ArrayList<ItemStack> soulboundItems = new ArrayList<>(); 

		for(ItemStack it : p.getInventory().getContents()) {
			if(it == null) continue;
			if(!isSoulboundItem(it)) continue;
			soulboundItems.add(it);
		}
		return soulboundItems;
	}

	public boolean validate(Player p) {
		ArrayList<ItemStack> soulboundItems = getSoulboundItems(p);

		if(isSuitable(soulboundItems)) return true;
		fixInventory(soulboundItems, p);
		return false;
	}
}

