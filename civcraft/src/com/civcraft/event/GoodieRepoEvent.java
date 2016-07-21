/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.event;

import java.util.Calendar;

import com.civcraft.config.CivSettings;
import com.civcraft.exception.InvalidConfiguration;
import com.civcraft.items.BonusGoodie;
import com.civcraft.main.CivGlobal;
import com.civcraft.main.CivLog;
import com.civcraft.main.CivMessage;
import com.civcraft.object.Town;
import com.civcraft.threading.TaskMaster;

public class GoodieRepoEvent implements EventInterface {

	public static void repoProcess() {
		class SyncTask implements Runnable {
			@Override
			public void run() {
				
				for (Town town : CivGlobal.getTowns()) {
					for (BonusGoodie goodie : town.getBonusGoodies()) {
						town.removeGoodie(goodie);
					}
				}
				
				for (BonusGoodie goodie : CivGlobal.getBonusGoodies()) {
					try {
						goodie.replenish();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
		}
		
		TaskMaster.syncTask(new SyncTask());
	}
	
	@Override
	public void process() {
		CivLog.info("TimerEvent: GoodieRepo -------------------------------------");
		repoProcess();
		CivMessage.global("Trade Goodies have been respawned at trade outposts.");
	}

	@Override
	public Calendar getNextDate() throws InvalidConfiguration {
		Calendar cal = EventTimer.getCalendarInServerTimeZone();
		int repo_days = CivSettings.getInteger(CivSettings.goodsConfig, "trade_goodie_repo_days");
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.add(Calendar.DATE, repo_days);
		return cal;
	}

}
