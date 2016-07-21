/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.util;

import java.util.UUID;

import org.bukkit.World;
import org.bukkit.entity.Entity;

public class EntityUtil {

	public static Entity getEntity(World world, UUID uuid) {

		for (Entity ent : world.getEntities()) {
			if (ent.getUniqueId().equals(uuid)) {
				return ent;
			}
		}
		
		return null;
	}

}
