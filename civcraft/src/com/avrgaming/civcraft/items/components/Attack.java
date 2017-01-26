package com.avrgaming.civcraft.items.components;

import java.util.Random;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

import com.avrgaming.civcraft.config.ConfigUnit;
import com.avrgaming.civcraft.items.units.Unit;
import com.avrgaming.civcraft.loreenhancements.LoreEnhancement;
import com.avrgaming.civcraft.loreenhancements.LoreEnhancementAttack;
import com.avrgaming.civcraft.loreenhancements.LoreEnhancementBonusDamageI;
import com.avrgaming.civcraft.loreenhancements.LoreEnhancementBonusDamageII;
import com.avrgaming.civcraft.loreenhancements.LoreEnhancementThor;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.util.CivColor;

import gpl.AttributeUtil;
import gpl.AttributeUtil.Attribute;
import gpl.AttributeUtil.AttributeType;

public class Attack extends ItemComponent {
	
	@Override
	public void onPrepareCreate(AttributeUtil attrs) {
		attrs.add(Attribute.newBuilder().name("Attack").
				type(AttributeType.GENERIC_ATTACK_DAMAGE).
				amount(this.getDouble("value")).build());
		attrs.addLore(CivColor.Rose+""+this.getDouble("value")+" Attack");
		return;
	}
	
	@Override
	public void onHold(PlayerItemHeldEvent event) {	
		Resident resident = CivGlobal.getResident(event.getPlayer());
		if (!resident.hasTechForItem(event.getPlayer().getInventory().getItem(event.getNewSlot()))) {
			CivMessage.send(resident, CivColor.Rose+"Warning - "+CivColor.LightGray+
					"You do not have the required technology to use this item. It's attack output will be reduced in half.");
		}
	}
	
	@Override
	public void onAttack(EntityDamageByEntityEvent event, ItemStack inHand) {
		AttributeUtil attrs = new AttributeUtil(inHand);
		double dmg = this.getDouble("value");
		double extraAtt = 0.0;
		/* Try to get any extra attack enhancements from this item. */
		for (LoreEnhancement enh : attrs.getEnhancements()) {
			if (enh instanceof LoreEnhancementAttack) {
				extraAtt +=  ((LoreEnhancementAttack)enh).getExtraAttack(attrs);
			}
			
			if (enh instanceof LoreEnhancementBonusDamageI) {
				extraAtt += ((LoreEnhancementBonusDamageI)enh).getExtraAttack(attrs);
			}
			
			if (enh instanceof LoreEnhancementBonusDamageII) {
				extraAtt += ((LoreEnhancementBonusDamageII)enh).getExtraAttack(attrs);
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
		if (event.getDamager() instanceof Player) {
			Player p = (Player)event.getDamager();
			Resident res = CivGlobal.getResident(p);
			
			ConfigUnit unit = Unit.getPlayerUnit(p);
			if (unit != null && unit.id.equals("u_warrior")) {
				dmg *= 1.5;
				CivMessage.send(p, CivColor.LightGrayItalic+"+50% damage with Warrior Unit.");
			}
			
			if (!res.hasTechForItem(inHand)) {
				dmg = dmg / 2;
			}
		}
		
//		if (dmg < 0.5) { /* Always do at least 0.5 damage. */
//			dmg = 0.5;
//		}
		event.setDamage(dmg);
	}
}