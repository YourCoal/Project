/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.threading.sync;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

import com.civcraft.exception.CivException;
import com.civcraft.threading.sync.request.UpdateInventoryRequest;

public class SyncUpdateInventory implements Runnable {
	
//	public static final int QUEUE_SIZE = 4096;
	public static final int UPDATE_LIMIT = 200;
	
	/*
	 * Performs the desired action on a provided multi-inventory.
	 */
	public static Queue<UpdateInventoryRequest> requestQueue = new LinkedList<UpdateInventoryRequest>();	
	public static ReentrantLock lock;
		
	public SyncUpdateInventory() {
		lock = new ReentrantLock();
	}

	@Override
	public void run() {
		
		Boolean retBool = false;
		if (lock.tryLock()) {
			try {
				for (int i = 0; i < UPDATE_LIMIT; i++) {
					UpdateInventoryRequest request = requestQueue.poll();
					if (request == null) {
						return;
					}
					
					
					switch(request.action) {
					case ADD:
						int leftovers = request.inv.addItem(request.stack);
						retBool = !(leftovers > 0);
						break;
					case REMOVE:
						try {
							retBool = request.inv.removeItem(request.stack);
						} catch (CivException e) {
							e.printStackTrace();
						}
						break;
					}
					
					request.result = retBool;
					request.finished = true;
					request.condition.signalAll();
				}
			} finally {
				lock.unlock();
			}
		} else {
			//CivLog.warning("Sync update inventory lock is busy, trying again next tick");
		}
	}
}
