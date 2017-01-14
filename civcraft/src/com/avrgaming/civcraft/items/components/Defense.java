package com.avrgaming.civcraft.items.components;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

import com.avrgaming.civcraft.loreenhancements.LoreEnhancement;
import com.avrgaming.civcraft.loreenhancements.LoreEnhancementDefense;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.util.CivColor;

import gpl.AttributeUtil;
import gpl.AttributeUtil.Attribute;
import gpl.AttributeUtil.AttributeType;

public class Defense extends ItemComponent {
	
	@Override
	public void onPrepareCreate(AttributeUtil attrs) {
		attrs.add(Attribute.newBuilder().name("Defense").
				type(AttributeType.GENERIC_ARMOR).
				amount(this.getDouble("value")).build());
		attrs.addLore(CivColor.Blue+""+this.getDouble("value")+" Defense");
		return;
	}
	
	@Override
	public void onHold(PlayerItemHeldEvent event) {
		Resident resident = CivGlobal.getResident(event.getPlayer());
		if (!resident.hasTechForItem(event.getPlayer().getInventory().getItem(event.getNewSlot()))) {		
			CivMessage.send(resident, CivColor.Rose+"Warning - "+CivColor.LightGray+
					"You do not have the required technology to use this item. It's defense output will be reduced in half.");
		}
	}
	
	@Override
	public void onDefense(EntityDamageByEntityEvent event, ItemStack stack) {
		double defValue = this.getDouble("value");
		double extraDef = 0;
		AttributeUtil attrs = new AttributeUtil(stack);
		/* Try to get any extra defense enhancements from this item. */
		for (LoreEnhancement enh : attrs.getEnhancements()) {
			if (enh instanceof LoreEnhancementDefense) {
				extraDef += ((LoreEnhancementDefense)enh).getExtraDefense(attrs);
			}
		}
		
		defValue += extraDef;
		if (event.getEntity() instanceof Player) {
			Resident resident = CivGlobal.getResident(((Player)event.getEntity()));
			if (!resident.hasTechForItem(stack)) {
				defValue = defValue / 2;
			}
		}
		
		double dmg = event.getDamage();
		dmg -= defValue;
		if (dmg < 0.5) { /* Always do at least 0.5 damage. */
			dmg = 0.5;
		}
		event.setDamage(dmg);
	}
}