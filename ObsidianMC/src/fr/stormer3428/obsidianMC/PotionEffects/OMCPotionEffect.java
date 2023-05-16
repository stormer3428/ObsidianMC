package fr.stormer3428.obsidianMC.PotionEffects;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.stormer3428.obsidianMC.Config.PluginTied;
import fr.stormer3428.obsidianMC.Manager.OMCPotionEffectManager;
import fr.stormer3428.obsidianMC.Util.OMCLang;
import fr.stormer3428.obsidianMC.Util.OMCLogger;

public class OMCPotionEffect implements PluginTied{

	public PotionEffectType effectType = null;
	public int duration = 0;
	public int amplifier = 0;

	public final List<OMCPotionEffectCondition> conditions = new ArrayList<>();

	private final OMCPotionEffectManager potionEffectManager;
	private final String path;

	public OMCPotionEffect(OMCPotionEffectManager potionEffectManager, String path) {
		this.potionEffectManager = potionEffectManager;
		this.path = path;
		potionEffectManager.registerPotionEffect(this);
	}

	@Override
	public void onPluginEnable() {
		onPluginReload();
	}

	@Override
	public void onPluginDisable() {}

	@Override
	public void onPluginReload() {
		conditions.clear();
		effectType = null;
		duration = 0;
		amplifier = 0;

		String potionEffectName = getManager().getString(path + ".type");
		if(potionEffectName == null) {
			OMCLogger.systemError(formatFile(OMCLang.ERROR_POTION_MANAGER_MISSING_POTIONEFFECT.toString()));
			return;
		}
		effectType = PotionEffectType.getByName(potionEffectName);
		if(effectType == null) {
			OMCLogger.systemError(formatPotioneffect(OMCLang.ERROR_POTION_MANAGER_NO_POTIONEFFECT.toString(), potionEffectName));
			return;
		}

		amplifier = getManager().getInt(path + ".amplifier"); OMCLogger.debug("amplifier : " + amplifier);
		duration = getManager().getInt(path + ".duration"); if(duration == 0) duration = potionEffectManager.getDefaultPotionDuration(); OMCLogger.debug("duration : " + duration);

		for(String conditionName : getManager().getStringList(path + ".conditions")) {
			OMCPotionEffectCondition condition = OMCPotionEffectCondition.fromName(conditionName);

			if(condition == null) {
				OMCLogger.systemError(formatConditions(OMCLang.ERROR_POTION_MANAGER_NO_CONDITION.toString(), conditionName));
				continue;
			}
			conditions.add(condition);
		}
		if(conditions.size() == 0) OMCLogger.systemError(formatConditions(OMCLang.ERROR_POTION_MANAGER_MISSING_CONDITION.toString(), ""));
	}

	public boolean shouldApply(LivingEntity p) {
		for(OMCPotionEffectCondition condition : conditions) if(condition.shouldApply(p)) return true;
		return false;
	}

	public void apply(LivingEntity p) {
		apply(p, false);
	}

	public void apply(LivingEntity p, boolean force) {
		if(!force && !shouldApply(p)) return;
		p.addPotionEffect(new PotionEffect(effectType, duration, amplifier));
	}

	public void clear(LivingEntity p) {
		p.removePotionEffect(effectType);
	}

	public OMCPotionEffectManager getManager() {
		return potionEffectManager;
	}

	private static String validConditions(String conditionName) {
		StringBuilder SB = new StringBuilder();
		for(OMCPotionEffectCondition c : OMCPotionEffectCondition.values()) SB.append("\n" + c.name() + (conditionName != null && c.name().toLowerCase().contains(conditionName) ? " <= (It might be this one)" : ""));
		return SB.toString();
	}

	private static String validPotioneffects(String potioneffectName) {
		StringBuilder SB = new StringBuilder();
		for(PotionEffectType p : PotionEffectType.values()) SB.append("\n" + p.getName() + (potioneffectName != null && p.getName().toLowerCase().contains(potioneffectName) ? " <= (It might be this one)" : ""));
		return SB.toString();
	}
	
	private String formatPotioneffect(String toFormat, String potionEffectName) {
		return formatFile(toFormat.replace("<%VALID>", validPotioneffects(potionEffectName)).replace("<%INVALID>", potionEffectName + ""));
	}

	private String formatConditions(String toFormat, String conditionName) {
		return formatFile(toFormat.replace("<%VALID>", validConditions(conditionName)).replace("<%INVALID>", conditionName + ""));
	}
	

	private String formatFile(String toFormat) {
		return toFormat.replace("<%FILE>", getManager().getConfigFile().getName()).replace("<%POTIONEFFECT>", path);
	}

}
