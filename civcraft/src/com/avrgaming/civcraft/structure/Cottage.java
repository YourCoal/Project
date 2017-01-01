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
import java.util.ArrayList;

import org.bukkit.Location;

import com.avrgaming.civcraft.config.CivSettings;
import com.avrgaming.civcraft.config.ConfigCottageLevel;
import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.exception.InvalidConfiguration;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.Buff;
import com.avrgaming.civcraft.object.Town;
import com.avrgaming.civcraft.sessiondb.SessionEntry;
import com.avrgaming.civcraft.threading.CivAsyncTask;
import com.avrgaming.civcraft.util.CivColor;

public class Cottage extends Structure {
	
	protected Cottage(Location center, String id, Town town) throws CivException {
		super(center, id, town);
	}
	
	public Cottage(ResultSet rs) throws SQLException, CivException {
		super(rs);
	}
	
	@Override
	public void loadSettings() {
		super.loadSettings();
	}
	
	@Override
	public String getDynmapDescription() {
		String out = "<u><b>"+this.getDisplayName()+"</u></b><br/>";
		return out;
	}
	
	@Override
	public String getMarkerIconName() {
		return "house";
	}
	
	public String getkey() {
		return this.getTown().getName()+"_"+this.getConfigId()+"_"+this.getCorner().toString(); 
	}
	
	/* Returns true if the granary has been poisoned, false otherwise. */
	public boolean processPoison() {
		//Check to make sure the granary has not been poisoned!
		String key = "posiongranary:"+getTown().getName();
		ArrayList<SessionEntry> entries;
		entries = CivGlobal.getSessionDB().lookup(key);
		int max_poison_ticks = -1;
		for (SessionEntry entry : entries) {
			int next = Integer.valueOf(entry.value);
			if (next > max_poison_ticks) {
				max_poison_ticks = next;
			} 
		}
		
		if (max_poison_ticks > 0) {
			CivGlobal.getSessionDB().delete_all(key);
			max_poison_ticks--;
			
			if (max_poison_ticks > 0)
				CivGlobal.getSessionDB().add(key, ""+max_poison_ticks, this.getTown().getCiv().getId(), this.getTown().getId(), this.getId());
	
			// Add some rotten flesh to the chest lol
			CivMessage.sendTown(this.getTown(), CivColor.Rose+"Our granaries have been poisoned!!");
			return true;
		}
		return false;
	}
	
	public void generateCoins(CivAsyncTask task) {
		if (!this.isActive()) {
			return;
		}	
		
		if (processPoison()) {
			return;
		}
		
		Town town = this.getTown();
		double townFood = town.getFoodCount();
		
		int lvls = getLevelFoodCount() + town.getLevel() + town.getCultureLevel();
		int foodCost = 5 + lvls; //Always at least 5 food.
		
		if (this.getTown().getBuffManager().hasBuff(Buff.REDUCE_CONSUME_PRODUCTION)) {
			foodCost *= this.getTown().getBuffManager().getEffectiveInt(Buff.REDUCE_CONSUME_PRODUCTION);
		}
		if (this.getTown().getBuffManager().hasBuff("buff_pyramid_cottage_consume")) {
			foodCost *= this.getTown().getBuffManager().getEffectiveDouble("buff_pyramid_cottage_consume");
		}
		
		/* Bail early for results that do not generate coins. */
		if (townFood < foodCost) {
			CivMessage.sendTownCottage(town, "Your town does not have the food ("+foodCost+") in order to supply the people! "
					+CivColor.Rose+CivColor.ITALIC+"Cottage generated no money.");
		} else {
			if (townFood >= foodCost) {
				/* Calculate how much money we made. */	
				int total_coins = (int)Math.round(getGeneratedCoins()*this.getTown().getCottageRate());
				if (this.getTown().getBuffManager().hasBuff("buff_pyramid_cottage_bonus")) {
					total_coins *= this.getTown().getBuffManager().getEffectiveDouble("buff_pyramid_cottage_bonus");
				}
				
				if (this.getCiv().hasRequiredTech("tech_taxation")) {
					double taxation_bonus;
					try {
						taxation_bonus = CivSettings.getDouble(CivSettings.techsConfig, "taxation_cottage_buff");
						total_coins *= taxation_bonus;
					} catch (InvalidConfiguration e) {
						e.printStackTrace();
					}
				}
				double taxesPaid = Math.round(total_coins*this.getTown().getDepositCiv().getIncomeTaxRate());
				this.getTown().getTreasury().deposit(total_coins - taxesPaid);
				this.getTown().getDepositCiv().taxPayment(this.getTown(), taxesPaid);
				this.getTown().takeFood(foodCost);
				CivMessage.sendTownCottage(town, "Your cottage has eaten "+foodCost+" food. Generated a total of "+total_coins+" coins. "+CivColor.Gold+CivColor.ITALIC+"(Paid "+taxesPaid+" in taxes)");
			}
		}
	}
	
	public int getLevelFoodCount() {
		if (!this.isComplete()) {
			return 0;
		}
		ConfigCottageLevel lvl = CivSettings.cottageLevels.get(this.getTown().saved_cottage_level);
		return lvl.amount;
	}
	
	public double getGeneratedCoins() {
		if (!this.isComplete()) {
			return 0.0;
		}
		ConfigCottageLevel lvl = CivSettings.cottageLevels.get(this.getTown().saved_cottage_level);
		return lvl.coins;	
	}
	
	@Override
	public void delete() throws SQLException {
		super.delete();
	}
	
	public void onDestroy() {
		super.onDestroy();
	}
}
