/**
 * CivCraft Created by - AVRGAMING LLC
 * This Code Modified by - https://www.youtube.com/user/cpcole556
 **/
package com.avrgaming.civcraft.structure.farm;

import com.avrgaming.civcraft.util.BlockCoord;

public class GrowBlock {
	
	public GrowBlock(String world, int x, int y, int z, int typeid2, int data2, boolean spawn2) {
		this.bcoord = new BlockCoord(world, x, y, z);
		this.typeId = typeid2;
		this.data = data2;
		this.spawn = spawn2;
	}
	
	public BlockCoord bcoord;
	public int typeId;
	public int data;
	public boolean spawn;
}
