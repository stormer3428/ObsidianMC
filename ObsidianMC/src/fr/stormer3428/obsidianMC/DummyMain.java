package fr.stormer3428.obsidianMC;

import org.bukkit.plugin.java.JavaPlugin;

public class DummyMain extends JavaPlugin{


	@Override
	public void onEnable() {
		loadConfig();
		OMCPlugin.DEBUG = getConfig().getBoolean("debug");
	}
	
	public void loadConfig() {
		getConfig().options().copyDefaults(true);
		saveConfig();
	}
	
}
