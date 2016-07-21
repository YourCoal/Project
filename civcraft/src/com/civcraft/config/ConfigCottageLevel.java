/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.configuration.file.FileConfiguration;

import com.civcraft.main.CivLog;

public class ConfigCottageLevel {
	public int level;			/* Current level number */
	public Map<Integer, Integer> consumes; /* A map of block ID's and amounts required for this level to progress */
	public int count; /* Number of times that consumes must be met to level up */
	public double coins; /* Coins generated each time for the cottage */
	
	public ConfigCottageLevel() {
		
	}
	
	public ConfigCottageLevel(ConfigCottageLevel currentlvl) {
		this.level = currentlvl.level;
		this.count = currentlvl.count;
		this.coins = currentlvl.coins;
		
		this.consumes = new HashMap<Integer, Integer>();
		for (Entry<Integer, Integer> entry : currentlvl.consumes.entrySet()) {
			this.consumes.put(entry.getKey(), entry.getValue());
		}
		
	}


	public static void loadConfig(FileConfiguration cfg, Map<Integer, ConfigCottageLevel> cottage_levels) {
		cottage_levels.clear();
		List<Map<?, ?>> cottage_list = cfg.getMapList("cottage_levels");
		Map<Integer, Integer> consumes_list = null;
		for (Map<?,?> cl : cottage_list ) {
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
			
			
			ConfigCottageLevel cottagelevel = new ConfigCottageLevel();
			cottagelevel.level = (Integer)cl.get("level");
			cottagelevel.consumes = consumes_list;
			cottagelevel.count = (Integer)cl.get("count");
			cottagelevel.coins = (Double)cl.get("coins");
			
			cottage_levels.put(cottagelevel.level, cottagelevel);
			
		}
		CivLog.info("Loaded "+cottage_levels.size()+" cottage levels.");		
	}
	
}
