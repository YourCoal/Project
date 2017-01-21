/*************************************************************************
 * 
 * AVRGAMING LLC
 * __________________
 * 
 *  [2013] AVRGAMING LLC
 *  All Rights Reserved.
 * 
 * NOTICE:  All information contained herein is, and remains
 * the property of AVRGAMING LLC and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to AVRGAMING LLC
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from AVRGAMING LLC.
 */
package com.avrgaming.civcraft.command.admin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.avrgaming.civcraft.command.CommandBase;
import com.avrgaming.civcraft.config.CivSettings;
import com.avrgaming.civcraft.config.ConfigTech;
import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.Resident;

public class AdminGUICommand extends CommandBase implements Listener {

	@Override
	public void init() {
		command = "/ad gui";
		displayName = "Admin GUI";
		
		commands.put("admin", "Opens the GUI for Admins.");
	}
	
	public void admin_cmd() {
		if (!(sender instanceof Player)) {
			CivMessage.sendError(sender, "Only a player can execute this command.");
			return;
		}
		Player p = (Player)sender;
		openAdminGUI(p);
	}
	
	public static void openAdminGUI(Player p) {
		Inventory inv = Bukkit.createInventory(null, 36, "Admin Control Panel");
		
		ItemStack civMoney = new ItemStack(Material.EMERALD, 1);
		ItemMeta civMoneyMeta = civMoney.getItemMeta();
		civMoneyMeta.setDisplayName("Prep Civ Treasury");
		civMoney.setItemMeta(civMoneyMeta);
		
		ItemStack townMoney = new ItemStack(Material.DIAMOND, 1);
		ItemMeta townMoneyMeta = townMoney.getItemMeta();
		townMoneyMeta.setDisplayName("Prep Town Treasury");
		townMoney.setItemMeta(townMoneyMeta);
		
		ItemStack playerMoney = new ItemStack(Material.GOLD_INGOT, 1);
		ItemMeta playerMoneyMeta = playerMoney.getItemMeta();
		playerMoneyMeta.setDisplayName("Prep Your Treasury");
		playerMoney.setItemMeta(playerMoneyMeta);
		
		ItemStack civAllTechs = new ItemStack(Material.BEACON, 1);
		ItemMeta civAllTechsMeta = civAllTechs.getItemMeta();
		civAllTechsMeta.setDisplayName("Give Civ All Tech");
		civAllTechs.setItemMeta(civAllTechsMeta);
		
		ItemStack civBeakerRate = new ItemStack(Material.BREWING_STAND_ITEM, 1);
		ItemMeta civBeakerRateMeta = civBeakerRate.getItemMeta();
		civBeakerRateMeta.setDisplayName("Change Civ Beaker Rate");
		civBeakerRate.setItemMeta(civBeakerRateMeta);
		
		ItemStack townCulture = new ItemStack(Material.PURPUR_BLOCK, 1);
		ItemMeta townCultureMeta = townCulture.getItemMeta();
		townCultureMeta.setDisplayName("Add Town Culture");
		townCulture.setItemMeta(townCultureMeta);
		
		ItemStack townHammerRate = new ItemStack(Material.COBBLE_WALL, 1);
		ItemMeta townHammerRateMeta = townHammerRate.getItemMeta();
		townHammerRateMeta.setDisplayName("Change Town Hammer Rate");
		townHammerRate.setItemMeta(townHammerRateMeta);
		
		inv.setItem(0, civMoney);
		inv.setItem(3, townMoney);
		inv.setItem(6, playerMoney);
		
		inv.setItem(9, civAllTechs);
		inv.setItem(18, civBeakerRate);
		
		inv.setItem(12, townCulture);
		inv.setItem(21, townHammerRate);
		p.openInventory(inv);
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (!event.getInventory().getName().equalsIgnoreCase("Admin Control Panel")) {
			return;
		}
		Player p = (Player) event.getWhoClicked();
		Resident res = CivGlobal.getResident(p);
		event.setCancelled(true);
		
		if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
//			p.closeInventory();
			return;
		}
		
		switch (event.getCurrentItem().getType()) {
		case EMERALD:
			p.closeInventory();
			if (res.getCiv() == null) {
				CivMessage.sendError(p, "You are not in a civilization!");
			}
			res.getCiv().getTreasury().deposit(10000000);
			res.getCiv().save();
			CivMessage.sendSuccess(p, "Added 10 million coins to civ treasury.");
			break;
		case DIAMOND:
			p.closeInventory();
			if (res.getTown() == null) {
				CivMessage.sendError(p, "You are not in a town!");
			}
			res.getTown().getTreasury().deposit(10000000);
			res.getTown().save();
			CivMessage.sendSuccess(p, "Added 10 million coins to town treasury.");
			break;
		case GOLD_INGOT:
			p.closeInventory();
			if (res == null) {
				CivMessage.sendError(p, "You are not a resident!?");
			}
			res.getTreasury().deposit(10000000);
			res.save();
			CivMessage.sendSuccess(p, "Added 10 million coins to your treasury.");
			break;
		case BEACON:
			p.closeInventory();
			if (res.getCiv() == null) {
				CivMessage.sendError(p, "You are not in a civilization!");
			}
			for (ConfigTech tech : CivSettings.techs.values()) {
				res.getCiv().addTech(tech);
			}
			res.getCiv().save();
			CivMessage.sendSuccess(p, "Gave all techs to your civilization.");
			break;
		case BREWING_STAND_ITEM:
			p.closeInventory();
			if (res.getCiv() == null) {
				CivMessage.sendError(p, "You are not in a civilization!");
			}
			res.getCiv().setBaseBeakers(10000000);
			res.getCiv().save();
			CivMessage.sendSuccess(p, "Set beakerrate to 10 million in your civilization.");
			break;
		case PURPUR_BLOCK:
			p.closeInventory();
			if (res.getTown() == null) {
				CivMessage.sendError(p, "You are not in a town!");
			}
			res.getTown().addAccumulatedCulture(50000);
			res.getTown().save();
			CivMessage.sendSuccess(p, "Added 50,000 culture to your town.");
			break;
		case COBBLE_WALL:
			p.closeInventory();
			if (res.getTown() == null) {
				CivMessage.sendError(p, "You are not in a town!");
			}
			res.getTown().setHammerRate(10000000);
			res.getTown().save();
			CivMessage.sendSuccess(p, "Set hammerrate to 10 million in your town.");
			break;
		case AIR:
			break;
		default:
//			p.closeInventory();
			break;
		}
	}
	
	@Override
	public void doDefaultAction() throws CivException {
		showHelp();
	}
	
	@Override
	public void showHelp() {
		showBasicHelp();
	}
	
	@Override
	public void permissionCheck() throws CivException {
		//Admin is checked in parent command
	}
}
