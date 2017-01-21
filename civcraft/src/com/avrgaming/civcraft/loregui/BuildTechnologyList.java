package com.avrgaming.civcraft.loregui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.avrgaming.civcraft.book.CivBook;
import com.avrgaming.civcraft.config.CivSettings;
import com.avrgaming.civcraft.config.ConfigTech;
import com.avrgaming.civcraft.lorestorage.LoreGuiItem;
import com.avrgaming.civcraft.lorestorage.LoreGuiItemListener;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.threading.TaskMaster;
import com.avrgaming.civcraft.util.CivColor;
import com.avrgaming.civcraft.util.ItemManager;

import gpl.AttributeUtil;

public class BuildTechnologyList implements GuiAction {

	@Override
	public void performAction(InventoryClickEvent event, ItemStack stack) {
		Player player = (Player)event.getWhoClicked();
		Resident res = CivGlobal.getResident(player);
		Inventory guiInventory = Bukkit.getServer().createInventory(player, 9*2, "Pick Technology to Research");
		
		for (ConfigTech info : CivSettings.techs.values()) {
			int type = ItemManager.getId(Material.PAPER);
//			if (info.itemTypeId != 0) {
//				type = info.itemTypeId;
//			}
			
			ItemStack is;
			if (!res.hasTown()) {
				is = LoreGuiItem.build(info.name, ItemManager.getId(Material.BEDROCK), 0, CivColor.Rose+"Must belong to a civ to research technology.");
			} else if (!res.getCiv().getLeaderGroup().hasMember(res) && !res.getCiv().getAdviserGroup().hasMember(res)) {
				is = LoreGuiItem.build(info.name, ItemManager.getId(Material.BEDROCK), 0, CivColor.Rose+"Must be a civilization leader or adviser to research.");
			} else {
				if (res.getCiv().hasTech(info.id)) {
				} else if (!res.getCiv().hasTechnology(info.require_techs)) {
				} else if (res.getCiv().getResearchTech() != null) {
					is = LoreGuiItem.build(info.name, ItemManager.getId(Material.WATER_BUCKET), 0, CivColor.Rose+"Already researching "+res.getCiv().getResearchTech().name);
//					is = LoreGuiItem.build(info.name, ItemManager.getId(Material.WATER_BUCKET), 0, CivColor.Rose+"Already researching "+res.getCiv().getResearchTech().name);
					guiInventory.addItem(is);
				} else if (!info.isAvailable(res.getCiv())) {
					is = LoreGuiItem.build(info.name, ItemManager.getId(Material.BARRIER), 0, CivColor.Rose+"Not available");
					guiInventory.addItem(is);
				} else {
					is = LoreGuiItem.build(info.name, type, 0, CivColor.Gold+"<Click To Research>");
					is = LoreGuiItem.setAction(is, "ResearchChooseTech");
					is = LoreGuiItem.setActionData(is, "info", info.id);
						AttributeUtil attrs = new AttributeUtil(is);
						attrs.setShiny();
					is = attrs.getStack();
					guiInventory.addItem(is);
				}
			}
		}
		
		/* Add back buttons. */
		ItemStack backButton = LoreGuiItem.build("Back", ItemManager.getId(Material.MAP), 0, "Back to Topics");
		backButton = LoreGuiItem.setAction(backButton, "OpenInventory");
		backButton = LoreGuiItem.setActionData(backButton, "invType", "showGuiInv");
		backButton = LoreGuiItem.setActionData(backButton, "invName", CivBook.guiInventory.getName());
		guiInventory.setItem((9*2)-1, backButton);
		
		LoreGuiItemListener.guiInventories.put(guiInventory.getName(), guiInventory);		
		TaskMaster.syncTask(new OpenInventoryTask(player, guiInventory));
	}
}
