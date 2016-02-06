package com.avrgaming.civcraft.components;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import com.avrgaming.civcraft.config.CivSettings;
import com.avrgaming.civcraft.exception.InvalidConfiguration;
import com.avrgaming.civcraft.object.Town;
import com.avrgaming.civcraft.structure.Buildable;
import com.avrgaming.civcraft.util.BlockCoord;

public class ProjectileLightningComponent extends ProjectileComponent {
	
	private int fireRate;
	private int halfSecondCount = 0;
	
	public ProjectileLightningComponent(Buildable buildable, Location turretCenter) {
		super(buildable, turretCenter);
	}
	
	@Override
	public void fire(Location turretLoc, Entity targetEntity) {
		if (halfSecondCount < fireRate) {
			halfSecondCount++;
			return;
		} else {
			halfSecondCount = 0;
		}
		World world = turretLoc.getWorld();
		Location location = targetEntity.getLocation();
		world.strikeLightning(location);
	}
	
	@Override
	public void loadSettings() {
		try {
			setDamage(CivSettings.getInteger(CivSettings.warConfig, "tesla_tower.damage"));
			range = CivSettings.getDouble(CivSettings.warConfig, "tesla_tower.range");
			if (this.getTown().getBuffManager().hasBuff("buff_great_lighthouse_tower_range") && this.getBuildable().getConfigId().equals("s_teslatower")) {
				range *= this.getTown().getBuffManager().getEffectiveDouble("buff_great_lighthouse_tower_range");
			}
			min_range = CivSettings.getDouble(CivSettings.warConfig, "tesla_tower.min_range");
			fireRate = CivSettings.getInteger(CivSettings.warConfig, "tesla_tower.fire_rate");
			this.proximityComponent.setBuildable(buildable);
			this.proximityComponent.setCenter(new BlockCoord(getTurretCenter()));
			this.proximityComponent.setRadius(range);
		} catch (InvalidConfiguration e) {
			e.printStackTrace();
		}
	}
	
	public int getHalfSecondCount() {
		return halfSecondCount;
	}
	
	public void setHalfSecondCount(int halfSecondCount) {
		this.halfSecondCount = halfSecondCount;
	}
	
	public Town getTown() {
		return buildable.getTown();
	}
}