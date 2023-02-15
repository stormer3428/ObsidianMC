package fr.stormer3428.obsidianMC;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.defaults.BukkitCommand;

public abstract class OMCCommandManager implements CommandExecutor, TabCompleter, PluginTied{

	public final ArrayList<OMCCommand> COMMANDS = new ArrayList<>();
	private final HashMap<String, BukkitCommand> COMMAND_MAP = new HashMap<>();

	@Override
	public void onPluginEnable() {
		if(!COMMANDS.isEmpty()) return;
		registerVariables();
		registerCommands();
		for(OMCCommand cmd : this.COMMANDS) {
			ArrayList<String> aliases = new ArrayList<>();
			for(String a : cmd.architecture.split(" ")) aliases.add(a);
			if(!COMMAND_MAP.containsKey(cmd.getBaseCommand())) {
				BukkitCommand bukkitCommand = new BukkitCommand(cmd.getBaseCommand(), cmd.getDescription(), cmd.architecture, aliases) {

					@Override
					public boolean execute(CommandSender sender, String alias, String[] args) {
						return onCommand(sender, this, alias, args);
					}
					
					@Override
					public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
						return onTabComplete(sender, this, alias, args);
					}
					
					//TODO Autocompletion for variables autocompletes token instead of list
				};
				bukkitCommand.setPermission(cmd.getPermissionString());
				try {
					final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
					bukkitCommandMap.setAccessible(true);
					CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
					commandMap.register(cmd.getBaseCommand(), bukkitCommand);
					COMMAND_MAP.put(cmd.getBaseCommand(), bukkitCommand);
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
			OMCLogger.systemNormal("Command registered. Permission : (" + cmd.getPermissionString() + ") \t\tSignature : (" + cmd.architecture + ")");
		}
	}

	@Override
	public void onPluginDisable() {}
	
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
