package fr.stormer3428.obsidianMC.Manager;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.stormer3428.obsidianMC.OMCPlugin;
import fr.stormer3428.obsidianMC.Util.OMCUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class OMCLogger {
	
	public static void setPrefixCommand(String prefix) {
		PREFIX_COMMAND = prefix;
	}
	
	public static void setPrefixError(String prefix) {
		PREFIX_ERROR = prefix;
	}
	
	private static String PREFIX_COMMAND;
	private static String PREFIX_ERROR;
	
	public OMCLogger(String prefix, String error) {
		setPrefixCommand(prefix);
		setPrefixError(error);
	}
	
	public static boolean normal(CommandSender p,String strg){
		String m = PREFIX_COMMAND + strg;
		p.sendMessage(m);
		return true;
	}

	public static boolean normal(String strg){
		for(Player p : Bukkit.getOnlinePlayers())normal(p, strg);
		return true;
	}

	public static boolean normal(String strg, List<String> p){
		for(Player pls : Bukkit.getOnlinePlayers())if(p.contains(pls.getName())) normal(pls, strg);
		return true;
	}

	public static boolean error(CommandSender p, String strg){
		String m = PREFIX_ERROR + strg;
		p.sendMessage(m);
		return false;
	}

	public static boolean error(String strg){
		for(Player p : Bukkit.getOnlinePlayers()) error(p, strg);
		return false;
	}

	public static boolean error(String strg, List<String> p){
		for(Player pls : Bukkit.getOnlinePlayers()) if(p.contains(pls.getName())) error(pls, strg);
		return false;
	}

	public static boolean systemNormal(String strg){
		strg = PREFIX_COMMAND + " " + strg;
		Bukkit.getConsoleSender().sendMessage(strg);
		return true;
	}

	public static boolean systemError(String strg){
		strg = PREFIX_ERROR + " " + strg;
		Bukkit.getConsoleSender().sendMessage(strg);
		return false;
	}

	public static boolean debug(String message) {
		if(OMCPlugin.DEBUG) return systemNormal("[DEBUG] " + message);
		return false;
	}

	public static boolean actionBar(Player p, String message) {
		p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(OMCUtil.translateChatColor(message)));
		return true;
	}

}
