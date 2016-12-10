package com.avrgaming.civcraft.config;

import java.util.List;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;

import com.avrgaming.civcraft.main.CivLog;

public class ConfigStableLlama {
	public int id;
	public double speed;
	public double jump;
	public double health;
	public int chestsize;
	public boolean other;
	public String variant;
	
	public static void loadConfig(FileConfiguration cfg, Map<Integer, ConfigStableLlama> horses) {
		horses.clear();
		List<Map<?, ?>> config_horses = cfg.getMapList("stable_llamas");
		for (Map<?, ?> level : config_horses) {
			ConfigStableLlama horse = new ConfigStableLlama();
			horse.id = (Integer)level.get("id");
			horse.speed = (Double)level.get("speed");
			horse.jump = (Double)level.get("jump");
			horse.health = (Double)level.get("health");
			horse.chestsize = (Integer)level.get("chestsize");
			horse.variant = (String)level.get("variant");
			
			Boolean other = (Boolean)level.get("other");
			if (other == null || other == false) {
				horse.other = false;
			} else {
				horse.other = true;
			}
			horses.put(horse.id, horse);
		}
		CivLog.info("Loaded "+horses.size()+" Horses.");
	}
	
}
