package fr.stormer3428.obsidianMC.assets;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;
import java.util.function.Consumer;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import fr.stormer3428.obsidianMC.Util.Geometry.ParticleUtils;

public class LightningStrike {

	private static final double RESOLUTION = 2.0;
	private static final double MAX_OFFSET = 2.0;
	private static final Random random = new Random();

	public LightningStrike(
			Location a, 
			Location b, 
			double raySize, 
			Particle particle, 
			DustOptions options,
			Consumer<LivingEntity> hitConsumer
			) {

		World world = a.getWorld();

		double distance = a.distance(b);
		int breakPointAmount = (int) (a.distance(b)/RESOLUTION);

		Vector a2b = b.toVector().subtract(a.toVector());
		Vector dir = a2b.clone().normalize();
		Vector offset = new Vector(0,1,0);
		if(offset.equals(dir) || offset.equals(dir.clone().multiply(-1))) offset = new Vector(1,0,0).crossProduct(dir).normalize();
		else offset = offset.crossProduct(dir).normalize();

		ArrayList<Double> breakPointDistances = new ArrayList<>();
		ArrayList<Vector> breakPoints = new ArrayList<>();
		for(int i = breakPointAmount; i > 0; i--) breakPointDistances.add(random.nextDouble() * distance);
		breakPointDistances.sort(Comparator.naturalOrder());
		for(Double distances : breakPointDistances) breakPoints.add(a.toVector().add(dir.clone().multiply(distances)));
		for(Vector breakPoint : breakPoints) breakPoint.add(offset.normalize().multiply(random.nextDouble() * MAX_OFFSET).rotateAroundAxis(dir, random.nextDouble() * Math.PI * 2));

		Vector previousPoint = a.toVector();
		for(Vector breakPoint : breakPoints) {
			ParticleUtils.drawParticleLine(previousPoint.toLocation(world), breakPoint.toLocation(world), particle, 0.1, 1, new Vector(), 0.0, options);
			previousPoint = breakPoint;
		}
		ParticleUtils.drawParticleLine(previousPoint.toLocation(world), b, particle, 0.1, 1, new Vector(), 0.0, options);
		b.getWorld().spawnParticle(Particle.FLASH, b, 1, 0, 0, 0, 0, null, true);
		
		if(hitConsumer != null) {

			ArrayList<LivingEntity> struck = new ArrayList<>();

			double distanceLeft = distance;
			Location rayLoc = a.clone();

			while(distanceLeft > 0) {
				RayTraceResult rtr = world.rayTraceEntities(a, dir, distanceLeft + 1, raySize, t -> {
					return t instanceof LivingEntity le && !struck.contains(le);
				});
				if(rtr == null || rtr.getHitEntity() == null) {
					distanceLeft = 0;
					break;
				}
				struck.add((LivingEntity) rtr.getHitEntity());
				distanceLeft -= rtr.getHitPosition().distance(rayLoc.toVector());
				rayLoc = rtr.getHitPosition().toLocation(world);
			}

			for(LivingEntity le : struck) {
				hitConsumer.accept(le);
			}
		}
	}

}
