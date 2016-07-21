/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.threading.sync;

import com.civcraft.main.CivGlobal;

public class SyncCheckForDuplicateGoodies implements Runnable {

	@Override
	public void run() {
		CivGlobal.checkForDuplicateGoodies();
	}

}
