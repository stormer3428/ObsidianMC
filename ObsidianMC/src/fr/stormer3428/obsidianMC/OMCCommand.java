package fr.stormer3428.obsidianMC;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class OMCCommand {

	public static final String ALIAS_SEPARATOR = "%%%";
	public static final ArrayList<OMCVariable> VARIABLES = new ArrayList<>();

	static {
		VARIABLES.add(new OMCVariable("%V%") {
			@Override
			protected ArrayList<String> complete(String incomplete) {
				return new ArrayList<>();
			}
		});

		VARIABLES.add(new OMCVariable("%P%") {
			@Override
			protected ArrayList<String> complete(String incomplete) {
				final ArrayList<String> list = new ArrayList<>();
				final String lower = incomplete.toLowerCase();
				for(Player p : Bukkit.getOnlinePlayers()) if(p.getName().toLowerCase().startsWith(lower)) list.add(p.getName());
				return list;
			}
		});

		VARIABLES.add(new OMCVariable("%B%") {
			@Override
			protected ArrayList<String> complete(String incomplete) {
				final ArrayList<String> list = new ArrayList<>();
				final String lower = incomplete.toLowerCase();
				if("true".startsWith(lower)) list.add("True");
				if("false".startsWith(lower)) list.add("False");
				return list;
			}
		});
	}

	public static void registerVariable(OMCVariable v) {
		VARIABLES.add(v);
	}

	private final String baseCommand;
	final String architecture;

	public OMCCommand(String architecture) {
		this.architecture = architecture;
		this.baseCommand = architecture.split(" ")[0].split(ALIAS_SEPARATOR)[0];
	}

	public boolean execute(CommandSender sender, String commandArchitecture) {
		String[] arch = architecture.split(" ");
		String[] fcmd = commandArchitecture.split(" ");
		ArrayList<String> variables = new ArrayList<>();
		for(int i = 0; i < arch.length; i++) for(OMCVariable v : VARIABLES) if(v.matchesArchitecture(arch[i])) variables.add(fcmd[i]);
		return execute(sender, variables);
	}
	public abstract boolean execute(CommandSender sender, ArrayList<String> vars);

	public String getBaseCommand() {
		return baseCommand;
	}

	public String getPermissionString() {
		String[] arch = architecture.split(" ");
		String permission = arch[0].split(ALIAS_SEPARATOR)[0];
		for(int i = 1; i < arch.length; i++) permission += "." + arch[i].split(ALIAS_SEPARATOR)[0];
		return permission;
	}

	public boolean architectureMatches(String fullCommand) {
		OMCLogger.debug("Checking if architecture matches \n" + architecture + "\n" + fullCommand);
		String[] arch = architecture.split(" ");
		String[] fcmd = fullCommand.split(" ");
		if(arch.length != fcmd.length) {
			OMCLogger.debug("Lenght mismatch");
			return false;
		}
		OMCLogger.debug("Lenght match");
		mainloop:
			for(int i = 0; i < arch.length; i++) {
				OMCLogger.debug("\n" + arch[i] + "\n" + fcmd[i]);
				for(OMCVariable v : VARIABLES) if(v.matchesArchitecture(arch[i])) continue mainloop;
				OMCLogger.debug("Alias matcher : ");
				for(String a : arch[i].split(ALIAS_SEPARATOR)) if(fcmd[i].equalsIgnoreCase(a)) {
					OMCLogger.debug("passed : " + a);
					continue mainloop;
				}

				OMCLogger.debug("Found no alias match");
				return false;
			}
		OMCLogger.debug("Architecture matches");
		return true;
	}

	public ArrayList<String> autocompletionMatches(CommandSender sender,  String[] fcmd) {
		ArrayList<String> list = new ArrayList<>();
		String[] arch = architecture.split(" ");
		if(fcmd.length > arch.length) return list;
		OMCLogger.debug("Checking if autocompletion architecture matches \n" + architecture);
		for(int i = 0; i < fcmd.length; i++) OMCLogger.debug(i + " " + fcmd[i]);

		mainLoop:
			for(int i = 1; i < fcmd.length; i++) {
				for(OMCVariable v : VARIABLES) if(v.matchesArchitecture(arch[i-1])) continue mainLoop;
				for(String a : arch[i].split(ALIAS_SEPARATOR)) if(fcmd[i].equalsIgnoreCase(a)) continue mainLoop;
				if(!fcmd[i].isBlank() && !fcmd[i].isEmpty()) {
					for(OMCVariable v : VARIABLES) if(v.matchesArchitecture(arch[i])) continue mainLoop;
					for(String a : arch[i].split(ALIAS_SEPARATOR)) if(a.toLowerCase().startsWith(fcmd[i].toLowerCase())) continue mainLoop;
					OMCLogger.debug("Returned early ("+ fcmd[i] +")");
					return list;
				}
			}
		//previous args matches
		OMCLogger.debug("Architecture matching up to this point, adding entries...");

		String a = arch[fcmd.length - 1];
		String f = fcmd[fcmd.length - 1];

		if((f.isEmpty() || f.isBlank()) || a.toLowerCase().startsWith(f.toLowerCase())) {
			list.add(a.split(ALIAS_SEPARATOR)[0]);
			OMCLogger.debug("Entry added : " + a.split(ALIAS_SEPARATOR)[0]);
			return list;
		}

		return list;
	}

	public String getDescription() {
		return "No description";
	}
}