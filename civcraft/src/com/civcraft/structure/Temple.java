/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.structure;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import com.civcraft.config.CivSettings;
import com.civcraft.config.ConfigTempleSacrifice;
import com.civcraft.exception.CivException;
import com.civcraft.main.CivMessage;
import com.civcraft.object.Town;
import com.civcraft.util.CivColor;

public class Temple extends Structure {

	protected Temple(Location center, String id, Town town) throws CivException {
		super(center, id, town);
	}

	public Temple(ResultSet rs) throws SQLException, CivException {
		super(rs);
	}
	
	@Override
	public String getDynmapDescription() {
		return null;
	}
	
	@Override
	public String getMarkerIconName() {
		return "church";
	}
	

	public void onEntitySacrifice(EntityType entityType) {
		ConfigTempleSacrifice sac = null;
		for (ConfigTempleSacrifice s : CivSettings.templeSacrifices) {
			for (String str : s.entites) {				
				if (str.equalsIgnoreCase(entityType.toString())) {
					sac = s;
					break;
				}
			}
		}
		
		if (sac == null) {
			return;
		}
		
		this.getTown().addAccumulatedCulture(sac.reward);
		CivMessage.sendTown(this.getTown(), "Our Sacrifice has awarded our town "+CivColor.LightPurple+sac.reward+CivColor.White+" culture.");
		this.getTown().save();
	}


}
