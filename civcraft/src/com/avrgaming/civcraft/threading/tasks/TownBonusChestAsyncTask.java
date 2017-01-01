package com.avrgaming.civcraft.threading.tasks;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.avrgaming.civcraft.exception.CivTaskAbortException;
import com.avrgaming.civcraft.lorestorage.LoreMaterial;
import com.avrgaming.civcraft.main.CivData;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.main.CivLog;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.StructureChest;
import com.avrgaming.civcraft.object.Town;
import com.avrgaming.civcraft.structure.Structure;
import com.avrgaming.civcraft.structure.TownHall;
import com.avrgaming.civcraft.structure.TownHall.THBonus;
import com.avrgaming.civcraft.threading.CivAsyncTask;
import com.avrgaming.civcraft.threading.sync.request.UpdateInventoryRequest.Action;
import com.avrgaming.civcraft.util.CivColor;
import com.avrgaming.civcraft.util.ItemManager;
import com.avrgaming.civcraft.util.MultiInventory;

public class TownBonusChestAsyncTask extends CivAsyncTask {
	
	TownHall townhall;
	public static HashSet<String> debugTowns = new HashSet<String>();

	public static void debug(TownHall townhall, String msg) {
		if (debugTowns.contains(townhall.getTown().getName())) {
			CivLog.warning("TownHall-Bonus-Debug:"+townhall.getTown().getName()+":"+msg);
		}
	}	
	
	public TownBonusChestAsyncTask(Structure townhall) {
		this.townhall = (TownHall)townhall;
	}
	
	public void processTownHallBonusUpdate() {
		if (!townhall.isActive()) {
			debug(townhall, "TownHall-Bonus- inactive...");
			return;
		}
		
		debug(townhall, "Processing TownHall-Bonus-...");
		ArrayList<StructureChest> destinations = townhall.getAllChestsById(99);
		if (destinations.size() != 2) {
			CivLog.error("Bad chests for TownHall-Bonus- in town:"+townhall.getTown().getName()+" dests:"+destinations.size());
			return;
		}
		
		MultiInventory dest_inv = new MultiInventory();
		try {
			boolean full = true;
			for (StructureChest dst : destinations) {
				Inventory tmp;
				try {
					tmp = this.getChestInventory(dst.getCoord().getWorldname(), dst.getCoord().getX(), dst.getCoord().getY(), dst.getCoord().getZ(), false);
				} catch (CivTaskAbortException e) {
					CivLog.warning("TownHall:"+e.getMessage());
					return;
				}
				
				if (tmp == null) {
					townhall.skippedCounter++;
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
				CivMessage.sendTown(townhall.getTown(), CivColor.Rose+"The town bonus chest is full! "+CivColor.BOLD+"YOU GOT ZERO ITEMS THIS TICK D:");
				return;
			}
		} catch (InterruptedException e) {
			return;
		}
		
		debug(townhall, "Processing TownHall-Bonus-:"+townhall.skippedCounter+1);
		for (int i = 0; i < townhall.skippedCounter+1;) {
			Random rand = new Random();
			int randMax = TownHall.BONUS_MAX;
			int rand1 = rand.nextInt(randMax);
			ItemStack newItem;
			if (rand1 < ((int)((townhall.getTHBonus(THBonus.T4PACKAGE))*randMax))) {
				int factor = rand.nextInt(3);
				if (factor == 1) {
					newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:t4_mine_package"), 1);
					CivMessage.sendTown(townhall.getTown(), CivColor.LightGreen+"The town generated an extra "
							+CivColor.Yellow+CivColor.BOLD+"1 T4 Mine Package"+CivColor.LightGreen+". You can find them in the chest at your Town Hall.");
				} else if (factor == 2) {
					newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:t4_lab_package"), 1);
					CivMessage.sendTown(townhall.getTown(), CivColor.LightGreen+"The town generated an extra "
							+CivColor.Yellow+CivColor.BOLD+"1 T4 Lab Package"+CivColor.LightGreen+". You can find them in the chest at your Town Hall.");
				} else {
					newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:t4_monument_package"), 1);
					CivMessage.sendTown(townhall.getTown(), CivColor.LightGreen+"The town generated an extra "
							+CivColor.Yellow+CivColor.BOLD+"1 T4 Monument Package"+CivColor.LightGreen+". You can find them in the chest at your Town Hall.");
				}
			} else if (rand1 < ((int)((townhall.getTHBonus(THBonus.T3PACKAGE))*randMax))) {
				int factor = rand.nextInt(3);
				if (factor == 1) {
					newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:t3_mine_package"), 1);
					CivMessage.sendTown(townhall.getTown(), CivColor.LightGreen+"The town generated an extra "
							+CivColor.Yellow+CivColor.BOLD+"1 T3 Mine Package"+CivColor.LightGreen+". You can find them in the chest at your Town Hall.");
				} else if (factor == 2) {
					newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:t3_lab_package"), 1);
					CivMessage.sendTown(townhall.getTown(), CivColor.LightGreen+"The town generated an extra "
							+CivColor.Yellow+CivColor.BOLD+"1 T3 Lab Package"+CivColor.LightGreen+". You can find them in the chest at your Town Hall.");
				} else {
					newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:t3_monument_package"), 1);
					CivMessage.sendTown(townhall.getTown(), CivColor.LightGreen+"The town generated an extra "
							+CivColor.Yellow+CivColor.BOLD+"1 T3 Monument Package"+CivColor.LightGreen+". You can find them in the chest at your Town Hall.");
				}
			} else if (rand1 < ((int)((townhall.getTHBonus(THBonus.T2PACKAGE))*randMax))) {
				int factor = rand.nextInt(3);
				if (factor == 1) {
					newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:t2_mine_package"), 1);
					CivMessage.sendTown(townhall.getTown(), CivColor.LightGreen+"The town generated an extra "
							+CivColor.Yellow+CivColor.BOLD+"1 T2 Mine Package"+CivColor.LightGreen+". You can find them in the chest at your Town Hall.");
				} else if (factor == 2) {
					newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:t2_lab_package"), 1);
					CivMessage.sendTown(townhall.getTown(), CivColor.LightGreen+"The town generated an extra "
							+CivColor.Yellow+CivColor.BOLD+"1 T2 Lab Package"+CivColor.LightGreen+". You can find them in the chest at your Town Hall.");
				} else {
					newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:t2_monument_package"), 1);
					CivMessage.sendTown(townhall.getTown(), CivColor.LightGreen+"The town generated an extra "
							+CivColor.Yellow+CivColor.BOLD+"1 T2 Monument Package"+CivColor.LightGreen+". You can find them in the chest at your Town Hall.");
				}
			} else if (rand1 < ((int)((townhall.getTHBonus(THBonus.T1PACKAGE))*randMax))) {
				int factor = rand.nextInt(3);
				if (factor == 1) {
					newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:t1_mine_package"), 1);
					CivMessage.sendTown(townhall.getTown(), CivColor.LightGreen+"The town generated an extra "
							+CivColor.Yellow+CivColor.BOLD+"1 T1 Mine Package"+CivColor.LightGreen+". You can find them in the chest at your Town Hall.");
				} else if (factor == 2) {
					newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:t1_lab_package"), 1);
					CivMessage.sendTown(townhall.getTown(), CivColor.LightGreen+"The town generated an extra "
							+CivColor.Yellow+CivColor.BOLD+"1 T1 Lab Package"+CivColor.LightGreen+". You can find them in the chest at your Town Hall.");
				} else {
					newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:t1_monument_package"), 1);
					CivMessage.sendTown(townhall.getTown(), CivColor.LightGreen+"The town generated an extra "
							+CivColor.Yellow+CivColor.BOLD+"1 T1 Monument Package"+CivColor.LightGreen+". You can find them in the chest at your Town Hall.");
				}
			} else if (rand1 < ((int)((townhall.getTHBonus(THBonus.BEAKER))*randMax))) {
				newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:base_beaker"), 16);
				CivMessage.sendTown(townhall.getTown(), CivColor.LightGreen+"The town generated an extra "
						+CivColor.Yellow+CivColor.BOLD+"16 Beakers"+CivColor.LightGreen+". You can find them in the chest at your Town Hall.");
			} else if (rand1 < ((int)((townhall.getTHBonus(THBonus.HAMMER))*randMax))) {
				newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:base_hammer"), 16);
				CivMessage.sendTown(townhall.getTown(), CivColor.LightGreen+"The town generated an extra "
						+CivColor.Yellow+CivColor.BOLD+"16 Hammers"+CivColor.LightGreen+". You can find them in the chest at your Town Hall.");
			} else {
//				CivMessage.sendTown(townhall.getTown(), CivColor.LightGreen+"The town generated nothing extra in its chest.");
				newItem = ItemManager.createItemStack(CivData.AIR, 1);
			} try {
				debug(townhall, "Updating inventory:"+newItem);
				this.updateInventory(Action.ADD, dest_inv, newItem);
			} catch (InterruptedException e) {
				return;
			}
			break;
		}
		townhall.skippedCounter = 0;
	}
	
	@Override
	public void run() {
		if (this.townhall.lock.tryLock()) {
			try {
				try {
					for (Town town : CivGlobal.getTowns()) {
						processTownHallBonusUpdate();
						processTownHallBonusUpdate();
						
						Random rand = new Random();
						int extra_chanceA = rand.nextInt(1 + 25);
						int extra_chanceB = rand.nextInt(1 + 25);
						if (extra_chanceA == extra_chanceB) {
							CivMessage.global("All towns are getting extra items this hourly tick!");
							processTownHallBonusUpdate();
						}
						
						if (town.getLevel() >= 4) {
							CivMessage.sendTown(townhall.getTown(), CivColor.LightGray+"Your town gets extra items for its level!");
							processTownHallBonusUpdate();
						}
						if (town.getLevel() >= 8) {
							CivMessage.sendTown(townhall.getTown(), CivColor.LightGray+"Your town gets extra items for its level!");
							processTownHallBonusUpdate();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} finally {
				this.townhall.lock.unlock();
			}
		} else {
			debug(this.townhall, "Failed to get lock while trying to start task, aborting.");
		}
	}
}