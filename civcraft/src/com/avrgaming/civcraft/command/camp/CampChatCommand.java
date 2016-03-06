package com.avrgaming.civcraft.command.camp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.util.CivColor;

public class CampChatCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if ((sender instanceof Player) == false) {
			return false;
		}
		
		Player player = (Player)sender;
		Resident resident = CivGlobal.getResident(player);
		if (resident == null) {
			CivMessage.sendError(sender, "You are not a resident? Relogin please..");
			return false;
		}
		
		if (args.length == 0) {
			resident.setCampChat(!resident.isCampChat());
			resident.setTownChat(false);
			resident.setCivChat(false);
			CivMessage.sendSuccess(sender, "Camp chat mode set to "+resident.isCampChat());
			return true;
		}
		
		String fullArgs = "";
		for (String arg : args) {
			fullArgs += arg + " ";
		}
		
		if (resident.getTown() == null) {
			player.sendMessage(CivColor.Rose+"You are not part of a camp, nobody hears you.");
			return false;
		}
		CivMessage.sendCampChat(resident.getCamp(), resident, "<%s> %s", fullArgs);
		return true;
	}

}
