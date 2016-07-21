/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.util;

import org.bukkit.entity.Player;
import org.kitteh.vanish.staticaccess.VanishNoPacket;
import org.kitteh.vanish.staticaccess.VanishNotLoadedException;

@SuppressWarnings("deprecation")
public class VanishNoPacketUtil {

	public static boolean isVanished(Player player) {
		try {
			return VanishNoPacket.isVanished(player.getName());
		} catch (VanishNotLoadedException | NoClassDefFoundError e ) {
			return false;
		}
	}
	
}
