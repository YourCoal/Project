package com.avrgaming.civcraft.command.town;

import org.apache.commons.lang.WordUtils;

import com.avrgaming.civcraft.command.CommandBase;
import com.avrgaming.civcraft.config.CivSettings;
import com.avrgaming.civcraft.config.ConfigTownUpgrade;
import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.Town;
import com.avrgaming.civcraft.util.CivColor;

public class TownUpgradeCommand extends CommandBase {

	@Override
	public void init() {
		command = "/town upgrade";
		displayName = "Town Upgrade";
		
		
		commands.put("list", "shows available upgrades to purchase.");
		commands.put("purchased", "shows a list of purchased upgrades.");
		commands.put("buy", "[name] - buys upgrade for town.");
		
	}

	public void purchased_cmd() throws CivException {
		Town town = this.getSelectedTown();
		CivMessage.sendHeading(sender, "Upgrades Purchased");

		String out = "";
		for (ConfigTownUpgrade upgrade : town.getUpgrades().values()) {
			out += upgrade.name+", ";
		}
		
		CivMessage.send(sender, out);
	}
	
	private void list_upgrades(String category, Town town) throws CivException {
		if (!ConfigTownUpgrade.categories.containsKey(category.toLowerCase()) && !category.equalsIgnoreCase("all")) {
			throw new CivException("No category called "+category);
		}
				
		for (ConfigTownUpgrade upgrade : CivSettings.townUpgrades.values()) {
			if (category.equalsIgnoreCase("all") || upgrade.category.equalsIgnoreCase(category)) {	
				if (upgrade.isAvailable(town)) {
					CivMessage.send(sender, upgrade.name+CivColor.LightGray+" Cost: "+CivColor.Yellow+upgrade.cost);
				}
			}
		}
	}
	
	public void list_cmd() throws CivException {
		Town town = this.getSelectedTown();
		
		CivMessage.sendHeading(sender, "Available Upgrades");
		
		if (args.length < 2) {
			CivMessage.send(sender, "- "+CivColor.Gold+"All "+
					CivColor.LightBlue+"("+ConfigTownUpgrade.getAvailableCategoryCount("all", town)+")");
			for (String category : ConfigTownUpgrade.categories.keySet()) {
				CivMessage.send(sender, "- "+CivColor.Gold+WordUtils.capitalize(category)+
						CivColor.LightBlue+" ("+ConfigTownUpgrade.getAvailableCategoryCount(category, town)+")");
			}
			return;
		}
		
		list_upgrades(args[1], town);		
	
	}
	
	public void buy_cmd() throws CivException {
		if (args.length < 2) {
			list_upgrades("all", getSelectedTown());
			CivMessage.send(sender, "Enter the name of the upgrade you wish to purchase.");
			return;
		}
		
		Town town = this.getSelectedTown();
		
		String combinedArgs = "";
		args = this.stripArgs(args, 1);
		for (String arg : args) {
			combinedArgs += arg + " ";
		}
		combinedArgs = combinedArgs.trim();
		
		ConfigTownUpgrade upgrade = CivSettings.getUpgradeByNameRegex(town, combinedArgs);
		if (upgrade == null) {
			throw new CivException("No upgrade by the name of "+combinedArgs+" could be found.");
		}
		
		if (town.hasUpgrade(upgrade.id)) {
			throw new CivException("You already have that upgrade.");
		}
		
		//TODO make upgrades take time by using hammers.
		town.purchaseUpgrade(upgrade);
		CivMessage.sendSuccess(sender, "Upgrade \""+upgrade.name+"\" purchased.");
	}
	
	@Override
	public void doDefaultAction() throws CivException {
		showHelp();
	}

	@Override
	public void showHelp() {
		showBasicHelp();
	}

	@Override
	public void permissionCheck() throws CivException {	
		this.validMayorAssistantLeader();
	}

}
