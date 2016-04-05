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
import com.avrgaming.civcraft.threading.TaskMaster;
import com.avrgaming.civcraft.util.CallbackInterface;
import com.avrgaming.civcraft.util.CivColor;

public class Heal5HP extends ItemComponent implements CallbackInterface {

	@Override
	public void onPrepareCreate(AttributeUtil attrUtil) {
		attrUtil.addLore(ChatColor.RESET+CivColor.Gold+"Heals 2.5 Hearts");
		attrUtil.addLore(ChatColor.RESET+CivColor.Rose+"<Right Click To Consume>");
	}
	
	public void check(Player player) throws CivException {
		Resident resident = CivGlobal.getResident(player);
		if (resident == null) {
			throw new CivException("You must be a registered resident. Re-log or contact an admin.");
		}
	}
	
	public void onInteract(PlayerInteractEvent event) {
		event.setCancelled(true);
		if (!event.getAction().equals(Action.RIGHT_CLICK_AIR) &&
				!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			return;
		}
		
		class SyncTask implements Runnable {
			String name;
			
			public SyncTask(String name) {
				this.name = name;
			}
			
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				Player player;
				try {
					player = CivGlobal.getPlayer(name);
					try {
						check(player);
					} catch (CivException e) {
						CivMessage.sendError(player, e.getMessage());
					}
				} catch (CivException e) {
					return;
				}
				player.updateInventory();
			}
		}
		TaskMaster.syncTask(new SyncTask(event.getPlayer().getName()));
	}
	
	@Override
	public void execute(String playerName) {
		Player p;
		try {
			p = CivGlobal.getPlayer(playerName);
		} catch (CivException e) {
			return;
		}
		if (p == null) {
			CivMessage.sendError(p, "You are not a resident? Relog or get an admin!");
		} else {
			p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 60, 2));
			CivMessage.send(p, CivColor.ITALIC+"You have healed 2.5 Hearts.");
		}
	}
}