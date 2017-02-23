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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.avrgaming.civcraft.camp.Camp;
import com.avrgaming.civcraft.command.CommandBase;
import com.avrgaming.civcraft.command.ReportChestsTask;
import com.avrgaming.civcraft.command.ReportPlayerInventoryTask;
import com.avrgaming.civcraft.config.CivSettings;
import com.avrgaming.civcraft.config.ConfigMaterial;
import com.avrgaming.civcraft.config.ConfigMaterialCategory;
import com.avrgaming.civcraft.config.ConfigUnit;
import com.avrgaming.civcraft.endgame.EndGameCondition;
import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.items.BonusGoodie;
import com.avrgaming.civcraft.listener.HolographicDisplaysListener;
import com.avrgaming.civcraft.lorestorage.LoreCraftableMaterial;
import com.avrgaming.civcraft.lorestorage.LoreGuiItem;
import com.avrgaming.civcraft.lorestorage.LoreGuiItemListener;
import com.avrgaming.civcraft.lorestorage.LoreMaterial;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.main.CivLog;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.Civilization;
import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.object.StructureSign;
import com.avrgaming.civcraft.object.Town;
import com.avrgaming.civcraft.object.TownChunk;
import com.avrgaming.civcraft.object.TradeGood;
import com.avrgaming.civcraft.sessiondb.SessionEntry;
import com.avrgaming.civcraft.structure.Structure;
import com.avrgaming.civcraft.structure.wonders.Wonder;
import com.avrgaming.civcraft.threading.TaskMaster;
import com.avrgaming.civcraft.util.ChunkCoord;
import com.avrgaming.civcraft.util.CivColor;
import com.avrgaming.civcraft.util.ItemManager;

public class AdminCommand extends CommandBase {

	@Override
	public void init() {
		command = "/ad";
		displayName = "Admin";
		
		commands.put("perm", "toggles your permission overrides, if on, ignores all plot permissions.");
		commands.put("sbperm", "Allows breaking of structure blocks");
		commands.put("cbinstantbreak", "Allows instant breaking of control blocks.");

		commands.put("recover", "Manage recovery commands");
		commands.put("server", "shows the name of this server");
		commands.put("spawnunit", "[unit-id] [town] spawn the unit with this id for this town.");

		commands.put("chestreport", "[radius] check in this radius for chests");
		commands.put("playerreport", "shows all player ender chest reports.");
		
		commands.put("civ", "Admin an individual civilization");
		commands.put("town", "Admin a town.");
		commands.put("war", "Manage war settings, turn wars off and on.... etc.");
		commands.put("lag", "Manage lag on the server by disabling expensive tasks.");	
		commands.put("camp", "Shows camp management subcommands.");
		commands.put("chat", "Manage admin chat options, tc, cc, listen etc");
		commands.put("res", "Manage resident options, settown, setcamp etc");
		commands.put("build", "Manage buildings. Demolish/repair wonders etc.");
		commands.put("items", "Opens inventory which allows you to spawn in custom items.");
		commands.put("item", "Does special things to the item in your hand.");
		commands.put("timer", "Manage timers.");
		commands.put("clearendgame", "[key] [civ] - clears this end game condition for this civ.");
		commands.put("endworld", "Starts the Apocalypse.");
		commands.put("arena", "Arena management commands.");
		commands.put("perk", "Admin perk management.");
		
		commands.put("mob", "Mob management commands.");
		commands.put("gui", "Opens book options. Type 'open' after book to get the GUI.");
		commands.put("reload", "Allows for some aspects of CivCraft to be reloaded in the server.");
		commands.put("tradeholo", "Enables all trade good holograms.");
		commands.put("test", "Literally for testing purposes.");
		commands.put("usd", "Update Structure Districts");
		commands.put("savesql", "Saves SQL Databases");
	}
	
	public void savesql_cmd() {
		try {
			for (Camp c : CivGlobal.getCamps()) {
				c.saveNow();
			}
			
			for (Civilization c : CivGlobal.getCivs()) {
				c.saveNow();
			}
			
			for (Town t : CivGlobal.getTowns()) {
				t.saveNow();
			}
			
			for (Resident r : CivGlobal.getResidents()) {
				r.saveNow();
			}
			
			for (TownChunk tc : CivGlobal.getTownChunks()) {
				tc.saveNow();
			}
			
			for (Structure s : CivGlobal.getStructures()) {
				s.saveNow();
			}
			
			for (Wonder w : CivGlobal.getWonders()) {
				w.saveNow();
			}
			
			for (TradeGood tg : CivGlobal.getTradeGoods()) {
				tg.saveNow();
			}
			
			for (BonusGoodie bg : CivGlobal.getBonusGoodies()) {
				bg.saveNow();
			}
			
			for (StructureSign ss : CivGlobal.getStructureSigns()) {
				ss.saveNow();
			}
			
			CivMessage.sendSuccess(sender, "Saved All SQL Information.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void usd_cmd() {
		for (Town t : CivGlobal.getTowns()) {
			t.updateStructureDistrictBonuses(t);
		}
		CivMessage.sendSuccess(sender, "Updated Structure Districts.");
	}
	
	public void tradeholo_cmd() {
		HolographicDisplaysListener.generateTradeGoodHolograms();
		CivMessage.sendSuccess(sender, "Updated holograms.");
	}
	
	public void test_cmd() {
		CivMessage.sendSuccess(sender, "Nothing to test right now.");
	}
	
/*	public void test_cmd() {
		try {
			String out1a = "The Civilization of [Civ Name] has acheived a [Victory Name] victory!";
			String out1b = "Please standby for more global messages.";
			
			String out2a = "Top 5 civs in score this phase:";
			String out2b = "";
			synchronized(CivGlobal.civilizationScores) {
				int i = 1;
				for (Integer score : CivGlobal.civilizationScores.descendingKeySet()) {
					out2b = i+") "+CivColor.Gold+CivGlobal.civilizationScores.get(score).getName()+CivColor.White+" - "+score+" points";
					i++;
					if (i > 5) {
						break;
					}
				}
			}
			
			String out3a = "Top 5 towns in score this phase:";
			String out3b = "";
			synchronized(CivGlobal.townScores) {
				int i = 1;
				for (Integer score : CivGlobal.townScores.descendingKeySet()) {
					out3b = i+") "+CivColor.Gold+CivGlobal.townScores.get(score).getName()+CivColor.White+" - "+score+" points";
					i++;
					if (i > 5) {
						break;
					}
				}
			}
			
			String out4a = "Top 5 richest civs in money this phase:";
			String out4b = "";
			synchronized(CivGlobal.civilizationEcon) {
				int i = 1;
				for (Integer money : CivGlobal.civilizationEcon.descendingKeySet()) {
					out4b = i+") "+CivColor.Gold+CivGlobal.civilizationEcon.get(money).getName()+CivColor.White+" - "+money+" coins";
					i++;
					if (i > 5) {
						break;
					}
				}
			}
			
			String out5a = "Top 5 richest towns in money this phase:";
			String out5b = "";
			synchronized(CivGlobal.townEcon) {
				int i = 1;
				for (Integer money : CivGlobal.townEcon.descendingKeySet()) {
					out5b = i+") "+CivColor.Gold+CivGlobal.townEcon.get(money).getName()+CivColor.White+" - "+money+" coins";
					i++;
					if (i > 5) {
						break;
					}
				}
			}
			
			String out6a = "Top 3 civs with most members this phase:";
			String out6b = "";
			synchronized(CivGlobal.civilizationMemberCount) {
				int i = 1;
				for (Integer members : CivGlobal.civilizationMemberCount.descendingKeySet()) {
					out6b = i+") "+CivColor.Gold+CivGlobal.civilizationMemberCount.get(members).getName()+CivColor.White+" - "+members+" members";
					i++;
					if (i > 3) {
						break;
					}
				}
			}
			
			String out7a = "Top 3 towns with most members this phase:";
			String out7b = "";
			synchronized(CivGlobal.townMemberCount) {
				int i = 1;
				for (Integer members : CivGlobal.townMemberCount.descendingKeySet()) {
					out7b = i+") "+CivColor.Gold+CivGlobal.townMemberCount.get(members).getName()+CivColor.White+" - "+members+" members";
					i++;
					if (i > 3) {
						break;
					}
				}
			}
			
			String out8a = "Top 5 civs with the most towns this phase:";
			String out8b = "";
			synchronized(CivGlobal.civilizationTownCount) {
				int i = 1;
				for (Integer townCount : CivGlobal.civilizationTownCount.descendingKeySet()) {
					out8b = i+") "+CivColor.Gold+CivGlobal.civilizationTownCount.get(townCount).getName()+CivColor.White+" - "+townCount+" towns";
					i++;
					if (i > 3) {
						break;
					}
				}
			}
			
			String out9a = "Top 5 teams with the most points this phase:";
			String out9b = "";
			for (int i = 0; ((i < 10) && (i < ArenaTeam.teamRankings.size())); i++) {
				ArenaTeam team = ArenaTeam.teamRankings.get(i);
				out9b = i+") "+CivColor.LightGreen+team.getName()+" - "+CivColor.White+team.getLadderPoints()+" points";
			}
			
			CivMessage.global(out1a);
			CivMessage.global(out1b);
			Thread.sleep(9000);
			CivMessage.global(out2a);
			CivMessage.global(out2b);
			Thread.sleep(9000);
			CivMessage.global(out3a);
			CivMessage.global(out3b);
			Thread.sleep(9000);
			CivMessage.global(out4a);
			CivMessage.global(out4b);
			Thread.sleep(9000);
			CivMessage.global(out5a);
			CivMessage.global(out5b);
			Thread.sleep(9000);
			CivMessage.global(out6a);
			CivMessage.global(out6b);
			Thread.sleep(9000);
			CivMessage.global(out7a);
			CivMessage.global(out7b);
			Thread.sleep(9000);
			CivMessage.global(out8a);
			CivMessage.global(out8b);
			Thread.sleep(9000);
			CivMessage.global(out9a);
			CivMessage.global(out9b);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}*/
	
	public void mob_cmd() {
		AdminMobCommand cmd = new AdminMobCommand();	
		cmd.onCommand(sender, null, "mob", this.stripArgs(args, 1));
	}
	
	public void gui_cmd() {
		AdminGUICommand cmd = new AdminGUICommand();	
		cmd.onCommand(sender, null, "gui", this.stripArgs(args, 1));
	}
	
	public void reload_cmd() {
		AdminReloadCommand cmd = new AdminReloadCommand();	
		cmd.onCommand(sender, null, "reload", this.stripArgs(args, 1));
	}
	
	public void perk_cmd() {
		AdminPerkCommand cmd = new AdminPerkCommand();	
		cmd.onCommand(sender, null, "perk", this.stripArgs(args, 1));
	}
	
	public void endworld_cmd() {
		CivGlobal.endWorld = !CivGlobal.endWorld;
		if (CivGlobal.endWorld) {			
			CivMessage.sendSuccess(sender, "It's the end of the world as we know it.");
		} else {
			CivMessage.sendSuccess(sender, "I feel fine.");
		}
	}
	
	public void clearendgame_cmd() throws CivException {
		String key = getNamedString(1, "enter key.");
		Civilization civ = getNamedCiv(2);
		
		ArrayList<SessionEntry> entries = CivGlobal.getSessionDB().lookup(key);
		if (entries.size() == 0) {
			throw new CivException("No end games by that key.");
		}
		
		for (SessionEntry entry : entries) {
			if (EndGameCondition.getCivFromSessionData(entry.value) == civ) {
				CivGlobal.getSessionDB().delete(entry.request_id, entry.key);
				CivMessage.sendSuccess(sender, "Deleted for "+civ.getName());
			}
		}		
	}
	
	public void cbinstantbreak_cmd() throws CivException {
		Resident resident = getResident();
		
		resident.setControlBlockInstantBreak(!resident.isControlBlockInstantBreak());
		CivMessage.sendSuccess(sender, "Set control block instant break:"+resident.isControlBlockInstantBreak());
	}
	
	public static Inventory spawnInventory = null; 
	public void items_cmd() throws CivException {
		Player player = getPlayer();
		
		if (spawnInventory == null) {
			spawnInventory = Bukkit.createInventory(player, LoreGuiItem.MAX_INV_SIZE, "Admin Item Spawn");
			
			/* Build the Category Inventory. */
			for (ConfigMaterialCategory cat : ConfigMaterialCategory.getCategories()) {
				ItemStack infoRec = LoreGuiItem.build(cat.name, 
						ItemManager.getId(Material.WRITTEN_BOOK), 
						0, 
						CivColor.LightBlue+cat.materials.size()+" Items",
						CivColor.Gold+"<Click To Open>");
						infoRec = LoreGuiItem.setAction(infoRec, "OpenInventory");
						infoRec = LoreGuiItem.setActionData(infoRec, "invType", "showGuiInv");
						infoRec = LoreGuiItem.setActionData(infoRec, "invName", cat.name+" Spawn");
						spawnInventory.addItem(infoRec);
						
				/* Build a new GUI Inventory. */
				Inventory inv = Bukkit.createInventory(player, LoreGuiItem.MAX_INV_SIZE, cat.name+" Spawn");
				for (ConfigMaterial mat : cat.materials.values()) {
					LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterialFromId(mat.id);
					ItemStack stack = LoreMaterial.spawn(craftMat);
					stack = LoreGuiItem.asGuiItem(stack);
					stack = LoreGuiItem.setAction(stack, "SpawnItem");
					inv.addItem(stack);
					LoreGuiItemListener.guiInventories.put(inv.getName(), inv);			
				}
			}
			

		}
		
		player.openInventory(spawnInventory);
	}
	
	public void arena_cmd() {
		AdminArenaCommand cmd = new AdminArenaCommand();	
		cmd.onCommand(sender, null, "arena", this.stripArgs(args, 1));
	}
	
	public void item_cmd() {
		AdminItemCommand cmd = new AdminItemCommand();	
		cmd.onCommand(sender, null, "camp", this.stripArgs(args, 1));
	}
	
	public void timer_cmd() {
		AdminTimerCommand cmd = new AdminTimerCommand();	
		cmd.onCommand(sender, null, "camp", this.stripArgs(args, 1));	
	}
	
	public void camp_cmd() {
		AdminCampCommand cmd = new AdminCampCommand();	
		cmd.onCommand(sender, null, "camp", this.stripArgs(args, 1));
	}
	
	public void playerreport_cmd() {
	
		LinkedList<OfflinePlayer> offplayers = new LinkedList<OfflinePlayer>();
		for (OfflinePlayer offplayer : Bukkit.getOfflinePlayers()) {
			offplayers.add(offplayer);
		}
		
		CivMessage.sendHeading(sender, "Players with Goodies");
		CivMessage.send(sender, "Processing (this may take a while)");
		TaskMaster.syncTask(new ReportPlayerInventoryTask(sender, offplayers), 0);
	}
	
	public void chestreport_cmd() throws CivException {
		Integer radius = getNamedInteger(1);
		Player player = getPlayer();
		
		LinkedList<ChunkCoord> coords = new LinkedList<ChunkCoord>();
		for (int x = -radius; x < radius; x++) {
			for (int z = -radius; z < radius; z++) {
				ChunkCoord coord = new ChunkCoord(player.getLocation());
				coord.setX(coord.getX() + x); coord.setZ(coord.getZ() + z);
				
				coords.add(coord);
			}
		}
		
		CivMessage.sendHeading(sender, "Chests with Goodies");
		CivMessage.send(sender, "Processing (this may take a while)");
		TaskMaster.syncTask(new ReportChestsTask(sender, coords), 0);	
	}
	
	public void spawnunit_cmd() throws CivException {		
		if (args.length < 2) {
			throw new CivException("Enter a unit id.");
		}
		
		ConfigUnit unit = CivSettings.units.get(args[1]);
		if (unit == null) {
			throw new CivException("No unit called "+args[1]);
		}
		
		Player player = getPlayer();
		Town town = getNamedTown(2);
		
//		if (args.length > 2) {
//			try {
//				player = CivGlobal.getPlayer(args[2]);
//			} catch (CivException e) {
//				throw new CivException("Player "+args[2]+" is not online.");
//			}
//		} else {
//			player = getPlayer();
//		}
		
		Class<?> c;
		try {
			c = Class.forName(unit.class_name);
			Method m = c.getMethod("spawn", Inventory.class, Town.class);
			m.invoke(null, player.getInventory(), town);
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException 
				| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new CivException(e.getMessage());
		}

		
		CivMessage.sendSuccess(sender, "Spawned a "+unit.name);
	}
	
	public void server_cmd() {
		CivMessage.send(sender, Bukkit.getServerName());
	}
	
	
	public void recover_cmd() {
		AdminRecoverCommand cmd = new AdminRecoverCommand();	
		cmd.onCommand(sender, null, "recover", this.stripArgs(args, 1));	
	}
	
	public void town_cmd() {
		AdminTownCommand cmd = new AdminTownCommand();	
		cmd.onCommand(sender, null, "town", this.stripArgs(args, 1));
	}
	
	public void civ_cmd() {
		AdminCivCommand cmd = new AdminCivCommand();	
		cmd.onCommand(sender, null, "civ", this.stripArgs(args, 1));
	}

	public void setfullmessage_cmd() {
		if (args.length < 2) {
			CivMessage.send(sender, "Current:"+CivGlobal.fullMessage);
			return;
		}
		
		synchronized(CivGlobal.maxPlayers) {
			CivGlobal.fullMessage = args[1];
		}
		
		CivMessage.sendSuccess(sender, "Set to:"+args[1]);
		
	}
	
	@SuppressWarnings("deprecation")
	public void unban_cmd() throws CivException {
		if (args.length < 2) {
			throw new CivException("Enter a player name to ban");
		}
		
		Resident r = CivGlobal.getResident(args[1]);
		
		OfflinePlayer offplayer = Bukkit.getOfflinePlayer(r.getUUID());
		if (offplayer != null && offplayer.isBanned()) {
			offplayer.setBanned(false);
			Resident resident = CivGlobal.getResident(offplayer.getName());
			if (resident != null) {
				resident.setBanned(false);
				resident.save();
			}
			CivMessage.sendSuccess(sender, "Unbanned "+args[1]);
		} else {
			CivMessage.sendSuccess(sender, "Couldn't find "+args[1]+" or he is not banned.");
		}
	}
	
	public void res_cmd() {
		AdminResCommand cmd = new AdminResCommand();	
		cmd.onCommand(sender, null, "war", this.stripArgs(args, 1));	}
	
	public void chat_cmd() {
		AdminChatCommand cmd = new AdminChatCommand();	
		cmd.onCommand(sender, null, "war", this.stripArgs(args, 1));
	}

	public void war_cmd() {
		AdminWarCommand cmd = new AdminWarCommand();	
		cmd.onCommand(sender, null, "war", this.stripArgs(args, 1));
	}
	
	public void lag_cmd() {
		AdminLagCommand cmd = new AdminLagCommand();	
		cmd.onCommand(sender, null, "war", this.stripArgs(args, 1));
	}
	
	public void build_cmd() {
		AdminBuildCommand cmd = new AdminBuildCommand();	
		cmd.onCommand(sender, null, "war", this.stripArgs(args, 1));
	}
	
	public void perm_cmd() throws CivException {
		Resident resident = getResident();
		
		if (resident.isPermOverride()) {
			resident.setPermOverride(false);
			CivMessage.sendSuccess(sender, "Permission override off.");
			return;
		}
		
		resident.setPermOverride(true);
		CivMessage.sendSuccess(sender, "Permission override on.");
		
	}
	
	public void sbperm_cmd() throws CivException {
		Resident resident = getResident();
		if (resident.isSBPermOverride()) {
			resident.setSBPermOverride(false);
			CivMessage.sendSuccess(sender, "Structure Permission override off.");
			return;
		}
		
		resident.setSBPermOverride(true);
		CivMessage.sendSuccess(sender, "Structure Permission override on.");
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
		
		if (sender instanceof Player) {
			if (((Player)sender).hasPermission(CivSettings.MINI_ADMIN)) {
				return;
			}
		}
		
		
		if (sender.isOp() == false) {
			throw new CivException("Only admins can use this command.");			
		}
	}

	@Override
	public void doLogging() {
		CivLog.adminlog(sender.getName(), "/ad "+this.combineArgs(args));
	}
	
}
