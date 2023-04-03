package fr.stormer3428.obsidianMC.Util.TemporaryBlock;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.stormer3428.obsidianMC.OMCPlugin;

public class TemporaryBlock {

	public static ArrayList<TemporaryBlock> all = new ArrayList<>();

	private TemporaryBlock instance = this;
	private Material material;
	private int remainingTicks;
	private Location location;

	public TemporaryBlock(Material material, int visibleTime, Location loc) {
		this.location = loc;
		this.material = material;
		this.remainingTicks = visibleTime;
		all.add(this);
		startLoop();
	}

	private void startLoop() {
		BlockData BData = this.material.createBlockData();
		for(Player p : Bukkit.getOnlinePlayers()) p.sendBlockChange(this.location, BData);
		new BukkitRunnable() {

			@Override
			public void run() {
				if(TemporaryBlock.this.remainingTicks <= 0) {
					for(Player p : Bukkit.getOnlinePlayers()) p.sendBlockChange(TemporaryBlock.this.location, TemporaryBlock.this.location.getBlock().getBlockData());
					all.remove(TemporaryBlock.this.instance);
					cancel();
				}
				TemporaryBlock.this.remainingTicks --;
			}
		}.runTaskTimer(OMCPlugin.i, 0, 1);
	}

	public Material getMaterial() {
		return this.material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public int getRemainingTicks() {
		return this.remainingTicks;
	}

	public void setRemainingTicks(int remainingTicks) {
		this.remainingTicks = remainingTicks;
	}

}
