package com.avrgaming.civcraft.threading.tasks;

import org.bukkit.entity.Player;

import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.object.Town;
import com.avrgaming.civcraft.util.CivColor;

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