/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.threading.sync;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;

import com.civcraft.main.CivData;
import com.civcraft.threading.sync.request.GetChestRequest;
import com.civcraft.util.ItemManager;

public class SyncGetChestInventory implements Runnable {
	
	public static final int TIMEOUT_SECONDS = 2;
	public static final int UPDATE_LIMIT = 20;
	
	public static ReentrantLock lock;
	
	public static Queue<GetChestRequest> requestQueue = new LinkedList<GetChestRequest>();
	
	public static boolean add(GetChestRequest request)  
	{
		//XXX is this needed anymore?
		return requestQueue.offer(request);
	}
	
	public SyncGetChestInventory() {
		lock = new ReentrantLock();
	}
	
	@Override
	public void run() {
		
		if (lock.tryLock()) {
			try {	
				for (int i = 0; i < UPDATE_LIMIT; i++) {
					GetChestRequest request = requestQueue.poll();
					if (request == null) {
						return;
					}
					
					Block b = Bukkit.getWorld(request.worldName).getBlockAt(request.block_x, request.block_y, request.block_z);
					Chest chest = null;

					// We will return NULL if the chunk was not loaded.
					if (b.getChunk().isLoaded()) {
						try {
							chest = (Chest)b.getState();
						} catch (ClassCastException e) {
							/* The block wasn't a chest, but force it. */
							ItemManager.setTypeId(b, CivData.CHEST);
							ItemManager.setTypeId(b.getState(), CivData.CHEST);
							b.getState().update();
							chest = (Chest)b.getState();
							
						}
					} 
			
					/* Set the result and signal all threads we're complete. */					
					request.result = chest.getBlockInventory();
					request.finished = true;
					request.condition.signalAll();
					
				}
			} finally {
				lock.unlock();
			}
		} else {
		//	CivLog.warning("Unable to aquire lock in sync tick thread. Lock busy.");
		}
	}
}