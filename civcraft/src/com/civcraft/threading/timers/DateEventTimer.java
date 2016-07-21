/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.threading.timers;

import java.util.Date;

import com.civcraft.main.CivGlobal;

public class DateEventTimer implements Runnable {

	/*
	 * This task runs once per min to check various times. If the time for an event is after the
	 * current time then we run the specified task.
	 */
		
	@Override
	public void run() {
	
		Date now = new Date();
		
		/* Check for spawn regen. */
		if (now.after(CivGlobal.getTodaysSpawnRegenDate())) {
			
		}
	}
}
