package com.avrgaming.civcraft.loregui;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.avrgaming.civcraft.loreenhancements.LoreEnhancement;
import com.avrgaming.civcraft.lorestorage.LoreGuiItem;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.util.CivColor;

public class _1ConfirmLibraryEnchant implements GuiAction {
	
	@Override
	public void performAction(InventoryClickEvent event, ItemStack stack) {
		String enchantCategory = LoreGuiItem.getActionData(stack, "enchantmentCategory");
		String enchantName = LoreGuiItem.getActionData(stack, "enchantmentname");
		String el = LoreGuiItem.getActionData(stack, "enchantmentlevel");
		Integer enchantLevel = Integer.parseInt(el);
		Player p = (Player)event.getWhoClicked();
		
		if (enchantCategory == "armour") {
			if (enchantName.contains("Projectile Protection")) {
				ItemStack item = new ItemStack(p.getInventory().getItemInMainHand().getType());
				if (!LoreEnhancement.isArmor(item)) {
					CivMessage.sendError(p, "This item cannot have this enchantment.");
					return;
				}
				p.getInventory().getItemInMainHand().addEnchantment(Enchantment.PROTECTION_PROJECTILE, enchantLevel);
				CivMessage.sendSuccess(p, "Enchanted your "+p.getInventory().getItemInMainHand().getItemMeta().getDisplayName()+" with "+enchantName+" "+enchantLevel+"!");
				event.getWhoClicked().closeInventory();
				return;
			} else if (enchantName.contains("Blast Protection")) {
				ItemStack item = new ItemStack(p.getInventory().getItemInMainHand().getType());
				if (!LoreEnhancement.isArmor(item)) {
					CivMessage.sendError(p, "This item cannot have this enchantment.");
					return;
				}
				p.getInventory().getItemInMainHand().addEnchantment(Enchantment.PROTECTION_EXPLOSIONS, enchantLevel);
				CivMessage.sendSuccess(p, "Enchanted your "+p.getInventory().getItemInMainHand().getItemMeta().getDisplayName()+" with "+enchantName+" "+enchantLevel+"!");
				event.getWhoClicked().closeInventory();
				return;
			} else if (enchantName.contains("Feather Falling")) {
				if (p.getInventory().getItemInMainHand().getType() != Material.LEATHER_BOOTS ||
						p.getInventory().getItemInMainHand().getType() != Material.GOLD_BOOTS ||
						p.getInventory().getItemInMainHand().getType() != Material.IRON_BOOTS ||
						p.getInventory().getItemInMainHand().getType() != Material.DIAMOND_BOOTS ||
						p.getInventory().getItemInMainHand().getType() != Material.CHAINMAIL_BOOTS) {
					CivMessage.sendError(p, "This item cannot have this enchantment.");
					return;
				}
				p.getInventory().getItemInMainHand().addEnchantment(Enchantment.PROTECTION_FALL, enchantLevel);
				CivMessage.sendSuccess(p, "Enchanted your "+p.getInventory().getItemInMainHand().getItemMeta().getDisplayName()+" with "+enchantName+" "+enchantLevel+"!");
				event.getWhoClicked().closeInventory();
				return;
			} else {
				CivMessage.sendError(p, "An error occured when trying to enchant this "+p.getInventory().getItemInMainHand().getItemMeta().getDisplayName()+CivColor.Rose+
							" with "+CivColor.Yellow+enchantName+CivColor.Rose+". If it continues, contact an admin. Error code GUILC001"); //GUI Library Confirm 001
				return;
			}
		} else {
			CivMessage.sendError(p, "An error occured when trying to enchant this "+p.getInventory().getItemInMainHand().getItemMeta().getDisplayName()+CivColor.Rose+
					" with "+CivColor.Yellow+enchantName+CivColor.Rose+". If it continues, contact an admin. Error code GUILC002"); //GUI Library Confirm 002
		}
	}
}
