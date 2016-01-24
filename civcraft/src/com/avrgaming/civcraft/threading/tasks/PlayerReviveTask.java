package com.avrgaming.civcraft.threading.tasks;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.structure.TownHall;
import com.avrgaming.civcraft.util.BlockCoord;
import com.avrgaming.civcraft.util.CivColor;

public class PlayerReviveTask implements Runnable {

	String playerName;
	int timeout;
	TownHall townhall;
	Location alternativeLocation;
	
	public PlayerReviveTask(Player player, int timeout, TownHall townhall, Location alt) {
		this.playerName = player.getName();
		this.timeout = timeout;
		this.townhall = townhall;
		this.alternativeLocation = alt;
	}
	
	public void setRespawnViaSessionDB() {
		//Player was logged out when the respawn event fired. Create a sessionDB entry
		//to respawn the player when they login.
		BlockCoord revive = townhall.getRandomRevivePoint();
		CivGlobal.getSessionDB().add("global:respawnPlayer", playerName+":"+revive.toString(), 0, 0, 0);
	}
	
	@Override
	public void run() {
		
		Player player;
		try {
			player = CivGlobal.getPlayer(playerName);
		} catch (CivException e1) {
			setRespawnViaSessionDB();
			return;
		}
		
		CivMessage.send(player, CivColor.LightGray+"You will respawn in "+timeout+" seconds.");
		
		try {
			synchronized(this) {
				this.wait(timeout*1000);
			}
		} catch (InterruptedException e) {
		}
		
		try {
			player = CivGlobal.getPlayer(playerName);
		} catch (CivException e1) {
			setRespawnViaSessionDB();
			return;
		}
		
		BlockCoord revive = townhall.getRandomRevivePoint();
		Location loc;
		if (revive == null) {
			loc = alternativeLocation;
		} else {
			loc = revive.getLocation();
		}
		
		CivMessage.send(player, CivColor.LightGreen+"Respawning...");
		
		try {
			synchronized(this) {
				this.wait(500);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		player.teleport(loc);		
		
	}

	
	
}
