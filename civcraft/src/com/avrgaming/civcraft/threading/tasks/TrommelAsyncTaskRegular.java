package com.avrgaming.civcraft.threading.tasks;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.avrgaming.civcraft.exception.CivTaskAbortException;
import com.avrgaming.civcraft.main.CivData;
import com.avrgaming.civcraft.main.CivLog;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.StructureChest;
import com.avrgaming.civcraft.object.TownChunk;
import com.avrgaming.civcraft.structure.Structure;
import com.avrgaming.civcraft.structure.Trommel;
import com.avrgaming.civcraft.structure.Trommel.Mineral;
import com.avrgaming.civcraft.threading.CivAsyncTask;
import com.avrgaming.civcraft.threading.sync.request.UpdateInventoryRequest.Action;
import com.avrgaming.civcraft.util.ItemManager;
import com.avrgaming.civcraft.util.MultiInventory;

public class TrommelAsyncTaskRegular extends CivAsyncTask {
	
	Trommel trommel;
	public static HashSet<String> debugTowns = new HashSet<String>();
	
	public static void debug(Trommel trommel, String msg) {
		if (debugTowns.contains(trommel.getTown().getName())) {
			CivLog.warning("Trommel(Regular) Debug:"+trommel.getTown().getName()+":"+msg);
		}
	}	
	
	public TrommelAsyncTaskRegular(Structure trommel) {
		this.trommel = (Trommel)trommel;
	}
	
	public void processTrommelUpdate() {
		if (!trommel.isActive()) {
			debug(trommel, "Trommel(Regular) inactive...");
			return;
		}
		
		debug(trommel, "Processing Trommel(Regular) ...");
		ArrayList<StructureChest> sources = trommel.getAllChestsById(1);
		ArrayList<StructureChest> destinations = trommel.getAllChestsById(2);
		
		if (sources.size() != 2 || destinations.size() != 2) {
			CivLog.error("Bad chests for Trommel(Regular) in town:"+trommel.getTown().getName()+" sources:"+sources.size()+" dests:"+destinations.size());
			return;
		}
		
		MultiInventory source_inv = new MultiInventory();
		MultiInventory dest_inv = new MultiInventory();
		try {
			for (StructureChest src : sources) {
				this.syncLoadChunk(src.getCoord().getWorldname(), src.getCoord().getX(), src.getCoord().getZ());				
				Inventory tmp;
				try {
					tmp = this.getChestInventory(src.getCoord().getWorldname(), src.getCoord().getX(), src.getCoord().getY(), src.getCoord().getZ(), false);
				} catch (CivTaskAbortException e) {
					e.printStackTrace();
					CivLog.warning("Trommel(Regular):"+e.getMessage());
					return;
				}
				if (tmp == null) {
					trommel.skippedCounter++;
					return;
				}
				source_inv.addInventory(tmp);
			}
			
			boolean full = true;
			for (StructureChest dst : destinations) {
				this.syncLoadChunk(dst.getCoord().getWorldname(), dst.getCoord().getX(), dst.getCoord().getZ());
				Inventory tmp;
				try {
					tmp = this.getChestInventory(dst.getCoord().getWorldname(), dst.getCoord().getX(), dst.getCoord().getY(), dst.getCoord().getZ(), false);
				} catch (CivTaskAbortException e) {
					e.printStackTrace();
					CivLog.warning("Trommel(Regular):"+e.getMessage());
					return;
				}
				if (tmp == null) {
					trommel.skippedCounter++;
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
		
		debug(trommel, "Processing trommel:"+trommel.skippedCounter+1);
		ItemStack[] contents = source_inv.getContents();
		for (int i = 0; i < trommel.skippedCounter+1; i++) {
			for(ItemStack stack : contents) {
				if (stack == null) {
					continue;
				}
				
				int mod = ((trommel.getTown().saved_trommel_level-1)/250)*4;
				
				if (ItemManager.getId(stack) == CivData.COBBLESTONE) {
					try {
						this.updateInventory(Action.REMOVE, source_inv, ItemManager.createItemStack(CivData.COBBLESTONE, 1));
					} catch (InterruptedException e) {
						return;
					}
					Random rand = new Random();
					int randMax = Trommel.COBBLE_MAX_RATE;
					int rand1 = rand.nextInt(randMax);
					ItemStack newItem;
					if (rand1 < ((int)((trommel.getGravelChance(Mineral.BLOCK)+mod)*randMax))) {
						ItemStack thatItem = ItemManager.createItemStack(CivData.COAL_BLOCK, 1);
						for (TownChunk tc : trommel.getTown().getTownChunks()) {
							if (tc.district.getID().equals(5)) {
								int tcChunkX = tc.getChunkCoord().getX();
								int tcChunkZ = tc.getChunkCoord().getZ();
								
								int sChunkX = trommel.getCorner().getLocation().getChunk().getX();
								int sChunkZ = trommel.getCorner().getLocation().getChunk().getZ();
								
								if (tcChunkX == sChunkX && tcChunkZ == sChunkZ) {
									CivMessage.global("Chunk SELECTED!!! :D");
									int theOre = rand.nextInt(110);
									if (theOre <= 50) {
										thatItem = ItemManager.createItemStack(CivData.IRON_BLOCK, 1);
									} else if (theOre >= 51 && theOre <= 75) {
										thatItem = ItemManager.createItemStack(CivData.GOLD_BLOCK, 1);
									} else if (theOre >= 76 && theOre <= 95) {
										thatItem = ItemManager.createItemStack(CivData.REDSTONE_BLOCK, 1);
									} else if (theOre >= 96 && theOre <= 105) {
										thatItem = ItemManager.createItemStack(CivData.DIAMOND_BLOCK, 1);
									} else if (theOre >= 106) {
										thatItem = ItemManager.createItemStack(CivData.EMERALD_BLOCK, 1);
									} else {
										CivMessage.global("Trommel FAILED!? Test#ID: "+theOre);
									}
								}
							}
						}
						newItem = thatItem;
					} else if (rand1 < ((int)((trommel.getGravelChance(Mineral.EMERALD)+mod)*randMax))) {
						newItem = ItemManager.createItemStack(CivData.EMERALD, 1);
					} else if (rand1 < ((int)((trommel.getGravelChance(Mineral.DIAMOND)+mod)*randMax))) {
						newItem = ItemManager.createItemStack(CivData.DIAMOND, 1);
					} else if (rand1 < ((int)((trommel.getGravelChance(Mineral.REDSTONE)+mod)*randMax))) {
						int red = rand.nextInt(4);
						if (red < 2) red = 2;
						newItem = ItemManager.createItemStack(CivData.REDSTONE_DUST, red);
					} else if (rand1 < ((int)((trommel.getGravelChance(Mineral.GOLD)+mod)*randMax))) {
						newItem = ItemManager.createItemStack(CivData.GOLD_INGOT, 1);
					} else if (rand1 < ((int)((trommel.getGravelChance(Mineral.IRON)+mod)*randMax))) {
						newItem = ItemManager.createItemStack(CivData.IRON_INGOT, 1);
					} else {
						int factor = rand.nextInt(5);
						if (factor == 1) {
							newItem = ItemManager.createItemStack(CivData.DIRT, 1);
						} else if (factor == 2) {
							newItem = ItemManager.createItemStack(CivData.GRAVEL, 1);
						} else {
							newItem = ItemManager.createItemStack(CivData.AIR, 1);
						}
					}
					try {
						debug(trommel, "Updating inventory:"+newItem);
						this.updateInventory(Action.ADD, dest_inv, newItem);
					} catch (InterruptedException e) {
						return;
					}
					break;
				}
				
				
				if (ItemManager.getId(stack) == CivData.STONE) {
					if (ItemManager.getData(stack) == ItemManager.getData(ItemManager.getMaterialData(CivData.STONE, CivData.DATA_0))) {
						try {
							this.updateInventory(Action.REMOVE, source_inv, ItemManager.createItemStack(CivData.STONE, 1, (short) CivData.DATA_0));
						} catch (InterruptedException e) {
							return;
						}
						Random rand = new Random();
						int randMax = Trommel.STONE_MAX_RATE;
						int rand1 = rand.nextInt(randMax);
						ItemStack newItem;
						if (rand1 < ((int)((trommel.getStoneChance(Mineral.EMERALD)+mod)*randMax))) {
							newItem = ItemManager.createItemStack(CivData.EMERALD, 1);
						} else if (rand1 < ((int)((trommel.getStoneChance(Mineral.DIAMOND)+mod)*randMax))) {
							newItem = ItemManager.createItemStack(CivData.DIAMOND, 1);
						} else if (rand1 < ((int)((trommel.getStoneChance(Mineral.REDSTONE)+mod)*randMax))) {
							int red = rand.nextInt(4);
							if (red < 2) red = 2;
							newItem = ItemManager.createItemStack(CivData.REDSTONE_DUST, red);
						} else if (rand1 < ((int)((trommel.getStoneChance(Mineral.GOLD)+mod)*randMax))) {
							newItem = ItemManager.createItemStack(CivData.GOLD_INGOT, 1);
						} else if (rand1 < ((int)((trommel.getStoneChance(Mineral.IRON)+mod)*randMax))) {
							newItem = ItemManager.createItemStack(CivData.IRON_INGOT, 1);
						} else {
							int factor = rand.nextInt(5);
							if (factor == 1) {
								newItem = ItemManager.createItemStack(CivData.DIRT, 1);
							} else if (factor == 2) {
								newItem = ItemManager.createItemStack(CivData.GRAVEL, 1);
							} else {
								newItem = ItemManager.createItemStack(CivData.AIR, 1);
							}
						}
						try {
							debug(trommel, "Updating inventory:"+newItem);
							this.updateInventory(Action.ADD, dest_inv, newItem);
						} catch (InterruptedException e) {
							return;
						}
						break;
					}
					
					
					if (ItemManager.getData(stack) == ItemManager.getData(ItemManager.getMaterialData(CivData.STONE, CivData.GRANITE))) {
						try {
							this.updateInventory(Action.REMOVE, source_inv, ItemManager.createItemStack(CivData.STONE, 1, (short) CivData.GRANITE));
						} catch (InterruptedException e) {
							return;
						}
						Random rand = new Random();
						int randMax = Trommel.GRANITE_MAX_RATE;
						int rand1 = rand.nextInt(randMax);
						ItemStack newItem;
						if (rand1 < ((int)((trommel.getGraniteChance(Mineral.EMERALD)+mod)*randMax))) {
							newItem = ItemManager.createItemStack(CivData.EMERALD, 1);
						} else if (rand1 < ((int)((trommel.getGraniteChance(Mineral.DIAMOND)+mod)*randMax))) {
							newItem = ItemManager.createItemStack(CivData.DIAMOND, 1);
						} else if (rand1 < ((int)((trommel.getGraniteChance(Mineral.REDSTONE)+mod)*randMax))) {
							int red = rand.nextInt(4);
							if (red < 2) red = 2;
							newItem = ItemManager.createItemStack(CivData.REDSTONE_DUST, red);
						} else if (rand1 < ((int)((trommel.getGraniteChance(Mineral.GOLD)+mod)*randMax))) {
							newItem = ItemManager.createItemStack(CivData.GOLD_INGOT, 1);
						} else if (rand1 < ((int)((trommel.getGraniteChance(Mineral.IRON)+mod)*randMax))) {
							newItem = ItemManager.createItemStack(CivData.IRON_INGOT, 1);
						} else {
							int factor = rand.nextInt(5);
							if (factor == 1) {
								newItem = ItemManager.createItemStack(CivData.DIRT, 1);
							} else if (factor == 2) {
								newItem = ItemManager.createItemStack(CivData.GRAVEL, 1);
							} else {
								newItem = ItemManager.createItemStack(CivData.AIR, 1);
							}
						}
						try {
							debug(trommel, "Updating inventory:"+newItem);
							this.updateInventory(Action.ADD, dest_inv, newItem);
						} catch (InterruptedException e) {
							return;
						}
						break;
					}
					
					
					if (ItemManager.getData(stack) == ItemManager.getData(ItemManager.getMaterialData(CivData.STONE, CivData.DIORITE))) {
						try {
							this.updateInventory(Action.REMOVE, source_inv, ItemManager.createItemStack(CivData.STONE, 1, (short) CivData.DIORITE));
						} catch (InterruptedException e) {
							return;
						}
						Random rand = new Random();
						int randMax = Trommel.DIORITE_MAX_RATE;
						int rand1 = rand.nextInt(randMax);
						ItemStack newItem;
						if (rand1 < ((int)((trommel.getDioriteChance(Mineral.EMERALD)+mod)*randMax))) {
							newItem = ItemManager.createItemStack(CivData.EMERALD, 1);
						} else if (rand1 < ((int)((trommel.getDioriteChance(Mineral.DIAMOND)+mod)*randMax))) {
							newItem = ItemManager.createItemStack(CivData.DIAMOND, 1);
						} else if (rand1 < ((int)((trommel.getDioriteChance(Mineral.REDSTONE)+mod)*randMax))) {
							int red = rand.nextInt(4);
							if (red < 2) red = 2;
							newItem = ItemManager.createItemStack(CivData.REDSTONE_DUST, red);
						} else if (rand1 < ((int)((trommel.getDioriteChance(Mineral.GOLD)+mod)*randMax))) {
							newItem = ItemManager.createItemStack(CivData.GOLD_INGOT, 1);
						} else if (rand1 < ((int)((trommel.getDioriteChance(Mineral.IRON)+mod)*randMax))) {
							newItem = ItemManager.createItemStack(CivData.IRON_INGOT, 1);
						} else {
							int factor = rand.nextInt(5);
							if (factor == 1) {
								newItem = ItemManager.createItemStack(CivData.DIRT, 1);
							} else if (factor == 2) {
								newItem = ItemManager.createItemStack(CivData.GRAVEL, 1);
							} else {
								newItem = ItemManager.createItemStack(CivData.AIR, 1);
							}
						}
						try {
							debug(trommel, "Updating inventory:"+newItem);
							this.updateInventory(Action.ADD, dest_inv, newItem);
						} catch (InterruptedException e) {
							return;
						}
						break;
					}
					
					
					if (ItemManager.getData(stack) == ItemManager.getData(ItemManager.getMaterialData(CivData.STONE, CivData.ANDESITE))) {
						try {
							this.updateInventory(Action.REMOVE, source_inv, ItemManager.createItemStack(CivData.STONE, 1, (short) CivData.ANDESITE));
						} catch (InterruptedException e) {
							return;
						}
						Random rand = new Random();
						int randMax = Trommel.ANDESITE_MAX_RATE;
						int rand1 = rand.nextInt(randMax);
						ItemStack newItem;
						if (rand1 < ((int)((trommel.getAndesiteChance(Mineral.EMERALD)+mod)*randMax))) {
							newItem = ItemManager.createItemStack(CivData.EMERALD, 1);
						} else if (rand1 < ((int)((trommel.getAndesiteChance(Mineral.DIAMOND)+mod)*randMax))) {
							newItem = ItemManager.createItemStack(CivData.DIAMOND, 1);
						} else if (rand1 < ((int)((trommel.getAndesiteChance(Mineral.REDSTONE)+mod)*randMax))) {
							int red = rand.nextInt(4);
							if (red < 2) red = 2;
							newItem = ItemManager.createItemStack(CivData.REDSTONE_DUST, red);
						} else if (rand1 < ((int)((trommel.getAndesiteChance(Mineral.GOLD)+mod)*randMax))) {
							newItem = ItemManager.createItemStack(CivData.GOLD_INGOT, 1);
						} else if (rand1 < ((int)((trommel.getAndesiteChance(Mineral.IRON)+mod)*randMax))) {
							newItem = ItemManager.createItemStack(CivData.IRON_INGOT, 1);
						} else {
							int factor = rand.nextInt(5);
							if (factor == 1) {
								newItem = ItemManager.createItemStack(CivData.DIRT, 1);
							} else if (factor == 2) {
								newItem = ItemManager.createItemStack(CivData.GRAVEL, 1);
							} else {
								newItem = ItemManager.createItemStack(CivData.AIR, 1);
							}
						}
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
		}
		trommel.skippedCounter = 0;
	}
	
	@Override
	public void run() {
		if (this.trommel.lock.tryLock()) {
			try {
				try {
					processTrommelUpdate();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} finally {
				this.trommel.lock.unlock();
			}
		} else {
			debug(this.trommel, "Failed to get lock while trying to start task, aborting.");
		}
	}
}
