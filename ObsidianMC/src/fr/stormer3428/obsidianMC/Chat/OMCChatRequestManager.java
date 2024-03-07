package fr.stormer3428.obsidianMC.Chat;

import java.util.ArrayList;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import fr.stormer3428.obsidianMC.OMCPlugin;
import fr.stormer3428.obsidianMC.PluginTied;

public class OMCChatRequestManager implements PluginTied, Listener{
	
	public final ArrayList<OMCChatRequest> requests = new ArrayList<>();
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		OMCChatRequest req = null;
		for(OMCChatRequest request : requests) if(request.target.equals(e.getPlayer())){
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
		for(OMCChatRequest request : requests) request.cancel();
		requests.clear();
	}

	@Override
	public final void onPluginReload() {
		for(OMCChatRequest request : requests) request.cancel();
		requests.clear();
	}
}














