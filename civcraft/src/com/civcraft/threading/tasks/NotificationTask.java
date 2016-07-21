/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.threading.tasks;

import org.bukkit.entity.Player;

import com.civcraft.exception.CivException;
import com.civcraft.main.CivGlobal;
import com.civcraft.main.CivMessage;

public class NotificationTask implements Runnable {
	//private Server server;
	String message;
	String playerName;
	
	public NotificationTask(String playerName, String msg) {
		message = msg;
		this.playerName = playerName;
	}

	@Override
	public void run() {
		try {
			Player player = CivGlobal.getPlayer(playerName);
			CivMessage.send(player, message);
		} catch (CivException e) {
			//Player not online
		}
		
	}
}