package fr.stormer3428.obsidianMC.Config;

public abstract class OMCDataType<T>{

	public static OMCDataType<Boolean> BOOLEAN(String name){
		return new OMCDataType<>(name) {

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

	public static OMCDataType<Integer> INTEGER(String name){ 
		return new OMCDataType<>(name) {

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
				return 0;
			}
		};
	}

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
