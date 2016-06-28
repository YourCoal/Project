package com.avrgaming.civcraft.config;

import java.util.List;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;

import com.avrgaming.civcraft.main.CivLog;

public class ConfigCitizenLevel {
	public int level;
	public int amount;
	public String displayName;
	public double resExchangeRate;
	
	public static void loadConfig(FileConfiguration cfg, Map<Integer, ConfigCitizenLevel> levels) {
		levels.clear();
		List<Map<?, ?>> citizen_levels = cfg.getMapList("citizen_levels");
		for (Map<?, ?> level : citizen_levels) {
			ConfigCitizenLevel citizen_level = new ConfigCitizenLevel();
			citizen_level.level = (Integer)level.get("level");
			citizen_level.amount = (Integer)level.get("amount");
			citizen_level.displayName = (String)level.get("displayName");
			citizen_level.resExchangeRate = (Double)level.get("resExchangeRate");
			levels.put(citizen_level.level, citizen_level);
		}
		CivLog.info("Loaded "+levels.size()+" citizen levels.");
	}
}
