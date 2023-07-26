package fr.stormer3428.obsidianMC.Util.Geometry;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import fr.stormer3428.obsidianMC.Util.OMCLogger;

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
		try {
			axis.checkFinite();
		}catch (Exception e) {
			OMCLogger.systemError("Error, attempted rotate around NaN (" + axis + ")");
			return this;
		}
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
			OMCLogger.systemError("Error, attempted to set direction to NaN (" + newDirection + ")");
			return this;
		}
		Vector crossProduct = direction.getCrossProduct(newDirection);
		crossProduct.normalize();
		try {
			crossProduct.checkFinite();
		}catch (Exception e) {
			OMCLogger.debug("Error, the cross product from old direction " + direction + " and the new direction " + newDirection + " is NaN (delta is" + newDirection.clone().subtract(direction) + ")");
			return this;
		}
		rotateAroundAxis(crossProduct, direction.angle(newDirection));
		direction = newDirection.clone();
		return this;
	}

	public Vector getDirection() {
		return direction;
	}
	

	public Geometry setParticleSpeed(float speed) {
		for(Drawable drawable : drawables) drawable.setParticleSpeed(speed);
		return this;
	}
	
	public Geometry setParticle(Particle particle) {
		for(Drawable drawable : drawables) drawable.setParticle(particle);
		return this;
	}

	public <T> Geometry setParticleData(T particleData) {
		for(Drawable drawable : drawables) drawable.setParticleData(particleData);
		return this;
	}

	public Geometry setForceRendering(boolean forceRender) {
		for(Drawable drawable : drawables) drawable.setForceRendering(forceRender);
		return this;
	}
	
	private ArrayList<Drawable> getDrawables() {
		return drawables;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Drawable>  ArrayList<T> getDrawables(Class<T> clazz) {
		ArrayList<T> list = new ArrayList<>();
		for(Drawable drawable : drawables) if(clazz.isInstance(drawable)) list.add((T) drawable);
		return list;
	}

	public Geometry merge(Geometry other) {
		for(Drawable drawable : other.getDrawables()) add(drawable);
		return this;
	}



}
