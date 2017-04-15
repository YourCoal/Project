package com.avrgaming.civcraft.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;

import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.main.CivLog;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.Civilization;

public class ConfigCivic {
	
	public String id;
	public String name;
	public double culture_cost;
	public double cost;
	public String require_civics;
	public Integer points;
	public Integer tier;
	public Integer era;
	
	public static void loadConfig(FileConfiguration cfg, Map<String, ConfigCivic> civic_maps) {
		civic_maps.clear();
		List<Map<?, ?>> civics = cfg.getMapList("civics");
		for (Map<?, ?> confCivic : civics) {
			ConfigCivic civic = new ConfigCivic();
			civic.id = (String)confCivic.get("id");
			civic.name = (String)confCivic.get("name");
			civic.culture_cost = (Double)confCivic.get("culture_cost");
			civic.cost = (Double)confCivic.get("cost");
			civic.require_civics = (String)confCivic.get("require_civics");
			civic.points = (Integer)confCivic.get("points");
			civic.tier = (Integer)confCivic.get("tier");
			civic.era = (Integer)confCivic.get("era");
			civic_maps.put(civic.id, civic);
		}
		CivLog.info("Loaded "+civic_maps.size()+" Civics.");		
	}
	
	public static double eraRate(Civilization civ) {
		double rate = 0.0;
		double era = (CivGlobal.highestCivEra-1) - civ.getCurrentEra();
		if (era > 0) {
			rate = (era/7.5);
		}
		return rate;
	}
	
	public double getAdjustedCultureCost(Civilization civ) {
		double rate = 1.0;
		rate -= eraRate(civ);
		return Math.floor(this.culture_cost*Math.max(rate, .01));
	}
	
	public double getAdjustedCivicCost(Civilization civ) {
		double rate = 1.0;
		rate = Math.max(rate, 0.75);
		rate -= eraRate(civ);
		return Math.floor(this.cost * Math.max(rate, .01));
	}
	
	public static ArrayList<ConfigCivic> getAvailableCivics(Civilization civ) {
		ArrayList<ConfigCivic> returnCivics = new ArrayList<ConfigCivic>();
		for (ConfigCivic civic : CivSettings.civics.values()) {
			if (!civ.hasCivicology(civic.id)) {
				if (civic.isAvailable(civ)) {
					returnCivics.add(civic);
				}
			}
		}
		return returnCivics;
	}
	
	public boolean isAvailable(Civilization civ) {
		if (CivGlobal.testFileFlag("debug-norequire")) {
			CivMessage.global("Ignoring requirements! debug-norequire found.");
			return true;
		}
		
		if (require_civics == null || require_civics.equals("")) {
			return true;			
		}
		
		String[] requireCivics = require_civics.split(":");
		for (String reqCivic : requireCivics) {
			if (!civ.hasCivicology(reqCivic)) {
				return false;
			}
		}
		return true;
	}
}
