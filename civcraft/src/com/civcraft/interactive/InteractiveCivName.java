/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.interactive;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.civcraft.exception.CivException;
import com.civcraft.main.CivGlobal;
import com.civcraft.main.CivMessage;
import com.civcraft.object.Resident;
import com.civcraft.util.CivColor;

public class InteractiveCivName implements InteractiveResponse {

	@Override
	public void respond(String message, Resident resident) {
		Player player;
		try {
			player = CivGlobal.getPlayer(resident);
		} catch (CivException e) {
			return;
		}

		if (message.equalsIgnoreCase("cancel")) {
			CivMessage.send(player, "Civilization creation cancelled.");
			resident.clearInteractiveMode();
			return;
		}

		if (!StringUtils.isAlpha(message)) {
			CivMessage.send(player, CivColor.Rose+ChatColor.BOLD+"Civilization names must only contain letters(A-Z). Enter another name.");
			return;
		}
	
		message = message.replace(" ", "_");
		message = message.replace("\"", "");
		message = message.replace("\'", "");
		
		resident.desiredCivName = message;
		CivMessage.send(player, CivColor.LightGreen+"The Civilization of "+CivColor.Yellow+message+CivColor.LightGreen+"? An awesome choice.");
		CivMessage.send(player, " ");
		CivMessage.send(player, CivColor.LightGreen+ChatColor.BOLD+"Now what should your capitol be called?");
		CivMessage.send(player, CivColor.LightGray+"(To cancel type 'cancel')");
		resident.setInteractiveMode(new InteractiveCapitolName());

		return;
		
	}

}
