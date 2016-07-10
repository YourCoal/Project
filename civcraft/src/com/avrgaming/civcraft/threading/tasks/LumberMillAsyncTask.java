/**
 * CivCraft Created by - AVRGAMING LLC
 * This Code Modified by - https://www.youtube.com/user/cpcole556
 **/
package com.avrgaming.civcraft.threading.tasks;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.avrgaming.civcraft.exception.CivTaskAbortException;
import com.avrgaming.civcraft.main.CivData;
import com.avrgaming.civcraft.main.CivLog;
import com.avrgaming.civcraft.object.StructureChest;
import com.avrgaming.civcraft.structure.LumberMill;
import com.avrgaming.civcraft.structure.Structure;
import com.avrgaming.civcraft.structure.LumberMill.Wood;
import com.avrgaming.civcraft.threading.CivAsyncTask;
import com.avrgaming.civcraft.threading.sync.request.UpdateInventoryRequest.Action;
import com.avrgaming.civcraft.util.ItemManager;
import com.avrgaming.civcraft.util.MultiInventory;

public class LumberMillAsyncTask extends CivAsyncTask {
	
	LumberMill lumber;
	
	public static HashSet<String> debugTowns = new HashSet<String>();
	
	public static void debug(LumberMill lumbermill, String msg) {
		if (debugTowns.contains(lumbermill.getTown().getName())) {
			CivLog.warning("LumberMill Debug:"+lumbermill.getTown().getName()+":"+msg);
		}
	}
	
	public LumberMillAsyncTask(Structure lumbermill) {
		this.lumber = (LumberMill)lumbermill;
	}
	
	public void processLumberUpdate() {
		if (!lumber.isActive()) {
			debug(lumber, "LumberMill inactive...");
			return;
		}
		
		debug(lumber, "Processing LumberMill...");
		ArrayList<StructureChest> sources = lumber.getAllChestsById(0);
		ArrayList<StructureChest> destinations = lumber.getAllChestsById(1);
		if (sources.size() != 2 || destinations.size() != 2) {
			CivLog.error("Bad chests for quarry in town:"+lumber.getTown().getName()+" sources:"+sources.size()+" dests:"+destinations.size());
			return;
		}
		
		MultiInventory source_inv = new MultiInventory();
		MultiInventory dest_inv = new MultiInventory();
		try {
			for (StructureChest src : sources) {	
				Inventory tmp;
				try {
					tmp = this.getChestInventory(src.getCoord().getWorldname(), src.getCoord().getX(), src.getCoord().getY(), src.getCoord().getZ(), false);
				} catch (CivTaskAbortException e) {
					CivLog.warning("LumberMill:"+e.getMessage());
					return;
				}
				if (tmp == null) {
					lumber.skippedCounter++;
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
					CivLog.warning("Quarry:"+e.getMessage());
					return;
				}
				if (tmp == null) {
					lumber.skippedCounter++;
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
		
		debug(lumber, "Processing LumberMill:"+lumber.skippedCounter+1);
		ItemStack[] contents = source_inv.getContents();
		for (int i = 0; i < lumber.skippedCounter+1; i++) {
			for(ItemStack stack : contents) {
				if (stack == null) {
					continue;
				}
				
				if (ItemManager.getId(stack) == CivData.WOOD_AXE) {
					try {
						short damage = ItemManager.getData(stack);
						this.updateInventory(Action.REMOVE, source_inv, ItemManager.createItemStack(CivData.WOOD_AXE, 1, damage));
						damage++;
						if (damage < 59) {
						this.updateInventory(Action.ADD, source_inv, ItemManager.createItemStack(CivData.WOOD_AXE, 1, damage));
						}
					} catch (InterruptedException e) {
						return;
					}
					Random rand = new Random();
					int randMax = LumberMill.MAX_CHANCE;
					int rand1 = rand.nextInt(randMax);
					ArrayList<ItemStack> newItems = new ArrayList<ItemStack>();
					if (rand1 < ((int)((lumber.getChance(Wood.APPLE))*randMax))) {
						newItems.add(ItemManager.createItemStack(CivData.APPLE, 1));
					} else if (rand1 < ((int)((lumber.getChance(Wood.SAPLING))*randMax))) {
						newItems.add(ItemManager.createItemStack(CivData.SAPLING, 1, (short) CivData.DATA_0));
						newItems.add(ItemManager.createItemStack(CivData.SAPLING, 1, (short) CivData.DATA_1));
						newItems.add(ItemManager.createItemStack(CivData.SAPLING, 1, (short) CivData.DATA_2));
						newItems.add(ItemManager.createItemStack(CivData.SAPLING, 1, (short) CivData.DATA_3));
					} else if (rand1 < ((int)((lumber.getChance(Wood.PLANK))*randMax))) {
						newItems.add(ItemManager.createItemStack(CivData.PLANKS, 1, (short) CivData.DATA_0));
						newItems.add(ItemManager.createItemStack(CivData.PLANKS, 1, (short) CivData.DATA_1));
						newItems.add(ItemManager.createItemStack(CivData.PLANKS, 1, (short) CivData.DATA_2));
						newItems.add(ItemManager.createItemStack(CivData.PLANKS, 1, (short) CivData.DATA_3));
					} else if (rand1 < ((int)((lumber.getChance(Wood.LOG))*randMax))) {
						newItems.add(ItemManager.createItemStack(CivData.WOOD, 1, (short) CivData.DATA_0));
						newItems.add(ItemManager.createItemStack(CivData.WOOD, 1, (short) CivData.DATA_1));
						newItems.add(ItemManager.createItemStack(CivData.WOOD, 1, (short) CivData.DATA_2));
						newItems.add(ItemManager.createItemStack(CivData.WOOD, 1, (short) CivData.DATA_3));
					} else {
						newItems.add(ItemManager.createItemStack(CivData.AIR, 1));
					}
					
					if (newItems.size() >= 1) {
						//Try to add the new item to the dest chest, if we cant, oh well.
						try {
							for (ItemStack item : newItems) {
								debug(lumber, "Updating inventory:"+item);
								this.updateInventory(Action.ADD, dest_inv, item);
							}
						} catch (InterruptedException e) {
							return;
						}
					} else {
						debug(lumber, "Didn't add any items. Did an error occur?");
					}
					break;
				}
				
				
				if (this.lumber.getLevel() >= 2 && ItemManager.getId(stack) == CivData.STONE_AXE) {
					try {
						short damage = ItemManager.getData(stack);
						this.updateInventory(Action.REMOVE, source_inv, ItemManager.createItemStack(CivData.STONE_AXE, 1, damage));
						damage++;
						if (damage < 131) {
						this.updateInventory(Action.ADD, source_inv, ItemManager.createItemStack(CivData.STONE_AXE, 1, damage));
						}
					} catch (InterruptedException e) {
						return;
					}
					Random rand = new Random();
					int randMax = LumberMill.MAX_CHANCE;
					int rand1 = rand.nextInt(randMax);
					ArrayList<ItemStack> newItems = new ArrayList<ItemStack>();
					if (rand1 < ((int)((lumber.getChance(Wood.APPLE))*randMax))) {
						newItems.add(ItemManager.createItemStack(CivData.APPLE, 1));
					} else if (rand1 < ((int)((lumber.getChance(Wood.SAPLING))*randMax))) {
						newItems.add(ItemManager.createItemStack(CivData.SAPLING, 1, (short) CivData.DATA_0));
						newItems.add(ItemManager.createItemStack(CivData.SAPLING, 1, (short) CivData.DATA_1));
						newItems.add(ItemManager.createItemStack(CivData.SAPLING, 1, (short) CivData.DATA_2));
						newItems.add(ItemManager.createItemStack(CivData.SAPLING, 1, (short) CivData.DATA_3));
					} else if (rand1 < ((int)((lumber.getChance(Wood.PLANK))*randMax))) {
						newItems.add(ItemManager.createItemStack(CivData.PLANKS, 1, (short) CivData.DATA_0));
						newItems.add(ItemManager.createItemStack(CivData.PLANKS, 1, (short) CivData.DATA_1));
						newItems.add(ItemManager.createItemStack(CivData.PLANKS, 1, (short) CivData.DATA_2));
						newItems.add(ItemManager.createItemStack(CivData.PLANKS, 1, (short) CivData.DATA_3));
					} else if (rand1 < ((int)((lumber.getChance(Wood.LOG))*randMax))) {
						newItems.add(ItemManager.createItemStack(CivData.WOOD, 1, (short) CivData.DATA_0));
						newItems.add(ItemManager.createItemStack(CivData.WOOD, 1, (short) CivData.DATA_1));
						newItems.add(ItemManager.createItemStack(CivData.WOOD, 1, (short) CivData.DATA_2));
						newItems.add(ItemManager.createItemStack(CivData.WOOD, 1, (short) CivData.DATA_3));
					} else {
						newItems.add(ItemManager.createItemStack(CivData.AIR, 1));
					}
					
					if (newItems.size() >= 1) {
						//Try to add the new item to the dest chest, if we cant, oh well.
						try {
							for (ItemStack item : newItems) {
								debug(lumber, "Updating inventory:"+item);
								this.updateInventory(Action.ADD, dest_inv, item);
							}
						} catch (InterruptedException e) {
							return;
						}
					} else {
						debug(lumber, "Didn't add any items. Did an error occur?");
					}
					break;
				}
				
				
				if (this.lumber.getLevel() >= 3 && ItemManager.getId(stack) == CivData.IRON_AXE) {
					try {
						short damage = ItemManager.getData(stack);
						this.updateInventory(Action.REMOVE, source_inv, ItemManager.createItemStack(CivData.IRON_AXE, 1, damage));
						damage++;
						if (damage < 250) {
						this.updateInventory(Action.ADD, source_inv, ItemManager.createItemStack(CivData.IRON_AXE, 1, damage));
						}
					} catch (InterruptedException e) {
						return;
					}
					Random rand = new Random();
					int randMax = LumberMill.MAX_CHANCE;
					int rand1 = rand.nextInt(randMax);
					ArrayList<ItemStack> newItems = new ArrayList<ItemStack>();
					if (rand1 < ((int)((lumber.getChance(Wood.APPLE))*randMax))) {
						newItems.add(ItemManager.createItemStack(CivData.APPLE, 1));
					} else if (rand1 < ((int)((lumber.getChance(Wood.SAPLING))*randMax))) {
						newItems.add(ItemManager.createItemStack(CivData.SAPLING, 1, (short) CivData.DATA_0));
						newItems.add(ItemManager.createItemStack(CivData.SAPLING, 1, (short) CivData.DATA_1));
						newItems.add(ItemManager.createItemStack(CivData.SAPLING, 1, (short) CivData.DATA_2));
						newItems.add(ItemManager.createItemStack(CivData.SAPLING, 1, (short) CivData.DATA_3));
					} else if (rand1 < ((int)((lumber.getChance(Wood.PLANK))*randMax))) {
						newItems.add(ItemManager.createItemStack(CivData.PLANKS, 1, (short) CivData.DATA_0));
						newItems.add(ItemManager.createItemStack(CivData.PLANKS, 1, (short) CivData.DATA_1));
						newItems.add(ItemManager.createItemStack(CivData.PLANKS, 1, (short) CivData.DATA_2));
						newItems.add(ItemManager.createItemStack(CivData.PLANKS, 1, (short) CivData.DATA_3));
					} else if (rand1 < ((int)((lumber.getChance(Wood.LOG))*randMax))) {
						newItems.add(ItemManager.createItemStack(CivData.WOOD, 1, (short) CivData.DATA_0));
						newItems.add(ItemManager.createItemStack(CivData.WOOD, 1, (short) CivData.DATA_1));
						newItems.add(ItemManager.createItemStack(CivData.WOOD, 1, (short) CivData.DATA_2));
						newItems.add(ItemManager.createItemStack(CivData.WOOD, 1, (short) CivData.DATA_3));
					} else {
						newItems.add(ItemManager.createItemStack(CivData.AIR, 1));
					}
					
					if (newItems.size() >= 1) {
						//Try to add the new item to the dest chest, if we cant, oh well.
						try {
							for (ItemStack item : newItems) {
								debug(lumber, "Updating inventory:"+item);
								this.updateInventory(Action.ADD, dest_inv, item);
							}
						} catch (InterruptedException e) {
							return;
						}
					} else {
						debug(lumber, "Didn't add any items. Did an error occur?");
					}
					break;
				}
				
				
				if (ItemManager.getId(stack) == CivData.GOLD_AXE) {
					try {
						short damage = ItemManager.getData(stack);
						this.updateInventory(Action.REMOVE, source_inv, ItemManager.createItemStack(CivData.GOLD_AXE, 1, damage));
						damage++;
						if (damage < 32) {
						this.updateInventory(Action.ADD, source_inv, ItemManager.createItemStack(CivData.GOLD_AXE, 1, damage));
						}
					} catch (InterruptedException e) {
						return;
					}
					Random rand = new Random();
					int randMax = LumberMill.MAX_CHANCE;
					int rand1 = rand.nextInt(randMax);
					ArrayList<ItemStack> newItems = new ArrayList<ItemStack>();
					if (rand1 < ((int)((lumber.getChance(Wood.APPLE))*randMax))) {
						newItems.add(ItemManager.createItemStack(CivData.APPLE, 1));
					} else if (rand1 < ((int)((lumber.getChance(Wood.SAPLING))*randMax))) {
						newItems.add(ItemManager.createItemStack(CivData.SAPLING, 1, (short) CivData.DATA_0));
						newItems.add(ItemManager.createItemStack(CivData.SAPLING, 1, (short) CivData.DATA_1));
						newItems.add(ItemManager.createItemStack(CivData.SAPLING, 1, (short) CivData.DATA_2));
						newItems.add(ItemManager.createItemStack(CivData.SAPLING, 1, (short) CivData.DATA_3));
					} else if (rand1 < ((int)((lumber.getChance(Wood.PLANK))*randMax))) {
						newItems.add(ItemManager.createItemStack(CivData.PLANKS, 1, (short) CivData.DATA_0));
						newItems.add(ItemManager.createItemStack(CivData.PLANKS, 1, (short) CivData.DATA_1));
						newItems.add(ItemManager.createItemStack(CivData.PLANKS, 1, (short) CivData.DATA_2));
						newItems.add(ItemManager.createItemStack(CivData.PLANKS, 1, (short) CivData.DATA_3));
					} else if (rand1 < ((int)((lumber.getChance(Wood.LOG))*randMax))) {
						newItems.add(ItemManager.createItemStack(CivData.WOOD, 1, (short) CivData.DATA_0));
						newItems.add(ItemManager.createItemStack(CivData.WOOD, 1, (short) CivData.DATA_1));
						newItems.add(ItemManager.createItemStack(CivData.WOOD, 1, (short) CivData.DATA_2));
						newItems.add(ItemManager.createItemStack(CivData.WOOD, 1, (short) CivData.DATA_3));
					} else {
						newItems.add(ItemManager.createItemStack(CivData.AIR, 1));
					}
					
					if (newItems.size() >= 1) {
						//Try to add the new item to the dest chest, if we cant, oh well.
						try {
							for (ItemStack item : newItems) {
								debug(lumber, "Updating inventory:"+item);
								this.updateInventory(Action.ADD, dest_inv, item);
							}
						} catch (InterruptedException e) {
							return;
						}
					} else {
						debug(lumber, "Didn't add any items. Did an error occur?");
					}
					break;
				}
				
				
				if (this.lumber.getLevel() >= 4 && ItemManager.getId(stack) == CivData.DIAMOND_AXE) {
					try {
						short damage = ItemManager.getData(stack);
						this.updateInventory(Action.REMOVE, source_inv, ItemManager.createItemStack(CivData.DIAMOND_AXE, 1, damage));
						damage++;
						if (damage < 1561) {
						this.updateInventory(Action.ADD, source_inv, ItemManager.createItemStack(CivData.DIAMOND_AXE, 1, damage));
						}
					} catch (InterruptedException e) {
						return;
					}
					Random rand = new Random();
					int randMax = LumberMill.MAX_CHANCE;
					int rand1 = rand.nextInt(randMax);
					ArrayList<ItemStack> newItems = new ArrayList<ItemStack>();
					if (rand1 < ((int)((lumber.getChance(Wood.APPLE))*randMax))) {
						newItems.add(ItemManager.createItemStack(CivData.APPLE, 1));
					} else if (rand1 < ((int)((lumber.getChance(Wood.SAPLING))*randMax))) {
						newItems.add(ItemManager.createItemStack(CivData.SAPLING, 1, (short) CivData.DATA_0));
						newItems.add(ItemManager.createItemStack(CivData.SAPLING, 1, (short) CivData.DATA_1));
						newItems.add(ItemManager.createItemStack(CivData.SAPLING, 1, (short) CivData.DATA_2));
						newItems.add(ItemManager.createItemStack(CivData.SAPLING, 1, (short) CivData.DATA_3));
					} else if (rand1 < ((int)((lumber.getChance(Wood.PLANK))*randMax))) {
						newItems.add(ItemManager.createItemStack(CivData.PLANKS, 1, (short) CivData.DATA_0));
						newItems.add(ItemManager.createItemStack(CivData.PLANKS, 1, (short) CivData.DATA_1));
						newItems.add(ItemManager.createItemStack(CivData.PLANKS, 1, (short) CivData.DATA_2));
						newItems.add(ItemManager.createItemStack(CivData.PLANKS, 1, (short) CivData.DATA_3));
					} else if (rand1 < ((int)((lumber.getChance(Wood.LOG))*randMax))) {
						newItems.add(ItemManager.createItemStack(CivData.WOOD, 1, (short) CivData.DATA_0));
						newItems.add(ItemManager.createItemStack(CivData.WOOD, 1, (short) CivData.DATA_1));
						newItems.add(ItemManager.createItemStack(CivData.WOOD, 1, (short) CivData.DATA_2));
						newItems.add(ItemManager.createItemStack(CivData.WOOD, 1, (short) CivData.DATA_3));
					} else {
						newItems.add(ItemManager.createItemStack(CivData.AIR, 1));
					}
					
					if (newItems.size() >= 1) {
						//Try to add the new item to the dest chest, if we cant, oh well.
						try {
							for (ItemStack item : newItems) {
								debug(lumber, "Updating inventory:"+item);
								this.updateInventory(Action.ADD, dest_inv, item);
							}
						} catch (InterruptedException e) {
							return;
						}
					} else {
						debug(lumber, "Didn't add any items. Did an error occur?");
					}
					break;
				}
			}
		}	
		lumber.skippedCounter = 0;
	}
	
	@Override
	public void run() {
		if (this.lumber.lock.tryLock()) {
			try {
				try {
					processLumberUpdate();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} finally {
				this.lumber.lock.unlock();
			}
		} else {
			debug(this.lumber, "Failed to get lock while trying to start task, aborting.");
		}
	}
}
