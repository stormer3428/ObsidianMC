package fr.stormer3428.obsidianMC.Chat;

import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import fr.stormer3428.obsidianMC.OMCPlugin;
import fr.stormer3428.obsidianMC.Manager.OMCChatRequestManager;
import fr.stormer3428.obsidianMC.Manager.OMCLogger;

public class ChatRequest {

	public final Player target;
	public final String message;
	public int delay = 20 * 30;
	public final OMCChatRequestManager manager;
	
	public ChatRequest(Player target, String message, OMCChatRequestManager manager) {
		this.target = target;
		this.message = message;
		this.manager = manager;
	}
	
	public ChatRequest setDelay(int delay) {
		this.delay = Math.max(0, delay);
		return this;
	}
	
	public void send() {
		manager.requests.add(this);
		new BukkitRunnable() {
			
			@Override
			public void run() {
				timeout();
			}
		}.runTaskLater(OMCPlugin.i, delay);
		OMCLogger.normal(target, message);
	}
	
	public final void cancel() {
		if(!manager.requests.contains(this)) return;
		manager.requests.remove(this);
		onCancel();
	}
	
	public final void timeout() {
		if(!manager.requests.contains(this)) return;
		manager.requests.remove(this);
		onTimeout();
	}
	
	public final void compete(AsyncPlayerChatEvent e) {
		if(!manager.requests.contains(this)) return;
		manager.requests.remove(this);
		onComplete(e.getMessage(), e);
	}
	
	public void onComplete(String receivedMessage, AsyncPlayerChatEvent e) {};
	public void onTimeout() {
		OMCLogger.error(target, "Chat request timed out");
	}
	public void onCancel() {
		OMCLogger.error(target, "Chat request cancelled");
	}
}
