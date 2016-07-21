/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.items.components;

import gpl.AttributeUtil;

import org.bukkit.event.block.BlockPlaceEvent;

public class AllowBlockPlace extends ItemComponent {

	@Override
	public void onPrepareCreate(AttributeUtil attrUtil) {}
	
	@Override
	public boolean onBlockPlaced(BlockPlaceEvent event) { 
		return true; 
	}

}
