package fr.stormer3428.obsidianMC.Util;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

public class OMCUtil {

	public static float noteBlockToPitch(int note) {
		return (float) ((Math.pow(2, note/12f))/2f);
	}
	
	public static ItemStack createNamedItemstack(Material material, int stackSize, String name) {
		return createNamedItemstack(material, stackSize, name, true);
	}

	public static ItemStack createNamedItemstack(Material material, int stackSize, String name, boolean hideData) {
		return createNamedItemstack(new ItemStack(material, stackSize), name, hideData);
	}

	public static ItemStack createNamedItemstack(ItemStack it, String name) {
		return createNamedItemstack(it, name, true);
	}

	public static ItemStack createNamedItemstack(ItemStack it, String name, boolean hideData) {

		ItemMeta itm = it.getItemMeta();
		itm.setDisplayName(name);
		if(hideData) {
			itm.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			itm.addItemFlags(ItemFlag.HIDE_DESTROYS);
			itm.addItemFlags(ItemFlag.HIDE_DYE);
			itm.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			itm.addItemFlags(ItemFlag.HIDE_PLACED_ON);
			itm.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
			itm.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		}
		it.setItemMeta(itm);
		return it;
	}
	
	public static ItemStack createCMDItemstack(Material material, String name, int CMD) {
		ItemStack it = new ItemStack(material);
		ItemMeta itm = it.getItemMeta();
		itm.setDisplayName(ChatColor.RESET + name);
		itm.setCustomModelData(CMD);
		it.setItemMeta(itm);
		return it;
	}
	
	public static String translateChatColor(String s) {
		return ChatColor.translateAlternateColorCodes('$', ChatColor.translateAlternateColorCodes('&', s));
	}
	
	public static boolean isUnderRain(LivingEntity le) {
		return le.getWorld().hasStorm() && isUnderSky(le) && !isInHotArea(le);
	}

	public static boolean isUnderSnow(LivingEntity le) {
		return isUnderRain(le) && isInColdArea(le);
	}

	public static boolean isUnderSky(LivingEntity le) {
		return le.getWorld().getHighestBlockAt(le.getLocation()).getY() < le.getLocation().getY() + 1;
	}
	
	public static boolean isInColdArea(LivingEntity le) {
		return le.getLocation().getBlock().getTemperature() <= 0.15;
	}

	public static boolean isInWarmArea(LivingEntity le) {
		return le.getLocation().getBlock().getTemperature() >= 0.5;
	}

	public static boolean isInHotArea(LivingEntity le) {
		return le.getLocation().getBlock().getTemperature() > 1;
	}

	public static int getSunlight(LivingEntity le) {
		return le.getLocation().getBlock().getLightFromSky();
	}

	public static boolean isWet(LivingEntity le) {
		return le.isInWater() || isUnderRain(le);
	}
	
	public static boolean isSubmerged(LivingEntity le) {
		Material b = le.getEyeLocation().getBlock().getType();

		boolean bool = le.getEyeLocation().getBlock().getBlockData() instanceof Waterlogged w && w.isWaterlogged()
				|| b.equals(Material.WATER)
				|| b.equals(Material.KELP)
				|| b.equals(Material.SEAGRASS)
				|| b.equals(Material.TALL_SEAGRASS)
				|| b.equals(Material.BUBBLE_COLUMN)
				;
		return bool;
	}

	public static void drawParticleLine(Location a, Location b, Particle particle) {
		drawParticleLine(a, b, particle, 0.1d);
	}

	public static void drawParticleLine(Location a, Location b, Particle particle, double resolution) {
		drawParticleLine(a, b, particle, resolution, 1);
	}

	public static void drawParticleLine(Location a, Location b, Particle particle, double resolution, int pamount) {
		drawParticleLine(a, b, particle, resolution, pamount, new Vector());
	}

	public static void drawParticleLine(Location a, Location b, Particle particle, double resolution, int pamount, Vector spread) {
		drawParticleLine(a, b, particle, resolution, pamount, spread, 0.0d);
	}
	
	public static void drawParticleLine(Location a, Location b, Particle particle, double resolution, int pamount, Vector spread, double speed) {
		double d = a.distance(b);
		int amount = (int) (d/resolution) + 2;

		Vector a2b = b.toVector().subtract(a.toVector()).normalize().multiply(d/amount);
		Location particleloc = a.clone();
		for(int c = amount; c > 0; c--) {
			particleloc.getWorld().spawnParticle(particle, particleloc, pamount, spread.getX(), spread.getY(), spread.getZ(), speed, null, true);
			particleloc.add(a2b);
		}
	}
	
	
	
	public static void spawnMovingParticle(Particle particle, Location loc, Vector dir, double speed) {
		loc.getWorld().spawnParticle(particle, loc, 0, dir.getX(), dir.getY(), dir.getZ(), speed, null, true);
	}
	
	public static final Vector VERTICAL = new Vector(0,1,0);

	public static Vector validateVector(Vector v) {
		if(v.getX() == 0) v.setX(Float.MIN_VALUE);
		if(v.getY() == 0) v.setY(Float.MIN_VALUE);
		if(v.getZ() == 0) v.setZ(Float.MIN_VALUE);
		return v;
	}

	public static Vector getPerpendicularVector(Vector v) {
		Vector direction = validateVector(v.clone());
		return new Vector(1, 1, (-direction.getX() * 1 - direction.getY() * 1) / direction.getZ()).normalize();
	}

	public static Vector getRandomVector() {
		Random r = new Random();
		return new Vector((r.nextDouble() * 2)-1d,(r.nextDouble() * 2)-1d,(r.nextDouble() * 2)-1d).normalize();
	}

	public static Vector getRelativeUpUnitVector(Location loc) {
		final Location location = loc.clone();
		location.setPitch(loc.getPitch() + 90);
		return location.getDirection();
	}
	
	public static double map(double input_start, double input_end, double output_start, double output_end, double input) {
//		double slope = (output_end - output_start) / (input_end - input_start);
//		double output = output_start + slope * (input - input_start);
//		return output;
		return mapOneRangeToAnother(input, input_start, input_end, output_start, output_end, 3);
	}
	
	public static double mapOneRangeToAnother(double sourceNumber, double fromA, double fromB, double toA, double toB, int decimalPrecision ) {
	    double deltaA = fromB - fromA;
	    double deltaB = toB - toA;
	    double scale  = deltaB / deltaA;
	    double negA   = -1 * fromA;
	    double offset = (negA * scale) + toA;
	    double finalNumber = (sourceNumber * scale) + offset;
	    int calcScale = (int) Math.pow(10, decimalPrecision);
	    return (double) Math.round(finalNumber * calcScale) / calcScale;
	}

	public static final int[] values = 			{1000,  900, 500,  400, 100,   90,  50,   40,  10,    9,   5,    4,   1};
	public static final String[] romanLetters = { "M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

	public static String intToRoman(int num){
		StringBuilder roman = new StringBuilder();
		for(int i = 0; i < values.length; i++){
			while(num >= values[i]){
				num = num - values[i];
				roman.append(romanLetters[i]);
			}
		}
		return roman.toString();
	}
}














