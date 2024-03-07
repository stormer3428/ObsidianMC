package fr.stormer3428.obsidianMC.Config;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

public abstract class OMCDataType<T>{

	public static OMCSerializableDataType<Boolean> BOOLEAN(String name){
		return new OMCSerializableDataType<>(name) {

			@Override
			public String toString(Boolean value) {
				return value.toString();
			}

			@Override
			public Boolean fromString(String serialized) {
				return Boolean.valueOf(serialized);
			}

			@Override
			public Boolean defaultVal() {
				return false;
			}
		};
	}

	public static OMCDataType<Integer> INTEGER(String name, int defaultVal){ 
		return new OMCSerializableDataType<>(name) {

			@Override
			public String toString(Integer value) {
				return value.toString();
			}

			@Override
			public Integer fromString(String serialized) {
				try {
					return Integer.parseInt(serialized);
				}catch (Exception e) {
					e.printStackTrace();
				}
				return defaultVal();
			}

			@Override
			public Integer defaultVal() {
				return defaultVal;
			}
		};
	}

	public static OMCDataType<Double> DOUBLE(String name, double defaultVal){ 
		return new OMCSerializableDataType<>(name) {

			@Override
			public String toString(Double value) {
				return value.toString();
			}

			@Override
			public Double fromString(String serialized) {
				try {
					return Double.parseDouble(serialized);
				}catch (Exception e) {
					e.printStackTrace();
				}
				return defaultVal();
			}

			@Override
			public Double defaultVal() {
				return defaultVal;
			}
		};
	}

	public static OMCDataType<List<String>> STRING_LIST(String name){ 
		return new OMCDataType<>(name) {
			
			@Override
			public List<String> get(ConfigurationSection section, String path) {
				return section.getStringList(path);
			}

			@Override
			public List<String> defaultVal() {
				return new ArrayList<String>();
			}
		};
	} 

	public void save(ConfigurationSection section, String path, T value) {
		section.set(path, value);
	}

	public abstract T get(ConfigurationSection section, String path);
	public abstract T defaultVal();
	
	private final String key;

	public OMCDataType(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}
}
