package com.avrgaming.civcraft.threading.timers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.structure.Buildable;
import com.avrgaming.civcraft.structure.Structure;
import com.avrgaming.civcraft.structure.wonders.Wonder;
import com.avrgaming.civcraft.util.CivColor;

import me.confuser.barapi.BarAPI;

public class BossBarUpdateTimer implements Runnable {
	
	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		try {
			for (Player p : Bukkit.getServer().getOnlinePlayers()) {
				Resident res = CivGlobal.getResident(p);
				
				int exposure = (int) res.getSpyExposure();
				BarAPI.setMessage(p, CivColor.GoldBold+"Spy Exposure: "+CivColor.LightGreen+exposure, 6);
				Thread.sleep(6000);
				
				String weather;
				if (p.getWorld().hasStorm() && p.getWorld().isThundering()) {
					weather = CivColor.LightGreen+"Storming";
				} else if (p.getWorld().hasStorm() && !p.getWorld().isThundering()) {
					weather = CivColor.LightBlue+"Raining";
				} else {
					weather = CivColor.Yellow+"Sunny";
				}
				BarAPI.setMessage(p, CivColor.GoldBold+"Weather: "+weather, 6);
				Thread.sleep(6000);
				
				String sp;
				Buildable b = res.getTown().getCurrentStructureInProgress();
				if (b != null && b instanceof Structure) {
					int percent = (int) (((double)b.getBuiltBlockCount() / (double)b.getTotalBlockCount())*100);	
					sp = CivColor.Yellow+b.getDisplayName()+": "+CivColor.ITALIC+"("+(percent)+"%)";
				} else {
					sp = CivColor.LightGray+"None";
				}
				BarAPI.setMessage(p, CivColor.GoldBold+"Structure: "+sp, 6);
				Thread.sleep(6000);
				
				String wp;
				Buildable w = res.getTown().getCurrentWonderInProgress();
				if (w != null && b instanceof Wonder) {
					int percent = (int) (((double)b.getBuiltBlockCount() / (double)b.getTotalBlockCount())*100);	
					wp = CivColor.LightPurple+b.getDisplayName()+": "+CivColor.ITALIC+"("+(percent)+"%)";
				} else {
					wp = CivColor.LightGray+"None";
				}
				BarAPI.setMessage(p, CivColor.GoldBold+"Wonder: "+wp, 6);
				Thread.sleep(6000);
				BarAPI.removeBar(p);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
