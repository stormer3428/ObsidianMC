package fr.stormer3428.obsidianMC.Item;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
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
	private static final String KEY_SECTION = "OMCItem";
	private static final String KEY_LORE = "lore";
	
	private final OMCItemManager itemManager;
	
	public final OMCItemManager getItemManager() {
		return itemManager;
	}

	public ConfigurationSection getConfigurationSection() {
		FileConfiguration config = itemManager.getItemConfig();
		if(config == null) {
			OMCLogger.systemError(OMCLang.ERROR_SMPITEM_MISSING_CONFIG.toString());
			return null;
		}
		String path = KEY_SECTION + "." + getRegistryName();
		if(!config.isConfigurationSection(path)) {
			OMCLogger.systemError(OMCLang.ERROR_SMPITEM_MISSING_CONFIG_SECTION.toString().replace("<%PATH>", path));
			return null;
		}
		return config.getConfigurationSection(path);
	}
	
	public File getFile() {
		File config = itemManager.getItemConfigFile();
		if(config == null) {
			OMCLogger.systemError(OMCLang.ERROR_SMPITEM_MISSING_CONFIG_FILE.toString());
			return null;
		}
		return config;
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
		ConfigurationSection config = getConfigurationSection();
		if(config == null) return "SMPItem." + getRegistryName();
		if(!config.contains(KEY_DISPLAYNAME, true)) {
			File file = getFile();
			OMCLogger.systemError(OMCLang.ERROR_CONFIG_MISSING_PATH.toString().replace("<%PATH>", String.join(".", config.getCurrentPath(), KEY_DISPLAYNAME)).replace("<%CONFIG>", file == null ? "null" : file.getName()));
			return "SMPItem." + getRegistryName();
		}
		return OMCUtil.translateChatColor(config.getString(KEY_DISPLAYNAME));
	}

	public List<String> getLore() {
		List<String> lore = new ArrayList<>();
		ConfigurationSection config = getConfigurationSection();
		if(config == null) return lore;
		if(!config.contains(KEY_LORE, true)) return lore;
		lore.addAll(config.getStringList(KEY_LORE));
		for(int i = 0; i < lore.size(); i++) lore.set(i, OMCUtil.translateChatColor(lore.get(i)));
		return lore;
	}

	public Material getMaterial() {
		ConfigurationSection config = getConfigurationSection();
		if(config == null) return Material.AIR;
		if(!config.contains(KEY_MATERIAL, true)) {
			File file = getFile();
			OMCLogger.systemError(OMCLang.ERROR_CONFIG_MISSING_PATH.toString().replace("<%PATH>", String.join(".", config.getCurrentPath(), KEY_MATERIAL)).replace("<%CONFIG>", file == null ? "null" : file.getName()));
			return Material.AIR;
		}
		String name = config.getString(KEY_MATERIAL);
		for(Material mat : Material.values()) if(mat.name().equals(name)) return mat;
		OMCLogger.systemError(OMCLang.ERROR_MATERIAL_INVALID_NAME.toString().replace("<%MATERIAL>", name));
		return Material.AIR;
	}

	public boolean hasCMD() {
		return !getCMDs().isEmpty();
	}
	
	public List<Integer> getCMDs() {
		List<Integer> list = new ArrayList<>();
		ConfigurationSection config = getConfigurationSection();
		if(config == null) {
			OMCLogger.systemError(OMCLang.ERROR_SMPITEM_MISSING_CONFIG.toString());
			return list;
		}
		list.addAll(config.getIntegerList(KEY_CMDS));
		return list;
	}

	public int getCMD() {
		return getCMD(0);
	}

	public int getCMD(int CMDId) {
		List<Integer> CMDs = getCMDs();
		if(CMDId < 0 || CMDId >= CMDs.size()) {
			OMCLogger.systemError(OMCLang.ERROR_SMPITEM_INVALID_CMD_ID.toString().replace("<CMDID>", CMDId + "").replace("<CMDS>", getCMDs().toString().replace("<SMPITEM>", getRegistryName())));
			return 0;
		}
		return CMDs.get(CMDId);
	}

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
		if(it.getType() != getMaterial()) return false;
		if(it.getItemMeta() == null) return false;
		if(it.getItemMeta().hasCustomModelData() != hasCMD()) return false;
		return !hasCMD() || getCMDs().contains(it.getItemMeta().getCustomModelData());
	}

	@Override
	public boolean matches(OMCItem other) {
		return other.equals(this);
	}

}
