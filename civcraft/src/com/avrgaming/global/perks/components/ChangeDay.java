package com.avrgaming.global.perks.components;

import org.bukkit.entity.Player;

import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.interactive.InteractiveConfirmDayChange;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.util.CivColor;

public class ChangeDay extends PerkComponent {

	@Override
	public void onActivate(Resident resident) {
		Player player;
		try {
			player = CivGlobal.getPlayer(resident);
		} catch (CivException e) {
			return;
		}
		if (player.getWorld().getTime() <= 10000) {
			CivMessage.sendError(resident, "It is already day. Cannot change time.");
			return;
		}
		
		CivMessage.sendHeading(resident, "Changing Time to Day");
		CivMessage.send(resident, CivColor.Green+"Are you sure you want to make it day?");
		CivMessage.send(resident, CivColor.LightGray+"If so type 'yes', or type anything else to cancel.");
		resident.setInteractiveMode(new InteractiveConfirmDayChange(this));
	}
}
