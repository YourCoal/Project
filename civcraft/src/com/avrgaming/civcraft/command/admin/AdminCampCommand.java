package com.avrgaming.civcraft.command.admin;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.avrgaming.civcraft.camp.Camp;
import com.avrgaming.civcraft.command.CommandBase;
import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.Resident;

public class AdminCampCommand extends CommandBase {

	@Override
	public void init() {
		command = "/ad camp";
		displayName = "Admin camp";	
		
		commands.put("destroy", "[name] - destroyes this camp.");
		commands.put("setraidtime", "[name] - d:M:y:H:m sets the raid time.");
		commands.put("rebuild", "rebuilds this camp template");
	}
	
	public void rebuild_cmd() throws CivException {
		Camp camp = this.getNamedCamp(1);
		
		try {
			camp.repairFromTemplate();
		} catch (IOException e) {
		} catch (CivException e) {
			e.printStackTrace();
		}
		camp.reprocessCommandSigns();
		CivMessage.send(sender, "Repaired.");
	}
	
	public void setraidtime_cmd() throws CivException {
		Resident resident = getNamedResident(1);
		
		if (!resident.hasCamp()) {
			throw new CivException("This resident does not have a camp.");
		}
		
		if (args.length < 3) {
			throw new CivException("Enter a camp owner and date like DAY:MONTH:YEAR:HOUR:MIN");
		}
		
		Camp camp = resident.getCamp();
		
		String dateStr = args[2];
		SimpleDateFormat parser = new SimpleDateFormat("d:M:y:H:m");
		
		Date next;
		try {
			next = parser.parse(dateStr);
			camp.setNextRaidDate(next);
			CivMessage.sendSuccess(sender, "Set raid date.");
		} catch (ParseException e) {
			throw new CivException("Couldnt parse "+args[2]+" into a date, use format: DAY:MONTH:YEAR:HOUR:MIN");
		}
		
	}
	
	public void destroy_cmd() throws CivException {
		Camp camp = getNamedCamp(1);		
		camp.destroy();
		CivMessage.sendSuccess(sender, "Camp destroyed.");
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
