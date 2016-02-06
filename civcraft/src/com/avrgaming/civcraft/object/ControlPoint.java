package com.avrgaming.civcraft.object;

import com.avrgaming.civcraft.structure.Buildable;
import com.avrgaming.civcraft.util.BlockCoord;

public class ControlPoint {

	/* Location of the control block. */
	private BlockCoord coord;
	
	/* Hitpoints for this control block. */
	private int hitpoints;
	
	/* Max hitpoints for this control block. */
	private int maxHitpoints;
	
	/* TownHall this control point belongs to. */
	private Buildable buildable;

	public ControlPoint (BlockCoord coord, Buildable buildable, int hitpoints) {
		this.coord = coord;
		this.setBuildable(buildable);
		this.maxHitpoints = hitpoints;
		this.hitpoints = this.maxHitpoints;
	}
	
	/**
	 * @return the coord
	 */
	public BlockCoord getCoord() {
		return coord;
	}

	/**
	 * @param coord the coord to set
	 */
	public void setCoord(BlockCoord coord) {
		this.coord = coord;
	}

	/**
	 * @return the hitpoints
	 */
	public int getHitpoints() {
		return hitpoints;
	}

	/**
	 * @param hitpoints the hitpoints to set
	 */
	public void setHitpoints(int hitpoints) {
		this.hitpoints = hitpoints;
	}

	/**
	 * @return the maxHitpoints
	 */
	public int getMaxHitpoints() {
		return maxHitpoints;
	}

	/**
	 * @param maxHitpoints the maxHitpoints to set
	 */
	public void setMaxHitpoints(int maxHitpoints) {
		this.maxHitpoints = maxHitpoints;
	}

	public void damage(int amount) {
		if (this.hitpoints <= 0) {
			return;
		}
		
		this.hitpoints -= amount;
		
		if (this.hitpoints <= 0) {
			this.hitpoints = 0;
		}
		
	}
	
	public boolean isDestroyed() {
		if (this.hitpoints <= 0) {
			return true;
		}
		return false;
	}

	public Buildable getBuildable() {
		return buildable;
	}

	public void setBuildable(Buildable buildable) {
		this.buildable = buildable;
	}
}