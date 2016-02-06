package com.avrgaming.civcraft.structure;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Location;

import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.object.Civilization;
import com.avrgaming.civcraft.object.Town;

public class NuclearPlant extends Structure {
	
	protected NuclearPlant(Location center, String id, Town town) throws CivException {
		super(center, id, town);
	}
	
	public NuclearPlant(ResultSet rs) throws SQLException, CivException {
		super(rs);
	}
	
	@Override
	public void loadSettings() {
		super.loadSettings();
	}
	
	@Override
	public String getMarkerIconName() {
		return "warning";
	}
	
	protected void removeBuffs() {
		this.removeBuffFromTown(this.getTown(), "buff_knowledge_advancement");
	}
	
	protected void addBuffs() {
		this.addBuffToTown(this.getTown(), "buff_knowledge_advancement");
	}
	
	protected void addBuffToTown(Town town, String id) {
		try {
			town.getBuffManager().addBuff(id, id, this.getDisplayName()+" in "+this.getTown().getName());
		} catch (CivException e) {
			e.printStackTrace();
		}
	}
	
	protected void addBuffToCiv(Civilization civ, String id) {
		for (Town t : civ.getTowns()) {
			addBuffToTown(t, id);
		}
	}
	
	protected void removeBuffFromTown(Town town, String id) {
		town.getBuffManager().removeBuff(id);
	}
	
	protected void removeBuffFromCiv(Civilization civ, String id) {
		for (Town t : civ.getTowns()) {
			removeBuffFromTown(t, id);
		}
	}
}