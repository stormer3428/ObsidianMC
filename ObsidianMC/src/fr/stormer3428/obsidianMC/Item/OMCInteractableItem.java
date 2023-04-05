package fr.stormer3428.obsidianMC.Item;

import org.bukkit.entity.Player;

public interface OMCInteractableItem extends OMCItem{

	public boolean listensToLeftClick();
	public boolean listensToRightClick();
	
	public void onLeftClick(Player p);
	public void onRightClick(Player p);
	
}
