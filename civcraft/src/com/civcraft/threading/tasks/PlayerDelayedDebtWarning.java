/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.threading.tasks;

import com.civcraft.object.Resident;


public class PlayerDelayedDebtWarning implements Runnable {
	Resident resident;
	
	public PlayerDelayedDebtWarning(Resident resident) {
		this.resident = resident;
	}
	
	@Override
	public void run() {
		resident.warnDebt();
	}
}
