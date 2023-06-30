package fr.stormer3428.obsidianMC.Manager;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.stormer3428.obsidianMC.OMCPlugin;
import fr.stormer3428.obsidianMC.Config.PluginTied;

public abstract class OMCSoulbindingManager implements PluginTied, Listener{
		
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
	public void inventoryClick(InventoryClickEvent e) {
		Inventory top = e.getView().getTopInventory();
		if(top == null) return;
		if(top instanceof CraftingInventory) return;
		if(
				isSoulboundItem(e.getCurrentItem()) ||
				isSoulboundItem(e.getCursor()) ||
				(e.getHotbarButton() == -1 ? false : isSoulboundItem(e.getView().getBottomInventory().getItem(e.getHotbarButton())))
				) {
			e.setCancelled(true);
			return;
		}
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		ArrayList<ItemStack> toRemove = new ArrayList<>();
		List<ItemStack> drops = e.getDrops();
		for(ItemStack item : drops) if(isSoulboundItem(item)) toRemove.add(item);
		for(ItemStack item : toRemove) {
			item.setAmount(0);
			drops.remove(item);
		}
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
	
	public boolean validate(Player p) {
		ArrayList<ItemStack> soulboundItems = new ArrayList<>(); 
		
		for(ItemStack it : p.getInventory().getContents()) {
			if(it == null) continue;
			if(!isSoulboundItem(it)) continue;
			soulboundItems.add(it);
		}
		if(isSuitable(soulboundItems)) return true;
		fixInventory(soulboundItems, p);
		return false;
	}
}

