package com.avrgaming.civcraft.listener.minecraft;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

import com.avrgaming.civcraft.config.CivSettings;
import com.avrgaming.civcraft.exception.InvalidConfiguration;
import com.avrgaming.civcraft.main.CivLog;
import com.avrgaming.civcraft.main.CivMessage;

/* https://github.com/gvlfm78/BukkitOldCombatMechanics */

public class MinecraftVersionListener implements Listener {
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onSwapHandItems(PlayerSwapHandItemsEvent e) {
		boolean enabled = false;
		try {
			if (CivSettings.getString(CivSettings.gameConfig, "inventory.allow_offhand") != "false") {
				enabled = true;
			}
		} catch (InvalidConfiguration e1) {
			e1.printStackTrace();
		}
		
		if (enabled == false) {
			CivMessage.sendError(e.getPlayer(), "You cannot switch items to off-hand!");
			e.setCancelled(true);
		}
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onInventoryClick(InventoryClickEvent e){
		if (!e.getInventory().getType().equals(InventoryType.CRAFTING)) {
			return; //Making sure it's a survival player's inventory
		}
		
		if (e.getSlot() != 40) {
			return; // If they didn't click into the offhand slot, return
		}
		
		if (!e.getCurrentItem().getType().equals(Material.AIR) && e.getCursor().getType().equals(Material.AIR)) {
			return; // If the slot is not empty, allow taking the item
		}
		
		if(shouldWeCancel(e.getCursor())){
			e.setResult(Event.Result.DENY);
			e.setCancelled(true);
		}
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onInventoryDrag(InventoryDragEvent e){
		if(!e.getInventory().getType().equals(InventoryType.CRAFTING) || !e.getInventorySlots().contains(40)) {
			return;
		}
		
		if(shouldWeCancel(e.getOldCursor())){
			e.setResult(Event.Result.DENY);
			e.setCancelled(true);
		}
	}
	
	private static ArrayList<Material> mats = new ArrayList<Material>();
	public boolean shouldWeCancel(ItemStack item) {		
		Material mat = item.getType();
		boolean isContained = mats.contains(mat);
		if(!isContained && !mat.equals(Material.AIR)) {
			return true;
		}
		return false;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerLogin(PlayerJoinEvent e) {
		boolean enabled = true;
		int GAS = 4;
		try {
			GAS = CivSettings.getInteger(CivSettings.gameConfig, "pvp.attack_speed");
			if (CivSettings.getString(CivSettings.gameConfig, "pvp.attack_cooldown_enabled") != "true") {
				enabled = false;
			}
		} catch (InvalidConfiguration e1) {
			e1.printStackTrace();
		}
		
		Player p = e.getPlayer();
		AttributeInstance attribute = p.getAttribute(Attribute.GENERIC_ATTACK_SPEED);
		double baseValue = attribute.getBaseValue();

		if (enabled == false) {
			GAS = 4; //If module is disabled, set attack speed to 1.9 default
			CivLog.debug("[Player Join] game.yml-pvp.attack_speed set to 4");
		}
		
		if (baseValue != GAS) {
			attribute.setBaseValue(GAS);
			CivLog.debug("[Player Join] game.yml-pvp.attack_speed set to "+GAS);
			p.saveData();
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerQuit(PlayerQuitEvent e){
		Player player = e.getPlayer();
		AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_ATTACK_SPEED);
		double baseValue = attribute.getBaseValue();
		if (baseValue != 4){ //If basevalue is not 1.9 default, set it back
			attribute.setBaseValue(4);
			CivLog.debug("[Player Leave] game.yml-pvp.attack_speed set to 4");
			player.saveData();
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onWorldChange(PlayerChangedWorldEvent e) {
		Player player = e.getPlayer();
		boolean enabled = true;
		int GAS = 4;
		try {
			GAS = CivSettings.getInteger(CivSettings.gameConfig, "pvp.attack_speed");
			if (CivSettings.getString(CivSettings.gameConfig, "pvp.attack_cooldown_enabled") != "true") {
				enabled = false;
			}
		} catch (InvalidConfiguration e1) {
			e1.printStackTrace();
		}
		
		AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_ATTACK_SPEED);
		double baseValue = attribute.getBaseValue();
		
		if (enabled == false) {
			GAS = 4; //If module is disabled, set attack speed to 1.9 default
			CivLog.debug("[Player World Change] game.yml-pvp.attack_speed set to 4");
		}
		
		if (baseValue!=GAS){
			attribute.setBaseValue(GAS);
			CivLog.debug("[Player World Change] game.yml-pvp.attack_speed set to "+GAS);
			player.saveData();
		}
	}
	
	
	
/*	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDamaged(EntityDamageByEntityEvent e) {
		if (!(e.getDamager() instanceof Player)) {
			return;
		}
		
		Player p = (Player) e.getDamager();
		Material mat = p.getInventory().getItemInMainHand().getType();
		onSwordAttack(e, p, mat);
	}
	
	private void onSwordAttack(EntityDamageByEntityEvent e, Player p, Material mat) {
		//Disable sword sweep
		int locHashCode = p.getLocation().hashCode(); // ATTACKER
		if (e.getDamage() == 1.0) {
			// Possibly a sword sweep attack
			if (sweepTask().swordLocations.contains(locHashCode)) {
				e.setCancelled(true);
			}
		} else {
			sweepTask().swordLocations.add(locHashCode);
		}
		e.setDamage(e.getDamage());
	}

	public SweepTask sweepTask() {
		return ((CivCraft) CivCraft.getPlugin()).sweepTask();
	}*/
}
