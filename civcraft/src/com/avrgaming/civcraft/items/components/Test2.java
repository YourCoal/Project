package com.avrgaming.civcraft.items.components;

import gpl.AttributeUtil;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.util.CivColor;

public class Test2 extends ItemComponent {
	
	Player player;
	
	@Override
	public void onPrepareCreate(AttributeUtil attrUtil) {
		attrUtil.addLore(ChatColor.RESET+CivColor.Gold+"Heals 2.5 Hearts");
		attrUtil.addLore(ChatColor.RESET+CivColor.Rose+"<Right Click To Consume>");
	}
	
	public void onInteract(PlayerInteractEvent event) {
		if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			final Player p = (Player) player.getPlayer();
			p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 60, 2));
			CivMessage.send(player, CivColor.ITALIC+"You have healed 2.5 Hearts.");
			return;
		}
	}
}
