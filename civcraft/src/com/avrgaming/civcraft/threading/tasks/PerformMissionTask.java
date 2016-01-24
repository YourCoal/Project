package com.avrgaming.civcraft.threading.tasks;

import com.avrgaming.civcraft.config.ConfigMission;
import com.avrgaming.civcraft.items.units.MissionBook;

public class PerformMissionTask implements Runnable {
	ConfigMission mission;
	String playerName;
	
	public PerformMissionTask (ConfigMission mission, String playerName) {
		this.mission = mission;
		this.playerName = playerName;
	}
	
	
	@Override
	public void run() {
		MissionBook.performMission(mission, playerName);
	}

}
