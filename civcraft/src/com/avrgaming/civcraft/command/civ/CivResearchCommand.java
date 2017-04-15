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
package com.avrgaming.civcraft.command.civ;

import java.util.ArrayList;

import com.avrgaming.civcraft.command.CommandBase;
import com.avrgaming.civcraft.config.CivSettings;
import com.avrgaming.civcraft.config.ConfigTech;
import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.Civilization;
import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.object.Town;
import com.avrgaming.civcraft.structure.TownHall;
import com.avrgaming.civcraft.util.CivColor;

public class CivResearchCommand extends CommandBase {

	@Override
	public void init() {
		command = "/civ research";
		displayName = "Civ Research";
		
		commands.put("list", "List the available technologies we can research.");
		commands.put("progress", "Shows progress on your current research.");
		commands.put("on", "[tech] - Starts researching on this technology.");
		commands.put("change", "[tech] - Stops researching our current tech, changes to this. You will lose all progress on your current tech.");
		commands.put("finished", "Shows which technologies we already have.");
		
		commands.put("addqueue", "[tech] - Adds this technology to your research queue.");
		commands.put("removequeue", "Removes the technology in your research queue.");
		commands.put("listqueue", "Displays current technology in your research queue.");
		commands.put("extendqueue", "[IN-DEVELOPMENT] Allows you to add additional technologies your research queue.");
	}
	
	public void extendqueue_cmd() throws CivException {
		throw new CivException("Currently In-Development. Check the Dev Notes for when it's released.");
	}
	
	public void addqueue_cmd() throws CivException {
		Civilization civ = getSenderCiv();
		if (args.length < 2) {
			throw new CivException("Enter the name of the technology you want to add to your research queue.");
		}
		
		Town capitol = CivGlobal.getTown(civ.getCapitolName());
		if (capitol == null) {
			throw new CivException("Couldn't find capitol town:"+civ.getCapitolName()+"! Internal Error!");
		}
		
		TownHall townhall = capitol.getTownHall();
		if (townhall == null) {
			throw new CivException("Couldn't find your capitol's town hall. Cannot perform research without a town hall! ");
		}
		
		if (!townhall.isActive()) {
			throw new CivException("Town hall must be completed before you can begin research.");
		}
		
		String techname = combineArgs(stripArgs(args, 1));
		ConfigTech tech = CivSettings.getTechByName(techname);
		if (tech == null) {
			throw new CivException("Couldn't find technology named "+techname);
		}
		
		if (civ.getResearchTech() == null) {
			throw new CivException("You aren't even researching a tech, dummy! Use '/civ research on "+tech.name+"' instead.");
		}
		
		if (civ.getResearchTech() == tech) {
			throw new CivException("Cannot queue a tech that you are already researching.");
		}
		
		if (civ.getTechQueued() != null) {
			if (civ.getTechQueued() == tech) {
				throw new CivException("Cannot queue the tech that is already queued.");
			}
			
			if (civ.getResearchTech() == null) {
				throw new CivException("You aren't even researching a tech, dummy! Use '/civ research on "+tech.name+"' instead.");
			}
			ConfigTech oldQueue = civ.getTechQueued();
			civ.setTechQueued(tech);
			CivMessage.sendSuccess(sender, "Added "+tech.name+" to your tech queue.");
			CivMessage.send(sender, CivColor.YellowBold+"[WARNING] You just removed the following tech from the queue, "+oldQueue.name+"."
					+ "If you want to change it back, re-run this command (with the proper tech name).");
			civ.save();
		} else {
			civ.setTechQueued(tech);
			CivMessage.sendSuccess(sender, "Added "+tech.name+" to your tech queue.");
			civ.save();
		}
	}
	
	public void removequeue_cmd() throws CivException {
		Civilization civ = getSenderCiv();
		if (civ.getTechQueued() == null) {
			throw new CivException("You have no tech in your queue right now.");
		} else {
			ConfigTech oldQueue = civ.getTechQueued();
			civ.setTechQueued(null);
			CivMessage.sendSuccess(sender, "Removed "+oldQueue.name+" from your tech queue.");
			civ.save();
		}
	}
	
	public void listqueue_cmd() throws CivException {
		Civilization civ = getSenderCiv();
		if (civ.getTechQueued() == null) {
			throw new CivException("You have no tech in your queue right now.");
		} else {
			CivMessage.sendSuccess(sender, "Current Queue: "+CivColor.LightPurple+civ.getTechQueued().name);
		}
	}
	
	public void change_cmd() throws CivException {
		Civilization civ = getSenderCiv();
		if (args.length < 2) {
			list_cmd();
			throw new CivException("enter the name of the technology you want to change to.");
		}
		
		String techname = combineArgs(stripArgs(args, 1));
		ConfigTech tech = CivSettings.getTechByName(techname);
		if (tech == null) {
			throw new CivException("Couldn't find technology named "+techname);
		}
		
		if (!civ.getTreasury().hasEnough(tech.getAdjustedTechCost(civ))) {
			throw new CivException("You do not have enough coins to research "+tech.name);
		}
		
		if(!tech.isAvailable(civ)) {
			throw new CivException("You cannot research this technology at this time.");
		}
		
		if (civ.getResearchTech() != null) {
			civ.setResearchTechProgress(0);
			CivMessage.send(sender, CivColor.Rose+"Progress on "+civ.getResearchTech().name+" has been lost.");
			civ.setResearchTech(null);
		}
		civ.startTechnologyResearch(tech);
		CivMessage.sendCiv(civ, "Our Civilization started researching "+tech.name);
	}
	
	public void finished_cmd() throws CivException {
		Civilization civ = getSenderCiv();
		CivMessage.sendHeading(sender, "Researched Technologies");
		String out = "";
		for (ConfigTech tech : civ.getTechs()) {
			out += tech.name+", ";
		}
		CivMessage.send(sender, out);
	}
	
	public void on_cmd() throws CivException {
		Civilization civ = getSenderCiv();
		
		if (args.length < 2) {
			throw new CivException("Enter the name of the technology you want to research.");
		}
		
		Town capitol = CivGlobal.getTown(civ.getCapitolName());
		if (capitol == null) {
			throw new CivException("Couldn't find capitol town:"+civ.getCapitolName()+"! Internal Error!");
		}
	
		TownHall townhall = capitol.getTownHall();
		if (townhall == null) {
			throw new CivException("Couldn't find your capitol's town hall. Cannot perform research without a town hall! ");
		}
		
		if (!townhall.isActive()) {
			throw new CivException("Town hall must be completed before you can begin research.");
		}
		
		String techname = combineArgs(stripArgs(args, 1));
		ConfigTech tech = CivSettings.getTechByName(techname);
		if (tech == null) {
			throw new CivException("Couldn't find technology named "+techname);
		}
		
		civ.startTechnologyResearch(tech);
		CivMessage.sendSuccess(sender, "Started researching "+tech.name);
	}
	
	public void progress_cmd() throws CivException {
		Civilization civ = getSenderCiv();
		
		CivMessage.sendHeading(sender, "Currently Researching");
		
		if (civ.getResearchTech() != null) {
			int percentageComplete = (int)((civ.getResearchTechProgress() / civ.getResearchTech().getAdjustedScienceCost(civ))*100);		
			CivMessage.send(sender, civ.getResearchTech().name+" is "+percentageComplete+"% complete. ("+
					civ.getResearchTechProgress()+" / "+civ.getResearchTech().getAdjustedScienceCost(civ)+ " ) ");
		} else {
			CivMessage.send(sender, "Nothing currently researching.");
		}
		
	}
	
	public void list_cmd() throws CivException {
		Civilization civ = getSenderCiv();
		ArrayList<ConfigTech> techs = ConfigTech.getAvailableTechs(civ);
		
		CivMessage.sendHeading(sender, "Available Research");
		for (ConfigTech tech : techs) {
			CivMessage.send(sender, tech.name+CivColor.LightGray+" Cost: "+
					CivColor.Yellow+tech.getAdjustedTechCost(civ)+CivColor.LightGray+" Science: "+
					CivColor.Yellow+tech.getAdjustedScienceCost(civ));
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
		Civilization civ = getSenderCiv();
		
		if (!civ.getLeaderGroup().hasMember(resident) && !civ.getAdviserGroup().hasMember(resident)) {
			throw new CivException("Only civ leaders and advisers can access research.");
		}		
	}

}
