/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.threading.timers;

import java.util.Date;

import com.civcraft.threading.TaskMaster;
import com.civcraft.util.TimeTools;
import com.civcraft.war.War;

public class WarEndCheckTask implements Runnable {

	@Override
	public void run() {

		Date now = new Date();
		if (War.isWarTime()) {
			if (War.getEnd() == null || now.after(War.getEnd())) {
				War.setWarTime(false);
			} else {
				TaskMaster.syncTask(this, TimeTools.toTicks(1));
			}
		}		
	}

}
