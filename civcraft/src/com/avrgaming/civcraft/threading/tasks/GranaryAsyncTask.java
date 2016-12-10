package com.avrgaming.civcraft.threading.tasks;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.avrgaming.civcraft.config.CivSettings;
import com.avrgaming.civcraft.exception.CivTaskAbortException;
import com.avrgaming.civcraft.main.CivData;
import com.avrgaming.civcraft.main.CivLog;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.StructureChest;
import com.avrgaming.civcraft.structure.GranaryUpdate;
import com.avrgaming.civcraft.structure.GranaryUpdate.Chance;
import com.avrgaming.civcraft.structure.Structure;
import com.avrgaming.civcraft.threading.CivAsyncTask;
import com.avrgaming.civcraft.threading.sync.request.UpdateInventoryRequest.Action;
import com.avrgaming.civcraft.util.CivColor;
import com.avrgaming.civcraft.util.ItemManager;
import com.avrgaming.civcraft.util.MultiInventory;

public class GranaryAsyncTask extends CivAsyncTask {
	
	GranaryUpdate granary;
	public static HashSet<String> debugTowns = new HashSet<String>();

	public static void debug(GranaryUpdate granary, String msg) {
		if (debugTowns.contains(granary.getTown().getName())) {
			CivLog.warning("TrommelDebug:"+granary.getTown().getName()+":"+msg);
		}
	}	
	
	public GranaryAsyncTask(Structure granary) {
		this.granary = (GranaryUpdate)granary;
	}
	
	public void processTrommelUpdate() {
		if (!granary.isActive()) {
			debug(granary, "granary inactive...");
			return;
		}
		
		debug(granary, "Processing granary...");
		ArrayList<StructureChest> sources = granary.getAllChestsById(1);
		ArrayList<StructureChest> destinations = granary.getAllChestsById(2);
		if (sources.size() != 2 || destinations.size() != 2) {
			CivLog.error("Bad chests for granary in town:"+granary.getTown().getName()+" sources:"+sources.size()+" dests:"+destinations.size());
			return;
		}
		
		// Make sure the chunk is loaded before continuing. Also, add get chest and add it to inventory.
		MultiInventory source_inv = new MultiInventory();
		MultiInventory dest_inv = new MultiInventory();
		
		try {
			for (StructureChest src : sources) {			
				Inventory tmp;
				try {
					tmp = this.getChestInventory(src.getCoord().getWorldname(), src.getCoord().getX(), src.getCoord().getY(), src.getCoord().getZ(), false);
				} catch (CivTaskAbortException e) {
					CivLog.warning("Trommel:"+e.getMessage());
					return;
				}
				if (tmp == null) {
					granary.skippedCounter++;
					return;
				}
				source_inv.addInventory(tmp);
			}
			
			boolean full = true;
			for (StructureChest dst : destinations) {
				Inventory tmp;
				try {
					tmp = this.getChestInventory(dst.getCoord().getWorldname(), dst.getCoord().getX(), dst.getCoord().getY(), dst.getCoord().getZ(), false);
				} catch (CivTaskAbortException e) {
					CivLog.warning("Trommel:"+e.getMessage());
					return;
				}
				if (tmp == null) {
					granary.skippedCounter++;
					return;
				}
				
				dest_inv.addInventory(tmp);
				for (ItemStack stack : tmp.getContents()) {
					if (stack == null) {
						full = false;
						break;
					}
				}
			}
			
			if (full) {
				return;
			}
		} catch (InterruptedException e) {
			return;
		}
		
		debug(granary, "Processing granary:"+granary.skippedCounter+1);
		ItemStack[] contents = source_inv.getContents();
		for (int i = 0; i < granary.skippedCounter+1; i++) {
			for(ItemStack stack : contents) {
				if (stack == null) {
					continue;
				}
				
				if (this.granary.getLevel() >= 1 && ItemManager.getId(stack) == CivData.BREAD) {
					try {
						this.updateInventory(Action.REMOVE, source_inv, ItemManager.createItemStack(CivData.BREAD, 1));
					} catch (InterruptedException e) {
						return;
					}
					
					Random rand = new Random();
					int randMax = GranaryUpdate.BREAD_MAX;
					int rand1 = rand.nextInt(randMax);
					ItemStack newItem;
					
					if (rand1 < ((int)((granary.getBreadChance(Chance.EXTREME))*randMax))) {
						this.granary.getTown().giveFood(15);
						CivMessage.sendTown(granary.getTown(), CivColor.LightGreen+"The town gained "
								+CivColor.Green+CivColor.BOLD+"[Extreme] 15"+CivColor.LightGreen+" food from the granary.");
						newItem = ItemManager.createItemStack(CivData.AIR, 1);
					} else if (rand1 < ((int)((granary.getBreadChance(Chance.ULTRA))*randMax))) {
						this.granary.getTown().giveFood(11);
						CivMessage.sendTown(granary.getTown(), CivColor.LightGreen+"The town gained "
								+CivColor.Green+CivColor.BOLD+"[Ultra] 11"+CivColor.LightGreen+" food from the granary.");
						newItem = ItemManager.createItemStack(CivData.AIR, 1);
					} else if (rand1 < ((int)((granary.getBreadChance(Chance.MEGA))*randMax))) {
						this.granary.getTown().giveFood(8);
						CivMessage.sendTown(granary.getTown(), CivColor.LightGreen+"The town gained "
								+CivColor.Green+CivColor.BOLD+"[Mega] 8"+CivColor.LightGreen+" food from the granary.");
						newItem = ItemManager.createItemStack(CivData.AIR, 1);
					} else if (rand1 < ((int)((granary.getBreadChance(Chance.HARD))*randMax))) {
						this.granary.getTown().giveFood(6);
						CivMessage.sendTown(granary.getTown(), CivColor.LightGreen+"The town gained "
								+CivColor.Green+CivColor.BOLD+"[Hard] 6"+CivColor.LightGreen+" food from the granary.");
						newItem = ItemManager.createItemStack(CivData.AIR, 1);
					} else if (rand1 < ((int)((granary.getBreadChance(Chance.MEDIUM))*randMax))) {
						this.granary.getTown().giveFood(4);
						CivMessage.sendTown(granary.getTown(), CivColor.LightGreen+"The town gained "
								+CivColor.Green+CivColor.BOLD+"[Medium] 4"+CivColor.LightGreen+" food from the granary.");
						newItem = ItemManager.createItemStack(CivData.AIR, 1);
					} else if (rand1 < ((int)((granary.getBreadChance(Chance.EASY))*randMax))) {
						this.granary.getTown().giveFood(3);
						CivMessage.sendTown(granary.getTown(), CivColor.LightGreen+"The town gained "
								+CivColor.Green+CivColor.BOLD+"[Easy] 3"+CivColor.LightGreen+" food from the granary.");
						newItem = ItemManager.createItemStack(CivData.AIR, 1);
					} else {
						this.granary.getTown().giveFood(2);
						CivMessage.sendTown(granary.getTown(), CivColor.LightGreen+"The town gained "
								+CivColor.Green+CivColor.BOLD+"[Regular] 2"+CivColor.LightGreen+" food from the granary.");
						newItem = ItemManager.createItemStack(CivData.AIR, 1);
					}
					
					try {
						debug(granary, "Updating inventory:"+newItem);
						this.updateInventory(Action.ADD, dest_inv, newItem);
					} catch (InterruptedException e) {
						return;
					}
					break;
				}
				
				if (this.granary.getLevel() >= 2 && ItemManager.getId(stack) == CivData.CARROT_ITEM) {
					try {
						this.updateInventory(Action.REMOVE, source_inv, ItemManager.createItemStack(CivData.CARROT_ITEM, 1));
					} catch (InterruptedException e) {
						return;
					}
					
					Random rand = new Random();
					int randMax = GranaryUpdate.CARROT_MAX;
					int rand1 = rand.nextInt(randMax);
					ItemStack newItem;
					
					if (rand1 < ((int)((granary.getBreadChance(Chance.EXTREME))*randMax))) {
						this.granary.getTown().giveFood(15);
						CivMessage.sendTown(granary.getTown(), CivColor.LightGreen+"The town gained "
								+CivColor.Green+CivColor.BOLD+"[Extreme] 15"+CivColor.LightGreen+" food from the granary.");
						newItem = ItemManager.createItemStack(CivData.AIR, 1);
					} else if (rand1 < ((int)((granary.getBreadChance(Chance.ULTRA))*randMax))) {
						this.granary.getTown().giveFood(11);
						CivMessage.sendTown(granary.getTown(), CivColor.LightGreen+"The town gained "
								+CivColor.Green+CivColor.BOLD+"[Ultra] 11"+CivColor.LightGreen+" food from the granary.");
						newItem = ItemManager.createItemStack(CivData.AIR, 1);
					} else if (rand1 < ((int)((granary.getBreadChance(Chance.MEGA))*randMax))) {
						this.granary.getTown().giveFood(8);
						CivMessage.sendTown(granary.getTown(), CivColor.LightGreen+"The town gained "
								+CivColor.Green+CivColor.BOLD+"[Meta] 8"+CivColor.LightGreen+" food from the granary.");
						newItem = ItemManager.createItemStack(CivData.AIR, 1);
					} else if (rand1 < ((int)((granary.getBreadChance(Chance.HARD))*randMax))) {
						this.granary.getTown().giveFood(6);
						CivMessage.sendTown(granary.getTown(), CivColor.LightGreen+"The town gained "
								+CivColor.Green+CivColor.BOLD+"[Hard] 6"+CivColor.LightGreen+" food from the granary.");
						newItem = ItemManager.createItemStack(CivData.AIR, 1);
					} else if (rand1 < ((int)((granary.getBreadChance(Chance.MEDIUM))*randMax))) {
						this.granary.getTown().giveFood(4);
						CivMessage.sendTown(granary.getTown(), CivColor.LightGreen+"The town gained "
								+CivColor.Green+CivColor.BOLD+"[Medium] 4"+CivColor.LightGreen+" food from the granary.");
						newItem = ItemManager.createItemStack(CivData.AIR, 1);
					} else if (rand1 < ((int)((granary.getBreadChance(Chance.EASY))*randMax))) {
						this.granary.getTown().giveFood(3);
						CivMessage.sendTown(granary.getTown(), CivColor.LightGreen+"The town gained "
								+CivColor.Green+CivColor.BOLD+"[Easy] 3"+CivColor.LightGreen+" food from the granary.");
						newItem = ItemManager.createItemStack(CivData.AIR, 1);
					} else {
						this.granary.getTown().giveFood(2);
						CivMessage.sendTown(granary.getTown(), CivColor.LightGreen+"The town gained "
								+CivColor.Green+CivColor.BOLD+"[Regular] 2"+CivColor.LightGreen+" food from the granary.");
						newItem = ItemManager.createItemStack(CivData.AIR, 1);
					}
					
					try {
						debug(granary, "Updating inventory:"+newItem);
						this.updateInventory(Action.ADD, dest_inv, newItem);
					} catch (InterruptedException e) {
						return;
					}
					break;
				}
			}	
		}
		granary.skippedCounter = 0;
	}
	
	
	@Override
	public void run() {
		if (this.granary.lock.tryLock()) {
			try {
				try {
					if (this.granary.getTown().getGovernment().id.equals("gov_theocracy")) {
						Random rand = new Random();
						int randMax = 100;
						int rand1 = rand.nextInt(randMax);
						Double chance = CivSettings.getDouble(CivSettings.structureConfig, "granary.penalty_rate") * 100;
						if (rand1 < chance) {
							processTrommelUpdate();
							debug(this.granary, "Not penalized");
						} else {

							debug(this.granary, "Skip Due to Penalty");
						}
					} else {
						processTrommelUpdate();
						if (this.granary.getTown().getGovernment().id.equals("gov_despotism")) {
							debug(this.granary, "Doing Bonus");
							processTrommelUpdate();
						}
					}					
				} catch (Exception e) {
					e.printStackTrace();
				}
			} finally {
				this.granary.lock.unlock();
			}
		} else {
			debug(this.granary, "Failed to get lock while trying to start task, aborting.");
		}
	}
}