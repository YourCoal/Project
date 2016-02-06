package com.avrgaming.civcraft.event;

import java.util.Calendar;

import com.avrgaming.civcraft.config.CivSettings;
import com.avrgaming.civcraft.exception.InvalidConfiguration;
import com.avrgaming.civcraft.items.BonusGoodie;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.main.CivLog;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.Town;
import com.avrgaming.civcraft.threading.TaskMaster;
import com.avrgaming.civcraft.util.CivColor;

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
		try {
			CivMessage.global("Trade Repo in 10 minutes.");
			Thread.sleep(540000);
			CivMessage.global("Trade Repo in 1 minute.");
			Thread.sleep(30000);
			CivMessage.global("Trade Repo in 30 seconds.");
			Thread.sleep(20000);
			CivMessage.global("Trade Repo in 10 seconds.");
			Thread.sleep(10000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		CivLog.info("TimerEvent: GoodieRepo -------------------------------------");
		repoProcess();
		CivMessage.globalTitle(CivColor.BOLD+"Trade Repo", CivColor.LightGray+CivColor.ITALIC+"All trade goods have been returned to their locations.");
	}
	
	@Override
	public Calendar getNextDate() throws InvalidConfiguration {
		Calendar cal = EventTimer.getCalendarInServerTimeZone();
		int repo_days = CivSettings.getInteger(CivSettings.goodsConfig, "trade_goodie_repo_days");
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 50);
		cal.add(Calendar.DATE, repo_days);
		return cal;
	}
}
