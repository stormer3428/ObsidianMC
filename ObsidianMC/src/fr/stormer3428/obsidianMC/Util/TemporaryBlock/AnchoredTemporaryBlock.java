package fr.stormer3428.obsidianMC.Util.TemporaryBlock;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import fr.stormer3428.obsidianMC.OMCPlugin;

public class AnchoredTemporaryBlock {

	public static ArrayList<AnchoredTemporaryBlock> all = new ArrayList<>();

	private AnchoredTemporaryBlock instance = this;
	private Material material;
	private int remainingTicks;
	private Vector relativeLocation;
	private Entity anchor;
	private Location oldLocation;

	public AnchoredTemporaryBlock(Material material, int visibleTime, Location loc, Entity entity) {

		this.oldLocation = loc.getBlock().getLocation();
		this.anchor = entity;
		this.material = material;
		this.remainingTicks = visibleTime;
		this.relativeLocation = loc.subtract(entity.getLocation()).toVector();
		all.add(this);
		startLoop();

	}

	private void startLoop() {
		BlockData BData = this.material.createBlockData();
		this.oldLocation = this.anchor.getLocation().add(this.relativeLocation);
		for(Player p : Bukkit.getOnlinePlayers()) p.sendBlockChange(this.anchor.getLocation().add(this.relativeLocation), BData);
		new BukkitRunnable() {

			@Override
			public void run() {
				if(AnchoredTemporaryBlock.this.remainingTicks <= 0) {
					for(Player p : Bukkit.getOnlinePlayers()) p.sendBlockChange(AnchoredTemporaryBlock.this.oldLocation, AnchoredTemporaryBlock.this.oldLocation.getBlock().getBlockData());
					all.remove(AnchoredTemporaryBlock.this.instance);
					cancel();
				}else {
					if(AnchoredTemporaryBlock.this.anchor.getLocation().add(AnchoredTemporaryBlock.this.relativeLocation).getBlock() != AnchoredTemporaryBlock.this.oldLocation) {
						for(Player p : Bukkit.getOnlinePlayers()) {
							p.sendBlockChange(AnchoredTemporaryBlock.this.oldLocation, AnchoredTemporaryBlock.this.oldLocation.getBlock().getBlockData());
							p.sendBlockChange(AnchoredTemporaryBlock.this.anchor.getLocation().add(AnchoredTemporaryBlock.this.relativeLocation), BData);
						}
					}
				}
				AnchoredTemporaryBlock.this.oldLocation = AnchoredTemporaryBlock.this.anchor.getLocation().add(AnchoredTemporaryBlock.this.relativeLocation).getBlock().getLocation();
				AnchoredTemporaryBlock.this.remainingTicks --;
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
