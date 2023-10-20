package fr.stormer3428.obsidianMC.Command;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.stormer3428.obsidianMC.OMCPlugin;
import fr.stormer3428.obsidianMC.Item.OMCItem;
import fr.stormer3428.obsidianMC.Manager.OMCItemManager;
import fr.stormer3428.obsidianMC.Manager.OMCLogger;
import fr.stormer3428.obsidianMC.Manager.OMCPowerManager;
import fr.stormer3428.obsidianMC.Power.OMCPower;
import fr.stormer3428.obsidianMC.Util.OMCLang;

public class OMCPremadeCommandFactory {

	public static OMCCommand[] getGiveCommands(OMCItemManager itemManager, String root, boolean requiresPermission) {
		return new OMCCommand[] {
				new OMCCommand(root + " give", requiresPermission) {

					@Override
					public boolean execute(CommandSender sender, ArrayList<String> args) {
						return OMCLogger.error(sender, OMCLang.ERROR_MISSINGARG_NOPLAYER.toString());
					}
				}
				,
				new OMCCommand(root + " give %P%", requiresPermission) {

					@Override
					public boolean execute(CommandSender sender, ArrayList<String> args) {
						return OMCLogger.error(sender, OMCLang.ERROR_MISSINGARG_ITEM.toString());
					}
				}
				,
				new OMCCommand(root + " give %P% %ITEM%", requiresPermission) {

					@Override
					public boolean execute(CommandSender sender, ArrayList<String> args) {
						Player p = Bukkit.getPlayer(args.get(0));
						if(p == null) return OMCLogger.error(sender, OMCLang.ERROR_INVALIDARG_NOPLAYER.toString().replace("<%PLAYER>", args.get(0)));
						OMCItem item = itemManager.fromName(args.get(1));
						if(item == null) return OMCLogger.error(sender, OMCLang.ERROR_GENERIC_NOITEM.toString().replace("<%ITEM>", args.get(1))); 
						ItemStack it = item.createItemsStack(1);
						if(it == null) return OMCLogger.error(sender, OMCLang.ERROR_ITEM_GENERATION_FAILED.toString().replace("<%ITEM>", item.getRegistryName())); 
						p.getInventory().addItem(it);
						return OMCLogger.normal(sender, OMCLang.ITEM_GIVE.toString().replace("<%AMOUNT>", "1").replace("<%PLAYER>", p.getName()).replace("<%ITEM>", item.getRegistryName()));
					}
				}
				,
				new OMCCommand(root + " give %P% %ITEM% %V%", requiresPermission) {

					@Override
					public boolean execute(CommandSender sender, ArrayList<String> args) {
						Player p = Bukkit.getPlayer(args.get(0));
						if(p == null) return OMCLogger.error(sender, OMCLang.ERROR_INVALIDARG_NOPLAYER.toString().replace("<%PLAYER>", args.get(0)));
						OMCItem item = itemManager.fromName(args.get(1));
						if(item == null) return OMCLogger.error(sender, OMCLang.ERROR_GENERIC_NOITEM.toString().replace("<%ITEM>", args.get(1))); 
						try {
							int amount = Integer.parseInt(args.get(2));
							p.getInventory().addItem(item.createItemsStack(amount));
							return OMCLogger.normal(sender, OMCLang.ITEM_GIVE.toString().replace("<%AMOUNT>", amount + "").replace("<%PLAYER>", p.getName()).replace("<%ITEM>", item.getRegistryName()));
						}catch (NumberFormatException e) {
							return OMCLogger.error(sender, OMCLang.ERROR_INVALIDARG_INTEGER.toString().replace("<%VALUE>", args.get(2)));
						}
					}
				}
		};
	}

	public static OMCCommand[] getCreditsCommands(String root, String name) {
		return new OMCCommand[] {
				new OMCCommand(root, false) {
					@Override
					public boolean execute(CommandSender sender, ArrayList<String> args) {
						return OMCLogger.normal(sender, name + " by stormer3428");
					}
				}
		};
	}

	public static OMCCommand[] getReloadCommands(String root) {
		return new OMCCommand[] {
				new OMCCommand(root + " reload", true) {

					@Override
					public boolean execute(CommandSender sender, ArrayList<String> args) {
						OMCPlugin.i.reload();
						return OMCLogger.normal(sender, OMCLang.RELOADED_CONFIG.toString());}
				}
		};
	}

	public static OMCCommand[] getPowerCommands(OMCPowerManager powerManager, String root) {
		return new OMCCommand[] {
				new OMCCommand(root + " power", true) {

					@Override
					public boolean execute(CommandSender sender, ArrayList<String> args) {
						return OMCLogger.normal(sender, OMCLang.ERROR_MISSINGARG_POWER.toString());
					}
				}
				,
				new OMCCommand(root + " power %POWER%", true) {

					@Override
					public boolean execute(CommandSender sender, ArrayList<String> args) {
						if(!(sender instanceof Player p)) return OMCLogger.error(sender, OMCLang.ERROR_PLAYERONLY.toString());
						OMCPower power = powerManager.fromName(args.get(0));
						if(power == null) return OMCLogger.error(sender, OMCLang.ERROR_GENERIC_NOPOWER.toString().replace("<%POWER>", args.get(0)));
						power.tryCast(null, p);
						return OMCLogger.normal(sender, OMCLang.POWER_MANUALCAST.toString().replace("<%POWER>", power.getRegistryName()));
					}
				}
				,
				new OMCCommand(root + " power %POWER% %P%", true) {

					@Override
					public boolean execute(CommandSender sender, ArrayList<String> args) {
						Player p = Bukkit.getPlayer(args.get(1));
						if(p == null) return OMCLogger.error(sender, OMCLang.ERROR_INVALIDARG_NOPLAYER.toString().replace("<%PLAYER>", args.get(0)));
						OMCPower power = powerManager.fromName(args.get(0));
						if(power == null) return OMCLogger.error(sender, OMCLang.ERROR_GENERIC_NOPOWER.toString().replace("<%POWER>", args.get(0)));
						power.tryCast(null, p);
						return OMCLogger.normal(sender, OMCLang.POWER_MANUALCAST.toString().replace("<%POWER>", power.getRegistryName()));
					}
				}
		};
	}
	

	public static OMCCommand[] getDebugCommands(String root) {
		return new OMCCommand[] {
				new OMCCommand(root + " debug%%%d resetplayer %P%", true) {

					@Override
					public boolean execute(CommandSender sender, ArrayList<String> args) {
						Player p = Bukkit.getPlayer(args.get(1));
						if(p == null) return OMCLogger.error(sender, OMCLang.ERROR_INVALIDARG_NOPLAYER.toString().replace("<%PLAYER>", args.get(0)));

						try {
							for(Attribute attribute : Attribute.values()) {
								AttributeInstance att = p.getAttribute(attribute);
								if(att == null) continue;
								for(AttributeModifier modifier : att.getModifiers()) {
									att.removeModifier(modifier);
								}
							}
						}catch (Exception e) {
							OMCLogger.error(sender, e.toString());
							for(StackTraceElement element : e.getStackTrace()) {
								OMCLogger.error(sender, element.toString());
							}
						}

						return OMCLogger.normal(sender, OMCLang.RELOADED_CONFIG.toString());
					}
				}
				,
				new OMCCommand(root + " debug%%%d viewstringblob%%%viewblob%%%vb", true) {

					@Override
					public boolean execute(CommandSender sender, ArrayList<String> vars) {
						if(!(sender instanceof Player p)) return OMCLogger.error(sender, OMCLang.ERROR_PLAYERONLY.toString());
						final ItemStack it = p.getInventory().getItemInMainHand();
						YamlConfiguration config = new YamlConfiguration();
						config.set("stringBlob", it);
						return OMCLogger.normal(p, config.saveToString());
					}
				}
		};
	}
	
	
	




}
