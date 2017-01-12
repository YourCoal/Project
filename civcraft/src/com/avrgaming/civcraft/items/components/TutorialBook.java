package com.avrgaming.civcraft.items.components;

import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.avrgaming.civcraft.book.CivBook;
import com.avrgaming.civcraft.util.CivColor;

import gpl.AttributeUtil;

public class TutorialBook extends ItemComponent {

	@Override
	public void onPrepareCreate(AttributeUtil attrs) {
		attrs.addLore(CivColor.Gold+"CivCraft Info");
		attrs.addLore(CivColor.Rose+"<Right Click to Open>");
	}

	
	public void onInteract(PlayerInteractEvent event) {
		event.setCancelled(true);
		if (!event.getAction().equals(Action.RIGHT_CLICK_AIR) &&
				!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			return;
		}
		CivBook.spawnGuiBook(event.getPlayer());
	}
	
	public void onItemSpawn(ItemSpawnEvent event) {
		event.setCancelled(true);
	}
}
