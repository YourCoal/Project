package com.avrgaming.civcraft.structure;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.avrgaming.civcraft.config.CivSettings;
import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.main.CivLog;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.StructureSign;
import com.avrgaming.civcraft.object.Town;
import com.avrgaming.civcraft.util.BlockCoord;
import com.avrgaming.civcraft.util.CivColor;
import com.avrgaming.civcraft.util.SimpleBlock;

public class Fishery extends Structure {
	
	public static final int MAX_CHANCE = CivSettings.getIntegerStructure("fishery.tierMax");
	public static final double FISH_T0_RATE = CivSettings.getDoubleStructure("fishery.t0_rate"); //100%
	public static final double FISH_T1_RATE = CivSettings.getDoubleStructure("fishery.t1_rate"); //100%
	public static final double FISH_T2_RATE = CivSettings.getDoubleStructure("fishery.t2_rate"); //100%
	public static final double FISH_T3_RATE = CivSettings.getDoubleStructure("fishery.t3_rate"); //100%
	public static final double FISH_T4_RATE = CivSettings.getDoubleStructure("fishery.t4_rate"); //100%
	
	private int level = 1;
	public int skippedCounter = 0;
	public ReentrantLock lock = new ReentrantLock();
	
	protected Fishery(Location center, String id, Town town) throws CivException {
		super(center, id, town);	
	}
	
	public Fishery(ResultSet rs) throws SQLException, CivException {
		super(rs);
	}
	
	@Override
	public String getDynmapDescription() {
		String out = "<u><b>"+this.getDisplayName()+"</u></b><br/>";
		out += "Level: "+this.level;
		return out;
	}
	
	@Override
	public String getMarkerIconName() {
		return "cutlery";
	}
	
	public double getChance(double chance) {
		return this.modifyChance(chance);
	}
	
	private double modifyChance(Double chance) {
		//No buffs at this time
		return chance;
	}
	
	@Override
	public void onPostBuild(BlockCoord absCoord, SimpleBlock commandBlock) {
		this.level = getTown().saved_fishery_level;
	}
	
	public int getLevel() {
		return level;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	private StructureSign getSignFromSpecialId(int special_id) {
		for (StructureSign sign : getSigns()) {
			int id = Integer.valueOf(sign.getAction());
			if (id == special_id) {
				return sign;
			}
		} return null;
	}
	
	@Override
	public void updateSignText() {
		int count = 0;
		for (count = 0; count < level; count++) {
			StructureSign sign = getSignFromSpecialId(count);
			if (sign == null) {
				CivLog.error("sign from special id was null, id:"+count);
				return;
			}
			sign.setText("Fishery\nPool\nOnline"+(count+1));
			sign.update();
		}
		for (; count < getSigns().size(); count++) {
			StructureSign sign = getSignFromSpecialId(count);
			if (sign == null) {
				CivLog.error("sign from special id was null, id:"+count);
				return;
			}
			sign.setText("Fishery\nPool\nOffline"+(count+1));
			sign.update();
		}
	}
	
	@Override
	public void processSignAction(Player player, StructureSign sign, PlayerInteractEvent event) {
		int special_id = Integer.valueOf(sign.getAction());
		if (special_id < this.level) {
			CivMessage.send(player, CivColor.LightGreen+"Fishery Pool "+(special_id+1)+" active.");
		} else {
			CivMessage.send(player, CivColor.LightGreen+"Fishery Pool "+(special_id+1)+" disabled. Upgrade the structure to enable.");
		}
	}

}