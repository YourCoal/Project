package com.avrgaming.civcraft.interactive;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.listener.ExpListener;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.Resident;

public class InteractEnchantTEMP implements InteractiveResponse {
	
	ExpListener expl;
	
	@Override
	public void respond(String message, Resident res) {
		Player p;
		try {
			p = CivGlobal.getPlayer(res);
		} catch (CivException e) {
			return;
		}
		
		Random rand = new Random();
		if (message.equalsIgnoreCase("cancel")) {
			CivMessage.sendEnch(p, "You decided to opt out.");
			res.clearInteractiveMode();
			return;
		}
		
		if (message.equalsIgnoreCase(""+expl.ench1)) {
			if (p.getPlayer().getItemInHand().getType() != expl.item) {
				CivMessage.sendError(res, "How dare you switch items! Please start all over.");
				return;
			}
			try {
				CivMessage.sendEnch(res, "Processing your enchantments...");
				synchronized(this) {
					this.wait(2000);
				}
				
				int ench1 = expl.ench1;
//				int ench2 = expl.ench2;
//				int ench3 = expl.ench3;
				
				//XXX https://github.com/Bukkit/Bukkit/blob/master/src/main/java/org/bukkit/enchantments/Enchantment.java
				if (p.getPlayer().getItemInHand().getType() == Material.DIAMOND_PICKAXE) {
					int eff1 = rand.nextInt(0 + ench1) + 1;
					if (eff1 == ench1) {
						p.getPlayer().getItemInHand().addEnchantment(Enchantment.DIG_SPEED, 1);
					}
					
					int eff2 = rand.nextInt(0 + ench1)*4 + 1;
					if (eff2 == ench1) {
						p.getPlayer().getItemInHand().addEnchantment(Enchantment.DIG_SPEED, 2);
					}
					
					int unbrk1 = rand.nextInt(0 + ench1)*8 + 1;
					if (unbrk1 == ench1) {
						p.getPlayer().getItemInHand().addEnchantment(Enchantment.DURABILITY, 1);
					}
				}
				CivMessage.sendEnch(res, "Successfully enchanted your item!");
				return;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		//TODO FIX MY LIFE
		if (message.equalsIgnoreCase("choose")) {
//			try {
				CivMessage.sendEnch(p, "You want to make your own choice? Awesome.");
				CivMessage.sendEnch(p, "Please choose an enchantment for this item:");
					//Make a list of enchants now
				
				//TODO Now make a rand.nextInt() here to decide enchantment.
				
//				synchronized(this) {
//					this.wait(2000);
//				}
				
//				CivMessage.sendEnch(p, "Your enchantment will cost "+CivColor.Yellow+CivColor.BOLD+cost+" coins"+CivColor.RESET+".");
//				CivMessage.sendEnch(res, "Now, what enchantment level do you want?");
//				CivMessage.send(res, "");
//				CivMessage.sendEnch(res, "If you want random level, say 'RANDOM'.");
//				CivMessage.sendEnch(res, "If you want a specific level, say 'CHOOSE'.");
//				CivMessage.sendEnch(res, "");
//				CivMessage.sendEnch(res, CivColor.LightGray+CivColor.ITALIC+"To cancel, type 'cancel'.");
//				res.setInteractiveMode(new InteractEnchantStageB1());
				return;
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
		}
		
		
		//TODO Add stage B1 (random) or B2 (choose) here
//		Camp.newCamp(resident, player, message);
		return;
	}
}
