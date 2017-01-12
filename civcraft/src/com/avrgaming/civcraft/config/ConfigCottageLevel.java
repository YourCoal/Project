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
package com.avrgaming.civcraft.config;

import java.util.List;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;

import com.avrgaming.civcraft.main.CivLog;

public class ConfigCottageLevel {
	
	public int level;	/* Current level number */
	public int amount; /* Number of food this cottage consumes */
	public double coins; /* coins generated each time hour */
	
	public static void loadConfig(FileConfiguration cfg, Map<Integer, ConfigCottageLevel> levels) {
		levels.clear();
		List<Map<?, ?>> cottage_levels = cfg.getMapList("cottage_levels");
		for (Map<?, ?> level : cottage_levels) {
			ConfigCottageLevel cottage_level = new ConfigCottageLevel();
			cottage_level.level = (Integer)level.get("level");
			cottage_level.amount = (Integer)level.get("amount");
			cottage_level.coins = (Double)level.get("coins");
			levels.put(cottage_level.level, cottage_level);
		}
		CivLog.info("Loaded "+levels.size()+" Cottage Levels.");
	}
}
