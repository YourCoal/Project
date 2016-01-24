package com.avrgaming.civcraft.threading.tasks;

import org.bukkit.entity.Player;

import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.Civilization;
import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.util.CivColor;

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
