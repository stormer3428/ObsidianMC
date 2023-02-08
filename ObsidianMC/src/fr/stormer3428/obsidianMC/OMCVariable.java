package fr.stormer3428.obsidianMC;

import java.util.ArrayList;

public abstract class OMCVariable {

	private final String signature;
	
	public boolean matchesArchitecture(String s) {
		return signature.equals(s);
	}
	
	public OMCVariable(String signature) {
		this.signature = signature;
	}
	
	protected abstract ArrayList<String> complete(String incomplete);
	
}
