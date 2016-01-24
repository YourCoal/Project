package com.avrgaming.civcraft.questions;

import org.bukkit.entity.Player;

import com.avrgaming.civcraft.camp.Camp;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.util.CivColor;

public class JoinCampResponse implements QuestionResponseInterface {

	public Camp camp;
	public Resident resident;
	public Player sender;
	
	@Override
	public void processResponse(String param) {
		if (param.equalsIgnoreCase("accept")) {
			CivMessage.send(sender, CivColor.LightGray+resident.getName()+" accepted our camp invitation.");
			
			if (!camp.hasMember(resident.getName())) {
				camp.addMember(resident);
				CivMessage.sendCamp(camp, resident.getName()+" has joined the camp.");
				resident.save();
			}
		} else {
			CivMessage.send(sender, CivColor.LightGray+resident.getName()+" denied our camp invitation.");
		}
	}
	
	@Override
	public void processResponse(String response, Resident responder) {
		processResponse(response);		
	}
}
