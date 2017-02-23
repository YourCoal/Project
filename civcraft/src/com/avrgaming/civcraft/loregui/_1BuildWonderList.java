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
import com.avrgaming.civcraft.loregui.GuiAction;
import com.avrgaming.civcraft.loregui.OpenInventoryTask;
import com.avrgaming.civcraft.lorestorage.LoreGuiItem;
import com.avrgaming.civcraft.lorestorage.LoreGuiItemListener;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.structure.wonders.Wonder;
import com.avrgaming.civcraft.threading.TaskMaster;
import com.avrgaming.civcraft.util.CivColor;
import com.avrgaming.civcraft.util.ItemManager;

import gpl.AttributeUtil;

public class _1BuildWonderList implements GuiAction {

	@Override
	public void performAction(InventoryClickEvent event, ItemStack stack) {
		Player player = (Player)event.getWhoClicked();
		Resident res = CivGlobal.getResident(player);
		Inventory guiInventory = Bukkit.getServer().createInventory(player,9*5, "Pick Wonder To Build");
		
		for (ConfigBuildableInfo info : CivSettings.wonders.values()) {
			int type = ItemManager.getId(Material.ANVIL);
			int data = info.itemData;
			if (info.itemId != 0) {
				type = info.itemId;
			}
			
			ItemStack is;
			if (!res.hasTown()) {
				is = LoreGuiItem.build(info.displayName, ItemManager.getId(Material.BEDROCK), 0, CivColor.Rose+"Must belong to a town to build a wonder.");
				guiInventory.addItem(is);
			} else if (!res.getTown().isMayor(res) && !res.getTown().isAssistant(res)) {
				is = LoreGuiItem.build(info.displayName, ItemManager.getId(Material.BEDROCK), 0, CivColor.Rose+"Must be a town mayor or assistant build wonders.");
				guiInventory.addItem(is);
			} else {
				if (!res.getCiv().hasTechnology(info.require_tech)) {
					ConfigTech tech = CivSettings.techs.get(info.require_tech);
					is = LoreGuiItem.build(info.displayName, ItemManager.getId(Material.PAPER), 0, CivColor.Rose+"Requires: "+tech.name);
					guiInventory.addItem(is);
				} else if (!info.isAvailable(res.getTown())) {
					is = LoreGuiItem.build(info.displayName, ItemManager.getId(Material.BARRIER), 0, CivColor.Rose+"Not available");
					guiInventory.addItem(is);
				} else if (!Wonder.isWonderAvailable(info.id)) {
					is = LoreGuiItem.build(info.displayName, ItemManager.getId(Material.BEACON), 0, CivColor.Rose+"This wonder is already built.");
					guiInventory.addItem(is);
				} else {
					is = LoreGuiItem.build(info.displayName, type, data, CivColor.Gold+"<Click To Build>");
					is = LoreGuiItem.setAction(is, "_1BuildChooseWonderTemplate");
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
		backButton = LoreGuiItem.setActionData(backButton, "invName", _1BuildMenuList.guiInventory.getName());
		guiInventory.setItem((9*5)-1, backButton);
		
		LoreGuiItemListener.guiInventories.put(guiInventory.getName(), guiInventory);
		TaskMaster.syncTask(new OpenInventoryTask(player, guiInventory));
	}
}
