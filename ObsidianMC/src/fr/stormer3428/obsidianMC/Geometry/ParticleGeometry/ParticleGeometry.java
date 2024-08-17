package fr.stormer3428.obsidianMC.Geometry.ParticleGeometry;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import fr.stormer3428.obsidianMC.OMCLogger;
import fr.stormer3428.obsidianMC.Util.GeometryUtils;

public class ParticleGeometry {

	ArrayList<ParticleDrawable> drawables = new ArrayList<>();
	private Vector direction = GeometryUtils.VERTICAL.clone();

	public ParticleGeometry() {}
	
	public ParticleGeometry add(ParticleDrawable ... drawables) {
		this.drawables.addAll(Arrays.asList(drawables));
		return this;
	}
	
	public ParticleGeometry draw(Location location) {
		return draw(location, 1.0);
	}
	
	public ParticleGeometry draw(Location location, double scale) {
		for(ParticleDrawable drawable : drawables) drawable.draw(location, scale);
		return this;
	}
	
	public ParticleGeometry rotateAroundAxis(Vector axis, double radians) {
		try {
			axis.checkFinite();
		}catch (Exception e) {
			OMCLogger.systemError("Error, attempted rotate around NaN (" + axis + ")");
			return this;
		}
		for(ParticleDrawable drawable : drawables) drawable.rotateAroundAxis(axis, radians);
		direction.rotateAroundAxis(axis, radians);
		return this;
	}
	
	public ParticleGeometry rotateAroundX(double radians) {
		for(ParticleDrawable drawable : drawables) drawable.rotateAroundX(radians);
		direction.rotateAroundX(radians);
		return this;
	}
	
	public ParticleGeometry rotateAroundY(double radians) {
		for(ParticleDrawable drawable : drawables) drawable.rotateAroundY(radians);
		direction.rotateAroundY(radians);
		return this;
	}
	
	public ParticleGeometry rotateAroundZ(double radians) {
		for(ParticleDrawable drawable : drawables) drawable.rotateAroundZ(radians);
		direction.rotateAroundZ(radians);
		return this;
	}
	
	public ParticleGeometry setDirection(Vector newDirection) {
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

	public ParticleGeometry setParticleSpeed(float speed) {
		for(ParticleDrawable drawable : drawables) drawable.setParticleSpeed(speed);
		return this;
	}
	
	public ParticleGeometry setParticle(Particle particle) {
		for(ParticleDrawable drawable : drawables) drawable.setParticle(particle);
		return this;
	}

	public <T> ParticleGeometry setParticleData(T particleData) {
		for(ParticleDrawable drawable : drawables) drawable.setParticleData(particleData);
		return this;
	}

	public ParticleGeometry setForceRendering(boolean forceRender) {
		for(ParticleDrawable drawable : drawables) drawable.setForceRendering(forceRender);
		return this;
	}
	
	private ArrayList<ParticleDrawable> getDrawables() {
		return drawables;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends ParticleDrawable>  ArrayList<T> getDrawables(Class<T> clazz) {
		ArrayList<T> list = new ArrayList<>();
		for(ParticleDrawable drawable : drawables) if(clazz.isInstance(drawable)) list.add((T) drawable);
		return list;
	}

	public ParticleGeometry merge(ParticleGeometry other) {
		for(ParticleDrawable drawable : other.getDrawables()) add(drawable);
		return this;
	}



}
