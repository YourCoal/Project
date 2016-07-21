/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.mobs;

import com.civcraft.mobs.MobSpawner.CustomMobLevel;
import com.civcraft.mobs.MobSpawner.CustomMobType;

public class TypeLevel {
	public CustomMobType type;
	public CustomMobLevel level;

	public TypeLevel(CustomMobType type, CustomMobLevel level) {
		this.type = type;
		this.level = level;
	}
}
