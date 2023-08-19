package fr.stormer3428.obsidianMC.Power;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import fr.stormer3428.obsidianMC.OMCPlugin;
import fr.stormer3428.obsidianMC.Config.PluginTied;

public abstract class OMCPower implements PluginTied, Listener{
	
//	public static final String KEY_ENABLED = "enabled";
//	public static final String KEY_COOLDOWN = "cooldown";

	public abstract boolean isEnabled();
	public abstract int getCooldown();
	
	protected ArrayList<UUID> onCooldown = new ArrayList<>();

//	protected final OMCPowerManager powerManager;
	protected final String registryName;

	public abstract void cast(ItemStack it, Player p);
	public abstract boolean meetsConditions(ItemStack it, Player p);
	public abstract void onCooldownEnd(Player p);
	public abstract void onTick(int ticker);
	public abstract String getDisplayName();

	public OMCPower(String registryName/*, OMCPowerManager powerManager*/) {
//		this.powerManager = powerManager;
		this.registryName = registryName;
	}

	public boolean tryCast(ItemStack it, Player p) {
		if(!isEnabled()) return false;
		if(isOnCooldown(p) || !meetsConditions(it, p)) return false;
		empower(it, p);
		return true;
	}

	public boolean isOnCooldown(Player p) {
		return onCooldown.contains(p.getUniqueId());
	}
	
	protected void empower(ItemStack it, Player p) {
		cast(it, p);
		putOnCooldown(p);
	}
	
	protected void putOnCooldown(Player p) {
		putOnCooldown(p, getCooldown());
	}
	
	protected void putOnCooldown(Player p, int abilityCooldown) {
		onCooldown.add(p.getUniqueId());
		new BukkitRunnable() {

			@Override
			public void run() {
				onCooldown.remove(p.getUniqueId());
				onCooldownEnd(p);
			}
		}.runTaskLater(OMCPlugin.i, abilityCooldown);
	}
	
	public void clearCooldown(Player p) {
		onCooldown.remove(p.getUniqueId());
	}
	
//	public final OMCPowerManager getPowerManager() {
//		return powerManager;
//	}
	
	public String getRegistryName() {
		return registryName;
	}
	
	public String path(String innerPath) {
		return getRegistryName() + "." + innerPath;
	}

}
