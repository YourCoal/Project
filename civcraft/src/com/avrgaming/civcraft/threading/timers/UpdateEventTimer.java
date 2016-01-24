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
import com.avrgaming.civcraft.threading.tasks.TrommelAsyncTask;
import com.avrgaming.civcraft.util.BlockCoord;

public class UpdateEventTimer extends CivAsyncTask {
		
	public static ReentrantLock lock = new ReentrantLock();
	
	public UpdateEventTimer() {
	}
	
	@Override
	public void run() {		

		if (!lock.tryLock()) {
			return;
		}
		
		try {
			// Loop through each structure, if it has an update function call it in another async process
			Iterator<Entry<BlockCoord, Structure>> iter = CivGlobal.getStructureIterator();
	
			while(iter.hasNext()) {
				Structure struct = iter.next().getValue();
				
				if (!struct.isActive())
					continue;
				
				try {
					if (struct.getUpdateEvent() != null && !struct.getUpdateEvent().equals("")) {
						if (struct.getUpdateEvent().equals("trommel_process")) {
							if (!CivGlobal.trommelsEnabled) {
								continue;
							}
							
							TaskMaster.asyncTask("trommel-"+struct.getCorner().toString(), new TrommelAsyncTask(struct), 0);
						}
					}
					
					struct.onUpdate();
				} catch (Exception e) {
					e.printStackTrace();
					//We need to catch any exception so that an error in one town/structure/good does not
					//break things for everybody.
					//TODO log exception into a file or something...
	//				if (struct.getTown() == null) {
	//					RJ.logException("TownUnknown struct:"+struct.config.displayName, e);
	//				} else {
	//					RJ.logException(struct.town.getName()+":"+struct.config.displayName, e);
	//				}
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
