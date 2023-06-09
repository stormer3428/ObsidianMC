package fr.stormer3428.obsidianMC.Util.Geometry;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class MovingPoint implements Drawable{

	private Vector location;
	private Particle particle = Particle.CRIT_MAGIC;
	private Vector particleDirection = new Vector(0,0,0);
	private Vector particleOffsetDirection = new Vector(0,0,0);

	@Override
	public void draw(Location location, double scale) {
		World world = location.getWorld();
		Location particleLoc = location.clone().add(this.location.clone().multiply(scale));
		world.spawnParticle(particle, particleLoc, 
				0, 
				particleDirection.getX() + particleOffsetDirection.getX(), 
				particleDirection.getY() + particleOffsetDirection.getY(), 
				particleDirection.getZ() + particleOffsetDirection.getZ(), 
				particleDirection.clone().add(particleOffsetDirection).length(), null, true);
	}

	public void draw(Location location) {
		draw(location, 1.0);
	}

	@Override
	public MovingPoint rotateAroundAxis(Vector axis, double radians) {
		location.rotateAroundAxis(axis, radians);
		particleDirection.rotateAroundAxis(axis, radians);
		particleOffsetDirection.rotateAroundAxis(axis, radians);
		return this;
	}

	@Override
	public MovingPoint rotateAroundX(double radians) {
		location.rotateAroundX(radians);
		particleDirection.rotateAroundX(radians);
		particleOffsetDirection.rotateAroundX(radians);
		return this;
	}

	@Override
	public MovingPoint rotateAroundY(double radians) {
		location.rotateAroundY(radians);
		particleDirection.rotateAroundY(radians);
		particleOffsetDirection.rotateAroundY(radians);
		return this;
	}

	@Override
	public MovingPoint rotateAroundZ(double radians) {
		location.rotateAroundZ(radians);
		particleDirection.rotateAroundZ(radians);
		particleOffsetDirection.rotateAroundZ(radians);
		return this;
	}

	public Vector getLocation() {
		return location;
	}

	public MovingPoint setLocation(Vector location) {
		this.location = location;
		return this;
	}

	@Override
	public Particle getParticle() {
		return particle;
	}

	@Override
	public MovingPoint setParticle(Particle particle) {
		this.particle = particle;
		return this;
	}

	public Vector getParticleDirection() {
		return particleDirection;
	}

	public MovingPoint setParticleDirection(Vector particleDirection) {
		this.particleDirection = particleDirection;
		return this;
	}

	public Vector getParticleOffsetDirection() {
		return particleOffsetDirection;
	}

	public MovingPoint setParticleOffsetDirection(Vector particleOffsetDirection) {
		this.particleOffsetDirection = particleOffsetDirection;
		return this;
	}



}
