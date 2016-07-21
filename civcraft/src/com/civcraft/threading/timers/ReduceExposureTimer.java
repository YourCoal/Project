/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.threading.timers;

import java.util.LinkedList;

import com.civcraft.main.CivGlobal;
import com.civcraft.object.Resident;
import com.civcraft.threading.TaskMaster;

public class ReduceExposureTimer implements Runnable {

	@Override
	public void run() {

		LinkedList<String> playersToReduce = new LinkedList<String>();
		
		for (Resident resident : CivGlobal.getResidents()) {
			if (!resident.isPerformingMission() && resident.getSpyExposure() > 0) {
				playersToReduce.add(resident.getName());
			}
		}
		
		
		class SyncTask implements Runnable {

			public LinkedList<String> playersToReduce;
			
			public SyncTask(LinkedList<String> list) {
				playersToReduce = list;
			}
			
			@Override
			public void run() {
				for (String name : playersToReduce) {
					Resident resident = CivGlobal.getResident(name);
					if (resident.getSpyExposure() <= 5) {
						resident.setSpyExposure(0.0);
					} else {
						resident.setSpyExposure(resident.getSpyExposure() - 5);
					}
				}
				
			}
			
		}
		
		TaskMaster.syncTask(new SyncTask(playersToReduce));
		
	}

}