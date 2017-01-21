package com.avrgaming.civcraft.threading.timers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.avrgaming.civcraft.listener.ActionBar;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.util.CivColor;

public class ActionBarUpdateTimer implements Runnable {
	
	@Override
	public void run() {
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			Resident res = CivGlobal.getResident(p);
			
			String citLevel = "Coming Soon";
			String citXP = " (0/64)";
			int exposure = (int) res.getSpyExposure();
			
			ActionBar abl = new ActionBar(
					CivColor.Gold+CivColor.BOLD+" Citizen Level: "+CivColor.LightGreen+CivColor.ITALIC+citLevel+citXP
					+CivColor.Gray+CivColor.BOLD+" |"
					+CivColor.Gold+CivColor.BOLD+" Spy Exposure: "+CivColor.LightGreen+CivColor.ITALIC+exposure);
			abl.sendToAll();
		}
	}
}
