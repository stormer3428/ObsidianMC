package fr.stormer3428.obsidianMC.Power;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import fr.stormer3428.obsidianMC.OMCPlugin;
import fr.stormer3428.obsidianMC.PluginTied;
import fr.stormer3428.obsidianMC.Manager.OMCPowerManager;
import fr.stormer3428.obsidianMC.Util.OMCLang;
import fr.stormer3428.obsidianMC.Util.OMCLogger;

public abstract class OMCPower implements PluginTied{
	
	private static final String KEY_SECTION = "OMCPowers";
	private static final String KEY_COOLDOWN = "cooldown";
	private static final String KEY_DURATION = "duration";
	private static final String KEY_ENABLED = "enabled";
	protected ArrayList<Player> empowered = new ArrayList<>();
	protected ArrayList<Player> onCooldown = new ArrayList<>();

	private final OMCPowerManager powerManager;
	private final String registryName;

	public abstract void onEmpower(Player p);
	public abstract void onDepower(Player p);
	public abstract void onCooldownEnd(Player p);
	public abstract void onTick(Player p, int ticker);	

	public boolean meetsPreconditions(Player p) {
		return true;
	}

	public OMCPower(String registryName, OMCPowerManager powerManager) {
		this.powerManager = powerManager;
		this.registryName = registryName;
	}
	
	public ConfigurationSection getConfigurationSection() {
		FileConfiguration config = powerManager.getPowerConfig();
		if(config == null) {
			OMCLogger.systemError(OMCLang.ERROR_OMCPOWER_MISSING_CONFIG.toString());
			return null;
		}
		String path = KEY_SECTION + "." + getRegistryName();
		if(!config.isConfigurationSection(path)) {
			OMCLogger.systemError(OMCLang.ERROR_OMCPOWER_MISSING_CONFIG_SECTION.toString().replace("<%PATH>", path));
			return null;
		}
		return config.getConfigurationSection(path);
	}
	
	public File getFile() {
		File config = powerManager.getPowerConfigFile();
		if(config == null) {
			OMCLogger.systemError(OMCLang.ERROR_OMCPOWER_MISSING_CONFIG_FILE.toString());
			return null;
		}
		return config;
	}

	public void tryCast(ItemStack it, Player p) {
		if(!isEnabled()) return;
		if(onCooldown.contains(p) || empowered.contains(p) || !meetsPreconditions(p)) return;
		empower(p);
	}

	private void empower(Player p) {
		empowered.add(p);
		onEmpower(p);
		new BukkitRunnable() {

			@Override
			public void run() {
				empowered.remove(p);
				putOnCooldown(p);
				onDepower(p);
			}
		}.runTaskLater(OMCPlugin.i, getDuration());
	}
	
	protected void putOnCooldown(Player p) {
		int abilityCooldown = getCooldown();
		onCooldown.add(p);
		onDepower(p);
		new BukkitRunnable() {

			@Override
			public void run() {
				onCooldown.remove(p);
				onCooldownEnd(p);
			}
		}.runTaskLater(OMCPlugin.i, abilityCooldown);
	}
	
	public void clearCooldown(Player p) {
		onCooldown.remove(p);
	}
	
	private int getCooldown() {
		ConfigurationSection config = getConfigurationSection();
		if(config == null) return 0;
		if(!config.isSet(KEY_COOLDOWN)) OMCLogger.systemError(OMCLang.ERROR_CONFIG_MISSING_PATH.toString().replace("<%PATH>", String.join(".", config.getCurrentPath(), KEY_COOLDOWN)));
		return config.getInt(KEY_COOLDOWN);
	}

	private int getDuration() {
		ConfigurationSection config = getConfigurationSection();
		if(config == null) return 0;
		if(!config.isSet(KEY_DURATION)) OMCLogger.systemError(OMCLang.ERROR_CONFIG_MISSING_PATH.toString().replace("<%PATH>", String.join(".", config.getCurrentPath(), KEY_DURATION)));
		return config.getInt(KEY_DURATION);
	}
	
	public boolean isEnabled() {
		ConfigurationSection config = getConfigurationSection();
		if(config == null) return false;
		if(!config.isSet(KEY_ENABLED)) OMCLogger.systemError(OMCLang.ERROR_CONFIG_MISSING_PATH.toString().replace("<%PATH>", String.join(".", config.getCurrentPath(), KEY_ENABLED)));
		return config.getBoolean(KEY_ENABLED);
	}

	public final OMCPowerManager getPowerManager() {
		return powerManager;
	}
	
	public String getRegistryName() {
		return registryName;
	}

}
