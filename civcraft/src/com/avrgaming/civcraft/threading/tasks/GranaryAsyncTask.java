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
	
	//Base levels, used to control the outcome of food more easily.
	int rglr = 1;
	int easy = 2;
	int medm = 4;
	int hard = 7;
	
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
	
	public void processGranaryUpdate() {
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
						if (stack.getAmount() < 2) {
							return; //Not enough of item.
						}
						this.updateInventory(Action.REMOVE, source_inv, ItemManager.createItemStack(CivData.BREAD, 2));
					} catch (InterruptedException e) {
						return;
					}
					
					Random rand = new Random();
					int randMax = GranaryUpdate.BREAD_MAX;
					int rand1 = rand.nextInt(randMax);
					ItemStack newItem;
					if (rand1 < ((int)((granary.getBreadChance(Chance.HARD))*randMax))) {
						int hardtot = hard + (2 * granary.getTown().getLevel());
						this.granary.getTown().giveFood(hardtot);
						CivMessage.sendTown(granary.getTown(), CivColor.LightGreen+"The town gained "
								+CivColor.Green+CivColor.BOLD+"[Hard] "+hardtot+CivColor.LightGreen+" food from the granary.");
						newItem = ItemManager.createItemStack(CivData.AIR, 1);
					} else if (rand1 < ((int)((granary.getBreadChance(Chance.MEDIUM))*randMax))) {
						int medmtot = medm + (2 * granary.getTown().getLevel());
						this.granary.getTown().giveFood(medmtot);
						CivMessage.sendTown(granary.getTown(), CivColor.LightGreen+"The town gained "
								+CivColor.Green+CivColor.BOLD+"[Medium] "+medmtot+CivColor.LightGreen+" food from the granary.");
						newItem = ItemManager.createItemStack(CivData.AIR, 1);
					} else if (rand1 < ((int)((granary.getBreadChance(Chance.EASY))*randMax))) {
						int easytot = easy + (2 * granary.getTown().getLevel());
						this.granary.getTown().giveFood(easytot);
						CivMessage.sendTown(granary.getTown(), CivColor.LightGreen+"The town gained "
								+CivColor.Green+CivColor.BOLD+"[Easy] "+easytot+CivColor.LightGreen+" food from the granary.");
						newItem = ItemManager.createItemStack(CivData.AIR, 1);
					} else {
						int rglrtot = rglr + (2 * granary.getTown().getLevel());
						this.granary.getTown().giveFood(rglrtot);
						CivMessage.sendTown(granary.getTown(), CivColor.LightGreen+"The town gained "
								+CivColor.Green+CivColor.BOLD+"[Regular] "+rglrtot+CivColor.LightGreen+" food from the granary.");
						newItem = ItemManager.createItemStack(CivData.AIR, 1);
					} try {
						debug(granary, "Updating inventory:"+newItem);
						this.updateInventory(Action.ADD, dest_inv, newItem);
					} catch (InterruptedException e) {
						return;
					}
					break;
				}
				
				if (this.granary.getLevel() >= 1 && ItemManager.getId(stack) == CivData.CARROT_ITEM) {
					try {
						if (stack.getAmount() < 2) {
							return; //Not enough of item.
						}
						this.updateInventory(Action.REMOVE, source_inv, ItemManager.createItemStack(CivData.CARROT_ITEM, 2));
					} catch (InterruptedException e) {
						return;
					}
					
					Random rand = new Random();
					int randMax = GranaryUpdate.CARROT_MAX;
					int rand1 = rand.nextInt(randMax);
					ItemStack newItem;
					if (rand1 < ((int)((granary.getCarrotChance(Chance.HARD))*randMax))) {
						int hardtot = hard + (2 * granary.getTown().getLevel());
						this.granary.getTown().giveFood(hardtot);
						CivMessage.sendTown(granary.getTown(), CivColor.LightGreen+"The town gained "
								+CivColor.Green+CivColor.BOLD+"[Hard] "+hardtot+CivColor.LightGreen+" food from the granary.");
						newItem = ItemManager.createItemStack(CivData.AIR, 1);
					} else if (rand1 < ((int)((granary.getCarrotChance(Chance.MEDIUM))*randMax))) {
						int medmtot = medm + (2 * granary.getTown().getLevel());
						this.granary.getTown().giveFood(medmtot);
						CivMessage.sendTown(granary.getTown(), CivColor.LightGreen+"The town gained "
								+CivColor.Green+CivColor.BOLD+"[Medium] "+medmtot+CivColor.LightGreen+" food from the granary.");
						newItem = ItemManager.createItemStack(CivData.AIR, 1);
					} else if (rand1 < ((int)((granary.getCarrotChance(Chance.EASY))*randMax))) {
						int easytot = easy + (2 * granary.getTown().getLevel());
						this.granary.getTown().giveFood(easytot);
						CivMessage.sendTown(granary.getTown(), CivColor.LightGreen+"The town gained "
								+CivColor.Green+CivColor.BOLD+"[Easy] "+easytot+CivColor.LightGreen+" food from the granary.");
						newItem = ItemManager.createItemStack(CivData.AIR, 1);
					} else {
						int rglrtot = rglr + (2 * granary.getTown().getLevel());
						this.granary.getTown().giveFood(rglrtot);
						CivMessage.sendTown(granary.getTown(), CivColor.LightGreen+"The town gained "
								+CivColor.Green+CivColor.BOLD+"[Regular] "+rglrtot+CivColor.LightGreen+" food from the granary.");
						newItem = ItemManager.createItemStack(CivData.AIR, 1);
					} try {
						debug(granary, "Updating inventory:"+newItem);
						this.updateInventory(Action.ADD, dest_inv, newItem);
					} catch (InterruptedException e) {
						return;
					}
					break;
				}
				
				if (this.granary.getLevel() >= 1 && ItemManager.getId(stack) == CivData.POTATO_ITEM) {
					try {
						if (stack.getAmount() < 2) {
							return; //Not enough of item.
						}
						this.updateInventory(Action.REMOVE, source_inv, ItemManager.createItemStack(CivData.POTATO_ITEM, 2));
					} catch (InterruptedException e) {
						return;
					}
					
					Random rand = new Random();
					int randMax = GranaryUpdate.POTATO_MAX;
					int rand1 = rand.nextInt(randMax);
					ItemStack newItem;
					if (rand1 < ((int)((granary.getPotatoChance(Chance.HARD))*randMax))) {
						int hardtot = hard + (2 * granary.getTown().getLevel());
						this.granary.getTown().giveFood(hardtot);
						CivMessage.sendTown(granary.getTown(), CivColor.LightGreen+"The town gained "
								+CivColor.Green+CivColor.BOLD+"[Hard] "+hardtot+CivColor.LightGreen+" food from the granary.");
						newItem = ItemManager.createItemStack(CivData.AIR, 1);
					} else if (rand1 < ((int)((granary.getPotatoChance(Chance.MEDIUM))*randMax))) {
						int medmtot = medm + (2 * granary.getTown().getLevel());
						this.granary.getTown().giveFood(medmtot);
						CivMessage.sendTown(granary.getTown(), CivColor.LightGreen+"The town gained "
								+CivColor.Green+CivColor.BOLD+"[Medium] "+medmtot+CivColor.LightGreen+" food from the granary.");
						newItem = ItemManager.createItemStack(CivData.AIR, 1);
					} else if (rand1 < ((int)((granary.getPotatoChance(Chance.EASY))*randMax))) {
						int easytot = easy + (2 * granary.getTown().getLevel());
						this.granary.getTown().giveFood(easytot);
						CivMessage.sendTown(granary.getTown(), CivColor.LightGreen+"The town gained "
								+CivColor.Green+CivColor.BOLD+"[Easy] "+easytot+CivColor.LightGreen+" food from the granary.");
						newItem = ItemManager.createItemStack(CivData.AIR, 1);
					} else {
						int rglrtot = rglr + (2 * granary.getTown().getLevel());
						this.granary.getTown().giveFood(rglrtot);
						CivMessage.sendTown(granary.getTown(), CivColor.LightGreen+"The town gained "
								+CivColor.Green+CivColor.BOLD+"[Regular] "+rglrtot+CivColor.LightGreen+" food from the granary.");
						newItem = ItemManager.createItemStack(CivData.AIR, 1);
					} try {
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
							processGranaryUpdate();
							debug(this.granary, "Not penalized");
						} else {
							debug(this.granary, "Skip Due to Penalty");
						}
					} else {
						processGranaryUpdate();
						if (this.granary.getTown().getGovernment().id.equals("gov_despotism")) {
							debug(this.granary, "Doing Bonus");
							processGranaryUpdate();
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