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
import com.avrgaming.civcraft.object.LibraryEnchantment;
import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.structure.Library;
import com.avrgaming.civcraft.structure.Structure;
import com.avrgaming.civcraft.threading.TaskMaster;
import com.avrgaming.civcraft.util.CivColor;
import com.avrgaming.civcraft.util.ItemManager;

public class _1BuildLibraryEnchantList implements GuiAction {
	
	static Inventory guiInventory;
	
	//00 - 08 : Armour
	//09 - 17 : Weapon
	//18 - 26 : Bow
	//27 - 35 : Tool
	//36 - 44 : Other
	
	@Override
	public void performAction(InventoryClickEvent event, ItemStack stack) {
		Player player = (Player)event.getWhoClicked();
		Resident res = CivGlobal.getResident(player);
		guiInventory = Bukkit.getServer().createInventory(player,9*5, "Library Enchanting");
		
		for (Structure s : res.getTown().getStructures()) {
			ItemStack is;
			if (s instanceof Library) {
				Library library = (Library)s;
				for (LibraryEnchantment le : library.getEnchants()) {
					Integer level = le.level;
					String cost = ""+le.price;
/*					if (!library.getEnchants().contains("Projectile Protection")) {
						is = LoreGuiItem.build("Projectile Protection "+level, ItemManager.getId(Material.BEDROCK), 0, "   "+CivColor.RoseBold+"ENCHANTMENT LOCKED", CivColor.LightGrayItalic+"Reduces Arrow Damage.");
						guiInventory.setItem(0, is);
					} else {
						is = LoreGuiItem.build(le.displayName+" "+level, ItemManager.getId(Material.SPECTRAL_ARROW), 0, "   "+CivColor.YellowBold+cost+" Coins", CivColor.LightGrayItalic+"Reduces Arrow Damage.");
						is = LoreGuiItem.setAction(is, "_1ConfirmLibraryEnchant");
						is = LoreGuiItem.setActionData(is, "enchantmentCategory", "armour");
						is = LoreGuiItem.setActionData(is, "enchantmentname", le.displayName);
						is = LoreGuiItem.setActionData(is, "enchantmentlevel", ""+le.level);
						guiInventory.setItem(0, is);
						continue;
					}
					
					if (!library.getEnchants().contains("Blast Protection")) {
						is = LoreGuiItem.build("Blast Protection "+level, ItemManager.getId(Material.BEDROCK), 0, "   "+CivColor.RoseBold+"ENCHANTMENT LOCKED", CivColor.LightGrayItalic+"Reduces Explosion Damage.");
						guiInventory.setItem(1, is);
					} else {
						is = LoreGuiItem.build(le.displayName+" "+level, ItemManager.getId(Material.EXPLOSIVE_MINECART), 0, "   "+CivColor.YellowBold+cost+" Coins", CivColor.LightGrayItalic+"Reduces Explosion Damage.");
						is = LoreGuiItem.setAction(is, "_1ConfirmLibraryEnchant");
						is = LoreGuiItem.setActionData(is, "enchantmentCategory", "armour");
						is = LoreGuiItem.setActionData(is, "enchantmentname", le.displayName);
						is = LoreGuiItem.setActionData(is, "enchantmentlevel", ""+le.level);
						guiInventory.setItem(1, is);
						continue;
					}*/
					
					if (library.getLevel() >= 1) {
						is = LoreGuiItem.build("Feather Falling "+level, ItemManager.getId(Material.FEATHER), 0, "   "+CivColor.YellowBold+cost+" Coins", CivColor.LightGrayItalic+"Reduces Fall Damage.");
						is = LoreGuiItem.setAction(is, "_1ConfirmLibraryEnchant");
						is = LoreGuiItem.setActionData(is, "enchantmentCategory", "armour");
						is = LoreGuiItem.setActionData(is, "enchantmentname", "Feather Falling");
						is = LoreGuiItem.setActionData(is, "enchantmentlevel", ""+le.level);
						guiInventory.setItem(4, is);
					} else {
						is = LoreGuiItem.build("Feather Falling "+level, ItemManager.getId(Material.BEDROCK), 0, "   "+CivColor.RoseBold+"ENCHANTMENT LOCKED", CivColor.LightGrayItalic+"Reduces Fall Damage.");
						guiInventory.setItem(4, is);
					}
					
					if (library.getLevel() >=  2) {
						is = LoreGuiItem.build("Efficiency "+level, ItemManager.getId(Material.SUGAR), 0, "   "+CivColor.YellowBold+cost+" Coins", CivColor.LightGrayItalic+"Increase Mining Speed.");
						is = LoreGuiItem.setAction(is, "_1ConfirmLibraryEnchant");
						is = LoreGuiItem.setActionData(is, "enchantmentCategory", "tool");
						is = LoreGuiItem.setActionData(is, "enchantmentname", "Efficiency");
						is = LoreGuiItem.setActionData(is, "enchantmentlevel", ""+le.level);
						guiInventory.setItem(9, is);
					} else {
						is = LoreGuiItem.build("Efficiency "+level, ItemManager.getId(Material.BEDROCK), 0, "   "+CivColor.RoseBold+"ENCHANTMENT LOCKED", CivColor.LightGrayItalic+"Increase Mining Speed.");
						guiInventory.setItem(9, is);
					}
					
					if (library.getLevel() >= 3) {
						is = LoreGuiItem.build("Fortune "+level, ItemManager.getId(Material.APPLE), 0, "   "+CivColor.YellowBold+cost+" Coins", CivColor.LightGrayItalic+"Increase Ore Drop Chance.");
						is = LoreGuiItem.setAction(is, "_1ConfirmLibraryEnchant");
						is = LoreGuiItem.setActionData(is, "enchantmentCategory", "tool");
						is = LoreGuiItem.setActionData(is, "enchantmentname", "Fortune");
						is = LoreGuiItem.setActionData(is, "enchantmentlevel", ""+le.level);
						guiInventory.setItem(11, is);
					} else {
						is = LoreGuiItem.build("Fortune "+level, ItemManager.getId(Material.BEDROCK), 0, "   "+CivColor.RoseBold+"ENCHANTMENT LOCKED", CivColor.LightGrayItalic+"Increase Ore Drop Chance.");
						guiInventory.setItem(11, is);
					}
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
