package fr.stormer3428.obsidianMC.Item;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.stormer3428.obsidianMC.Manager.OMCItemManager;
import fr.stormer3428.obsidianMC.Util.OMCLang;
import fr.stormer3428.obsidianMC.Util.OMCLogger;
import fr.stormer3428.obsidianMC.Util.OMCUtil;

public abstract class SMPItem implements OMCItem{

	private static final String KEY_DISPLAYNAME = "displayName";
	private static final String KEY_CMDS = "customModelData";
	private static final String KEY_MATERIAL = "material";
	private static final String KEY_LORE = "lore";
	
	private final OMCItemManager itemManager;
	
	public final OMCItemManager getItemManager() {
		return itemManager;
	}
	
	private final String registryName;
	
	public SMPItem(String registryName, OMCItemManager itemManager) {
		this.itemManager = itemManager;
		this.registryName = registryName;
	}
	
	@Override
	public String getRegistryName() {
		return registryName;
	}

	public String getDisplayName() {
		return OMCUtil.translateChatColor(itemManager.getString(registryName + "." + KEY_DISPLAYNAME));
	}

	public boolean hasCMD() {
		return !itemManager.getIntegerList(registryName + "." + KEY_CMDS).isEmpty();
	}

	public int getCMD() {
		return getCMD(0);
	}

	public int getCMD(int CMDId) {
		List<Integer> CMDs = itemManager.getIntegerList(registryName + "." + KEY_CMDS);
		if(CMDId < 0 || CMDId >= CMDs.size()) {
			OMCLogger.systemError(OMCLang.ERROR_INVALID_CMD_ID.toString().replace("<CMDID>", CMDId + "").replace("<CMDS>", CMDs.toString().replace("<SMPITEM>", getRegistryName())));
			return 0;
		}
		return CMDs.get(CMDId);
	}

	public Material getMaterial() {
		String name = itemManager.getString(registryName + "." + KEY_MATERIAL);
		if(name == null) return Material.AIR;
		for(Material mat : Material.values()) if(mat.name().equals(name)) return mat;
		OMCLogger.systemError(OMCLang.ERROR_INVALIDARG_MATERIAL.toString().replace("<%MATERIAL>", name));
		return Material.AIR;
	}
	
	@Override
	public ItemStack createItemsStack(int amount) {
		Material material = getMaterial();
		ItemStack it = new ItemStack(material, amount);
		if(amount == 0 || material == Material.AIR) return it;
		ItemMeta itm = it.getItemMeta();
		itm.setDisplayName(ChatColor.RESET + getDisplayName());
		if(hasCMD()) itm.setCustomModelData(getCMD());
		List<String> lore = itemManager.getStringList(registryName + "." + KEY_LORE);
		itm.setLore(lore.isEmpty() ? null : lore);
		it.setItemMeta(itm);
		return it;
	}

	@Override
	public boolean matches(ItemStack it) {
		if(it.getType() != getMaterial()) return false;
		if(it.getItemMeta() == null) return false;
		if(it.getItemMeta().hasCustomModelData() != hasCMD()) return false;
		return !hasCMD() || itemManager.getIntegerList(registryName + "." + KEY_CMDS).contains(it.getItemMeta().getCustomModelData());
	}

	@Override
	public boolean matches(OMCItem other) {
		return other.equals(this);
	}

}
