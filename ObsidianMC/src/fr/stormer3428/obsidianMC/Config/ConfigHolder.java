package fr.stormer3428.obsidianMC.Config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

/**
 * Allows for an easy config implementation, only requiring to be given a file. <br>
 * Upon {@link #onPluginEnable()} and {@link #onPluginReload()}, will check for the config in the config folder, <br>
 * if it is not found it will import it from the plugin's jar file <br>
 * <br>
 * Will also log any accesses to undefined paths, allowing for an easier time debugging at the configuration stage
 * 
 * @implNote
 * implements {@link PluginTied}, remember to register it using {@link OMCPlugin#registerPluginTied(PluginTied)}
 * 
 * 
 * @author stormer3428
 *
 */
public class ConfigHolder implements PluginTied{

	private File configFile;
	private String resourcePath;
	private YamlConfiguration config;

	private static final ArrayList<ConfigHolder> HOLDERS = new ArrayList<>();

	public static void reloadAllConfigs() {
		for(ConfigHolder holder : HOLDERS) holder.reloadConfig();
	}

	public ConfigHolder(File configFile) {
		this(configFile, configFile.getName());
	}

	public ConfigHolder(File configFile, String resourceName) {
		this.configFile = configFile;
		this.resourcePath = resourceName;
		reloadConfig();
		HOLDERS.add(this);
	}

	/**
	 * @implNote
	 * Calls {@link #onPluginReload()}
	 */
	@Override
	public void onPluginEnable() {
		onPluginReload();
	}

	@Override
	public void onPluginDisable() {}

	/**
	 * @implNote
	 * Calls {@link #reloadConfigs()}
	 */
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

	public void saveConfig() {
		try {
			getConfig().save(getConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Checks for the existence of the config file in the data folder, if it does not exists it will be imported from the jar file.
	 * Then loads the {@link YamlConfiguration} from {@link #getConfigFile()}
	 */
	public void reloadConfig() {
		if(!configFile.exists())  {
			OMCLogger.systemError(OMCLang.ERROR_MISSING_CONFIG_FILE.toString().replace("<%FILE>", configFile.getName()));
			createConfigFile(false);
		}
		config = YamlConfiguration.loadConfiguration(configFile);
	}

	public void createConfigFile(boolean force) {
		File dataFolder = OMCPlugin.i.getDataFolder();

		InputStream in = OMCPlugin.i.getResource(resourcePath);
		//        if (in == null) {
		//            throw new IllegalArgumentException("Failed to find config '" + resourcePath + "' inside of the jar file");
		//        }

		File outFile = new File(dataFolder, resourcePath);
		if(!outFile.exists())
			try {
				outFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		int lastIndex = resourcePath.lastIndexOf('/');
		File outDir = new File(dataFolder, resourcePath.substring(0, lastIndex >= 0 ? lastIndex : 0));

		if (!outDir.exists()) {
			outDir.mkdirs();
		}

		if(in != null) try {

			if (!outFile.exists() || force) {
				OutputStream out = new FileOutputStream(outFile);
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				out.close();
				in.close();
			} else {
				OMCLogger.systemError("Could not save " + outFile.getName() + " to " + outFile + " because " + outFile.getName() + " already exists.");
			}
		} catch (IOException ex) {
			OMCLogger.systemError("Could not save " + outFile.getName() + " to " + outFile);
		}
	}

	public String getString(String path) {
		if(config == null) {
			OMCLogger.systemError(OMCLang.ERROR_MISSING_CONFIG.toString().replace("<%FILE>", configFile.getName()).replace("<%PATH>", path));
			return "null";
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
		return getStringList(path, true);
	}

	public List<String> getStringList(String path, boolean ignoreIfNull) {
		List<String> list = new ArrayList<>();
		if(config == null) {
			OMCLogger.systemError(OMCLang.ERROR_MISSING_CONFIG.toString());
			return list;
		}
		if(!config.isSet(path)) {
			if(!ignoreIfNull) OMCLogger.systemError(OMCLang.ERROR_CONFIG_MISSING_PATH.toString().replace("<%PATH>", config.getCurrentPath().isBlank() ? path : String.join(".", config.getCurrentPath(), path)).replace("<%CONFIG>", getConfigFile().getName()));
			return list;
		}
		list.addAll(config.getStringList(path));
		for(int i = 0; i < list.size(); i++) list.set(i, OMCUtil.translateChatColor(list.get(i)));
		return list;
	}

	public List<Integer> getIntegerList(String path) {
		return getIntegerList(path, false);
	}

	public List<Integer> getIntegerList(String path, boolean ignoreIfNull) {
		List<Integer> list = new ArrayList<>();
		if(config == null) {
			OMCLogger.systemError(OMCLang.ERROR_MISSING_CONFIG.toString());
			return list;
		}
		if(!config.isSet(path)) {
			if(!ignoreIfNull) OMCLogger.systemError(OMCLang.ERROR_CONFIG_MISSING_PATH.toString().replace("<%PATH>", config.getCurrentPath().isBlank() ? path : String.join(".", config.getCurrentPath(), path)).replace("<%CONFIG>", getConfigFile().getName()));
			return list;
		}
		list.addAll(config.getIntegerList(path));
		return list;
	}

	public List<Boolean> getBooleanList(String path) {
		List<Boolean> list = new ArrayList<>();
		if(config == null) {
			OMCLogger.systemError(OMCLang.ERROR_MISSING_CONFIG.toString());
			return list;
		}
		if(!config.isSet(path)) {
			OMCLogger.systemError(OMCLang.ERROR_CONFIG_MISSING_PATH.toString().replace("<%PATH>", config.getCurrentPath().isBlank() ? path : String.join(".", config.getCurrentPath(), path)).replace("<%CONFIG>", getConfigFile().getName()));
			return list;
		}
		list.addAll(config.getBooleanList(path));
		return list;
	}

	public List<Double> getDoubleList(String path) {
		List<Double> list = new ArrayList<>();
		if(config == null) {
			OMCLogger.systemError(OMCLang.ERROR_MISSING_CONFIG.toString());
			return list;
		}
		if(!config.isSet(path)) {
			OMCLogger.systemError(OMCLang.ERROR_CONFIG_MISSING_PATH.toString().replace("<%PATH>", config.getCurrentPath().isBlank() ? path : String.join(".", config.getCurrentPath(), path)).replace("<%CONFIG>", getConfigFile().getName()));
			return list;
		}
		list.addAll(config.getDoubleList(path));
		return list;
	}

	public List<Float> getFloatList(String path) {
		List<Float> list = new ArrayList<>();
		if(config == null) {
			OMCLogger.systemError(OMCLang.ERROR_MISSING_CONFIG.toString());
			return list;
		}
		if(!config.isSet(path)) {
			OMCLogger.systemError(OMCLang.ERROR_CONFIG_MISSING_PATH.toString().replace("<%PATH>", config.getCurrentPath().isBlank() ? path : String.join(".", config.getCurrentPath(), path)).replace("<%CONFIG>", getConfigFile().getName()));
			return list;
		}
		list.addAll(config.getFloatList(path));
		return list;
	}

	public List<Long> getLongList(String path) {
		List<Long> list = new ArrayList<>();
		if(config == null) {
			OMCLogger.systemError(OMCLang.ERROR_MISSING_CONFIG.toString());
			return list;
		}
		if(!config.isSet(path)) {
			OMCLogger.systemError(OMCLang.ERROR_CONFIG_MISSING_PATH.toString().replace("<%PATH>", config.getCurrentPath().isBlank() ? path : String.join(".", config.getCurrentPath(), path)).replace("<%CONFIG>", getConfigFile().getName()));
			return list;
		}
		list.addAll(config.getLongList(path));
		return list;
	}

	public List<Byte> getByteList(String path) {
		List<Byte> list = new ArrayList<>();
		if(config == null) {
			OMCLogger.systemError(OMCLang.ERROR_MISSING_CONFIG.toString());
			return list;
		}
		if(!config.isSet(path)) {
			OMCLogger.systemError(OMCLang.ERROR_CONFIG_MISSING_PATH.toString().replace("<%PATH>", config.getCurrentPath().isBlank() ? path : String.join(".", config.getCurrentPath(), path)).replace("<%CONFIG>", getConfigFile().getName()));
			return list;
		}
		list.addAll(config.getByteList(path));
		return list;
	}

	public List<Character> getCharacterList(String path) {
		List<Character> list = new ArrayList<>();
		if(config == null) {
			OMCLogger.systemError(OMCLang.ERROR_MISSING_CONFIG.toString());
			return list;
		}
		if(!config.isSet(path)) {
			OMCLogger.systemError(OMCLang.ERROR_CONFIG_MISSING_PATH.toString().replace("<%PATH>", config.getCurrentPath().isBlank() ? path : String.join(".", config.getCurrentPath(), path)).replace("<%CONFIG>", getConfigFile().getName()));
			return list;
		}
		list.addAll(config.getCharacterList(path));
		return list;
	}

	public List<Short> getShortList(String path) {
		List<Short> list = new ArrayList<>();
		if(config == null) {
			OMCLogger.systemError(OMCLang.ERROR_MISSING_CONFIG.toString());
			return list;
		}
		if(!config.isSet(path)) {
			OMCLogger.systemError(OMCLang.ERROR_CONFIG_MISSING_PATH.toString().replace("<%PATH>", config.getCurrentPath().isBlank() ? path : String.join(".", config.getCurrentPath(), path)).replace("<%CONFIG>", getConfigFile().getName()));
			return list;
		}
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
