package com.avrgaming.civcraft.threading.tasks;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.structure.ScoutTower;
import com.avrgaming.civcraft.structure.Structure;
import com.avrgaming.civcraft.util.BlockCoord;

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
