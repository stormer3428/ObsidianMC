package fr.stormer3428.obsidianMC.Power;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import fr.stormer3428.obsidianMC.OMCPlugin;
import fr.stormer3428.obsidianMC.Config.PluginTied;
import fr.stormer3428.obsidianMC.Manager.OMCPowerManager;

public abstract class OMCPower implements PluginTied, Listener{
	
	public static final String KEY_COOLDOWN = "cooldown";
	public static final String KEY_DURATION = "duration";
	public static final String KEY_ENABLED = "enabled";
	protected ArrayList<UUID> empowered = new ArrayList<>();
	protected ArrayList<UUID> onCooldown = new ArrayList<>();

	private final OMCPowerManager powerManager;
	private final String registryName;

	public abstract void onEmpower(ItemStack it, Player p);
	public abstract boolean meetsConditions(ItemStack it, Player p);
	public abstract void onDepower(Player p);
	public abstract void onCooldownEnd(Player p);
	public abstract void onTick(int ticker);	


	public OMCPower(String registryName, OMCPowerManager powerManager) {
		this.powerManager = powerManager;
		this.registryName = registryName;
	}

	public void tryCast(ItemStack it, Player p) {
		if(!powerManager.getBoolean(path(KEY_ENABLED))) return;
		if(onCooldown.contains(p.getUniqueId()) || empowered.contains(p.getUniqueId()) || !meetsConditions(it, p)) return;
		empower(it, p);
	}

	protected void empower(ItemStack it, Player p) {
		empowered.add(p.getUniqueId());
		onEmpower(it, p);
		new BukkitRunnable() {

			@Override
			public void run() {
				empowered.remove(p.getUniqueId());
				putOnCooldown(p);
				onDepower(p);
			}
		}.runTaskLater(OMCPlugin.i, powerManager.getInt(path(KEY_DURATION)));
	}
	
	protected void putOnCooldown(Player p) {
		int abilityCooldown = powerManager.getInt(path(KEY_COOLDOWN));
		onCooldown.add(p.getUniqueId());
		onDepower(p);
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

	public boolean isEmpowered(Player p) {
		return empowered.contains(p.getUniqueId());
	}
	
	public final OMCPowerManager getPowerManager() {
		return powerManager;
	}
	
	public String getRegistryName() {
		return registryName;
	}
	
	public String path(String innerPath) {
		return getRegistryName() + "." + innerPath;
	}

}
