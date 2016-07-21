/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.threading.tasks;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class DelayItemDrop implements Runnable {

	Location loc;
	ItemStack stack;
	
	public DelayItemDrop(ItemStack stack, Location loc) {
		this.loc = loc;
		this.stack = stack;
	}
	
	@Override
	public void run() {
		loc.getWorld().dropItem(loc, stack);
	}

}
