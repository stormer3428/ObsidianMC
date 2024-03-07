package fr.stormer3428.obsidianMC.Util.Geometry;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public abstract class Drawable {

	private double renderDistance = 64.0;
	private double renderDistanceSQ = renderDistance * renderDistance;

	public final void draw(Location location, double scale) { draw(location, scale, Bukkit.getOnlinePlayers());}
	public final void draw(Location location, double scale, Collection<? extends Player> pls) { draw(location, scale, pls.toArray(new Player[0])); }
	public final void draw(Location location, double scale, Player ... pls) { for(Player p : pls) if(p.getWorld().equals(location.getWorld()) && p.getLocation().distanceSquared(location) < renderDistanceSQ) draw(location, scale, p); }
	public final void forceDraw(Location location, double scale, Player ... pls) { for(Player p : pls) draw(location, scale, p); }
	public abstract void draw(Location location, double scale, Player p);

	public abstract Drawable rotateAroundAxis(Vector axis, double radians);
	public abstract Drawable rotateAroundX(double radians);
	public abstract Drawable rotateAroundY(double radians);
	public abstract Drawable rotateAroundZ(double radians);

	public abstract Particle getParticle();
	public abstract Drawable setParticle(Particle particle);
	public abstract Drawable setParticleSpeed(float particleSpeed);
	public abstract <T> Drawable setParticleData(T particleData);
	public double getRenderDistance() { return renderDistance; }
	public double getRenderDistanceSQ() {return renderDistanceSQ;}
	public Drawable setRenderDistance(double renderDistance) {
		renderDistance = Math.max(-1, renderDistance);
		this.renderDistance = renderDistance;
		this.renderDistanceSQ = renderDistance * renderDistance;
		return this;
	}
}
