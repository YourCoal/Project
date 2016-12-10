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
package com.avrgaming.civcraft.command.town;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.avrgaming.civcraft.command.CommandBase;
import com.avrgaming.civcraft.config.CivSettings;
import com.avrgaming.civcraft.config.ConfigCultureLevel;
import com.avrgaming.civcraft.config.ConfigHappinessState;
import com.avrgaming.civcraft.config.ConfigTownLevel;
import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.exception.InvalidConfiguration;
import com.avrgaming.civcraft.items.BonusGoodie;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.AttrSource;
import com.avrgaming.civcraft.object.Buff;
import com.avrgaming.civcraft.object.Civilization;
import com.avrgaming.civcraft.object.CultureChunk;
import com.avrgaming.civcraft.object.Relation;
import com.avrgaming.civcraft.object.Relation.Status;
import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.object.Town;
import com.avrgaming.civcraft.object.TradeGood;
import com.avrgaming.civcraft.structure.Bank;
import com.avrgaming.civcraft.structure.Buildable;
import com.avrgaming.civcraft.structure.Cottage;
import com.avrgaming.civcraft.structure.Mine;
import com.avrgaming.civcraft.structure.Structure;
import com.avrgaming.civcraft.structure.TownHall;
import com.avrgaming.civcraft.structure.wonders.Wonder;
import com.avrgaming.civcraft.util.CivColor;

public class TownInfoCommand extends CommandBase {

	@Override
	public void init() {
		command = "/town info";
		displayName = "Town Info";
		
		commands.put("upkeep", "Shows town upkeep information.");
		commands.put("cottage", "Shows cottage information for town.");
		commands.put("structures", "Shows upkeep information related to structures.");
		commands.put("culture", "Shows culture information for town.");
		commands.put("trade", "Shows town trade good information.");
		commands.put("mine", "Shows mine production information.");
		commands.put("hammers", "Shows town hammer information.");
		commands.put("goodies", "Shows which goodies are being used by the town.");
		commands.put("rates", "Shows the culture,growth,trade and cottage rates of this town.");
		commands.put("growth", "Shows growth info about the town.");
		commands.put("buffs", "Show all special buffs awarded to this town.");
		commands.put("online", "Shows a list of town members that are currently online.");
		commands.put("happiness", "Shows information about this town's happiness.");
		commands.put("beakers", "Shows information about this town's beakers");
		commands.put("area", "Shows the various attributes generated by culture chunks.");
		commands.put("disabled", "Shows information about disabled structures.");
	}

	public void disabled_cmd() throws CivException {
		Town town = getSelectedTown();
		
		CivMessage.sendHeading(sender, "Disabled Structures");
		LinkedList<String> out = new LinkedList<String>();
		boolean showhelp = false;
		
		for (Buildable buildable : town.getDisabledBuildables()) {
			showhelp = true;
			out.add(CivColor.Green+buildable.getDisplayName()+CivColor.LightGreen+" Coord:"+buildable.getCorner().toString());
		}
		
		if (showhelp)  {
			out.add(CivColor.LightGray+"These structures have been disabled in this town since they've exceeded the structure limit.");
			out.add(CivColor.LightGray+"To enable them, you must do one of the following:");
			out.add(CivColor.LightGray+"1) Move this structure to another town using: /town movestructure <coord>");
			out.add(CivColor.LightGray+"2) Demolish this structure with /build demolish <coord> or /build demolishnearest.");
			out.add(CivColor.LightGray+"3) Move other structures of the same type to another town, or demolish them, and issue /town enablestructure <coord>");
		}
		
		CivMessage.send(sender, out);
	}
	
	public void area_cmd() throws CivException {
		Town town = getSelectedTown();
		
		CivMessage.sendHeading(sender, "Area Info");
		HashMap<String, Integer> biomes = new HashMap<String, Integer>();
		
		double hammers = 0.0;
		double growth = 0.0;
		double happiness = 0.0;
		double beakers = 0.0;
		DecimalFormat df = new DecimalFormat();
		
		for (CultureChunk cc : town.getCultureChunks()) {
			/* Increment biome counts. */
			if (!biomes.containsKey(cc.getBiome().name())) {
				biomes.put(cc.getBiome().name(), 1);
			} else {
				Integer value = biomes.get(cc.getBiome().name());
				biomes.put(cc.getBiome().name(), value+1);
			}
			
			hammers += cc.getHammers();
			growth += cc.getGrowth();
			happiness += cc.getHappiness();
			beakers += cc.getBeakers();
		}
		
		CivMessage.send(sender, CivColor.LightBlue+"Biome Counts");
		String out = "";
		//int totalBiomes = 0;
		for (String biome : biomes.keySet()) {
			Integer count = biomes.get(biome);
			out += CivColor.Green+biome+": "+CivColor.LightGreen+count+CivColor.Green+", ";
		//	totalBiomes += count;
		}
		CivMessage.send(sender, out);
		
		//CivMessage.send(sender, CivColor.Green+"Biome Count:"+CivColor.LightGreen+totalBiomes);
		
		CivMessage.send(sender, CivColor.LightBlue+"Totals");
		CivMessage.send(sender, CivColor.Green+" Happiness:"+CivColor.LightGreen+df.format(happiness)+
				CivColor.Green+" Hammers:"+CivColor.LightGreen+df.format(hammers)+
				CivColor.Green+" Growth:"+CivColor.LightGreen+df.format(growth)+
				CivColor.Green+" Beakers:"+CivColor.LightGreen+df.format(beakers));
		
	}
	
	public void beakers_cmd() throws CivException {
		Town town = getSelectedTown();
		
		CivMessage.sendHeading(sender, "Beakers Info");
		
		AttrSource beakerSources = town.getBeakers();
		CivMessage.send(sender, beakerSources.getSourceDisplayString(CivColor.Green, CivColor.LightGreen));
		CivMessage.send(sender, beakerSources.getRateDisplayString(CivColor.Green, CivColor.LightGreen));
		CivMessage.send(sender, beakerSources.getTotalDisplayString(CivColor.Green, CivColor.LightGreen));
	
	}
	
	public void happiness_cmd() throws CivException {
		Town town = getSelectedTown();
		
		CivMessage.sendHeading(sender, "Happiness Info");
		ArrayList<String> out = new ArrayList<String>();

		out.add(CivMessage.buildSmallTitle("Happiness Sources"));
		AttrSource happySources = town.getHappiness();

		DecimalFormat df = new DecimalFormat();
		df.applyPattern("###,###");
		for (String source : happySources.sources.keySet()) {
			Double value = happySources.sources.get(source);
			out.add(CivColor.Green+source+": "+CivColor.LightGreen+df.format(value));
		}
		out.add(CivColor.LightPurple+"Total: "+CivColor.LightGreen+df.format(happySources.total));

		
		out.add(CivMessage.buildSmallTitle("Unhappiness Sources"));
		AttrSource unhappySources = town.getUnhappiness();
		for (String source : unhappySources.sources.keySet()) {
			Double value = unhappySources.sources.get(source);
			out.add(CivColor.Green+source+": "+CivColor.LightGreen+value);
		}
		out.add(CivColor.LightPurple+"Total: "+CivColor.LightGreen+df.format(unhappySources.total));

		out.add(CivMessage.buildSmallTitle("Total"));
		ConfigHappinessState state = town.getHappinessState();
		out.add(CivColor.LightGreen+df.format(town.getHappinessPercentage()*100)+"%"+CivColor.Green+" Happiness. State: "+CivColor.valueOf(state.color)+state.name);
		CivMessage.send(sender, out);	

		
	}
	
	public void online_cmd() throws CivException {
		Town town = getSelectedTown();
		
		CivMessage.sendHeading(sender, "Online Players In "+town.getName());
		String out = "";
		for (Resident resident : town.getOnlineResidents()) {
			out += resident.getName()+" ";
		}
		CivMessage.send(sender, out);
	}
	
	public void buffs_cmd() throws CivException {
		Town town = getSelectedTown();
		
		CivMessage.sendHeading(sender, town.getName()+" Buffs");
		ArrayList<String> out = new ArrayList<String>();

		for (Buff buff : town.getBuffManager().getAllBuffs()) {
			out.add(CivColor.Green+"Buff: "+CivColor.LightGreen+buff.getDisplayName()+CivColor.Green+" from "+CivColor.LightGreen+buff.getSource());
		}
		
		CivMessage.send(sender, out);
	}
	
	public void growth_cmd() throws CivException {
		Town town = getSelectedTown();
		AttrSource growthSources = town.getGrowth();
		
		CivMessage.sendHeading(sender, town.getName()+" Growth");
		CivMessage.send(sender, growthSources.getSourceDisplayString(CivColor.Green, CivColor.LightGreen));
		CivMessage.send(sender, growthSources.getRateDisplayString(CivColor.Green, CivColor.LightGreen));
		CivMessage.send(sender, growthSources.getTotalDisplayString(CivColor.Green, CivColor.LightGreen));
	}
	
	public void goodies_cmd() throws CivException {
		Town town = getSelectedTown();
		CivMessage.sendHeading(sender, town.getName()+" Goodies");
	//	HashSet<BonusGoodie> effectiveGoodies = town.getEffectiveBonusGoodies();
		
		for (BonusGoodie goodie : town.getBonusGoodies()) {
			CivMessage.send(sender, CivColor.LightGreen+goodie.getDisplayName());
			String goodBonuses = goodie.getBonusDisplayString();
			
			String[] split = goodBonuses.split(";");
			for (String str : split) {
				CivMessage.send(sender, "    "+CivColor.LightPurple+str);
			}
			 
		}
	}
	
	public void hammers_cmd() throws CivException {
		Town town = getSelectedTown();
		
		CivMessage.sendHeading(sender, "Hammer Info");
		AttrSource hammerSources = town.getHammers();
		
		CivMessage.send(sender, hammerSources.getSourceDisplayString(CivColor.Green, CivColor.LightGreen));
		CivMessage.send(sender, hammerSources.getRateDisplayString(CivColor.Green, CivColor.LightGreen));
		CivMessage.send(sender, hammerSources.getTotalDisplayString(CivColor.Green, CivColor.LightGreen));
	}
	
	public void culture_cmd() throws CivException {
		Town town = getSelectedTown();
		AttrSource cultureSources = town.getCulture();
		
		CivMessage.sendHeading(sender, "Culture Info");
		
		CivMessage.send(sender, cultureSources.getSourceDisplayString(CivColor.Green, CivColor.LightGreen));
		CivMessage.send(sender, cultureSources.getRateDisplayString(CivColor.Green, CivColor.LightGreen));
		CivMessage.send(sender, cultureSources.getTotalDisplayString(CivColor.Green, CivColor.LightGreen));
		
	}
	
	
	public void rates_cmd() throws CivException {
		Town town = getSelectedTown();
		
		CivMessage.sendHeading(sender, town.getName()+" Rates Summary");
		
		CivMessage.send(sender, 
				CivColor.Green+"Growth: "+CivColor.LightGreen+(town.getGrowthRate().total*100)+
				CivColor.Green+" Culture: "+CivColor.LightGreen+(town.getCulture().total*100)+
				CivColor.Green+" Cottage: "+CivColor.LightGreen+(town.getCottageRate()*100)+
				CivColor.Green+" Trade: "+CivColor.LightGreen+(town.getTradeRate()*100)+		
				CivColor.Green+" Beakers: "+CivColor.LightGreen+(town.getBeakerRate().total*100)			
				);
		
	}
	
	public void trade_cmd() throws CivException {
		Town town = getSelectedTown();
		
		ArrayList<String> out = new ArrayList<String>();
		CivMessage.sendHeading(sender, town.getName()+" Trade Good Summary");
		out.add(CivColor.Green+"Trade Mulitplier: "+CivColor.LightGreen+df.format(town.getTradeRate()));
		boolean maxedCount = false;		
		int goodMax;
		try {
			goodMax = (Integer)CivSettings.getInteger(CivSettings.goodsConfig, "trade_good_multiplier_max");
		} catch (InvalidConfiguration e) {
			e.printStackTrace();
			throw new CivException("Invalid configuration error.");
		}

		
		if (town.getBonusGoodies().size() > 0) {
			for (BonusGoodie goodie : town.getBonusGoodies()) {
				TradeGood good = goodie.getOutpost().getGood();
				
				int count = TradeGood.getTradeGoodCount(goodie, town)-1;
				String countString = ""+count;
				if (count > goodMax) {
					maxedCount = true;
					count = goodMax;
					countString = CivColor.LightPurple+count+CivColor.Yellow;
				}
				
				CultureChunk cc = CivGlobal.getCultureChunk(goodie.getOutpost().getCorner().getLocation());
				if (cc == null) {
					out.add(CivColor.Rose+goodie.getDisplayName()+" - Trade Outpost not inside culture! Goodie cannot be used.");
				} else {
					out.add(CivColor.LightGreen+goodie.getDisplayName()+"("+goodie.getOutpost().getCorner()+")"+CivColor.Yellow+" "+
							TradeGood.getBaseValue(good)+" * (1.0 + (0.5 * "+(countString)+") = "+df.format(TradeGood.getTradeGoodValue(goodie, town)));
				}
			}
		} else {
			out.add(CivColor.Rose+"No trade goods.");
		}
		
		out.add(CivColor.LightBlue+"=================================================");
		if (maxedCount) {
			out.add(CivColor.LightPurple+"Goods in this color have reached the max good multiplier");
		}
		out.add(CivColor.LightGray+"Base Value * ( 100% + ( 50% * MIN(ExtraGoods,"+goodMax+") )) = Good Value");
		out.add(CivColor.Green+"Total Trade: Good Total: "+CivColor.Yellow+df.format(TradeGood.getTownBaseGoodPaymentViaGoodie(town))+" * "+df.format(town.getTradeRate())+" = "
					+df.format(TradeGood.getTownTradePayment(town)));
		
		CivMessage.send(sender, out);
		return;
	}
	
	public void showDebugStructureInfo(Town town) {
		
		CivMessage.sendHeading(sender, "Structures In Town");
		for (Structure struct : town.getStructures()) {
			CivMessage.send(sender, struct.getDisplayName()+": Corner:"+struct.getCorner()+" center:"+struct.getCenterLocation());
		}
	}
	
	public void structures_cmd() throws CivException {
		Town town = getSelectedTown();
		
		if (args.length > 1) {
			if (args[1].equalsIgnoreCase("debug")) {
				showDebugStructureInfo(town);
				return;
			}
		}
			
		HashMap<String, Double> structsByName = new HashMap<String, Double>();
		for (Structure struct : town.getStructures()) {
			Double upkeep = structsByName.get(struct.getConfigId());
			if (upkeep == null) {
				structsByName.put(struct.getDisplayName(), struct.getUpkeepCost());
			} else {
				upkeep += struct.getUpkeepCost();
				structsByName.put(struct.getDisplayName(), upkeep);			
			}
		}
				
		CivMessage.sendHeading(sender, town.getName()+" Structure Info");
		for (String structName : structsByName.keySet()) {
			Double upkeep = structsByName.get(structName);
			CivMessage.send(sender, CivColor.Green+structName+" Upkeep: "+CivColor.LightGreen+upkeep);
			
		}
		
		CivMessage.sendHeading(sender, town.getName()+" Wonder Info");
		for (Wonder wonder : town.getWonders()) {
			CivMessage.send(sender, CivColor.Green+wonder.getDisplayName()+" Upkeep: "+CivColor.LightGreen+wonder.getUpkeepCost());
		}
			
	}
	
	
	public void cottage_cmd() throws CivException {
		Town town = getSelectedTown();
		ArrayList<String> out = new ArrayList<String>();	
		
		CivMessage.sendHeading(sender, town.getName()+" Cottage Info");
		double total = 0;
		
		for (Structure struct : town.getStructures()) {
			if (!struct.getConfigId().equals("ti_cottage")) {
				continue;
			}
			
			Cottage cottage = (Cottage)struct;
			
			String color;
			if (struct.isActive()) {
				color = CivColor.LightGreen;
			} else {
				color = CivColor.Rose;
			}
						
			double coins = cottage.getCoinsGenerated();
			if (town.getCiv().hasRequiredTech("tech_taxation")) {
				double taxation_bonus;
				try {
					taxation_bonus = CivSettings.getDouble(CivSettings.techsConfig, "taxation_cottage_buff");
					coins *= taxation_bonus;
				} catch (InvalidConfiguration e) {
					e.printStackTrace();
				}
			}
			
			if (!struct.isDestroyed()) {
				out.add(color+"Cottage ("+struct.getCorner()+")");
				out.add(CivColor.Green+"    level: "+CivColor.Yellow+cottage.getLevel()+
						CivColor.Green+" count: "+CivColor.Yellow+"("+cottage.getCount()+"/"+cottage.getMaxCount()+")");
				out.add(CivColor.Green+"    base coins: "+CivColor.Yellow+coins+
						CivColor.Green+" Last Result: "+CivColor.Yellow+cottage.getLastResult().name());
			} else {
				out.add(color+"Cottage ("+struct.getCorner()+")");
				out.add(CivColor.Rose+"    DESTROYED ");
			}
			
			total += coins;
			
		}
		out.add(CivColor.Green+"----------------------------");
		out.add(CivColor.Green+"Sub Total: "+CivColor.Yellow+total);
		out.add(CivColor.Green+"Cottage Rate: "+CivColor.Yellow+df.format(town.getCottageRate()*100)+"%");
		total *= town.getCottageRate();
		out.add(CivColor.Green+"Total: "+CivColor.Yellow+df.format(total)+" coins.");
		
		CivMessage.send(sender, out);
	}
	
	
	public void mine_cmd() throws CivException {
		Town town = getSelectedTown();
		ArrayList<String> out = new ArrayList<String>();	
		
		CivMessage.sendHeading(sender, town.getName()+" Mine Info");
		double total = 0;
		
		for (Structure struct : town.getStructures()) {
			if (!struct.getConfigId().equals("ti_mine")) {
				continue;
			}
			
			Mine mine = (Mine)struct;
			
			String color;
			if (struct.isActive()) {
				color = CivColor.LightGreen;
			} else {
				color = CivColor.Rose;
			}
									
			out.add(color+"Mine ("+struct.getCorner()+")");
			out.add(CivColor.Green+"    level: "+CivColor.Yellow+mine.getLevel()+
					CivColor.Green+" count: "+CivColor.Yellow+"("+mine.getCount()+"/"+mine.getMaxCount()+")");
			out.add(CivColor.Green+"    hammers per tile: "+CivColor.Yellow+mine.getHammersPerTile()+
					CivColor.Green+" Last Result: "+CivColor.Yellow+mine.getLastResult().name());
			
			total += mine.getHammersPerTile()*9; //XXX estimate based on tile radius of 1.
			
		}
		out.add(CivColor.Green+"----------------------------");
		out.add(CivColor.Green+"Sub Total: "+CivColor.Yellow+total);
		out.add(CivColor.Green+"Total: "+CivColor.Yellow+df.format(total)+" hammers (estimate).");
		
		CivMessage.send(sender, out);
	}
	
	public void upkeep_cmd() throws CivException {
		Town town = getSelectedTown();
		
		CivMessage.sendHeading(sender, town.getName()+" Upkeep Info");
		CivMessage.send(sender, CivColor.Green+"Base Upkeep: "+CivColor.LightGreen+town.getBaseUpkeep());
		
		try {
			CivMessage.send(sender, CivColor.Green+"Spread Upkeep: "+CivColor.LightGreen+town.getSpreadUpkeep());
		} catch (InvalidConfiguration e) {
			e.printStackTrace();
			throw new CivException("Internal configuration error.");
		}
		
		CivMessage.send(sender, CivColor.Green+"Structure Upkeep: "+CivColor.LightGreen+town.getStructureUpkeep());

		try {
			CivMessage.send(sender, CivColor.Green+"SubTotal: "+CivColor.LightGreen+town.getTotalUpkeep()+
					CivColor.Green+" Upkeep Rate: "+CivColor.LightGreen+town.getGovernment().upkeep_rate);
		} catch (InvalidConfiguration e) {
			e.printStackTrace();
			throw new CivException("Internal configuration error.");
		}
		CivMessage.send(sender, CivColor.LightGray+"---------------------------------");
		try {
			CivMessage.send(sender, CivColor.Green+"Total: "+CivColor.LightGreen+town.getTotalUpkeep()*town.getCiv().getGovernment().upkeep_rate);
		} catch (InvalidConfiguration e) {
			e.printStackTrace();
			throw new CivException("Internal configuration error.");
		}
		
	}
	
	public static void show(CommandSender sender, Resident resident, Town town, Civilization civ, CommandBase parent) throws CivException {
		DecimalFormat df = new DecimalFormat();
		boolean isAdmin = false;
		
		if (resident != null) {
			Player player = CivGlobal.getPlayer(resident);
			isAdmin = player.hasPermission(CivSettings.MINI_ADMIN);
		} else {
			/* We're the console! */
			isAdmin = true;
		}
		
		CivMessage.sendHeading(sender, town.getName()+" Info ");
		ConfigTownLevel level = CivSettings.townLevels.get(town.getLevel());
		
		CivMessage.send(sender, CivColor.Green+"Civilization: "+CivColor.LightGreen+town.getCiv().getName());
		
		CivMessage.send(sender, CivColor.Green+"Town Level: "+CivColor.LightGreen+town.getLevel()+" ("+town.getLevelTitle()+")"+
								CivColor.Green+"  Score: "+CivColor.LightGreen+town.getScore());
		
		if (town.getMayorGroup() == null) {
			CivMessage.send(sender, CivColor.Green+"Mayors: "+CivColor.Rose+"NONE");
		} else {
			CivMessage.send(sender, CivColor.Green+"Mayors: "+CivColor.LightGreen+town.getMayorGroup().getMembersString());			
		}
		
		if (town.getAssistantGroup() == null) {
			CivMessage.send(sender, CivColor.Green+"Assistants: "+CivColor.Rose+"NONE");
		} else {
			CivMessage.send(sender, CivColor.Green+"Assistants: "+CivColor.LightGreen+town.getAssistantGroup().getMembersString());		
		}
		
		if (resident == null || civ.hasResident(resident) || isAdmin) {
			String color = CivColor.LightGreen;
			if (town.getTileCount() > level.tiles) {
				color = CivColor.Rose;
			}
			if (town.getOutpostCount() > level.outposts) {
				color = CivColor.Rose;
			}
			
			//TODO Food is like Treasury, Population idk.
			CivMessage.send(sender, CivColor.Green+"Food: "+CivColor.LightGreen+df.format(town.getFoodCount())+
									CivColor.Green+"  Population: "+CivColor.LightGreen+df.format(town.getPopulation().total));
			
			CivMessage.send(sender, CivColor.Green+"Plots: "+CivColor.LightGreen+"("+town.getTownChunks().size()+"/"+town.getMaxPlots()+") "+
									CivColor.Green+"  Tiles: "+CivColor.LightGreen+"("+color+town.getTileCount()+CivColor.LightGreen+"/"+level.tiles+")"+
									CivColor.Green+"  Outposts: "+CivColor.LightGreen+"("+color+town.getOutpostCount()+CivColor.LightGreen+"/"+level.outposts+")");
			
			CivMessage.send(sender, CivColor.Green+"Growth: "+CivColor.LightGreen+df.format(town.getGrowth().total)+
									CivColor.Green+"  Hammers: "+CivColor.LightGreen+df.format(town.getHammers().total)+
									CivColor.Green+"  Beakers: "+CivColor.LightGreen+df.format(town.getBeakers().total));
			
			CivMessage.send(sender, CivColor.Green+"Members: "+CivColor.LightGreen+town.getResidentCount()+
									CivColor.Green+"  Tax Rate: "+CivColor.LightGreen+town.getTaxRateString()+
									CivColor.Green+"  Flat Tax: "+CivColor.LightGreen+town.getFlatTax()+" coins");
			
			ConfigHappinessState state = town.getHappinessState();
			CivMessage.send(sender, CivColor.Green+"Happiness: "+CivColor.LightGreen+df.format(Math.floor(town.getHappinessPercentage()*100))+"%"+
					CivColor.Green+"  State: "+CivColor.valueOf(state.color)+state.name);
			
//			HashMap<String,String> info = new HashMap<String, String>();
//			info.put("Happiness", CivColor.LightGreen+df.format(Math.floor(town.getHappinessPercentage()*100))+"%");
//			ConfigHappinessState state = town.getHappinessState();
//			info.put("State", ""+CivColor.valueOf(state.color)+state.name);	
//			CivMessage.send(sender, parent.makeInfoString(info, CivColor.Green, CivColor.LightGreen));
			
			ConfigCultureLevel clc = CivSettings.cultureLevels.get(town.getCultureLevel());	
			CivMessage.send(sender, CivColor.Green+"Culture Level: "+CivColor.LightGreen+clc.level+" ("+town.getAccumulatedCulture()+"/"+clc.amount+")"+
									CivColor.Green+"  Online: "+CivColor.LightGreen+town.getOnlineResidents().size());
			
//			ConfigFaithLevel cfl = CivSettings.faithLevels.get(town.getFaithLevel());	
//			CivMessage.send(sender, CivColor.Green+"Faith Level: "+CivColor.LightGreen+cfl.level+" ("+town.getAccumulatedFaith()+"/"+cfl.amount+")");
		}
		
		if (town.getBonusGoodies().size() > 0) {
			String goodies = "";
			for (BonusGoodie goodie : town.getBonusGoodies()) {
				goodies += goodie.getDisplayName()+",";
			}
			CivMessage.send(sender, CivColor.Green+"Goodies: "+CivColor.LightGreen+goodies);
		}
		
		if (resident == null || town.isInGroup("mayors", resident) || town.isInGroup("assistants", resident) || 
				civ.getLeaderGroup().hasMember(resident) || civ.getAdviserGroup().hasMember(resident) || isAdmin) {
			try {
				CivMessage.send(sender, CivColor.Green+"Treasury: "+CivColor.LightGreen+town.getBalance()+CivColor.Green+" coins"+
														"  Upkeep: "+CivColor.LightGreen+town.getTotalUpkeep()*town.getGovernment().upkeep_rate);
				
				Structure bank = town.getStructureByType("s_bank");
				if (bank != null) { 
					CivMessage.send(sender, CivColor.Green+"Interest Rate: "+CivColor.LightGreen+df.format(((Bank)bank).getInterestRate()*100)+"%"+
											CivColor.Green+"  Principal: "+CivColor.LightGreen+town.getTreasury().getPrincipalAmount());
				} else {
					CivMessage.send(sender, CivColor.Green+"Interest Rate: "+CivColor.LightGreen+"None (No Bank) "+
											CivColor.Green+"  Principal: "+CivColor.LightGreen+"None (No Bank)");
				}
			} catch (InvalidConfiguration e) {
				e.printStackTrace();
				throw new CivException("Internal configuration error.");
			}
		}
		
		if (town.inDebt()) {
			CivMessage.send(sender, CivColor.Green+"Debt: "+CivColor.Yellow+town.getDebt()+" coins");
			CivMessage.send(sender, CivColor.Yellow+"Our town is in debt! Use '/town deposit' to pay it off.");
		}
		
		if (town.getMotherCiv() != null) {
			CivMessage.send(sender, CivColor.Yellow+"We yearn for our old motherland of "+CivColor.LightPurple+town.getMotherCiv().getName()+CivColor.Yellow+"!");
		}
		
		if (town.hasDisabledStructures()) {
			CivMessage.send(sender, CivColor.Rose+"Town has some disabled structures. See /town info disabled.");
		}
		
		if (isAdmin) {
			TownHall townhall = town.getTownHall();
			if (townhall == null) {
				CivMessage.send(sender, CivColor.LightPurple+"NO TOWN HALL");
			} else {
				CivMessage.send(sender, CivColor.LightPurple+"Location:"+townhall.getCorner());
			}
			
			String wars = "";
			for (Relation relation : town.getCiv().getDiplomacyManager().getRelations()) {
				if (relation.getStatus() == Status.WAR) {
					wars += relation.getOtherCiv().getName()+", ";
				}
			}
			CivMessage.send(sender, CivColor.LightPurple+"Wars: "+wars);
		}
	}
	
	private void show_info() throws CivException {
		Civilization civ = getSenderCiv();	
		Town town = getSelectedTown();
		Resident resident = getResident();
		
		show(sender, resident, town, civ, this);	
				
	}
	
	@Override
	public void doDefaultAction() throws CivException {
		show_info();
		CivMessage.send(sender, CivColor.LightGray+"Subcommands available: See /town info help");
	}

	@Override
	public void showHelp() {
		showBasicHelp();
	}

	@Override
	public void permissionCheck() throws CivException {		
	}

}
