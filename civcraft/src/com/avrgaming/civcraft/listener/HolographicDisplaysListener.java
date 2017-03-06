package com.avrgaming.civcraft.listener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.avrgaming.civcraft.config.CivSettings;
import com.avrgaming.civcraft.main.CivCraft;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.main.CivLog;
import com.avrgaming.civcraft.object.StructureSign;
import com.avrgaming.civcraft.object.TradeGood;
import com.avrgaming.civcraft.structure.Bank;
import com.avrgaming.civcraft.structure.Structure;
import com.avrgaming.civcraft.util.BlockCoord;
import com.avrgaming.civcraft.util.CivColor;
import com.gmail.filoghost.holograms.api.HolographicDisplaysAPI;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

@SuppressWarnings("deprecation")
public class HolographicDisplaysListener {
	
	public static void generateTradeGoodHolograms() {
		if (CivSettings.hasHolographicDisplays == false) {
			CivLog.warning("A person tried generating Trade Good Holograms without HolographicDisplays plugin! This is fine, but no holograms can generate for items.");
		}
		
		Plugin p = CivCraft.getPlugin();
		CivLog.info(HolographicDisplaysAPI.getHolograms(p).length+" Trade Good Holograms deleted.");
		for (com.gmail.filoghost.holograms.api.Hologram hologram : HolographicDisplaysAPI.getHolograms(CivCraft.getPlugin())) {
			hologram.delete();
		}
		
		for (TradeGood good : CivGlobal.getTradeGoods()) {
			BlockCoord coord = good.getCoord();
			int cX = coord.getBlock().getChunk().getX()*16;
			int cZ = coord.getBlock().getChunk().getZ()*16;
			
			Location loc = new Location(coord.getBlock().getWorld(), cX+1.5, coord.getBlock().getY()+6.25, cZ+3.5);
			Hologram hologram = HologramsAPI.createHologram(p, loc);
			if (good.getInfo().water == true) {
				hologram.appendItemLine(new ItemStack(good.getInfo().material, 1, (short)good.getInfo().material_data));
				hologram.appendTextLine(CivColor.GoldBold+"Trade Good: "+CivColor.LightBlueBold+CivColor.ITALIC+good.getInfo().name);
				hologram.appendTextLine(CivColor.GoldBold+"Value: "+CivColor.YellowBold+CivColor.ITALIC+good.getInfo().value+" Coins");
//				hologram.appendTextLine(CivColor.GoldBold+"Culture: "+CivColor.YellowBold+CivColor.ITALIC+"n/a"
//										+CivColor.GrayBold+CivColor.BOLD+" | Food: "+CivColor.YellowBold+CivColor.ITALIC+"n/a");
				hologram.appendTextLine(CivColor.GoldBold+"Culture: "+CivColor.YellowBold+CivColor.ITALIC+good.getInfo().culture
										+CivColor.GoldBold+" | Food: "+CivColor.YellowBold+CivColor.ITALIC+good.getInfo().food);
			} else {
				hologram.appendItemLine(new ItemStack(good.getInfo().material, 1, (short)good.getInfo().material_data));
				hologram.appendTextLine(CivColor.GoldBold+"Trade Good: "+CivColor.LightGreenBold+CivColor.ITALIC+good.getInfo().name);
				hologram.appendTextLine(CivColor.GoldBold+"Value: "+CivColor.YellowBold+CivColor.ITALIC+good.getInfo().value+" Coins");
//				hologram.appendTextLine(CivColor.GoldBold+"Culture: "+CivColor.YellowBold+CivColor.ITALIC+"n/a"
//										+CivColor.GoldBold+" | Food: "+CivColor.YellowBold+CivColor.ITALIC+"n/a");
				hologram.appendTextLine(CivColor.GoldBold+"Culture: "+CivColor.YellowBold+CivColor.ITALIC+good.getInfo().culture
										+CivColor.GoldBold+" | Food: "+CivColor.YellowBold+CivColor.ITALIC+good.getInfo().food);
			}
		}
		CivLog.info(CivGlobal.getTradeGoods().size()+" Trade Good Holograms created.");
	}
	
	public static void generateBankHolograms() {
		if (CivSettings.hasHolographicDisplays == false) {
			CivLog.warning("A person tried generating Bank Holograms without HolographicDisplays plugin! This is fine, but no holograms can generate for items.");
		}
		
		Plugin p = CivCraft.getPlugin();
		CivLog.info(HolographicDisplaysAPI.getHolograms(p).length+" Bank Holograms deleted.");
		for (com.gmail.filoghost.holograms.api.Hologram hologram : HolographicDisplaysAPI.getHolograms(CivCraft.getPlugin())) {
			hologram.delete();
		}
		
		
		for (Structure s : CivGlobal.getStructures()) {
			if (s instanceof Bank) {
				Bank b = (Bank)s;
				for (StructureSign ss : b.getSigns()) {
					switch (ss.getAction().toLowerCase()) {
					case "iron":
						break;
					case "gold":
						BlockCoord coord = ss.getCoord();
						Location loc = new Location(coord.getBlock().getWorld(), coord.getX(), coord.getBlock().getY()+4, coord.getZ());
						Hologram hologram = HologramsAPI.createHologram(p, loc);
						hologram.appendItemLine(new ItemStack(Material.NETHER_STAR, 1));
						hologram.appendTextLine(CivColor.GoldBold+"Bank Level: "+CivColor.LightGreenBold+b.getLevel());
						hologram.appendTextLine(CivColor.GoldBold+"Exchange Rate: "+CivColor.LightGreenBold+b.getBankExchangeRate()*100+"%");
						hologram.appendTextLine(CivColor.GoldBold+"Non-Resident Fee: "+CivColor.LightGreenBold+b.getNonResidentFee()*100+"%");
						break;
					case "diamond":
						break;			
					case "emerald":
							break;
					}
				}
			}
		}
		CivLog.info(CivGlobal.getTradeGoods().size()+" Bank Holograms created.");
	}
	
	
/*	public void tradeOutpostHolo(Plugin p, TradeGood tg, World w, double x, double y, double z) {
		Location loc = new Location(w, x, y ,z);
		Hologram hologram = HologramsAPI.createHologram(p, loc);
		if (tg.getInfo().water == true) {
			hologram.appendItemLine(new ItemStack(tg.getInfo().material, 1, (short)tg.getInfo().material_data));
			hologram.appendTextLine(CivColor.GoldBold+"Trade Good: "+CivColor.LightBlueBold+CivColor.ITALIC+tg.getInfo().name);
			hologram.appendTextLine(CivColor.GoldBold+"Value: "+CivColor.YellowBold+CivColor.ITALIC+tg.getInfo().value+" Coins");
			hologram.appendTextLine(CivColor.GoldBold+"Culture: "+CivColor.YellowBold+CivColor.ITALIC+"n/a"
									+CivColor.GoldBold+" | Food: "+CivColor.YellowBold+CivColor.ITALIC+"n/a");
//			hologram.appendTextLine(CivColor.GoldBold+"Culture: "+CivColor.YellowBold+CivColor.ITALIC+tg.getInfo().culture
//									+CivColor.GoldBold+" | Food:"+CivColor.YellowBold+CivColor.ITALIC+tg.getInfo().food);
		} else {
			hologram.appendItemLine(new ItemStack(tg.getInfo().material, 1, (short)tg.getInfo().material_data));
			hologram.appendTextLine(CivColor.GoldBold+"Trade Good: "+CivColor.LightGreenBold+CivColor.ITALIC+tg.getInfo().name);
			hologram.appendTextLine(CivColor.GoldBold+"Value: "+CivColor.YellowBold+CivColor.ITALIC+tg.getInfo().value+" Coins");
			hologram.appendTextLine(CivColor.GoldBold+"Culture: "+CivColor.YellowBold+CivColor.ITALIC+"n/a"
									+CivColor.GoldBold+" | Food: "+CivColor.YellowBold+CivColor.ITALIC+"n/a");
//			hologram.appendTextLine(CivColor.GoldBold+"Culture: "+CivColor.YellowBold+CivColor.ITALIC+tg.getInfo().culture
//									+CivColor.GoldBold+" | Food:"+CivColor.YellowBold+CivColor.ITALIC+tg.getInfo().food);
		}
	}*/
}
