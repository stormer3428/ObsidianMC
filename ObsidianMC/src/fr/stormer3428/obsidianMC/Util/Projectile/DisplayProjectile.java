package fr.stormer3428.obsidianMC.Util.Projectile;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import fr.stormer3428.obsidianMC.Util.DisplayGeometry.DisplayGeometry;

public abstract class DisplayProjectile extends OMCprojectile{

	protected DisplayGeometry geometry;
	
	public DisplayProjectile(Location location, Vector velocity, double projectileWidth, DisplayGeometry geometry) {
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
