/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.items;

import java.util.LinkedList;

import org.bukkit.entity.Player;

import com.civcraft.exception.CivException;
import com.civcraft.listener.CustomItemManager;
import com.civcraft.main.CivGlobal;

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