package fr.stormer3428.obsidianMC.Power;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.stormer3428.obsidianMC.OMCPlugin;

public abstract class SetDurationPower extends UnsetDurationPower{
	
	public abstract int getDuration();
	
	public SetDurationPower(String registryName/*, OMCPowerManager powerManager*/) {
		super(registryName/*, powerManager*/);
	}
	
	public boolean empower(ItemStack it, Player p) {
		if(!cast(it, p)) return false;
		empowered.add(p.getUniqueId());
		Bukkit.getScheduler().scheduleSyncDelayedTask(OMCPlugin.i, () -> putOnCooldown(p), getDuration());
		return true;
	}
}
