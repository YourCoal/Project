/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.event;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.civcraft.config.CivSettings;
import com.civcraft.exception.InvalidConfiguration;
import com.civcraft.main.CivLog;
import com.civcraft.mobs.CommonCustomMob;
import com.civcraft.mobs.MobSpawner;

public class AutoClearMobEvent implements EventInterface {
	
	@Override
	public void process() {
		String auto_clear_mobs = null;
		try {
			auto_clear_mobs = CivSettings.getString(CivSettings.timerConfig, "mob_spawner_timer");
		} catch (InvalidConfiguration e) {
			e.printStackTrace();
		}
		
		if (auto_clear_mobs == "true") {
		CivLog.info("TimerEvent: Clear Mobs -------------------------------------");
		CommonCustomMob.customMobs.remove("Yobo");
		CommonCustomMob.customMobs.remove("AngryYobo");
		CommonCustomMob.customMobs.remove("Behemoth");
		CommonCustomMob.customMobs.remove("Ruffian");
		CommonCustomMob.customMobs.remove("Savage");
		MobSpawner.despawnAll();
		CivLog.info("TimerEvent: Clear Mobs Finished ----------------------------");
		} else {
			CivLog.info("TimerEvent: Did Not Clear Mobs -- Event Disabled in timer.yml");
		}
	}
	
	@Override
	public Calendar getNextDate() throws InvalidConfiguration {
		SimpleDateFormat sdf = new SimpleDateFormat("M/dd h:mm:ss a z");
		Calendar cal = EventTimer.getCalendarInServerTimeZone();
		int hours_peroid = CivSettings.getInteger(CivSettings.timerConfig, "hours_tick");
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.add(Calendar.SECOND, hours_peroid);
		sdf.setTimeZone(cal.getTimeZone());
		return cal;
	}
}
