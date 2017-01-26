package com.avrgaming.civcraft.loreenhancements;

import org.bukkit.inventory.ItemStack;

import com.avrgaming.civcraft.util.CivColor;

import gpl.AttributeUtil;

public class LoreEnhancementBonusDamageII extends LoreEnhancement {
	
	public AttributeUtil add(AttributeUtil attrs) {
		attrs.addEnhancement("LoreEnhancementBonusDamageII", null, null);
		attrs.addLore(CivColor.LightGray+getDisplayName());
		return attrs;
	}
	
	public boolean canEnchantItem(ItemStack item) {
		return isSword(item);
	}
	
	public boolean hasEnchantment(ItemStack item) {
		AttributeUtil attrs = new AttributeUtil(item);
		return attrs.hasEnhancement("LoreEnhancementBonusDamageII");
	}
	
	public String getDisplayName() {
		return "Bonus Damage II";
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
		return 3.0;
	}
}
