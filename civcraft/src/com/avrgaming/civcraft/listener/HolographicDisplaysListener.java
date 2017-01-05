package com.avrgaming.civcraft.listener;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.avrgaming.civcraft.main.CivCraft;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.main.CivLog;
import com.avrgaming.civcraft.object.TradeGood;
import com.avrgaming.civcraft.util.BlockCoord;
import com.avrgaming.civcraft.util.CivColor;
import com.gmail.filoghost.holograms.api.HolographicDisplaysAPI;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

@SuppressWarnings("deprecation")
public class HolographicDisplaysListener {
	
	public static void generateTradeGoodHolograms() {
		Plugin p = CivCraft.getPlugin();
		for (com.gmail.filoghost.holograms.api.Hologram hologram : HolographicDisplaysAPI.getHolograms(CivCraft.getPlugin())) {
			hologram.delete();
		}
		CivLog.info(HolographicDisplaysAPI.getHolograms(p).length+" Trade Good Holograms deleted.");
		
		for (TradeGood good : CivGlobal.getTradeGoods()) {
			BlockCoord coord = good.getCoord();
			Location loc = new Location(coord.getBlock().getWorld(), coord.getBlock().getX()+0.5, coord.getBlock().getY()+5, coord.getBlock().getZ()+0.5);
			Hologram hologram = HologramsAPI.createHologram(p, loc);
			hologram.appendItemLine(new ItemStack(good.getInfo().material, 1, (short)good.getInfo().material_data));
			hologram.appendTextLine(CivColor.BoldGold+"Trade Resource:");
			hologram.appendTextLine(CivColor.BoldLightPurple+good.getInfo().name);
		}
		CivLog.info(CivGlobal.getTradeGoods().size()+" Trade Good Holograms created.");
	}
}
