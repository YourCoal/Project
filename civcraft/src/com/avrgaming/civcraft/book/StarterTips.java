package com.avrgaming.civcraft.book;

import gpl.AttributeUtil;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.avrgaming.civcraft.lorestorage.LoreCraftableMaterial;
import com.avrgaming.civcraft.lorestorage.LoreGuiItem;
import com.avrgaming.civcraft.lorestorage.LoreGuiItemListener;
import com.avrgaming.civcraft.lorestorage.LoreMaterial;
import com.avrgaming.civcraft.util.CivColor;
import com.avrgaming.civcraft.util.ItemManager;

public class StarterTips {

	public static Inventory cooldownsInventory = null;
	public static Inventory guiInventory = null;
	public static final int MAX_CHEST_SIZE = 6;
	
	public static void showCooldownsInventory(Player player) {	
		if (cooldownsInventory == null) {
			cooldownsInventory = Bukkit.getServer().createInventory(player, 9*3, "CivCraft Cooldowns");
			
			cooldownsInventory.addItem(LoreGuiItem.build(CivColor.LightBlue+ChatColor.BOLD+"PvP Timer", ItemManager.getId(Material.WOOD_SWORD), 59, 
					ChatColor.RESET+"When you first spawn in, you will be given 150 minutes",
					ChatColor.RESET+"PvP free, meaning that you cannot attack players, and",
					ChatColor.RESET+"players will not be able to you too. If you with to",
					ChatColor.RESET+"disable this, you can execute the following command:",
					ChatColor.RESET+"",
					ChatColor.RESET+CivColor.Rose+CivColor.BOLD+"/res pvptimer disable"
					));
			
			cooldownsInventory.addItem(LoreGuiItem.build(CivColor.LightBlue+ChatColor.BOLD+"PvE Timer", ItemManager.getId(Material.SKULL_ITEM), 2, 
					ChatColor.RESET+"When you first spawn in, you will be given 300 minutes",
					ChatColor.RESET+"PvE free, meaning that you cannot attack monsters, and",
					ChatColor.RESET+"monsters will not be able to you too. If you with to",
					ChatColor.RESET+"disable this, you can execute the following command:",
					ChatColor.RESET+"",
//					ChatColor.RESET+CivColor.Rose+CivColor.BOLD+"/res pvptimer disable"
					ChatColor.RESET+CivColor.Rose+CivColor.BOLD+"COMMAND COMING SOON"				
					));
			
			cooldownsInventory.setItem(9, LoreGuiItem.build(CivColor.LightBlue+ChatColor.BOLD+"Camp Timer", ItemManager.getId(LoreMaterial.spawn(LoreMaterial.materialMap.get("mat_found_camp"))), 0, 
					ChatColor.RESET+"When you first spawn in, cannot craft or found a camp",
					ChatColor.RESET+"for the first 2 hours. This is to encourage you to",
					ChatColor.RESET+"explore the lands before you try to just sit in the",
					ChatColor.RESET+"worst spot you could ever find."
					));
			cooldownsInventory.setItem(10, getInfoBookForItem("mat_found_camp"));
			
			//TODO Civilizations
			cooldownsInventory.setItem(18, LoreGuiItem.build(CivColor.LightBlue+ChatColor.BOLD+"Civilization Timer", ItemManager.getId(LoreMaterial.spawn(LoreMaterial.materialMap.get("mat_found_civ"))), 0, 
					ChatColor.RESET+"When you first spawn in, cannot craft or found a civ",
					ChatColor.RESET+"for the first 26 hours. This is to, first, make sure",
					ChatColor.RESET+"you get a camp first. Second, we want you to adventure",
					ChatColor.RESET+"and collect resources before you never move again."
					));
			cooldownsInventory.setItem(19, getInfoBookForItem("mat_found_civ"));
			
			LoreGuiItemListener.guiInventories.put(cooldownsInventory.getName(), cooldownsInventory);
		}
		
		if (player != null && player.isOnline() && player.isValid()) {
			player.openInventory(cooldownsInventory);	
		}
	}
	
	public static ItemStack getInfoBookForItem(String matID) {
		LoreCraftableMaterial loreMat = LoreCraftableMaterial.getCraftMaterialFromId(matID);
		ItemStack stack = LoreMaterial.spawn(loreMat);
		if (!loreMat.isCraftable()) {
			return null;
		}
		
		AttributeUtil attrs = new AttributeUtil(stack);
		attrs.removeAll(); /* Remove all attribute modifiers to prevent them from displaying */		
		stack = attrs.getStack();
		return stack;
	}
	
	public static void spawnGuiBook(Player player) {
		if (guiInventory == null) {
			guiInventory = Bukkit.getServer().createInventory(player, 3*9, "CivCraft Information");
			
			ItemStack infoRec = LoreGuiItem.build("New Cooldowns!",
					ItemManager.getId(Material.WRITTEN_BOOK), 0, CivColor.Gold+"<Click To View>");
			infoRec = LoreGuiItem.setAction(infoRec, "OpenInventory");
			infoRec = LoreGuiItem.setActionData(infoRec, "invType", "showCooldownsInventory");
			guiInventory.addItem(infoRec);
			
			LoreGuiItemListener.guiInventories.put(guiInventory.getName(), guiInventory);
		}
		player.openInventory(guiInventory);
	}
}
