package com.avrgaming.civcraft.threading.timers;

import java.util.LinkedList;
import java.util.Queue;

import org.bukkit.entity.Player;

import com.avrgaming.civcraft.cache.PlayerLocationCache;
import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.main.CivGlobal;

public class PlayerLocationCacheUpdate implements Runnable {

	public static int UPDATE_LIMIT = 20;
	public static Queue<String> playerQueue = new LinkedList<String>();
	
	@Override
	public void run() {

	//	if (PlayerLocationCache.lock.tryLock()) {		
			try {
				for (int i = 0; i < UPDATE_LIMIT; i++) {
					String playerName = playerQueue.poll();
					if (playerName == null) {
						return;
					}
					
					try {
						Player player = CivGlobal.getPlayer(playerName);
						PlayerLocationCache.updateLocation(player);
						playerQueue.add(playerName);
						
					} catch (CivException e) {
						// player not online. remove from queue by not re-adding.
						PlayerLocationCache.remove(playerName);
						continue;
					}
				}
			} finally {
			//	PlayerLocationCache.lock.unlock();
			}
		//}
	}

}
