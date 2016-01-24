package com.avrgaming.civcraft.threading.tasks;

import com.avrgaming.civcraft.structure.Barracks;

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
