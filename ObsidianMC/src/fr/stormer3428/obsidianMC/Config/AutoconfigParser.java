package fr.stormer3428.obsidianMC.Config;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

import org.bukkit.configuration.file.YamlConfiguration;

import fr.stormer3428.obsidianMC.OMCPlugin;
import fr.stormer3428.obsidianMC.Util.OMCLogger;

public class AutoconfigParser {

	final ArrayList<Class<?>> annotatedClasses = new ArrayList<>();

	public void updateValues() {
		OMCLogger.systemNormal("Updating values in classes");
		for(Class<?> clazz : annotatedClasses) {
			OMCLogger.systemNormal("Updating values of class " + clazz.getName());
			AutoConfig autoConfig = clazz.getDeclaredAnnotation(AutoConfig.class);
			if(autoConfig == null) {
				OMCLogger.systemError("Error, missing @AutoConfig annotation");
				continue;
			}

			String configName = autoConfig.config();
			File file = new File(OMCPlugin.i.getDataFolder(), configName);
			if(!file.exists()) {
				OMCLogger.systemNormal("could not find file, creating...");
				file.getParentFile().mkdirs();
				try {
					file.createNewFile();
					OMCLogger.systemNormal("Success!");
				} catch (IOException e) {
					e.printStackTrace();
					OMCLogger.systemError("failed...");
					continue;
				}
			}
			YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
			for(Field field : clazz.getDeclaredFields()) {

				field.setAccessible(true);
				StringConfigValue stringConfigValue = field.getAnnotation(StringConfigValue.class);
				IntConfigValue intConfigValue = field.getAnnotation(IntConfigValue.class);
				DoubleConfigValue doubleConfigValue = field.getAnnotation(DoubleConfigValue.class);
				BooleanConfigValue booleanConfigValue = field.getAnnotation(BooleanConfigValue.class);
				if(stringConfigValue != null) {
					OMCLogger.systemNormal("Updating field " + field.getName());
					String defaultValue = stringConfigValue.defaultValue();
					String path = stringConfigValue.path();
					if(!config.contains(path)) config.set(path, defaultValue);
					String value = config.getString(path);
					config.set(path, value);
					try {
						field.set(null, config.get(path));
						OMCLogger.debug("Successfully updated string value!");
					} catch (IllegalArgumentException | IllegalAccessException e) {
						OMCLogger.systemError("Error, annotated field " + field.getName() + " in class " + clazz.getName() + " is either not static or of wrong type (expected String)");
						e.printStackTrace();
						continue;
					}
				}else if(intConfigValue != null) {
					OMCLogger.systemNormal("Updating field " + field.getName());
					int defaultValue = intConfigValue.defaultValue();
					String path = intConfigValue.path();
					if(!config.contains(path)) config.set(path, defaultValue);
					int value = config.getInt(path);
					config.set(path, value);
					try {
						field.set(null, value);
						OMCLogger.debug("Successfully updated integer value!");
					} catch (IllegalArgumentException | IllegalAccessException e) {
						OMCLogger.systemError("Error, annotated field " + field.getName() + " in class " + clazz.getName() + " is either not static or of wrong type (expected Integer)");
						e.printStackTrace();
						continue;
					}
				}else if(doubleConfigValue != null) {
					OMCLogger.systemNormal("Updating field " + field.getName());
					double defaultValue = doubleConfigValue.defaultValue();
					String path = doubleConfigValue.path();
					if(!config.contains(path)) config.set(path, defaultValue);
					double value = config.getDouble(path);
					config.set(path, value);
					try {
						field.set(null, value);
						OMCLogger.debug("Successfully updated double value!");
					} catch (IllegalArgumentException | IllegalAccessException e) {
						OMCLogger.systemError("Error, annotated field " + field.getName() + " in class " + clazz.getName() + " is either not static or of wrong type (expected Double)");
						e.printStackTrace();
						continue;
					}
				}else if(booleanConfigValue != null) {
					OMCLogger.systemNormal("Updating field " + field.getName());
					boolean defaultValue = booleanConfigValue.defaultValue();
					String path = booleanConfigValue.path();
					if(!config.contains(path)) config.set(path, defaultValue);
					boolean value = config.getBoolean(path);
					config.set(path, value);
					try {
						field.set(null, value);
						OMCLogger.debug("Successfully updated boolean value!");
					} catch (IllegalArgumentException | IllegalAccessException e) {
						OMCLogger.systemError("Error, annotated field " + field.getName() + " in class " + clazz.getName() + " is either not static or of wrong type (expected Boolean)");
						e.printStackTrace();
						continue;
					}
				}else {
					OMCLogger.debug("No annotation");
				}
			}
			try {
				config.save(file);
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
		}
	}

	public void registerAutoConfigClass(Class<?> clazz) {
		annotatedClasses.add(clazz);
	}



}
