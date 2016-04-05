package com.avrgaming.civcraft.object.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.avrgaming.civcraft.command.CommandBase;
import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.lorestorage.LoreGuiItemListener;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.Town;
import com.avrgaming.civcraft.structure.ScoutTower;
import com.avrgaming.civcraft.structure.Structure;

public class TownScoutRate extends CommandBase {

	public static Inventory tutorialInventory = null;
	public static Inventory guiInventory = null;
	public static final int MAX_CHEST_SIZE = 6;
	
	public static void showInventory(Player player) {	
		if (tutorialInventory == null) {
			tutorialInventory = Bukkit.getServer().createInventory(player, 9*2, "Town Set Scoutrate");
			
//			tutorialInventory.setItem(8, LoreGuiItem.build(CivColor.LightBlue+ChatColor.BOLD+"More Info?", ItemManager.getId(Material.BOOK_AND_QUILL), 0, 
//				ChatColor.RESET+"There is much more information you will require for your",
//				ChatColor.RESET+"journey into CivCraft. Please visit the wiki at ",
//				ChatColor.RESET+CivColor.LightGreen+ChatColor.BOLD+"http://civcraft.net/wiki",
//				ChatColor.RESET+"For more detailed information about CivCraft and it's features."
//			));
			
			ItemStack survival = new ItemStack (Material.GRILLED_PORK);
			ItemMeta survivalMeta = survival.getItemMeta();
			ItemStack creative = new ItemStack (Material.GOLDEN_APPLE);
			ItemMeta creativeMeta = creative.getItemMeta();
			
			survivalMeta.setDisplayName(ChatColor.DARK_RED + "Survival");
			survival.setItemMeta(survivalMeta);
			
			creativeMeta.setDisplayName(ChatColor.DARK_GREEN + "Creative");
			creative.setItemMeta(creativeMeta);
			
			tutorialInventory.setItem(3, survival);
			tutorialInventory.setItem(5, creative);
			
			LoreGuiItemListener.guiInventories.put(tutorialInventory.getName(), tutorialInventory);
		}
		
		if (player != null && player.isOnline() && player.isValid()) {
			player.openInventory(tutorialInventory);	
		}
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) throws CivException {
		Player player = (Player) event.getWhoClicked();
		event.setCancelled(true);
//		if(event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR || !event.getCurrentItem().hasItemMeta()) {
//			player.closeInventory();
//			return;
//		}
		
		Town town = getSelectedTown();
		switch(event.getCurrentItem().getType()) {
		case GRILLED_PORK:
			CivMessage.send(player, "...");
			for (Structure struct : town.getStructures()) {
				if (struct instanceof ScoutTower) {
					((ScoutTower)struct).setReportSeconds(10);
				}
			}
			player.closeInventory();
			CivMessage.sendSuccess(player, "Set scout tower report interval to 10 seconds.");
			break;
			
			
		case GOLDEN_APPLE:
			CivMessage.send(player, "...");
			for (Structure struct : town.getStructures()) {
				if (struct instanceof ScoutTower) {
					((ScoutTower)struct).setReportSeconds(30);
				}
			}
			player.closeInventory();
			CivMessage.sendSuccess(player, "Set scout tower report interval to 30 seconds.");
			break;
			default:
			player.closeInventory();
			break;
		}
	}
	
	@Override
	public void init() {
	}
	@Override
	public void doDefaultAction() throws CivException {
	}
	@Override
	public void showHelp() {
	}
	@Override
	public void permissionCheck() throws CivException {
	}
	
//	public static void spawnGuiBook(Player player) {
//		if (guiInventory == null) {
//			guiInventory = Bukkit.getServer().createInventory(player, 3*9, "CivCraft Information");
//			
//			ItemStack infoRec = LoreGuiItem.build("CivCraft Info", ItemManager.getId(Material.WRITTEN_BOOK), 0, CivColor.Gold+"<Click To View>");
//			infoRec = LoreGuiItem.setAction(infoRec, "OpenInventory");
//			infoRec = LoreGuiItem.setActionData(infoRec, "invType", "showInventory");
//			guiInventory.addItem(infoRec);
//			
//			LoreGuiItemListener.guiInventories.put(guiInventory.getName(), guiInventory);
//		}
//		player.openInventory(guiInventory);
//	}
}
