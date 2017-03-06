/*************************************************************************
 * 
 * AVRGAMING LLC
 * __________________
 * 
 *  [2013] AVRGAMING LLC
 *  All Rights Reserved.
 * 
 * NOTICE:  All information contained herein is, and remains
 * the property of AVRGAMING LLC and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to AVRGAMING LLC
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from AVRGAMING LLC.
 */
package com.avrgaming.civcraft.structure;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.inventory.Inventory;

import com.avrgaming.civcraft.components.ConsumeLevelComponent;
import com.avrgaming.civcraft.components.ConsumeLevelComponent.Result;
import com.avrgaming.civcraft.config.CivSettings;
import com.avrgaming.civcraft.config.ConfigMonumentLevel;
import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.exception.CivTaskAbortException;
import com.avrgaming.civcraft.exception.InvalidConfiguration;
import com.avrgaming.civcraft.main.CivData;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.StructureChest;
import com.avrgaming.civcraft.object.Town;
import com.avrgaming.civcraft.sessiondb.SessionEntry;
import com.avrgaming.civcraft.threading.CivAsyncTask;
import com.avrgaming.civcraft.util.CivColor;
import com.avrgaming.civcraft.util.ItemManager;
import com.avrgaming.civcraft.util.MultiInventory;

public class Monument extends Structure {
	
	private ConsumeLevelComponent consumeComp = null;
	
	protected Monument(Location center, String id, Town town) throws CivException {
		super(center, id, town);
	}
	
	public Monument(ResultSet rs) throws SQLException, CivException {
		super(rs);
	}
	
	public ConsumeLevelComponent getConsumeComponent() {
		if (consumeComp == null) {
			consumeComp = (ConsumeLevelComponent) this.getComponent(ConsumeLevelComponent.class.getSimpleName());
		}
		return consumeComp;
	}
	
	@Override
	public void loadSettings() {
		super.loadSettings();
	}
	
	@Override
	public String getDynmapDescription() {
		String out = "<u><b>Monument</u></b><br/>";
		if (getConsumeComponent() == null) {
			out += "Level: Null";
		} else {
			out += "Level: "+getConsumeComponent().getLevel()+" "+getConsumeComponent().getCountString();
		}
		return out;
	}
	
	@Override
	public String getMarkerIconName() {
		return "building";
	}
	
	public String getkey() {
		return this.getTown().getName()+"_"+this.getConfigId()+"_"+this.getCorner().toString(); 
	}

	/* Returns true if the monument has been poisoned, false otherwise. */
	public boolean processFatigue(MultiInventory inv) {
		//Check to make sure the granary has not been poisoned!
		String key = "fatiguemonument:"+getTown().getName();
		ArrayList<SessionEntry> entries;
		entries = CivGlobal.getSessionDB().lookup(key);
		int max_fatigue_ticks = -1;
		for (SessionEntry entry : entries) {
			int next = Integer.valueOf(entry.value);
			
			if (next > max_fatigue_ticks) {
				max_fatigue_ticks = next;
			} 
		}
		
		if (max_fatigue_ticks > 0) {
			CivGlobal.getSessionDB().delete_all(key);
			max_fatigue_ticks--;
			
			if (max_fatigue_ticks > 0)
				CivGlobal.getSessionDB().add(key, ""+max_fatigue_ticks, this.getTown().getCiv().getId(), this.getTown().getId(), this.getId());
	
			// Add some rotten flesh to the chest lol
			CivMessage.sendTown(this.getTown(), CivColor.Rose+"Our monuments have been fatiged!!");
			inv.addItem(ItemManager.createItemStack(CivData.ROTTEN_FLESH, 4));
			return true;
		}
		return false;
	}
	
	public void generateTownCulture(CivAsyncTask task) {
		if (!this.isActive()) {
			return;
		}
		
		/* Build a multi-inv from monuments. */
		MultiInventory multiInv = new MultiInventory();
		for (Structure struct : this.getTown().getStructures()) {
			if (struct instanceof Monument) {
				ArrayList<StructureChest> chests = struct.getAllChestsById(1);
				// Make sure the chunk is loaded and add it to the inventory.
				try {
					for (StructureChest c : chests) {
						task.syncLoadChunk(c.getCoord().getWorldname(), c.getCoord().getX(), c.getCoord().getZ());
						Inventory tmp;
						try {
							tmp = task.getChestInventory(c.getCoord().getWorldname(), c.getCoord().getX(), c.getCoord().getY(), c.getCoord().getZ(), true);
							multiInv.addInventory(tmp);
						} catch (CivTaskAbortException e) {
							e.printStackTrace();
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		getConsumeComponent().setSource(multiInv);
		
		double monument_consume_mod = 1.0; //allows buildings and govs to change the totals for monument consumption
		
		//TODO fix this
//		if (this.getTown().getBuffManager().hasBuff(Buff.REDUCE_CONSUME_PRODUCTION)) {
//			monument_consume_mod *= this.getTown().getBuffManager().getEffectiveDouble(Buff.REDUCE_CONSUME_PRODUCTION);
//		}
		
		//TODO make a new buff that works for monuments/monuments
//		if (this.getTown().getBuffManager().hasBuff("buff_pyramid_cottage_consume")) {
//			monument_consume_mod *= this.getTown().getBuffManager().getEffectiveDouble("buff_pyramid_cottage_consume");
//		}
		
		//TODO make a new buff that works for monuments/monuments
//		if (this.getTown().getBuffManager().hasBuff(Buff.FISHING)) {
//			int breadPerFish = this.getTown().getBuffManager().getEffectiveInt(Buff.FISHING);
//			getConsumeComponent().addEquivExchange(CivData.BREAD, CivData.FISH_RAW, breadPerFish);
//		}
		
		getConsumeComponent().setConsumeRate(monument_consume_mod);
		Result result = getConsumeComponent().processConsumption();
		getConsumeComponent().onSave();
		getConsumeComponent().clearEquivExchanges();
		
		/* Bail early for results that do not generate coins. */
		switch (result) {
		case STARVE:
			CivMessage.sendTown(getTown(), CivColor.LightGreen+"A level "+getConsumeComponent().getLevel()+" monument "+CivColor.Rose+"starved"+
					getConsumeComponent().getCountString()+CivColor.LightGreen+" and generated no culture.");
			return;
		case LEVELDOWN:
			CivMessage.sendTown(getTown(), CivColor.LightGreen+"A level "+(getConsumeComponent().getLevel()+1)+" monument "+CivColor.Red+"leveled-down"+
					CivColor.LightGreen+" and generated no culture.");
			return;
		case STAGNATE:
			CivMessage.sendTown(getTown(), CivColor.LightGreen+"A level "+getConsumeComponent().getLevel()+" monument "+CivColor.Yellow+"stagnated"+
					getConsumeComponent().getCountString()+CivColor.LightGreen+" and generated no culture.");
			return;
		case UNKNOWN:
			CivMessage.sendTown(getTown(), CivColor.LightGreen+CivColor.LightGreen+"Something "+CivColor.DarkPurple+" UnKnOwN "+CivColor.LightGreen+" happened to a monument. It generates no culture.");
			return;
		default:
			break;
		}
		
		if (processFatigue(multiInv)) {
			return;
		}
		
		/* Calculate how much money we made. */
		/* leveling down doesnt generate coins, so we don't have to check it here. */
		ConfigMonumentLevel lvl = null;
		if (result == Result.LEVELUP) {
			lvl = CivSettings.monumentLevels.get(getConsumeComponent().getLevel()-1);	
		} else {
			lvl = CivSettings.monumentLevels.get(getConsumeComponent().getLevel());
		}
		
		double total_culture = lvl.culture*this.getTown().getMonumentRate();
		//TODO make a new buff that works for monuments/monuments
//		if (this.getTown().getBuffManager().hasBuff("buff_pyramid_cottage_bonus")) {
//			total_culture *= this.getTown().getBuffManager().getEffectiveDouble("buff_pyramid_cottage_bonus");
//		}
		
//		total_culture *= this.getTown().getBuffManager().getEffectiveDouble(Buff.ADVANCED_TOURING);
		if (this.getCiv().hasTech("tech_feudalism")) {
			double tech_bonus;
			try {
				tech_bonus = CivSettings.getDouble(CivSettings.techsConfig, "feudalism_monument_buff");
				total_culture *= tech_bonus;
			} catch (InvalidConfiguration e) {
				e.printStackTrace();
			}
		}
		
		String stateMessage = "";
		switch (result) {
		case GROW:
			stateMessage = CivColor.Green+"grew"+getConsumeComponent().getCountString()+CivColor.LightGreen;
			break;
		case LEVELUP:
			stateMessage = CivColor.Green+"leveled up"+CivColor.LightGreen;
			break;
		case MAXED:
			stateMessage = CivColor.Green+"is maxed"+getConsumeComponent().getCountString()+CivColor.LightGreen;
			break;
		default:
			break;
		}
		DecimalFormat df = new DecimalFormat("#.0");
		String newTotal = df.format(total_culture);
		double addGenerated = Double.parseDouble(newTotal);
		this.getTown().addAccumulatedCulture(addGenerated);
		CivMessage.sendTown(this.getTown(), CivColor.LightGreen+"A level "+getConsumeComponent().getLevel()+" monument "+stateMessage+" and generated "+addGenerated+" culture!");
	}
	
	public int getLevel() {
		return getConsumeComponent().getLevel();
	}
	
	public int getCount() {
		return getConsumeComponent().getCount();
	}
	
	public Result getLastResult() {
		return getConsumeComponent().getLastResult();
	}
	
	public int getMaxCount() {
		int level = getLevel();
		ConfigMonumentLevel lvl = CivSettings.monumentLevels.get(level);
		return lvl.count;
	}
	
	public double getBonusCulture() {
		if (!this.isComplete()) {
			return 0.0;
		}
		
		if (getConsumeComponent().getLevel() == 0 && getConsumeComponent().getCount() == 0) {
			return 0.0;
		}
		
		int level = getLevel(); 
		ConfigMonumentLevel lvl = CivSettings.monumentLevels.get(level);
		
		double total_culture = lvl.culture*this.getTown().getMonumentRate();
		//TODO make a new buff that works for monuments/monuments
//		if (this.getTown().getBuffManager().hasBuff("buff_pyramid_cottage_bonus")) {
//			total_culture *= this.getTown().getBuffManager().getEffectiveDouble("buff_pyramid_cottage_bonus");
//		}
		
//		total_culture *= this.getTown().getBuffManager().getEffectiveDouble(Buff.ADVANCED_TOURING);
		if (this.getCiv().hasTech("tech_feudalism")) {
			double tech_bonus;
			try {
				tech_bonus = CivSettings.getDouble(CivSettings.techsConfig, "feudalism_monument_buff");
				total_culture *= tech_bonus;
			} catch (InvalidConfiguration e) {
				e.printStackTrace();
			}
		}
		return total_culture;
	}
	
	public void delevel() {
		int currentLevel = getLevel();
		if (currentLevel > 1) {
			getConsumeComponent().setLevel(getLevel()-1);
			getConsumeComponent().setCount(0);
			getConsumeComponent().onSave();
		}
	}
	
	@Override
	public void delete() throws SQLException {
		super.delete();
		if (getConsumeComponent() != null) {
			getConsumeComponent().onDelete();
		}
	}
	
	public void onDestroy() {
		super.onDestroy();
		getConsumeComponent().setLevel(1);
		getConsumeComponent().setCount(0);
		getConsumeComponent().onSave();
	}
}