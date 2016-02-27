package com.avrgaming.civcraft.structure.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.avrgaming.civcraft.lorestorage.LoreGuiItem;
import com.avrgaming.civcraft.lorestorage.LoreGuiItemListener;
import com.avrgaming.civcraft.util.CivColor;
import com.avrgaming.civcraft.util.ItemManager;

public class StructureGUI {
	
	public static Inventory trommel = null;
	public static Inventory granary = null;
	public static Inventory guiInventory = null;
	public static final int MAX_CHEST_SIZE = 6;
	
	public static void viewTrommel(Player player) {
//		Resident resident = CivGlobal.getResident(player.getName());
		if (trommel == null) {
			trommel = Bukkit.getServer().createInventory(player, 9*2, "Trommel Information");
			
			trommel.setItem(2, LoreGuiItem.build(CivColor.LightBlue+CivColor.BOLD+"Function", ItemManager.getId(Material.FURNACE), 0, 
				CivColor.RESET+"The trommel is a structure that allows players to deposit stones",
				CivColor.RESET+"and other blocks to get an outcome of ingots and other special",
				CivColor.RESET+"items. You can upgrade the trommel to allow a more variety of",
				CivColor.RESET+"blocks to input, or increase output chance of valuables."
			));
			
//			if (resident.getTown() == null || resident.hasCamp()) {
//				trommel.setItem(6, LoreGuiItem.build(CivColor.LightBlue+CivColor.BOLD+"Current Levels (for your town)", ItemManager.getId(Material.CHEST), 0, 
//					CivColor.LightGray+CivColor.BOLD+"Block Level:",
//					CivColor.Yellow+CivColor.ITALIC+"You are not in a town.",
//					CivColor.LightGray+CivColor.BOLD+"Bonus Level:",
//					CivColor.Yellow+CivColor.ITALIC+"You are not in a town."
//				));
//				//XXX Made for something like town hall to get treasury
//			} else if (!resident.getTown().isMayor(resident)) {
//				trommel.setItem(6, LoreGuiItem.build(CivColor.LightBlue+CivColor.BOLD+"Current Levels (for your town)", ItemManager.getId(Material.CHEST), 0, 
//					CivColor.LightGray+CivColor.BOLD+"Block Level:",
//					CivColor.Yellow+CivColor.ITALIC+"Must be Mayor",
//					CivColor.LightGray+CivColor.BOLD+"Bonus Level:",
//					CivColor.Yellow+CivColor.ITALIC+"Must be Mayor"
//				));
//			} else {
		//		trommel.setItem(6, LoreGuiItem.build(CivColor.LightBlue+CivColor.BOLD+"Current Levels (for your town)", ItemManager.getId(Material.CHEST), 0, 
		//			CivColor.LightGray+CivColor.BOLD+"Block Level:",
		//			CivColor.Yellow+CivColor.ITALIC+Trommel.getBlockLevel(),
		//			CivColor.LightGray+CivColor.BOLD+"Bonus Level:",
		//			CivColor.Yellow+CivColor.ITALIC+Trommel.getBonusLevel()
		//		));
//			}
			
			trommel.setItem(9, LoreGuiItem.build(CivColor.LightBlue+CivColor.BOLD+"Block Level", ItemManager.getId(Material.STONE), 0, 
					CivColor.RESET+"The trommel's block level is what can be upgraded to allow for",
					CivColor.RESET+"more types of blocks to become ingots and more useless blocks.",
					CivColor.RESET+"Level 1: Cobblestone And Regular Stone",
					CivColor.RESET+"Level 2: Stone Brick (Reg, Moss, Cracked, Chiseled)",
					CivColor.RESET+"Level 3: Regular Granite, Diorite, Andesite",
					CivColor.RESET+"Level 4: Polished Granite, Diorite, Andesite",
					CivColor.RESET+"Level 5: Dirt, Grass, and Gravel",
					CivColor.RESET+"Level 6: Netherrack and End Stone",
					CivColor.RESET+"Level 7: Iron Block",
					CivColor.RESET+"Level 8: Gold Block",
					CivColor.RESET+"Level 9: Diamond Block",
					CivColor.RESET+"Level 10: Emerald Block"
				));
			
			trommel.setItem(10, LoreGuiItem.build(CivColor.LightBlue+CivColor.BOLD+"Bonus Level", ItemManager.getId(Material.BEACON), 0, 
					CivColor.RESET+"The trommel's bonus level is what can be upgraded to allow for",
					CivColor.RESET+"the ourput of valuables to have an increased chance.",
					CivColor.RESET+"Level 1: Default 100%",
					CivColor.RESET+"Level 2: 115%",
					CivColor.RESET+"Level 3: 130%",
					CivColor.RESET+"Level 4: 140%",
					CivColor.RESET+"Level 5: 150%",
					CivColor.RESET+"Level 6: 160%",
					CivColor.RESET+"Level 7: 170%",
					CivColor.RESET+"Level 8: 180%",
					CivColor.RESET+"Level 9: 190%",
					CivColor.RESET+"Level 10: 200%"
				));
			
			LoreGuiItemListener.guiInventories.put(trommel.getName(), trommel);
		}
		
		if (player != null && player.isOnline() && player.isValid()) {
			player.openInventory(trommel);	
		}
	}
	
	//XXX To make this a thing, so you can carry around with you?
	public static void spawnGuiBook(Player player) {
		if (guiInventory == null) {
			guiInventory = Bukkit.getServer().createInventory(player, 2*9, "Structure Information");

			ItemStack infoRec = LoreGuiItem.build("Trommel",  ItemManager.getId(Material.COBBLESTONE), 
						0, CivColor.Gold+"<Click To View>");
			infoRec = LoreGuiItem.setAction(infoRec, "OpenStructureInventory");
			infoRec = LoreGuiItem.setActionData(infoRec, "invType", "viewTrommel");
			guiInventory.addItem(infoRec);
			
			LoreGuiItemListener.guiInventories.put(guiInventory.getName(), guiInventory);
		}
		player.openInventory(guiInventory);
	}
}
