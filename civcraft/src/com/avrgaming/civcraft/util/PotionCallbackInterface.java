package com.avrgaming.civcraft.util;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.avrgaming.civcraft.exception.CivException;

public interface PotionCallbackInterface {
	
	public void usePotion(PlayerInteractEvent event, Player player) throws CivException;
}
