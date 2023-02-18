package fr.stormer3428.obsidianMC;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public abstract class OMCCommandManager implements CommandExecutor, TabCompleter, PluginTied{

	public final ArrayList<OMCCommand> COMMANDS = new ArrayList<>();

	@Override
	public void onPluginEnable() {
		if(!COMMANDS.isEmpty()) return; //Already registered commands
		registerVariables();
		registerCommands();
		registerExecutorsAndTabCompelters();
	}

	@Override	
	public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
		for(OMCCommand command : this.COMMANDS) if(command.matches(cmd.getName(), args)) return command.execute(sender, args);
		StringBuilder architecture = new StringBuilder(cmd.getName());
		for(String arg : args) architecture.append(arg);
		return OMCLogger.error(sender, OMCLang.COMMAND_SYNTAX_ERROR.toString().replace("<%SYNTAX>", architecture.toString().trim()));
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		ArrayList<String> list = new ArrayList<>();
		for(OMCCommand command : this.COMMANDS) list.addAll(command.autocomplete(sender, cmd.getName(), args));
		list.sort(Comparator.naturalOrder());
		return list;
	}

	private void registerExecutorsAndTabCompelters() {
		for(OMCCommand cmd : this.COMMANDS)for(String alias : cmd.architecture.get(0)) {
			OMCPlugin.i.getCommand(alias).setExecutor(OMCCommandManager.this);
			OMCPlugin.i.getCommand(alias).setTabCompleter(OMCCommandManager.this);
			OMCLogger.systemNormal("Register executor and completer for " + alias + ". Permission : (" + cmd.getPermissionString() + ") \t\tSignature : (" + cmd.rawArchitecture + ")");
		}
	}

	@Override
	public void onPluginDisable() {}
	
	protected abstract void registerVariables();
	protected abstract void registerCommands();

}
