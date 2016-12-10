package com.avrgaming.civcraft.command.town;

import java.util.ArrayList;

import com.avrgaming.civcraft.command.CommandBase;
import com.avrgaming.civcraft.config.CivSettings;
import com.avrgaming.civcraft.config.ConfigCivic;
import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.object.Town;
import com.avrgaming.civcraft.structure.TownHall;
import com.avrgaming.civcraft.util.CivColor;

public class TownCivicCommand extends CommandBase {

	@Override
	public void init() {
		command = "/town civic";
		displayName = "Town Civic";
		
		commands.put("list", "List the available civics we can research.");
		commands.put("progress", "Shows progress on your current civic.");
		commands.put("on", "[civic] - Starts researching on this civic.");
		commands.put("change", "[civic] - Stops researching our current civic, changes to this. WARNING: You will lose all progress on your current civic.");
		commands.put("finished", "Shows which civics we already have.");
	}
	
	public void change_cmd() throws CivException {
		Town t = getSelectedTown();
		if (args.length < 2) {
			list_cmd();
			throw new CivException("enter the name of the technology you want to change to.");
		}
		
		String cvcname = combineArgs(stripArgs(args, 1));
		ConfigCivic cvc = CivSettings.getCivicByName(cvcname);
		if (cvc == null) {
			throw new CivException("Couldn't find a civic named "+cvcname);
		}
		
		if (!t.getTreasury().hasEnough(cvc.cost)) {
			throw new CivException("You do not have enough coins to research "+cvc.name);
		}
		
		if(!cvc.isAvailable(t)) {
			throw new CivException("You cannot research this technology at this time.");
		}
		
		if (t.getCivicResearch() != null) {
			t.setCivicResearchProgress(0);
			CivMessage.send(sender, CivColor.Rose+"Progress on "+t.getCivicResearch().name+" has been lost.");
			t.setCivicResearch(null);
		}
		t.startCivicResearch(cvc);
		CivMessage.sendTown(t, "Our Town started researching civic "+cvc.name);
	}
	
	public void finished_cmd() throws CivException {
		Town t = getSelectedTown();
		CivMessage.sendHeading(sender, "Researched Civics");
		String out = "";
		for (ConfigCivic cvc : t.getCivics()) {
			out += cvc.name+", ";
		}
		CivMessage.send(sender, out);
	}

	public void on_cmd() throws CivException {
		Town t = getSelectedTown();
		if (args.length < 2) {
			throw new CivException("Enter the name of the civic you want to research.");
		}
		
		Town city = CivGlobal.getTown(t.getName());
		if (city == null) {
			throw new CivException("Couldn't find this town: "+t.getCiv().getCapitolName()+"! Internal Error!");
		}
		
		TownHall th = city.getTownHall();
		if (th == null) {
			throw new CivException("Couldn't find your town hall. Cannot perform civic research without a town hall! ");
		}
		
		if (!th.isActive()) {
			throw new CivException("Town hall must be completed before you can begin civic research.");
		}
		
		String cvcname = combineArgs(stripArgs(args, 1));
		ConfigCivic cvc = CivSettings.getCivicByName(cvcname);
		if (cvc == null) {
			throw new CivException("Couldn't find civic named "+cvcname);
		}
		t.startCivicResearch(cvc);
		CivMessage.sendSuccess(sender, "Started researching civic "+cvc.name);
	}
	
	public void progress_cmd() throws CivException {
		Town t = getSelectedTown();
		CivMessage.sendHeading(sender, "Current Civic Researching");
		if (t.getCivicResearch() != null) {
			int percentageComplete = (int)((t.getCivicResearchProgress() / t.getCivicResearch().culture_cost)*100);		
			CivMessage.send(sender, t.getCivicResearch().name+" is "+percentageComplete+"% complete. ("+
					t.getCivicResearchProgress()+" / "+t.getCivicResearch().culture_cost+ " ) ");
		} else {
			CivMessage.send(sender, "No civic currently researching.");
		}
	}
	
	public void list_cmd() throws CivException {
		Town t = getSelectedTown();
		ArrayList<ConfigCivic> civics = ConfigCivic.getAvailableCivics(t);
		CivMessage.sendHeading(sender, "Available Civics");
		for (ConfigCivic civic : civics) {
			CivMessage.send(sender, civic.name+CivColor.LightGray+" Cost: "+CivColor.Yellow+civic.cost
											+CivColor.LightGray+" Culture: "+CivColor.Yellow+civic.culture_cost
											+CivColor.LightGray+" Bonus: "+CivColor.Yellow+civic.bonus_factors);
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
		Resident resident = getResident();
		Town t = getSelectedTown();
		if (!t.getCiv().getLeaderGroup().hasMember(resident) && !t.getMayorGroup().hasMember(resident)) {
			throw new CivException("Only civ leaders and mayors can access civic research.");
		}		
	}
}
