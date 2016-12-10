package com.avrgaming.civcraft.loregui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.avrgaming.civcraft.books.Information;
import com.avrgaming.civcraft.books.Tutorial;
import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.lorestorage.LoreGuiItem;
import com.avrgaming.civcraft.lorestorage.LoreGuiItemListener;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.main.CivLog;
import com.avrgaming.civcraft.threading.TaskMaster;

public class OpenInventory implements GuiAction {

	@Override
	public void performAction(InventoryClickEvent event, ItemStack stack) {
		Player player = (Player)event.getWhoClicked();
		player.closeInventory();
		
		class SyncTaskDelayed implements Runnable {
			String playerName;
			ItemStack stack;
			
			public SyncTaskDelayed(String playerName, ItemStack stack) {
				this.playerName = playerName;
				this.stack = stack;
			}
			
			@Override
			public void run() {
				Player player;
				try {
					player = CivGlobal.getPlayer(playerName);
				} catch (CivException e) {
					e.printStackTrace();
					return;
				}
				
				switch (LoreGuiItem.getActionData(stack, "invType")) {
				//civcraft/books/Tutorial.java
				case "showTutorialInventory":
					Tutorial.showTutorialInventory(player);
					break;
				//civcraft/books/Information.java
				case "showTechInventory":
					Information.showTechInventory(player);
					break;
				case "showCraftingHelp":
					Information.showCraftingHelp(player);
					break;
					
				case "showGuiInv":
					String invName = LoreGuiItem.getActionData(stack, "invName");
					Inventory inv = LoreGuiItemListener.guiInventories.get(invName);
					if (inv != null) {
						player.openInventory(inv);
					} else {
						CivLog.error("Couldn't find GUI inventory:"+invName);
					}
					break;
				default:
					break;
				}
			}
		}
		TaskMaster.syncTask(new SyncTaskDelayed(player.getName(), stack));		
	}
}
