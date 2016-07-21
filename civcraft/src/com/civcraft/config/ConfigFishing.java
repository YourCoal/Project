/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;

import com.civcraft.main.CivLog;

public class ConfigFishing {
	public String craftMatId;
	public int type_id;	
	public double drop_chance;
	
	public static void loadConfig(FileConfiguration cfg, ArrayList<ConfigFishing> configList) {
		  configList.clear();
			  List<Map<?, ?>> drops = cfg.getMapList("fishing_drops");
			  for (Map<?, ?> item : drops) {
			   ConfigFishing g = new ConfigFishing();
			   
			   g.craftMatId = (String)item.get("craftMatId");
			   g.type_id = (Integer)item.get("type_id");
			   g.drop_chance = (Double)item.get("drop_chance");
			   
			   configList.add(g);
			   
			  }
		  CivLog.info("Loaded "+configList.size()+" fishing drops.");  
		  
	}

}


