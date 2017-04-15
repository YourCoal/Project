package com.avrgaming.civcraft.endgame;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.configuration.file.FileConfiguration;

import com.avrgaming.civcraft.main.CivLog;

public class ConfigEndCondition {
	public String id;
	public String  className;
	public String victoryName;
	public Integer days_held;
	public Integer victory_points;
	
	public HashMap<String, String> attributes = new HashMap<String, String>();
	
	public static void loadConfig(FileConfiguration cfg, Map<String, ConfigEndCondition> endconditionMap) {
		endconditionMap.clear();
		List<Map<?, ?>> perks = cfg.getMapList("end_conditions");
		for (Map<?, ?> obj : perks) {
			ConfigEndCondition p = new ConfigEndCondition();
			
			p.id = (String)obj.get("id");
			p.className = (String)obj.get("class");
			p.victoryName = (String)obj.get("name");
			p.days_held = (Integer)obj.get("days_held");
			p.victory_points = (Integer)obj.get("victory_points");
			
			for (Entry<?, ?> entry : obj.entrySet()) {
				if (entry.getKey() instanceof String && entry.getValue() instanceof String) {
					p.attributes.put((String)entry.getKey(), (String)entry.getValue());
				}
			}
			endconditionMap.put(p.id, p);
		}
		CivLog.info("Loaded "+endconditionMap.size()+" End Conditions.");		
	}
}
