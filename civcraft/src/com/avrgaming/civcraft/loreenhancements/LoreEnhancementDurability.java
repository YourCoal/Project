package com.avrgaming.civcraft.loreenhancements;

import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import com.avrgaming.civcraft.util.CivColor;

import gpl.AttributeUtil;

public class LoreEnhancementDurability extends LoreEnhancement {
	
	public AttributeUtil add(AttributeUtil attrs) {
		attrs.addEnhancement("LoreEnhancementDurability", null, null);
		attrs.addLore(CivColor.LightGray+getDisplayName());
		return attrs;
	}
	
	public boolean onDeath(final PlayerDeathEvent event, final ItemStack stack) {
		event.getDrops().remove(stack);
		return true;
	};
	
	public boolean canEnchantItem(ItemStack item) {
		return isArmor(item);
	}
	
	public boolean hasEnchantment(ItemStack item) {
		AttributeUtil attrs = new AttributeUtil(item);
		return attrs.hasEnhancement("LoreEnhancementDurability");
	}
	
	public String getDisplayName() {
		return "Durability";
	}
	
	@Override
	public String serialize(ItemStack stack) {
		return "";
	}

	@Override
	public ItemStack deserialize(ItemStack stack, String data) {
		return stack;
	}
}
