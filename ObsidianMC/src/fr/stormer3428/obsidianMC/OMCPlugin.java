package fr.stormer3428.obsidianMC;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.plugin.java.JavaPlugin;

public abstract class OMCPlugin extends JavaPlugin{
	
	public static OMCPlugin i;
	public static OMCLogger logger;

	public OMCConfigManager configManager;
	
	private final ArrayList<PluginTied> pluginTied = new ArrayList<>();
	
	@Override
	public void onEnable() {
		i = this;
		configManager = new OMCConfigManager(new File(getDataFolder().getAbsolutePath(), "config.yml"));
		OMCLang.loadFromConfig();
		loadLangFromConfig();
		instantiateLogger();
		loadConfig();
		registerNativePluginTied();
		registerPluginTied();
		for(PluginTied pluginTied : pluginTied) pluginTied.onPluginEnable();
		onObsidianEnable();
	}
	

	private void registerNativePluginTied() {}

	@Override
	public void onDisable() {
		for(PluginTied pluginTied : pluginTied) pluginTied.onPluginDisable();	
		onObsidianDisable();
	}
	
	public void loadConfig() {
		configManager.loadConfig();
	}
	

	public void registerPluginTied(PluginTied pluginTied) {
		this.pluginTied.add(pluginTied);
	}
	
	public abstract void loadLangFromConfig();
	public abstract void registerPluginTied();
	public abstract void onObsidianEnable();
	public abstract void onObsidianDisable();
	public abstract OMCLogger instantiateLogger();
	
}
