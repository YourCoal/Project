package com.avrgaming.civcraft.questions;

import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.Civilization;
import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.util.CivColor;

public class SurrenderRequest implements QuestionResponseInterface {

	public Civilization fromCiv;
	public Civilization toCiv;
	
	@Override
	public void processResponse(String param) {
		if (param.equalsIgnoreCase("accept")) {
			fromCiv.onDefeat(toCiv);
			CivMessage.global(fromCiv.getName()+" has surrendered to "+toCiv.getName());
		} else {
			CivMessage.sendCiv(fromCiv, CivColor.LightGray+toCiv.getName()+" declined our offer.");
		}
	}

	@Override
	public void processResponse(String response, Resident responder) {
		processResponse(response);		
	}
}
