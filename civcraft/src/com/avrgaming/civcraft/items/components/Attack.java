package com.avrgaming.civcraft.items.components;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

import com.avrgaming.civcraft.loreenhancements.LoreEnhancement;
import com.avrgaming.civcraft.loreenhancements.LoreEnhancementAttack;
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
		}
		
		dmg += extraAtt;
		if (event.getDamager() instanceof Player) {
			Resident resident = CivGlobal.getResident(((Player)event.getDamager()));
			if (!resident.hasTechForItem(inHand)) {
				dmg = dmg / 2;
			}
		}
		
		if (dmg < 0.5) { /* Always do at least 0.5 damage. */
			dmg = 0.5;
		}
		event.setDamage(dmg);
	}
}