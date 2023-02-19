package fr.stormer3428.obsidianMC;

import java.util.ArrayList;

import org.bukkit.plugin.java.JavaPlugin;

public abstract class OMCPlugin extends JavaPlugin{
	
	public static OMCPlugin i;
	public static OMCLogger logger;

	
	private final ArrayList<PluginTied> pluginTied = new ArrayList<>();
	
	@Override
	public void onEnable() {
		i = this;		
		this.reload();
		registerNativePluginTied();
		registerPluginTied();
		for(PluginTied pluginTied : pluginTied) pluginTied.onPluginEnable();
		onObsidianEnable();
	}
	
	private void registerNativePluginTied() {}

	public void reload() {
		OMCLang.loadFromConfig();
		loadLangFromConfig();
		instantiateLogger();
		loadConfig();
	}
	
	@Override
	public void onDisable() {
		for(PluginTied pluginTied : pluginTied) pluginTied.onPluginDisable();	
		onObsidianDisable();
	}
	
	public void loadConfig() {
		getConfig().options().copyDefaults(true);
		saveConfig();
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
