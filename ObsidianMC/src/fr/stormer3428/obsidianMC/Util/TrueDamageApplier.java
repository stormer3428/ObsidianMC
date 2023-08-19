package fr.stormer3428.obsidianMC.Util;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;

import fr.stormer3428.obsidianMC.OMCPlugin;
import fr.stormer3428.obsidianMC.Config.PluginTied;

@SuppressWarnings("deprecation")
public class TrueDamageApplier implements Listener,PluginTied{

	public class DamageRequest {

		public final double damage;
		public final Damageable damageable;
		public final DamageModifier[] bypassedModifiers;

		public DamageRequest(Damageable damageable, double damage) {
			this(damageable, damage, DamageModifier.ARMOR, DamageModifier.HARD_HAT, DamageModifier.MAGIC, DamageModifier.RESISTANCE);
		}

		public DamageRequest(Damageable damageable, double damage, DamageModifier ... bypassedDamageMoodifiers) {
			this.damage = damage;
			this.damageable = damageable;
			this.bypassedModifiers = bypassedDamageMoodifiers;
		}
	}

	private final ArrayList<DamageRequest> damageBuffer = new ArrayList<>();

	public void trueDamage(Damageable damageable, double damage) {
		DamageRequest request = new DamageRequest(damageable, damage);
		damageBuffer.add(request);
		//				Bukkit.broadcastMessage("True damage request of " + damage + " with no source entity ");
		damageable.damage(damage);
		Bukkit.getScheduler().scheduleSyncDelayedTask(OMCPlugin.i, () -> {
			//						if(
			damageBuffer.remove(request)
			//								) Bukkit.broadcastMessage("True damage request of " + request.damage + " unsatisfied, " + damageBuffer.size() + " left")
			;
		},1l);	
	}

	public void trueDamage(Damageable damageable, double damage, Entity source) {
		DamageRequest request = new DamageRequest(damageable, damage);
		damageBuffer.add(request);
		//				Bukkit.broadcastMessage("True damage request of " + damage + " with no source entity ");
		damageable.damage(damage, source);
		Bukkit.getScheduler().scheduleSyncDelayedTask(OMCPlugin.i, () -> {
			//						if(
			damageBuffer.remove(request)
			//								) Bukkit.broadcastMessage("True damage request of " + request.damage + " unsatisfied, " + damageBuffer.size() + " left")
			;
		},1l);	
	}

	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if(!(e.getEntity() instanceof Damageable dmg)) return;
		if(damageBuffer.isEmpty()) return;
		DamageRequest request = damageBuffer.remove(0);
		for(DamageModifier modifier : request.bypassedModifiers) if(e.isApplicable(modifier)) e.setDamage(modifier, 0);
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
