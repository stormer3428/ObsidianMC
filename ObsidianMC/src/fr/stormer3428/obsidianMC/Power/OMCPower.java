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

	public abstract boolean cast(ItemStack it, Player p);
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
		if(isOnCooldown(p)) return false;
		empower(it, p);
		return true;
	}

	public boolean isOnCooldown(Player p) {
		return isOnCooldown(p.getUniqueId());
	}

	public boolean isOnCooldown(UUID uuid) {
		return onCooldown.containsKey(uuid);
	}

	protected boolean empower(ItemStack it, Player p) {
		if(!cast(it, p)) return false;
		putOnCooldown(p);
		return true;
	}

	public void putOnCooldown(Player p) {
		putOnCooldown(p, getCooldown());
	}

	protected void putOnCooldown(Player p, int abilityCooldown) {
		putOnCooldown(p.getUniqueId(), abilityCooldown);
	}

	protected void putOnCooldown(UUID uuid) {
		putOnCooldown(uuid, getCooldown());
	}

	protected void putOnCooldown(UUID uuid, int abilityCooldown) {
		onCooldown.put(uuid, Math.max(getCooldown(uuid), abilityCooldown));
	}

	public void clearCooldown(Player p) {
		onCooldown.remove(p.getUniqueId());
	}

	public int getCooldown(Player p) {
		return getCooldown(p.getUniqueId());
	}

	public int getCooldown(UUID uuid) {
		if(!isOnCooldown(uuid)) return 0;
		return onCooldown.get(uuid);
	}

	public String getRegistryName() {
		return registryName;
	}

	public String path(String innerPath) {
		return getRegistryName() + "." + innerPath;
	}

}
