package com.avrgaming.civcraft.items.components;

import gpl.AttributeUtil;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.util.CivColor;

public class Heal5HP extends ItemComponent {

	@Override
	public void onPrepareCreate(AttributeUtil attrUtil) {
		attrUtil.addLore(ChatColor.RESET+CivColor.Gold+"Heals 2.5 Hearts");
		attrUtil.addLore(ChatColor.RESET+CivColor.Rose+"<Right Click To Consume>");
	}
	
	public void usePotion(PlayerInteractEvent event, Player player) throws CivException {
		if (!event.getAction().equals(Action.RIGHT_CLICK_AIR) && !event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			Resident resident = CivGlobal.getResident(player);
			if (resident == null) {
				throw new CivException("You are not a resident? Relog or get an admin!");
			} else {
				final Player p = (Player) player.getPlayer();
				p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 60, 2));
				CivMessage.send(player, CivColor.ITALIC+"You have healed 2.5 Hearts.");
			}
		}
	}
}
