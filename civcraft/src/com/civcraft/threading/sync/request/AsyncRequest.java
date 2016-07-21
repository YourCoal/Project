/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.threading.sync.request;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class AsyncRequest {
	
	public AsyncRequest(ReentrantLock lock) {
		condition = lock.newCondition();
	}
	
	public Condition condition;
	public Boolean finished = false;
	public Object result = null;
	
}
