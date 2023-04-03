package fr.stormer3428.obsidianMC.Util.Geometry;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class Point {

	private Vector location;
	private Particle particle = Particle.CRIT_MAGIC;
	private int particleAmount = 1;
	private float particleSpreadX = 0;
	private float particleSpreadY = 0;
	private float particleSpreadZ = 0;
	private float particleSpeed = 0;
	
	public void draw(World world) {
		world.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), particleAmount, particleSpreadX, particleSpreadY, particleSpreadZ, particleSpeed, null, true);
	}

	public void draw(Location location, double scale) {
		World world = location.getWorld();
		Location particleLoc = location.clone().add(this.location.clone().multiply(scale));
		world.spawnParticle(particle, particleLoc.getX(), particleLoc.getY(), particleLoc.getZ(), particleAmount, particleSpreadX, particleSpreadY, particleSpreadZ, particleSpeed, null, true);
	}
	
	public void draw(Location location) {
		draw(location, 1.0);
	}
	
	public Point rotateAroundAxis(Vector axis, double radians) {
		location.rotateAroundAxis(axis, radians);
		return this;
	}
	
	public Point rotateAroundX(double radians) {
		location.rotateAroundX(radians);
		return this;
	}
	
	public Point rotateAroundY(double radians) {
		location.rotateAroundY(radians);
		return this;
	}
	
	public Point rotateAroundZ(double radians) {
		location.rotateAroundZ(radians);
		return this;
	}

	public Vector getLocation() {
		return location;
	}

	public Point setLocation(Vector location) {
		this.location = location;
		return this;
	}
	
	public Particle getParticle() {
		return particle;
	}

	public Point setParticle(Particle particle) {
		this.particle = particle;
		return this;
	}

	public int getParticleAmount() {
		return particleAmount;
	}

	public Point setParticleAmount(int particleAmount) {
		this.particleAmount = particleAmount;
		return this;
	}

	public float getParticleSpreadX() {
		return particleSpreadX;
	}

	public Point setParticleSpreadX(float particleSpreadX) {
		this.particleSpreadX = particleSpreadX;
		return this;
	}

	public float getParticleSpreadY() {
		return particleSpreadY;
	}

	public Point setParticleSpreadY(float particleSpreadY) {
		this.particleSpreadY = particleSpreadY;
		return this;
	}

	public float getParticleSpreadZ() {
		return particleSpreadZ;
	}

	public Point setParticleSpreadZ(float particleSpreadZ) {
		this.particleSpreadZ = particleSpreadZ;
		return this;
	}

	public float getParticleSpeed() {
		return particleSpeed;
	}

	public Point setParticleSpeed(float particleSpeed) {
		this.particleSpeed = particleSpeed;
		return this;
	}
	
}
