package com.avrgaming.civcraft.war;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import com.avrgaming.civcraft.camp.WarCamp;
import com.avrgaming.civcraft.config.CivSettings;
import com.avrgaming.civcraft.endgame.EndGameCondition;
import com.avrgaming.civcraft.event.EventTimer;
import com.avrgaming.civcraft.event.ToggleCommandsEvent;
import com.avrgaming.civcraft.exception.InvalidConfiguration;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.main.CivLog;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.Civilization;
import com.avrgaming.civcraft.object.Relation;
import com.avrgaming.civcraft.object.Relation.Status;
import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.object.Town;
import com.avrgaming.civcraft.sessiondb.SessionEntry;
import com.avrgaming.civcraft.siege.Cannon;
import com.avrgaming.civcraft.util.CivColor;
import com.avrgaming.global.perks.PlatinumManager;

public class War {

	/* If true, WarTime is on. */
	private static boolean warTime;

	private static Date start = null;
	private static Date end = null;
	
	private static boolean onlyWarriors = false;
	
	private static HashMap<String, Civilization> defeatedTowns = new HashMap<String, Civilization>();
	private static HashMap<String, Civilization> defeatedCivs = new HashMap<String, Civilization>();
	
	public static void saveDefeatedTown(String townName, Civilization master) {
		defeatedTowns.put(townName, master);
		/* Save in the SessionDB just in case the server goes down. */
		String key = "capturedTown";
		String value = townName+":"+master.getId();
		
		CivGlobal.getSessionDB().add(key, value, master.getId(), 0, 0);
	}
	
	public static void saveDefeatedCiv(Civilization defeated, Civilization master) {
		defeatedCivs.put(defeated.getName(), master);
		/* Save in the SessionDB just in case the server goes down. */
		String key = "capturedCiv";
		String value = defeated.getName()+":"+master.getId();
		
		for (Town town : master.getTowns()) {
			for (Resident resident : town.getResidents()) {
				PlatinumManager.givePlatinum(resident, 
						CivSettings.platinumRewards.get("winningWar").amount, 
						"Spoils to the victor! You've earned %d");	
			}
		}
		
		EndGameCondition.onCivilizationWarDefeat(defeated);
		CivGlobal.getSessionDB().add(key, value, master.getId(), 0, 0);
	}
	
	/*
	 * Rebuild the defeated civs session db vars when a transfer of defeated towns occurs.
	 */
	public static void resaveAllDefeatedCivs() {
		CivGlobal.getSessionDB().delete_all("capturedCiv");

		for (String civName : defeatedCivs.keySet()) {
			Civilization master = defeatedCivs.get(civName);
			saveDefeatedCiv(CivGlobal.getCiv(civName), master);
		}
	}
	
	public static void resaveAllDefeatedTowns() {
		CivGlobal.getSessionDB().delete_all("capturedTown");
		
		for (String townName : defeatedTowns.keySet()) {
			Civilization master = defeatedTowns.get(townName);
			saveDefeatedTown(townName, master);
		}
	}
	
	public static void loadDefeatedTowns() {
		ArrayList<SessionEntry> entries = CivGlobal.getSessionDB().lookup("capturedTown");
		
		for (SessionEntry entry : entries) {
			String[] split = entry.value.split(":");
			defeatedTowns.put(split[0], CivGlobal.getCivFromId(Integer.valueOf(split[1])));
		}
	}
	
	public static void loadDefeatedCivs() {
		ArrayList<SessionEntry> entries = CivGlobal.getSessionDB().lookup("capturedCiv");
		
		for (SessionEntry entry : entries) {
			String[] split = entry.value.split(":");
			defeatedCivs.put(split[0], CivGlobal.getCivFromId(Integer.valueOf(split[1])));
		}
	}
	
	public static void clearSavedDefeats() {
		CivGlobal.getSessionDB().delete_all("capturedTown");
		CivGlobal.getSessionDB().delete_all("capturedCiv");
	}
	
	public static void init() {
		loadDefeatedTowns();
		loadDefeatedCivs();
		processDefeated();
	}
	
	public static boolean isWarTime() {
		return warTime;
	}
	
	public static boolean hasWars() {
		for (Civilization civ : CivGlobal.getCivs()) {
			for (Relation relation : civ.getDiplomacyManager().getRelations()) {
				if (relation.getStatus().equals(Status.WAR)) {
					return true;
				}
			}
		} return false;
	}
	
	public static void setWarTime(boolean warTime) {
		if (warTime == true && !War.hasWars()) {
			
			//XXX TEST
			new Thread(new Runnable(){
                public void run() {
                    try {
        				Thread.sleep(1000);
        				CivMessage.globalTitle(CivColor.BOLD+"Wartime was Skipped", CivColor.LightGray+CivColor.ITALIC+"There were no wars for this wartime.");
        				ToggleCommandsEvent.enableCommands();
        				Thread.sleep(3500);
        				CivMessage.globalTitle(CivColor.BOLD+"Wartime was Skipped", CivColor.LightGray+CivColor.ITALIC+"All commands are now enabled.");
        				Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
                }
            }).start();
			//XXX TEST
			
			
				//CivMessage.globalTitle(CivColor.BOLD+"Wartime was Skipped", CivColor.ITALIC+"There were no wars for this wartime.");
				//ToggleCommandsEvent.enableCommands();
				return;
			} else if (warTime == false && !War.isWarTime()) {
		}
		if (warTime == false) {
			/* War time has ended. */
			War.setStart(null);
			War.setEnd(null);
			War.restoreAllTowns();
			War.repositionPlayers("You've been teleported back to your town hall. WarTime ended and you were in enemy territory.");
			War.processDefeated();
			
			CivGlobal.growthEnabled = true;
			CivGlobal.fisheriesEnabled = true;
			CivGlobal.quarriesEnabled = true;
			CivGlobal.trommelsEnabled = true;
			CivGlobal.tradeEnabled = true;
			
			/* Delete any wartime file used to prevent reboots. */
			File file = new File("wartime");
			file.delete();
			
			//XXX TEST
			new Thread(new Runnable(){
                public void run() {
                    try {
        				Thread.sleep(1000);
        				CivMessage.globalTitle(CivColor.BOLD+"Wartime has Ended", CivColor.LightGray+CivColor.ITALIC+"All commands are now enabled.");
        				ToggleCommandsEvent.enableCommands();
        				Thread.sleep(3500);
        				CivMessage.globalTitle(CivColor.BOLD+"Wartime has Ended", CivColor.LightGray+CivColor.ITALIC+"Most Lethal: "+WarStats.getTopKiller());
        				Thread.sleep(3500);
        				CivMessage.globalTitle(CivColor.BOLD+"Wartime has Ended", CivColor.LightGray+CivColor.ITALIC+"Most Deaths: "+WarStats.getTopDeaths());
        				Thread.sleep(3500);
        				CivMessage.globalTitle(CivColor.BOLD+"Wartime has Ended", CivColor.LightGray+CivColor.ITALIC+"Captured Civs: "+WarStats.getCapturedCivs());
        				Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
                }
            }).start();
			//XXX TEST
			
//			ToggleCommandsEvent.enableCommands();
//			CivMessage.globalTitle(CivColor.BOLD+"WarTime Has Ended", CivColor.ITALIC+"Most Lethal: ");
//			/* display some stats. */
//			CivMessage.global("Most Lethal: "+WarStats.getTopKiller());
//			List<String> civs = WarStats.getCapturedCivs();
//			if (civs.size() > 0) {
//				for (String str : civs) {
//					CivMessage.global(str);
//				}
//			}
//			WarStats.clearStats();
			for (Civilization civ : CivGlobal.getCivs()) {
				civ.onWarEnd();
			}
		} else {
			int mins = 0;
			try {
				mins = CivSettings.getInteger(CivSettings.warConfig, "war.time_length");
			} catch (InvalidConfiguration e2) {
				e2.printStackTrace();
			}
			/* War time has started. */
			ToggleCommandsEvent.disableCommands();
			CivMessage.globalTitle(CivColor.BOLD+"WarTime Has Started",CivColor.ITALIC+"Some structures & other stuff has been disabled.");
			War.setStart(new Date());
			War.repositionPlayers("You've been teleported back to your town hall. WarTime has started and you were in enemy territory.");
			//War.vassalTownsWithNoTownHalls();
			War.resetTownClaimFlags();
			WarAntiCheat.kickUnvalidatedPlayers();
			
			/* Put a flag on the filesystem to prevent cron reboots. */
			File file = new File("wartime");
			try {
				file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			CivGlobal.growthEnabled = false;
			CivGlobal.fisheriesEnabled = false;
			CivGlobal.quarriesEnabled = false;
			CivGlobal.trommelsEnabled = false;
			CivGlobal.tradeEnabled = false;
			
			Calendar endCal = Calendar.getInstance();
			endCal.add(Calendar.MINUTE, mins);
			War.setEnd(endCal.getTime());
		}
		War.warTime = warTime;
	}

	/*  When a civ conqueres a town, then has its capitol conquered,
	 * the town that it just conquered needs to go to the new owner.
	 * If the new owner was the town's old owner, then that town is no longer
	 * defeated. */
	public static void transferDefeated(Civilization loser, Civilization winner) {
		
		/* Transfer any defeated towns */
		ArrayList<String> removeUs = new ArrayList<String>();
		
		for (String townName : defeatedTowns.keySet()) {
			Civilization civ = defeatedTowns.get(townName);
			if (civ == loser) {
				Town town = CivGlobal.getTown(townName);
				if (town.getCiv() == winner) {
					removeUs.add(townName);
				} else {
					defeatedTowns.put(townName, winner);
				}
			}
		}
		
		for (String townName : removeUs) {
			defeatedTowns.remove(townName);
		}
		resaveAllDefeatedTowns();
		
		/* Transfer any defeated civs */
		for (String civName : defeatedCivs.keySet()) {
			Civilization civ = defeatedCivs.get(civName);
	
			/* Defeated civs should never be our own, always transfer. */
			if (civ == loser) {
				defeatedCivs.put(civName, winner);
			}
		}
		resaveAllDefeatedCivs();
		
	}
	
	private static void processDefeated() {
		
		if (!CivGlobal.isCasualMode()) {
			for (String townName : defeatedTowns.keySet()) {
				try {
					Town town = CivGlobal.getTown(townName);
					if (town == null) {
						continue;
					}
					
					Civilization winner = defeatedTowns.get(townName);
					
					town.onDefeat(winner);
					CivMessage.sendTown(town, CivColor.LightBlue+"Welcome our new overlords "+winner.getName());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			for (String civName : defeatedCivs.keySet()) {
				try {
					Civilization civ = CivGlobal.getCiv(civName);
					if (civ == null) {
						/* 
						 * Civ is no longer on the list of active civs. Which means it's 
						 * already been conquered?
						 */
						CivLog.error("Couldn't find civilization named "+civName+" in civ hash table while trying to process it's defeat.");
						continue;
					}
					
					Civilization winner = defeatedCivs.get(civName);
					CivMessage.sendCiv(civ, CivColor.LightBlue+"Welcome our new overlords "+winner.getName());
					civ.onDefeat(winner);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			/* Casual Mode */
			
			/* Nothing happens for defeated towns. */
			
			/* Defeated Civs cause the war to 'reset' */
			for (String civName : defeatedCivs.keySet()) {
				try {
					Civilization civ = CivGlobal.getCiv(civName);
					if (civ == null) {
						/* 
						 * Civ is no longer on the list of active civs. Which means it's 
						 * already been conquered?
						 */
						CivLog.error("Couldn't find civilization named "+civName+" in civ hash table while trying to process it's defeat.");
						continue;
					}
					
					Civilization winner = defeatedCivs.get(civName);
					CivMessage.sendCiv(winner, " has honorably defeated "+civName+". Their differences settled, they now revert to a neutral relationship.");
					CivMessage.sendCiv(civ, " has honorably defeated "+civName+". Their differences settled, they now revert to a neutral relationship.");
					CivGlobal.setRelation(winner, civ, Status.NEUTRAL);				
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		defeatedTowns.clear();
		defeatedCivs.clear();
		clearSavedDefeats();
	}

	/*
	 * The 'claimed' flag is used to tag towns that had illegal town hall placements, and have already been claimed.
	 * When war time starts, we should reset the claim flag so it can be claimed by someone else.
	 */
	private static void resetTownClaimFlags() {
		for (Town t : CivGlobal.getTowns()) {
			t.claimed = false;
			t.defeated = false;
		}
	}

	private static void repositionPlayers(String reason) {
		for (Civilization civ : CivGlobal.getCivs()) {
			try {
				civ.repositionPlayers(reason);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void restoreAllTowns() {
		for (Town town : CivGlobal.getTowns()) {
			try {
				WarRegen.restoreBlocksFor(town.getName());
				if (town.getTownHall() != null && town.getTownHall().isActive()) {
					town.getTownHall().regenControlBlocks();
				}
			} catch (Exception e) {
				CivLog.error("Error encountered while restoring town:"+town.getName());
				e.printStackTrace();
			}
		}
		
		WarRegen.restoreBlocksFor(WarCamp.RESTORE_NAME);
		WarRegen.restoreBlocksFor(Cannon.RESTORE_NAME);
		WarRegen.restoreBlocksFor(WarListener.RESTORE_NAME);
		Cannon.cleanupAll();
	}

	/**
	 * @return the start
	 */
	public static Date getStart() {
		return start;
	}

	
	/**
	 * @param start the start to set
	 */
	public static void setStart(Date start) {
		War.start = start;
	}

	/**
	 * @return the end
	 */
	public static Date getEnd() {
		return end;
	}

	/**
	 * @param end the end to set
	 */
	public static void setEnd(Date end) {
		War.end = end;
	}

	public static Date getNextWarTime() {
		EventTimer warTimer = EventTimer.timers.get("war");
		return warTimer.getNext().getTime();
	}

	public static boolean isOnlyWarriors() {
		return onlyWarriors;
	}

	public static void setOnlyWarriors(boolean onlyWarriors) {
		War.onlyWarriors = onlyWarriors;
	}
	
	public static boolean isWithinWarDeclareDays() {
		Date nextWar = War.getNextWarTime();
		Date now = new Date();
		int time_declare_days = getTimeDeclareDays();
		
		if ((now.getTime() + time_declare_days*(1000*60*60*24)) >= nextWar.getTime()) {
			return true;
		}
		
		return false;	
	}

	public static boolean isWithinAllyDeclareHours() {
		Date nextWar = War.getNextWarTime();
		int ally_declare_hours = getAllyDeclareHours();
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR_OF_DAY, ally_declare_hours);
				
		if (cal.getTime().after(nextWar)) {
			return true;
		}
		
		return false;	
	}
	
	public static int getTimeDeclareDays() {
		try {
			int time_declare_days = CivSettings.getInteger(CivSettings.warConfig, "war.time_declare_days");
			return time_declare_days;
		} catch (InvalidConfiguration e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public static int getAllyDeclareHours() {
		try {
			int ally_decare_hours = CivSettings.getInteger(CivSettings.warConfig, "war.ally_declare_hours");
			return ally_decare_hours;
		} catch (InvalidConfiguration e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public static boolean isCivAggressor(Civilization civ) {
		for (Relation relation : civ.getDiplomacyManager().getRelations()) {
			if (relation.getStatus() == Status.WAR) {
				if (relation.getAggressor() == civ) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean isCivAggressorToAlly(Civilization enemy, Civilization ourCiv) {
		for (Relation relation : ourCiv.getDiplomacyManager().getRelations()) {
			if (relation.getStatus() == Status.ALLY) {
				Civilization ally = relation.getOtherCiv();
				Relation allyRelation = ally.getDiplomacyManager().getRelation(enemy);
				if (allyRelation == null) {
					continue;
				}
				
				if (allyRelation.getStatus() == Status.WAR && (allyRelation.getAggressor() == enemy)) {
					return true;
				}
			}
		}
		return false;
	}
	
}
