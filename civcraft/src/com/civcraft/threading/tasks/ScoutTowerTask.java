/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.threading.tasks;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

import com.civcraft.main.CivGlobal;
import com.civcraft.structure.ScoutTower;
import com.civcraft.structure.Structure;
import com.civcraft.util.BlockCoord;

public class ScoutTowerTask implements Runnable {
	@Override
	public void run() {		
		HashSet<String> announced = new HashSet<String>();
		
		try {
			if (!CivGlobal.towersEnabled) {
				return;
			}
			
			Iterator<Entry<BlockCoord, Structure>> iter = CivGlobal.getStructureIterator();
			while(iter.hasNext()) {
				Structure struct = iter.next().getValue();
				if (struct instanceof ScoutTower) {
					((ScoutTower)struct).process(announced);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
