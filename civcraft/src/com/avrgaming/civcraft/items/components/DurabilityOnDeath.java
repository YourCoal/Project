/*************************************************************************
 * 
 * AVRGAMING LLC
 * __________________
 * 
 *  [2013] AVRGAMING LLC
 *  All Rights Reserved.
 * 
 * NOTICE:  All information contained herein is, and remains
 * the property of AVRGAMING LLC and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to AVRGAMING LLC
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from AVRGAMING LLC.
 */
package com.avrgaming.civcraft.items.components;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import com.avrgaming.civcraft.loreenhancements.LoreEnhancement;
import com.avrgaming.civcraft.loreenhancements.LoreEnhancementDurability;
import com.avrgaming.civcraft.lorestorage.ItemChangeResult;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.util.CivColor;

import gpl.AttributeUtil;

public class DurabilityOnDeath extends ItemComponent {
	
	@Override
	public void onPrepareCreate(AttributeUtil attrs) {
	}
	
	@Override
	public ItemChangeResult onDurabilityDeath(PlayerDeathEvent event, ItemChangeResult result, ItemStack sourceStack) {
		if (result == null) {
			result = new ItemChangeResult();
			result.stack = sourceStack;
			result.destroyItem = false;
		}
		
		if (result.destroyItem) {
			return result;
		}
		
		double percent = this.getDouble("value");
		
		AttributeUtil attrs = new AttributeUtil(result.stack);
		/* Try to get any extra enhancements from this item. */
		for (LoreEnhancement enh : attrs.getEnhancements()) {
			if (enh instanceof LoreEnhancementDurability) {
				percent = percent/2;
				CivMessage.send((Player)event.getEntity(), CivColor.LightGrayItalic+"Due to the enhancement 'Durability' on your "+sourceStack.getItemMeta().getDisplayName()
															+CivColor.LightGrayItalic+", your armor saved 50% more durability.");
			}
		}
		
		int reduction = (int)(result.stack.getType().getMaxDurability()*percent);
		int durabilityLeft = result.stack.getType().getMaxDurability() - result.stack.getDurability();
		
		if (durabilityLeft > reduction) {
			result.stack.setDurability((short)(result.stack.getDurability() + reduction));
		} else {
			result.destroyItem = true;
		}		
		return result;
	}
}
