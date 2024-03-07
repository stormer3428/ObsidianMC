package fr.stormer3428.obsidianMC.Geometry.ParticleGeometry;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

public interface ParticleDrawable {

	public void draw(Location location, double scale);

	public ParticleDrawable rotateAroundAxis(Vector axis, double radians);
	public ParticleDrawable rotateAroundX(double radians);
	public ParticleDrawable rotateAroundY(double radians);
	public ParticleDrawable rotateAroundZ(double radians);
	
	public Particle getParticle();
	public ParticleDrawable setParticle(Particle particle);
	public ParticleDrawable setParticleSpeed(float particleSpeed);
	public <T> ParticleDrawable setParticleData(T particleData);
	public ParticleDrawable setForceRendering(boolean forceRender);
	public boolean isForceRendering();
	
}
