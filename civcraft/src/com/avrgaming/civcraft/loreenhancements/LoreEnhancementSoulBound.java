package com.avrgaming.civcraft.loreenhancements;

import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import com.avrgaming.civcraft.util.CivColor;

import gpl.AttributeUtil;

public class LoreEnhancementSoulBound extends LoreEnhancement {
	
	public AttributeUtil add(AttributeUtil attrs) {
		attrs.addEnhancement("LoreEnhancementSoulBound", null, null);
		attrs.addLore(CivColor.Gold+getDisplayName());
		return attrs;
	}
	
	//TODO FIX THIS so it rolls correctly and when it finds 1 erorr, it doesn't kill all items
	public boolean onDeath(PlayerDeathEvent event, ItemStack stack) {
/*		Random r = new Random();
		int destroy = r.nextInt(1 + 100) + 1;
		if (destroy <= 25) {
			CivMessage.send(event.getEntity().getPlayer(), CivColor.Gold+"[Soulbound] "+CivColor.Rose+"Rolled "+destroy);
			event.getEntity().getInventory().remove(stack);
			return true;
		} else {
			CivMessage.send(event.getEntity().getPlayer(), CivColor.Gold+"[Soulbound] "+CivColor.LightGreen+"Rolled "+destroy);
			return true;
		}*/
		
		
		event.getDrops().remove(stack);
		return true;
	}
	
	public boolean canEnchantItem(ItemStack item) {
		return isWeaponArmorTool(item);
	}
	
	public boolean hasEnchantment(ItemStack item) {
		AttributeUtil attrs = new AttributeUtil(item);
		return attrs.hasEnhancement("LoreEnhancementSoulBound");
	}
	
	public String getDisplayName() {
		return "SoulBound";
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
