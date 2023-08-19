package fr.stormer3428.obsidianMC.Power;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import fr.stormer3428.obsidianMC.OMCPlugin;
import fr.stormer3428.obsidianMC.Config.PluginTied;

public abstract class OMCPower implements PluginTied, Listener{

	public abstract boolean isEnabled();
	public abstract int getCooldown();

	protected HashMap<UUID, Integer> onCooldown = new HashMap<>();

	protected final String registryName;

	public abstract void cast(ItemStack it, Player p);
	public abstract boolean meetsConditions(ItemStack it, Player p);
	public abstract void onCooldownEnd(Player p);
	public abstract void onTick(int ticker);
	public abstract String getDisplayName();

	@Override
	public void onPluginEnable() {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				Iterator<Entry<UUID, Integer>> iterator = onCooldown.entrySet().iterator();
				while(iterator.hasNext()) {
					Entry<UUID, Integer> entry = iterator.next();
					UUID uuid = entry.getKey();
					int cooldown = entry.getValue();
					cooldown --;
					if(cooldown > 0) {
						entry.setValue(cooldown);
						continue;
					}
					iterator.remove();
					Player p = Bukkit.getPlayer(uuid);
					if(p == null) continue;
					onCooldownEnd(p);
				}
			}
		}.runTaskTimer(OMCPlugin.i, 0, 1);
	}
	
	public OMCPower(String registryName) {
		this.registryName = registryName;
	}

	public boolean tryCast(ItemStack it, Player p) {
		if(!isEnabled()) return false;
		if(isOnCooldown(p) || !meetsConditions(it, p)) return false;
		empower(it, p);
		return true;
	}

	public boolean isOnCooldown(Player p) {
		return isOnCooldown(p.getUniqueId());
	}
	
	public boolean isOnCooldown(UUID uuid) {
		return onCooldown.containsKey(uuid);
	}

	protected void empower(ItemStack it, Player p) {
		cast(it, p);
		putOnCooldown(p);
	}

	protected void putOnCooldown(Player p) {
		putOnCooldown(p, getCooldown());
	}

	protected void putOnCooldown(Player p, int abilityCooldown) {
		onCooldown.put(p.getUniqueId(), Math.max(getCooldown(p), abilityCooldown));
	}

	public void clearCooldown(Player p) {
		onCooldown.remove(p.getUniqueId());
	}

	public int getCooldown(Player p) {
		if(!isOnCooldown(p)) return 0;
		return onCooldown.get(p.getUniqueId());
	}

	public String getRegistryName() {
		return registryName;
	}

	public String path(String innerPath) {
		return getRegistryName() + "." + innerPath;
	}

}
