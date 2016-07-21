/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.database;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import com.civcraft.object.SQLObject;

public class SQLUpdate implements Runnable {
	
//	public static final int QUEUE_SIZE = 4096;
//	public static final int UPDATE_LIMIT = 50;
//	public static ReentrantLock lock = new ReentrantLock();
	
	private static ConcurrentLinkedQueue<SQLObject> saveObjects = new ConcurrentLinkedQueue<SQLObject>();
	public static ConcurrentHashMap<String, Integer> statSaveRequests = new ConcurrentHashMap<String, Integer>();
	public static ConcurrentHashMap<String, Integer> statSaveCompletions = new ConcurrentHashMap<String, Integer>();

	public static void add(SQLObject obj) {
		
		Integer count = statSaveRequests.get(obj.getClass().getSimpleName());
		if (count == null) {
			count = 0;
		}
		statSaveRequests.put(obj.getClass().getSimpleName(), ++count);
		
		saveObjects.add(obj);
	}
	
	@Override
	public void run() {	
		while(true) {
			try {
				
				SQLObject obj = saveObjects.poll();
				if (obj == null) {
					continue;
				}
				
				obj.saveNow();
				
				Integer count = statSaveCompletions.get(obj.getClass().getSimpleName());
				if (count == null) {
					count = 0;
				}
				statSaveCompletions.put(obj.getClass().getSimpleName(), ++count);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}	
}
