package com.avrgaming.civcraft.threading.timers;

import java.util.Iterator;
import java.util.Map.Entry;

import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.structure.Barracks;
import com.avrgaming.civcraft.structure.Structure;
import com.avrgaming.civcraft.util.BlockCoord;

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
