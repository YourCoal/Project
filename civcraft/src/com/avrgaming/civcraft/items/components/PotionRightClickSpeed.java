package com.avrgaming.civcraft.items.components;

import gpl.AttributeUtil;

import java.util.Collection;

import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PotionRightClickSpeed extends ItemComponent {
	
	Player player;
	
	@SuppressWarnings("unchecked")
	public void onInteract(PlayerInteractEvent event) {
		if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			player.addPotionEffects((Collection<PotionEffect>) PotionEffectType.SPEED);
		}
	}

	@Override
	public void onPrepareCreate(AttributeUtil attrUtil) {
	}
}
