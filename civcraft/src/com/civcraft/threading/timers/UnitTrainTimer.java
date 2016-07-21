/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.threading.timers;

import java.util.Iterator;
import java.util.Map.Entry;

import com.civcraft.main.CivGlobal;
import com.civcraft.structure.Barracks;
import com.civcraft.structure.Structure;
import com.civcraft.util.BlockCoord;

public class UnitTrainTimer implements Runnable {

	@Override
	public void run() {
		
		Iterator<Entry<BlockCoord, Structure>> iter = CivGlobal.getStructureIterator();
		while(iter.hasNext()) {
			Structure struct = iter.next().getValue();
			if (struct instanceof Barracks) {
				((Barracks)struct).updateTraining();
			}
		}
	}

}
