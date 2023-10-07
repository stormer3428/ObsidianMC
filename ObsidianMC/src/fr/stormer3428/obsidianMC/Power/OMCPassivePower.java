package fr.stormer3428.obsidianMC.Power;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.stormer3428.obsidianMC.Item.OMCItem;
import fr.stormer3428.obsidianMC.Util.OMCLogger;

public abstract class OMCPassivePower extends OMCPower{

	public OMCPassivePower(String registryName) {
		super(registryName);
	}
	
	public abstract OMCItem getItem();
	public abstract void onHoldingTick(Player p, int ticker, ItemStack it, boolean offHand);
	public abstract void onStopHolding(Player p, boolean offHand);
	public abstract void onStartHolding(Player p, boolean offHand);
	public abstract void onSwap(Player p, boolean offHand);

	@Override
	public int getCooldown() {
		return 0;
	}

	@Override
	public boolean cast(ItemStack it, Player p) {
		return false;
	}

	@Override
	public void onCooldownEnd(Player p) {}

	@Override
	public void onTick(int ticker) {
		OMCItem omcItem = getItem();
		if(omcItem == null) {
			OMCLogger.systemError("Tried to tick a passive with no OMCItem (" + getRegistryName() + ")");
			return;
		}
		for(Player p : Bukkit.getOnlinePlayers()) {
			boolean offHand = false;
			ItemStack it = p.getInventory().getItemInMainHand();
			if(!omcItem.matches(it)) {
				it = p.getInventory().getItemInOffHand();
				offHand = true;
			}
			if(!omcItem.matches(it)) continue;
			onHoldingTick(p, ticker, it, offHand);
		}
	}

	@Override
	public String getDisplayName() {
		return "passive.displayname." + getClass().getSimpleName();
	}



}
