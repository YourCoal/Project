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

public class GranaryUpdate extends Structure {
	public static final int BREAD_MAX = CivSettings.getIntegerStructure("granary_bread.max");
	private static final double BREAD_EASY_CHANCE = CivSettings.getDoubleStructure("granary_bread.easy_chance");
	private static final double BREAD_MEDIUM_CHANCE = CivSettings.getDoubleStructure("granary_bread.medium_chance");
	private static final double BREAD_HARD_CHANCE = CivSettings.getDoubleStructure("granary_bread.hard_chance");
	private static final double BREAD_MEGA_CHANCE = CivSettings.getDoubleStructure("granary_bread.mega_chance");
	private static final double BREAD_ULTRA_CHANCE = CivSettings.getDoubleStructure("granary_bread.ultra_chance");
	private static final double BREAD_EXTREME_CHANCE = CivSettings.getDoubleStructure("granary_bread.extreme_chance");
	
	public static final int CARROT_MAX = CivSettings.getIntegerStructure("granary_carrot.max");
	private static final double CARROT_EASY_CHANCE = CivSettings.getDoubleStructure("granary_carrot.easy_chance");
	private static final double CARROT_MEDIUM_CHANCE = CivSettings.getDoubleStructure("granary_carrot.medium_chance");
	private static final double CARROT_HARD_CHANCE = CivSettings.getDoubleStructure("granary_carrot.hard_chance");
	private static final double CARROT_MEGA_CHANCE = CivSettings.getDoubleStructure("granary_carrot.mega_chance");
	private static final double CARROT_ULTRA_CHANCE = CivSettings.getDoubleStructure("granary_carrot.ultra_chance");
	private static final double CARROT_EXTREME_CHANCE = CivSettings.getDoubleStructure("granary_carrot.extreme_chance");
	
	public static final int POTATO_MAX = CivSettings.getIntegerStructure("granary_potato.max");
	private static final double POTATO_EASY_CHANCE = CivSettings.getDoubleStructure("granary_potato.easy_chance");
	private static final double POTATO_MEDIUM_CHANCE = CivSettings.getDoubleStructure("granary_potato.medium_chance");
	private static final double POTATO_HARD_CHANCE = CivSettings.getDoubleStructure("granary_potato.hard_chance");
	private static final double POTATO_MEGA_CHANCE = CivSettings.getDoubleStructure("granary_potato.mega_chance");
	private static final double POTATO_ULTRA_CHANCE = CivSettings.getDoubleStructure("granary_potato.ultra_chance");
	private static final double POTATO_EXTREME_CHANCE = CivSettings.getDoubleStructure("granary_potato.extreme_chance");
	
	private int level = 1;
	public int skippedCounter = 0;
	public ReentrantLock lock = new ReentrantLock();
	
	//l1 bread
	//l2 carrot
	//l3 potato
	//l4 beetroot
	//l5 melon
	
	public enum Chance {
//		REGULAR, // 2 food (Do not need enabled..)
		EASY, // 3 food
		MEDIUM, //4 food
		HARD, // 6 food
		MEGA, // 8 food
		ULTRA, // 11 food
		EXTREME // 15 food
	}
	
	protected GranaryUpdate(Location center, String id, Town town) throws CivException {
		super(center, id, town);	
		setLevel(town.saved_trommel_level);
	}
	
	public GranaryUpdate(ResultSet rs) throws SQLException, CivException {
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
		return "minecart";
	}
	
	public double getBreadChance(Chance c) {
		double chance = 0;
		switch (c) {
		case EASY:
			chance = BREAD_EASY_CHANCE;
			break;
		case MEDIUM:
			chance = BREAD_MEDIUM_CHANCE;
			break;
		case HARD:
			chance = BREAD_HARD_CHANCE;
			break;
		case MEGA:
			chance = BREAD_MEGA_CHANCE;
			break;
		case ULTRA:
			chance = BREAD_ULTRA_CHANCE;
			break;
		case EXTREME:
			chance = BREAD_EXTREME_CHANCE;
			break;
//		default:
//			break;
		}
		return this.modifyChance(chance);
	}
	
	public double getCarrotChance(Chance c) {
		double chance = 0;
		switch (c) {
		case EASY:
			chance = CARROT_EASY_CHANCE;
			break;
		case MEDIUM:
			chance = CARROT_MEDIUM_CHANCE;
			break;
		case HARD:
			chance = CARROT_HARD_CHANCE;
			break;
		case MEGA:
			chance = CARROT_MEGA_CHANCE;
			break;
		case ULTRA:
			chance = CARROT_ULTRA_CHANCE;
			break;
		case EXTREME:
			chance = CARROT_EXTREME_CHANCE;
			break;
//		default:
//			break;
		}
		return this.modifyChance(chance);
	}
	
	public double getPotatoChance(Chance c) {
		double chance = 0;
		switch (c) {
		case EASY:
			chance = POTATO_EASY_CHANCE;
			break;
		case MEDIUM:
			chance = POTATO_MEDIUM_CHANCE;
			break;
		case HARD:
			chance = POTATO_HARD_CHANCE;
			break;
		case MEGA:
			chance = POTATO_MEGA_CHANCE;
			break;
		case ULTRA:
			chance = POTATO_ULTRA_CHANCE;
			break;
		case EXTREME:
			chance = POTATO_EXTREME_CHANCE;
			break;
//		default:
//			break;
		}
		return this.modifyChance(chance);
	}
	
	private double modifyChance(Double chance) {
		double increase = chance*this.getTown().getBuffManager().getEffectiveDouble(Buff.EXTRACTION);
		chance += increase;
//		try {
//			if (this.getTown().getGovernment().id.equals("gov_despotism")) {
//				chance *= CivSettings.getDouble(CivSettings.structureConfig, "trommel.despotism_rate");
//			} else if (this.getTown().getGovernment().id.equals("gov_theocracy") || this.getTown().getGovernment().id.equals("gov_monarchy")){
//				chance *= CivSettings.getDouble(CivSettings.structureConfig, "trommel.penalty_rate");
//			}
//		} catch (InvalidConfiguration e) {
//			e.printStackTrace();
//		}
		return chance;
	}
	
	@Override
	public void onPostBuild(BlockCoord absCoord, SimpleBlock commandBlock) {
		this.level = getTown().saved_granary_level;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}