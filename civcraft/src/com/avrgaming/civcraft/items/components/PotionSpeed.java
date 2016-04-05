package com.avrgaming.civcraft.items.components;

import gpl.AttributeUtil;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.lorestorage.LoreMaterial;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.util.CallbackInterface;
import com.avrgaming.civcraft.util.CivColor;

public class PotionSpeed extends ItemComponent implements CallbackInterface{
	
	PlayerPickupItemEvent event;
	
	@Override
	public void onPrepareCreate(AttributeUtil attrUtil) {
		attrUtil.addLore(ChatColor.RESET+CivColor.Gold+"Gives Speed");
		attrUtil.addLore(ChatColor.RESET+CivColor.Rose+"<Right Click To Use>");
	}
	
	public void onInteract(PlayerInteractEvent event) {
		event.setCancelled(true);
		if (!event.getAction().equals(Action.RIGHT_CLICK_AIR) &&
				!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			execute(name);
		}
	}
	
	@Override
	public void execute(String playerName) {
		Player p;
		try {
			p = CivGlobal.getPlayer(playerName);
		} catch (CivException e) {
			return;
		}
		p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 300, 0));
		CivMessage.send(p, "You were given SPEED for 5 MINUTES");
		ItemStack newStack = new ItemStack(LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:speed_item")));
		newStack.setAmount(event.getItem().getItemStack().getAmount()-1);
	}
}
