package fr.stormer3428.obsidianMC.Util.Block;

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
	private BlockData blockData;
	private int remainingTicks;
	private Location location;

	private TemporaryBlock(BlockData blockData, int visibleTime, Location loc) {
		this.location = loc;
		this.remainingTicks = visibleTime;
		setBlockData(blockData);
		all.add(this);
		startLoop();
	}

	public static TemporaryBlock createNew(Material material, int visibleTime, Location loc) {
		return createNew(material.createBlockData(), visibleTime, loc);
	}
	
	public static TemporaryBlock createNew(BlockData blockData, int visibleTime, Location loc) {
		for(TemporaryBlock block : all) if(block.location.equals(loc)) {
			block.setRemainingTicks(visibleTime);
			block.setBlockData(blockData);
			return block;
		}
		return new TemporaryBlock(blockData, visibleTime, loc);
	}
	
	private void startLoop() {
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

	public int getRemainingTicks() {
		return this.remainingTicks;
	}

	public void setRemainingTicks(int remainingTicks) {
		this.remainingTicks = remainingTicks;
	}

	public BlockData getBlockData() {
		return blockData;
	}

	public void setBlockData(BlockData blockData) {
		this.blockData = blockData;
		for(Player p : Bukkit.getOnlinePlayers()) p.sendBlockChange(this.location, blockData);
	}

}
