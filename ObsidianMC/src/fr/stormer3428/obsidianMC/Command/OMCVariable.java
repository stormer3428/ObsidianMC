package fr.stormer3428.obsidianMC.Command;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;

import fr.stormer3428.obsidianMC.Util.OMCLogger;

public abstract class OMCVariable {

	protected final String signature;
	
	public boolean matches(String s) {
		return signature.equalsIgnoreCase(s);
	}
	
	public OMCVariable(String signature) {
		OMCLogger.debug("creating variable : " + signature);
		this.signature = signature;
	}
	
	protected abstract ArrayList<String> complete(CommandSender sender, String incomplete);
	
	@Override
	public String toString() {
		return "[OMCVariable : " + signature + "]";
	}
}
