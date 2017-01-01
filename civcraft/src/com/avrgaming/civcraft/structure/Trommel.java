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
import java.util.concurrent.locks.ReentrantLock;

import org.bukkit.Location;

import com.avrgaming.civcraft.config.CivSettings;
import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.object.Buff;
import com.avrgaming.civcraft.object.Town;
import com.avrgaming.civcraft.util.BlockCoord;
import com.avrgaming.civcraft.util.SimpleBlock;

public class Trommel extends Structure {
	
	public static final int COBBLESTONE_MAX = CivSettings.getIntegerStructure("trommel_cobblestone.max");
	private static final double COBBLESTONE_IRON_RATE = CivSettings.getDoubleStructure("trommel_cobblestone.iron_rate");
	private static final double COBBLESTONE_GOLD_RATE = CivSettings.getDoubleStructure("trommel_cobblestone.gold_rate");
	private static final double COBBLESTONE_LAPIS_REDSTONE_RATE = CivSettings.getDoubleStructure("trommel_cobblestone.lapis_redstone_rate");
	private static final double COBBLESTONE_DIAMOND_RATE = CivSettings.getDoubleStructure("trommel_cobblestone.diamond_rate");
	private static final double COBBLESTONE_EMERALD_RATE = CivSettings.getDoubleStructure("trommel_cobblestone.emerald_rate");
	private static final double COBBLESTONE_CUSTOM_ORE_RATE = CivSettings.getDoubleStructure("trommel_cobblestone.custom_ore_rate");
	
	public static final int STONE_MAX = CivSettings.getIntegerStructure("trommel_cobblestone.max");
	private static final double STONE_IRON_RATE = CivSettings.getDoubleStructure("trommel_stone.iron_rate");
	private static final double STONE_GOLD_RATE = CivSettings.getDoubleStructure("trommel_stone.gold_rate");
	private static final double STONE_LAPIS_REDSTONE_RATE = CivSettings.getDoubleStructure("trommel_stone.lapis_redstone_rate");
	private static final double STONE_DIAMOND_RATE = CivSettings.getDoubleStructure("trommel_stone.diamond_rate");
	private static final double STONE_EMERALD_RATE = CivSettings.getDoubleStructure("trommel_stone.emerald_rate");
	private static final double STONE_CUSTOM_ORE_RATE = CivSettings.getDoubleStructure("trommel_stone.custom_ore_rate");
	
	public static final int GRANITE_MAX = CivSettings.getIntegerStructure("trommel_granite.max");
	private static final double GRANITE_IRON_RATE = CivSettings.getDoubleStructure("trommel_granite.iron_rate");
	private static final double GRANITE_GOLD_RATE = CivSettings.getDoubleStructure("trommel_granite.gold_rate");
	private static final double GRANITE_LAPIS_REDSTONE_RATE = CivSettings.getDoubleStructure("trommel_granite.lapis_redstone_rate");
	private static final double GRANITE_DIAMOND_RATE = CivSettings.getDoubleStructure("trommel_granite.diamond_rate");
	private static final double GRANITE_EMERALD_RATE = CivSettings.getDoubleStructure("trommel_granite.emerald_rate");
	private static final double GRANITE_CUSTOM_ORE_RATE = CivSettings.getDoubleStructure("trommel_granite.custom_ore_rate");
	
	public static final int DIORITE_MAX = CivSettings.getIntegerStructure("trommel_diorite.max");
	private static final double DIORITE_IRON_RATE = CivSettings.getDoubleStructure("trommel_diorite.iron_rate");
	private static final double DIORITE_GOLD_RATE = CivSettings.getDoubleStructure("trommel_diorite.gold_rate");
	private static final double DIORITE_LAPIS_REDSTONE_RATE = CivSettings.getDoubleStructure("trommel_diorite.lapis_redstone_rate");
	private static final double DIORITE_DIAMOND_RATE = CivSettings.getDoubleStructure("trommel_diorite.diamond_rate");
	private static final double DIORITE_EMERALD_RATE = CivSettings.getDoubleStructure("trommel_diorite.emerald_rate");
	private static final double DIORITE_CUSTOM_ORE_RATE = CivSettings.getDoubleStructure("trommel_diorite.custom_ore_rate");
	
	public static final int ANDESITE_MAX = CivSettings.getIntegerStructure("trommel_andesite.max");
	private static final double ANDESITE_IRON_RATE = CivSettings.getDoubleStructure("trommel_andesite.iron_rate");
	private static final double ANDESITE_GOLD_RATE = CivSettings.getDoubleStructure("trommel_andesite.gold_rate");
	private static final double ANDESITE_LAPIS_REDSTONE_RATE = CivSettings.getDoubleStructure("trommel_andesite.lapis_redstone_rate");
	private static final double ANDESITE_DIAMOND_RATE = CivSettings.getDoubleStructure("trommel_andesite.diamond_rate");
	private static final double ANDESITE_EMERALD_RATE = CivSettings.getDoubleStructure("trommel_andesite.emerald_rate");
	private static final double ANDESITE_CUSTOM_ORE_RATE = CivSettings.getDoubleStructure("trommel_andesite.custom_ore_rate");
	
	private int level = 1;
	public int skippedCounter = 0;
	public ReentrantLock lock = new ReentrantLock();
	
	public enum Mineral {
		CUSTOMINGOT,
		CUSTOMORE,
		EMERALD,
		DIAMOND,
		LAPIS_REDSTONE,
		GOLD,
		IRON,
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
	
	public double getCobblestoneChance(Mineral mineral) {
		double chance = 0;
		switch (mineral) {
		case CUSTOMORE:
			chance = COBBLESTONE_CUSTOM_ORE_RATE;
			break;
		case EMERALD:
			chance = COBBLESTONE_EMERALD_RATE;
			break;
		case DIAMOND:
			chance = COBBLESTONE_DIAMOND_RATE;
			break;
		case LAPIS_REDSTONE:
			chance = COBBLESTONE_LAPIS_REDSTONE_RATE;
			break;
		case GOLD:
			chance = COBBLESTONE_GOLD_RATE;
			break;
		case IRON:
			chance = COBBLESTONE_IRON_RATE;
			break;
		default:
			break;
		}
		return this.modifyChance(chance);
	}
	
	public double getStoneChance(Mineral mineral) {
		double chance = 0;
		switch (mineral) {
		case CUSTOMORE:
			chance = STONE_CUSTOM_ORE_RATE;
			break;
		case EMERALD:
			chance = STONE_EMERALD_RATE;
			break;
		case DIAMOND:
			chance = STONE_DIAMOND_RATE;
			break;
		case LAPIS_REDSTONE:
			chance = STONE_LAPIS_REDSTONE_RATE;
			break;
		case GOLD:
			chance = STONE_GOLD_RATE;
			break;
		case IRON:
			chance = STONE_IRON_RATE;
			break;
		default:
			break;
		}
		return this.modifyChance(chance);
	}
	
	public double getGraniteChance(Mineral mineral) {
		double chance = 0;
		switch (mineral) {
		case CUSTOMORE:
			chance = GRANITE_CUSTOM_ORE_RATE;
			break;
		case EMERALD:
			chance = GRANITE_EMERALD_RATE;
			break;
		case DIAMOND:
			chance = GRANITE_DIAMOND_RATE;
			break;
		case LAPIS_REDSTONE:
			chance = GRANITE_LAPIS_REDSTONE_RATE;
			break;
		case GOLD:
			chance = GRANITE_GOLD_RATE;
			break;
		case IRON:
			chance = GRANITE_IRON_RATE;
			break;
		default:
			break;
		}
		return this.modifyChance(chance);
	}
	
	public double getDioriteChance(Mineral mineral) {
		double chance = 0;
		switch (mineral) {
		case CUSTOMORE:
			chance = DIORITE_CUSTOM_ORE_RATE;
			break;
		case EMERALD:
			chance = DIORITE_EMERALD_RATE;
			break;
		case DIAMOND:
			chance = DIORITE_DIAMOND_RATE;
			break;
		case LAPIS_REDSTONE:
			chance = DIORITE_LAPIS_REDSTONE_RATE;
			break;
		case GOLD:
			chance = DIORITE_GOLD_RATE;
			break;
		case IRON:
			chance = DIORITE_IRON_RATE;
			break;
		default:
			break;
		}
		return this.modifyChance(chance);
	}
	
	public double getAndesiteChance(Mineral mineral) {
		double chance = 0;
		switch (mineral) {
		case CUSTOMORE:
			chance = ANDESITE_CUSTOM_ORE_RATE;
			break;
		case EMERALD:
			chance = ANDESITE_EMERALD_RATE;
			break;
		case DIAMOND:
			chance = ANDESITE_DIAMOND_RATE;
			break;
		case LAPIS_REDSTONE:
			chance = ANDESITE_LAPIS_REDSTONE_RATE;
			break;
		case GOLD:
			chance = ANDESITE_GOLD_RATE;
			break;
		case IRON:
			chance = ANDESITE_IRON_RATE;
			break;
		default:
			break;
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
		this.level = getTown().saved_trommel_level;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
}