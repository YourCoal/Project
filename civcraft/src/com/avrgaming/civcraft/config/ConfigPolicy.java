//Added 1.1pre4 6/25/2016
package com.avrgaming.civcraft.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;

import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.main.CivLog;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.Civilization;

public class ConfigPolicy {
	public String id;
	public String name;
	public double beaker_cost;
	public double cost;
	public Integer points;
	public String tree;
	public String required_policies;
	public static Integer required_era;
	
	public static void loadConfig(FileConfiguration cfg, Map<String, ConfigPolicy> tech_maps) {
		tech_maps.clear();
		List<Map<?, ?>> policies = cfg.getMapList("policies");
		for (Map<?, ?> confPol : policies) {
			ConfigPolicy pol = new ConfigPolicy();
			
			pol.id = (String)confPol.get("id");
			pol.name = (String)confPol.get("name");
			pol.beaker_cost = (Double)confPol.get("beaker_cost");
			pol.cost = (Double)confPol.get("cost");
			pol.points = (Integer)confPol.get("points");
			pol.tree = (String)confPol.get("tree");
			
			pol.required_policies = (String)confPol.get("required_policies");
			ConfigPolicy.required_era = (Integer)confPol.get("required_era");
			
			tech_maps.put(pol.id, pol);
		}
		CivLog.info("Loaded "+tech_maps.size()+" social policies.");
	}
	
	public static boolean hasRequiredEra(Civilization civ) {
		if (civ.getCurrentEra() < required_era) {
			return false;
		} else {
			return true;
		}
	}
	
	public double getAdjustedBeakerCost(Civilization civ) {
		double rate = 1.0;
		return Math.floor(this.beaker_cost*Math.max(rate, .01));
	}
	
	public double getAdjustedPolicyCost(Civilization civ) {
		double rate = 1.0;
		
//		for (Town town : civ.getTowns()) {
//			if (town.getBuffManager().hasBuff("buff_profit_sharing")) {
//				rate -= town.getBuffManager().getEffectiveDouble("buff_profit_sharing");
//			}
//		}
		rate = Math.max(rate, 0.75);
		return Math.floor(this.cost * Math.max(rate, .01));
	}
	
	public static ArrayList<ConfigPolicy> getAvailablePolicies(Civilization civ) {
		ArrayList<ConfigPolicy> returnPols = new ArrayList<ConfigPolicy>();
		for (ConfigPolicy pol : CivSettings.policies.values()) {
			if (!civ.hasPolicy(pol.id)) {
				if (pol.isAvailable(civ)) {
					returnPols.add(pol);
				}
			}
		}
		return returnPols;
	}
	
	public boolean isAvailable(Civilization civ) {
		if (CivGlobal.testFileFlag("debug-norequire")) {
			CivMessage.global("Ignoring requirements! debug-norequire found.");
			return true;
		}
		
		if (required_policies == null || required_policies.equals("")) {
			return true;			
		}
		
		if (!hasRequiredEra(civ)) {
			return false;
		}
		
		String[] requirePols = required_policies.split(":");
		for (String reqPol : requirePols) {
			if (!civ.hasPolicy(reqPol)) {
				return false;
			}
		}
		return true;
	}
}
