package fr.stormer3428.obsidianMC.Projectile;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import fr.stormer3428.obsidianMC.Geometry.DisplayGeometry.DisplayGeometry;

public abstract class OMCDisplayProjectile extends OMCprojectile{

	protected DisplayGeometry geometry;
	
	public OMCDisplayProjectile(Location location, Vector velocity, double projectileWidth, DisplayGeometry geometry) {
		super(location, velocity, projectileWidth);
		this.geometry = geometry;
	}

	@Override
	public synchronized void cancel() throws IllegalStateException {
		super.cancel();
		geometry.kill();
	}

	@Override
	protected void display() {
		geometry.setLocation(location);
	}

}
