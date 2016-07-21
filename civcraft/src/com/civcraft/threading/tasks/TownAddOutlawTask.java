/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.threading.tasks;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.civcraft.exception.CivException;
import com.civcraft.main.CivGlobal;
import com.civcraft.main.CivMessage;
import com.civcraft.object.Town;
import com.civcraft.util.CivColor;

public class TownAddOutlawTask implements Runnable {

	String name;
	Town town;
	
	
	public TownAddOutlawTask(String name, Town town) {
		this.name = name;
		this.town = town;
	}

	@Override
	public void run() {
		
		try {
			Player player = CivGlobal.getPlayer(name);
			CivMessage.send(player, CivColor.Yellow+ChatColor.BOLD+"You are now an outlaw to "+town.getName()+" towers will fire upon you if you visit them!");
		} catch (CivException e) {
		}
		
		town.addOutlaw(name);
		town.save();
		CivMessage.sendTown(town, CivColor.Yellow+name+" is now an outlaw in this town!");
		
	}
	
}
