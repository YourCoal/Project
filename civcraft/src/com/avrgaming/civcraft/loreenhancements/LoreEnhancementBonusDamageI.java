package com.avrgaming.civcraft.loreenhancements;

import org.bukkit.inventory.ItemStack;

import com.avrgaming.civcraft.util.CivColor;

import gpl.AttributeUtil;

public class LoreEnhancementBonusDamageI extends LoreEnhancement {
	
	public AttributeUtil add(AttributeUtil attrs) {
		attrs.addEnhancement("LoreEnhancementBonusDamageI", null, null);
		attrs.addLore(CivColor.LightGray+getDisplayName());
		return attrs;
	}
	
	public boolean canEnchantItem(ItemStack item) {
		return isWeapon(item);
	}
	
	public boolean hasEnchantment(ItemStack item) {
		AttributeUtil attrs = new AttributeUtil(item);
		return attrs.hasEnhancement("LoreEnhancementBonusDamageI");
	}
	
	public String getDisplayName() {
		return "Bonus Damage I";
	}
	
	@Override
	public String serialize(ItemStack stack) {
		return "";
	}
	
	@Override
	public ItemStack deserialize(ItemStack stack, String data) {
		return stack;
	}
	
	public double getExtraAttack(AttributeUtil attrs) {
		return 1.5;
	}
}
