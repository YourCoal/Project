/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.threading.tasks;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.civcraft.main.CivGlobal;
import com.civcraft.object.Civilization;
import com.civcraft.object.Resident;
import com.civcraft.threading.TaskMaster;
import com.civcraft.threading.sync.SyncUpdateTagsBetweenCivs;

public class UpdateTagBetweenCivsTask implements Runnable {

	Civilization civ;
	Civilization otherCiv;
	
	public UpdateTagBetweenCivsTask(Civilization civ, Civilization otherCiv) {
		this.civ = civ;
		this.otherCiv = otherCiv;
	}
	
	@Override
	public void run() {
		Set<Player> civList = new HashSet<Player>();
		Set<Player> otherCivList = new HashSet<Player>();
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			Resident resident = CivGlobal.getResident(player);
			if (resident == null || !resident.hasTown()) {
				continue;
			}
			
			if (resident.getTown().getCiv() == civ) {
				civList.add(player);
			} else if (resident.getTown().getCiv() == otherCiv) {
				otherCivList.add(player);
			}
		}
		
		TaskMaster.syncTask(new SyncUpdateTagsBetweenCivs(civList, otherCivList));		
	}

}
