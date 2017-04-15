package com.avrgaming.civcraft.config;

import java.util.List;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;

import com.avrgaming.civcraft.main.CivLog;

public class ConfigCivicItem {
	
	public int id;
	public String name;
	public String require_civic;
	
	public static void loadConfig(FileConfiguration cfg, Map<Integer, ConfigCivicItem> tech_maps) {
		tech_maps.clear();
		List<Map<?, ?>> techs = cfg.getMapList("items");
		for (Map<?, ?> confCivic : techs) {
			ConfigCivicItem tech = new ConfigCivicItem();
			tech.id = (Integer)confCivic.get("id");
			tech.name = (String)confCivic.get("name");
			tech.require_civic = (String)confCivic.get("require_civic");			
			tech_maps.put(tech.id, tech);
		}
		CivLog.info("Loaded "+tech_maps.size()+" Civics (Required Items).");		
	}
}
