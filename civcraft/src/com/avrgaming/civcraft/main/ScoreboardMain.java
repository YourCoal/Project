package com.avrgaming.civcraft.main;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Scoreboard;

import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.util.CivColor;

public class ScoreboardMain {
	
	public  HashMap<String, Scoreboard> scoreboards = new HashMap<String, Scoreboard>();
	
	
	@SuppressWarnings("deprecation")
	public void addTeam(ScoreboardTeam team) throws CivException {
		this.scoreboards.put(team.getName(), ScoreboardManager.scoreboardManager.getNewScoreboard());
		team.setScoreboardTeam(getScoreboard(team.getName()).registerNewTeam(team.getName()));
		team.getScoreboardTeam().setAllowFriendlyFire(false);
		team.setTeamColor(CivColor.LightPurple);
		team.getScoreboardTeam().setPrefix(team.getTeamColor()+"["+team.getName()+"]");
		for (Resident resident : team.teamMembers) {
			try {
			CivGlobal.getPlayer(resident);
			} catch (CivException e) {
				continue;
			}
			team.getScoreboardTeam().addPlayer(Bukkit.getOfflinePlayer(resident.getUUID()));
		}
	}
	
	public Scoreboard getScoreboard(String name) {
		return this.scoreboards.get(name);
	}
}
