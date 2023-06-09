package fr.stormer3428.obsidianMC.Util.Geometry;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

public class Geometry {

	ArrayList<Drawable> drawables = new ArrayList<>();
	private Vector direction;
	
	public Geometry(Vector direction) {
		this.direction = direction;
	}
	
	public Geometry add(Drawable ... drawables) {
		this.drawables.addAll(Arrays.asList(drawables));
		return this;
	}
	
	public Geometry draw(Location location) {
		return draw(location, 1.0);
	}
	
	public Geometry draw(Location location, double scale) {
		for(Drawable drawable : drawables) drawable.draw(location, scale);
		return this;
	}
	
	public Geometry rotateAroundAxis(Vector axis, double radians) {
		for(Drawable drawable : drawables) drawable.rotateAroundAxis(axis, radians);
		direction.rotateAroundAxis(axis, radians);
		return this;
	}
	
	public Geometry rotateAroundX(double radians) {
		for(Drawable drawable : drawables) drawable.rotateAroundX(radians);
		direction.rotateAroundX(radians);
		return this;
	}
	
	public Geometry rotateAroundY(double radians) {
		for(Drawable drawable : drawables) drawable.rotateAroundY(radians);
		direction.rotateAroundY(radians);
		return this;
	}
	
	public Geometry rotateAroundZ(double radians) {
		for(Drawable drawable : drawables) drawable.rotateAroundZ(radians);
		direction.rotateAroundZ(radians);
		return this;
	}
	
	public Geometry setDirection(Vector newDirection) {
		if(newDirection.equals(direction)) return this;
		try {
			newDirection.checkFinite();
		}catch (Exception e) {
			return this;
		}
		rotateAroundAxis(direction.getCrossProduct(newDirection).normalize(), direction.angle(newDirection));
		direction = newDirection.clone();
		return this;
	}

	public Vector getDirection() {
		return direction;
	}
	
	public Geometry setParticle(Particle particle) {
		for(Drawable drawable : drawables) drawable.setParticle(particle);
		return this;
	}

	@SuppressWarnings("unchecked")
	public <T extends Drawable>  ArrayList<T> getDrawables(Class<T> clazz) {
		ArrayList<T> list = new ArrayList<>();
		for(Drawable drawable : drawables) if(clazz.isInstance(drawable)) list.add((T) drawable);
		return list;
	}
}
