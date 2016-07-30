/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.threading.timers;

import com.civcraft.main.CivGlobal;
import com.civcraft.main.CivMessage;
import com.civcraft.object.Civilization;
import com.civcraft.object.Town;
import com.civcraft.structure.TownHall;
import com.civcraft.threading.CivAsyncTask;

public class BeakerTimer extends CivAsyncTask {

	//private double beakersPerRun;
	
	public static final int BEAKER_PERIOD = 60;
	
	public BeakerTimer(int periodInSeconds) {
		
	//	this.beakersPerRun = ((double)periodInSeconds/60);
	}
	
	@Override
	public void run() {
		
		for (Civilization civ : CivGlobal.getCivs()) {
			
			if (civ.getCapitolName() == null) {
				CivMessage.sendCiv(civ, "ERROR: your capitol name is not set right! No research is progressing. Contact an admin.");
				continue;
			}
			
			Town town = CivGlobal.getTown(civ.getCapitolName());
			if (town == null) {
				CivMessage.sendCiv(civ, "ERROR: Couldn't find your capitol town named "+civ.getCapitolName()+"! No research is progressing. Contact an admin.");
				continue;
			}
			
			TownHall townhall = town.getTownHall();
			if (townhall == null) {
				CivMessage.sendCiv(civ, "Your captial doesn't have a town hall! You are not generating any beakers.");
			}
			
			try {
				/* 
				 * The base_beakers defines the number of beakers per hour to give.
				 * This timer runs every min, so dividing my 60 will give us the number
				 * of beakers per min.
				 */
				if (civ.getResearchTech() != null) {
					civ.addBeakers(civ.getBeakers() / BEAKER_PERIOD);
				} else {
					civ.processUnusedBeakers();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
