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

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReentrantLock;

import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.main.CivLog;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.AttrSource;
import com.avrgaming.civcraft.object.Civilization;
import com.avrgaming.civcraft.object.Town;
import com.avrgaming.civcraft.structure.Cottage;
import com.avrgaming.civcraft.structure.Lab;
import com.avrgaming.civcraft.structure.Mine;
import com.avrgaming.civcraft.structure.Structure;
import com.avrgaming.civcraft.structure.TownHall;
import com.avrgaming.civcraft.threading.CivAsyncTask;
import com.avrgaming.civcraft.util.BlockCoord;
import com.avrgaming.civcraft.util.CivColor;

public class EffectEventTimer extends CivAsyncTask {
	
	//public static Boolean running = false;
	
	public static ReentrantLock runningLock = new ReentrantLock();
	
	public EffectEventTimer() {
	}

	private void processTick() {
		/* Clear the last taxes so they don't accumulate. */
		for (Civilization civ : CivGlobal.getCivs()) {
			civ.lastTaxesPaidMap.clear();
		}
		
		//HashMap<Town, Integer> cultureGenerated = new HashMap<Town, Integer>();
		
		// Loop through each structure, if it has an update function call it in another async process
		Iterator<Entry<BlockCoord, Structure>> iter = CivGlobal.getStructureIterator();
		
		while(iter.hasNext()) {
			Structure struct = iter.next().getValue();
			TownHall townhall = struct.getTown().getTownHall();

			if (townhall == null) {
				continue;
			}

			if (!struct.isActive())
				continue;

			struct.onEffectEvent();

			if (struct.getEffectEvent() == null || struct.getEffectEvent().equals(""))
				continue;
			
			String[] split = struct.getEffectEvent().toLowerCase().split(":"); 
			switch (split[0]) {
			case "generate_coins":
				if (struct instanceof Cottage) {
					Cottage cottage = (Cottage)struct;
					cottage.generateCoins(this);
				}
				break;
			case "process_mine":
				if (struct instanceof Mine) {
					Mine mine = (Mine)struct;
					mine.generateTownHammers(this);
					mine.generateItemHammers(this);
				}
				break;
			case "process_lab":
				if (struct instanceof Lab) {
					Lab lab = (Lab)struct;
					lab.generateBeakers(this);
				}
				break;
			}
		}
		
		/* Process any hourly attributes for this town - Culture */
		for (Town town : CivGlobal.getTowns()) {
			//high-jack this loop to display town hall warning.
			TownHall townhall = town.getTownHall();
			if (townhall == null) {
				CivMessage.sendTown(town, CivColor.Yellow+"Your town does not have a town hall! Structures have no effect!");
				continue;
			}
			
			double totalCulture = 0;
			AttrSource cultureSources = town.getCulture();
			totalCulture += cultureSources.total;
			
/*			try {
				if (town.getCiv().getResearchProgress() > 0) {
					return;
				}
				double unusedBeakers = Math.round(town.getUnusedBeakers());
				double cultureToBeakerConversion = CivSettings.getDouble(CivSettings.cultureConfig, "beakers_per_culture");
				if (unusedBeakers > 0) {
					double cultureFromBeakers = Math.round(unusedBeakers*cultureToBeakerConversion);
					if (cultureFromBeakers > 0) {
						CivMessage.sendTown(town, CivColor.LightGreen+"Converted "+CivColor.LightPurple+unusedBeakers+CivColor.LightGreen+" beakers into "+CivColor.LightPurple+
								cultureFromBeakers+CivColor.LightGreen+" culture, since no tech was being researched.");
						
						totalCulture += cultureFromBeakers;
						totalCulture += Math.round(totalCulture);
						town.addAccumulatedCulture(totalCulture);
						town.setUnusedBeakers(0);
					}
				}
			} catch (InvalidConfiguration e) {
				e.printStackTrace();
				return;
			}*/
			CivMessage.sendTown(town, CivColor.LightGreen+"Generated "+CivColor.LightPurple+totalCulture+CivColor.LightGreen+" culture.");
		}
		/* Checking for expired vassal states. */
		CivGlobal.checkForExpiredRelations();
	}
	
	@Override
	public void run() {
		
		if (runningLock.tryLock()) {
			try {
				processTick();
			} finally {
				runningLock.unlock();
			}
		} else {
			CivLog.error("COULDN'T GET LOCK FOR HOURLY TICK. LAST TICK STILL IN PROGRESS?");
		}
		
				
	}
	

}
