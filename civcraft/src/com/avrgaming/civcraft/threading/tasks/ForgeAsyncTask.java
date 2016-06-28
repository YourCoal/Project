package com.avrgaming.civcraft.threading.tasks;

/*import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.avrgaming.civcraft.config.CivSettings;
import com.avrgaming.civcraft.exception.CivTaskAbortException;
import com.avrgaming.civcraft.lorestorage.LoreMaterial;
import com.avrgaming.civcraft.main.CivData;
import com.avrgaming.civcraft.main.CivLog;
import com.avrgaming.civcraft.object.StructureChest;
import com.avrgaming.civcraft.structure.Forge;
import com.avrgaming.civcraft.structure.Forge.Mineral;
import com.avrgaming.civcraft.structure.Structure;*/
import com.avrgaming.civcraft.threading.CivAsyncTask;
/*import com.avrgaming.civcraft.threading.sync.request.UpdateInventoryRequest.Action;
import com.avrgaming.civcraft.util.ItemManager;
import com.avrgaming.civcraft.util.MultiInventory;*/

public class ForgeAsyncTask extends CivAsyncTask {
	
	
	//TODO WE NEED TO MAKE IT SO THAT WE CAN REMOVE CUSTOM ITEMS BY NAME!!!
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
/*	Forge forge;
	public static HashSet<String> debugTowns = new HashSet<String>();
	
	public static void debug(Forge forge, String msg) {
		if (debugTowns.contains(forge.getTown().getName())) {
			CivLog.warning("ForgeDebug:"+forge.getTown().getName()+":"+msg);
		}
	}	
	
	public ForgeAsyncTask(Structure forge) {
		this.forge = (Forge)forge;
	}
	
	public void processForgeUpdate() {
		if (!forge.isActive()) {
			debug(forge, "Forge inactive...");
			return;
		}
		
		debug(forge, "Processing Forge...");
		ArrayList<StructureChest> sources = forge.getAllChestsById(1);
		ArrayList<StructureChest> destinations = forge.getAllChestsById(2);
		
		if (sources.size() != 2 || destinations.size() != 2) {
			CivLog.error("Bad chests for Forge in town:"+forge.getTown().getName()+" sources:"+sources.size()+" dests:"+destinations.size());
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
					CivLog.warning("Forge:"+e.getMessage());
					return;
				}
				if (tmp == null) {
					forge.skippedCounter++;
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
					CivLog.warning("Forge:"+e.getMessage());
					return;
				}
				if (tmp == null) {
					forge.skippedCounter++;
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
				//Stop processing
				return;
			}
		} catch (InterruptedException e) {
			return;
		}
		
		debug(forge, "Processing Forge:"+forge.skippedCounter+1);
		ItemStack[] contents = source_inv.getContents();
		for (int i = 0; i < forge.skippedCounter+1; i++) {
			for(ItemStack stack : contents) {
				if (stack == null) {
					continue;
				}
				
				//XXX Level 1, Cobblestone
				//XXX I made this useable to take in custom items, if we want to. :D
				if (ItemManager.getId(stack) == CivData.COBBLESTONE) {
				//if (ItemManager.getId(stack) == ItemManager.getData(LoreMaterial.materialMap.get("civ:rare_emerald_ore_rock"))) {
					try {
						this.updateInventory(Action.REMOVE, source_inv, ItemManager.createItemStack(CivData.COBBLESTONE, 1));
						//this.updateInventoryCust(Action.REMOVE_CUST, source_inv, ItemManager.createItemStack(LoreMaterial.materialMap.get("civ:rare_emerald_ore_rock")));
					} catch (InterruptedException e) {
						return;
					}
					
					// Attempt to get special resources
					Random rand = new Random();
					int randMax = Trommel.GRAVEL_MAX_CHANCE;
					int rand1 = rand.nextInt(randMax);
					ItemStack newItem;			
					if (rand1 < ((int)((trommel.getGravelChance(Mineral.CHROMIUM))*randMax))) {
						newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("mat_chromium_ore"));
					} else if (rand1 < ((int)((trommel.getGravelChance(Mineral.EMERALD))*randMax))) {
						int rand2 = rand.nextInt(randMax);
						if (rand2 < (randMax/10)) {
							newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:rare_emerald_ore_rock"));
						} else {
							newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:common_emerald_ore_rock"));
						}
					} else if (rand1 < ((int)((trommel.getGravelChance(Mineral.DIAMOND))*randMax))) {
						int rand2 = rand.nextInt(randMax);
						if (rand2 < (randMax/10)) {
							newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:rare_diamond_ore_rock"));
						} else {
							newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:common_diamond_ore_rock"));
						}
					} else if (rand1 < ((int)((trommel.getGravelChance(Mineral.REDSTONE))*randMax))) {
						int rand2 = rand.nextInt(randMax);
						if (rand2 < (randMax/10)) {
							newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:rare_redstone_ore_rock"));
						} else {
							newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:common_redstone_ore_rock"));
						}
					} else if (rand1 < ((int)((trommel.getGravelChance(Mineral.LAPIS))*randMax))) {
						int rand2 = rand.nextInt(randMax);
						if (rand2 < (randMax/10)) {
							newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:rare_lapis_ore_rock"));
						} else {
							newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:common_lapis_ore_rock"));
						}
					} else if (rand1 < ((int)((trommel.getGravelChance(Mineral.GOLD))*randMax))) {
						int rand2 = rand.nextInt(randMax);
						if (rand2 < (randMax/10)) {
							newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:rare_gold_ore_rock"));
						} else {
							newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:common_gold_ore_rock"));
						}
					} else if (rand1 < ((int)((trommel.getGravelChance(Mineral.IRON))*randMax))) {
						int rand2 = rand.nextInt(randMax);
						if (rand2 < (randMax/10)) {
							newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:rare_iron_ore_rock"));
						} else {
							newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:common_iron_ore_rock"));
						}
					} else {
						int rand2 = rand.nextInt(randMax);
						if (rand2 < (randMax/15)) {
							newItem = ItemManager.createItemStack(CivData.GRAVEL, 1);
						} else if (rand2 < (randMax/5)) {
							newItem = ItemManager.createItemStack(CivData.DIRT, 1);
						} else {
							newItem = ItemManager.createItemStack(CivData.AIR, 1);
						}
					}
					
					//Try to add the new item to the dest chest, if we cant, oh well.
					try {
						debug(trommel, "Updating inventory:"+newItem);
						this.updateInventory(Action.ADD, dest_inv, newItem);
					} catch (InterruptedException e) {
						return;
					}
					break;
				}
					
					
				//XXX Level 2, Regular Stone
				if (ItemManager.getId(stack) == CivData.STONE) {
					if (this.trommel.getLevel() >= 2 && ItemManager.getData(stack) == ItemManager.getData(ItemManager.getMaterialData(CivData.STONE, CivData.DATA_0))) {
						try {
							this.updateInventory(Action.REMOVE, source_inv, ItemManager.createItemStack(CivData.STONE, 1, (short) CivData.DATA_0));
						} catch (InterruptedException e) {
							return;
						}
						
						// Attempt to get special resources
						Random rand = new Random();
						int randMax = Trommel.STONE_MAX_CHANCE;
						int rand1 = rand.nextInt(randMax);
						ItemStack newItem;			
						if (rand1 < ((int)((trommel.getStoneChance(Mineral.CHROMIUM))*randMax))) {
							newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("mat_chromium_ore"));
						} else if (rand1 < ((int)((trommel.getStoneChance(Mineral.EMERALD))*randMax))) {
							int rand2 = rand.nextInt(randMax);
							if (rand2 < (randMax/10)) {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:rare_emerald_ore_rock"));
							} else {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:common_emerald_ore_rock"));
							}
						} else if (rand1 < ((int)((trommel.getStoneChance(Mineral.DIAMOND))*randMax))) {
							int rand2 = rand.nextInt(randMax);
							if (rand2 < (randMax/10)) {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:rare_diamond_ore_rock"));
							} else {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:common_diamond_ore_rock"));
							}
						} else if (rand1 < ((int)((trommel.getStoneChance(Mineral.REDSTONE))*randMax))) {
							int rand2 = rand.nextInt(randMax);
							if (rand2 < (randMax/10)) {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:rare_redstone_ore_rock"));
							} else {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:common_redstone_ore_rock"));
							}
						} else if (rand1 < ((int)((trommel.getStoneChance(Mineral.LAPIS))*randMax))) {
							int rand2 = rand.nextInt(randMax);
							if (rand2 < (randMax/10)) {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:rare_lapis_ore_rock"));
							} else {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:common_lapis_ore_rock"));
							}
						} else if (rand1 < ((int)((trommel.getStoneChance(Mineral.GOLD))*randMax))) {
							int rand2 = rand.nextInt(randMax);
							if (rand2 < (randMax/10)) {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:rare_gold_ore_rock"));
							} else {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:common_gold_ore_rock"));
							}
						} else if (rand1 < ((int)((trommel.getStoneChance(Mineral.IRON))*randMax))) {
							int rand2 = rand.nextInt(randMax);
							if (rand2 < (randMax/10)) {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:rare_iron_ore_rock"));
							} else {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:common_iron_ore_rock"));
							}
						} else {
							int rand2 = rand.nextInt(randMax);
							if (rand2 < (randMax/15)) {
								newItem = ItemManager.createItemStack(CivData.GRAVEL, 1);
							} else if (rand2 < (randMax/5)) {
								newItem = ItemManager.createItemStack(CivData.DIRT, 1);
							} else {
								newItem = ItemManager.createItemStack(CivData.AIR, 1);
							}
						}
						
						//Try to add the new item to the dest chest, if we cant, oh well.
						try {
							debug(trommel, "Updating inventory:"+newItem);
							this.updateInventory(Action.ADD, dest_inv, newItem);
						} catch (InterruptedException e) {
							return;
						}
						break;
					}
					
					
					//XXX Level 2, Granite
					if (this.trommel.getLevel() >= 2 && ItemManager.getData(stack) == ItemManager.getData(ItemManager.getMaterialData(CivData.STONE, CivData.DATA_1))) {
						try {
							this.updateInventory(Action.REMOVE, source_inv, ItemManager.createItemStack(CivData.STONE, 1, (short) CivData.DATA_1));
						} catch (InterruptedException e) {
							return;
						}
						
						// Attempt to get special resources
						Random rand = new Random();
						int randMax = Trommel.GRANITE_MAX_CHANCE;
						int rand1 = rand.nextInt(randMax);
						ItemStack newItem;			
						if (rand1 < ((int)((trommel.getGraniteChance(Mineral.CHROMIUM))*randMax))) {
							newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("mat_chromium_ore"));
						} else if (rand1 < ((int)((trommel.getGraniteChance(Mineral.EMERALD))*randMax))) {
							int rand2 = rand.nextInt(randMax);
							if (rand2 < (randMax/10)) {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:rare_emerald_ore_rock"));
							} else {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:common_emerald_ore_rock"));
							}
						} else if (rand1 < ((int)((trommel.getGraniteChance(Mineral.DIAMOND))*randMax))) {
							int rand2 = rand.nextInt(randMax);
							if (rand2 < (randMax/10)) {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:rare_diamond_ore_rock"));
							} else {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:common_diamond_ore_rock"));
							}
						} else if (rand1 < ((int)((trommel.getGraniteChance(Mineral.REDSTONE))*randMax))) {
							int rand2 = rand.nextInt(randMax);
							if (rand2 < (randMax/10)) {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:rare_redstone_ore_rock"));
							} else {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:common_redstone_ore_rock"));
							}
						} else if (rand1 < ((int)((trommel.getGraniteChance(Mineral.LAPIS))*randMax))) {
							int rand2 = rand.nextInt(randMax);
							if (rand2 < (randMax/10)) {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:rare_lapis_ore_rock"));
							} else {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:common_lapis_ore_rock"));
							}
						} else if (rand1 < ((int)((trommel.getGraniteChance(Mineral.GOLD))*randMax))) {
							int rand2 = rand.nextInt(randMax);
							if (rand2 < (randMax/10)) {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:rare_gold_ore_rock"));
							} else {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:common_gold_ore_rock"));
							}
						} else if (rand1 < ((int)((trommel.getGraniteChance(Mineral.IRON))*randMax))) {
							int rand2 = rand.nextInt(randMax);
							if (rand2 < (randMax/10)) {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:rare_iron_ore_rock"));
							} else {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:common_iron_ore_rock"));
							}
						} else {
							int rand2 = rand.nextInt(randMax);
							if (rand2 < (randMax/15)) {
								newItem = ItemManager.createItemStack(CivData.GRAVEL, 1);
							} else if (rand2 < (randMax/5)) {
								newItem = ItemManager.createItemStack(CivData.DIRT, 1);
							} else {
								newItem = ItemManager.createItemStack(CivData.AIR, 1);
							}
						}
						
						//Try to add the new item to the dest chest, if we cant, oh well.
						try {
							debug(trommel, "Updating inventory:"+newItem);
							this.updateInventory(Action.ADD, dest_inv, newItem);
						} catch (InterruptedException e) {
							return;
						}
						break;
					}
					
					
					//XXX Level 3, Diorite
					if (this.trommel.getLevel() >= 3 && ItemManager.getData(stack) == ItemManager.getData(ItemManager.getMaterialData(CivData.STONE, CivData.DATA_3))) {
						try {
							this.updateInventory(Action.REMOVE, source_inv, ItemManager.createItemStack(CivData.STONE, 1, (short) CivData.DATA_3));
						} catch (InterruptedException e) {
							return;
						}
						// Attempt to get special resources
						Random rand = new Random();
						int randMax = Trommel.DIORITE_MAX_CHANCE;
						int rand1 = rand.nextInt(randMax);
						ItemStack newItem;
						if (rand1 < ((int)((trommel.getDioriteChance(Mineral.CHROMIUM))*randMax))) {
							newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("mat_chromium_ore"));
						} else if (rand1 < ((int)((trommel.getDioriteChance(Mineral.EMERALD))*randMax))) {
							int rand2 = rand.nextInt(randMax);
							if (rand2 < (randMax/10)) {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:rare_emerald_ore_rock"));
							} else {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:common_emerald_ore_rock"));
							}
						} else if (rand1 < ((int)((trommel.getDioriteChance(Mineral.DIAMOND))*randMax))) {
							int rand2 = rand.nextInt(randMax);
							if (rand2 < (randMax/10)) {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:rare_diamond_ore_rock"));
							} else {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:common_diamond_ore_rock"));
							}
						} else if (rand1 < ((int)((trommel.getDioriteChance(Mineral.REDSTONE))*randMax))) {
							int rand2 = rand.nextInt(randMax);
							if (rand2 < (randMax/10)) {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:rare_redstone_ore_rock"));
							} else {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:common_redstone_ore_rock"));
							}
						} else if (rand1 < ((int)((trommel.getDioriteChance(Mineral.LAPIS))*randMax))) {
							int rand2 = rand.nextInt(randMax);
							if (rand2 < (randMax/10)) {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:rare_lapis_ore_rock"));
							} else {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:common_lapis_ore_rock"));
							}
						} else if (rand1 < ((int)((trommel.getDioriteChance(Mineral.GOLD))*randMax))) {
							int rand2 = rand.nextInt(randMax);
							if (rand2 < (randMax/10)) {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:rare_gold_ore_rock"));
							} else {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:common_gold_ore_rock"));
							}
						} else if (rand1 < ((int)((trommel.getDioriteChance(Mineral.IRON))*randMax))) {
							int rand2 = rand.nextInt(randMax);
							if (rand2 < (randMax/10)) {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:rare_iron_ore_rock"));
							} else {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:common_iron_ore_rock"));
							}
						} else {
							int rand2 = rand.nextInt(randMax);
							if (rand2 < (randMax/15)) {
								newItem = ItemManager.createItemStack(CivData.GRAVEL, 1);
							} else if (rand2 < (randMax/5)) {
								newItem = ItemManager.createItemStack(CivData.DIRT, 1);
							} else {
								newItem = ItemManager.createItemStack(CivData.AIR, 1);
							}
						}
						
						//Try to add the new item to the dest chest, if we cant, oh well.
						try {
							debug(trommel, "Updating inventory:"+newItem);
							this.updateInventory(Action.ADD, dest_inv, newItem);
						} catch (InterruptedException e) {
							return;
						}
						break;
					}
					
					
					//XXX Level 3, Polished Granite
					if (this.trommel.getLevel() >= 3 && ItemManager.getData(stack) == ItemManager.getData(ItemManager.getMaterialData(CivData.STONE, CivData.DATA_2))) {
						try {
							this.updateInventory(Action.REMOVE, source_inv, ItemManager.createItemStack(CivData.STONE, 1, (short) CivData.DATA_2));
						} catch (InterruptedException e) {
							return;
						}
						// Attempt to get special resources
						Random rand = new Random();
						int randMax = Trommel.POL_GRANITE_MAX_CHANCE;
						int rand1 = rand.nextInt(randMax);
						ItemStack newItem;
						if (rand1 < ((int)((trommel.getPolGraniteChance(Mineral.CHROMIUM))*randMax))) {
							newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("mat_chromium_ore"));
						} else if (rand1 < ((int)((trommel.getPolGraniteChance(Mineral.EMERALD))*randMax))) {
							int rand2 = rand.nextInt(randMax);
							if (rand2 < (randMax/10)) {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:rare_emerald_ore_rock"));
							} else {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:common_emerald_ore_rock"));
							}
						} else if (rand1 < ((int)((trommel.getPolGraniteChance(Mineral.DIAMOND))*randMax))) {
							int rand2 = rand.nextInt(randMax);
							if (rand2 < (randMax/10)) {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:rare_diamond_ore_rock"));
							} else {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:common_diamond_ore_rock"));
							}
						} else if (rand1 < ((int)((trommel.getPolGraniteChance(Mineral.REDSTONE))*randMax))) {
							int rand2 = rand.nextInt(randMax);
							if (rand2 < (randMax/10)) {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:rare_redstone_ore_rock"));
							} else {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:common_redstone_ore_rock"));
							}
						} else if (rand1 < ((int)((trommel.getPolGraniteChance(Mineral.LAPIS))*randMax))) {
							int rand2 = rand.nextInt(randMax);
							if (rand2 < (randMax/10)) {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:rare_lapis_ore_rock"));
							} else {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:common_lapis_ore_rock"));
							}
						} else if (rand1 < ((int)((trommel.getPolGraniteChance(Mineral.GOLD))*randMax))) {
							int rand2 = rand.nextInt(randMax);
							if (rand2 < (randMax/10)) {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:rare_gold_ore_rock"));
							} else {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:common_gold_ore_rock"));
							}
						} else if (rand1 < ((int)((trommel.getPolGraniteChance(Mineral.IRON))*randMax))) {
							int rand2 = rand.nextInt(randMax);
							if (rand2 < (randMax/10)) {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:rare_iron_ore_rock"));
							} else {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:common_iron_ore_rock"));
							}
						} else {
							int rand2 = rand.nextInt(randMax);
							if (rand2 < (randMax/15)) {
								newItem = ItemManager.createItemStack(CivData.GRAVEL, 1);
							} else if (rand2 < (randMax/5)) {
								newItem = ItemManager.createItemStack(CivData.DIRT, 1);
							} else {
								newItem = ItemManager.createItemStack(CivData.AIR, 1);
							}
						}
						
						//Try to add the new item to the dest chest, if we cant, oh well.
						try {
							debug(trommel, "Updating inventory:"+newItem);
							this.updateInventory(Action.ADD, dest_inv, newItem);
						} catch (InterruptedException e) {
							return;
						}
						break;
					}
					
					
					//XXX Level 4, Andesite
					if (this.trommel.getLevel() >= 4 && ItemManager.getData(stack) == ItemManager.getData(ItemManager.getMaterialData(CivData.STONE, CivData.DATA_5))) {
						try {
							this.updateInventory(Action.REMOVE, source_inv, ItemManager.createItemStack(CivData.STONE, 1, (short) CivData.DATA_5));
						} catch (InterruptedException e) {
							return;
						}
						// Attempt to get special resources
						Random rand = new Random();
						int randMax = Trommel.ANDESITE_MAX_CHANCE;
						int rand1 = rand.nextInt(randMax);
						ItemStack newItem;
						if (rand1 < ((int)((trommel.getAndesiteChance(Mineral.CHROMIUM))*randMax))) {
							newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("mat_chromium_ore"));
						} else if (rand1 < ((int)((trommel.getAndesiteChance(Mineral.EMERALD))*randMax))) {
							int rand2 = rand.nextInt(randMax);
							if (rand2 < (randMax/10)) {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:rare_emerald_ore_rock"));
							} else {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:common_emerald_ore_rock"));
							}
						} else if (rand1 < ((int)((trommel.getAndesiteChance(Mineral.DIAMOND))*randMax))) {
							int rand2 = rand.nextInt(randMax);
							if (rand2 < (randMax/10)) {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:rare_diamond_ore_rock"));
							} else {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:common_diamond_ore_rock"));
							}
						} else if (rand1 < ((int)((trommel.getAndesiteChance(Mineral.REDSTONE))*randMax))) {
							int rand2 = rand.nextInt(randMax);
							if (rand2 < (randMax/10)) {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:rare_redstone_ore_rock"));
							} else {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:common_redstone_ore_rock"));
							}
						} else if (rand1 < ((int)((trommel.getAndesiteChance(Mineral.LAPIS))*randMax))) {
							int rand2 = rand.nextInt(randMax);
							if (rand2 < (randMax/10)) {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:rare_lapis_ore_rock"));
							} else {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:common_lapis_ore_rock"));
							}
						} else if (rand1 < ((int)((trommel.getAndesiteChance(Mineral.GOLD))*randMax))) {
							int rand2 = rand.nextInt(randMax);
							if (rand2 < (randMax/10)) {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:rare_gold_ore_rock"));
							} else {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:common_gold_ore_rock"));
							}
						} else if (rand1 < ((int)((trommel.getAndesiteChance(Mineral.IRON))*randMax))) {
							int rand2 = rand.nextInt(randMax);
							if (rand2 < (randMax/10)) {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:rare_iron_ore_rock"));
							} else {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:common_iron_ore_rock"));
							}
						} else {
							int rand2 = rand.nextInt(randMax);
							if (rand2 < (randMax/15)) {
								newItem = ItemManager.createItemStack(CivData.GRAVEL, 1);
							} else if (rand2 < (randMax/5)) {
								newItem = ItemManager.createItemStack(CivData.DIRT, 1);
							} else {
								newItem = ItemManager.createItemStack(CivData.AIR, 1);
							}
						}
						
						//Try to add the new item to the dest chest, if we cant, oh well.
						try {
							debug(trommel, "Updating inventory:"+newItem);
							this.updateInventory(Action.ADD, dest_inv, newItem);
						} catch (InterruptedException e) {
							return;
						}
						break;
					}
					
					
					//XXX Level 4, Polished Diorite
					if (this.trommel.getLevel() >= 4 && ItemManager.getData(stack) == ItemManager.getData(ItemManager.getMaterialData(CivData.STONE, CivData.DATA_4))) {
						try {
							this.updateInventory(Action.REMOVE, source_inv, ItemManager.createItemStack(CivData.STONE, 1, (short) CivData.DATA_4));
						} catch (InterruptedException e) {
							return;
						}
						// Attempt to get special resources
						Random rand = new Random();
						int randMax = Trommel.POL_DIORITE_MAX_CHANCE;
						int rand1 = rand.nextInt(randMax);
						ItemStack newItem;
						if (rand1 < ((int)((trommel.getPolDioriteChance(Mineral.CHROMIUM))*randMax))) {
							newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("mat_chromium_ore"));
						} else if (rand1 < ((int)((trommel.getPolDioriteChance(Mineral.EMERALD))*randMax))) {
							int rand2 = rand.nextInt(randMax);
							if (rand2 < (randMax/10)) {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:rare_emerald_ore_rock"));
							} else {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:common_emerald_ore_rock"));
							}
						} else if (rand1 < ((int)((trommel.getPolDioriteChance(Mineral.DIAMOND))*randMax))) {
							int rand2 = rand.nextInt(randMax);
							if (rand2 < (randMax/10)) {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:rare_diamond_ore_rock"));
							} else {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:common_diamond_ore_rock"));
							}
						} else if (rand1 < ((int)((trommel.getPolDioriteChance(Mineral.REDSTONE))*randMax))) {
							int rand2 = rand.nextInt(randMax);
							if (rand2 < (randMax/10)) {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:rare_redstone_ore_rock"));
							} else {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:common_redstone_ore_rock"));
							}
						} else if (rand1 < ((int)((trommel.getPolDioriteChance(Mineral.LAPIS))*randMax))) {
							int rand2 = rand.nextInt(randMax);
							if (rand2 < (randMax/10)) {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:rare_lapis_ore_rock"));
							} else {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:common_lapis_ore_rock"));
							}
						} else if (rand1 < ((int)((trommel.getPolDioriteChance(Mineral.GOLD))*randMax))) {
							int rand2 = rand.nextInt(randMax);
							if (rand2 < (randMax/10)) {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:rare_gold_ore_rock"));
							} else {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:common_gold_ore_rock"));
							}
						} else if (rand1 < ((int)((trommel.getPolDioriteChance(Mineral.IRON))*randMax))) {
							int rand2 = rand.nextInt(randMax);
							if (rand2 < (randMax/10)) {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:rare_iron_ore_rock"));
							} else {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:common_iron_ore_rock"));
							}
						} else {
							int rand2 = rand.nextInt(randMax);
							if (rand2 < (randMax/15)) {
								newItem = ItemManager.createItemStack(CivData.GRAVEL, 1);
							} else if (rand2 < (randMax/5)) {
								newItem = ItemManager.createItemStack(CivData.DIRT, 1);
							} else {
								newItem = ItemManager.createItemStack(CivData.AIR, 1);
							}
						}
						
						//Try to add the new item to the dest chest, if we cant, oh well.
						try {
							debug(trommel, "Updating inventory:"+newItem);
							this.updateInventory(Action.ADD, dest_inv, newItem);
						} catch (InterruptedException e) {
							return;
						}
						break;
					}
					
					
					//XXX Level 5, Polished Andesite
					if (this.trommel.getLevel() >= 5 && ItemManager.getData(stack) == ItemManager.getData(ItemManager.getMaterialData(CivData.STONE, CivData.DATA_6))) {
						try {
							this.updateInventory(Action.REMOVE, source_inv, ItemManager.createItemStack(CivData.STONE, 1, (short) CivData.DATA_6));
						} catch (InterruptedException e) {
							return;
						}
						// Attempt to get special resources
						Random rand = new Random();
						int randMax = Trommel.POL_DIORITE_MAX_CHANCE;
						int rand1 = rand.nextInt(randMax);
						ItemStack newItem;
						if (rand1 < ((int)((trommel.getPolAndesiteChance(Mineral.CHROMIUM))*randMax))) {
							newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("mat_chromium_ore"));
						} else if (rand1 < ((int)((trommel.getPolAndesiteChance(Mineral.EMERALD))*randMax))) {
							int rand2 = rand.nextInt(randMax);
							if (rand2 < (randMax/10)) {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:rare_emerald_ore_rock"));
							} else {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:common_emerald_ore_rock"));
							}
						} else if (rand1 < ((int)((trommel.getPolAndesiteChance(Mineral.DIAMOND))*randMax))) {
							int rand2 = rand.nextInt(randMax);
							if (rand2 < (randMax/10)) {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:rare_diamond_ore_rock"));
							} else {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:common_diamond_ore_rock"));
							}
						} else if (rand1 < ((int)((trommel.getPolAndesiteChance(Mineral.REDSTONE))*randMax))) {
							int rand2 = rand.nextInt(randMax);
							if (rand2 < (randMax/10)) {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:rare_redstone_ore_rock"));
							} else {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:common_redstone_ore_rock"));
							}
						} else if (rand1 < ((int)((trommel.getPolAndesiteChance(Mineral.LAPIS))*randMax))) {
							int rand2 = rand.nextInt(randMax);
							if (rand2 < (randMax/10)) {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:rare_lapis_ore_rock"));
							} else {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:common_lapis_ore_rock"));
							}
						} else if (rand1 < ((int)((trommel.getPolAndesiteChance(Mineral.GOLD))*randMax))) {
							int rand2 = rand.nextInt(randMax);
							if (rand2 < (randMax/10)) {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:rare_gold_ore_rock"));
							} else {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:common_gold_ore_rock"));
							}
						} else if (rand1 < ((int)((trommel.getPolAndesiteChance(Mineral.IRON))*randMax))) {
							int rand2 = rand.nextInt(randMax);
							if (rand2 < (randMax/10)) {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:rare_iron_ore_rock"));
							} else {
								newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:common_iron_ore_rock"));
							}
						} else {
							int rand2 = rand.nextInt(randMax);
							if (rand2 < (randMax/15)) {
								newItem = ItemManager.createItemStack(CivData.GRAVEL, 1);
							} else if (rand2 < (randMax/5)) {
								newItem = ItemManager.createItemStack(CivData.DIRT, 1);
							} else {
								newItem = ItemManager.createItemStack(CivData.AIR, 1);
							}
						}
						
						//Try to add the new item to the dest chest, if we cant, oh well.
						try {
							debug(trommel, "Updating inventory:"+newItem);
							this.updateInventory(Action.ADD, dest_inv, newItem);
						} catch (InterruptedException e) {
							return;
						}
						break;
					}
				}
				
				
				//XXX Level 5, Obsidian
				if (this.trommel.getLevel() >= 5 && ItemManager.getId(stack) == CivData.OBSIDIAN) {
					try {
						this.updateInventory(Action.REMOVE, source_inv, ItemManager.createItemStack(CivData.OBSIDIAN, 1));
					} catch (InterruptedException e) {
						return;
					}
					// Attempt to get special resources
					Random rand = new Random();
					int randMax = Trommel.ANDESITE_MAX_CHANCE;
					int rand1 = rand.nextInt(randMax);
					ItemStack newItem;
					if (rand1 < ((int)((trommel.getObsidianChance(Mineral.CHROMIUM))*randMax))) {
						newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("mat_chromium_ore"));
					} else if (rand1 < ((int)((trommel.getObsidianChance(Mineral.EMERALD))*randMax))) {
						int rand2 = rand.nextInt(randMax);
						if (rand2 < (randMax/10)) {
							newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:rare_emerald_ore_rock"));
						} else {
							newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:common_emerald_ore_rock"));
						}
					} else if (rand1 < ((int)((trommel.getObsidianChance(Mineral.DIAMOND))*randMax))) {
						int rand2 = rand.nextInt(randMax);
						if (rand2 < (randMax/10)) {
							newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:rare_diamond_ore_rock"));
						} else {
							newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:common_diamond_ore_rock"));
						}
					} else if (rand1 < ((int)((trommel.getObsidianChance(Mineral.REDSTONE))*randMax))) {
						int rand2 = rand.nextInt(randMax);
						if (rand2 < (randMax/10)) {
							newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:rare_redstone_ore_rock"));
						} else {
							newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:common_redstone_ore_rock"));
						}
					} else if (rand1 < ((int)((trommel.getObsidianChance(Mineral.LAPIS))*randMax))) {
						int rand2 = rand.nextInt(randMax);
						if (rand2 < (randMax/10)) {
							newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:rare_lapis_ore_rock"));
						} else {
							newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:common_lapis_ore_rock"));
						}
					} else if (rand1 < ((int)((trommel.getObsidianChance(Mineral.GOLD))*randMax))) {
						int rand2 = rand.nextInt(randMax);
						if (rand2 < (randMax/10)) {
							newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:rare_gold_ore_rock"));
						} else {
							newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:common_gold_ore_rock"));
						}
					} else if (rand1 < ((int)((trommel.getObsidianChance(Mineral.IRON))*randMax))) {
						int rand2 = rand.nextInt(randMax);
						if (rand2 < (randMax/10)) {
							newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:rare_iron_ore_rock"));
						} else {
							newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:common_iron_ore_rock"));
						}
					} else {
						int rand2 = rand.nextInt(randMax);
						if (rand2 < (randMax/15)) {
							newItem = ItemManager.createItemStack(CivData.GRAVEL, 1);
						} else if (rand2 < (randMax/5)) {
							newItem = ItemManager.createItemStack(CivData.DIRT, 1);
						} else {
							newItem = ItemManager.createItemStack(CivData.AIR, 1);
						}
					}
					
					//Try to add the new item to the dest chest, if we cant, oh well.
					try {
						debug(trommel, "Updating inventory:"+newItem);
						this.updateInventory(Action.ADD, dest_inv, newItem);
					} catch (InterruptedException e) {
						return;
					}
					break;
				}
			}	
		}
		trommel.skippedCounter = 0;
	}
	
	@Override
	public void run() {
		if (this.trommel.lock.tryLock()) {
			try {
				try {
					if (this.trommel.getTown().getGovernment().id.equals("gov_theocracy") || this.trommel.getTown().getGovernment().id.equals("gov_monarchy")) {
						Random rand = new Random();
						int randMax = 100;
						int rand1 = rand.nextInt(randMax);
						Double chance = CivSettings.getDouble(CivSettings.structureConfig, "trommel.penalty_rate")*100;
						if (rand1 < chance) {
							processTrommelUpdate();
							debug(this.trommel, "Not penalized");
						} else {
							debug(this.trommel, "Skip Due to Penalty");
						}
					} else {
						processTrommelUpdate();
						if (this.trommel.getTown().getGovernment().id.equals("gov_despotism")) {
							debug(this.trommel, "Doing Bonus");
							processTrommelUpdate();
						}
					}					
				} catch (Exception e) {
					e.printStackTrace();
				}
			} finally {
				this.trommel.lock.unlock();
			}
		} else {
			debug(this.trommel, "Failed to get lock while trying to start task, aborting.");
		}
	}*/
}
