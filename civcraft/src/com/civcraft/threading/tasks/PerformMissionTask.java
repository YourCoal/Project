/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.threading.tasks;

import com.civcraft.config.ConfigMission;
import com.civcraft.items.units.MissionBook;

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
