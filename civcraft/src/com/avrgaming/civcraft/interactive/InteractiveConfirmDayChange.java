package com.avrgaming.civcraft.interactive;

import org.bukkit.entity.Player;

import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.util.CivColor;
import com.avrgaming.global.perks.components.ChangeDay;

public class InteractiveConfirmDayChange implements InteractiveResponse {
	
	ChangeDay perk;
	public InteractiveConfirmDayChange(ChangeDay perk) {
		this.perk = perk;
	}
	
	@Override
	public void respond(String message, Resident resident) {
		resident.clearInteractiveMode();
		if (message.equalsIgnoreCase("yes")) {
			Player player;
			try {
				player = CivGlobal.getPlayer(resident);
				player.getWorld().setTime(0);
				CivMessage.globalPerk(resident.getName()+" has used a "+CivColor.Yellow+CivColor.BOLD+"Day Change"+CivColor.RESET+" token to make it day!");
				perk.markAsUsed(resident);
			} catch (CivException e) {
			}
		} else {
			CivMessage.send(resident, "Day Change cancelled.");
		}
	}
}
