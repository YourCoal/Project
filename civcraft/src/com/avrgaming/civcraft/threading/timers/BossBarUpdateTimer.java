package com.avrgaming.civcraft.threading.timers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.avrgaming.civcraft.main.CivCraft;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.structure.Buildable;
import com.avrgaming.civcraft.structure.Structure;
import com.avrgaming.civcraft.structure.wonders.Wonder;
import com.avrgaming.civcraft.util.CivColor;
import com.avrgaming.civcraft.war.War;

import me.confuser.barapi.BarAPI;

public class BossBarUpdateTimer implements Runnable {
	
	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		if (CivCraft.isDisable) {
			return;
		}
		
		try {
			for (Player p : Bukkit.getServer().getOnlinePlayers()) {
				Resident res = CivGlobal.getResident(p);
				
				if (War.isWarTime()) {
					BarAPI.removeBar(p);
					return;
				}
				
				BarAPI.removeBar(p);
				String weather;
				if (p.getWorld().hasStorm() && p.getWorld().isThundering()) {
					weather = CivColor.LightGreen+"Storming";
				} else if (p.getWorld().hasStorm() && !p.getWorld().isThundering()) {
					weather = CivColor.LightBlue+"Raining";
				} else {
					weather = CivColor.Yellow+"Sunny";
				}
				BarAPI.setMessage(p, CivColor.GoldBold+"Weather: "+weather, 5);
				Thread.sleep(4900);
				BarAPI.removeBar(p);
				
				if (CivCraft.isDisable) {
					return;
				}
				
				String sp;
				if (!res.hasTown()) {
					sp = CivColor.Rose+"No Town";
				} else {
					Buildable b = res.getTown().getCurrentStructureInProgress();
					if (b != null && b instanceof Structure) {
						int percent = (int) (((double)b.getBuiltBlockCount() / (double)b.getTotalBlockCount())*100);	
						sp = CivColor.Yellow+b.getDisplayName()+": "+CivColor.ITALIC+"("+(percent)+"%)";
					} else {
						sp = CivColor.LightGray+"None";
					}
				}
				BarAPI.setMessage(p, CivColor.GoldBold+"Structure: "+sp, 5);
				Thread.sleep(4900);
				BarAPI.removeBar(p);
				
				if (CivCraft.isDisable) {
					return;
				}
				
				String wp;
				if (!res.hasTown()) {
					wp = CivColor.Rose+"No Town";
				} else {
					Buildable w = res.getTown().getCurrentWonderInProgress();
					if (w != null && w instanceof Wonder) {
						int percent = (int) (((double)w.getBuiltBlockCount() / (double)w.getTotalBlockCount())*100);	
						wp = CivColor.LightPurple+w.getDisplayName()+": "+CivColor.ITALIC+"("+(percent)+"%)";
					} else {
						wp = CivColor.LightGray+"None";
					}
				}
				BarAPI.setMessage(p, CivColor.GoldBold+"Wonder: "+wp, 5);
				Thread.sleep(4900);
				BarAPI.removeBar(p);
				
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			for (Player p : Bukkit.getServer().getOnlinePlayers()) {
				BarAPI.removeBar(p);
			}
		}
	}
}
