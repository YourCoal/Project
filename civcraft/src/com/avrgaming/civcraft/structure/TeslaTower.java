package com.avrgaming.civcraft.structure;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Location;

import com.avrgaming.civcraft.components.ProjectileLightningComponent;
import com.avrgaming.civcraft.config.CivSettings;
import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.exception.InvalidConfiguration;
import com.avrgaming.civcraft.object.Buff;
import com.avrgaming.civcraft.object.Town;
import com.avrgaming.civcraft.util.BlockCoord;

public class TeslaTower extends Structure {
	
	ProjectileLightningComponent teslaComponent;
	
	protected TeslaTower(Location center, String id, Town town)
			throws CivException {
		super(center, id, town);
		this.hitpoints = this.getMaxHitPoints();
	}
	
	protected TeslaTower(ResultSet rs) throws SQLException, CivException {
		super(rs);
	}
	
	@Override
	public void loadSettings() {
		super.loadSettings();
		teslaComponent = new ProjectileLightningComponent(this, this.getCenterLocation().getLocation()); 
		teslaComponent.createComponent(this);
	}
	
	public int getDamage() {
		double rate = 1;
		return (int)(teslaComponent.getDamage()*rate);
	}
	
	@Override
	public int getMaxHitPoints() {
		double rate = 1;
		if (this.getTown().getBuffManager().hasBuff("buff_chichen_itza_tower_hp")) {
			rate += this.getTown().getBuffManager().getEffectiveDouble("buff_chichen_itza_tower_hp");
			rate += this.getTown().getBuffManager().getEffectiveDouble(Buff.BARRICADE);
		} return (int) (info.max_hitpoints * rate);
	}
	
	public void setTurretLocation(BlockCoord absCoord) {
		teslaComponent.setTurretLocation(absCoord);
	}
	
	@Override
	public void onCheck() throws CivException {
		try {
			double build_distance = CivSettings.getDouble(CivSettings.warConfig, "tesla_tower.build_distance");
			for (Town town : this.getTown().getCiv().getTowns()) {
				for (Structure struct : town.getStructures()) {
					if (struct instanceof TeslaTower) {
						BlockCoord center = struct.getCenterLocation();
						double distance = center.distance(this.getCenterLocation());
						if (distance <= build_distance) {
							throw new CivException("Too close to another Tesla Tower at "+(center.getX()+","+center.getY()+","+center.getZ()));
						}
					}
				}
			}
		} catch (InvalidConfiguration e) {
			e.printStackTrace();
			throw new CivException(e.getMessage());
		}
	}
}
