package com.avrgaming.civcraft.structure;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;

import org.bukkit.Location;

import com.avrgaming.civcraft.config.CivSettings;
import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.object.Buff;
import com.avrgaming.civcraft.object.Town;
import com.avrgaming.civcraft.util.BlockCoord;
import com.avrgaming.civcraft.util.SimpleBlock;

public class Quarry extends Structure {
	
	//Custom Items
	public static final int HAMMERS_MAX = CivSettings.getIntegerStructure("quarry.hammers.max");
	private static final double STONE_RATE = CivSettings.getDoubleStructure("quarry.hammers.stone_rate");
	private static final double GDA_RATE = CivSettings.getDoubleStructure("quarry.hammers.gda_rate"); //Granite, Diorite, Andesite
	
	public static final int BEAKERS_MAX = CivSettings.getIntegerStructure("quarry.beakers.max");
	private static final double WARTBLOCK_RATE = CivSettings.getDoubleStructure("quarry.beakers.wartblock_rate");
	private static final double BRM_RATE = CivSettings.getDoubleStructure("quarry.beakers.brm_rate"); //Nether Brick, Red Nether Brick, Magma Block
	
	
	//Minecraft Items
	public static final int WP_MAX = CivSettings.getIntegerStructure("quarry.wp.max");
	private static final double WP_COAL_RATE = CivSettings.getDoubleStructure("quarry.wp.coal_rate");
	private static final double WP_IRON_RATE = CivSettings.getDoubleStructure("quarry.wp.iron_rate");
	
	public static final int SP_MAX = CivSettings.getIntegerStructure("quarry.sp.max");
	private static final double SP_COAL_RATE = CivSettings.getDoubleStructure("quarry.sp.coal_rate");
	private static final double SP_IRON_RATE = CivSettings.getDoubleStructure("quarry.sp.iron_rate");
	private static final double SP_GOLD_RATE = CivSettings.getDoubleStructure("quarry.sp.gold_rate");
	private static final double SP_REDSTONE_RATE = CivSettings.getDoubleStructure("quarry.sp.redstone_rate");
	
	private int level = 1;
	public int skippedCounter = 0;
	public ReentrantLock lock = new ReentrantLock();
	
	public enum Block {
		EMERALD,
		DIAMOND,
		REDSTONE,
		LAPIS,
		GOLD,
		IRON,
		COAL,
		WARTBLOCK,
		STONE,
		BRM,
		GDA
		
	}
	
	protected Quarry(Location center, String id, Town town) throws CivException {
		super(center, id, town);	
		setLevel(town.saved_quarry_level);
	}
	
	public Quarry(ResultSet rs) throws SQLException, CivException {
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
		return "gear";
	}

	public double getHammerChance(Block block) {
		double chance = 0;
		switch (block) {
		case GDA:
			chance = GDA_RATE;
			break;
		case STONE:
			chance = STONE_RATE;
			break;
		default:
			break;
		}
		return this.modifyChance(chance);
	}
	
	public double getBeakerChance(Block block) {
		double chance = 0;
		switch (block) {
		case BRM:
			chance = BRM_RATE;
			break;
		case WARTBLOCK:
			chance = WARTBLOCK_RATE;
			break;
		default:
			break;
		}
		return this.modifyChance(chance);
	}
	
	public double getWPChance(Block block) {
		double chance = 0;
		switch (block) {
		case IRON:
			chance = WP_IRON_RATE;
			break;
		case COAL:
			chance = WP_COAL_RATE;
			break;
		default:
			break;
		}
		return this.modifyChance(chance);
	}
	
	public double getSPChance(Block block) {
		double chance = 0;
		switch (block) {
		case REDSTONE:
			chance = SP_REDSTONE_RATE;
			break;
		case GOLD:
			chance = SP_GOLD_RATE;
			break;
		case IRON:
			chance = SP_IRON_RATE;
			break;
		case COAL:
			chance = SP_COAL_RATE;
			break;
		default:
			break;
		}
		return this.modifyChance(chance);
	}
	
	private double modifyChance(Double chance) {
		double increase = chance*this.getTown().getBuffManager().getEffectiveDouble(Buff.EXTRACTION);
		chance += increase;
		
/*		try {
			if (this.getTown().getGovernment().id.equals("gov_despotism")) {
				chance *= CivSettings.getDouble(CivSettings.structureConfig, "quarry.despotism_rate");
			} else if (this.getTown().getGovernment().id.equals("gov_theocracy")) {
				chance *= CivSettings.getDouble(CivSettings.structureConfig, "quarry.penalty_rate");
			}
		} catch (InvalidConfiguration e) {
			e.printStackTrace();
		}*/
		return chance;
	}
	
	@Override
	public void onPostBuild(BlockCoord absCoord, SimpleBlock commandBlock) {
		this.level = getTown().saved_quarry_level;
	}
	
	public int getLevel() {
		return level;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
}
