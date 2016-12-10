package com.avrgaming.civcraft.items.components;

import gpl.AttributeUtil;
import gpl.AttributeUtil.Attribute;
import gpl.AttributeUtil.AttributeType;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.util.CivColor;

public class ArmorToughness extends ItemComponent {
	
	@Override
	public void onPrepareCreate(AttributeUtil attrs) {
		attrs.add(Attribute.newBuilder().name("ArmorToughness").
				type(AttributeType.GENERIC_ARMOR_TOUGHNESS).
				amount(this.getDouble("value")).build());
		attrs.addLore(CivColor.Blue+""+this.getDouble("value")+" Armor Toughness");
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
		double dmg = event.getDamage();
		
		if (event.getEntity() instanceof Player) {
			Resident resident = CivGlobal.getResident(((Player)event.getEntity()));
			if (!resident.hasTechForItem(stack)) {
				defValue = defValue / 2;
			}
		}
		
		dmg -= defValue;
//		if (dmg < 0.5) {
//			/* Always do at least 0.5 damage. */
//			dmg = 0.5;
//		}
		event.setDamage(dmg);
	}
}
