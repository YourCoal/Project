package com.avrgaming.civcraft.threading.timers;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.object.Resident;

import net.minecraft.server.v1_11_R1.IChatBaseComponent;
import net.minecraft.server.v1_11_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_11_R1.PacketPlayOutChat;

public class ActionBarUpdateTimer implements Runnable {
	
	@Override
	public void run() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			Resident res = CivGlobal.getResident(p);
			try {
				Player pl = CivGlobal.getPlayer(res);
				
				String town = res.getTown().getName();
				double treasury = res.getTown().getTreasury().getBalance();
				
				sendActionBar(pl, "Town: "+town+"  Treasury: "+treasury);
			} catch (CivException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void sendActionBar(Player p, String msg) {
		IChatBaseComponent cbc = ChatSerializer.a("{\"text\": \"" + msg +"\"}");
		PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc,(byte) 2);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(ppoc);
	}
}
