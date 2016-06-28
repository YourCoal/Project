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

public class Forge extends Structure {
	
	//We will set this default as COMMON materials, make RARE materials /10
	//Coal Rocks
	public static final int C_COAL_ORE_ROCK_CHANCE = CivSettings.getIntegerStructure("forge_c_coal_ore_rock.max");
	private static final double C_COAL_ORE_FRAGMENT_CHANCE = CivSettings.getDoubleStructure("forge_c_coal_ore_rock.fragment_chance");
	private static final double C_COAL_ORE_INGOT_CHANCE = CivSettings.getDoubleStructure("forge_c_coal_ore_rock.ingot_chance");
	//----------------------------------------------------------------\\
	public static final int R_COAL_ORE_ROCK_CHANCE = CivSettings.getIntegerStructure("forge_r_coal_ore_rock.max");
	private static final double R_COAL_ORE_FRAGMENT_CHANCE = CivSettings.getDoubleStructure("forge_r_coal_ore_rock.fragment_chance");
	private static final double R_COAL_ORE_INGOT_CHANCE = CivSettings.getDoubleStructure("forge_r_coal_ore_rock.ingot_chance");
	//Iron Rocks
	public static final int C_IRON_ORE_ROCK_CHANCE = CivSettings.getIntegerStructure("forge_c_iron_ore_rock.max");
	private static final double C_IRON_ORE_FRAGMENT_CHANCE = CivSettings.getDoubleStructure("forge_c_iron_ore_rock.fragment_chance");
	private static final double C_IRON_ORE_INGOT_CHANCE = CivSettings.getDoubleStructure("forge_c_iron_ore_rock.ingot_chance");
	//----------------------------------------------------------------\\
	public static final int R_IRON_ORE_ROCK_CHANCE = CivSettings.getIntegerStructure("forge_r_iron_ore_rock.max");
	private static final double R_IRON_ORE_FRAGMENT_CHANCE = CivSettings.getDoubleStructure("forge_r_iron_ore_rock.fragment_chance");
	private static final double R_IRON_ORE_INGOT_CHANCE = CivSettings.getDoubleStructure("forge_r_iron_ore_rock.ingot_chance");
	//Gold Rocks
	public static final int C_GOLD_ORE_ROCK_CHANCE = CivSettings.getIntegerStructure("forge_c_gold_ore_rock.max");
	private static final double C_GOLD_ORE_FRAGMENT_CHANCE = CivSettings.getDoubleStructure("forge_c_gold_ore_rock.fragment_chance");
	private static final double C_GOLD_ORE_INGOT_CHANCE = CivSettings.getDoubleStructure("forge_c_gold_ore_rock.ingot_chance");
	//----------------------------------------------------------------\\
	public static final int R_GOLD_ORE_ROCK_CHANCE = CivSettings.getIntegerStructure("forge_r_gold_ore_rock.max");
	private static final double R_GOLD_ORE_FRAGMENT_CHANCE = CivSettings.getDoubleStructure("forge_r_gold_ore_rock.fragment_chance");
	private static final double R_GOLD_ORE_INGOT_CHANCE = CivSettings.getDoubleStructure("forge_r_gold_ore_rock.ingot_chance");
	
	
	private int level = 1;
	public int skippedCounter = 0;
	public ReentrantLock lock = new ReentrantLock();
	
	public enum Mineral {
		FRAGMENT,
		INGOT
	}
	
	protected Forge(Location center, String id, Town town) throws CivException {
		super(center, id, town);	
		setLevel(town.saved_forge_level);
	}
	
	public Forge(ResultSet rs) throws SQLException, CivException {
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
	
	public double getComnCoalOreChance(Mineral mineral) {
		double chance = 0;
		switch (mineral) {
		case FRAGMENT:
			chance = C_COAL_ORE_FRAGMENT_CHANCE;
			break;
		case INGOT:
			chance = C_COAL_ORE_INGOT_CHANCE;
			break;
		}
		return this.modifyChance(chance);
	}
	
	public double getRareCoalOreChance(Mineral mineral) {
		double chance = 0;
		switch (mineral) {
		case FRAGMENT:
			chance = R_COAL_ORE_FRAGMENT_CHANCE;
			break;
		case INGOT:
			chance = R_COAL_ORE_INGOT_CHANCE;
			break;
		}
		return this.modifyChance(chance);
	}
	
	public double getComnIronOreChance(Mineral mineral) {
		double chance = 0;
		switch (mineral) {
		case FRAGMENT:
			chance = C_IRON_ORE_FRAGMENT_CHANCE;
			break;
		case INGOT:
			chance = C_IRON_ORE_INGOT_CHANCE;
			break;
		}
		return this.modifyChance(chance);
	}
	
	public double getRareIronOreChance(Mineral mineral) {
		double chance = 0;
		switch (mineral) {
		case FRAGMENT:
			chance = R_IRON_ORE_FRAGMENT_CHANCE;
			break;
		case INGOT:
			chance = R_IRON_ORE_INGOT_CHANCE;
			break;
		}
		return this.modifyChance(chance);
	}
	
	public double getComnGoldOreChance(Mineral mineral) {
		double chance = 0;
		switch (mineral) {
		case FRAGMENT:
			chance = C_GOLD_ORE_FRAGMENT_CHANCE;
			break;
		case INGOT:
			chance = C_GOLD_ORE_INGOT_CHANCE;
			break;
		}
		return this.modifyChance(chance);
	}
	
	public double getRareGoldOreChance(Mineral mineral) {
		double chance = 0;
		switch (mineral) {
		case FRAGMENT:
			chance = R_GOLD_ORE_FRAGMENT_CHANCE;
			break;
		case INGOT:
			chance = R_GOLD_ORE_INGOT_CHANCE;
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
		this.level = getTown().saved_forge_level;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
}
