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
			protected ArrayList<String> complete(CommandSender sender, String incomplete) {
				return new ArrayList<>();
			}
		});

		VARIABLES.add(new OMCVariable("%P%") {
			@Override
			protected ArrayList<String> complete(CommandSender sender, String incomplete) {
				final ArrayList<String> list = new ArrayList<>();
				final String lower = incomplete.toLowerCase();
				for(Player p : Bukkit.getOnlinePlayers()) if(p.getName().toLowerCase().startsWith(lower)) list.add(p.getName());
				return list;
			}
		});

		VARIABLES.add(new OMCVariable("%B%") {
			@Override
			protected ArrayList<String> complete(CommandSender sender, String incomplete) {
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

	final ArrayList<String[]> architecture = new ArrayList<>();
	final String rawArchitecture;

	public OMCCommand(String givenArchitecture) {
		for(String arg : givenArchitecture.split(" ")) architecture.add(arg.split(ALIAS_SEPARATOR));
		rawArchitecture = givenArchitecture;
	}

	public boolean execute(CommandSender sender, String[] args) {
		if(!sender.hasPermission(getPermissionString())) return OMCLogger.error(sender, OMCLang.ERROR_GENERIC_NOPERMISSION.toString().replace("<%PERMISSION>", getPermissionString()));
		ArrayList<String> variables = new ArrayList<>();
		int i = 0;
		for(String arg : args) {
			i++;
			for(OMCVariable variable : VARIABLES) if(variable.matches(architecture.get(i)[0])) {
				variables.add(arg);
				break;
			}
		}		
		return execute(sender, variables);
	}

	public abstract boolean execute(CommandSender sender, ArrayList<String> vars);

	public String getPermissionString() {
		StringBuilder permission = new StringBuilder();
		for(String[] stage : architecture) permission.append(String.join(ALIAS_SEPARATOR, stage) + " ");
		return permission.toString().trim().replace(" ", ".");
	}


	public boolean matches(String command, String[] args) {
		if(architecture.size() != args.length + 1) return false;
		ArrayList<String> fullArgs = new ArrayList<>(args.length + 1);
		fullArgs.add(command);
		for(String arg : args) fullArgs.add(arg);

		int stageIndex = -1;
		argLoop:for(String arg : fullArgs) {
			stageIndex++;
			String[] stage = architecture.get(stageIndex);
			if(stage.length == 1) for(OMCVariable variable : VARIABLES) if(variable.matches(stage[0])) continue argLoop; //if variable, we accept anything so we also match anything
			//we dont expect a variable
			for(String stageElement : stage) if(stageElement.equalsIgnoreCase(arg)) continue argLoop; //it matches one of the aliases
			return false;
		}
		return true;
	}

	public ArrayList<String> autocomplete(CommandSender sender, String command, String[] args) {
		ArrayList<String> list = new ArrayList<>();

		if(architecture.size() < args.length + 1) {
			OMCLogger.debug(architecture.get(0)[0] + " lenght mismatched, expected " + architecture.size() + " but got " + (args.length + 1));
			return list;
		}
		
		OMCLogger.debug(architecture.get(0)[0] + " lenght matched, expected " + architecture.size() + " got " + (args.length + 1));
		
		ArrayList<String> fullArgs = new ArrayList<>(args.length + 1);
		fullArgs.add(command);
		for(String arg : args) fullArgs.add(arg);

		int stageIndex = -1;
		argLoop:for(String arg : fullArgs) {
			stageIndex++;
			boolean isLastStage = stageIndex == fullArgs.size() - 1;
			String[] stage = architecture.get(stageIndex);
			if(stage.length == 1) for(OMCVariable variable : VARIABLES) if(variable.matches(stage[0])) {
				if(!isLastStage) continue argLoop; //if variable, we accept anything so we also match anything
				ArrayList<String> completed = variable.complete(sender, arg);
				OMCLogger.debug("Matched variable " + variable.signature);
				for(String s : completed) OMCLogger.debug("Variable Entry added : " + s);
				list.addAll(completed);
				OMCLogger.debug("returned variable completed list (" + completed.size() +")");
				return list;
			}
			OMCLogger.debug("No variable matched, checking for architecture");
			//we dont expect a variable
			final String lowarArg = arg.toLowerCase();
			for(String stageElement : stage) {
				final String lowerStageElement = stageElement.toLowerCase();
				if(!isLastStage) {
					if(lowerStageElement.equals(lowarArg)) continue argLoop; //it matches one of the aliases
				}else if(lowerStageElement.startsWith(lowarArg)) {
					list.add(stage[0]);
					OMCLogger.debug("Regular entry added : " + stage[0]);
					return list;
				}
			}
			OMCLogger.debug("Architecture match failed, returning");
			return list;
		}
		OMCLogger.debug("Architecture matches exactly with no ther outcome, returning");
		return list;
	}


	public String getDescription() {
		return "No description";
	}
}