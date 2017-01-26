package com.avrgaming.civcraft.items.components;

import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.util.CivColor;

import gpl.AttributeUtil;

public class MobExperience1 extends ItemComponent {

	@Override
	public void onPrepareCreate(AttributeUtil attrUtil) {
		attrUtil.addLore(CivColor.GoldItalic+"<Right Click for XP>");
	}
	
	public void onInteract(PlayerInteractEvent event) {
		if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			event.setCancelled(true);
			
			Player p = event.getPlayer();
			Resident res = CivGlobal.getResident(p);
			if (p.getInventory().getItemInMainHand().getAmount() > 1) {
				p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount()-1);
			} else {
				p.getInventory().removeItem(p.getInventory().getItemInMainHand());
			}
			res.getTreasury().deposit(5);
			CivMessage.send(res, CivColor.GoldItalic+"Gained 1 Citizen XP");
			return;
		}
	}
}
