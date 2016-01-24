package com.avrgaming.civcraft.questions;

import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.Civilization;
import com.avrgaming.civcraft.object.Relation;
import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.util.CivColor;

public class ChangeRelationResponse implements QuestionResponseInterface {

	public Civilization fromCiv;
	public Civilization toCiv;
	public Relation.Status status;
	
	@Override
	public void processResponse(String param) {
		if (param.equalsIgnoreCase("accept")) {
			CivGlobal.setRelation(fromCiv, toCiv, status);
		} else {
			CivMessage.sendCiv(fromCiv, CivColor.LightGray+toCiv.getName()+" declined our offer.");
		}
	}
	@Override
	public void processResponse(String response, Resident responder) {
		processResponse(response);		
	}
}
