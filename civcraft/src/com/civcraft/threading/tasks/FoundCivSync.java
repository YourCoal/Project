/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.threading.tasks;

import org.bukkit.entity.Player;

import com.civcraft.exception.CivException;
import com.civcraft.main.CivGlobal;
import com.civcraft.main.CivMessage;
import com.civcraft.object.Civilization;
import com.civcraft.object.Resident;
import com.civcraft.util.CivColor;

public class FoundCivSync implements Runnable {

	Resident resident;
	
	public FoundCivSync(Resident resident) {
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
			Civilization.newCiv(resident.desiredCivName, resident.desiredCapitolName, resident, player, resident.desiredTownLocation);
		} catch (CivException e) {
			CivMessage.send(player, CivColor.Rose+e.getMessage());
		}
		
	}

	
	
}
