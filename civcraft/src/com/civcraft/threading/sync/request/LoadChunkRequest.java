/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.threading.sync.request;

import java.util.concurrent.locks.ReentrantLock;

public class LoadChunkRequest extends AsyncRequest {

	public LoadChunkRequest(ReentrantLock lock) {
		super(lock);
	}

	public String worldName;
	public int x;
	public int z;
	
}
