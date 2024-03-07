package fr.stormer3428.obsidianMC.Geometry.ParticleGeometry;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class ParticleLine implements ParticleDrawable{

	private Vector a;
	private Vector b;
	private Particle particle = Particle.CRIT_MAGIC;
	private Object particleData = null;
	private int particleAmount = 1;
	private float particleSpreadX = 0;
	private float particleSpreadY = 0;
	private float particleSpreadZ = 0;
	private float particleSpeed = 0;
	private double resolution = 0.1d;
	private boolean forceRender = true;
	
	public ParticleLine(Vector a, Vector b) {
		this.a = a.clone();
		this.b = b.clone();
	}

	public Vector getA() {
		return a;
	}

	public void setA(Vector a) {
		this.a = a;
	}

	public Vector getB() {
		return b;
	}

	public void setB(Vector b) {
		this.b = b;
	}
	
	public void drawPoint(World world, Vector location) {
		world.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), particleAmount, particleSpreadX, particleSpreadY, particleSpreadZ, particleSpeed, particleData, forceRender);
	}

	@Override
	public void draw(Location location, double scale) {
		World world = location.getWorld();
		Vector locationVector = location.toVector();
		for(Vector point : getInterpolatedPoints(a.clone().multiply(scale) , b.clone().multiply(scale))) drawPoint(world, point.add(locationVector));		
	}

	public ArrayList<Vector> getInterpolatedPoints(Vector a, Vector b){
		return getInterpolatedPoints(a, b, resolution);
	}
	
	public static ArrayList<Vector> getInterpolatedPoints(Vector a, Vector b, double resolution){
		double d = a.distance(b);
		int amount = (int) (d/resolution) + 2;
		Vector delta = b.clone().subtract(a).normalize().multiply(d/amount);
		ArrayList<Vector> points = new ArrayList<>();
		Vector point = a.clone();
		for(int c = amount; c > 0; c--) points.add(point.add(delta).clone());
		return points;
	}

	@Override
	public ParticleLine rotateAroundAxis(Vector axis, double radians) {
		a.rotateAroundAxis(axis, radians);
		b.rotateAroundAxis(axis, radians);
		return this;
	}

	@Override
	public ParticleLine rotateAroundX(double radians) {
		a.rotateAroundX(radians);
		b.rotateAroundX(radians);
		return this;
	}

	@Override
	public ParticleLine rotateAroundY(double radians) {
		a.rotateAroundY(radians);
		b.rotateAroundY(radians);
		return this;
	}

	@Override
	public ParticleLine rotateAroundZ(double radians) {
		a.rotateAroundZ(radians);
		b.rotateAroundZ(radians);
		return this;
	}

	@Override
	public Particle getParticle() {
		return particle;
	}

	@Override
	public ParticleLine setParticle(Particle particle) {
		this.particle = particle;
		return this;
	}

	public int getParticleAmount() {
		return particleAmount;
	}

	public ParticleLine setParticleAmount(int particleAmount) {
		this.particleAmount = particleAmount;
		return this;
	}

	public float getParticleSpreadX() {
		return particleSpreadX;
	}

	public ParticleLine setParticleSpreadX(float particleSpreadX) {
		this.particleSpreadX = particleSpreadX;
		return this;
	}

	public float getParticleSpreadY() {
		return particleSpreadY;
	}

	public ParticleLine setParticleSpreadY(float particleSpreadY) {
		this.particleSpreadY = particleSpreadY;
		return this;
	}

	public float getParticleSpreadZ() {
		return particleSpreadZ;
	}

	public ParticleLine setParticleSpreadZ(float particleSpreadZ) {
		this.particleSpreadZ = particleSpreadZ;
		return this;
	}

	public float getParticleSpeed() {
		return particleSpeed;
	}

	public ParticleLine setParticleSpeed(float particleSpeed) {
		this.particleSpeed = particleSpeed;
		return this;
	}

	@Override
	public ParticleLine setParticleData(Object particleData) {
		this.particleData = particleData;
		return this;
	}

	@Override
	public ParticleLine setForceRendering(boolean forceRender) {
		this.forceRender = forceRender;
		return this;
	}
	
	public ParticleLine setResolution(double resolution) {
		this.resolution = resolution;
		return this;
	}
	
	public double getResolution() {
		return resolution;
	}

	@Override
	public boolean isForceRendering() {
		return forceRender;
	}
}
