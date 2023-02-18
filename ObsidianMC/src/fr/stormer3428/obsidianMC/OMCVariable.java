package fr.stormer3428.obsidianMC;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;

public abstract class OMCVariable {

	protected final String signature;
	
	public boolean matches(String s) {
		return signature.equalsIgnoreCase(s);
	}
	
	public OMCVariable(String signature) {
		this.signature = signature;
	}
	
	protected abstract ArrayList<String> complete(CommandSender sender, String incomplete);
	
}
