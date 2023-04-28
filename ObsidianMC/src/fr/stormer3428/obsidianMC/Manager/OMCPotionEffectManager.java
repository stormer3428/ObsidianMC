package fr.stormer3428.obsidianMC.Manager;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.event.Listener;

import fr.stormer3428.obsidianMC.OMCPlugin;
import fr.stormer3428.obsidianMC.Config.ConfigHolder;
import fr.stormer3428.obsidianMC.PotionEffects.OMCPotionEffect;

public class OMCPotionEffectManager extends ConfigHolder implements Listener{

	private final ArrayList<OMCPotionEffect> registeredPotionEffects = new ArrayList<>();
	
	public OMCPotionEffectManager(String configName) {
		super(new File(OMCPlugin.i.getDataFolder(), configName));
	}

	public int getDefaultPotionDuration() {
		return getInt("defaultPotionDuration");
	}	
	
	@Override
	public void onPluginEnable() {
		super.onPluginEnable();
		for(OMCPotionEffect potionEffect : registeredPotionEffects) potionEffect.onPluginEnable();
	}
	
	@Override
	public void onPluginReload() {
		super.onPluginReload();
		for(OMCPotionEffect potionEffect : registeredPotionEffects) potionEffect.onPluginReload();
	}
	
	@Override
	public void onPluginDisable() {
		super.onPluginDisable();
		for(OMCPotionEffect potionEffect : registeredPotionEffects) potionEffect.onPluginDisable();
	}

	public void registerPotionEffect(OMCPotionEffect omcPotionEffect) {
		registeredPotionEffects.add(omcPotionEffect);
	}	
}
