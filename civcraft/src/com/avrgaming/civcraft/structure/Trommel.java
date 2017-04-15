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

public class Trommel extends Structure {
	
	private static final double DISTRICT_BLOCK_CHANCE = CivSettings.getDoubleStructure("trommel.district_block_chance");
	
	public static final int COBBLE_MAX_RATE = CivSettings.getIntegerStructure("trommel_cobble.max");
	private static final double COBBLE_IRON = CivSettings.getDoubleStructure("trommel_cobble.iron_rate");
	private static final double COBBLE_GOLD = CivSettings.getDoubleStructure("trommel_cobble.gold_rate");
	private static final double COBBLE_DIAMOND = CivSettings.getDoubleStructure("trommel_cobble.diamond_rate");
	private static final double COBBLE_EMERALD = CivSettings.getDoubleStructure("trommel_cobble.emerald_rate");
	private static final double COBBLE_REDSTONE = CivSettings.getDoubleStructure("trommel_cobble.redstone_rate");
	
	public static final int STONE_MAX_RATE = CivSettings.getIntegerStructure("trommel_stone.max");
	private static final double STONE_IRON = CivSettings.getDoubleStructure("trommel_stone.iron_rate");
	private static final double STONE_GOLD = CivSettings.getDoubleStructure("trommel_stone.gold_rate");
	private static final double STONE_DIAMOND = CivSettings.getDoubleStructure("trommel_stone.diamond_rate");
	private static final double STONE_EMERALD = CivSettings.getDoubleStructure("trommel_stone.emerald_rate");
	private static final double STONE_REDSTONE = CivSettings.getDoubleStructure("trommel_stone.redstone_rate");
	
	public static final int GRANITE_MAX_RATE = CivSettings.getIntegerStructure("trommel_granite.max");
	private static final double GRANITE_IRON = CivSettings.getDoubleStructure("trommel_granite.iron_rate");
	private static final double GRANITE_GOLD = CivSettings.getDoubleStructure("trommel_granite.gold_rate");
	private static final double GRANITE_DIAMOND = CivSettings.getDoubleStructure("trommel_granite.diamond_rate");
	private static final double GRANITE_EMERALD = CivSettings.getDoubleStructure("trommel_granite.emerald_rate");
	private static final double GRANITE_REDSTONE = CivSettings.getDoubleStructure("trommel_granite.redstone_rate");
	
	public static final int DIORITE_MAX_RATE = CivSettings.getIntegerStructure("trommel_diorite.max");
	private static final double DIORITE_IRON = CivSettings.getDoubleStructure("trommel_diorite.iron_rate");
	private static final double DIORITE_GOLD = CivSettings.getDoubleStructure("trommel_diorite.gold_rate");
	private static final double DIORITE_DIAMOND = CivSettings.getDoubleStructure("trommel_diorite.diamond_rate");
	private static final double DIORITE_EMERALD = CivSettings.getDoubleStructure("trommel_diorite.emerald_rate");
	private static final double DIORITE_REDSTONE = CivSettings.getDoubleStructure("trommel_diorite.redstone_rate");
	
	public static final int ANDESITE_MAX_RATE = CivSettings.getIntegerStructure("trommel_andesite.max");
	private static final double ANDESITE_IRON = CivSettings.getDoubleStructure("trommel_andesite.iron_rate");
	private static final double ANDESITE_GOLD = CivSettings.getDoubleStructure("trommel_andesite.gold_rate");
	private static final double ANDESITE_DIAMOND = CivSettings.getDoubleStructure("trommel_andesite.diamond_rate");
	private static final double ANDESITE_EMERALD = CivSettings.getDoubleStructure("trommel_andesite.emerald_rate");
	private static final double ANDESITE_REDSTONE = CivSettings.getDoubleStructure("trommel_andesite.redstone_rate");
	
	private int level = 1;
	public int skippedCounter = 0;
	public ReentrantLock lock = new ReentrantLock();
	
	public enum Mineral {
		BLOCK,
		EMERALD,
		DIAMOND,
		REDSTONE,
		GOLD,
		IRON
	}
	
	protected Trommel(Location center, String id, Town town) throws CivException {
		super(center, id, town);
		setLevel(town.saved_trommel_level);
	}
	
	public Trommel(ResultSet rs) throws SQLException, CivException {
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
	
	public double getGravelChance(Mineral mineral) {
		double chance = 0;
		switch (mineral) {
		case BLOCK:
			chance = DISTRICT_BLOCK_CHANCE;
			break;
		case EMERALD:
			chance = COBBLE_EMERALD;
			break;
		case DIAMOND:
			chance = COBBLE_DIAMOND;
			break;
		case GOLD:
			chance = COBBLE_GOLD;
			break;
		case IRON:
			chance = COBBLE_IRON;
			break;
		case REDSTONE:
			chance = COBBLE_REDSTONE;
			break;
		}
		return this.modifyChance(chance);
	}
	
	public double getStoneChance(Mineral mineral) {
		double chance = 0;
		switch (mineral) {
		case BLOCK:
			chance = DISTRICT_BLOCK_CHANCE;
			break;
		case EMERALD:
			chance = STONE_EMERALD;
			break;
		case DIAMOND:
			chance = STONE_DIAMOND;
			break;
		case GOLD:
			chance = STONE_GOLD;
			break;
		case IRON:
			chance = STONE_IRON;
			break;
		case REDSTONE:
			chance = STONE_REDSTONE;
			break;
		}
		return this.modifyChance(chance);
	}
	
	public double getGraniteChance(Mineral mineral) {
		double chance = 0;
		switch (mineral) {
		case BLOCK:
			chance = DISTRICT_BLOCK_CHANCE;
			break;
		case EMERALD:
			chance = GRANITE_EMERALD;
			break;
		case DIAMOND:
			chance = GRANITE_DIAMOND;
			break;
		case GOLD:
			chance = GRANITE_GOLD;
			break;
		case IRON:
			chance = GRANITE_IRON;
			break;
		case REDSTONE:
			chance = GRANITE_REDSTONE;
			break;
		}
		return this.modifyChance(chance);
	}
	
	public double getDioriteChance(Mineral mineral) {
		double chance = 0;
		switch (mineral) {
		case BLOCK:
			chance = DISTRICT_BLOCK_CHANCE;
			break;
		case EMERALD:
			chance = DIORITE_EMERALD;
			break;
		case DIAMOND:
			chance = DIORITE_DIAMOND;
			break;
		case GOLD:
			chance = DIORITE_GOLD;
			break;
		case IRON:
			chance = DIORITE_IRON;
			break;
		case REDSTONE:
			chance = DIORITE_REDSTONE;
			break;
		}
		return this.modifyChance(chance);
	}
	
	public double getAndesiteChance(Mineral mineral) {
		double chance = 0;
		switch (mineral) {
		case BLOCK:
			chance = DISTRICT_BLOCK_CHANCE;
			break;
		case EMERALD:
			chance = ANDESITE_EMERALD;
			break;
		case DIAMOND:
			chance = ANDESITE_DIAMOND;
			break;
		case GOLD:
			chance = ANDESITE_GOLD;
			break;
		case IRON:
			chance = ANDESITE_IRON;
			break;
		case REDSTONE:
			chance = ANDESITE_REDSTONE;
			break;
		}
		return this.modifyChance(chance);
	}
	
	private double modifyChance(Double chance) {
		double increase = chance*this.getTown().getBuffManager().getEffectiveDouble(Buff.EXTRACTION);
		chance += increase;
		try {
			if (this.getTown().getGovernment().id.equals("gov_technocracy")) {
				chance += CivSettings.getDouble(CivSettings.structureConfig, "trommel.despotism_rate");
			} else if (this.getTown().getGovernment().id.equals("gov_anarchy")){
				chance -= CivSettings.getDouble(CivSettings.structureConfig, "trommel.penalty_rate");
			}
		} catch (InvalidConfiguration e) {
			e.printStackTrace();
		}
		return chance;
	}
	
	@Override
	public void onPostBuild(BlockCoord absCoord, SimpleBlock commandBlock) {
		this.level = getTown().saved_trommel_level;
	}
	
	public int getLevel() {
		return level;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
}
