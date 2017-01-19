package com.avrgaming.civcraft.threading.timers;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.util.CivColor;
import com.nametagedit.plugin.NametagEdit;

public class PlayerTagUpdateTimer implements Runnable {
	
	@Override
	public void run() {
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			Resident res = CivGlobal.getResident(p);
			String pName;
			if (res.getCiv() != null) {
				pName = CivColor.LightPurple+CivColor.BOLD+" ["+StringUtils.left(res.getCiv().getName(), 5)+"]";
			} else if (res.getCiv() == null && res.getCamp() != null) {
				pName = CivColor.Gold+CivColor.BOLD+" ["+StringUtils.left(res.getCamp().getName(), 5)+"]";
			} else {
				pName = CivColor.LightGray+CivColor.BOLD+" [-----]";
			}
			NametagEdit.getApi().setSuffix(p, pName);
			
			if (p.isOp()) {
				NametagEdit.getApi().setPrefix(p, CivColor.RedBold+"[A] "+CivColor.LightGray);
			}
		}
	}
}
