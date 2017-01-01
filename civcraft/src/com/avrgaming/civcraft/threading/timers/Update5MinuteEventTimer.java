package com.avrgaming.civcraft.threading.timers;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReentrantLock;

import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.structure.Structure;
import com.avrgaming.civcraft.threading.CivAsyncTask;
import com.avrgaming.civcraft.threading.TaskMaster;
import com.avrgaming.civcraft.threading.tasks.GranaryAsyncTask;
import com.avrgaming.civcraft.util.BlockCoord;

public class Update5MinuteEventTimer extends CivAsyncTask {
	
	//This file runs the structures Granary,
	public static ReentrantLock lock = new ReentrantLock();
	
	public Update5MinuteEventTimer() {
	}
	
	@Override
	public void run() {		
		if (!lock.tryLock()) {
			return;
		}
		
		try {
			Iterator<Entry<BlockCoord, Structure>> iter = CivGlobal.getStructureIterator();
			while(iter.hasNext()) {
				Structure struct = iter.next().getValue();
				if (!struct.isActive())
					continue;
				
				try {
					if (struct.getUpdateEvent() != null && !struct.getUpdateEvent().equals("")) {
						if (struct.getUpdateEvent().equals("granary_process")) {
							if (!CivGlobal.granariesEnabled) {
								continue;
							} //Run 5 times because we don't want to spam the player every minute with messages.
							TaskMaster.asyncTask("Granary-"+struct.getCorner().toString(), new GranaryAsyncTask(struct), 0);
							TaskMaster.asyncTask("Granary-"+struct.getCorner().toString(), new GranaryAsyncTask(struct), 0);
							TaskMaster.asyncTask("Granary-"+struct.getCorner().toString(), new GranaryAsyncTask(struct), 0);
							TaskMaster.asyncTask("Granary-"+struct.getCorner().toString(), new GranaryAsyncTask(struct), 0);
							TaskMaster.asyncTask("Granary-"+struct.getCorner().toString(), new GranaryAsyncTask(struct), 0);
						}
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
