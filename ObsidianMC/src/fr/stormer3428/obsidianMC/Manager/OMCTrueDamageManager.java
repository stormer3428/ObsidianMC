package fr.stormer3428.obsidianMC.Manager;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import fr.stormer3428.obsidianMC.OMCPlugin;
import fr.stormer3428.obsidianMC.Config.PluginTied;

public class OMCTrueDamageManager implements Listener,PluginTied{

	public class DamageRequest {

		public final double damage;
		public final Damageable damageable;

		public DamageRequest(Damageable damageable, double damage) {
			this.damage = damage;
			this.damageable = damageable;
		}
	}

	private final ArrayList<DamageRequest> damageBuffer = new ArrayList<>();

	public void trueDamage(Damageable damageable, double damage) {
		DamageRequest request = new DamageRequest(damageable, damage);
		damageBuffer.add(request);
		Bukkit.broadcastMessage("True damage request of " + damage + " with no source entity ");
		damageable.damage(damage);
		Bukkit.getScheduler().scheduleSyncDelayedTask(OMCPlugin.i, () -> {
			if(damageBuffer.remove(request)) Bukkit.broadcastMessage("True damage request of " + request.damage + " unsatisfied, " + damageBuffer.size() + " left");
		},1l);	
	}

	public void trueDamage(Damageable damageable, double damage, Entity source) {
		trueDamage(damageable, damage);
		//		damageBuffer.add(damage);
		//		Bukkit.broadcastMessage("True damage request of " + damage + " with source entity " + source.getName());
		//		damageable.damage(damage, source);
	}

	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if(!(e.getEntity() instanceof Damageable dmg)) return;
		if(damageBuffer.isEmpty()) return;
		DamageRequest request = damageBuffer.remove(0);
		e.setDamage(request.damage);
		Bukkit.broadcastMessage("True damage request of " + request.damage + " satisfied, " + damageBuffer.size() + " left");
		//		e.setDamage(DamageModifier.ABSORPTION, trueDamage);
	}

	@Override
	public void onPluginDisable() {}

	@Override
	public void onPluginEnable() {
		OMCPlugin.i.getServer().getPluginManager().registerEvents(this, OMCPlugin.i);
	}

	@Override
	public void onPluginReload() {}

}
