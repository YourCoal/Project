package com.avrgaming.civcraft.loregui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.avrgaming.civcraft.book.CivBook;
import com.avrgaming.civcraft.config.CivSettings;
import com.avrgaming.civcraft.config.ConfigNewspaper;
import com.avrgaming.civcraft.lorestorage.LoreGuiItem;
import com.avrgaming.civcraft.lorestorage.LoreGuiItemListener;
import com.avrgaming.civcraft.threading.TaskMaster;
import com.avrgaming.civcraft.util.CivColor;
import com.avrgaming.civcraft.util.ItemManager;

public class NewspaperInventory implements GuiAction {
	
	static Inventory guiInventory;
	
	public String[] messages(String... messages) {
		return messages;
	}
	
	@Override
	public void performAction(InventoryClickEvent event, ItemStack stack) {
		Player player = (Player)event.getWhoClicked();
		guiInventory = Bukkit.getServer().createInventory(player,9*3, "Daily News");
		
		for (int i = 0; i < 27; i++) {
			ItemStack is = LoreGuiItem.build("", ItemManager.getId(Material.STAINED_GLASS_PANE), 8);
			guiInventory.setItem(i, is);
		}
		
		for (ConfigNewspaper news : CivSettings.newspapers.values()) {
			ItemStack is;
			is = LoreGuiItem.build(CivColor.BOLD+news.headline+" "+CivColor.BOLD+news.lineotd, news.item, news.iData, CivColor.LightGrayItalic+news.date,
					CivColor.LightGreenBold+"Civilization:", CivColor.White+news.line1, CivColor.White+news.line2, CivColor.White+news.line3,
					CivColor.RoseBold+"Political:", CivColor.White+news.line4, CivColor.White+news.line5, CivColor.White+news.line6,
					CivColor.LightPurpleBold+"Victory:", CivColor.White+news.line7, CivColor.White+news.line8, CivColor.White+news.line9);
			guiInventory.setItem(news.guiData, is);
		}
		
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
