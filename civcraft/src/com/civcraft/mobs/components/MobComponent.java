/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.mobs.components;

import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.civcraft.mobs.CommonCustomMob;

public class MobComponent {

	public void onDefense(EntityDamageByEntityEvent event) {}

	public static void onDefense(Entity entity, EntityDamageByEntityEvent event) {
		CommonCustomMob custom = CommonCustomMob.customMobs.get(entity.getUniqueId());
		if (custom != null) {
			for (MobComponent comp : custom.getMobComponents()) {
				comp.onDefense(event);
			}
		}
	}
}