package com.avrgaming.civcraft.threading.tasks;

import java.io.IOException;
import java.sql.SQLException;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.structure.Structure;
import com.avrgaming.civcraft.template.Template;

public class StartStructureBuild implements Runnable {

	public String playerName;
	public Structure struct;
	public Template tpl;
	public Location centerLoc;
	
	@Override
	public void run() {
		Player player;
		try {
			player = CivGlobal.getPlayer(playerName);
		} catch (CivException e1) {
			e1.printStackTrace();
			return;
		}

		try {
			struct.doBuild(player, centerLoc, tpl);
			struct.save();
		} catch (CivException e) {
			CivMessage.sendError(player, "Unable to build: "+e.getMessage());
		} catch (IOException e) {
			CivMessage.sendError(player, "Internal IO error.");
			e.printStackTrace();
		} catch (SQLException e) {
			CivMessage.sendError(player, "Internal SQL error.");
			e.printStackTrace();
		}
	}

}