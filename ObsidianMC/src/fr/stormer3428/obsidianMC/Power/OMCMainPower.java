package fr.stormer3428.obsidianMC.Power;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.stormer3428.obsidianMC.Manager.OMCLogger;

public abstract class OMCMainPower extends OMCPassivePower{

	public OMCMainPower(String registryName) {
		super(registryName);
	}

	public abstract ArrayList<OMCPower> getAvailablePowers(Player p);

	protected HashMap<UUID, Integer> selectedPower = new HashMap<>();
	
	@Override
	public boolean cast(ItemStack it, Player p) {
		if(p.isSneaking()) {
			cycle(p);
			return true;
		}
		return castCurrent(p);
	}

	public int getSelected(Player p) {
		if(!selectedPower.containsKey(p.getUniqueId())) selectedPower.put(p.getUniqueId(), 0);
		return selectedPower.get(p.getUniqueId());
	}

	public boolean castCurrent(Player p) {
		ArrayList<OMCPower> powers = getAvailablePowers(p);
		if(powers.isEmpty()) return false;
		int selected = getSelected(p);
		return powers.get(selected).tryCast(null, p);
	}

	public OMCPower getSelectedPower(Player p) {
		ArrayList<OMCPower> powers = getAvailablePowers(p);
		final int size = powers.size();		
		if(size == 0) return null;
		return powers.get(getSelected(p));
	}

	public void cycle(Player p) {
		final int size = getAvailablePowers(p).size();				
		int selected = getSelected(p);
		if(size == 0) selected = 0;
		else if(++selected >= size) selected = 0;

		selectedPower.put(p.getUniqueId(), selected);
		onActionBarTick(p);
	}


	public abstract String getActionBar(Player p);
	
	public void onActionBarTick(Player p) {
		OMCLogger.actionBar(p, getActionBar(p));
	}
	
	@Override
	public void onHoldingTick(Player p, int ticker, ItemStack it, boolean offHand) {
		if(ticker%20==0) onActionBarTick(p);
	}

	@Override
	public void onStartHolding(Player p, boolean offHand) {
		onActionBarTick(p);
	}

	@Override
	public void onStopHolding(Player p, boolean offHand) {}
	
	@Override
	public void onSwap(Player p, boolean offHand) {
		onActionBarTick(p);
	}
}
