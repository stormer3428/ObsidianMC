package fr.stormer3428.obsidianMC.Manager;

import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;

import fr.stormer3428.obsidianMC.Config.PluginTied;

public class OMCTrueDamageManager_SET implements /*Listener,*/PluginTied{

	//	public class DamageRequest {
	//
	//		public final double damage;
	//		public final Damageable damageable;
	//		public final Entity damager;
	//
	//		public DamageRequest(Damageable damageable, double damage, Entity damager) {
	//			this.damage = damage;
	//			this.damageable = damageable;
	//			this.damager = damager;
	//		}
	//	}

	//	private final ArrayList<DamageRequest> damageBuffer = new ArrayList<>();

	public void trueDamage(Damageable damageable, double damage) {
		trueDamage(damageable, damage, null);
	}

	//	public void trueDamage(Damageable damageable, double damage, Entity source) {
	//		if(getRequest(damage, damageable, source) != null) return;
	//		DamageRequest request = new DamageRequest(damageable, damage, source);
	//		damageBuffer.add(request);
	//		if(source == null) damageable.damage(damage);
	//		else damageable.damage(damage, source);
	//		Bukkit.getScheduler().scheduleSyncDelayedTask(OMCPlugin.i, () -> {
	//			if(damageBuffer.remove(request)) OMCLogger.systemError("True damage request of " + request.damage + " unsatisfied, " + damageBuffer.size() + " left");
	//		},1l);
	//	}

	public void trueDamage(Damageable damageable, double damage, Entity source) {
		double updatedHealth = damageable.getHealth() - damage;
		if(source == null) damageable.damage(0);
		else damageable.damage(0, source);
		damageable.setHealth(updatedHealth);
		if (updatedHealth > 0) return;
		if(!(damageable instanceof HumanEntity le)) return;
		ItemStack totem = le.getInventory().getItemInMainHand();
		if(totem == null || !totem.getType().equals(Material.TOTEM_OF_UNDYING)) le.getInventory().getItemInOffHand();
		if(totem == null || !totem.getType().equals(Material.TOTEM_OF_UNDYING)) return;
		damageable.setHealth(1);
		damageable.damage(1000000, damageable);
	}

	//	@EventHandler
	//	public void onDamage(EntityDamageEvent e) {
	//		if(!(e.getEntity() instanceof Damageable dmg)) return;
	//		Entity damager = null;
	//		if(e instanceof EntityDamageByEntityEvent edbe) damager = edbe.getDamager();
	//		if(damageBuffer.isEmpty()) return;
	//		DamageRequest request = getRequest(e.getDamage(), dmg, damager);
	//		if(request == null) return;
	//		damageBuffer.remove(request);
	//		e.setDamage(request.damage);
	////		Bukkit.broadcastMessage("True damage request of " + request.damage + " satisfied, " + damageBuffer.size() + " left");
	//		//		e.setDamage(DamageModifier.ABSORPTION, trueDamage);
	//	}

	//	public DamageRequest getRequest(double damage, Damageable damageable, Entity damager) {
	//		for(DamageRequest request : damageBuffer) {
	//			if(request.damage != damage) continue;
	//			if(request.damageable != damageable) continue;
	//			if(request.damager != damager) continue;
	//			return request;
	//		}
	//		return null;
	//	}

	@Override
	public void onPluginDisable() {}

	@Override
	public void onPluginEnable() {
		//		OMCPlugin.i.getServer().getPluginManager().registerEvents(this, OMCPlugin.i);
	}

	@Override
	public void onPluginReload() {}

}
