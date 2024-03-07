package fr.stormer3428.obsidianMC.Data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import fr.stormer3428.obsidianMC.OMCLang;
import fr.stormer3428.obsidianMC.OMCLogger;
import fr.stormer3428.obsidianMC.OMCPlugin;
import fr.stormer3428.obsidianMC.PluginTied;

public class OMCPlayerStorageManager implements PluginTied{

	private File configFile;
	private String resourcePath;
	private YamlConfiguration config;
	
	public OMCPlayerStorageManager(File configFile) {
		this(configFile, configFile.getName());
	}

	public OMCPlayerStorageManager(File configFile, String resourceName) {
		this.configFile = configFile;
		this.resourcePath = resourceName;
		reloadConfig();
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
		OMCLogger.systemNormal("creating config file " + resourcePath);
		File dataFolder = OMCPlugin.i.getDataFolder();

		InputStream in = OMCPlugin.i.getResource(resourcePath);

		File outFile = new File(dataFolder, resourcePath);
		int lastIndex = resourcePath.lastIndexOf('/');
		File outDir = new File(dataFolder, resourcePath.substring(0, lastIndex >= 0 ? lastIndex : 0));

		if (!outDir.exists()) {
			outDir.mkdirs();
		}

		if(in != null) try {
			if (!outFile.exists() || force) {
				OMCLogger.systemNormal("streaming data from jar");
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
		} else if(!outFile.exists()) try {
			outDir.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public <T> void set(Player p, OMCDataType<T> type, T value) {
		set(p.getUniqueId(), type, value);
	}
	
	public <T> void set(UUID uuid, OMCDataType<T> type, T value) {
		String path = uuid.toString() + "." + type.getKey();
		type.save(getConfig(), path, value);
		saveConfig();
	}
	
	public <T> T get(UUID uuid, OMCDataType<T> type) {
		String path = uuid.toString() + "." + type.getKey();
		if(!getConfig().contains(path)) set(uuid, type, type.defaultVal());
		return type.get(getConfig(), path);
	}
}