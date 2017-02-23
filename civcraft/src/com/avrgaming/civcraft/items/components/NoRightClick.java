package com.avrgaming.civcraft.items.components;

import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import gpl.AttributeUtil;

public class NoRightClick extends ItemComponent {

	@Override
	public void onPrepareCreate(AttributeUtil attrUtil) {
	}

	
	@SuppressWarnings("deprecation")
	public void onInteract(PlayerInteractEvent event) {
		if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if (event.getClickedBlock().getType() != Material.CHEST && event.getClickedBlock().getType() != Material.TRAPPED_CHEST
					&& event.getClickedBlock().getType() != Material.ENDER_CHEST && event.getClickedBlock().getType() != Material.WORKBENCH
					&& event.getClickedBlock().getType() != Material.FURNACE && event.getClickedBlock().getType() != Material.BURNING_FURNACE
					&& event.getClickedBlock().getType() != Material.BREWING_STAND && event.getClickedBlock().getType() != Material.BEACON
					&& event.getClickedBlock().getType() != Material.ANVIL && event.getClickedBlock().getType() != Material.ENCHANTMENT_TABLE
					&& event.getClickedBlock().getType() != Material.HOPPER && event.getClickedBlock().getType() != Material.TRAP_DOOR) {
				event.getPlayer().updateInventory();
				event.setCancelled(true);
				return;
			} else {
				event.getPlayer().updateInventory();
				event.setCancelled(true);
				return;
			}
		}
	}
	
	@Override
	public boolean onBlockPlaced(BlockPlaceEvent event) { 
		return false; 
	}
	
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		event.setCancelled(true);
	}
}
