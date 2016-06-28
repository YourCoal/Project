package com.avrgaming.civcraft.main;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.object.Town;
import com.avrgaming.civcraft.structure.ScoutShip;
import com.avrgaming.civcraft.structure.ScoutTower;
import com.avrgaming.civcraft.structure.Structure;

public class CivInventory implements Listener {
	
	@EventHandler
	public void onInventoryClick_TownToggle(InventoryClickEvent event) {
		Inventory inv = event.getInventory();
		if (!inv.getTitle().equals("Scout Rate"))
			return;
		
		if (!(event.getWhoClicked() instanceof Player))
			return;
		
		Player player = (Player) event.getWhoClicked();
		Resident res = CivGlobal.getResident(player);
		ItemStack item = event.getCurrentItem();
		
		if (item.getType() == Material.IRON_BLOCK) {
			Town town = res.getSelectedTown();
			for (Structure struct : town.getStructures()) {
				if (struct instanceof ScoutTower) {
					((ScoutTower)struct).setReportSeconds(10);
				} else if (struct instanceof ScoutShip) {
					((ScoutShip)struct).setReportSeconds(10);
				}
			}
		}
		
		if (item.getType() == Material.GOLD_BLOCK) {
			Town town = res.getSelectedTown();
			for (Structure struct : town.getStructures()) {
				if (struct instanceof ScoutTower) {
					((ScoutTower)struct).setReportSeconds(30);
				} else if (struct instanceof ScoutShip) {
					((ScoutShip)struct).setReportSeconds(30);
				}
			}
		}
		event.setCancelled(true);
		player.closeInventory();
	}
	
	public static ItemStack nameItem(ItemStack item, String name) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack nameItem(Material item, String name) {
		return nameItem(new ItemStack(item), name);
	}
}
