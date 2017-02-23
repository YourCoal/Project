package com.avrgaming.civcraft.loregui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.avrgaming.civcraft.book.CivBook;
import com.avrgaming.civcraft.loregui.GuiAction;
import com.avrgaming.civcraft.loregui.OpenInventoryTask;
import com.avrgaming.civcraft.lorestorage.LoreGuiItem;
import com.avrgaming.civcraft.lorestorage.LoreGuiItemListener;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.threading.TaskMaster;
import com.avrgaming.civcraft.util.CivColor;
import com.avrgaming.civcraft.util.ItemManager;

import gpl.AttributeUtil;

public class _1BuildMenuList implements GuiAction {
	
	static Inventory guiInventory;
	
	@Override
	public void performAction(InventoryClickEvent event, ItemStack stack) {
		Player player = (Player)event.getWhoClicked();
		Resident res = CivGlobal.getResident(player);
		guiInventory = Bukkit.getServer().createInventory(player,9*3, "Structure Menu");
		
		ItemStack is;
		if (!res.hasTown()) {
			is = LoreGuiItem.build("Cannot Display", ItemManager.getId(Material.BEDROCK), 0, CivColor.Rose+"Must belong to a town to view building commands.");
			guiInventory.setItem(0, is);
			
			is = LoreGuiItem.build("Cannot Display", ItemManager.getId(Material.BEDROCK), 0, CivColor.Rose+"Must belong to a town to view structure info.");
			guiInventory.setItem(2, is);
		} else {
			is = LoreGuiItem.build("Build Commands", ItemManager.getId(Material.COMMAND), 0, CivColor.LightGrayItalic+"Select building commands.");
//			is = LoreGuiItem.setAction(is, "BuildCommandList");
			guiInventory.setItem(0, is);
			
			is = LoreGuiItem.build("Structure Info", ItemManager.getId(Material.STONE_SLAB2), 0, CivColor.LightGrayItalic+"Show town structure information.");
			is = LoreGuiItem.setAction(is, "_1BuildStructureInfoList");
			guiInventory.setItem(2, is);
		}
		
		/*if (res.getTown().hasStructure("s_library") && player.isOp()) {
			is = LoreGuiItem.build("Library Enchanting", ItemManager.getId(Material.ENCHANTED_BOOK), 0, CivColor.LightGrayItalic+"Opens enchanting menu. Requires a Library & Admin.");
			is = LoreGuiItem.setAction(is, "_1BuildLibraryEnchantList");
			guiInventory.setItem(13, is);
		} else {
			is = LoreGuiItem.build("Library Enchanting", ItemManager.getId(Material.BEDROCK), 0, CivColor.RoseItalic+"Opens enchanting menu. "+CivColor.BOLD+"+Requires a Library & Admin.");
			guiInventory.setItem(13, is);
		}*/
			
		if (res.getCiv().getLeaderGroup().hasMember(res) && res.getTown().isMayor(res) || res.getTown().isAssistant(res)) {
			is = LoreGuiItem.build("Structure Info", ItemManager.getId(Material.BRICK_STAIRS), 3, CivColor.LightGray+CivColor.ITALIC+"Build a structure in the town.");
			is = LoreGuiItem.setAction(is, "_1BuildStructureList");
			guiInventory.setItem(4, is);
			
			is = LoreGuiItem.build("Wonder Info", ItemManager.getId(Material.BRICK_STAIRS), 4, CivColor.LightGray+CivColor.ITALIC+"Build a wonder in the town.");
			is = LoreGuiItem.setAction(is, "_1BuildWonderList");
			AttributeUtil attrs = new AttributeUtil(is);
				attrs.setShiny();
				is = attrs.getStack();
			guiInventory.setItem(5, is);
/*		} else if (!res.getCiv().getLeaderGroup().hasMember(res) && !res.getTown().isMayor(res) && !res.getTown().isAssistant(res) && !res.hasTown()) {
			is = LoreGuiItem.build("Cannot Display", ItemManager.getId(Material.BEDROCK), 0, CivColor.Rose+"Must be a town mayor or assistant build structures.");
			guiInventory.setItem(4, is);
			
			is = LoreGuiItem.build("Cannot Display", ItemManager.getId(Material.BEDROCK), 0, CivColor.Rose+"Must be a town mayor or assistant build wonders.");
			AttributeUtil attrs = new AttributeUtil(is);
			attrs.setShiny();
			is = attrs.getStack();
			guiInventory.setItem(5, is);*/
		}
		
		/* Add Information Item */
		is = LoreGuiItem.build("Information", ItemManager.getId(Material.SHIELD), 0,
				CivColor.White+"This GUI Allows for players to see",
				CivColor.White+"information on structures they own,",
				CivColor.White+"or for mayors/assistants to build a",
				CivColor.White+"structure for the town."
				);
		guiInventory.setItem((9*3)-2, is);
		
		/* Add back buttons. */
		ItemStack backButton = LoreGuiItem.build("Back", ItemManager.getId(Material.MAP), 0, "Back to Topics");
		backButton = LoreGuiItem.setAction(backButton, "OpenInventory");
		backButton = LoreGuiItem.setActionData(backButton, "invType", "showGuiInv");
		backButton = LoreGuiItem.setActionData(backButton, "invName", CivBook.guiInventory.getName());
		guiInventory.setItem((9*3)-1, backButton);
		
		LoreGuiItemListener.guiInventories.put(guiInventory.getName(), guiInventory);
		TaskMaster.syncTask(new OpenInventoryTask(player, guiInventory));
	}
}
