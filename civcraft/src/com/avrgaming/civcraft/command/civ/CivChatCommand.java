package com.avrgaming.civcraft.command.civ;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.util.CivColor;

public class CivChatCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		//TODO let non players use this command
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
			resident.setCivChat(!resident.isCivChat());
			resident.setTownChat(false);
			CivMessage.sendSuccess(sender, "Civ chat mode set to "+resident.isCivChat());
			return true;
		}
		
		
		String fullArgs = "";
		for (String arg : args) {
			fullArgs += arg + " ";
		}
	
		if (resident.getTown() == null) {
			player.sendMessage(CivColor.Rose+"You are not part of a civ, nobody hears you.");
			return false;
		}
		
		CivMessage.sendCivChat(resident.getTown().getCiv(), resident, "<%s> %s", fullArgs);
		return true;
	}
}
