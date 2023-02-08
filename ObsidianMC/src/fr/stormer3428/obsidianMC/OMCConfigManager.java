package fr.stormer3428.obsidianMC;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class OMCConfigManager {

	private final File file;
	private final FileConfiguration config;
	
	public OMCConfigManager(String fileName) {
		this(new File(fileName));
	}
	
	public OMCConfigManager(File configuration) {
		file = configuration;
		config = YamlConfiguration.loadConfiguration(file);
	}
	
	public void loadConfig() {
		config.options().copyDefaults(true);
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
//	
//	public String get(String key) {
//		
//	}
	
}
