package com.avrgaming.civcraft.threading.timers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.avrgaming.civcraft.listener.ActionBar;
import com.avrgaming.civcraft.main.CivCraft;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.util.CivColor;

public class ActionBarUpdateTimer implements Runnable {
	
	@Override
	public void run() {
		if (CivCraft.isDisable) {
			return;
		}
		
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			Resident res = CivGlobal.getResident(p);
			
			if (res != null) {
//				String citLevel = "Coming Soon";
//				String citXP = " (0/64)";
				int exposure = (int) res.getSpyExposure();
				
				ActionBar abl = new ActionBar(
//						CivColor.GoldBold+" Citizen Level: "+CivColor.LightGreenItalic+citLevel+citXP
//						+CivColor.GrayBold+" |"
//						+CivColor.GoldBold+" Spy Exposure: "+CivColor.LightGreenItalic+exposure);
						CivColor.GoldBold+" Spy Exposure: "+CivColor.LightGreenItalic+exposure);
				abl.sendToAll();
			} else {
				ActionBar abl = new ActionBar(CivColor.RoseItalic+" Spy Exposure: null");
				abl.sendToAll();
			}
		}
	}
}
