/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.test;

import java.util.Date;
import java.util.Random;

import com.civcraft.exception.CivTaskAbortException;
import com.civcraft.main.CivLog;
import com.civcraft.threading.CivAsyncTask;

public class TestGetChestThread extends CivAsyncTask {
	
	public TestGetChestThread() {
	}
	
	@Override
	public void run() {
		
		Date startTime = new Date();
		long start = startTime.getTime();
		int requests = 0;
		
		while (true) {
			Random rand = new Random();
			try {
				Date nowTime = new Date();
				long now = nowTime.getTime();
				
				this.getChestInventory("world", rand.nextInt(2000), rand.nextInt(200), rand.nextInt(2000), true);
				requests++;
				
				long diff = now -  start;
				if (diff > 5000) {
					start = now;
					double requestsPerSecond = (double)requests / ((double)diff/1000);
					CivLog.warning("Processed "+requestsPerSecond+" requests per second.");
					requests = 0;
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CivTaskAbortException e) {
				CivLog.warning("Can't keep up! "+e.getMessage());
			}
		}
		
	}

	
	
}
