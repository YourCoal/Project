package com.avrgaming.civcraft.command.admin;

import java.sql.SQLException;

import com.avrgaming.civcraft.camp.Camp;
import com.avrgaming.civcraft.command.CommandBase;
import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.Civilization;
import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.object.Town;

public class AdminSQLCommand extends CommandBase {
	
	String message;
	
	@Override
	public void init() {
		command = "/ad town";
		displayName = "Admin Town";
		commands.put("disbandciv", "[civ] - Disbands this civilization.");
		commands.put("disbandtown", "[town] - Disbands this town.");
		commands.put("disbandcamp", "[camp] - Disbands this camp.");
		commands.put("resetres", "[resident] Resets this [resident] data in SQLs.");
	}
	
	public void resetres_cmd() throws CivException {
		Resident res = getNamedResident(1);
		this.message = getAdminMessage(2);
		CivMessage.send(res, "Your civcraft player data has been reset! Relog to become a real human again.");
		CivMessage.global("The resident "+res+" has been reset by an admin! Reason: "+message);
		try {
			res.delete();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		CivMessage.sendSuccess(sender, "Resident Cleared!");
	}
	
	public void disbandcamp_cmd() throws CivException {
		Camp camp = getNamedCamp(1);
		this.message = getAdminMessage(2);
		CivMessage.sendCamp(camp, "Your camp is has disbanded by an admin!");
		CivMessage.global("The camp of "+camp+" has been disbanded by an admin! Reason: "+message);
		try {
			camp.delete();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		CivMessage.sendSuccess(sender, "Camp disbanded!");
	}
	
	public void disbandtown_cmd() throws CivException {
		Town town = getNamedTown(1);
		this.message = getAdminMessage(2);
		if (town.isCapitol()) {
			throw new CivException("Cannot disband the capitol town, disband the civilization instead.");
		}
		
		CivMessage.sendTown(town, "Your town is has disbanded by an admin!");
		CivMessage.global("The town of "+town+" in civilization "+town.getCiv().getName()+" has been disbanded by an admin! Reason: "+message);
		try {
			town.delete();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		CivMessage.sendSuccess(sender, "Town disbanded!");
	}
	
	public void disbandciv_cmd() throws CivException {
		Civilization civ = getNamedCiv(1);
		this.message = getAdminMessage(2);
		CivMessage.sendCiv(civ, "Your civ is has disbanded by an admin!");
		CivMessage.global("The civilization of "+civ+" has been disbanded by an admin! Reason: "+message);
		try {
			civ.delete();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		CivMessage.sendSuccess(sender, "Civilization disbanded!");
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
	}
}
