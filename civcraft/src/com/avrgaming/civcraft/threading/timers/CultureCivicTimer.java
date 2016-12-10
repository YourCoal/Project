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
package com.avrgaming.civcraft.threading.timers;

import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.Town;
import com.avrgaming.civcraft.structure.TownHall;
import com.avrgaming.civcraft.threading.CivAsyncTask;

public class CultureCivicTimer extends CivAsyncTask {

	//private double beakersPerRun;
	
	public static final int CULTURE_PERIOD = 60;
	
	public CultureCivicTimer(int periodInSeconds) {
	}
	
	@Override
	public void run() {
		for (Town t : CivGlobal.getTowns()) {
			
			if (t.getName() == null) {
				CivMessage.sendTown(t, "ERROR: your town name is not set right! No civic research is progressing. Contact an admin.");
				continue;
			}
			
			Town town = CivGlobal.getTown(t.getName());
			if (town == null) {
				CivMessage.sendTown(t, "ERROR: Couldn't find your town town named "+t.getName()+"! No civic research is progressing. Contact an admin.");
				continue;
			}
			
			TownHall th = town.getTownHall();
			if (th == null) {
				CivMessage.sendTown(t, "Your town doesn't have a town hall! You are not generating any beakers.");
			}
			
			try {
				/* The base_beakers defines the number of beakers per hour to give.
				 * This timer runs every min, so dividing my 60 will give us the number
				 * of beakers per min. */
				if (t.getCivicResearch() != null) {
					t.addCultureForCivics(t.getCultureForCivics() / CULTURE_PERIOD);
				} else {
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
