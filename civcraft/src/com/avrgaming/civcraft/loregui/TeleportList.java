package com.avrgaming.civcraft.loregui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.avrgaming.civcraft.lorestorage.LoreGuiItem;
import com.avrgaming.civcraft.lorestorage.LoreGuiItemListener;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.object.Town;
import com.avrgaming.civcraft.threading.TaskMaster;
import com.avrgaming.civcraft.util.CivColor;
import com.avrgaming.civcraft.util.ItemManager;

public class TeleportList implements GuiAction {
	
	@Override
	public void performAction(InventoryClickEvent event, ItemStack stack) {
		Player player = (Player)event.getWhoClicked();
		Resident res = CivGlobal.getResident(player);
		Inventory guiInventory = Bukkit.getServer().createInventory(player, LoreGuiItem.MAX_INV_SIZE, "Pick a Teleport Location");
		for (Town t : CivGlobal.getTowns()) {
			
			ItemStack is;
			String denied = t.getName()+CivColor.BoldRed+" [DENIED]";
			String allowed = t.getName()+CivColor.BoldGreen+" [ALLOWED]";
			if (!res.hasTown()) {
				is = LoreGuiItem.build(denied, ItemManager.getId(Material.BEDROCK), 0,
						CivColor.Rose+"You must belong to a town to teleport.");
			} else {
				int cost = 1000;
				if (!res.getTreasury().hasEnough(cost)) {
					is = LoreGuiItem.build(denied, ItemManager.getId(Material.EMERALD), 0,
							CivColor.Rose+"You do not have the required amount of coins ["+cost+"] to teleport.");
				} else {
					is = LoreGuiItem.build(allowed, ItemManager.getId(Material.BEACON), 0,
							CivColor.LightGreen+"You can possibly teleport here!");
					is = LoreGuiItem.setAction(is, "OpenInventory");
					is = LoreGuiItem.setActionData(is, "invType", "showCraftingHelp");
				}
			}
			guiInventory.addItem(is);
		}
		LoreGuiItemListener.guiInventories.put(guiInventory.getName(), guiInventory);		
		TaskMaster.syncTask(new OpenInventoryTask(player, guiInventory));
		
		
		
		
		
	}
/*		Player player = (Player)event.getWhoClicked();
		Resident res = CivGlobal.getResident(player);
		Inventory guiInventory = Bukkit.getServer().createInventory(player, LoreGuiItem.MAX_INV_SIZE, "Pick A Teleport Location");
		
		for (Town t : CivGlobal.getTowns()) {
			ItemStack is;
			String denied = t.getName()+CivColor.BoldRed+" [DENIED]";
			String allowed = t.getName()+CivColor.BoldGreen+" [ALLOWED]";
			
			if (!res.hasTown()) {
				is = LoreGuiItem.build(denied, ItemManager.getId(Material.BARRIER), 0,
						CivColor.Rose+"Must belong to a town teleport to embassies.");
				return;
			} else {
				if (!res.getTown().getMayorGroup().hasMember(res) ||!res.getTown().getAssistantGroup().hasMember(res)
						|| !res.getCiv().getLeaderGroup().hasMember(res) || !res.getCiv().getAdviserGroup().hasMember(res)) {
					is = LoreGuiItem.build(denied, ItemManager.getId(Material.BARRIER), 0,
							CivColor.Rose+"Must be a leader or adviser of the civ, or a mayor or assistant of a town.");
					return;
				} else {
					if (!t.hasEmbassy()) {
						is = LoreGuiItem.build(denied, ItemManager.getId(Material.EYE_OF_ENDER), 0,
								CivColor.Rose+"This town does not have an embassy.");
						return;
					} else {
						if (!t.canTeleport) {
							is = LoreGuiItem.build(denied, ItemManager.getId(Material.EYE_OF_ENDER), 0,
									CivColor.Rose+"This town does not have teleportation enabled.");
							return;
						} else {
							int cost = 1000;
							if (!res.getTreasury().hasEnough(cost)) {
								is = LoreGuiItem.build(denied, ItemManager.getId(Material.EMERALD), 0,
										CivColor.Rose+"You do not have the required amount of coins ["+cost+"] to teleport.");
							} else {
								is = LoreGuiItem.build(allowed, ItemManager.getId(Material.BEACON), 0,
										CivColor.LightGreen+"You can possibly teleport here!");
//								is = LoreGuiItem.setAction(is, "TeleportCheck");
								//TODO In TeleportCheck.java, make sure they aren't hostile/war, or outlawed
								//Also, maybe make sure they're peace or allies and NOT neutral?
								//Could disable checks at war and let anyone teleport.
							}
						}
					}
				}
			}
			guiInventory.addItem(is);
		}
		LoreGuiItemListener.guiInventories.put(guiInventory.getName(), guiInventory);		
		TaskMaster.syncTask(new OpenInventoryTask(player, guiInventory));
	}*/
}
