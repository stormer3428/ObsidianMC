package fr.stormer3428.obsidianMC;

import java.io.File;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

public enum OMCLang {

	ERROR_GENERIC_NOPERMISSION("You do not have the permission to perform this command (<%PERMISSION>)"),
	ERROR_GENERIC_NOPLAYER("No player with such name : <%PLAYER>"),
	ERROR_PLAYERONLY("You may only run this command as a player"),
	ERROR_INTEGER("Invalid argument \"<%VALUE>\", expected an integer"),
	ERROR_FLOAT("Invalid argument \"<%VALUE>\", expected a float"),
	ERROR_MIN_GT_MAX("Invalid argument, the minimum value <%MIN> is greater than the maximum value <%MAX>"),

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
			return ChatColor.translateAlternateColorCodes('&', LANG.getString(this.path, this.def));
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
