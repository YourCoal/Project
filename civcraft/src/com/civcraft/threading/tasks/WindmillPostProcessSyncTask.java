/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.threading.tasks;

import java.util.ArrayList;
import java.util.Random;

import com.civcraft.exception.CivException;
import com.civcraft.main.CivData;
import com.civcraft.structure.Windmill;
import com.civcraft.util.BlockCoord;
import com.civcraft.util.ItemManager;
import com.civcraft.util.MultiInventory;

public class WindmillPostProcessSyncTask implements Runnable {

	ArrayList<BlockCoord> plantBlocks;
	Windmill windmill;
	int breadCount;
	int carrotCount;
	int potatoCount;
	MultiInventory source_inv;
	
	public WindmillPostProcessSyncTask(Windmill windmill, ArrayList<BlockCoord> plantBlocks,
			int breadCount, int carrotCount, int potatoCount, MultiInventory source_inv) {
		this.plantBlocks = plantBlocks;
		this.windmill = windmill;
		this.breadCount = breadCount;
		this.carrotCount = carrotCount;
		this.potatoCount = potatoCount;
		this.source_inv = source_inv;
	}
	
	@Override
	public void run() {
		Random rand = new Random();
		
		for (BlockCoord coord : plantBlocks) {
			
			int randomCropType = rand.nextInt(3);
			
			switch (randomCropType) {
			case 0:
				if (breadCount > 0) {
					/* bread seed */
					try {
						source_inv.removeItem(CivData.BREAD_SEED, 1);
					} catch (CivException e) {
						e.printStackTrace();
					}
					breadCount--;
					ItemManager.setTypeId(coord.getBlock(), CivData.WHEAT);
					ItemManager.setData(coord.getBlock(), 0, true);
					continue;
				}
			case 1:
				if (carrotCount > 0) {
					/* carrots */
					try {
						source_inv.removeItem(CivData.CARROT_ITEM, 1);
					} catch (CivException e) {
						e.printStackTrace();
					}
					carrotCount--;
					ItemManager.setTypeId(coord.getBlock(), CivData.CARROTS);
					ItemManager.setData(coord.getBlock(), 0, true);

					continue;
				}
				break;
			case 2: 
				if (potatoCount > 0) {
					/* potatoes */
					try {
						source_inv.removeItem(CivData.POTATO_ITEM, 1);
					} catch (CivException e) {
						e.printStackTrace();
					}
					potatoCount--;
					ItemManager.setTypeId(coord.getBlock(), CivData.POTATOES);
					ItemManager.setData(coord.getBlock(), 0, true);

					continue;
				}
			}	
			
			/* our randomly selected crop couldn't be placed, try them all now. */
			if (breadCount > 0) {
				/* bread seed */
				try {
					source_inv.removeItem(CivData.BREAD_SEED, 1);
				} catch (CivException e) {
					e.printStackTrace();
				}
				breadCount--;
				ItemManager.setTypeId(coord.getBlock(), CivData.WHEAT);
				ItemManager.setData(coord.getBlock(), 0, true);

				continue;
			}
			if (carrotCount > 0) {
				/* carrots */
				try {
					source_inv.removeItem(CivData.CARROT_ITEM, 1);
				} catch (CivException e) {
					e.printStackTrace();
				}
				carrotCount--;
				ItemManager.setTypeId(coord.getBlock(), CivData.CARROTS);
				ItemManager.setData(coord.getBlock(), 0, true);

				continue;
			}
			if (potatoCount > 0) {
				/* potatoes */
				try {
					source_inv.removeItem(CivData.POTATO_ITEM, 1);
				} catch (CivException e) {
					e.printStackTrace();
				}
				potatoCount--;
				ItemManager.setTypeId(coord.getBlock(), CivData.POTATOES);
				ItemManager.setData(coord.getBlock(), 0, true);
				continue;
			}
			
		}
		
	}

}
