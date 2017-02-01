package com.avrgaming.civcraft.loregui.buildings;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.avrgaming.civcraft.book.CivBook;
import com.avrgaming.civcraft.config.CivSettings;
import com.avrgaming.civcraft.config.ConfigTownUpgrade;
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

public class BuildUpgradeList implements GuiAction {

	@Override
	public void performAction(InventoryClickEvent event, ItemStack stack) {
		Player player = (Player)event.getWhoClicked();
		Resident res = CivGlobal.getResident(player);
		Inventory guiInventory = Bukkit.getServer().createInventory(player, 9*5, "Pick Upgrade to Buy");
		
		for (ConfigTownUpgrade info : CivSettings.townUpgrades.values()) {
			int type = ItemManager.getId(Material.PRISMARINE_SHARD);
//			if (info.itemTypeId != 0) {
//				type = info.itemTypeId;
//			}
			
			ItemStack is;
			if (!res.hasTown()) {
				is = LoreGuiItem.build(info.name, ItemManager.getId(Material.BEDROCK), 0, CivColor.Rose+"Must belong to a town to purchase town upgrades.");
			} else if (!res.getTown().isMayor(res) && !res.getTown().isAssistant(res)) {
				is = LoreGuiItem.build(info.name, ItemManager.getId(Material.BEDROCK), 0, CivColor.Rose+"Must be a town mayor or assistant to purchase town upgrades.");
			} else {
				if (res.getTown().hasUpgrade(info.id)) {
				} else if (!res.getCiv().hasTechnology(info.require_tech)) {
				} else if (!info.isAvailable(res.getTown())) {
					is = LoreGuiItem.build(info.name, ItemManager.getId(Material.BARRIER), 0, CivColor.Rose+"Not available");
					guiInventory.addItem(is);
				} else {
					is = LoreGuiItem.build(info.name, type, 0, CivColor.Gold+"<Click To Upgrade>");
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
		guiInventory.setItem((9*5)-1, backButton);
		
		LoreGuiItemListener.guiInventories.put(guiInventory.getName(), guiInventory);		
		TaskMaster.syncTask(new OpenInventoryTask(player, guiInventory));
	}
}
