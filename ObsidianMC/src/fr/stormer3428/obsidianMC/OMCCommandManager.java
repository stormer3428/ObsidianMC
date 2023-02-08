package fr.stormer3428.obsidianMC;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public abstract class OMCCommandManager implements CommandExecutor, TabCompleter{

	public final ArrayList<OMCCommand> COMMANDS = new ArrayList<>();

	public OMCCommandManager() {
		registerVariables();
		registerCommands();
		for(OMCCommand cmd : this.COMMANDS) {
			OMCPlugin.i.getCommand(cmd.getBaseCommand()).setExecutor(this);
			OMCPlugin.i.getCommand(cmd.getBaseCommand()).setTabCompleter(this);
			OMCLogger.systemNormal("Command registered. Permission : (" + cmd.getPermissionString() + ") \t\tSignature : (" + cmd.architecture + ")");
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] argsArr) {
		ArrayList<String> args = new ArrayList<>(Arrays.asList(argsArr));
		String commandArchitecture = cmd.getName();
		for(String s : args) commandArchitecture += " " + s;
		for(OMCCommand command : this.COMMANDS) if(command.architectureMatches(commandArchitecture)) return command.execute(sender, commandArchitecture);
		return OMCLogger.error(sender, OMCLang.COMMAND_SYNTAX_ERROR.toString().replace("<%SYNTAX>", commandArchitecture));
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] argsArr) {
		OMCLogger.debug("<============================================>");
		String[] args = new String[argsArr.length + 1];
		for(int i = 0; i < argsArr.length; i++) args[i+1] = argsArr[i];
		args[0] = cmd.getName();
		ArrayList<String> list = new ArrayList<>();
		for(OMCCommand command : this.COMMANDS) list.addAll(command.autocompletionMatches(sender, args));
		list.sort(Comparator.naturalOrder());
		return list;
	}

	protected abstract void registerVariables();
	protected abstract void registerCommands();

}
