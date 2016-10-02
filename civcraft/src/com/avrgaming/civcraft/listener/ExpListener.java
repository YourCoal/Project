package com.avrgaming.civcraft.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ExpBottleEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.util.CivColor;

public class ExpListener implements Listener {
	
	public Material item;
	public int ench1 = 0;
	public int ench2 = 0;
	public int ench3 = 0;
	
	@EventHandler(priority = EventPriority.LOW)
	public void onExpBottleEvent(ExpBottleEvent event) {
		event.setExperience(0);
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onItemSpawnEvent(ItemSpawnEvent event) {
//		if (event.getEntity().getType().equals(EntityType.EXPERIENCE_ORB)) {
//			event.setCancelled(true);
//		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			return;
		}
		
		Block block = event.getClickedBlock();
		if (block.getType().equals(Material.ANVIL)) {
			CivMessage.sendError(event.getPlayer(), "Anvils are disabled in CivCraft.");
			event.setCancelled(true);
		}
		
	/**	//TODO
		/*
		 * First, we will ask the player if they want RANDOM or CHOSEN enchantment.
		 * 		1. If they ask for RANDOM, run rand.nextInt() and determine enchant. DO NOT TELL THEM ENCHANT.
		 * 			Now ask if they want RANDOM or CHOSEN level.
		 * 				a. If they ask for RANDOM, run rand.NextInt() and determine level. DO NOT TELL THEM LEVEL
		 * 				b. If they ask for CHOSEN, set this level.
		 * 		2. If they ask for CHOSEN, ask if they want RANDOM or CHOSEN level.
		 * 				a. If they ask for RANDOM, run rand.NextInt() and determine level. DO NOT TELL THEM LEVEL
		 * 				b. If they ask for CHOSEN, set this level.
		 * 		MAKE SURE TO TELL THEM THE FRESH COST AFTER EVERY CHANGE. TODO ALLOW THEM TO UNDO DECISION?
		 * 
		 * TODO not only take coins, but maybe XP? unsure of calculation to balance this yet.
		 * finish.
		*_
		
		if (block.getType().equals(Material.ENCHANTMENT_TABLE)) {
			//CivMessage.sendError(event.getPlayer(), "Cannot use enchantment tables. XP and Levels disabled in CivCraft.");
			event.setCancelled(true);
			
//			Random rand = new Random();
			Resident res = CivGlobal.getResident(event.getPlayer());
			if (event.getPlayer().getItemInHand().getType() != Material.DIAMOND_PICKAXE) {
				return;
			}
			
			CivMessage.sendEnch(res, "Want to enchant your "+event.getPlayer().getItemInHand().getType()+"?");
			CivMessage.send(res, "");
			CivMessage.sendEnch(res, "If you want random enchantment, say 'RANDOM'.");
			CivMessage.sendEnch(res, "If you want a specific enchantment, say 'CHOOSE'.");
			CivMessage.sendEnch(res, "");
			CivMessage.sendEnch(res, CivColor.LightGray+CivColor.ITALIC+"To cancel, type 'cancel'.");
			res.setInteractiveMode(new InteractEnchantStageA());
			
			
//			CivMessage.sendEnch(res, "It will cost "+CivColor.Yellow+CivColor.BOLD+"10,000 coins "+CivColor.RESET+" for a random enchant.");
		}**/
		
		
		/* XXX We do not want to code the stuff above until we can decide what we want to do. for now, I will just make a method below. */
/*		if (block.getType().equals(Material.ENCHANTMENT_TABLE)) {
			//CivMessage.sendError(event.getPlayer(), "Cannot use enchantment tables. XP and Levels disabled in CivCraft.");
			Random rand = new Random();
			Resident res = CivGlobal.getResident(event.getPlayer());
			event.setCancelled(true);
			
			if (event.getPlayer().getExp() <= 0) {
				CivMessage.sendError(res, "You must have at least one level of EXP to enchant.");
			}
			
			if (event.getPlayer().getItemInHand().getType() != Material.DIAMOND_PICKAXE) {
				return;
			}
			
			item = event.getPlayer().getItemInHand().getType();
			
			int expLvl = event.getPlayer().getExpToLevel();
			
			if (expLvl >= 2) {
				ench1 = rand.nextInt(0 + expLvl) + 1;
			}
			if (expLvl >= 8) {
				ench2 = rand.nextInt(0 + expLvl) + 1;
			} else {
				ench1 = rand.nextInt(0 + expLvl) + 1;
			}
			if (expLvl >= 15) {
				ench3 = rand.nextInt(0 + expLvl) + 1;
			} else {
				ench1 = rand.nextInt(0 + expLvl) + 1;
			}
			
			CivMessage.sendEnch(res, "Want to enchant your "+CivColor.LightGray+CivColor.ITALIC+event.getPlayer().getItemInHand().getType().toString()+CivColor.RESET+"? Cool.");
			CivMessage.sendEnch(res, "You have "+CivColor.LightGray+CivColor.ITALIC+expLvl+CivColor.RESET+" levels to use. Please type a listed number below:");
			CivMessage.send(res, "");
			CivMessage.send(res, "    "+CivColor.Green+CivColor.BOLD+ench1);
			CivMessage.send(res, "    "+CivColor.Green+CivColor.BOLD+ench2);
			CivMessage.send(res, "    "+CivColor.Green+CivColor.BOLD+ench3);
			CivMessage.sendEnch(res, "");
			CivMessage.sendEnch(res, CivColor.LightGray+CivColor.ITALIC+"(To cancel, type 'cancel'.)");
			res.setInteractiveMode(new InteractEnchantTEMP());
		}*/
		
		if (block.getType().equals(Material.ENCHANTMENT_TABLE)) {
			event.setCancelled(true);
			CivMessage.sendEnch(event.getPlayer(), "Disabled for now. Sorry!");
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerExpChange(PlayerExpChangeEvent event) {
		Resident resident = CivGlobal.getResident(event.getPlayer());
		CivMessage.send(resident, CivColor.LightGreen+"Picked up "+CivColor.Yellow+event.getAmount()+CivColor.LightGreen+" coins.");
		resident.getTreasury().deposit(event.getAmount());
		event.setAmount(0);
	}
}
