package fr.stormer3428.obsidianMC.Item;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;

import fr.stormer3428.obsidianMC.Manager.OMCItemManager;
import fr.stormer3428.obsidianMC.Util.OMCLang;
import fr.stormer3428.obsidianMC.Util.OMCLogger;
import fr.stormer3428.obsidianMC.Util.OMCUtil;

/**
 * An implementation of {@link OMCItem} that handles custom name, custom model data, material, enchant glint and lore <br>
 * <b>IMPORTANT</b>: Relies on the customModelData and material to tell custom items apart, if the uniqueness is not respected, some items will overlap
 * 
 * @author stormer3428
 * @see OMCItemManager
 */

public class SMPItem implements OMCItem {

	private static final String KEY_DISPLAYNAME = "displayName";
	private static final String KEY_CMDS = "customModelData";
	private static final String KEY_MATERIAL = "material";
	private static final String KEY_GLINT = "addGlint";
	private static final String KEY_LORE = "lore";

	private final OMCItemManager itemManager;

	public final OMCItemManager getItemManager() {
		return itemManager;
	}

	private final String registryName;

	/**
	 * 
	 * @param registryName
	 * The name of the item in the config
	 * @param itemManager
	 * The manager that holds this item's config
	 */
	public SMPItem(String registryName, OMCItemManager itemManager) {
		this.itemManager = itemManager;
		this.registryName = registryName;
	}

	@Override
	public String getRegistryName() {
		return registryName;
	}
	
	/**
	 * 
	 * @implNote
	 * Automatically translates chatcolor using {@link OMCUtil#translateChatColor(String)}
	 * @return The displayName set in the config of the linked {@link #itemManager}
	 */
	public String getDisplayName() {
		return OMCUtil.translateChatColor(itemManager.getString(registryName + "." + KEY_DISPLAYNAME));
	}

	/**
	 * @implNote
	 * Inted to be used for custom implementation as this one requires a customModeldata to be set
	 * @return whether the item has any customModelData set in the config
	 * @see #getCMD(int)
	 * @see #getCMD()
	 */
	public boolean hasCMD() {
		return !itemManager.getIntegerList(registryName + "." + KEY_CMDS, true).isEmpty();
	}

	/**
	 * 
	 * @return the first custom model data set in the config
	 * @see #getCMD(int)
	 * @see #hasCMD()
	 */
	public int getCMD() {
		return getCMD(0);
	}
	
	/**
	 * 
	 * @param CMDId
	 * @return the custom model data which holds this position in the list defined in the config
	 * @implNote
	 * Inted to be used for custom implementation as this one only uses a single customModeldata
	 * @see #getCMD()
	 * @see #hasCMD()
	 */
	public int getCMD(int CMDId) {
		List<Integer> CMDs = itemManager.getIntegerList(registryName + "." + KEY_CMDS, true);
		if(CMDId < 0 || CMDId >= CMDs.size()) {
			OMCLogger.systemError(OMCLang.ERROR_INVALID_CMD_ID.toString().replace("<CMDID>", CMDId + "").replace("<CMDS>", CMDs.toString().replace("<SMPITEM>", getRegistryName())));
			return 0;
		}
		return CMDs.get(CMDId);
	}


	/**
	 * 
	 * @implNote
	 * Returns {@link Material#AIR} when undefined
	 * @return The {@link Material} set in the config of the linked {@link #itemManager}
	 */	
	public Material getMaterial() {
		String name = itemManager.getString(registryName + "." + KEY_MATERIAL);
		if(name == null) return Material.AIR;
		for(Material mat : Material.values()) if(mat.name().equals(name)) return mat;
		OMCLogger.systemError(OMCLang.ERROR_INVALIDARG_MATERIAL.toString().replace("<%MATERIAL>", name));
		return Material.AIR;
	}
	
	/**
	 * Creates the {@link ItemStack} based on the config of the linked {@link #itemManager}
	 */
	@Override
	public ItemStack createItemsStack(int amount) {
		Material material = getMaterial();
		ItemStack it = new ItemStack(material, amount);
		if(amount == 0 || material == Material.AIR) return it;
		ItemMeta itm = it.getItemMeta();
		itm.setDisplayName(ChatColor.RESET + getDisplayName());
		if(hasCMD()) itm.setCustomModelData(getCMD());
		List<String> lore = itemManager.getStringList(registryName + "." + KEY_LORE);
		if(getItemManager().getConfig().getBoolean(registryName + "." + KEY_GLINT)) {
			itm.addEnchant(Enchantment.DAMAGE_ARTHROPODS, 5, false);
			itm.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}
		itm.setLore(lore.isEmpty() ? null : lore);
		it.setItemMeta(itm);
		return it;
	}

	/**
	 * Returns whether the given {@link ItemStack} matches this {@link SMPItem}
	 * @implNote
	 * Relies strictly on {@link Material} and CustomModelData, if two {@link SMPItem} have the same material and CMD, this will return true depsite other apparent changes
	 */
	@Override
	public boolean matches(ItemStack it) {
		if(it == null) return false;
		if(it.getType() != getMaterial()) return false;
		if(it.getItemMeta() == null) return false;
		if(it.getItemMeta().hasCustomModelData() != hasCMD()) return false;
		return !hasCMD() || itemManager.getIntegerList(registryName + "." + KEY_CMDS).contains(it.getItemMeta().getCustomModelData());
	}
	
	/**
	 * Alias of {@link #equals(Object)}
	 */
	@Override
	public boolean matches(OMCItem other) {
		return other.equals(this);
	}

	/**
	 * @return an empty list
	 * @implNote
	 * Intended to be ovewritten for easy chain defining in things such as {@link Enum}
	 */
	public List<Recipe> getRecipes() {
		return new ArrayList<>();
	}
	
	/**
	 * 
	 * @param The desired key from the item's configurationSection
	 * @return The formatted path to that value
	 */
	public String path(String innerPath) {
		return getRegistryName() + "." + innerPath;
	}

}
