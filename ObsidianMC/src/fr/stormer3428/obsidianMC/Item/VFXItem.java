package fr.stormer3428.obsidianMC.Item;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.stormer3428.obsidianMC.Manager.OMCItemManager;

public class VFXItem extends SMPItem{

	private final int[] CMD;
	private final int CMDCount;
	private final Material material;
	private final boolean isLooping;

	public VFXItem(String registryName, OMCItemManager itemManager, Material material, boolean isLooping, int ... cmd) {
		super(registryName, itemManager);
		this.material = material;
		this.isLooping = isLooping;
		this.CMD = cmd;
		this.CMDCount = cmd.length;
	}

	@Override
	public String getDisplayName() {
		return getRegistryName();
	}
	
	@Override
	public boolean hasCMD() {
		return CMDCount != 0;
	}
	
	@Override
	public int getCMD(int CMDId) {
		if(CMDId >= 0 && CMDId < CMDCount) return CMD[CMDId];
		return 0;
	}

	@Override
	public Material getMaterial() {
		return material;
	}
	
	@Override
	public boolean matches(ItemStack it) {
		if(it == null) return false;
		if(it.getType() != getMaterial()) return false;
		if(it.getItemMeta() == null) return false;
		if(!it.getItemMeta().hasCustomModelData()) return false;
		int itemCMD = it.getItemMeta().getCustomModelData();
		for(int cmd : CMD) if(cmd == itemCMD) return true;
		return false;
	}

	@Override
	public ItemStack createItemsStack(int amount) {
		Material material = getMaterial();
		ItemStack it = new ItemStack(material, amount);
		if(amount == 0 || material == Material.AIR) return it;
		ItemMeta itm = it.getItemMeta();
		itm.setDisplayName(ChatColor.RESET + getDisplayName());
		if(hasCMD()) itm.setCustomModelData(getCMD());
		it.setItemMeta(itm);
		return it;
	}
	
	public int getCMDCount() {
		return CMDCount;
	}

	public boolean isLooping() {
		return isLooping;
	}
}
