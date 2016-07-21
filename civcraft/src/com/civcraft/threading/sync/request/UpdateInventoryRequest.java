/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.threading.sync.request;

import java.util.concurrent.locks.ReentrantLock;

import org.bukkit.inventory.ItemStack;

import com.civcraft.util.MultiInventory;

public class UpdateInventoryRequest extends AsyncRequest {

	public UpdateInventoryRequest(ReentrantLock lock) {
		super(lock);
	}

	public enum Action {
		ADD,
		REMOVE
	}
	
	public MultiInventory inv;
	public ItemStack stack;
	public Action action;
	
	
}
