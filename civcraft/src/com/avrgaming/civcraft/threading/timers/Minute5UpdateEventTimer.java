package com.avrgaming.civcraft.threading.timers;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReentrantLock;

import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.structure.Structure;
import com.avrgaming.civcraft.threading.CivAsyncTask;
import com.avrgaming.civcraft.util.BlockCoord;

public class Minute5UpdateEventTimer extends CivAsyncTask {
	
	//Controls Granary, Shipyard,
	public static ReentrantLock lock = new ReentrantLock();
	
	public Minute5UpdateEventTimer() {
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
/*						if (struct.getUpdateEvent().equals("granary_process")) {
							if (!CivGlobal.questStructuresEnabled) {
								continue;
							}
							TaskMaster.asyncTask("Granary-"+struct.getCorner().toString(), new GranaryAsyncTask(struct), 0);
						}*/
					}
					struct.onUpdate();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} finally {
			lock.unlock();
		}
	}
}
