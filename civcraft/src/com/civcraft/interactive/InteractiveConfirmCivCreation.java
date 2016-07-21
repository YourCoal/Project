/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.interactive;

import org.bukkit.entity.Player;

import com.civcraft.exception.CivException;
import com.civcraft.main.CivGlobal;
import com.civcraft.main.CivMessage;
import com.civcraft.object.Resident;
import com.civcraft.threading.TaskMaster;
import com.civcraft.threading.tasks.FoundCivSync;
import com.civcraft.util.CivColor;

public class InteractiveConfirmCivCreation implements InteractiveResponse {

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
			CivMessage.send(player, "Civilization creation cancelled.");
			return;
		}
		
		if (resident.desiredCapitolName == null || resident.desiredCivName == null) {
			CivMessage.send(player, CivColor.Rose+"Internal Error Creating Civ... =(");
			return;
		}
		
		TaskMaster.syncTask(new FoundCivSync(resident));

	}

}
