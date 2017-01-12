package com.avrgaming.civcraft.threading.tasks;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.avrgaming.civcraft.exception.CivTaskAbortException;
import com.avrgaming.civcraft.lorestorage.LoreMaterial;
import com.avrgaming.civcraft.main.CivData;
import com.avrgaming.civcraft.main.CivLog;
import com.avrgaming.civcraft.object.StructureChest;
import com.avrgaming.civcraft.structure.Quarry;
import com.avrgaming.civcraft.structure.Quarry.Block;
import com.avrgaming.civcraft.structure.Structure;
import com.avrgaming.civcraft.threading.CivAsyncTask;
import com.avrgaming.civcraft.threading.sync.request.UpdateInventoryRequest.Action;
import com.avrgaming.civcraft.util.ItemManager;
import com.avrgaming.civcraft.util.MultiInventory;

public class QuarryAsyncTask extends CivAsyncTask {
	
	Quarry quarry;
	
	public static HashSet<String> debugTowns = new HashSet<String>();
	public static void debug(Quarry quarry, String msg) {
		if (debugTowns.contains(quarry.getTown().getName())) {
			CivLog.warning("QuarryDebug:"+quarry.getTown().getName()+":"+msg);
		}
	}	
	
	public QuarryAsyncTask(Structure quarry) {
		this.quarry = (Quarry)quarry;
	}
	
	private int getDigSpeed(ItemStack stack) {
		if (stack.hasItemMeta()) {
			ItemMeta testEnchantMeta = stack.getItemMeta();
			if (testEnchantMeta.hasEnchant(Enchantment.DIG_SPEED)) {
				debug(quarry, "Pickaxe has DIG_SPEED lvl: "+testEnchantMeta.getEnchantLevel(Enchantment.DIG_SPEED));
				return testEnchantMeta.getEnchantLevel(Enchantment.DIG_SPEED)+1;
			}
		}
		return 1;
	}
	
	private int getFortune(ItemStack stack) {
		if (stack.hasItemMeta()) {
			ItemMeta testEnchantMeta = stack.getItemMeta();
			if (testEnchantMeta.hasEnchant(Enchantment.LOOT_BONUS_BLOCKS)) {
				debug(quarry, "Pickaxe has LOOT BONUS BLOCKS lvl: "+testEnchantMeta.getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS));
				return testEnchantMeta.getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS)+1;
			}
		}
		return 1;
	}
	
	public void processQuarryUpdate() {
		if (!quarry.isActive()) {
			debug(quarry, "Quarry inactive...");
			return;
		}
		
		debug(quarry, "Processing Quarry...");
		// Grab each CivChest object we'll require.
		ArrayList<StructureChest> sources = quarry.getAllChestsById(1);
		ArrayList<StructureChest> destinations = quarry.getAllChestsById(2);
		if (sources.size() != 2 || destinations.size() != 2) {
			CivLog.error("Bad chests for Quarry in town:"+quarry.getTown().getName()+" sources:"+sources.size()+" dests:"+destinations.size());
			return;
		}
		
		// Make sure the chunk is loaded before continuing. Also, add get chest and add it to inventory.
		MultiInventory source_inv = new MultiInventory();
		MultiInventory dest_inv = new MultiInventory();
		
		try {
			for (StructureChest src : sources) {
				//this.syncLoadChunk(src.getCoord().getWorldname(), src.getCoord().getX(), src.getCoord().getZ());				
				Inventory tmp;
				try {
					tmp = this.getChestInventory(src.getCoord().getWorldname(), src.getCoord().getX(), src.getCoord().getY(), src.getCoord().getZ(), false);
				} catch (CivTaskAbortException e) {
					//e.printStackTrace();
					CivLog.warning("Quarry:"+e.getMessage());
					return;
				}
				if (tmp == null) {
					quarry.skippedCounter++;
					return;
				}
				source_inv.addInventory(tmp);
			}
			
			boolean full = true;
			for (StructureChest dst : destinations) {
				//this.syncLoadChunk(dst.getCoord().getWorldname(), dst.getCoord().getX(), dst.getCoord().getZ());
				Inventory tmp;
				try {
					tmp = this.getChestInventory(dst.getCoord().getWorldname(), dst.getCoord().getX(), dst.getCoord().getY(), dst.getCoord().getZ(), false);
				} catch (CivTaskAbortException e) {
					//e.printStackTrace();
					CivLog.warning("Quarry:"+e.getMessage());
					return;
				}
				if (tmp == null) {
					quarry.skippedCounter++;
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
				//destination chest is full, stop processing.
				return;
			}
		} catch (InterruptedException e) {
			return;
		}
		
		debug(quarry, "Processing Quarry:"+quarry.skippedCounter+1);
		ItemStack[] contents = source_inv.getContents();
		for (int i = 0; i < quarry.skippedCounter+1; i++) {
			for(ItemStack stack : contents) {
				if (stack == null) {
					continue;
				}
				
				//Grab custom items first.
				if (LoreMaterial.isCustom(stack)) {
					String itemID = LoreMaterial.getMaterial(stack).getId();
					if (itemID.contains("base_hammer")) {
						try {
							ItemStack newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get(itemID));
							this.updateInventory(Action.REMOVE, source_inv, newItem);
						} catch (InterruptedException e) {
							return;
						}
						
						Random rand = new Random();
						int randMax = Quarry.HAMMERS_MAX;
						int rand1 = rand.nextInt(randMax);
						ArrayList<ItemStack> newItems = new ArrayList<ItemStack>();
						if (rand1 < ((int)((quarry.getHammerChance(Block.GDA))*randMax))) {
							int factor = rand.nextInt(3);
							if (factor == 1) {
								newItems.add(ItemManager.createItemStack(CivData.STONE, 2, (short)CivData.GRANITE));
							} else if (factor == 2) {
								newItems.add(ItemManager.createItemStack(CivData.STONE, 2, (short)CivData.DIORITE));
							} else {
								newItems.add(ItemManager.createItemStack(CivData.STONE, 2, (short)CivData.ANDESITE));
							}
						} else if (rand1 < ((int)((quarry.getHammerChance(Block.STONE))*randMax))) {
							newItems.add(ItemManager.createItemStack(CivData.STONE, 2));
						} else {
							newItems.add(ItemManager.createItemStack(CivData.COBBLESTONE, 2));
						} try {
							for (ItemStack item : newItems) {
								debug(quarry, "Updating inventory:"+item);
								this.updateInventory(Action.ADD, dest_inv, item);
							}
						} catch (InterruptedException e) {
							return;
						}
						break;
					}
					
					if (itemID.contains("base_beaker")) {
						try {
							ItemStack newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get(itemID));
							this.updateInventory(Action.REMOVE, source_inv, newItem);
						} catch (InterruptedException e) {
							return;
						}
						
						Random rand = new Random();
						int randMax = Quarry.BEAKERS_MAX;
						int rand1 = rand.nextInt(randMax);
						ArrayList<ItemStack> newItems = new ArrayList<ItemStack>();
						if (rand1 < ((int)((quarry.getBeakerChance(Block.BRM))*randMax))) {
							int factor = rand.nextInt(3);
							if (factor == 1) {
								newItems.add(ItemManager.createItemStack(CivData.MAGMA_BLOCK, 2));
							} else if (factor == 2) {
								newItems.add(ItemManager.createItemStack(CivData.RED_NETHER_BRICK, 2));
							} else {
								newItems.add(ItemManager.createItemStack(CivData.NETHER_BRICK, 2));
							}
						} else if (rand1 < ((int)((quarry.getBeakerChance(Block.WARTBLOCK))*randMax))) {
							newItems.add(ItemManager.createItemStack(CivData.NETHER_WART_BLOCK, 2));
						} else {
							newItems.add(ItemManager.createItemStack(CivData.NETHERRACK, 2));
						} try {
							for (ItemStack item : newItems) {
								debug(quarry, "Updating inventory:"+item);
								this.updateInventory(Action.ADD, dest_inv, item);
							}
						} catch (InterruptedException e) {
							return;
						}
						break;
					}
				}
				
				//Regular items continue here.
				int mod = getDigSpeed(stack)*getFortune(stack);
				
				if (ItemManager.getId(stack) == CivData.WOOD_PICKAXE) {
					try {
						short damage = ItemManager.getData(stack);
						this.updateInventory(Action.REMOVE, source_inv, stack);
						damage+= mod;
						stack.setDurability(damage);
						if (damage < 59) {
							this.updateInventory(Action.ADD, source_inv, stack);
						}
					} catch (InterruptedException e) {
						return;
					}
					
					// Attempt to get special resources
					Random rand = new Random();
					int randMax = Quarry.WP_MAX;
					int rand1 = rand.nextInt(randMax);
					ItemStack newItem;
					
					if (rand1 < (int)((quarry.getWPChance(Block.IRON))*randMax)) {
						newItem = ItemManager.createItemStack(CivData.IRON_ORE, mod);
					} else if (rand1 < (int)((quarry.getWPChance(Block.COAL))*randMax)) {
						newItem = ItemManager.createItemStack(CivData.COAL_ORE, mod);
					} else {
						newItem = ItemManager.createItemStack(CivData.COBBLESTONE, mod);
					}
					
					//Try to add the new item to the dest chest, if we cant, oh well.
					try {
						debug(quarry, "Updating inventory:"+newItem);
						this.updateInventory(Action.ADD, dest_inv, newItem);
					} catch (InterruptedException e) {
						return;
					}
					break;
				}
				
				if (ItemManager.getId(stack) == CivData.STONE_PICKAXE) {
					try {
						short damage = ItemManager.getData(stack);
						this.updateInventory(Action.REMOVE, source_inv, stack);
						damage+= mod;
						stack.setDurability(damage);
						if (damage < 131) {
							this.updateInventory(Action.ADD, source_inv, stack);
						}
					} catch (InterruptedException e) {
						return;
					}
					
					// Attempt to get special resources
					Random rand = new Random();
					int randMax = Quarry.WP_MAX;
					int rand1 = rand.nextInt(randMax);
					ItemStack newItem;
					
					if (rand1 < (int)((quarry.getWPChance(Block.REDSTONE))*randMax)) {
						newItem = ItemManager.createItemStack(CivData.REDSTONE_ORE, mod);
					} else if (rand1 < (int)((quarry.getWPChance(Block.GOLD))*randMax)) {
						newItem = ItemManager.createItemStack(CivData.GOLD_ORE, mod);
					} else if (rand1 < (int)((quarry.getWPChance(Block.IRON))*randMax)) {
						newItem = ItemManager.createItemStack(CivData.IRON_ORE, mod);
					} else if (rand1 < (int)((quarry.getWPChance(Block.COAL))*randMax)) {
						newItem = ItemManager.createItemStack(CivData.COAL_ORE, mod);
					} else {
						newItem = ItemManager.createItemStack(CivData.COBBLESTONE, mod);
					}
					
					//Try to add the new item to the dest chest, if we cant, oh well.
					try {
						debug(quarry, "Updating inventory:"+newItem);
						this.updateInventory(Action.ADD, dest_inv, newItem);
					} catch (InterruptedException e) {
						return;
					}
					break;
				}
			}	
		}
		quarry.skippedCounter = 0;
	}
	
	@Override
	public void run() {
		if (this.quarry.lock.tryLock()) {
			try {
				try {
					processQuarryUpdate();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} finally {
				this.quarry.lock.unlock();
			}
		} else {
			debug(this.quarry, "Failed to get lock while trying to start task, aborting.");
		}
	}
}
