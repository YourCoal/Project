package com.avrgaming.civcraft.items.components;

import java.util.Random;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.avrgaming.civcraft.config.ConfigUnit;
import com.avrgaming.civcraft.items.units.Unit;
import com.avrgaming.civcraft.loreenhancements.LoreEnhancement;
import com.avrgaming.civcraft.loreenhancements.LoreEnhancementAttack;
import com.avrgaming.civcraft.loreenhancements.LoreEnhancementThor;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.util.CivColor;

import gpl.AttributeUtil;

public class RangedAttack extends ItemComponent {

	@Override
	public void onPrepareCreate(AttributeUtil attrs) {
//		attrs.setHideFlag(63);
		attrs.addLore(CivColor.Rose+this.getDouble("value")+" Ranged Attack Damage");	
	}
	
	private static double ARROW_MAX_VEL = 6.0; 
	
	@Override
	public void onInteract(PlayerInteractEvent event) {
		if (Unit.isWearingAnyMetal(event.getPlayer())) {
			event.setCancelled(true);
			CivMessage.sendError(event.getPlayer(), "Cannot use a bow while wearing metal armor.");
			return;
		}
	}
	
	@Override
	public void onHold(PlayerItemHeldEvent event) {	
		
		Resident resident = CivGlobal.getResident(event.getPlayer());
		if (!resident.hasTechForItem(event.getPlayer().getInventory().getItem(event.getNewSlot()))) {		
			CivMessage.send(resident, CivColor.Rose+"Warning - "+CivColor.LightGray+
					"You do not have the required technology to use this item. It's attack output will be reduced in half.");
		}
	}
	
	public void onRangedAttack(EntityDamageByEntityEvent event, ItemStack inHand) {
		AttributeUtil attrs = new AttributeUtil(inHand);
		double dmg = this.getDouble("value");
		
		if (event.getDamager() instanceof Arrow) {
			Arrow arrow = (Arrow)event.getDamager();
			if (arrow.getShooter() instanceof Player) {
				Player attacker = (Player)arrow.getShooter();
				if (Unit.isWearingAnyMetal(attacker)) {
					event.setCancelled(true);
					CivMessage.sendError(attacker, "Cannot use a bow while wearing metal armor.");
					return;
				}
			}
		}
		
		double extraAtt = 0.0;
		for (LoreEnhancement enh : attrs.getEnhancements()) {
			if (enh instanceof LoreEnhancementAttack) {
				extraAtt +=  ((LoreEnhancementAttack)enh).getExtraAttack(attrs);
			}
			
			if (enh instanceof LoreEnhancementThor) {
				Random r = new Random();
				int lightning_chance = r.nextInt(20);
				if (lightning_chance == 1) {
					CivMessage.send(event.getDamager(), CivColor.LightBlueItalic+"You bring down lightning on your enemy.");
					event.getEntity().getWorld().strikeLightning(event.getEntity().getLocation());
				}
			}
		}
		
		dmg += extraAtt;
		
		Vector vel = event.getDamager().getVelocity();
		double magnitudeSquared = Math.pow(vel.getX(), 2) + Math.pow(vel.getY(), 2) + Math.pow(vel.getZ(), 2);
		
		double percentage = magnitudeSquared / ARROW_MAX_VEL;
		double totalDmg = percentage * dmg;
		
		if (totalDmg > dmg) {
			totalDmg = dmg;
		}
		
		if (event.getDamager() instanceof Arrow) {
			Arrow arrow = (Arrow)event.getDamager();
			if (arrow.getShooter() instanceof Player) {
				Player p = (Player)arrow.getShooter();
				Resident res = CivGlobal.getResident((Player)arrow.getShooter());
				
				ConfigUnit unit = Unit.getPlayerUnit(p);
				if (unit != null && unit.id.equals("u_warrior")) {
					dmg = dmg - (dmg*0.1);
				} else if (unit != null && unit.id.equals("u_berserker")) {
					dmg *= 1.10;
				} else if (unit != null && unit.id.equals("u_archer")) {
					dmg *= 1.25;
				} else if (unit != null && unit.id.equals("u_swordsman")) {
					dmg = dmg - (dmg*0.2);
				} else if (unit != null && unit.id.equals("u_berserker")) {
					dmg *= 1.10;
				} else if (unit != null && unit.id.equals("u_crossbowman")) {
					dmg *= 1.40;
				}
				
				if (!res.hasTechForItem(inHand)) {
					totalDmg = totalDmg / 2;
				}
			}
		}
		
//		if (totalDmg < 0.5) { /* Always do at least 0.5 damage. */
//			totalDmg = 0.5;
//		}
		event.setDamage(totalDmg);
	}
}
