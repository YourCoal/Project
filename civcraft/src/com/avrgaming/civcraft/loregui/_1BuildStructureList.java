package com.avrgaming.civcraft.loregui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.avrgaming.civcraft.config.CivSettings;
import com.avrgaming.civcraft.config.ConfigBuildableInfo;
import com.avrgaming.civcraft.config.ConfigTech;
import com.avrgaming.civcraft.lorestorage.LoreGuiItem;
import com.avrgaming.civcraft.lorestorage.LoreGuiItemListener;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.threading.TaskMaster;
import com.avrgaming.civcraft.util.CivColor;
import com.avrgaming.civcraft.util.ItemManager;

public class _1BuildStructureList implements GuiAction {

	@Override
	public void performAction(InventoryClickEvent event, ItemStack stack) {
		Player player = (Player)event.getWhoClicked();
		Resident res = CivGlobal.getResident(player);
		Inventory guiInventory = Bukkit.getServer().createInventory(player,9*6, "Pick Structure To Build");
		
		for (ConfigBuildableInfo info : CivSettings.structures.values()) {
			int type = ItemManager.getId(Material.ANVIL);
			int data = info.itemData;
			if (info.itemId != 0) {
				type = info.itemId;
			}
			
			ItemStack is;
			if (!res.hasTown()) {
				is = LoreGuiItem.build(info.displayName, ItemManager.getId(Material.BEDROCK), 0, CivColor.Rose+"Must belong to a town to view structure info.");
				guiInventory.addItem(is);
			} else if (!res.getTown().isMayor(res) && !res.getTown().isAssistant(res)) {
				is = LoreGuiItem.build(info.displayName, ItemManager.getId(Material.BEDROCK), 0, CivColor.Rose+"Must be a town mayor or assistant build structures.");
				guiInventory.setItem(info.position, is);
			} else {
				if (!res.getCiv().hasTechnology(info.require_tech)) {
					ConfigTech tech = CivSettings.techs.get(info.require_tech);
					is = LoreGuiItem.build(info.displayName, ItemManager.getId(Material.PAPER), 0, CivColor.Rose+"Requires: "+tech.name);
					guiInventory.setItem(info.position, is);
				} else if (res.getTown().getStructureTypeCount(info.id) >= info.limit && info.limit != 0) {
					is = LoreGuiItem.build(info.displayName, ItemManager.getId(Material.BARRIER), 0, CivColor.Rose+"Max Limit ("+info.limit+"/"+info.limit+")");
					guiInventory.setItem(info.position, is);
				} else if (!info.isAvailable(res.getTown())) {
					is = LoreGuiItem.build(info.displayName, ItemManager.getId(Material.BARRIER), 0, CivColor.Rose+"Not available", "Other Reason");
					guiInventory.setItem(info.position, is);
				} else {
					is = LoreGuiItem.build(info.displayName, type, data, CivColor.Gold+"<Click To Build>");
					is = LoreGuiItem.setAction(is, "_1BuildChooseStructureTemplate");
					is = LoreGuiItem.setActionData(is, "info", info.id);
//						AttributeUtil attrs = new AttributeUtil(is);
//						attrs.setShiny();
//					is = attrs.getStack();
					guiInventory.setItem(info.position, is);
				}
			}
		}
		
		/* Add back buttons. */
		ItemStack backButton = LoreGuiItem.build("Back", ItemManager.getId(Material.MAP), 0, "Back to Topics");
		backButton = LoreGuiItem.setAction(backButton, "OpenInventory");
		backButton = LoreGuiItem.setActionData(backButton, "invType", "showGuiInv");
		backButton = LoreGuiItem.setActionData(backButton, "invName", _1BuildMenuList.guiInventory.getName());
		guiInventory.setItem((9*6)-1, backButton);
		
		LoreGuiItemListener.guiInventories.put(guiInventory.getName(), guiInventory);
		TaskMaster.syncTask(new OpenInventoryTask(player, guiInventory));
	}
}
