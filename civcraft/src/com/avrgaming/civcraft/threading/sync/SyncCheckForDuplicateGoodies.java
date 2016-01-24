package com.avrgaming.civcraft.threading.sync;

import com.avrgaming.civcraft.main.CivGlobal;

public class SyncCheckForDuplicateGoodies implements Runnable {

	@Override
	public void run() {
		CivGlobal.checkForDuplicateGoodies();
	}

}
