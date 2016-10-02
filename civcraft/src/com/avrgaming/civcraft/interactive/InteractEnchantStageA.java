package com.avrgaming.civcraft.interactive;

import org.bukkit.entity.Player;

import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.Resident;

public class InteractEnchantStageA implements InteractiveResponse {
	
	@Override
	public void respond(String message, Resident res) {
		Player p;
		try {
			p = CivGlobal.getPlayer(res);
		} catch (CivException e) {
			return;
		}
		
		if (message.equalsIgnoreCase("cancel")) {
			CivMessage.sendEnch(p, "You decided to opt out.");
			res.clearInteractiveMode();
			return;
		}
		
		/*
		 * Prices will be determined when Library enchants get re-configed.
		 * 'Random' option price will be 10% less cost than library.
		 * 'Choose' option price will be 10% more cost than library.
		 * TODO: Make library just enchanting room? c:
		*/
		
		if (message.equalsIgnoreCase("random")) {
			try {
				CivMessage.sendEnch(p, "You want to make a random choice? Awesome.");
				CivMessage.sendEnch(p, "Processing your decision...");
				
				//TODO Now make a rand.nextInt() here to decide enchantment.
				
				synchronized(this) {
					this.wait(2000);
				}
				
//				CivMessage.sendEnch(p, "Your enchantment will cost "+CivColor.Yellow+CivColor.BOLD+cost+" coins"+CivColor.RESET+".");
//				CivMessage.sendEnch(res, "Now, what enchantment level do you want?");
//				CivMessage.send(res, "");
//				CivMessage.sendEnch(res, "If you want random level, say 'RANDOM'.");
//				CivMessage.sendEnch(res, "If you want a specific level, say 'CHOOSE'.");
//				CivMessage.sendEnch(res, "");
//				CivMessage.sendEnch(res, CivColor.LightGray+CivColor.ITALIC+"To cancel, type 'cancel'.");
//				res.setInteractiveMode(new InteractEnchantStageB1());
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
