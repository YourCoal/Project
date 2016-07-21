/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.command.town;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.civcraft.main.CivGlobal;
import com.civcraft.main.CivMessage;
import com.civcraft.object.Resident;
import com.civcraft.util.CivColor;

public class TownChatCommand implements CommandExecutor {

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
			resident.setTownChat(!resident.isTownChat());
			resident.setCivChat(false);
			CivMessage.sendSuccess(sender, "Town chat mode set to "+resident.isTownChat());
			return true;
		}
		
		
		String fullArgs = "";
		for (String arg : args) {
			fullArgs += arg + " ";
		}
	
		if (resident.getTown() == null) {
			player.sendMessage(CivColor.Rose+"You are not part of a town, nobody hears you.");
			return false;
		}
		CivMessage.sendTownChat(resident.getTown(), resident, "<%s> %s", fullArgs);
		return true;
	}

}
