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

public class ConfigMonumentLevel {
	
	public int level;
	public Map<Integer, Integer> consumes;
	public int count;
	public double culture;
	
	public ConfigMonumentLevel() {
	}
	
	public ConfigMonumentLevel(ConfigMonumentLevel currentlvl) {
		this.level = currentlvl.level;
		this.count = currentlvl.count;
		this.culture = currentlvl.culture;
		this.consumes = new HashMap<Integer, Integer>();
		for (Entry<Integer, Integer> entry : currentlvl.consumes.entrySet()) {
			this.consumes.put(entry.getKey(), entry.getValue());
		}
	}
	
	public static void loadConfig(FileConfiguration cfg, Map<Integer, ConfigMonumentLevel> monument_levels) {
		monument_levels.clear();
		List<Map<?, ?>> monument_list = cfg.getMapList("monument_levels");
		Map<Integer, Integer> consumes_list = null;
		for (Map<?,?> cl : monument_list ) {
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
			
			ConfigMonumentLevel monumentlevel = new ConfigMonumentLevel();
			monumentlevel.level = (Integer)cl.get("level");
			monumentlevel.consumes = consumes_list;
			monumentlevel.count = (Integer)cl.get("count");
			monumentlevel.culture = (Double)cl.get("culture");
			monument_levels.put(monumentlevel.level, monumentlevel);
		}
		CivLog.info("Loaded "+monument_levels.size()+" Monument Levels.");		
	}
}