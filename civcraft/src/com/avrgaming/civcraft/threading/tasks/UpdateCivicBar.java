package com.avrgaming.civcraft.threading.tasks;

import java.util.LinkedList;
import java.util.Queue;

import com.avrgaming.civcraft.main.CivData;
import com.avrgaming.civcraft.object.Town;
import com.avrgaming.civcraft.structure.TownHall;
import com.avrgaming.civcraft.threading.CivAsyncTask;
import com.avrgaming.civcraft.util.BlockCoord;
import com.avrgaming.civcraft.util.SimpleBlock;
import com.avrgaming.civcraft.util.SimpleBlock.Type;

public class UpdateCivicBar extends CivAsyncTask {
	
	private Town town;
	
	public UpdateCivicBar(Town town) {
		this.town = town;
	}
	
	@Override
	public void run() {
		Queue<SimpleBlock> sbs = new LinkedList<SimpleBlock>();
		double percentageDone = 0.0;
		TownHall townhall = town.getTownHall();
		if (townhall == null) {
			return;
		}
		
		if (!townhall.isActive()) {
			return;
		}
		
		SimpleBlock sb;
		if (townhall.getCivicnameSign() != null) {
			BlockCoord bcoord = townhall.getCivicnameSign();
			sb = new SimpleBlock(CivData.WALL_SIGN, townhall.getCivicnameSignData());
			sb.x = bcoord.getX(); sb.y = bcoord.getY(); sb.z = bcoord.getZ();
			sb.worldname = bcoord.getWorldname();
			sb.specialType = Type.LITERAL;			
			if (town.getCivicResearch() != null) {			
				sb.message[0] = "Researching";
				sb.message[1] = "Civic:";
				sb.message[2] = "";
				sb.message[3] = town.getCivicResearch().name;
			} else {
				sb.message[0] = "Researching";
				sb.message[1] = "Civic:";
				sb.message[2] = "";
				sb.message[3] = "Nothing";			
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
			if (town.getCivicResearch() != null) {
				percentageDone = Math.round(percentageDone*100);
				sb.message[0] = "Percent";
				sb.message[1] = "Complete";
				sb.message[2] = ""+percentageDone+"%";
				sb.message[3] = "";
				} else {
				sb.message[0] = "Use";
				sb.message[1] = "/town civic";
				sb.message[2] = "to start";
				sb.message[3] = "";				
			}
			sbs.add(sb);
			townhall.addStructureBlock(townhall.getCivicdataSign(), false);
		}
		this.updateBlocksQueue(sbs);
	}
}
