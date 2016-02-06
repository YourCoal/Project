package com.avrgaming.civcraft.command.admin;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;

import com.avrgaming.civcraft.command.CommandBase;
import com.avrgaming.civcraft.config.CivSettings;
import com.avrgaming.civcraft.config.ConfigGovernment;
import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.exception.InvalidConfiguration;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.Civilization;
import com.avrgaming.sls.SLSManager;

public class AdminReloadCommand extends CommandBase {
	
	@Override
	public void init() {
		command = "/ad reload";
		displayName = "Admin Reload";
		commands.put("fishing", "Reloads the Fishing Drops.");
		commands.put("govs", "Reloads the Governments.");
		commands.put("nocheat", "Reloads the Validated Mods.");
		commands.put("perks", "Reloads the Perks.");
		commands.put("sls", "Reloads the Server Listening Service to civilizationcraft.net.");
	}
	
	public void fishing_cmd() throws FileNotFoundException, IOException, InvalidConfigurationException, InvalidConfiguration {
		CivSettings.reloadFishingConfigFiles();
		CivMessage.send(sender, "Reloaded Fishing Drops.");
	}
	
	public void govs_cmd() throws FileNotFoundException, IOException, InvalidConfigurationException, InvalidConfiguration {
		CivSettings.reloadGovConfigFiles();
		for (Civilization civ : CivGlobal.getCivs()) {
			ConfigGovernment gov = civ.getGovernment();
			civ.setGovernment(gov.id);
		}
		CivMessage.send(sender, "Reloaded Governments.");
	}
	
	public void nocheat_cmd() throws FileNotFoundException, IOException, InvalidConfigurationException, InvalidConfiguration {
		CivSettings.reloadNoCheatConfigFiles();
		CivMessage.send(sender, "Reloaded NoCheat Validated Mods.");
	}
	
	public void perks_cmd() throws FileNotFoundException, IOException, InvalidConfigurationException, InvalidConfiguration {
		CivSettings.reloadPerkConfigFiles();
		CivMessage.send(sender, "Reloaded Perks.");
	}
	
	public void sls_cmd() {
		SLSManager.sendHeartbeat();
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
		if (sender.isOp() == false) {
			throw new CivException("Only admins can use this command.");			
		}	
	}
}
