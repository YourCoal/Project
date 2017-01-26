package com.avrgaming.civcraft.loregui;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.avrgaming.civcraft.lorestorage.LoreGuiItem;
import com.avrgaming.civcraft.lorestorage.LoreGuiItemListener;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.object.LibraryEnchantment;
import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.structure.Bank;
import com.avrgaming.civcraft.structure.Lab;
import com.avrgaming.civcraft.structure.Library;
import com.avrgaming.civcraft.structure.Mine;
import com.avrgaming.civcraft.structure.Structure;
import com.avrgaming.civcraft.threading.TaskMaster;
import com.avrgaming.civcraft.util.CivColor;
import com.avrgaming.civcraft.util.ItemManager;

public class BuildStructureInfoList implements GuiAction {

	@Override
	public void performAction(InventoryClickEvent event, ItemStack stack) {
		Player player = (Player)event.getWhoClicked();
		Resident res = CivGlobal.getResident(player);
		Inventory guiInventory = Bukkit.getServer().createInventory(player,9*5, "Your Town Structures");
		
		for (Structure s : res.getTown().getStructures()) {
			int type = ItemManager.getId(Material.ANVIL);
//			if (info.itemTypeId != 0) {
//				type = info.itemTypeId;
//			}
			
			ItemStack is;
			if (!res.hasTown()) {
				is = LoreGuiItem.build("Cannot Display", ItemManager.getId(Material.BEDROCK), 0, CivColor.Rose+"Must belong to a town to view structure info.");
				guiInventory.addItem(is);
			} else if (s instanceof Bank) {
				is = LoreGuiItem.build(s.getDisplayName(), type, 0,
						CivColor.LightPurpleBold+"Location: "+CivColor.LightBlue+"X "+s.getCenterLocation().getX()+", Y "+s.getCenterLocation().getY()+", Z "+s.getCenterLocation().getZ(),
						CivColor.GreenBold+"Level: "+CivColor.Yellow+s.getTown().saved_bank_level,
						CivColor.GreenBold+"Interest: "+CivColor.Yellow+s.getTown().saved_bank_interest_amount);
				guiInventory.addItem(is);
			} else if (s instanceof Library) {
				Library library = (Library)s;
				
				ArrayList<String> list = new ArrayList<String>();
				for (LibraryEnchantment le : library.getEnchants()) {
					String name = le.displayName;
					String lvl = " "+le.level;
					if (le.displayName.contains("Feather Falling")) {
						name = "F. Falling";
					}
					if (le.displayName.equals("Bonus Damage I")) {
						name = "B. Damage";
						lvl = " 1";
					}
					if (le.displayName.equals("Bonus Damage II")) {
						name = "B. Damage";
						lvl = " 2";
					}
					list.add(name+lvl);
				}
				
				is = LoreGuiItem.build(s.getDisplayName(), type, 0,
						CivColor.LightPurpleBold+"Location: "+CivColor.LightBlue+"X "+s.getCenterLocation().getX()+", Y "+s.getCenterLocation().getY()+", Z "+s.getCenterLocation().getZ(),
						CivColor.GreenBold+"Level: "+CivColor.Yellow+library.getLevel(),
//						CivColor.GreenBold+"Enchantments: "+CivColor.Yellow+list);
						CivColor.GreenBold+"Enchantments: ",
						CivColor.Yellow+list);
				guiInventory.addItem(is);
			} else if (s instanceof Mine) {
				Mine mine = (Mine)s;
				is = LoreGuiItem.build(s.getDisplayName(), type, 0,
						CivColor.LightPurpleBold+"Location: "+CivColor.LightBlue+"X "+s.getCenterLocation().getX()+", Y "+s.getCenterLocation().getY()+", Z "+s.getCenterLocation().getZ(),
						CivColor.GreenBold+"Level: "+CivColor.Yellow+mine.getLevel(),
						CivColor.GreenBold+"Count: "+CivColor.Yellow+mine.getCount(),
						CivColor.GreenBold+"Hammers/Hour: "+CivColor.Yellow+mine.getProducedHammers());
				guiInventory.addItem(is);
			} else if (s instanceof Lab) {
				Lab lab = (Lab)s;
				is = LoreGuiItem.build(s.getDisplayName(), type, 0,
						CivColor.LightPurpleBold+"Location: "+CivColor.LightBlue+"X "+s.getCenterLocation().getX()+", Y "+s.getCenterLocation().getY()+", Z "+s.getCenterLocation().getZ(),
						CivColor.GreenBold+"Level: "+CivColor.Yellow+lab.getLevel(),
						CivColor.GreenBold+"Count: "+CivColor.Yellow+lab.getCount(),
						CivColor.GreenBold+"Beakers/Hour: "+CivColor.Yellow+lab.getProducedBeakers());
				guiInventory.addItem(is);
			} else {
				is = LoreGuiItem.build(s.getDisplayName(), type, 0, 
						CivColor.LightPurpleBold+"Location: "+CivColor.LightBlue+"X "+s.getCenterLocation().getX()+", Y "+s.getCenterLocation().getY()+", Z "+s.getCenterLocation().getZ(),
						"");
				guiInventory.addItem(is);
			}
		}
		
		/* Add back buttons. */
		ItemStack backButton = LoreGuiItem.build("Back", ItemManager.getId(Material.MAP), 0, "Back to Topics");
		backButton = LoreGuiItem.setAction(backButton, "OpenInventory");
		backButton = LoreGuiItem.setActionData(backButton, "invType", "showGuiInv");
		backButton = LoreGuiItem.setActionData(backButton, "invName", BuildMenuList.guiInventory.getName());
		guiInventory.setItem((9*5)-1, backButton);
		
		LoreGuiItemListener.guiInventories.put(guiInventory.getName(), guiInventory);
		TaskMaster.syncTask(new OpenInventoryTask(player, guiInventory));
	}
}
