package fr.stormer3428.obsidianMC.Util.Geometry;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class Geometry {

	ArrayList<Line> lines = new ArrayList<>();
	private Vector direction;
	
	public Geometry(Vector direction) {
		this.direction = direction;
	}
	
	public Geometry addLine(Line ... line) {
		this.lines.addAll(Arrays.asList(line));
		return this;
	}

	public Geometry draw(Location location) {
		return draw(location, 1.0);
	}
	
	public Geometry draw(Location location, double scale) {
		for(Line line : lines) line.draw(location, scale);
		return this;
	}
	
	public Geometry rotateAroundAxis(Vector axis, double radians) {
		for(Line line : lines) line.rotateAroundAxis(axis, radians);
		direction.rotateAroundAxis(axis, radians);
		return this;
	}
	
	public Geometry rotateAroundX(double radians) {
		for(Line line : lines) line.rotateAroundX(radians);
		direction.rotateAroundX(radians);
		return this;
	}
	
	public Geometry rotateAroundY(double radians) {
		for(Line line : lines) line.rotateAroundY(radians);
		direction.rotateAroundY(radians);
		return this;
	}
	
	public Geometry rotateAroundZ(double radians) {
		for(Line line : lines) line.rotateAroundZ(radians);
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
		return rotateAroundAxis(direction.getCrossProduct(newDirection).normalize(), direction.angle(newDirection));
	}
}
