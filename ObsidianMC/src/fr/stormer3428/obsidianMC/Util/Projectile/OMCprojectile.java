package fr.stormer3428.obsidianMC.Util.Projectile;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import com.google.common.base.Predicate;

public abstract class OMCprojectile extends BukkitRunnable{

	protected Predicate<Entity> entityPredicate = (e) -> true;
	protected boolean blockColliding = true;
	protected boolean entityColliding = true;
	protected boolean inGround = false;
	protected int maxLifespan = 20 * 30;
	protected int afterImpactLifespan = 20 * 5;
	protected int subtick = 1;
	protected Location location;
	protected Vector velocity;
	protected double projectileWidth;


	public OMCprojectile(Location location, Vector velocity, double projectileWidth) {
		this.location = location;
		this.velocity = velocity;
		this.projectileWidth = projectileWidth;
	}

	/**
	 * 
	 * @param rtr the rayTraceResult that triggered impact
	 * @return whether the projectile should stop moving or not
	 */
	protected abstract boolean onHit(RayTraceResult rtr);
	protected abstract void inGroundTick();
	protected abstract void display();

	public void move() {
		Vector effectiveVelocity = velocity.clone().multiply(1.0/subtick);
		if(inGround) return;
		if(blockColliding && entityColliding) {
			RayTraceResult rtr = location.getWorld().rayTrace(location, effectiveVelocity, effectiveVelocity.length(), FluidCollisionMode.NEVER, true, projectileWidth, entityPredicate);
			if(rtr != null && onHit(rtr)) {
				inGround = true;
				return;
			}
		}else if(blockColliding){
			RayTraceResult rtr = location.getWorld().rayTraceBlocks(location, effectiveVelocity, effectiveVelocity.length(), FluidCollisionMode.NEVER, true);
			if(rtr != null && onHit(rtr)) {
				inGround = true;
				return;
			}
		}else if(entityColliding) {
			RayTraceResult rtr = location.getWorld().rayTraceEntities(location, effectiveVelocity, effectiveVelocity.length(), projectileWidth, entityPredicate);
			if(rtr != null && onHit(rtr)) {
				inGround = true;
				return;
			}
		}
		location.add(effectiveVelocity);
	}

	@Override
	public void run() {
		for(int i = subtick; i > 0; i--) {
			move();
			display();
			if(inGround) {
				if(subtick == 0 && --afterImpactLifespan <= 0) cancel();
				else inGroundTick();
			}else if(subtick == 0 && --maxLifespan <= 0) cancel();
		}
	}

	public boolean isBlockColliding() {
		return blockColliding;
	}

	public OMCprojectile setBlockColliding(boolean blockColliding) {
		this.blockColliding = blockColliding;
		return this;
	}

	public boolean isEntityColliding() {
		return entityColliding;
	}

	public OMCprojectile setEntityColliding(boolean entityColliding) {
		this.entityColliding = entityColliding;
		return this;
	}

	public Predicate<Entity> getEntityPredicate() {
		return entityPredicate;
	}

	public OMCprojectile setEntityPredicate(Predicate<Entity> entityPredicate) {
		this.entityPredicate = entityPredicate;
		return this;
	}

	public int getAfterImpactLifespan() {
		return afterImpactLifespan;
	}

	public OMCprojectile setAfterImpactLifespan(int afterImpactLifespan) {
		this.afterImpactLifespan = afterImpactLifespan;
		return this;
	}

	public OMCprojectile setMaxLifespan(int maxLifespan) {
		this.maxLifespan = maxLifespan;
		return this;
	}

	public int getMaxLifespan() {
		return maxLifespan;
	}
}
