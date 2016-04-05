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

@SuppressWarnings("unused")
public class Quarry extends Structure {
	
	//XXX Level 1
	public static final int BONUS1_MAX = CivSettings.getIntegerStructure("quarry_bonus1.max"); //100%
	private static final double BONUS1_STONE = CivSettings.getDoubleStructure("quarry_bonus1.stone");
	private static final double BONUS1_GRANITE_R = CivSettings.getDoubleStructure("quarry_bonus1.granite_r");
	private static final double BONUS1_GRANITE_P = CivSettings.getDoubleStructure("quarry_bonus1.granite_p");
	private static final double BONUS1_DIORITE_R = CivSettings.getDoubleStructure("quarry_bonus1.diorite_r");
	private static final double BONUS1_DIORITE_P = CivSettings.getDoubleStructure("quarry_bonus1.diorite_p");
	private static final double BONUS1_ANDESITE_R = CivSettings.getDoubleStructure("quarry_bonus1.andesite_r");
	private static final double BONUS1_ANDESITE_P = CivSettings.getDoubleStructure("quarry_bonus1.andesite_p");
	private static final double BONUS1_BASALT = CivSettings.getDoubleStructure("quarry_bonus1.basalt");
	private static final double BONUS1_CHALK = CivSettings.getDoubleStructure("quarry_bonus1.chalk");
	private static final double BONUS1_SHALE  = CivSettings.getDoubleStructure("quarry_bonus1.shale");
	private static final double BONUS1_SCHIST = CivSettings.getDoubleStructure("quarry_bonus1.schist");
	private static final double BONUS1_SLATE = CivSettings.getDoubleStructure("quarry_bonus1.slate");
	private static final double BONUS1_HAMMERPACK = CivSettings.getDoubleStructure("quarry_bonus1.hammerpack");
	
	public static final int MAX_CHANCE = CivSettings.getIntegerStructure("quarry.max");
	private static final double COBBLESTONE_RATE = CivSettings.getDoubleStructure("quarry.cobblestone_rate"); //100%
	private static final double OTHER_RATE = CivSettings.getDoubleStructure("quarry.other_rate"); //10%
	private static final double COAL_RATE = CivSettings.getDoubleStructure("quarry.coal_rate"); //10%
	private static final double REDSTONE_CHANCE = CivSettings.getDoubleStructure("quarry.redstone_chance");
	private static final double IRON_CHANCE = CivSettings.getDoubleStructure("quarry.iron_chance");
	private static final double GOLD_CHANCE = CivSettings.getDoubleStructure("quarry.gold_chance");
	private static final double TUNGSTEN_CHANCE = CivSettings.getDoubleStructure("quarry.tungsten_chance");
	private static final double RARE_CHANCE = CivSettings.getDoubleStructure("quarry.rare_chance");
	
	private static int pickaxe_level = 1;
	private static int bonus_level = 1;
	public int skippedCounter = 0;
	public ReentrantLock lock = new ReentrantLock();
	
	public enum Mineral {
		STONE,
		GRANITE_R,
		GRANITE_P,
		DIORITE_R,
		DIORITE_P,
		ANDESITE_R,
		ANDESITE_P,
		BASALT,
		CHALK,
		SHALE,
		SCHIST,
		SLATE,
		
		
		RARE,
		TUNGSTEN,
		GOLD,
		REDSTONE,
		IRON,
		COAL,
		OTHER,
		COBBLESTONE
	}
	
	protected Quarry(Location center, String id, Town town) throws CivException {
		super(center, id, town);	
		setPickaxeLevel(town.saved_quarry_pickaxe_level);
		setBonusLevel(town.saved_quarry_bonus_level);
	}
	
	public Quarry(ResultSet rs) throws SQLException, CivException {
		super(rs);
	}

	@Override
	public String getDynmapDescription() {
		String out = "<u><b>"+this.getDisplayName()+"</u></b><br/>";
		out += "Pickaxe Level: "+Quarry.pickaxe_level;
		out += "Bonus Level: "+Quarry.bonus_level;
		return out;
	}
	
	@Override
	public String getMarkerIconName() {
		return "minecart";
	}

	@SuppressWarnings("incomplete-switch")
	public double getChance(Mineral mineral) {
		double chance = 0;
		switch (mineral) {
		case RARE:
			chance = RARE_CHANCE;
			break;
		case TUNGSTEN:
			chance = TUNGSTEN_CHANCE;
			break;
		case GOLD:
			chance = GOLD_CHANCE;
			break;
		case IRON:
			chance = IRON_CHANCE;
			break;
		case REDSTONE:
			chance = REDSTONE_CHANCE;
			break;
		case COAL:
			chance = COAL_RATE;
			break;
		case OTHER:
			chance = OTHER_RATE;
			break;
		case COBBLESTONE:
			chance = COBBLESTONE_RATE;
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
		Quarry.pickaxe_level = getTown().saved_quarry_pickaxe_level;
		Quarry.bonus_level = getTown().saved_quarry_bonus_level;
	}
	
	public static int getPickaxeLevel() {
		return pickaxe_level;
	}
	
	public static int getBonusLevel() {
		return bonus_level;
	}
	
	public void setPickaxeLevel(int pickaxe_level) {
		Quarry.pickaxe_level = pickaxe_level;
	}
	
	public void setBonusLevel(int bonus_level) {
		Quarry.bonus_level = bonus_level;
	}
}
