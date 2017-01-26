package com.avrgaming.civcraft.loregui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.avrgaming.civcraft.book.CivBook;
import com.avrgaming.civcraft.lorestorage.LoreGuiItem;
import com.avrgaming.civcraft.lorestorage.LoreGuiItemListener;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.threading.TaskMaster;
import com.avrgaming.civcraft.util.CivColor;
import com.avrgaming.civcraft.util.ItemManager;

import gpl.AttributeUtil;

public class BuildMenuList implements GuiAction {
	
	static Inventory guiInventory;
	
	@Override
	public void performAction(InventoryClickEvent event, ItemStack stack) {
		Player player = (Player)event.getWhoClicked();
		Resident res = CivGlobal.getResident(player);
		guiInventory = Bukkit.getServer().createInventory(player,9*1, "Structure Menu List");
		
		ItemStack is;
		if (!res.hasTown()) {
			is = LoreGuiItem.build("Cannot Display", ItemManager.getId(Material.BEDROCK), 0, CivColor.Rose+"Must belong to a town to view structure info.");
			guiInventory.addItem(is);
		} else {
			is = LoreGuiItem.build("Structure Info", ItemManager.getId(Material.STONE_SLAB2), 0, CivColor.LightGray+CivColor.ITALIC+"Show town structure information.");
			is = LoreGuiItem.setAction(is, "BuildStructureInfoList");
			guiInventory.addItem(is);
			
			if (res.getTown().isMayor(res) || res.getTown().isAssistant(res)) {
				is = LoreGuiItem.build("Structure Info", ItemManager.getId(Material.BRICK_STAIRS), 3, CivColor.LightGray+CivColor.ITALIC+"Build a structure in the town.");
				is = LoreGuiItem.setAction(is, "BuildStructureList");
				guiInventory.setItem(2, is);
				
				is = LoreGuiItem.build("Wonder Info", ItemManager.getId(Material.BRICK_STAIRS), 4, CivColor.LightGray+CivColor.ITALIC+"Build a wonder in the town.");
				is = LoreGuiItem.setAction(is, "BuildWonderList");
				AttributeUtil attrs = new AttributeUtil(is);
					attrs.setShiny();
					is = attrs.getStack();
				guiInventory.setItem(3, is);
			} else if (!res.getTown().isMayor(res) && !res.getTown().isAssistant(res)) {
				is = LoreGuiItem.build("Cannot Display", ItemManager.getId(Material.BEDROCK), 0, CivColor.Rose+"Must be a town mayor or assistant build structures and wonders.");
				guiInventory.setItem(2, is);
			}
		}
		
		/* Add Information Item */
		is = LoreGuiItem.build("Information", ItemManager.getId(Material.SHIELD), 0,
				CivColor.White+"This GUI Allows for players to see",
				CivColor.White+"information on structures they own,",
				CivColor.White+"or for mayors/assistants to build a",
				CivColor.White+"structure for the town."
				);
		guiInventory.setItem((9*1)-2, is);
		
		/* Add back buttons. */
		ItemStack backButton = LoreGuiItem.build("Back", ItemManager.getId(Material.MAP), 0, "Back to Topics");
		backButton = LoreGuiItem.setAction(backButton, "OpenInventory");
		backButton = LoreGuiItem.setActionData(backButton, "invType", "showGuiInv");
		backButton = LoreGuiItem.setActionData(backButton, "invName", CivBook.guiInventory.getName());
		guiInventory.setItem((9*1)-1, backButton);
		
		LoreGuiItemListener.guiInventories.put(guiInventory.getName(), guiInventory);
		TaskMaster.syncTask(new OpenInventoryTask(player, guiInventory));
	}
}
