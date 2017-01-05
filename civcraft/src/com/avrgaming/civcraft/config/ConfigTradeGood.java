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

import org.bukkit.configuration.file.FileConfiguration;

import com.avrgaming.civcraft.main.CivLog;

public class ConfigTradeGood implements Comparable<ConfigTradeGood> {
	public String id;
	public String name;
	public String type;
	public double value;
	public double culture;
	public int food;
	public HashMap<String, ConfigBuff> buffs = new HashMap<String, ConfigBuff>();
	public int material;
	public int material_data;
	public String hemiString = null;
	public boolean water;
	public Double rarity = null;
	
	
	public static void loadBuffsString(ConfigTradeGood good, String bonus) {
		String[] keys = bonus.split(",");
		
		for (String key : keys) {
			ConfigBuff cBuff = CivSettings.buffs.get(key.replace(" ", ""));
			if (cBuff != null) {
				good.buffs.put(key, cBuff);
			}
		}
		
	}
	
	public static void loadConfig(FileConfiguration cfg, Map<String, ConfigTradeGood> goods, Map<String, ConfigTradeGood> landGoods, Map<String, ConfigTradeGood> waterGoods) {
		goods.clear();
		List<Map<?, ?>> land_goods = cfg.getMapList("land_goods");
		for (Map<?, ?> g : land_goods) {
			ConfigTradeGood good = new ConfigTradeGood();
			good.id = (String)g.get("id");
			good.name = (String)g.get("name");
			good.type = (String)g.get("type");
			good.value = (Double)g.get("value");
//			good.culture = (Double)g.get("culture");
//			good.food = (Integer)g.get("food");
			loadBuffsString(good, (String)g.get("buffs"));
			good.material = (Integer)g.get("material");
			good.material_data = (Integer)g.get("material_data");
			good.hemiString = ((String)g.get("hemispheres"));
			good.water = false;
			good.rarity = ((Double)g.get("rarity"));
			if (good.rarity == null) {
				good.rarity = 1.0;
			}
			landGoods.put(good.id, good);
			goods.put(good.id, good);
		}
		
		List<Map<?, ?>> water_goods = cfg.getMapList("water_goods");
		for (Map<?, ?> g : water_goods) {
			ConfigTradeGood good = new ConfigTradeGood();
			good.id = (String)g.get("id");
			good.name = (String)g.get("name");
			good.type = (String)g.get("type");
			good.value = (Double)g.get("value");
//			good.culture = (Double)g.get("culture");
//			good.food = (Integer)g.get("food");
			loadBuffsString(good, (String)g.get("buffs"));
			good.material = (Integer)g.get("material");
			good.material_data = (Integer)g.get("material_data");
			good.hemiString = ((String)g.get("hemispheres"));
			good.water = true;
			good.rarity = ((Double)g.get("rarity"));
			if (good.rarity == null) {
				good.rarity = 1.0;
			}
			waterGoods.put(good.id, good);
			goods.put(good.id, good);
		}
		CivLog.info("Loaded "+goods.size()+" Trade Goods.");
	}
	
	@Override
	public int compareTo(ConfigTradeGood otherGood) {
		if (this.rarity < otherGood.rarity) {
			// A lower rarity should go first.
			return 1;
		} else if (this.rarity == otherGood.rarity) {
			return 0;
		}
		return -1;
	}
}
