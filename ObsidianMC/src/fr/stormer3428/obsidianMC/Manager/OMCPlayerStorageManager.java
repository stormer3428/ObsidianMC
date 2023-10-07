package fr.stormer3428.obsidianMC.Manager;

import java.io.File;
import java.util.UUID;

import org.bukkit.entity.Player;

import fr.stormer3428.obsidianMC.Config.ConfigHolder;
import fr.stormer3428.obsidianMC.Config.OMCDataType;

public class OMCPlayerStorageManager extends ConfigHolder{

	public OMCPlayerStorageManager(File configFile) {
		super(configFile);
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