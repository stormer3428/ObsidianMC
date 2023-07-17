package fr.stormer3428.obsidianMC;

import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.plugin.java.JavaPlugin;

import fr.stormer3428.obsidianMC.Config.AutoConfig;
import fr.stormer3428.obsidianMC.Config.AutoconfigParser;
import fr.stormer3428.obsidianMC.Config.BooleanConfigValue;
import fr.stormer3428.obsidianMC.Config.PluginTied;
import fr.stormer3428.obsidianMC.Util.OMCLang;
import fr.stormer3428.obsidianMC.Util.OMCLogger;

@AutoConfig
public abstract class OMCPlugin extends JavaPlugin{

    public static OMCPlugin i;
	public static OMCLogger logger;

	public OMCPlugin() {
		i = this;
		autoconfigParser.registerAutoConfigClass(OMCPlugin.class);
	}

	@BooleanConfigValue(path = "debug")
	public static boolean DEBUG = false;

	public final AutoconfigParser autoconfigParser = new AutoconfigParser();;
	protected final HashSet<PluginTied> pluginTieds = new HashSet<>();

	@Override
	public void onEnable() {
		this.reload();
		OMCLogger.debug("registering native plugin tied classes");
		registerNativePluginTied();
		OMCLogger.debug("reguesting registering of plugin tied classes");
		registerPluginTied();
		OMCLogger.debug("reguesting enabling of plugin tied classes");
		for(PluginTied pluginTied : new ArrayList<>(pluginTieds)) pluginTied.onPluginEnable();
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
		OMCLogger.debug("injecting config values into classes");
		autoconfigParser.updateValues();
		OMCLogger.debug("reloading plugin tied");
		for(PluginTied pluginTied : new ArrayList<>(pluginTieds)) pluginTied.onPluginReload();
	}

	@Override
	public void onDisable() {
		OMCLogger.debug("reguesting disabling of plugin tied classes");
		for(PluginTied pluginTied : pluginTieds) pluginTied.onPluginDisable();	
		OMCLogger.debug("reguesting disabling of child plugin");
		onObsidianDisable();
	}

	public void registerPluginTied(PluginTied pluginTied) {
		OMCLogger.debug("reguesting plugin tied class : " + pluginTied.getClass().getSimpleName());
		this.pluginTieds.add(pluginTied);
	}
	
	public abstract void loadLangFromConfig();
	public abstract void registerPluginTied();
	public abstract void onObsidianEnable();
	public abstract void onObsidianDisable();
	public abstract OMCLogger instantiateLogger();

}
