package com.avrgaming.civcraft.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.Civilization;
import com.avrgaming.civcraft.object.Resident;

public class ChatListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	void OnPlayerAsyncChatEvent(AsyncPlayerChatEvent event) {
		
		Resident resident = CivGlobal.getResident(event.getPlayer());
		if (resident == null) {
			/* resident not found, I guess just let the chat through. */
			return;
		}
		
		if (resident.isTownChat()) {
			event.setCancelled(true);
			if (resident.getTownChatOverride() == null) {
				CivMessage.sendTownChat(resident.getTown(), resident, event.getFormat(), event.getMessage());
			} else {
				CivMessage.sendTownChat(resident.getTownChatOverride(), resident, event.getFormat(), event.getMessage());
			}
		}
		
		if (resident.isCivChat()) {
			Civilization civ;
			event.setCancelled(true);
			if (resident.getTown() == null) {
				civ = null;
			} else {
				civ = resident.getTown().getCiv();
			}
			
			if (resident.getCivChatOverride() == null) {
				CivMessage.sendCivChat(civ, resident, event.getFormat(), event.getMessage());
			} else {
				CivMessage.sendCivChat(resident.getCivChatOverride(), resident, event.getFormat(), event.getMessage());
			}
		}
		
		if (resident.isInteractiveMode()) {
			resident.getInteractiveResponse().respond(event.getMessage(), resident);
			event.setCancelled(true);
		}
		
	//	CivLog.debug("Got message:"+event.getMessage());
		//event.setFormat("[[[%s %s]]]");
	}
	
}
