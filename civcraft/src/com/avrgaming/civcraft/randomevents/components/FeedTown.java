package com.avrgaming.civcraft.randomevents.components;

import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.main.CivLog;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.randomevents.RandomEventComponent;

public class FeedTown extends RandomEventComponent {
	
	@Override
	public void process() {
		String playerName = this.getParent().componentVars.get(getString("playername_var"));
		if (playerName == null) {
			CivLog.warning("No playername var for pay player.");
			return;
		}
		Resident resident = CivGlobal.getResident(playerName);
		double food = this.getDouble("amount");
		resident.getTown().getFood().giveFood(food);
		CivMessage.sendTown(resident.getTown(), "The town gained "+food+" food!");	
	}
}
