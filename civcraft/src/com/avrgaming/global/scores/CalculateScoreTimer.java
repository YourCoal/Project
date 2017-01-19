
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
 */package com.avrgaming.global.scores;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeMap;

import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.object.Civilization;
import com.avrgaming.civcraft.object.Town;
import com.avrgaming.civcraft.sessiondb.SessionEntry;
import com.avrgaming.civcraft.threading.CivAsyncTask;

public class CalculateScoreTimer extends CivAsyncTask {
	
	@Override
	public void run() {
		
		if (!CivGlobal.scoringEnabled) {
			return;
		}
		
		ArrayList<SessionEntry> entries = CivGlobal.getSessionDB().lookup("endgame:winningCiv");
		if (entries.size() != 0) {
			/* we have a winner, do not accumulate scores anymore. */
			return;
		}
		
		TreeMap<Integer, Civilization> civScores = new TreeMap<Integer, Civilization>();
		for (Civilization civ : CivGlobal.getCivs()) {
			if (civ.isAdminCiv()) {
				continue;
			}
			civScores.put(civ.getScore(), civ);
			
			try {
				ScoreManager.UpdateScore(civ, civ.getScore());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		TreeMap<Integer, Town> townScores = new TreeMap<Integer, Town>();	
		for (Town town : CivGlobal.getTowns()) {
			if (town.getCiv().isAdminCiv()) {
				continue;
			}
			try {
				townScores.put(town.getScore(), town);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				ScoreManager.UpdateScore(town, town.getScore());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		TreeMap<Integer, Civilization> civEcon = new TreeMap<Integer, Civilization>();
		for (Civilization civ : CivGlobal.getCivs()) {
			if (civ.isAdminCiv()) {
				continue;
			}
			civEcon.put((int) civ.getTreasury().getBalance(), civ);
		}
		
		TreeMap<Integer, Town> townEcon = new TreeMap<Integer, Town>();	
		for (Town town : CivGlobal.getTowns()) {
			if (town.getCiv().isAdminCiv()) {
				continue;
			}
			townEcon.put((int) town.getTreasury().getBalance(), town);
		}
		
		TreeMap<Integer, Civilization> civMemberCount = new TreeMap<Integer, Civilization>();
		for (Civilization civ : CivGlobal.getCivs()) {
			if (civ.isAdminCiv()) {
				continue;
			}
			
			for (Town town : civ.getTowns()) {
				civMemberCount.put((int) town.getResidentCount(), civ);
			}
		}
		
		TreeMap<Integer, Town> townMemberCount = new TreeMap<Integer, Town>();	
		for (Town town : CivGlobal.getTowns()) {
			if (town.getCiv().isAdminCiv()) {
				continue;
			}
			townMemberCount.put((int) town.getResidentCount(), town);
		}
		
		TreeMap<Integer, Civilization> civTownCount = new TreeMap<Integer, Civilization>();
		for (Civilization civ : CivGlobal.getCivs()) {
			if (civ.isAdminCiv()) {
				continue;
			}
			civTownCount.put((int) civ.getTownCount(), civ);
		}
		
		synchronized(CivGlobal.civilizationScores) {
			CivGlobal.civilizationScores = civScores;
		}
		
		synchronized(CivGlobal.townScores) {
			CivGlobal.townScores = townScores;
		}
		
		synchronized(CivGlobal.civilizationEcon) {
			CivGlobal.civilizationEcon = civEcon;
		}
		
		synchronized(CivGlobal.townEcon) {
			CivGlobal.townEcon = townEcon;
		}
		
		synchronized(CivGlobal.civilizationMemberCount) {
			CivGlobal.civilizationMemberCount = civMemberCount;
		}
		
		synchronized(CivGlobal.townMemberCount) {
			CivGlobal.townMemberCount = townMemberCount;
		}
		
		synchronized(CivGlobal.civilizationTownCount) {
			CivGlobal.civilizationTownCount = civTownCount;
		}
		
		
		
//		//Save out to file.
//		try {
//			writeCivScores(civScores);
//			writeTownScores(townScores);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

}
