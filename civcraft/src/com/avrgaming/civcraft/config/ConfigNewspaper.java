package com.avrgaming.civcraft.config;

import java.util.List;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;

import com.avrgaming.civcraft.main.CivLog;

public class ConfigNewspaper {
	
	public String id;
	public Integer item;
	public Integer iData;
	public Integer guiData;
	public String headline;
	public String lineotd;
	public String date;
	
//	public String line;
	public String line1;
	public String line2;
	public String line3;
	public String line4;
	public String line5;
	public String line6;
	public String line7;
	public String line8;
	public String line9;
	
	public ConfigNewspaper() {
	}
	
	public ConfigNewspaper(ConfigNewspaper newspaper) {
		this.id = newspaper.id;
		this.item = newspaper.item;
		this.iData = newspaper.iData;
		this.guiData = newspaper.guiData;
		this.headline = newspaper.headline;
		this.lineotd = newspaper.lineotd;
		this.date = newspaper.date;
		
//		this.line = newspaper.line;
		this.line2 = newspaper.line1;
		this.line2 = newspaper.line2;
		this.line3 = newspaper.line3;
		this.line4 = newspaper.line4;
		this.line5 = newspaper.line5;
		this.line6 = newspaper.line6;
		this.line7 = newspaper.line7;
		this.line8 = newspaper.line8;
		this.line9 = newspaper.line9;
	}
	
	public static void loadConfig(FileConfiguration cfg, Map<String, ConfigNewspaper> newspapers) {
		newspapers.clear();
		List<Map<?, ?>> confignewspapers = cfg.getMapList("newspaper");
		for (Map<?, ?> b : confignewspapers) {
			ConfigNewspaper newspaper = new ConfigNewspaper();
			newspaper.id = (String)b.get("id");
			newspaper.item = (Integer)b.get("item");
			newspaper.iData = (Integer)b.get("iData");
			newspaper.guiData = (Integer)b.get("guiData");
			newspaper.headline = (String)b.get("headline");
			newspaper.lineotd = (String)b.get("lineotd");
			newspaper.date = (String)b.get("date");
			
//			newspaper.line = (String)b.get("line");
			newspaper.line1 = (String)b.get("line1");
			newspaper.line2 = (String)b.get("line2");
			newspaper.line3 = (String)b.get("line3");
			newspaper.line4 = (String)b.get("line4");
			newspaper.line5 = (String)b.get("line5");
			newspaper.line6 = (String)b.get("line6");
			newspaper.line7 = (String)b.get("line7");
			newspaper.line8 = (String)b.get("line8");
			newspaper.line9 = (String)b.get("line9");
			
			newspapers.put(newspaper.id.toLowerCase(), newspaper);
		}
		CivLog.info("Loaded "+newspapers.size()+" Newspaper(s).");
	}
}
