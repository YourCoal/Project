/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.structure.farm;

import java.util.LinkedList;

import com.civcraft.main.CivGlobal;


public class FarmCachePopulateTask implements Runnable {

	LinkedList<FarmChunk> farms;
	
	public FarmCachePopulateTask(LinkedList<FarmChunk> farms) {
		this.farms = farms;
	}
	
	@Override
	public void run() {
		if (!CivGlobal.growthEnabled) {
			return;
		}
		
		for (FarmChunk fc : farms) {
			try {
				fc.populateCropLocationCache();
			} catch (Exception e){
				e.printStackTrace();
			}
		}

	}

}
