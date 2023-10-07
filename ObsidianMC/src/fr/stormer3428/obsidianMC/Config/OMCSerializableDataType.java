package fr.stormer3428.obsidianMC.Config;

import org.bukkit.configuration.ConfigurationSection;

public abstract class OMCSerializableDataType<T> extends OMCDataType<T>{

	public OMCSerializableDataType(String key) {
		super(key);
	}

	public void save(ConfigurationSection section, String path, T value) {
		section.set(path, toString(value));
	}
	
	public abstract String toString(T value);
	public abstract T fromString(String serialized);

	@Override
	public T get(ConfigurationSection section, String path) {
		return fromString(section.getString(path));
	}

	
}
