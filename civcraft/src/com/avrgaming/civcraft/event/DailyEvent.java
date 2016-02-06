package com.avrgaming.civcraft.event;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.avrgaming.civcraft.config.CivSettings;
import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.exception.InvalidConfiguration;
import com.avrgaming.civcraft.main.CivLog;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.threading.TaskMaster;
import com.avrgaming.civcraft.threading.tasks.CultureProcessAsyncTask;
import com.avrgaming.civcraft.threading.timers.DailyTimer;
import com.avrgaming.civcraft.util.CivColor;

public class DailyEvent implements EventInterface {
	
	public static Boolean dailyTimerFinished = true;
	
	public static int dayExecuted = 0;
	
	@Override
	public void process() {
		try {
			CivMessage.global("Daily Timer running in 10 minutes.");
			Thread.sleep(540000);
			CivMessage.global("Daily Timer running in 1 minute.");
			Thread.sleep(30000);
			CivMessage.global("Daily Timer running in 30 seconds.");
			Thread.sleep(20000);
			CivMessage.global("Daily Timer running in 10 seconds.");
			Thread.sleep(10000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		CivLog.info("TimerEvent: Daily -------------------------------------");
		while (!CultureProcessAsyncTask.cultureProcessedSinceStartup) {
			CivLog.info("DailyTimer: Waiting for culture to finish processing.");
			try {
				Thread.sleep(10*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}
		}
		CivMessage.globalTitle(CivColor.BOLD+"Daily Event", CivColor.LightGray+CivColor.ITALIC+"Upkeep, Taxes, and Debt are being processed.");
		//TODO I don't think this timer needs to be synchronous.. we can find a way.
		CivLog.info("Daily timer was finished, starting a new timer.");
		Calendar cal = Calendar.getInstance();
		if (dayExecuted != cal.get(Calendar.DAY_OF_MONTH)) {
			dayExecuted = cal.get(Calendar.DAY_OF_MONTH);
			TaskMaster.syncTask(new DailyTimer(), 0);
		} else {
			try {
				throw new CivException("TRIED TO EXECUTE DAILY EVENT TWICE: "+dayExecuted);
			} catch (CivException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public Calendar getNextDate() throws InvalidConfiguration {
		Calendar cal = EventTimer.getCalendarInServerTimeZone();
		int daily_upkeep_hour;
		daily_upkeep_hour = CivSettings.getInteger(CivSettings.civConfig, "global.daily_upkeep_hour");
		cal.set(Calendar.MINUTE, 50);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.HOUR_OF_DAY, daily_upkeep_hour);

		Calendar now = Calendar.getInstance();
		if (now.after(cal)) {
			cal.add(Calendar.DATE, 1);
			cal.set(Calendar.HOUR_OF_DAY, daily_upkeep_hour);
			cal.set(Calendar.MINUTE, 50);
			cal.set(Calendar.SECOND, 0);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("M/dd h:mm:ss a z");
		CivLog.info("Setting Next Daily Event:"+sdf.format(cal.getTime()));
		return cal;		
	}
}