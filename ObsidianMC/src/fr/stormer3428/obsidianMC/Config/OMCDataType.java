package fr.stormer3428.obsidianMC.Config;

public abstract class OMCDataType<T>{
	
	public abstract String toString(T value);
	public abstract T fromString(String serialized);
	public abstract T defaultVal();
	
	private final String key;
	
	public OMCDataType(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}
}
