/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.threading.sync;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;
import org.kitteh.tag.TagAPI;


public class SyncUpdateTagsBetweenCivs implements Runnable {
	Set<Player> civList = new HashSet<Player>();
	Set<Player> otherCivList = new HashSet<Player>();
	
	public SyncUpdateTagsBetweenCivs(Set<Player> civList, Set<Player> otherCivList) {
		this.civList = civList;
		this.otherCivList = otherCivList;
	}

	@Override
	public void run() {
		
		for (Player player : civList) {
			if (!otherCivList.isEmpty()) {
				TagAPI.refreshPlayer(player, otherCivList);
			}
		}
		
		for (Player player : otherCivList) {
			if (!civList.isEmpty()) {
				TagAPI.refreshPlayer(player, civList);
			}
		}
			
	}
	
}
