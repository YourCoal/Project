package com.avrgaming.civcraft.command.debug;

import com.avrgaming.civcraft.command.CommandBase;
import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.Town;
import com.avrgaming.civcraft.threading.tasks.FisheryAsyncTask;
import com.avrgaming.civcraft.threading.tasks.QuarryAsyncTask;
import com.avrgaming.civcraft.threading.tasks.TrommelAsyncTask;

public class DebugStructuresCommand extends CommandBase {
	
	@Override
	public void init() {
		command = "/dbg structures ";
		displayName = "Debug Structures Commands";
		commands.put("trommel", "[name] - turn on this town's trommel debugging.");
		commands.put("quarry", "[name] - turn on this town's quarry debugging.");
		commands.put("fishery", "[name] - turn on this town's Fish Hatchery debugging.");
	}
	
	public void fishery_cmd() throws CivException {
		Town town = getNamedTown(1);
		if (FisheryAsyncTask.debugTowns.contains(town.getName())) {
			FisheryAsyncTask.debugTowns.remove(town.getName());
		} else {
			FisheryAsyncTask.debugTowns.add(town.getName());
		}
		CivMessage.send(sender, "Fishery toggled.");
	}
	
	public void trommel_cmd() throws CivException {
		Town town = getNamedTown(1);
		if (TrommelAsyncTask.debugTowns.contains(town.getName())) {
			TrommelAsyncTask.debugTowns.remove(town.getName());
		} else {
			TrommelAsyncTask.debugTowns.add(town.getName());
		}
		CivMessage.send(sender, "Trommel toggled.");
	}
	
	public void quarry_cmd() throws CivException {
		Town town = getNamedTown(1);
		if (QuarryAsyncTask.debugTowns.contains(town.getName())) {
			QuarryAsyncTask.debugTowns.remove(town.getName());
		} else {
			QuarryAsyncTask.debugTowns.add(town.getName());
		}
		CivMessage.send(sender, "Quarry toggled.");
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
