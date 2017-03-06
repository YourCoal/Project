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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.configuration.file.FileConfiguration;

import com.avrgaming.civcraft.main.CivLog;

public class ConfigMineLevel {
	
	public int level;
	public Map<Integer, Integer> consumes;
	public int count;
	public double production;
	public int item_hammers = 32;
	
	public ConfigMineLevel() {
	}
	
	public ConfigMineLevel(ConfigMineLevel currentlvl) {
		this.level = currentlvl.level;
		this.count = currentlvl.count;
		this.production = currentlvl.production;
		this.consumes = new HashMap<Integer, Integer>();
		for (Entry<Integer, Integer> entry : currentlvl.consumes.entrySet()) {
			this.consumes.put(entry.getKey(), entry.getValue());
		}
	}
	
	public static void loadConfig(FileConfiguration cfg, Map<Integer, ConfigMineLevel> mine_levels) {
		mine_levels.clear();
		List<Map<?, ?>> mine_list = cfg.getMapList("mine_levels");
		Map<Integer, Integer> consumes_list = null;
		for (Map<?,?> cl : mine_list ) {
			List<?> consumes = (List<?>)cl.get("consumes");
			if (consumes != null) {
				consumes_list = new HashMap<Integer, Integer>();
				for (int i = 0; i < consumes.size(); i++) {
					String line = (String) consumes.get(i);
					String split[];
					split = line.split(",");
					consumes_list.put(Integer.valueOf(split[0]), Integer.valueOf(split[1]));
				}
			}
			
			ConfigMineLevel minelevel = new ConfigMineLevel();
			minelevel.level = (Integer)cl.get("level");
			minelevel.consumes = consumes_list;
			minelevel.count = (Integer)cl.get("count");
			minelevel.production = (Double)cl.get("production");
			mine_levels.put(minelevel.level, minelevel);
		}
		CivLog.info("Loaded "+mine_levels.size()+" Mine Levels.");		
	}
}