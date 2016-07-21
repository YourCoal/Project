/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.threading.sync.request;

import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;

import com.civcraft.structure.farm.FarmChunk;
import com.civcraft.structure.farm.GrowBlock;

public class GrowRequest extends AsyncRequest {

	public GrowRequest(ReentrantLock lock) {
		super(lock);
	}
	
	public LinkedList<GrowBlock> growBlocks;
	public FarmChunk farmChunk;

}
