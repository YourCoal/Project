/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.threading.sync;

import java.util.Collection;

import org.bukkit.entity.Player;
import org.kitteh.tag.TagAPI;

import com.civcraft.exception.CivException;
import com.civcraft.main.CivGlobal;
import com.civcraft.object.Resident;

public class SyncUpdateTags implements Runnable {

	Collection<Resident> residentsToSendUpdate;
	String playerToUpdate;
	
	public SyncUpdateTags(String playerToUpdate, Collection<Resident> residentsToSendUpdate) {
		this.residentsToSendUpdate = residentsToSendUpdate;
		this.playerToUpdate = playerToUpdate;
	}

	@Override
	public void run() {
		try {
			Player player = CivGlobal.getPlayer(playerToUpdate);		
			for (Resident resident : residentsToSendUpdate) {
				try {
					Player resPlayer = CivGlobal.getPlayer(resident);
					if (player == resPlayer) {
						continue;
					}
					
					TagAPI.refreshPlayer(player, resPlayer);
					TagAPI.refreshPlayer(resPlayer, player);
				} catch (CivException e) {
					// one of these players is not online.
				}
			}
			
			
		} catch (CivException e1) {
			return;
		}		
	}
	
}
