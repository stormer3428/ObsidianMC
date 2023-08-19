package fr.stormer3428.obsidianMC.Power;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import fr.stormer3428.obsidianMC.OMCPlugin;

public abstract class SetDurationPower extends UnsetDurationPower{
	
	public abstract int getDuration();
	
	public SetDurationPower(String registryName/*, OMCPowerManager powerManager*/) {
		super(registryName/*, powerManager*/);
	}
	
	protected void empower(ItemStack it, Player p) {
		empowered.add(p.getUniqueId());
		cast(it, p);
		new BukkitRunnable() {

			@Override
			public void run() {
				putOnCooldown(p);
			}
		}.runTaskLater(OMCPlugin.i, getDuration());
	}
}
