package com.avrgaming.civcraft.threading.sync.request;

import java.util.concurrent.locks.ReentrantLock;

import org.bukkit.inventory.ItemStack;

import com.avrgaming.civcraft.util.MultiInventory;

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
