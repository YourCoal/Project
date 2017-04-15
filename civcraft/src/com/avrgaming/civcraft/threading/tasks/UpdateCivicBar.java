package com.avrgaming.civcraft.threading.tasks;

import java.util.LinkedList;
import java.util.Queue;

import com.avrgaming.civcraft.main.CivData;
import com.avrgaming.civcraft.object.Civilization;
import com.avrgaming.civcraft.object.Town;
import com.avrgaming.civcraft.structure.TownHall;
import com.avrgaming.civcraft.threading.CivAsyncTask;
import com.avrgaming.civcraft.util.BlockCoord;
import com.avrgaming.civcraft.util.SimpleBlock;
import com.avrgaming.civcraft.util.SimpleBlock.Type;

public class UpdateCivicBar extends CivAsyncTask {
	
	private Civilization civ;
	
	public UpdateCivicBar(Civilization civ) {
		this.civ = civ;
	}
	
	@Override
	public void run() {
		Queue<SimpleBlock> sbs = new LinkedList<SimpleBlock>();
		for (Town town : civ.getTowns()) {
			double percentageDone = 0.0;
			TownHall townhall = town.getTownHall();
			if (townhall == null) {
				return;
			}
			
			if (!townhall.isActive()) {
				return;
			}
			
			SimpleBlock sb; 
			if (civ.getResearchCivic() != null) {
				percentageDone = (civ.getResearchCivicProgress() / civ.getResearchCivic().getAdjustedCultureCost(civ));
				/* Get the number of blocks to light up. */
				int size = townhall.getCivicBarSize();
				int blockCount = (int)(percentageDone*townhall.getCivicBarSize());		
				for (int i = 0; i < size; i++) {
					BlockCoord bcoord = townhall.getCivicBarBlockCoord(i);
					if (bcoord == null) { /* civic bar DNE, might not be finished yet. */
						continue;
					}
					
					if (i <= blockCount) {
						sb = new SimpleBlock(CivData.STAINED_CLAY, CivData.DATA_2);
						sb.x = bcoord.getX(); sb.y = bcoord.getY(); sb.z = bcoord.getZ();
						sb.worldname = bcoord.getWorldname();
						sbs.add(sb);
					} else {
						sb = new SimpleBlock(CivData.STAINED_CLAY, CivData.DATA_15);
						sb.x = bcoord.getX(); sb.y = bcoord.getY(); sb.z = bcoord.getZ();
						sb.worldname = bcoord.getWorldname();
						sbs.add(sb);				
					}
					townhall.addStructureBlock(townhall.getCivicBar(i), false);
				}
			} else {
				/* Resets the bar after a civic is finished. */
				int size = townhall.getCivicBarSize();
				for (int i = 0; i < size; i++) {
					BlockCoord bcoord = townhall.getCivicBarBlockCoord(i);
					if (bcoord == null) { /* civic bar DNE, might not be finished yet. */
						continue;
					}
					sb = new SimpleBlock(CivData.STAINED_CLAY, CivData.DATA_15);
					sb.x = bcoord.getX(); sb.y = bcoord.getY(); sb.z = bcoord.getZ();
					sb.worldname = bcoord.getWorldname();
					sbs.add(sb);
					townhall.addStructureBlock(townhall.getCivicBar(i), false);
				}
			}
			
			if (townhall.getCivicnameSign() != null) {
				BlockCoord bcoord = townhall.getCivicnameSign();
				sb = new SimpleBlock(CivData.WALL_SIGN, townhall.getCivicnameSignData());
				sb.x = bcoord.getX(); sb.y = bcoord.getY(); sb.z = bcoord.getZ();
				sb.worldname = bcoord.getWorldname();
				sb.specialType = Type.LITERAL;
				
				if (civ.getResearchCivic() != null) {			
					sb.message[0] = "Researching Civic";
					sb.message[1] = "";
					sb.message[2] = civ.getResearchCivic().name;
					sb.message[3] = "";
				} else {
					sb.message[0] = "Researching Civic";
					sb.message[1] = "";
					sb.message[2] = "Nothing";
					sb.message[3] = "";
				}
				sbs.add(sb);
				townhall.addStructureBlock(townhall.getCivicnameSign(), false);
			}
			
			if (townhall.getCivicdataSign() != null) {
				BlockCoord bcoord = townhall.getCivicdataSign();
				sb = new SimpleBlock(CivData.WALL_SIGN, townhall.getCivicdataSignData());
				sb.x = bcoord.getX(); sb.y = bcoord.getY(); sb.z = bcoord.getZ();
				sb.worldname = bcoord.getWorldname();
				sb.specialType = Type.LITERAL;
				if (civ.getResearchCivic() != null) {
					percentageDone = Math.round(percentageDone*100);
					sb.message[0] = "Percent";
					sb.message[1] = "Complete";
					sb.message[2] = ""+percentageDone+"%";
					sb.message[3] = "";
				} else {
					sb.message[0] = "Use";
					sb.message[1] = "/civ civic";
					sb.message[2] = "to start";
					sb.message[3] = "";				
				}
				sbs.add(sb);
				townhall.addStructureBlock(townhall.getCivicdataSign(), false);
			}
		}
		this.updateBlocksQueue(sbs);
	}
}
