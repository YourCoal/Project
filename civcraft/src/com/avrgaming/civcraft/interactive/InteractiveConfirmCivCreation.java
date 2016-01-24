package com.avrgaming.civcraft.interactive;

import org.bukkit.entity.Player;

import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.threading.TaskMaster;
import com.avrgaming.civcraft.threading.tasks.FoundCivSync;
import com.avrgaming.civcraft.util.CivColor;

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
