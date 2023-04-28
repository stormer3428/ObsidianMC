package fr.stormer3428.obsidianMC.PotionEffects;

import org.bukkit.World.Environment;
import org.bukkit.entity.LivingEntity;

import fr.stormer3428.obsidianMC.Util.OMCUtil;

public enum OMCPotionEffectCondition{
	ALWAYS{

		@Override
		public boolean shouldApply(LivingEntity e) {
			return true;
		}
		
	},
	WATER{

		@Override
		public boolean shouldApply(LivingEntity e) {
			return e.isInWater() || OMCUtil.isUnderRain(e);
		}
		
	},
	FIRE{

		@Override
		public boolean shouldApply(LivingEntity e) {
			return e.getFireTicks() != -20;
		}
		
	},
	NETHER{

		@Override
		public boolean shouldApply(LivingEntity e) {
			return e.getWorld().getEnvironment().equals(Environment.NETHER);
		}
		
	},
	
	;
	
	public static OMCPotionEffectCondition fromName(String name) {
		for(OMCPotionEffectCondition c : OMCPotionEffectCondition.values()) if(c.name().equals(name)) return c;
		return null;
	}
	
	public abstract boolean shouldApply(LivingEntity e);
}
