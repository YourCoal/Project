/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.questions;

import com.civcraft.main.CivGlobal;
import com.civcraft.main.CivMessage;
import com.civcraft.object.Civilization;
import com.civcraft.object.Relation;
import com.civcraft.object.Resident;
import com.civcraft.util.CivColor;

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
