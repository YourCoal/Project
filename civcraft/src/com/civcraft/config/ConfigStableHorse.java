/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.config;

import java.util.List;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;

import com.civcraft.main.CivLog;

public class ConfigStableHorse {
	public int id;
	public double speed;
	public double jump;
	public double health;
	public boolean mule;
	public String variant;
	
	public static void loadConfig(FileConfiguration cfg, Map<Integer, ConfigStableHorse> horses) {
		horses.clear();
		List<Map<?, ?>> config_horses = cfg.getMapList("stable_horses");
		for (Map<?, ?> level : config_horses) {
			ConfigStableHorse horse = new ConfigStableHorse();
			horse.id = (Integer)level.get("id");
			horse.speed = (Double)level.get("speed");
			horse.jump = (Double)level.get("jump");
			horse.health = (Double)level.get("health");
			horse.variant = (String)level.get("variant");
			
			Boolean mule = (Boolean)level.get("mule");
			if (mule == null || mule == false) {
				horse.mule = false;
			} else {
				horse.mule = true;
			}
			
			horses.put(horse.id, horse);
		}
		CivLog.info("Loaded "+horses.size()+" Horses.");
	}
	
}
