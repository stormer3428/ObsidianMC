package fr.stormer3428.obsidianMC.Power;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class UnsetDurationPower extends OMCPower{
	
	protected ArrayList<UUID> empowered = new ArrayList<>();

	public abstract void onDepower(Player p);

	public UnsetDurationPower(String registryName/*, OMCPowerManager powerManager*/) {
		super(registryName/*, powerManager*/);
	}
	
	@Override
	public boolean tryCast(ItemStack it, Player p) {
		if(!isEnabled()) return false;
		if(isOnCooldown(p) || empowered.contains(p.getUniqueId()) || !meetsConditions(it, p)) return false;
		empower(it, p);
		return true;
	}
	
	/*
	 * It is expected to call "putOnCooldown when ability ends"
	 */
	protected void empower(ItemStack it, Player p) {
		empowered.add(p.getUniqueId());
		cast(it, p);
	}
	
	protected void putOnCooldown(Player p, int abilityCooldown) {
		empowered.remove(p.getUniqueId());
		onDepower(p);
		super.putOnCooldown(p, abilityCooldown);
	}
	

	public boolean isEmpowered(Player p) {
		return empowered.contains(p.getUniqueId());
	}

}
