/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.object;

import com.civcraft.structure.Buildable;
import com.civcraft.util.BlockCoord;

public class StructureChest {

	private BlockCoord coord;
	private Buildable owner;
	private int direction;
	
	/* The chest id defines which chests are 'paired' for double chests. */
	private int chestId;
	
	public StructureChest(BlockCoord coord, Buildable owner) {
		this.setCoord(coord);
		this.setOwner(owner);
	}
	
	public BlockCoord getCoord() {
		return coord;
	}

	public void setCoord(BlockCoord coord) {
		this.coord = coord;
	}

	public Buildable getOwner() {
		return owner;
	}

	public void setOwner(Buildable owner) {
		this.owner = owner;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public int getChestId() {
		return chestId;
	}

	public void setChestId(int chestId) {
		this.chestId = chestId;
	}

	
	
}
