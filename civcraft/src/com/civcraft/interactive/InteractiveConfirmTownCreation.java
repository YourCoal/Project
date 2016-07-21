/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.interactive;

import org.bukkit.entity.Player;

import com.civcraft.exception.CivException;
import com.civcraft.main.CivGlobal;
import com.civcraft.main.CivMessage;
import com.civcraft.object.Resident;
import com.civcraft.questions.TownNewRequest;
import com.civcraft.util.CivColor;

public class InteractiveConfirmTownCreation implements InteractiveResponse {

	@Override
	public void respond(String message, Resident resident) {

		Player player;
		try {
			player = CivGlobal.getPlayer(resident);
		} catch (CivException e) {
			return;
		}

		resident.clearInteractiveMode();

		if (!message.equalsIgnoreCase("yes")) {
			CivMessage.send(player, "Town creation cancelled.");
			return;
		}
		
		if (resident.desiredTownName == null) {
			CivMessage.send(player, CivColor.Rose+"Internal Error Creating Town... =(");
			return;
		}
		
		TownNewRequest join = new TownNewRequest();		
		join.resident = resident;
		join.civ = resident.getCiv();
		try {
			CivGlobal.questionLeaders(player, resident.getCiv(), player.getName()+" would like to found the town of "+
					resident.desiredTownName+" at "+player.getLocation().getBlockX()+","+player.getLocation().getBlockY()+","+player.getLocation().getBlockZ(),
					30*1000, join);
		} catch (CivException e) {
			CivMessage.sendError(player, e.getMessage());
			return;
		}
		
		CivMessage.send(player, CivColor.Yellow+"Sent request to civilization leaders for this town. Awaiting their reply.");
//		CivGlobal.questionPlayer(player, CivGlobal.getPlayer(newResident), 
//				"Would you like to join the town of "+town.getName()+"?",
//				INVITE_TIMEOUT, join);
		
//		TaskMaster.syncTask(new FoundTownSync(resident));
	}

}