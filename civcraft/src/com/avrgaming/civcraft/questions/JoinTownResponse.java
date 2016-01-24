package com.avrgaming.civcraft.questions;

import org.bukkit.entity.Player;

import com.avrgaming.civcraft.exception.AlreadyRegisteredException;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.object.Town;
import com.avrgaming.civcraft.util.CivColor;

public class JoinTownResponse implements QuestionResponseInterface {

	public Town town;
	public Resident resident;
	public Player sender;
	
	@Override
	public void processResponse(String param) {
		if (param.equalsIgnoreCase("accept")) {
			CivMessage.send(sender, CivColor.LightGray+resident.getName()+" accepted our town invitation.");
			
			try {
				town.addResident(resident);
			} catch (AlreadyRegisteredException e) {
				CivMessage.sendError(sender, resident.getName()+" is already a town member.");
				return;
			}

			CivMessage.sendTown(town, resident.getName()+" has joined the town.");
			resident.save();
		} else {
			CivMessage.send(sender, CivColor.LightGray+resident.getName()+" denied our town invitation.");
		}
	}
	
	@Override
	public void processResponse(String response, Resident responder) {
		processResponse(response);		
	}
}
