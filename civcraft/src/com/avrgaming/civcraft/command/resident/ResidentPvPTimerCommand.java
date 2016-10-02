package com.avrgaming.civcraft.command.resident;

import com.avrgaming.civcraft.command.CommandBase;
import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.Resident;

public class ResidentPvPTimerCommand extends CommandBase {
	
	@Override
	public void init() {
		command = "/resident pvptimer";
		displayName = "Resident PvPTimer";
		
		commands.put("disable", "Remove your PvP Timer. This is a PERMENANT change, and can not be undone.");
		commands.put("check", "See if your PvP Timer is still enabled, and how much longer it will last.");
	}
	
	public void disable_cmd() throws CivException {
		Resident res = getResident();
		if (!res.isProtected()) {
			CivMessage.sendError(sender, "You are not even protected, you wasted your time.");
		}
		res.setisProtected(false);
		CivMessage.sendSuccess(sender, "You are no longer protected, good luck!");
	}
	
	public void check_cmd() throws CivException {
		Resident res = getResident();
		if (!res.isProtected()) {
			CivMessage.sendError(sender, "You currently have no protection.");
		} else {
			CivMessage.sendSuccess(sender, "You currently are protected.");
		}
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
