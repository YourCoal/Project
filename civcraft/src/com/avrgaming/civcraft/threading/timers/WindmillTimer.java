package com.avrgaming.civcraft.threading.timers;

import java.util.Iterator;
import java.util.Map.Entry;

import com.avrgaming.civcraft.config.CivSettings;
import com.avrgaming.civcraft.exception.InvalidConfiguration;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.structure.Structure;
import com.avrgaming.civcraft.structure.Windmill;
import com.avrgaming.civcraft.util.BlockCoord;
import com.avrgaming.civcraft.war.War;

public class WindmillTimer implements Runnable {

	@Override
	public void run() {
		if (War.isWarTime()) {
			return;
		}
		
		Iterator<Entry<BlockCoord, Structure>> iter = CivGlobal.getStructureIterator();
		while(iter.hasNext()) {
			Structure struct = iter.next().getValue();
			if (struct instanceof Windmill) {
				if (struct.getCiv().hasTechnology("tech_combustion")) {
					try {
						((Windmill)struct).processWindmill();
						Thread.sleep(CivSettings.getInteger(CivSettings.techsConfig, "windmill_buffs.combustion_plant_speed"));
						((Windmill)struct).processWindmill();
					} catch (InterruptedException | InvalidConfiguration e) {
						e.printStackTrace();
					}
				} else {
					((Windmill)struct).processWindmill();
				}
			}
		}
	}
}
