package com.avrgaming.civcraft.threading.timers;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReentrantLock;

import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.structure.Structure;
import com.avrgaming.civcraft.threading.CivAsyncTask;
import com.avrgaming.civcraft.threading.TaskMaster;
import com.avrgaming.civcraft.threading.tasks.TownBonusChestAsyncTask;
import com.avrgaming.civcraft.util.BlockCoord;

public class SyncTownHallBonusChest extends CivAsyncTask {
	
	public static ReentrantLock lock = new ReentrantLock();
	
	public SyncTownHallBonusChest() {
	}
	
	@Override
	public void run() {		
		if (!lock.tryLock()) {
			return;
		} try {
			Iterator<Entry<BlockCoord, Structure>> iter = CivGlobal.getStructureIterator();
			while(iter.hasNext()) {
				Structure struct = iter.next().getValue();
				if (!struct.isActive())
					continue;
				
				try {
					if (struct.getUpdateEvent() != null && !struct.getUpdateEvent().equals("")) {
						if (struct.getUpdateEvent().equals("bonus_chest")) {
							TaskMaster.asyncTask("TownHall-Bonus-"+struct.getCorner().toString(), new TownBonusChestAsyncTask(struct), 0);
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
