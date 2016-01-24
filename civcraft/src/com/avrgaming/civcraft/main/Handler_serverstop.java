package com.avrgaming.civcraft.main;

import java.util.Iterator;
import java.util.Map.Entry;

import com.avrgaming.civcraft.structure.Structure;
import com.avrgaming.civcraft.util.BlockCoord;

public class Handler_serverstop extends Thread {

	public void run() {
		Iterator<Entry<BlockCoord, Structure>> iter = CivGlobal.getStructureIterator();
		while(iter.hasNext()) {
			Structure struct = iter.next().getValue();	
			struct.onUnload();
		}
	}
	
}
