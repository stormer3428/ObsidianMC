package fr.stormer3428.obsidianMC.Util;

import java.io.File;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import fr.stormer3428.obsidianMC.OMCPlugin;

public enum OMCLang {

	ERROR_GENERIC_NOPERMISSION("You do not have the permission to perform this command (<%PERMISSION>)"),
	ERROR_GENERIC_NOPLAYER("No player with such name : <%PLAYER>"),
	ERROR_PLAYERONLY("You may only run this command as a player"),
	ERROR_INTEGER("Invalid argument \"<%VALUE>\", expected an integer"),
	ERROR_FLOAT("Invalid argument \"<%VALUE>\", expected a float"),
	ERROR_MIN_GT_MAX("Invalid argument, the minimum value <%MIN> is greater than the maximum value <%MAX>"),
	ERROR_MATERIAL_INVALID_NAME("Error, no material with such name '<%MATERIAL>'"),
	
	ERROR_SMPITEM_MISSING_CONFIG("Error, failed to retrieve the configuration of SMP items"),
	ERROR_SMPITEM_MISSING_CONFIG_SECTION("Error, missing a OMCItem configuration section at '<%PATH>'"),
	ERROR_SMPITEM_MISSING_CONFIG_FILE("Error, failed to retrieve the configuration File of SMP items"),
	ERROR_SMPITEM_INVALID_CMD_ID("Error, tried to retrieve custom model data <%CMDID> for SMPItem <%SMPITEM>, but only <%CMDS> available"),
	
	ERROR_OMCPOWER_MISSING_CONFIG("Error, failed to retrieve the configuration of OMCPowers"),
	ERROR_OMCPOWER_MISSING_CONFIG_SECTION("Error, missing a OMCPower configuration section at '<%PATH>'"),
	ERROR_OMCPOWER_MISSING_CONFIG_FILE("Error, failed to retrieve the configuration File of OMCPowers"),

	ERROR_ITEM_MANAGER_REGISTER_NULL("Tried to register a null item to the OMCItem manager"),
	ERROR_ITEM_MANAGER_REGISTER_NULL_NAME("Tried to register an item with no registry name to the OMCItem manager"),
	
	ERROR_POWER_MANAGER_REGISTER_NULL("Tried to register a null power to the OMCPower manager"),
	ERROR_POWER_MANAGER_REGISTER_NULL_NAME("Tried to register a power with no registry name to the OMCPower manager"),
	
	ERROR_CONFIG_MISSING_PATH("Error, tried to access undefined path <%PATH> in config file '<%CONFIG>'"),

	COMMAND_SYNTAX_ERROR("No command with signature \n\"<%SYNTAX>\""), 
	;

	private String path;
	private String def;
	private static YamlConfiguration LANG;

	OMCLang(String d){
		this.path = this.name();
		this.def = d;
	}

	public static void setFile(YamlConfiguration config) {
		LANG = config;
	}

	@Override
	public String toString() {
		try {
			return ChatColor.translateAlternateColorCodes('&', ChatColor.translateAlternateColorCodes('§', LANG.getString(this.path, this.def)));
		}catch (Exception e) {
			return "INTERNAL ERROR, LANG NEVER INSTANCIATED, PLEASE CONTACT AN ADMIN ABOUT THIS ISSUE, (" + getClass().getSimpleName() + ")";
		}
	}

	public String getPath() {
		return this.path;
	}

	public String getDef() {
		return this.def;
	}

	public static void loadFromConfig() {
		File lang = new File(OMCPlugin.i.getDataFolder(), "lang.yml");
		YamlConfiguration langConfig = YamlConfiguration.loadConfiguration(lang);
		if(!lang.exists()) try {
			langConfig.save(lang);
		}catch (IOException e) {
			e.printStackTrace();
			OMCLogger.systemError("Failed to create language file");
			OMCLogger.systemError("Disabling...");
			OMCPlugin.i.getServer().getPluginManager().disablePlugin(OMCPlugin.i);
		}
		for(OMCLang l : OMCLang.values()) if(langConfig.getString(l.getPath()) == null) langConfig.set(l.getPath(), l.getDef());
		OMCLang.setFile(langConfig);
		try {
			langConfig.save(lang);
		}catch (IOException e) {
			e.printStackTrace();
			OMCLogger.systemError("Failed to save language file");
			OMCLogger.systemError("Disabling...");
			OMCPlugin.i.getServer().getPluginManager().disablePlugin(OMCPlugin.i);
		}
	}
}
