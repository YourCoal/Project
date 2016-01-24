package com.avrgaming.civcraft.questions;

import com.avrgaming.civcraft.main.CivLog;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.Civilization;
import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.object.Town;
import com.avrgaming.civcraft.util.CivColor;

public class DiplomacyGiftResponse implements QuestionResponseInterface {

	public Object giftedObject;
	public Civilization fromCiv;
	public Civilization toCiv;
	
	@Override
	public void processResponse(String param) {
		if (param.equalsIgnoreCase("accept")) {
			
			if (giftedObject instanceof Town) {
				Town town = (Town)giftedObject;
				
				if (!toCiv.getTreasury().hasEnough(town.getGiftCost())) {
					CivMessage.sendCiv(toCiv, CivColor.Rose+" We cannot accept the town of "+town.getName()+" as a gift because we do not have the required "+town.getGiftCost()+" coins.");
					CivMessage.sendCiv(fromCiv, CivColor.Rose+toCiv.getName()+" cannot accept the town of "+town.getName()+" as a gift because they did not have the required "+
							town.getGiftCost()+" coins.");
					return;
				}
				
				toCiv.getTreasury().withdraw(town.getGiftCost());
				town.changeCiv(toCiv);
				CivMessage.sendCiv(fromCiv, CivColor.LightGray+toCiv.getName()+" has accepted the offer of our town of "+town.getName());
				return;
			} else if (giftedObject instanceof Civilization) {
				int coins = fromCiv.getMergeCost();
				
				if (!toCiv.getTreasury().hasEnough(coins)) {
					CivMessage.sendCiv(toCiv, CivColor.Rose+" We cannot accept the merge of "+fromCiv.getName()+" because we do not have the required "+coins+" coins.");
					CivMessage.sendCiv(fromCiv, CivColor.Rose+toCiv.getName()+" cannot accept the merge of "+fromCiv.getName()+" because they do not have the required "+coins+" coins.");
					return;
				}
				
				toCiv.getTreasury().withdraw(coins);
				CivMessage.sendCiv(fromCiv, CivColor.Yellow+toCiv.getName()+" has accepted the offer, our civ is now merging with theirs!");
				toCiv.mergeInCiv(fromCiv);
				CivMessage.global("The Civilization of "+fromCiv.getName()+" has agreed to merge into the Civilizaiton of "+toCiv.getName());
				return;
			} else {
				CivLog.error("Unexpected object in gift response:"+giftedObject);
				return;
			}
		} else {
			CivMessage.sendCiv(fromCiv, CivColor.LightGray+toCiv.getName()+" declined our offer.");
		}
		
	}
	@Override
	public void processResponse(String response, Resident responder) {
		processResponse(response);		
	}
}
