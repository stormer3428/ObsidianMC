package fr.stormer3428.obsidianMC;

import java.util.ArrayList;

import org.bukkit.plugin.java.JavaPlugin;

import fr.stormer3428.obsidianMC.Util.OMCLang;
import fr.stormer3428.obsidianMC.Util.OMCLogger;

public abstract class OMCPlugin extends JavaPlugin{

    public static OMCPlugin i;
	public static OMCLogger logger;

//	private File file;
//	private YamlConfiguration config;

	public static boolean DEBUG = false;

	private final ArrayList<PluginTied> pluginTied = new ArrayList<>();

	@Override
	public void onEnable() {
		i = this;
//		file = new File(OMCPlugin.i.getDataFolder(), "config.yml");
//		config = YamlConfiguration.loadConfiguration(file);
		OMCLogger.debug("loading native lang");
		OMCLang.loadFromConfig();
		OMCLogger.debug("loading stranger lang");
		loadLangFromConfig();
		OMCLogger.debug("requesting logger instanciation");
		instantiateLogger();
		loadConfig();	
		OMCLogger.debug("requestiong a reload");
		this.reload();
		OMCLogger.debug("registering native plugin tied classes");
		registerNativePluginTied();
		OMCLogger.debug("reguesting registering of plugin tied classes");
		registerPluginTied();
		OMCLogger.debug("reguesting enabling of plugin tied classes");
		for(PluginTied pluginTied : pluginTied) pluginTied.onPluginEnable();
		OMCLogger.debug("reguesting enabling of child plugin");
		onObsidianEnable();
	}

	private void registerNativePluginTied() {}

	public void reload() {
		OMCLogger.debug("loading native lang");
		OMCLang.loadFromConfig();
		OMCLogger.debug("loading stranger lang");
		loadLangFromConfig();
		OMCLogger.debug("requesting logger instanciation");
		instantiateLogger();
		loadConfig();	
	}

	@Override
	public void onDisable() {
		OMCLogger.debug("reguesting disabling of plugin tied classes");
		for(PluginTied pluginTied : pluginTied) pluginTied.onPluginDisable();	
		OMCLogger.debug("reguesting disabling of child plugin");
		onObsidianDisable();
	}

//	@Override
//	public FileConfiguration getConfig() {
//		return config;
//	}
//
//	@Override
//	public void saveConfig() {
//		try {
//			getConfig().save(file);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

	public void loadConfig() {
		OMCLogger.debug("loading config");
		getConfig().options().copyDefaults(true);
		saveConfig();
	}

	public void registerPluginTied(PluginTied pluginTied) {
		OMCLogger.debug("reguesting plugin tied class : " + pluginTied.getClass().getSimpleName());
		this.pluginTied.add(pluginTied);
	}

	public abstract void loadLangFromConfig();
	public abstract void registerPluginTied();
	public abstract void onObsidianEnable();
	public abstract void onObsidianDisable();
	public abstract OMCLogger instantiateLogger();

}
