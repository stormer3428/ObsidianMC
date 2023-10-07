package fr.stormer3428.obsidianMC.Item;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.stormer3428.obsidianMC.Manager.OMCItemManager;
import fr.stormer3428.obsidianMC.Power.OMCPower;

public abstract class ManualGenericSMPPowerItem extends GenericSMPPowerItem implements OMCItem{

	public ManualGenericSMPPowerItem(String registryName, OMCItemManager itemManager, OMCPower omcPower) {
		super(registryName, itemManager, omcPower);
	}
	
	@Override
	public abstract String getDisplayName();

	@Override
	public abstract int getCMD(int CMDId);
	
	@Override
	public abstract boolean hasCMD();
	
	@Override
	public abstract Material getMaterial();
	
	@Override
	public boolean matches(OMCItem other) {
		// TODO Auto-generated method stub
		return super.matches(other);
	}
	
	public abstract List<String> getLore();

	@Override
	public ItemStack createItemsStack(int amount) {
		Material material = getMaterial();
		ItemStack it = new ItemStack(material, amount);
		if(amount == 0 || material == Material.AIR) return it;
		ItemMeta itm = it.getItemMeta();
		itm.setDisplayName(ChatColor.RESET + getDisplayName());
		if(hasCMD()) itm.setCustomModelData(getCMD());
		List<String> lore = getLore();
		itm.setLore(lore.isEmpty() ? null : lore);
		it.setItemMeta(itm);
		return it;
	}
	
	@Override
	public boolean matches(ItemStack it) {
		if(it == null) return false;
		if(it.getType() != getMaterial()) return false;
		if(it.getItemMeta() == null) return false;
		if(it.getItemMeta().hasCustomModelData() != hasCMD()) return false;
		return !hasCMD() || it.getItemMeta().getCustomModelData() == getCMD();
	}
	
}
