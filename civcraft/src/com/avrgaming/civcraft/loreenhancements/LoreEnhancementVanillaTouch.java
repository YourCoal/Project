package com.avrgaming.civcraft.loreenhancements;

public class LoreEnhancementVanillaTouch {
	
}
/*
import gpl.AttributeUtil;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import com.avrgaming.civcraft.util.CivColor;

public class LoreEnhancementVanillaTouch extends LoreEnhancement {
	
	public AttributeUtil add(AttributeUtil attrs) {
		attrs.addEnhancement("LoreEnhancementVanillaTouch", null, null);
		attrs.addLore(CivColor.Gold+getDisplayName());
		return attrs;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreakBlocks(BlockBreakEvent event) {
		if (event.getBlock().getType().equals(Material.LAPIS_ORE)) {
			if (!event.getPlayer().getInventory().getItemInMainHand().containsEnchantment(Enchantment.SILK_TOUCH)) {
				event.getBlock().breakNaturally();
				return;
			} else if (event.getPlayer().getInventory().getItemInMainHand().containsEnchantment(Enchantment.SILK_TOUCH)) {
				return;
			}
		}
		
//		if (event.getBlock().getType().equals(Material.REDSTONE_ORE)) {
//			if (!event.getPlayer().getInventory().getItemInMainHand().containsEnchantment(Enchantment.SILK_TOUCH)) {
//				event.getBlock().breakNaturally();
//			}
//		}
	}
	
	public boolean canEnchantItem(ItemStack item) {
		return isWeaponArmorTool(item);
	}
	
	public boolean hasEnchantment(ItemStack item) {
		AttributeUtil attrs = new AttributeUtil(item);
		return attrs.hasEnhancement("LoreEnhancementVanillaTouch");
	}
	
	public String getDisplayName() {
		return "VanillaTouch";
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
*/