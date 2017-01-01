package com.avrgaming.civcraft.threading.timers;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReentrantLock;

import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.structure.Structure;
import com.avrgaming.civcraft.threading.CivAsyncTask;
import com.avrgaming.civcraft.util.BlockCoord;

public class Update1MinuteEventTimer extends CivAsyncTask {
	
	//This file runs the structures Fishery,
	public static ReentrantLock lock = new ReentrantLock();
	
	public Update1MinuteEventTimer() {
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
						if (struct.getUpdateEvent().equals("fishery_process")) {
							if (!CivGlobal.fisheriesEnabled) {
								continue;
							}
//							TaskMaster.asyncTask("Fishery-"+struct.getCorner().toString(), new FisheryAsyncTask(struct), 0);
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
