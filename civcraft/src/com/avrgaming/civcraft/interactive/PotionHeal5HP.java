package com.avrgaming.civcraft.interactive;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.Resident;

public class PotionHeal5HP implements InteractiveResponse {

	@Override
	public void respond(String message, Resident resident) {
		Player player;
		try {
			player = CivGlobal.getPlayer(resident);
		} catch (CivException e) {
			return;
		}
		if (message.equalsIgnoreCase("cancel")) {
			CivMessage.send(player, "No loner consuming 5HP Healer.");
			resident.clearInteractiveMode();
			return;
		} else if (message.equalsIgnoreCase("yes")) {
			final Player p = (Player) player.getPlayer();
			p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 60, 2));
		} else {
			CivMessage.send(player, "Please say 'yes' or 'cancel'.");
		} return;
	}
}