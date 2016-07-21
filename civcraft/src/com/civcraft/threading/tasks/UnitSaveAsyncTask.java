/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.threading.tasks;

import com.civcraft.structure.Barracks;

public class UnitSaveAsyncTask implements Runnable {

	Barracks barracks;
	
	public UnitSaveAsyncTask(Barracks barracks) {
		this.barracks = barracks;
	}

	@Override
	public void run() {
		barracks.saveProgress();
	}
	
	
	
}
