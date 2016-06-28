package com.avrgaming.civcraft.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ExpBottleEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.avrgaming.civcraft.config.CivSettings;
import com.avrgaming.civcraft.main.CivData;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.util.CivColor;
import com.avrgaming.civcraft.util.ItemManager;

//XXX Completely reworking, began 1.1pre6
public class DisableXPListener implements Listener {
	
	@EventHandler(priority = EventPriority.LOW)
	public void onExpBottleEvent(ExpBottleEvent event) {
//		event.setExperience(0);
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onItemSpawnEvent(ItemSpawnEvent event) {
//		if (event.getEntity().getType().equals(EntityType.EXPERIENCE_ORB)) {
//			event.setCancelled(true);
//		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			return;
		}
		
		if (event.getClickedBlock() == null || ItemManager.getId(event.getClickedBlock()) == CivData.AIR) {
			return;
		}
		
		Block block = event.getClickedBlock();
		if (block.getType().equals(Material.ENCHANTMENT_TABLE)) {
			CivMessage.sendError(event.getPlayer(), CivSettings.localize.localizedString("customItem_enchantTableDisabled"));
			event.setCancelled(true);
		}
		
		if (block.getType().equals(Material.ANVIL)) {
/*			// Started to get annoyed not being able to rename items as OP. This makes it easier.
			if (!(event.getPlayer().isOp())) {
				CivMessage.sendError(event.getPlayer(), CivSettings.localize.localizedString("customItem_anvilDisabled"));
				event.setCancelled(true);
			}*/
			
			Player p = event.getPlayer();
			CivMessage.send(p, CivColor.LightGray+"[Anvil] "+CivColor.White+"I can rename the "+p.getItemInHand().getType()+" in your hand!");
			CivMessage.send(p, CivColor.LightGray+"[Anvil] "+CivColor.White+"Sadly, I am not enabled right now. Sorry!");
		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerExpChange(PlayerExpChangeEvent event) {
		int citXP = event.getAmount()/2;
		int coins = event.getAmount();
		int exp = coins/4;
		Resident resident = CivGlobal.getResident(event.getPlayer());
		
		CivMessage.send(resident, CivColor.LightGreen+"You've picked up "+CivColor.Yellow+coins+CivColor.LightGreen+" coins, "
					+CivColor.LightBlue+exp+CivColor.LightGreen+" EXP, "+CivColor.Rose+citXP+CivColor.LightGreen+" Citizen XP");
		
		resident.addAccumulatedCitizenXP(citXP);
		resident.getTreasury().deposit(coins);
		event.setAmount(exp);
	}
}
