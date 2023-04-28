package fr.stormer3428.obsidianMC.Config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import fr.stormer3428.obsidianMC.OMCPlugin;
import fr.stormer3428.obsidianMC.Util.OMCLang;
import fr.stormer3428.obsidianMC.Util.OMCLogger;
import fr.stormer3428.obsidianMC.Util.OMCUtil;

public class ConfigHolder implements PluginTied{

	private File configFile;
	private YamlConfiguration config;

	private static final ArrayList<ConfigHolder> HOLDERS = new ArrayList<>();

	public static void reloadAllConfigs() {
		for(ConfigHolder holder : HOLDERS) holder.reloadConfig();
	}

	public ConfigHolder(File configFile) {
		this.configFile = configFile;
		HOLDERS.add(this);
	}

	@Override
	public void onPluginEnable() {
		onPluginReload();
	}

	@Override
	public void onPluginDisable() {}

	@Override
	public void onPluginReload() {
		reloadConfig();
	}

	public File getConfigFile() {
		return configFile;
	}

	public YamlConfiguration getConfig() {
		return config;
	}

	public void reloadConfig() {
		if(!configFile.exists())  {
			OMCLogger.systemError(OMCLang.ERROR_MISSING_CONFIG_FILE.toString().replace("<%FILE>", configFile.getName()));
			OMCPlugin.i.saveResource(configFile.getName(), false);
		}
		config = YamlConfiguration.loadConfiguration(configFile);
	}

	public String getString(String path) {
		if(config == null) {
			OMCLogger.systemError(OMCLang.ERROR_MISSING_CONFIG.toString().replace("<%FILE>", configFile.getName()));
			return path;
		}
		if(!config.isSet(path)) OMCLogger.systemError(OMCLang.ERROR_CONFIG_MISSING_PATH.toString().replace("<%PATH>", config.getCurrentPath().isBlank() ? path : String.join(".", config.getCurrentPath(), path)).replace("<%CONFIG>", getConfigFile().getName()));
		return config.getString(path);
	}

	public int getInt(String path) {
		if(config == null) {
			OMCLogger.systemError(OMCLang.ERROR_MISSING_CONFIG.toString());
			return 0;
		}
		if(!config.isSet(path)) OMCLogger.systemError(OMCLang.ERROR_CONFIG_MISSING_PATH.toString().replace("<%PATH>", config.getCurrentPath().isBlank() ? path : String.join(".", config.getCurrentPath(), path)).replace("<%CONFIG>", getConfigFile().getName()));
		return config.getInt(path);
	}

	public boolean getBoolean(String path) {
		if(config == null) {
			OMCLogger.systemError(OMCLang.ERROR_MISSING_CONFIG.toString());
			return false;
		}
		if(!config.isSet(path)) OMCLogger.systemError(OMCLang.ERROR_CONFIG_MISSING_PATH.toString().replace("<%PATH>", config.getCurrentPath().isBlank() ? path : String.join(".", config.getCurrentPath(), path)).replace("<%CONFIG>", getConfigFile().getName()));
		return config.getBoolean(path);
	}

	public double getDouble(String path) {
		if(config == null) {
			OMCLogger.systemError(OMCLang.ERROR_MISSING_CONFIG.toString());
			return 0;
		}
		if(!config.isSet(path)) OMCLogger.systemError(OMCLang.ERROR_CONFIG_MISSING_PATH.toString().replace("<%PATH>", config.getCurrentPath().isBlank() ? path : String.join(".", config.getCurrentPath(), path)).replace("<%CONFIG>", getConfigFile().getName()));
		return config.getDouble(path);
	}

	public long getLong(String path) {
		if(config == null) {
			OMCLogger.systemError(OMCLang.ERROR_MISSING_CONFIG.toString());
			return 0;
		}
		if(!config.isSet(path)) OMCLogger.systemError(OMCLang.ERROR_CONFIG_MISSING_PATH.toString().replace("<%PATH>", config.getCurrentPath().isBlank() ? path : String.join(".", config.getCurrentPath(), path)).replace("<%CONFIG>", getConfigFile().getName()));
		return config.getLong(path);
	}

	public List<String> getStringList(String path) {
		List<String> list = new ArrayList<>();
		if(config == null) {
			OMCLogger.systemError(OMCLang.ERROR_MISSING_CONFIG.toString());
			return list;
		}
		if(!config.contains(path, true)) return list;
		if(!config.isSet(path)) OMCLogger.systemError(OMCLang.ERROR_CONFIG_MISSING_PATH.toString().replace("<%PATH>", config.getCurrentPath().isBlank() ? path : String.join(".", config.getCurrentPath(), path)).replace("<%CONFIG>", getConfigFile().getName()));
		list.addAll(config.getStringList(path));
		for(int i = 0; i < list.size(); i++) list.set(i, OMCUtil.translateChatColor(list.get(i)));
		return list;
	}

	public List<Integer> getIntegerList(String path) {
		List<Integer> list = new ArrayList<>();
		if(config == null) {
			OMCLogger.systemError(OMCLang.ERROR_MISSING_CONFIG.toString());
			return list;
		}
		if(!config.contains(path, true)) return list;
		if(!config.isSet(path)) OMCLogger.systemError(OMCLang.ERROR_CONFIG_MISSING_PATH.toString().replace("<%PATH>", config.getCurrentPath().isBlank() ? path : String.join(".", config.getCurrentPath(), path)).replace("<%CONFIG>", getConfigFile().getName()));
		list.addAll(config.getIntegerList(path));
		return list;
	}

	public List<Boolean> getBooleanList(String path) {
		List<Boolean> list = new ArrayList<>();
		if(config == null) {
			OMCLogger.systemError(OMCLang.ERROR_MISSING_CONFIG.toString());
			return list;
		}
		if(!config.contains(path, true)) return list;
		if(!config.isSet(path)) OMCLogger.systemError(OMCLang.ERROR_CONFIG_MISSING_PATH.toString().replace("<%PATH>", config.getCurrentPath().isBlank() ? path : String.join(".", config.getCurrentPath(), path)).replace("<%CONFIG>", getConfigFile().getName()));
		list.addAll(config.getBooleanList(path));
		return list;
	}

	public List<Double> getDoubleList(String path) {
		List<Double> list = new ArrayList<>();
		if(config == null) {
			OMCLogger.systemError(OMCLang.ERROR_MISSING_CONFIG.toString());
			return list;
		}
		if(!config.contains(path, true)) return list;
		if(!config.isSet(path)) OMCLogger.systemError(OMCLang.ERROR_CONFIG_MISSING_PATH.toString().replace("<%PATH>", config.getCurrentPath().isBlank() ? path : String.join(".", config.getCurrentPath(), path)).replace("<%CONFIG>", getConfigFile().getName()));
		list.addAll(config.getDoubleList(path));
		return list;
	}

	public List<Float> getFloatList(String path) {
		List<Float> list = new ArrayList<>();
		if(config == null) {
			OMCLogger.systemError(OMCLang.ERROR_MISSING_CONFIG.toString());
			return list;
		}
		if(!config.contains(path, true)) return list;
		if(!config.isSet(path)) OMCLogger.systemError(OMCLang.ERROR_CONFIG_MISSING_PATH.toString().replace("<%PATH>", config.getCurrentPath().isBlank() ? path : String.join(".", config.getCurrentPath(), path)).replace("<%CONFIG>", getConfigFile().getName()));
		list.addAll(config.getFloatList(path));
		return list;
	}

	public List<Long> getLongList(String path) {
		List<Long> list = new ArrayList<>();
		if(config == null) {
			OMCLogger.systemError(OMCLang.ERROR_MISSING_CONFIG.toString());
			return list;
		}
		if(!config.contains(path, true)) return list;
		if(!config.isSet(path)) OMCLogger.systemError(OMCLang.ERROR_CONFIG_MISSING_PATH.toString().replace("<%PATH>", config.getCurrentPath().isBlank() ? path : String.join(".", config.getCurrentPath(), path)).replace("<%CONFIG>", getConfigFile().getName()));
		list.addAll(config.getLongList(path));
		return list;
	}

	public List<Byte> getByteList(String path) {
		List<Byte> list = new ArrayList<>();
		if(config == null) {
			OMCLogger.systemError(OMCLang.ERROR_MISSING_CONFIG.toString());
			return list;
		}
		if(!config.contains(path, true)) return list;
		if(!config.isSet(path)) OMCLogger.systemError(OMCLang.ERROR_CONFIG_MISSING_PATH.toString().replace("<%PATH>", config.getCurrentPath().isBlank() ? path : String.join(".", config.getCurrentPath(), path)).replace("<%CONFIG>", getConfigFile().getName()));
		list.addAll(config.getByteList(path));
		return list;
	}

	public List<Character> getCharacterList(String path) {
		List<Character> list = new ArrayList<>();
		if(config == null) {
			OMCLogger.systemError(OMCLang.ERROR_MISSING_CONFIG.toString());
			return list;
		}
		if(!config.contains(path, true)) return list;
		if(!config.isSet(path)) OMCLogger.systemError(OMCLang.ERROR_CONFIG_MISSING_PATH.toString().replace("<%PATH>", config.getCurrentPath().isBlank() ? path : String.join(".", config.getCurrentPath(), path)).replace("<%CONFIG>", getConfigFile().getName()));
		list.addAll(config.getCharacterList(path));
		return list;
	}

	public List<Short> getShortList(String path) {
		List<Short> list = new ArrayList<>();
		if(config == null) {
			OMCLogger.systemError(OMCLang.ERROR_MISSING_CONFIG.toString());
			return list;
		}
		if(!config.contains(path, true)) return list;
		if(!config.isSet(path)) OMCLogger.systemError(OMCLang.ERROR_CONFIG_MISSING_PATH.toString().replace("<%PATH>", config.getCurrentPath().isBlank() ? path : String.join(".", config.getCurrentPath(), path)).replace("<%CONFIG>", getConfigFile().getName()));
		list.addAll(config.getShortList(path));
		return list;
	}

	public Vector getVector(String path) {
		if(config == null) {
			OMCLogger.systemError(OMCLang.ERROR_MISSING_CONFIG.toString());
			return null;
		}
		if(!config.isSet(path)) OMCLogger.systemError(OMCLang.ERROR_CONFIG_MISSING_PATH.toString().replace("<%PATH>", config.getCurrentPath().isBlank() ? path : String.join(".", config.getCurrentPath(), path)).replace("<%CONFIG>", getConfigFile().getName()));
		return config.getVector(path);
	}

	public OfflinePlayer getOfflinePlayer(String path) {
		if(config == null) {
			OMCLogger.systemError(OMCLang.ERROR_MISSING_CONFIG.toString());
			return null;
		}
		if(!config.isSet(path)) OMCLogger.systemError(OMCLang.ERROR_CONFIG_MISSING_PATH.toString().replace("<%PATH>", config.getCurrentPath().isBlank() ? path : String.join(".", config.getCurrentPath(), path)).replace("<%CONFIG>", getConfigFile().getName()));
		return config.getOfflinePlayer(path);
	}

	public ItemStack getItemStack(String path) {
		if(config == null) {
			OMCLogger.systemError(OMCLang.ERROR_MISSING_CONFIG.toString());
			return null;
		}
		if(!config.isSet(path)) OMCLogger.systemError(OMCLang.ERROR_CONFIG_MISSING_PATH.toString().replace("<%PATH>", config.getCurrentPath().isBlank() ? path : String.join(".", config.getCurrentPath(), path)).replace("<%CONFIG>", getConfigFile().getName()));
		return config.getItemStack(path);
	}

	public Color getColor(String path) {
		if(config == null) {
			OMCLogger.systemError(OMCLang.ERROR_MISSING_CONFIG.toString());
			return null;
		}
		if(!config.isSet(path)) OMCLogger.systemError(OMCLang.ERROR_CONFIG_MISSING_PATH.toString().replace("<%PATH>", config.getCurrentPath().isBlank() ? path : String.join(".", config.getCurrentPath(), path)).replace("<%CONFIG>", getConfigFile().getName()));
		return config.getColor(path);
	}

	public Location getLocation(String path) {
		if(config == null) {
			OMCLogger.systemError(OMCLang.ERROR_MISSING_CONFIG.toString());
			return null;
		}
		if(!config.isSet(path)) OMCLogger.systemError(OMCLang.ERROR_CONFIG_MISSING_PATH.toString().replace("<%PATH>", config.getCurrentPath().isBlank() ? path : String.join(".", config.getCurrentPath(), path)).replace("<%CONFIG>", getConfigFile().getName()));
		return config.getLocation(path);
	}

	public ConfigurationSection getConfigurationSection(String path) {
		if(config == null) {
			OMCLogger.systemError(OMCLang.ERROR_MISSING_CONFIG.toString());
			return null;
		}
		if(!config.isConfigurationSection(path)) {
			OMCLogger.systemError(OMCLang.ERROR_MISSING_CONFIG_SECTION.toString().replace("<%PATH>", path));
			return null;
		}
		return config.getConfigurationSection(path);
	}

}
