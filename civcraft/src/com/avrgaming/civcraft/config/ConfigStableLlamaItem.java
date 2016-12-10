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
import java.util.Set;

import org.bukkit.configuration.file.FileConfiguration;

import com.avrgaming.civcraft.main.CivLog;

public class ConfigStableLlamaItem {
	public String name;
	public double cost;
	public int store_id;
	public int item_id;
	public int item_data;
	public int llama_id;
	
	public static void loadConfig(FileConfiguration cfg, Set<ConfigStableLlamaItem> items) {
		items.clear();
		List<Map<?, ?>> cfg_items = cfg.getMapList("llama_stable_items");
		for (Map<?, ?> level : cfg_items) {
			ConfigStableLlamaItem itm = new ConfigStableLlamaItem();
			itm.name = (String)level.get("name");
			itm.cost = (Double)level.get("cost");
			itm.store_id = (Integer)level.get("store_id");
			itm.item_id = (Integer)level.get("item_id");
			itm.item_data = (Integer)level.get("item_data");
			itm.llama_id = (Integer)level.get("llama_id");
			items.add(itm);
		}
		CivLog.info("Loaded "+items.size()+" Llama Stable Items.");
	}
	
}
