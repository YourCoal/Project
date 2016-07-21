/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.threading.tasks;

import org.bukkit.entity.Player;

import com.civcraft.exception.CivException;
import com.civcraft.main.CivGlobal;
import com.civcraft.main.CivMessage;
import com.civcraft.object.Resident;
import com.civcraft.object.Town;
import com.civcraft.util.CivColor;

public class FoundTownSync implements Runnable {

	Resident resident;
	
	public FoundTownSync(Resident resident) {
		this.resident = resident;
	}
	
	@Override
	public void run() {
		Player player;
		try {
			player = CivGlobal.getPlayer(resident);
		} catch (CivException e1) {
			return;
		}
		
		try {
			Town.newTown(resident, resident.desiredTownName, resident.getCiv(), false, false, resident.desiredTownLocation);
		} catch (CivException e) {
			CivMessage.send(player, CivColor.Rose+e.getMessage());
			return;
		}

		//CivMessage.sendSuccess(sender, "Town "+args[1]+" has been founded.");
		CivMessage.global(resident.desiredTownName+" has been founded by "+resident.getCiv().getName()+"!");
	}

}
