/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.command.town;

import java.util.ArrayList;

import com.civcraft.command.CommandBase;
import com.civcraft.config.ConfigTownUpgrade;
import com.civcraft.exception.CivException;
import com.civcraft.main.CivMessage;
import com.civcraft.object.Town;
import com.civcraft.structure.Library;
import com.civcraft.structure.Store;

public class TownResetCommand extends CommandBase {

	@Override
	public void init() {
		command = "/town reset";
		displayName = "Town Reset";
		
		commands.put("library", "Removes all town library enchantment upgrades.");
		commands.put("store", "Removes all town store material upgrades.");
	}

	public void library_cmd() throws CivException {
		Town town = getSelectedTown();
		
		Library library = (Library) town.findStructureByConfigId("s_library");
		if (library == null) {
			throw new CivException("Your town doesn't have a library.");
		}
		
		ArrayList<ConfigTownUpgrade> removeUs = new ArrayList<ConfigTownUpgrade>();
		for(ConfigTownUpgrade upgrade : town.getUpgrades().values()) {
			if (upgrade.action.contains("enable_library_enchantment")) {
				removeUs.add(upgrade);
			}
		}
		
		for (ConfigTownUpgrade upgrade : removeUs) {
			town.removeUpgrade(upgrade);
		}
		
		library.reset();
		
		town.save();
		CivMessage.sendSuccess(sender, "Library enchantment upgrades reset!");
	}
	
	public void store_cmd() throws CivException {
		Town town = getSelectedTown();
		
		Store store = (Store) town.findStructureByConfigId("s_store");
		if (store == null) {
			throw new CivException("Your town doesn't have a library.");
		}
		
		ArrayList<ConfigTownUpgrade> removeUs = new ArrayList<ConfigTownUpgrade>();
		for(ConfigTownUpgrade upgrade : town.getUpgrades().values()) {
			if (upgrade.action.contains("set_store_material")) {
				removeUs.add(upgrade);
			}
		}
		
		for (ConfigTownUpgrade upgrade : removeUs) {
			town.removeUpgrade(upgrade);
		}
		
		store.reset();
		
		town.save();
		CivMessage.sendSuccess(sender, "Store material upgrades reset!");
	}
	
	@Override
	public void doDefaultAction() throws CivException {
		showHelp();
	}

	@Override
	public void showHelp() {
		this.showBasicHelp();
	}

	@Override
	public void permissionCheck() throws CivException {
		this.validMayorAssistantLeader();
	}

}
