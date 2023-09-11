package fr.stormer3428.obsidianMC.Power;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class UnsetDurationPower extends OMCPower{

	protected ArrayList<UUID> empowered = new ArrayList<>();

	public abstract void onDepower(Player p);
	public abstract boolean onEmpoweredTryCast(ItemStack it, Player p);

	public UnsetDurationPower(String registryName/*, OMCPowerManager powerManager*/) {
		super(registryName/*, powerManager*/);
	}

	@Override
	public boolean tryCast(ItemStack it, Player p) {
		if(!isEnabled()) return false;
		if(isOnCooldown(p)) return false;
		if(empowered.contains(p.getUniqueId())) return onEmpoweredTryCast(it, p);
		empower(it, p);
		return true;
	}


	/*
	 * It is expected to call "putOnCooldown when ability ends"
	 */
	protected boolean empower(ItemStack it, Player p) {
		if(!cast(it, p)) return false;
		empowered.add(p.getUniqueId());
		return true;
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
