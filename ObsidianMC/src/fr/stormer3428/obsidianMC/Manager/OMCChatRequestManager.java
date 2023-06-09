package fr.stormer3428.obsidianMC.Manager;

import java.util.ArrayList;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import fr.stormer3428.obsidianMC.OMCPlugin;
import fr.stormer3428.obsidianMC.Chat.ChatRequest;
import fr.stormer3428.obsidianMC.Config.PluginTied;

public class OMCChatRequestManager implements PluginTied, Listener{
	
	public final ArrayList<ChatRequest> requests = new ArrayList<>();
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		ChatRequest req = null;
		for(ChatRequest request : requests) if(request.target.equals(e.getPlayer())){
			req = request;
			break;
		}
		if(req == null) return;
		req.compete(e);
		e.setCancelled(true);
	}

	@Override
	public final void onPluginEnable() {
		OMCPlugin.i.getServer().getPluginManager().registerEvents(this, OMCPlugin.i);
	}

	@Override
	public final void onPluginDisable() {
		for(ChatRequest request : requests) request.cancel();
		requests.clear();
	}

	@Override
	public final void onPluginReload() {
		for(ChatRequest request : requests) request.cancel();
		requests.clear();
	}
}














