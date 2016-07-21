/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.structure.farm;

import java.util.concurrent.TimeUnit;

import com.civcraft.main.CivGlobal;
import com.civcraft.threading.CivAsyncTask;

public class FarmGrowthSyncTask extends CivAsyncTask {
	
	// XXX Despite being named a sync timer, this task is now actually asynchronous
	
	public void processFarmChunks() {
		if (!CivGlobal.growthEnabled) {
			return;
		}
		
		//Queue<FarmChunk> regrow = new LinkedList<FarmChunk>();
		for (FarmChunk fc : CivGlobal.getFarmChunks()) {
			if (fc.getTown() == null || fc.getStruct() == null) {
				System.out.println("FarmChunkError: Could not process farm chunk, town or struct was null. Orphan?");
				continue;
			}
						
			/* Since we're now async, we can wait on this lock. */
			try {
				if(!fc.lock.tryLock(TIMEOUT, TimeUnit.MILLISECONDS)) {
					return;
				}
			} catch (InterruptedException e1) {
				e1.printStackTrace();
				return;
			}
			try {
				try {
					fc.processGrowth(this);
				} catch (InterruptedException e) {
					e.printStackTrace();
					return;
				}
			} finally {
				fc.lock.unlock();
			}
			//} else {
			//	regrow.add(fc);
			//}
		}
		
		//if (regrow.size() > 0) {
	//		TaskMaster.syncTask(new FarmGrowthRegrowTask(regrow), 0);
	//	}
	}
	
	
	@Override
	public void run() {
		try {
			processFarmChunks();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
}