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

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReentrantLock;

import com.avrgaming.civcraft.config.CivSettings;
import com.avrgaming.civcraft.exception.InvalidConfiguration;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.main.CivLog;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.AttrSource;
import com.avrgaming.civcraft.object.Civilization;
import com.avrgaming.civcraft.object.Town;
import com.avrgaming.civcraft.structure.Cottage;
import com.avrgaming.civcraft.structure.Granary;
import com.avrgaming.civcraft.structure.Lab;
import com.avrgaming.civcraft.structure.Mine;
import com.avrgaming.civcraft.structure.Monument;
import com.avrgaming.civcraft.structure.Structure;
import com.avrgaming.civcraft.structure.TownHall;
import com.avrgaming.civcraft.threading.CivAsyncTask;
import com.avrgaming.civcraft.threading.TaskMaster;
import com.avrgaming.civcraft.threading.tasks.GranaryAsyncTask;
import com.avrgaming.civcraft.util.BlockCoord;
import com.avrgaming.civcraft.util.CivColor;

public class EffectEventTimer extends CivAsyncTask {
	
	//public static Boolean running = false;
	
	public static ReentrantLock runningLock = new ReentrantLock();
	
	public EffectEventTimer() {
	}

	private void processTick() throws InterruptedException {
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
					mine.generateTownProduction(this);
//					mine.generateItemHammers(this);
				}
				break;
			case "process_lab":
				if (struct instanceof Lab) {
					Lab lab = (Lab)struct;
					lab.generateTownScience(this);
				}
				break;
			case "process_monument":
				if (struct instanceof Monument) {
					Monument monument = (Monument)struct;
					monument.generateTownCulture(this);
				}
				break;
			case "granary_process":
				if (struct instanceof Granary) {
					TaskMaster.asyncTask("Granary-"+struct.getCorner().toString(), new GranaryAsyncTask(struct), 0);
				}
				break;
			}
		}
		
		Thread.sleep(1000);
		
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
			
			try {
				double baseScience = CivSettings.getDouble(CivSettings.cultureConfig, "base_culture") * (town.getGovernment().culture_rate*2);
				if (baseScience <= 0) {
					baseScience = 0.5;
				}
				
				DecimalFormat df = new DecimalFormat("#.0");
				String newTotal = df.format(baseScience);
				double newBaseScience = Double.parseDouble(newTotal);
				
				CivMessage.sendTown(town, CivColor.LightGreen+"Given "+CivColor.LightPurple+newBaseScience+CivColor.LightGreen+" culture from government rate.");
				totalCulture += newBaseScience;
			} catch (InvalidConfiguration e) {
				e.printStackTrace();
				return;
			}
			
/*			try {
				if (town.getCiv().getResearchProgress() > 0) {
					return;
				}
				double unusedScience = Math.round(town.getUnusedScience());
				double cultureToScienceConversion = CivSettings.getDouble(CivSettings.cultureConfig, "science_per_culture");
				if (unusedScience > 0) {
					double cultureFromScience = Math.round(unusedScience*cultureToScienceConversion);
					if (cultureFromScience > 0) {
						CivMessage.sendTown(town, CivColor.LightGreen+"Converted "+CivColor.LightPurple+unusedScience+CivColor.LightGreen+" science into "+CivColor.LightPurple+
								cultureFromScience+CivColor.LightGreen+" culture, since no tech was being researched.");
						
						totalCulture += cultureFromScience;
						totalCulture += Math.round(totalCulture);
						town.addAccumulatedCulture(totalCulture);
						town.setUnusedScience(0);
					}
				}
			} catch (InvalidConfiguration e) {
				e.printStackTrace();
				return;
			}*/
			
			DecimalFormat df = new DecimalFormat("#.0");
			String newTotal = df.format(totalCulture);
			double newTotalCulture = Double.parseDouble(newTotal);
			town.addAccumulatedCulture(newTotalCulture);
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
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				runningLock.unlock();
			}
		} else {
			CivLog.error("COULDN'T GET LOCK FOR HOURLY TICK. LAST TICK STILL IN PROGRESS?");
		}	
	}
}
