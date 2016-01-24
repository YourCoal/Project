package com.avrgaming.civcraft.items;

import java.util.LinkedList;

import org.bukkit.entity.Player;

import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.listener.CustomItemManager;
import com.avrgaming.civcraft.main.CivGlobal;

public class ItemDuraSyncTask implements Runnable {

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		
		for (String playerName : CustomItemManager.itemDuraMap.keySet()) {
			Player player;
			try {
				player = CivGlobal.getPlayer(playerName);
			} catch (CivException e) {
				continue;
			}
			
			LinkedList<ItemDurabilityEntry> entries = CustomItemManager.itemDuraMap.get(playerName);
			
			for (ItemDurabilityEntry entry : entries) {
				entry.stack.setDurability(entry.oldValue);
			}
			
			player.updateInventory();
		}
		
		CustomItemManager.duraTaskScheduled = false;
	}
}