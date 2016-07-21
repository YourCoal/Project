/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package mobs.moblib;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MobLibCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (sender instanceof Player) {

			Player player = (Player)sender;
			if (player.isOp()) {
				player.sendMessage("Spawning test yobo");
				MobLib.spawnCustom(args[0], player.getLocation());
			}
		}
		
		return false;
	}

}
