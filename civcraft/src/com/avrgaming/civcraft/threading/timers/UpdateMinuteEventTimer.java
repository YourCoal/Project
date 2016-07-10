/**
 * CivCraft Created by - AVRGAMING LLC
 * This Code Modified by - https://www.youtube.com/user/cpcole556
 **/
package com.avrgaming.civcraft.threading.timers;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReentrantLock;

import com.avrgaming.civcraft.camp.Camp;
import com.avrgaming.civcraft.camp.CampUpdateTick;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.structure.Structure;
import com.avrgaming.civcraft.structure.wonders.Wonder;
import com.avrgaming.civcraft.threading.CivAsyncTask;
import com.avrgaming.civcraft.threading.TaskMaster;
import com.avrgaming.civcraft.threading.tasks.FisheryAsyncTask;
import com.avrgaming.civcraft.threading.tasks.LumberMillAsyncTask;
import com.avrgaming.civcraft.threading.tasks.MobGrinderAsyncTask;
import com.avrgaming.civcraft.util.BlockCoord;

public class UpdateMinuteEventTimer extends CivAsyncTask {
	
	public static ReentrantLock lock = new ReentrantLock();
	
	public UpdateMinuteEventTimer() {
	}
	
	@Override
	public void run() {		
		if (!lock.tryLock()) {
			return;
		} try {
			// Loop through each structure, if it has an update function call it in another async process
			Iterator<Entry<BlockCoord, Structure>> iter = CivGlobal.getStructureIterator();
			while(iter.hasNext()) {
				Structure struct = iter.next().getValue();
				if (!struct.isActive())
					continue;
				
				try {
					if (struct.getUpdateEvent() != null && !struct.getUpdateEvent().equals("")) {
						if (struct.getUpdateEvent().equals("mobGrinder_process")) {
							if (!CivGlobal.mobGrinderEnabled) {
								continue;
							}
							TaskMaster.asyncTask("Mob Grinder-"+struct.getCorner().toString(), new MobGrinderAsyncTask(struct), 0);
						} else if (struct.getUpdateEvent().equals("fish_hatchery_process")) {
							if (!CivGlobal.fisheryEnabled) {
								continue;
							}
							TaskMaster.asyncTask("Fishery-"+struct.getCorner().toString(), new FisheryAsyncTask(struct), 0);
						} else if (struct.getUpdateEvent().equals("lumber_mill_process")) {
							if (!CivGlobal.lumberMillsEnabled) {
								continue;
							}
							TaskMaster.asyncTask("Lumber Mill-"+struct.getCorner().toString(), new LumberMillAsyncTask(struct), 0);
						}
					}
					struct.onUpdate();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			for (Wonder wonder : CivGlobal.getWonders()) {
				wonder.onUpdate();
			}
			
			for (Camp camp : CivGlobal.getCamps()) {
				if (!camp.sifterLock.isLocked()) {
					TaskMaster.asyncTask(new CampUpdateTick(camp), 0);
				}
			}
		} finally {
			lock.unlock();
		}
	}
}
