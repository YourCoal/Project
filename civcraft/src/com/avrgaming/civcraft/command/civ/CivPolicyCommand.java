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
import com.avrgaming.civcraft.config.ConfigPolicy;
import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.Civilization;
import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.object.Town;
import com.avrgaming.civcraft.structure.TownHall;
import com.avrgaming.civcraft.util.CivColor;

public class CivPolicyCommand extends CommandBase {

	@Override
	public void init() {
		command = "/civ policy";
		displayName = "Civ Policy";
		commands.put("list", "List the available policies we can research.");
		commands.put("progress", "Shows progress on your current policy.");
		commands.put("adopt", "[policy] - Starts researching on this policy.");
		commands.put("change", "[policy] - Stops researching our current policy, changes to this. You will lose all progress on your current policy.");
		commands.put("finished", "Shows which policies we already have.");
	}
	
	public void change_cmd() throws CivException {
		Civilization civ = getSenderCiv();
		if (args.length < 2) {
			list_cmd();
			throw new CivException("enter the name of the policy you want to change to.");
		}
		
		String policyname = combineArgs(stripArgs(args, 1));
		ConfigPolicy policy = CivSettings.getPolicyByName(policyname);
		if (policy == null) {
			throw new CivException("Couldn't find a policy named "+policyname);
		}
		
		if(!policy.isAvailable(civ)) {
			throw new CivException("You cannot research this policy at this time.");
		}
		
		if (!civ.getTreasury().hasEnough(policy.cost)) {
			throw new CivException("You do not have enough coins to research policy "+policy.name);
		}
		
		if (civ.getResearchPolicy() != null) {
			civ.setPolicyResearchProgress(0);
			CivMessage.send(sender, CivColor.Rose+"Progress on policy "+civ.getResearchPolicy().name+" has been lost.");
			civ.setResearchPolicy(null);
		}
		civ.startPolicyResearch(policy);
		CivMessage.sendCiv(civ, "Our Civilization started researching policy "+policy.name);
	}
	
	public void finished_cmd() throws CivException {
		Civilization civ = getSenderCiv();
		CivMessage.sendHeading(sender, "Researched Policies");
		String out = "";
		for (ConfigPolicy policy : civ.getPolicies()) {
			out += policy.name+", ";
		}
		CivMessage.send(sender, out);
	}
	
	public void adopt_cmd() throws CivException {
		Civilization civ = getSenderCiv();
		if (args.length < 2) {
			throw new CivException("Enter the name of the policy you want to research.");
		}
		
		Town capitol = CivGlobal.getTown(civ.getCapitolName());
		if (capitol == null) {
			throw new CivException("Couldn't find capitol town:"+civ.getCapitolName()+"! Internal Error!");
		}
		
		TownHall townhall = capitol.getTownHall();
		if (townhall == null) {
			throw new CivException("Couldn't find your capitol's town hall. Cannot perform policy research without a town hall! ");
		}
		
		if (!townhall.isActive()) {
			throw new CivException("Town hall must be completed before you can begin researching policies.");
		}
		
		String polname = combineArgs(stripArgs(args, 1));
		ConfigPolicy p = CivSettings.getPolicyByName(polname);
		if (p == null) {
			throw new CivException("Couldn't find a policy named "+polname);
		}
		civ.startPolicyResearch(p);
		CivMessage.sendSuccess(sender, "Started researching policy "+p.name);
	}
	
	public void progress_cmd() throws CivException {
		Civilization civ = getSenderCiv();
		CivMessage.sendHeading(sender, "Currently Researching Policy");
		if (civ.getResearchPolicy() != null) {
			int percentageComplete = (int)((civ.getPolicyResearchProgress() / civ.getResearchPolicy().beaker_cost)*100);	
			CivMessage.send(sender, civ.getResearchPolicy().name+" is "+percentageComplete+"% complete. ("+
					civ.getPolicyResearchProgress()+" / "+civ.getResearchPolicy().beaker_cost+ " ) ");
		} else {
			CivMessage.send(sender, "Currently not adpoting a policy.");
		}
	}
	
	public void list_cmd() throws CivException {
		Civilization civ = getSenderCiv();
		ArrayList<ConfigPolicy> policy = ConfigPolicy.getAvailablePolicies(civ);
		CivMessage.sendHeading(sender, "Available Policy Adoptions");
		for (ConfigPolicy p : policy) {
			CivMessage.send(sender, p.name+CivColor.LightGray+" Cost: "+
					CivColor.Yellow+p.cost+CivColor.LightGray+" Beakers: "+
					CivColor.Yellow+p.beaker_cost+CivColor.LightGray+" Tree: "+
					CivColor.Yellow+p.tree);
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
