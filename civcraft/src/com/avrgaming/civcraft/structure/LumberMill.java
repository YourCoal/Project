/**
 * CivCraft Created by - AVRGAMING LLC
 * This Code Modified by - https://www.youtube.com/user/cpcole556
 **/
package com.avrgaming.civcraft.structure;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;

import org.bukkit.Location;

import com.avrgaming.civcraft.config.CivSettings;
import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.exception.InvalidConfiguration;
import com.avrgaming.civcraft.object.Buff;
import com.avrgaming.civcraft.object.Town;
import com.avrgaming.civcraft.util.BlockCoord;
import com.avrgaming.civcraft.util.SimpleBlock;

public class LumberMill extends Structure {
	
	public static final int MAX_CHANCE = CivSettings.getIntegerStructure("lumber_mill.max");
	private static final double LOG_CHANCE = CivSettings.getDoubleStructure("lumber_mill.log_chance"); //75%
	private static final double PLANK_CHANCE = CivSettings.getDoubleStructure("lumber_mill.plank_chance"); //15%
	private static final double SAPLING_CHANCE = CivSettings.getDoubleStructure("lumber_mill.sapling_chance"); //5%
	private static final double APPLE_CHANCE = CivSettings.getDoubleStructure("lumber_mill.apple_chance"); //0.75%
	
	private int level = 1;
	public int skippedCounter = 0;
	public ReentrantLock lock = new ReentrantLock();
	
	public enum Wood {
		LOG,
		PLANK,
		SAPLING,
		APPLE
	}
	
	protected LumberMill(Location center, String id, Town town) throws CivException {
		super(center, id, town);	
		setLevel(town.saved_lumber_mill_level);
	}
	
	public LumberMill(ResultSet rs) throws SQLException, CivException {
		super(rs);
	}
	
	@Override
	public String getDynmapDescription() {
		String out = "<u><b>"+this.getDisplayName()+"</u></b><br/>";
		out += "Level: "+this.level;
		return out;
	}
	
	@Override
	public String getMarkerIconName() {
		return "tree";
	}
	
	public double getChance(Wood wood) {
		double chance = 0;
		switch (wood) {
		case LOG:
			chance = LOG_CHANCE;
			break;
		case PLANK:
			chance = PLANK_CHANCE;
			break;
		case SAPLING:
			chance = SAPLING_CHANCE;
			break;
		case APPLE:
			chance = APPLE_CHANCE;
			break;
		}
		return this.modifyChance(chance);
	}
	
	private double modifyChance(Double chance) {
		double increase = chance*this.getTown().getBuffManager().getEffectiveDouble(Buff.EXTRACTION);
		chance += increase;
		try {
			if (this.getTown().getGovernment().id.equals("gov_despotism")) {
				chance *= CivSettings.getDouble(CivSettings.structureConfig, "quarry.despotism_rate");
			} else if (this.getTown().getGovernment().id.equals("gov_theocracy") || this.getTown().getGovernment().id.equals("gov_monarchy")){
				chance *= CivSettings.getDouble(CivSettings.structureConfig, "quarry.penalty_rate");
			}
		} catch (InvalidConfiguration e) {
			e.printStackTrace();
		}
		return chance;
	}
	
	@Override
	public void onPostBuild(BlockCoord absCoord, SimpleBlock commandBlock) {
		this.level = getTown().saved_lumber_mill_level;
	}
	
	public int getLevel() {
		return level;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
}
