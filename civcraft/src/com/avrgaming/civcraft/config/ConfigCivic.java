package com.avrgaming.civcraft.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;

import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.main.CivLog;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.Town;

public class ConfigCivic {
	public String id;
	public String name;
	public double culture_cost;
	public double cost;
	public Integer points;
	public String require_techs;
	public String require_civics;
	public String bonus_factors;
	public int era;
	
	public static void loadConfig(FileConfiguration cfg, Map<String, ConfigCivic> civic_maps) {
		civic_maps.clear();
		List<Map<?, ?>> civics = cfg.getMapList("civics");
		for (Map<?, ?> confCivic : civics) {
			ConfigCivic civic = new ConfigCivic();
			
			civic.id = (String)confCivic.get("id");
			civic.name = (String)confCivic.get("name");
			civic.culture_cost = (Double)confCivic.get("culture_cost");
			civic.cost = (Double)confCivic.get("cost");
			civic.points = (Integer)confCivic.get("points");
			civic.require_techs = (String)confCivic.get("require_techs");
			civic.require_civics = (String)confCivic.get("require_civics");
			if ((String)confCivic.get("bonus_factors") != null) {
				civic.bonus_factors = (String)confCivic.get("bonus_factors");
			} else {
				civic.bonus_factors = "unknown";
			}
			civic.era = (Integer)confCivic.get("era");
			
			civic_maps.put(civic.id, civic);
		}
		CivLog.info("Loaded "+civic_maps.size()+" Civic Policies.");		
	}
	
	public static double eraRate(Town t) {
		double rate = 0.0;
		double era = (CivGlobal.highestCivEra-1) - t.getCiv().getCurrentEra();
		if (era > 0) {
			rate = (era/10);
		}
		return rate;
	}
	
/*	public double getAdjustedBeakerCost(Civilization civ) {
		double rate = 1.0;
		rate -= eraRate(civ);
		return Math.floor(this.beaker_cost*Math.max(rate, .01));
	}
	
	public double getAdjustedTechCost(Civilization civ) {
		double rate = 1.0;
		
//		for (Town town : civ.getTowns()) {
//			if (town.getBuffManager().hasBuff("buff_profit_sharing")) {
//				rate -= town.getBuffManager().getEffectiveDouble("buff_profit_sharing");
//			}
//		}
		rate = Math.max(rate, 0.75);
		rate -= eraRate(civ);
		
		return Math.floor(this.cost * Math.max(rate, .01));
	}*/
	
	
	public static ArrayList<ConfigCivic> getAvailableCivics(Town t) {
		ArrayList<ConfigCivic> returnCivics = new ArrayList<ConfigCivic>();
		for (ConfigCivic civic : CivSettings.civics.values()) {
			if (!t.hasCivic(civic.id)) {
				if (civic.isAvailable(t)) {
					returnCivics.add(civic);
				}
			}
		}
		return returnCivics;
	}
	
	public boolean isAvailable(Town t) {
		if (CivGlobal.testFileFlag("debug-norequire")) {
			CivMessage.global("Ignoring requirements! debug-norequire found.");
			return true;
		}
		
		if (require_civics == null || require_civics.equals("")) {
			return true;			
		}
		
		if (require_techs == null || require_techs.equals("")) {
			return true;			
		}
		
		String[] requireCivics = require_civics.split(":");
		for (String reqCivic : requireCivics) {
			if (!t.hasCivic(reqCivic)) {
				return false;
			}
		}
		
		String[] requireTechs = require_techs.split(":");
		for (String reqTech : requireTechs) {
			if (!t.getCiv().hasRequiredTech(reqTech)) {
				return false;
			}
		}
		return true;
	}
}
