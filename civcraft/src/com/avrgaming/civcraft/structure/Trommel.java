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
	
	public static final int GRAVEL_MAX_CHANCE = CivSettings.getIntegerStructure("trommel_cobble.max");
	private static final double GRAVEL_IRON_CHANCE = CivSettings.getDoubleStructure("trommel_cobble.iron_chance"); //5%
	private static final double GRAVEL_GOLD_CHANCE = CivSettings.getDoubleStructure("trommel_cobble.gold_chance"); //2.5%
	private static final double GRAVEL_LAPIS_CHANCE = CivSettings.getDoubleStructure("trommel_cobble.lapis_chance"); //1.75%
	private static final double GRAVEL_REDSTONE_CHANCE = CivSettings.getDoubleStructure("trommel_cobble.redstone_chance"); //1.25%
	private static final double GRAVEL_DIAMOND_CHANCE = CivSettings.getDoubleStructure("trommel_cobble.diamond_chance"); //0.5%
	private static final double GRAVEL_EMERALD_CHANCE = CivSettings.getDoubleStructure("trommel_cobble.emerald_chance"); //0.25%
	private static final double GRAVEL_CHROMIUM_CHANCE = CivSettings.getDoubleStructure("trommel_cobble.chromium_chance"); //0.1%
	
	public static final int STONE_MAX_CHANCE = CivSettings.getIntegerStructure("trommel_stone.max");
	private static final double STONE_IRON_CHANCE = CivSettings.getDoubleStructure("trommel_stone.iron_chance"); //10%
	private static final double STONE_GOLD_CHANCE = CivSettings.getDoubleStructure("trommel_stone.gold_chance"); //5%
	private static final double STONE_LAPIS_CHANCE = CivSettings.getDoubleStructure("trommel_stone.lapis_chance"); //3.5%
	private static final double STONE_REDSTONE_CHANCE = CivSettings.getDoubleStructure("trommel_stone.redstone_chance"); //2.5%
	private static final double STONE_DIAMOND_CHANCE = CivSettings.getDoubleStructure("trommel_stone.diamond_chance"); //1%
	private static final double STONE_EMERALD_CHANCE = CivSettings.getDoubleStructure("trommel_stone.emerald_chance"); //0.5%
	private static final double STONE_CHROMIUM_CHANCE = CivSettings.getDoubleStructure("trommel_stone.chromium_chance"); //0.2%
	
	public static final int GRANITE_MAX_CHANCE = CivSettings.getIntegerStructure("trommel_granite.max");
	private static final double GRANITE_IRON_CHANCE = CivSettings.getDoubleStructure("trommel_granite.iron_chance"); //5%
	private static final double GRANITE_GOLD_CHANCE = CivSettings.getDoubleStructure("trommel_granite.gold_chance"); //2.5%
	private static final double GRANITE_LAPIS_CHANCE = CivSettings.getDoubleStructure("trommel_granite.lapis_chance"); //1.75%
	private static final double GRANITE_REDSTONE_CHANCE = CivSettings.getDoubleStructure("trommel_granite.redstone_chance"); //1.25%
	private static final double GRANITE_DIAMOND_CHANCE = CivSettings.getDoubleStructure("trommel_granite.diamond_chance"); //0.5%
	private static final double GRANITE_EMERALD_CHANCE = CivSettings.getDoubleStructure("trommel_granite.emerald_chance"); //0.25%
	private static final double GRANITE_CHROMIUM_CHANCE = CivSettings.getDoubleStructure("trommel_granite.chromium_chance"); //0.1%
	
	public static final int POL_GRANITE_MAX_CHANCE = CivSettings.getIntegerStructure("trommel_polgranite.max");
	private static final double POL_GRANITE_IRON_CHANCE = CivSettings.getDoubleStructure("trommel_polgranite.iron_chance"); //10%
	private static final double POL_GRANITE_GOLD_CHANCE = CivSettings.getDoubleStructure("trommel_polgranite.gold_chance"); //5%
	private static final double POL_GRANITE_LAPIS_CHANCE = CivSettings.getDoubleStructure("trommel_polgranite.lapis_chance"); //3.5%
	private static final double POL_GRANITE_REDSTONE_CHANCE = CivSettings.getDoubleStructure("trommel_polgranite.redstone_chance"); //2.5%
	private static final double POL_GRANITE_DIAMOND_CHANCE = CivSettings.getDoubleStructure("trommel_polgranite.diamond_chance"); //1%
	private static final double POL_GRANITE_EMERALD_CHANCE = CivSettings.getDoubleStructure("trommel_polgranite.emerald_chance"); //0.5%
	private static final double POL_GRANITE_CHROMIUM_CHANCE = CivSettings.getDoubleStructure("trommel_polgranite.chromium_chance"); //0.2%
	
	public static final int DIORITE_MAX_CHANCE = CivSettings.getIntegerStructure("trommel_diorite.max");
	private static final double DIORITE_IRON_CHANCE = CivSettings.getDoubleStructure("trommel_diorite.iron_chance"); //5%
	private static final double DIORITE_GOLD_CHANCE = CivSettings.getDoubleStructure("trommel_diorite.gold_chance"); //2.5%
	private static final double DIORITE_LAPIS_CHANCE = CivSettings.getDoubleStructure("trommel_diorite.lapis_chance"); //1.75%
	private static final double DIORITE_REDSTONE_CHANCE = CivSettings.getDoubleStructure("trommel_diorite.redstone_chance"); //1.25%
	private static final double DIORITE_DIAMOND_CHANCE = CivSettings.getDoubleStructure("trommel_diorite.diamond_chance"); //0.5%
	private static final double DIORITE_EMERALD_CHANCE = CivSettings.getDoubleStructure("trommel_diorite.emerald_chance"); //0.25%
	private static final double DIORITE_CHROMIUM_CHANCE = CivSettings.getDoubleStructure("trommel_diorite.chromium_chance"); //0.1%
	
	public static final int POL_DIORITE_MAX_CHANCE = CivSettings.getIntegerStructure("trommel_poldiorite.max");
	private static final double POL_DIORITE_IRON_CHANCE = CivSettings.getDoubleStructure("trommel_poldiorite.iron_chance"); //10%
	private static final double POL_DIORITE_GOLD_CHANCE = CivSettings.getDoubleStructure("trommel_poldiorite.gold_chance"); //5%
	private static final double POL_DIORITE_LAPIS_CHANCE = CivSettings.getDoubleStructure("trommel_poldiorite.lapis_chance"); //3.5%
	private static final double POL_DIORITE_REDSTONE_CHANCE = CivSettings.getDoubleStructure("trommel_poldiorite.redstone_chance"); //2.5%
	private static final double POL_DIORITE_DIAMOND_CHANCE = CivSettings.getDoubleStructure("trommel_poldiorite.diamond_chance"); //1%
	private static final double POL_DIORITE_EMERALD_CHANCE = CivSettings.getDoubleStructure("trommel_poldiorite.emerald_chance"); //0.5%
	private static final double POL_DIORITE_CHROMIUM_CHANCE = CivSettings.getDoubleStructure("trommel_poldiorite.chromium_chance"); //0.2%
	
	public static final int ANDESITE_MAX_CHANCE = CivSettings.getIntegerStructure("trommel_andesite.max");
	private static final double ANDESITE_IRON_CHANCE = CivSettings.getDoubleStructure("trommel_andesite.iron_chance"); //5%
	private static final double ANDESITE_GOLD_CHANCE = CivSettings.getDoubleStructure("trommel_andesite.gold_chance"); //2.5%
	private static final double ANDESITE_LAPIS_CHANCE = CivSettings.getDoubleStructure("trommel_andesite.lapis_chance"); //1.75%
	private static final double ANDESITE_REDSTONE_CHANCE = CivSettings.getDoubleStructure("trommel_andesite.redstone_chance"); //1.25%
	private static final double ANDESITE_DIAMOND_CHANCE = CivSettings.getDoubleStructure("trommel_andesite.diamond_chance"); //0.5%
	private static final double ANDESITE_EMERALD_CHANCE = CivSettings.getDoubleStructure("trommel_andesite.emerald_chance"); //0.25%
	private static final double ANDESITE_CHROMIUM_CHANCE = CivSettings.getDoubleStructure("trommel_andesite.chromium_chance"); //0.1%
	
	public static final int POL_ANDESITE_MAX_CHANCE = CivSettings.getIntegerStructure("trommel_polandesite.max");
	private static final double POL_ANDESITE_IRON_CHANCE = CivSettings.getDoubleStructure("trommel_polandesite.iron_chance"); //10%
	private static final double POL_ANDESITE_GOLD_CHANCE = CivSettings.getDoubleStructure("trommel_polandesite.gold_chance"); //5%
	private static final double POL_ANDESITE_LAPIS_CHANCE = CivSettings.getDoubleStructure("trommel_polandesite.lapis_chance"); //3.5%
	private static final double POL_ANDESITE_REDSTONE_CHANCE = CivSettings.getDoubleStructure("trommel_polandesite.redstone_chance"); //2.5%
	private static final double POL_ANDESITE_DIAMOND_CHANCE = CivSettings.getDoubleStructure("trommel_polandesite.diamond_chance"); //1%
	private static final double POL_ANDESITE_EMERALD_CHANCE = CivSettings.getDoubleStructure("trommel_polandesite.emerald_chance"); //0.5%
	private static final double POL_ANDESITE_CHROMIUM_CHANCE = CivSettings.getDoubleStructure("trommel_polandesite.chromium_chance"); //0.2%
	
	public static final int OBSIDIAN_MAX_CHANCE = CivSettings.getIntegerStructure("trommel_obsidian.max");
	private static final double OBSIDIAN_IRON_CHANCE = CivSettings.getDoubleStructure("trommel_obsidian.iron_chance"); //20%
	private static final double OBSIDIAN_GOLD_CHANCE = CivSettings.getDoubleStructure("trommel_obsidian.gold_chance"); //10%
	private static final double OBSIDIAN_LAPIS_CHANCE = CivSettings.getDoubleStructure("trommel_obsidian.lapis_chance"); //7%
	private static final double OBSIDIAN_REDSTONE_CHANCE = CivSettings.getDoubleStructure("trommel_obsidian.redstone_chance"); //5%
	private static final double OBSIDIAN_DIAMOND_CHANCE = CivSettings.getDoubleStructure("trommel_obsidian.diamond_chance"); //12%
	private static final double OBSIDIAN_EMERALD_CHANCE = CivSettings.getDoubleStructure("trommel_obsidian.emerald_chance"); //1%
	private static final double OBSIDIAN_CHROMIUM_CHANCE = CivSettings.getDoubleStructure("trommel_obsidian.chromium_chance"); //0.4%
	
	private int level = 1;
	public int skippedCounter = 0;
	public ReentrantLock lock = new ReentrantLock();
	
	public enum Mineral {
		CHROMIUM,
		EMERALD,
		DIAMOND,
		REDSTONE,
		LAPIS,
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
		case EMERALD:
			chance = GRAVEL_EMERALD_CHANCE;
			break;
		case DIAMOND:
			chance = GRAVEL_DIAMOND_CHANCE;
			break;
		case REDSTONE:
			chance = GRAVEL_REDSTONE_CHANCE;
			break;
		case LAPIS:
			chance = GRAVEL_LAPIS_CHANCE;
			break;
		case GOLD:
			chance = GRAVEL_GOLD_CHANCE;
			break;
		case IRON:
			chance = GRAVEL_IRON_CHANCE;
			break;
		case CHROMIUM:
			chance = GRAVEL_CHROMIUM_CHANCE;
		default:
			break;
		}
		return this.modifyChance(chance);
	}
	
	public double getStoneChance(Mineral mineral) {
		double chance = 0;
		switch (mineral) {
		case EMERALD:
			chance = STONE_EMERALD_CHANCE;
			break;
		case DIAMOND:
			chance = STONE_DIAMOND_CHANCE;
			break;
		case REDSTONE:
			chance = STONE_REDSTONE_CHANCE;
			break;
		case LAPIS:
			chance = STONE_LAPIS_CHANCE;
			break;
		case GOLD:
			chance = STONE_GOLD_CHANCE;
			break;
		case IRON:
			chance = STONE_IRON_CHANCE;
			break;
		case CHROMIUM:
			chance = STONE_CHROMIUM_CHANCE;
			break;
		}
		return this.modifyChance(chance);
	}
	
	public double getGraniteChance(Mineral mineral) {
		double chance = 0;
		switch (mineral) {
		case EMERALD:
			chance = GRANITE_EMERALD_CHANCE;
			break;
		case DIAMOND:	
			chance = GRANITE_DIAMOND_CHANCE;
			break;
		case REDSTONE:
			chance = GRANITE_REDSTONE_CHANCE;
			break;
		case LAPIS:
			chance = GRANITE_LAPIS_CHANCE;
			break;
		case GOLD:
			chance = GRANITE_GOLD_CHANCE;
			break;
		case IRON:
			chance = GRANITE_IRON_CHANCE;
			break;
		case CHROMIUM:
			chance = GRANITE_CHROMIUM_CHANCE;
			break;
		}
		return this.modifyChance(chance);
	}
	
	public double getPolGraniteChance(Mineral mineral) {
		double chance = 0;
		switch (mineral) {
		case EMERALD:
			chance = POL_GRANITE_EMERALD_CHANCE;
			break;
		case DIAMOND:	
			chance = POL_GRANITE_DIAMOND_CHANCE;
			break;
		case REDSTONE:
			chance = POL_GRANITE_REDSTONE_CHANCE;
			break;
		case LAPIS:
			chance = POL_GRANITE_LAPIS_CHANCE;
			break;
		case GOLD:
			chance = POL_GRANITE_GOLD_CHANCE;
			break;
		case IRON:
			chance = POL_GRANITE_IRON_CHANCE;
			break;
		case CHROMIUM:
			chance = POL_GRANITE_CHROMIUM_CHANCE;
			break;
		}
		return this.modifyChance(chance);
	}
	
	public double getDioriteChance(Mineral mineral) {
		double chance = 0;
		switch (mineral) {
		case EMERALD:
			chance = DIORITE_EMERALD_CHANCE;
			break;
		case DIAMOND:	
			chance = DIORITE_DIAMOND_CHANCE;
			break;
		case REDSTONE:
			chance = DIORITE_REDSTONE_CHANCE;
			break;
		case LAPIS:
			chance = DIORITE_LAPIS_CHANCE;
			break;
		case GOLD:
			chance = DIORITE_GOLD_CHANCE;
			break;
		case IRON:
			chance = DIORITE_IRON_CHANCE;
			break;
		case CHROMIUM:
			chance = DIORITE_CHROMIUM_CHANCE;
			break;
		}
		return this.modifyChance(chance);
	}
	
	public double getPolDioriteChance(Mineral mineral) {
		double chance = 0;
		switch (mineral) {
		case EMERALD:
			chance = POL_DIORITE_EMERALD_CHANCE;
			break;
		case DIAMOND:	
			chance = POL_DIORITE_DIAMOND_CHANCE;
			break;
		case REDSTONE:
			chance = POL_DIORITE_REDSTONE_CHANCE;
			break;
		case LAPIS:
			chance = POL_DIORITE_LAPIS_CHANCE;
			break;
		case GOLD:
			chance = POL_DIORITE_GOLD_CHANCE;
			break;
		case IRON:
			chance = POL_DIORITE_IRON_CHANCE;
			break;
		case CHROMIUM:
			chance = POL_DIORITE_CHROMIUM_CHANCE;
			break;
		}
		return this.modifyChance(chance);
	}
	
	public double getAndesiteChance(Mineral mineral) {
		double chance = 0;
		switch (mineral) {
		case EMERALD:
			chance = ANDESITE_EMERALD_CHANCE;
			break;
		case DIAMOND:	
			chance = ANDESITE_DIAMOND_CHANCE;
			break;
		case REDSTONE:
			chance = ANDESITE_REDSTONE_CHANCE;
			break;
		case LAPIS:
			chance = ANDESITE_LAPIS_CHANCE;
			break;
		case GOLD:
			chance = ANDESITE_GOLD_CHANCE;
			break;
		case IRON:
			chance = ANDESITE_IRON_CHANCE;
			break;
		case CHROMIUM:
			chance = ANDESITE_CHROMIUM_CHANCE;
			break;
		}
		return this.modifyChance(chance);
	}
	
	public double getPolAndesiteChance(Mineral mineral) {
		double chance = 0;
		switch (mineral) {
		case EMERALD:
			chance = POL_ANDESITE_EMERALD_CHANCE;
			break;
		case DIAMOND:	
			chance = POL_ANDESITE_DIAMOND_CHANCE;
			break;
		case REDSTONE:
			chance = POL_ANDESITE_REDSTONE_CHANCE;
			break;
		case LAPIS:
			chance = POL_ANDESITE_LAPIS_CHANCE;
			break;
		case GOLD:
			chance = POL_ANDESITE_GOLD_CHANCE;
			break;
		case IRON:
			chance = POL_ANDESITE_IRON_CHANCE;
			break;
		case CHROMIUM:
			chance = POL_ANDESITE_CHROMIUM_CHANCE;
			break;
		}
		return this.modifyChance(chance);
	}
	
	public double getObsidianChance(Mineral mineral) {
		double chance = 0;
		switch (mineral) {
		case EMERALD:
			chance = OBSIDIAN_EMERALD_CHANCE;
			break;
		case DIAMOND:
			chance = OBSIDIAN_DIAMOND_CHANCE;
			break;
		case REDSTONE:
			chance = OBSIDIAN_REDSTONE_CHANCE;
			break;
		case LAPIS:
			chance = OBSIDIAN_LAPIS_CHANCE;
			break;
		case GOLD:
			chance = OBSIDIAN_GOLD_CHANCE;
			break;
		case IRON:
			chance = OBSIDIAN_IRON_CHANCE;
			break;
		case CHROMIUM:
			chance = OBSIDIAN_CHROMIUM_CHANCE;
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
//			} else if (this.getTown().getGovernment().id.equals("gov_theocracy") || this.getTown().getGovernment().id.equals("gov_monarchy")) {
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
