/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.config;

import java.util.List;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;

import com.civcraft.main.CivLog;
import com.civcraft.util.CivColor;

public class ConfigBuff {
	public String id;
	public String name;
	public String description;
	public String value;
	public boolean stackable;
	public String parent;
	
	public static void loadConfig(FileConfiguration cfg, Map<String, ConfigBuff> buffs){
		buffs.clear();
		List<Map<?, ?>> configBuffs = cfg.getMapList("buffs");
		for (Map<?, ?> b : configBuffs) {
			ConfigBuff buff = new ConfigBuff();
			buff.id = (String)b.get("id");
			buff.name = (String)b.get("name");
			
			buff.description = (String)b.get("description");
			buff.description = CivColor.colorize(buff.description);
			
			buff.value = (String)b.get("value");
			buff.stackable = (Boolean)b.get("stackable");
			buff.parent = (String)b.get("parent");
			
			if (buff.parent == null) {
				buff.parent = buff.id;
			}
			
			buffs.put(buff.id, buff);
		}
		
		CivLog.info("Loaded "+buffs.size()+" Buffs.");
	}	
}
