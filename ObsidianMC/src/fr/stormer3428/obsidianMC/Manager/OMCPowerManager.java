package fr.stormer3428.obsidianMC.Manager;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import fr.stormer3428.obsidianMC.PluginTied;
import fr.stormer3428.obsidianMC.Command.OMCCommand;
import fr.stormer3428.obsidianMC.Command.OMCVariable;
import fr.stormer3428.obsidianMC.Power.OMCPower;
import fr.stormer3428.obsidianMC.Util.OMCLang;
import fr.stormer3428.obsidianMC.Util.OMCLogger;

public abstract class OMCPowerManager implements PluginTied{

	private final ArrayList<OMCPower> registeredPowers = new ArrayList<>();

	/**
	 * Creates a {@link OMCVariable} with the given signature that completes for registered {@link OMCPower}
	 * 
	 * @param variableSignature
	 * the signature of the variable
	 * @return the variable
	 * @see OMCCommand
	 * @see #registerPower(OMCPower)
	 */
	public OMCVariable getPowerVariable(String variableSignature) {
		return new OMCVariable(variableSignature) {

			@Override
			protected ArrayList<String> complete(CommandSender sender, String incomplete) {
				String lowercase = incomplete.toLowerCase();
				ArrayList<String> list = new ArrayList<>();
				for(OMCPower power : registeredPowers) {
					String uppercase = power.getRegistryName();
					String name = uppercase.toLowerCase();
					if(name.startsWith(lowercase) || name.contains(lowercase)) list.add(uppercase);
				}
				return list;
			}
		};
	}
	
	/**
	 * Should return the {@link FileConfiguration} used by this power manager
	 * @return the config of this manager
	 * @see #getPowerConfigFile()
	 */
	public abstract FileConfiguration getPowerConfig();
	/**
	 * Should return the {@link File} {@link #getPowerConfig()} uses
	 * @return the {@link File} used to store the config of this manager
	 */
	public abstract File getPowerConfigFile();


	/**
	 * 
	 * Registers an {@link OMCPower} to the {@link OMCPowerManager}. A power registered to the powerManager will show up in the {@link #getPowerVariable(String)} completion list
	 * 
	 * @param power 
	 * the power to register
	 * @see #getPowerVariable(String)
	 */
	public void registerPower(OMCPower power) {
		if(power == null) {
			OMCLogger.systemError(OMCLang.ERROR_POWER_MANAGER_REGISTER_NULL.toString());
			return;
		}
		if(power.getRegistryName() == null || power.getRegistryName().isBlank()) {
			OMCLogger.systemError(OMCLang.ERROR_POWER_MANAGER_REGISTER_NULL_NAME.toString());
			return;
		}
		registeredPowers.add(power);
	}

	/**
	 * 
	 * @return a copy of the registered power list
	 * @see #registerPower(OMCPower)
	 */
	public ArrayList<OMCPower> getPowers() {
		return new ArrayList<>(registeredPowers);
	}
	

	/**
	 * Will return the corresponding {@link OMCPower} registered in this manager, or null if it is unrecognized
	 * @implNote
	 * This method uses {@link String#equalsIgnoreCase(String)} to find the correlating {@link OMCPower}
	 * 
	 * @param name
	 * The power name to search for
	 * @return The corresponding {@link OMCPower}
	 */
	public OMCPower fromName(String name) {
		for(OMCPower omcPower : registeredPowers) if(omcPower.getRegistryName().equalsIgnoreCase(name)) return omcPower;
		return null;
	}
	
}
