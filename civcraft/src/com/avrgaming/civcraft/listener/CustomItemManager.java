/*************************************************************************
 * 
 * AVRGAMING LLC
 * __________________
 * 
 *  [2013] AVRGAMING LLC
 *  All Rights Reserved.
 * 
 * NOTICE:  All information contained herein is, and remains
 * the property of AVRGAMING LLC and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to AVRGAMING LLC
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from AVRGAMING LLC.
 */
package com.avrgaming.civcraft.listener;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import org.bukkit.CropState;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.material.Crops;

import com.avrgaming.civcraft.cache.ArrowFiredCache;
import com.avrgaming.civcraft.cache.CivCache;
import com.avrgaming.civcraft.config.CivSettings;
import com.avrgaming.civcraft.config.ConfigRemovedRecipes;
import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.exception.InvalidConfiguration;
import com.avrgaming.civcraft.items.ItemDurabilityEntry;
import com.avrgaming.civcraft.items.components.Catalyst;
import com.avrgaming.civcraft.loreenhancements.LoreEnhancement;
import com.avrgaming.civcraft.lorestorage.ItemChangeResult;
import com.avrgaming.civcraft.lorestorage.LoreCraftableMaterial;
import com.avrgaming.civcraft.lorestorage.LoreGuiItem;
import com.avrgaming.civcraft.lorestorage.LoreMaterial;
import com.avrgaming.civcraft.main.CivData;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.main.CivLog;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.threading.TaskMaster;
import com.avrgaming.civcraft.util.CivColor;
import com.avrgaming.civcraft.util.ItemManager;

import gpl.AttributeUtil;

public class CustomItemManager implements Listener {
	
	public static HashMap<String, LinkedList<ItemDurabilityEntry>> itemDuraMap = new HashMap<String, LinkedList<ItemDurabilityEntry>>();
	public static boolean duraTaskScheduled = false;
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEnchantItemEvent(EnchantItemEvent event) {
		CivMessage.sendError(event.getEnchanter(), "Items cannot be enchanted with enchantment tables.");
		event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockBreak(BlockBreakEvent event) {
//		this.onItemDurabilityChange(event.getPlayer(), event.getPlayer().getInventory().getItemInMainHand());
//		for (ItemStack stack : event.getBlock().getDrops()) {
//			event.setCancelled(true);
//			event.getBlock().setType(Material.AIR);
//			event.getPlayer().getWorld().dropItem(new Location(event.getBlock().getWorld(), event.getBlock().getX(), event.getBlock().getY()+0.5, event.getBlock().getZ()), stack);
//		}
	}
	
	//XXX Fully Grown Wheat Crop
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerWheatCropBreak(BlockBreakEvent event) {
		if (event.getBlock().getType().equals(Material.CROPS)) {
			Crops crops = (Crops) event.getBlock().getState().getData();
			if (crops.getState() != CropState.RIPE) {
				return;
			}
			
			event.setCancelled(true);
			ItemManager.setTypeIdAndData(event.getBlock(), CivData.AIR, (byte)0, true);
			try {
				Random randSeeds = new Random();
				int minSeeds = CivSettings.getInteger(CivSettings.dropsConfig, "wheatcrop.seeds.min");
				int maxSeeds;
				if (event.getPlayer().getInventory().getItemInMainHand().containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
					maxSeeds = CivSettings.getInteger(CivSettings.dropsConfig, "wheatcrop.seeds.max_fortune");
				} else {
					maxSeeds = CivSettings.getInteger(CivSettings.dropsConfig, "wheatcrop.seeds.max");
				}
				int randSeedAmt = randSeeds.nextInt(minSeeds + maxSeeds);
				randSeedAmt -= minSeeds;
				if (randSeedAmt <= 0) {
					randSeedAmt = 1;
				}
				
				for (int i = 0; i < randSeedAmt; i++) {
					int randExtraSeedsAmt = randSeeds.nextInt(1 + 40);
					int randExtraSeedsNum = randSeeds.nextInt(1 + 40);
					ItemStack stackSeeds = new ItemStack(Material.SEEDS);
					if (randExtraSeedsAmt == randExtraSeedsNum) {
						event.getPlayer().getWorld().dropItem(new Location(event.getBlock().getWorld(), event.getBlock().getX(), event.getBlock().getY()+0.5, event.getBlock().getZ()), stackSeeds);
						event.getPlayer().getWorld().dropItem(new Location(event.getBlock().getWorld(), event.getBlock().getX(), event.getBlock().getY()+0.5, event.getBlock().getZ()), stackSeeds);
						CivMessage.send(event.getPlayer(), CivColor.LightGray+"Double Seeds!");
					} else {
						event.getPlayer().getWorld().dropItem(new Location(event.getBlock().getWorld(), event.getBlock().getX(), event.getBlock().getY()+0.5, event.getBlock().getZ()), stackSeeds);
					}
				}
				
				Random randWheat = new Random();
				int minWheat = CivSettings.getInteger(CivSettings.dropsConfig, "wheatcrop.wheat.min");
				int maxWheat;
				if (event.getPlayer().getInventory().getItemInMainHand().containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
					maxWheat = CivSettings.getInteger(CivSettings.dropsConfig, "wheatcrop.wheat.max_fortune");
				} else {
					maxWheat = CivSettings.getInteger(CivSettings.dropsConfig, "wheatcrop.wheat.max");
				}
				int randWheatAmt = randWheat.nextInt(minWheat + maxWheat);
				randWheatAmt -= minWheat;
				if (randWheatAmt <= 0) {
					randWheatAmt = 1;
				}
				
				for (int i = 0; i < randWheatAmt; i++) {
					ItemStack stackWheat = new ItemStack(Material.WHEAT);
					int randExtraWheatAmt = randSeeds.nextInt(1 + 40);
					int randExtraWheatNum = randSeeds.nextInt(1 + 40);
					if (randExtraWheatAmt == randExtraWheatNum) {
						event.getPlayer().getWorld().dropItem(new Location(event.getBlock().getWorld(), event.getBlock().getX(), event.getBlock().getY()+0.5, event.getBlock().getZ()), stackWheat);
						event.getPlayer().getWorld().dropItem(new Location(event.getBlock().getWorld(), event.getBlock().getX(), event.getBlock().getY()+0.5, event.getBlock().getZ()), stackWheat);
						CivMessage.send(event.getPlayer(), CivColor.LightGray+"Double Wheat!");
					} else {
						event.getPlayer().getWorld().dropItem(new Location(event.getBlock().getWorld(), event.getBlock().getX(), event.getBlock().getY()+0.5, event.getBlock().getZ()), stackWheat);
					}
				}
				
				/* Try to get any extra bonus enhancement from this item. */
/*				AttributeUtil attrs = new AttributeUtil(event.getPlayer().getInventory().getItemInMainHand());
				for (LoreEnhancement enh : attrs.getEnhancements()) {
					if (enh instanceof LoreEnhancementCivBonus) {
						ItemStack stackCust1 = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:test_item"), 1);
						event.getPlayer().getWorld().dropItem(new Location(event.getBlock().getWorld(), event.getBlock().getX(), event.getBlock().getY()+0.5, event.getBlock().getZ()), stackCust1);
						CivMessage.send(event.getPlayer(), CivColor.LightGray+"Civ Bonus Success!");
					}
				}*/
			} catch (InvalidConfiguration e) {
				e.printStackTrace();
				return;
			}
		}
	}
	
	//XXX Fully Grown Carrot Crop
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerCarrotCropBreak(BlockBreakEvent event) {
		if (event.getBlock().getType().equals(Material.CARROT)) {
			Crops crops = (Crops) event.getBlock().getState().getData();
			if (crops.getState() != CropState.RIPE) {
				return;
			}
			
			event.setCancelled(true);
			ItemManager.setTypeIdAndData(event.getBlock(), CivData.AIR, (byte)0, true);
			try {
				Random randCarrots = new Random();
				int minCarrots = CivSettings.getInteger(CivSettings.dropsConfig, "carrotcrop.carrots.min");
				int maxCarrots;
				if (event.getPlayer().getInventory().getItemInMainHand().containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
					maxCarrots = CivSettings.getInteger(CivSettings.dropsConfig, "carrotcrop.carrots.max_fortune");
				} else {
					maxCarrots = CivSettings.getInteger(CivSettings.dropsConfig, "carrotcrop.carrots.max");
				}
				int randCarrotAmt = randCarrots.nextInt(minCarrots + maxCarrots);
				randCarrotAmt -= minCarrots;
				if (randCarrotAmt <= 0) {
					randCarrotAmt = 1;
				}
				
				for (int i = 0; i < randCarrotAmt; i++) {
					int randExtraCarrotsAmt = randCarrots.nextInt(1 + 40);
					int randExtraCarrotsNum = randCarrots.nextInt(1 + 40);
					ItemStack stackCarrots = new ItemStack(Material.CARROT_ITEM);
					if (randExtraCarrotsAmt == randExtraCarrotsNum) {
						event.getPlayer().getWorld().dropItem(new Location(event.getBlock().getWorld(), event.getBlock().getX(), event.getBlock().getY()+0.5, event.getBlock().getZ()), stackCarrots);
						event.getPlayer().getWorld().dropItem(new Location(event.getBlock().getWorld(), event.getBlock().getX(), event.getBlock().getY()+0.5, event.getBlock().getZ()), stackCarrots);
						CivMessage.send(event.getPlayer(), CivColor.LightGray+"Double Carrots!");
					} else {
						event.getPlayer().getWorld().dropItem(new Location(event.getBlock().getWorld(), event.getBlock().getX(), event.getBlock().getY()+0.5, event.getBlock().getZ()), stackCarrots);
					}
				}
			} catch (InvalidConfiguration e) {
				e.printStackTrace();
				return;
			}
		}
	}
	
	//XXX Fully Grown Carrot Crop
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerPotatoCropBreak(BlockBreakEvent event) {
		if (event.getBlock().getType().equals(Material.POTATO)) {
			Crops crops = (Crops) event.getBlock().getState().getData();
			if (crops.getState() != CropState.RIPE) {
				return;
			}
			
			event.setCancelled(true);
			ItemManager.setTypeIdAndData(event.getBlock(), CivData.AIR, (byte)0, true);
			try {
				Random randPotatoes = new Random();
				int minPotatoes = CivSettings.getInteger(CivSettings.dropsConfig, "potatocrop.potatoes.min");
				int maxPotatoes;
				if (event.getPlayer().getInventory().getItemInMainHand().containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
					maxPotatoes = CivSettings.getInteger(CivSettings.dropsConfig, "potatocrop.potatoes.max_fortune");
				} else {
					maxPotatoes = CivSettings.getInteger(CivSettings.dropsConfig, "potatocrop.potatoes.max");
				}
				int randPotatoAmt = randPotatoes.nextInt(minPotatoes + maxPotatoes);
				randPotatoAmt -= minPotatoes;
				if (randPotatoAmt <= 0) {
					randPotatoAmt = 1;
				}
				
				for (int i = 0; i < randPotatoAmt; i++) {
					int randExtraPotatoesAmt = randPotatoes.nextInt(1 + 40);
					int randExtraPotatoesNum = randPotatoes.nextInt(1 + 40);
					ItemStack stackPotatoes = new ItemStack(Material.POTATO_ITEM);
					if (randExtraPotatoesAmt == randExtraPotatoesNum) {
						event.getPlayer().getWorld().dropItem(new Location(event.getBlock().getWorld(), event.getBlock().getX(), event.getBlock().getY()+0.5, event.getBlock().getZ()), stackPotatoes);
						event.getPlayer().getWorld().dropItem(new Location(event.getBlock().getWorld(), event.getBlock().getX(), event.getBlock().getY()+0.5, event.getBlock().getZ()), stackPotatoes);
						CivMessage.send(event.getPlayer(), CivColor.LightGray+"Double Potatoes!");
					} else {
						event.getPlayer().getWorld().dropItem(new Location(event.getBlock().getWorld(), event.getBlock().getX(), event.getBlock().getY()+0.5, event.getBlock().getZ()), stackPotatoes);
					}
				}
			} catch (InvalidConfiguration e) {
				e.printStackTrace();
				return;
			}
		}
	}
	
	//XXX Fully Grown Beetroot Crop
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerBeetrootCropBreak(BlockBreakEvent event) {
		if (event.getBlock().getType().equals(Material.BEETROOT_BLOCK)) {
			Crops crops = (Crops) event.getBlock().getState().getData();
			if (crops.getState() != CropState.RIPE) {
				return;
			}
			
			event.setCancelled(true);
			ItemManager.setTypeIdAndData(event.getBlock(), CivData.AIR, (byte)0, true);
			try {
				Random randSeeds = new Random();
				int minSeeds = CivSettings.getInteger(CivSettings.dropsConfig, "beetrootcrop.seeds.min");
				int maxSeeds;
				if (event.getPlayer().getInventory().getItemInMainHand().containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
					maxSeeds = CivSettings.getInteger(CivSettings.dropsConfig, "beetrootcrop.seeds.max_fortune");
				} else {
					maxSeeds = CivSettings.getInteger(CivSettings.dropsConfig, "beetrootcrop.seeds.max");
				}
				int randSeedAmt = randSeeds.nextInt(minSeeds + maxSeeds);
				randSeedAmt -= minSeeds;
				if (randSeedAmt <= 0) {
					randSeedAmt = 1;
				}
				
				for (int i = 0; i < randSeedAmt; i++) {
					int randExtraSeedsAmt = randSeeds.nextInt(1 + 40);
					int randExtraSeedsNum = randSeeds.nextInt(1 + 40);
					ItemStack stackSeeds = new ItemStack(Material.BEETROOT_SEEDS);
					if (randExtraSeedsAmt == randExtraSeedsNum) {
						event.getPlayer().getWorld().dropItem(new Location(event.getBlock().getWorld(), event.getBlock().getX(), event.getBlock().getY()+0.5, event.getBlock().getZ()), stackSeeds);
						event.getPlayer().getWorld().dropItem(new Location(event.getBlock().getWorld(), event.getBlock().getX(), event.getBlock().getY()+0.5, event.getBlock().getZ()), stackSeeds);
						CivMessage.send(event.getPlayer(), CivColor.LightGray+"Double Beetroot Seeds!");
					} else {
						event.getPlayer().getWorld().dropItem(new Location(event.getBlock().getWorld(), event.getBlock().getX(), event.getBlock().getY()+0.5, event.getBlock().getZ()), stackSeeds);
					}
				}
				
				Random randBeetroot = new Random();
				int minBeetroot = CivSettings.getInteger(CivSettings.dropsConfig, "beetrootcrop.beetroots.min");
				int maxBeetroot;
				if (event.getPlayer().getInventory().getItemInMainHand().containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
					maxBeetroot = CivSettings.getInteger(CivSettings.dropsConfig, "beetrootcrop.beetroots.max_fortune");
				} else {
					maxBeetroot = CivSettings.getInteger(CivSettings.dropsConfig, "beetrootcrop.beetroots.max");
				}
				int randBeetrootAmt = randBeetroot.nextInt(minBeetroot + maxBeetroot);
				randBeetrootAmt -= minBeetroot;
				if (randBeetrootAmt <= 0) {
					randBeetrootAmt = 1;
				}
				
				for (int i = 0; i < randBeetrootAmt; i++) {
					ItemStack stackBeetroots = new ItemStack(Material.BEETROOT);
					int randExtraBeetrootAmt = randSeeds.nextInt(1 + 40);
					int randExtraBeetrootNum = randSeeds.nextInt(1 + 40);
					if (randExtraBeetrootAmt == randExtraBeetrootNum) {
						event.getPlayer().getWorld().dropItem(new Location(event.getBlock().getWorld(), event.getBlock().getX(), event.getBlock().getY()+0.5, event.getBlock().getZ()), stackBeetroots);
						event.getPlayer().getWorld().dropItem(new Location(event.getBlock().getWorld(), event.getBlock().getX(), event.getBlock().getY()+0.5, event.getBlock().getZ()), stackBeetroots);
						CivMessage.send(event.getPlayer(), CivColor.LightGray+"Double Beetroots!");
					} else {
						event.getPlayer().getWorld().dropItem(new Location(event.getBlock().getWorld(), event.getBlock().getX(), event.getBlock().getY()+0.5, event.getBlock().getZ()), stackBeetroots);
					}
				}
			} catch (InvalidConfiguration e) {
				e.printStackTrace();
				return;
			}
		}
	}
	
	
	
	
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onWaterBreaksCropEvent(BlockFromToEvent event) {
		//XXX Fully Grown Wheat Crop
		if (event.getBlock().getType().equals(Material.WATER) || event.getBlock().getType().equals(Material.STATIONARY_WATER)
					&& event.getToBlock().getType().equals(Material.CROPS)) {
			if (event.getToBlock().getType().equals(Material.CROPS)) {
				Crops crops = (Crops) event.getToBlock().getState().getData();
				if (crops.getState() != CropState.RIPE) {
					return;
				}
				
				ItemManager.setTypeIdAndData(event.getToBlock(), CivData.AIR, (byte)0, true);
				try { //Drop seeds
					Random rand = new Random();
					int min = CivSettings.getInteger(CivSettings.dropsConfig, "wheatcrop.water_broken.minseeds");
					int max = CivSettings.getInteger(CivSettings.dropsConfig, "wheatcrop.water_broken.maxseeds");
					int randAmount = rand.nextInt(max - min) + 1;
					randAmount -= min;
					if (randAmount <= 0) {
						randAmount = 1;
					}
					
					for (int i = 0; i < randAmount; i++) {
						ItemStack stack = new ItemStack(Material.SEEDS);
						event.getToBlock().getWorld().dropItem(event.getBlock().getLocation(), stack);
					}
				} catch (InvalidConfiguration e) {
					e.printStackTrace();
					return;
				} try { //Drop Wheat
					Random rand = new Random();
					int min = CivSettings.getInteger(CivSettings.dropsConfig, "wheatcrop.water_broken.minwheat");
					int max = CivSettings.getInteger(CivSettings.dropsConfig, "wheatcrop.water_broken.maxwheat");
					int randAmount = rand.nextInt(max - min) + 1;
					randAmount -= min;
					if (randAmount <= 0) {
						randAmount = 1;
					}
					
					for (int i = 0; i < randAmount; i++) {
						ItemStack stack = new ItemStack(Material.WHEAT);
						event.getToBlock().getWorld().dropItem(event.getBlock().getLocation(), stack);
					}
				} catch (InvalidConfiguration e) {
					e.printStackTrace();
					return;
				}
			}
		}
		
		//XXX Fully Grown Carrot Crop
		if (event.getBlock().getType().equals(Material.WATER) || event.getBlock().getType().equals(Material.STATIONARY_WATER)
					&& event.getToBlock().getType().equals(Material.CARROT)) {
			if (event.getToBlock().getType().equals(Material.CARROT)) {
				Crops crops = (Crops) event.getToBlock().getState().getData();
				if (crops.getState() != CropState.RIPE) {
					return;
				}
				
				ItemManager.setTypeIdAndData(event.getToBlock(), CivData.AIR, (byte)0, true);
				try { //Drop seeds
					Random rand = new Random();
					int min = CivSettings.getInteger(CivSettings.dropsConfig, "carrotcrop.water_broken.mincarrot");
					int max = CivSettings.getInteger(CivSettings.dropsConfig, "carrotcrop.water_broken.maxcarrot");
					int randAmount = rand.nextInt(max - min) + 1;
					randAmount -= min;
					if (randAmount <= 0) {
						randAmount = 1;
					}
					
					for (int i = 0; i < randAmount; i++) {
						ItemStack stack = new ItemStack(Material.CARROT_ITEM);
						event.getToBlock().getWorld().dropItem(event.getBlock().getLocation(), stack);
					}
				} catch (InvalidConfiguration e) {
					e.printStackTrace();
					return;
				}
			}
		}
		
		//XXX Fully Grown Potato Crop
		if (event.getBlock().getType().equals(Material.WATER) || event.getBlock().getType().equals(Material.STATIONARY_WATER)
					&& event.getToBlock().getType().equals(Material.POTATO)) {
			if (event.getToBlock().getType().equals(Material.POTATO)) {
				Crops crops = (Crops) event.getToBlock().getState().getData();
				if (crops.getState() != CropState.RIPE) {
					return;
				}
				
				ItemManager.setTypeIdAndData(event.getToBlock(), CivData.AIR, (byte)0, true);
				try { //Drop seeds
					Random rand = new Random();
					int min = CivSettings.getInteger(CivSettings.dropsConfig, "potatocrop.water_broken.minpotato");
					int max = CivSettings.getInteger(CivSettings.dropsConfig, "potatocrop.water_broken.maxpotato");
					int randAmount = rand.nextInt(max - min) + 1;
					randAmount -= min;
					if (randAmount <= 0) {
						randAmount = 1;
					}
					
					for (int i = 0; i < randAmount; i++) {
						ItemStack stack = new ItemStack(Material.POTATO_ITEM);
						event.getToBlock().getWorld().dropItem(event.getBlock().getLocation(), stack);
					}
				} catch (InvalidConfiguration e) {
					e.printStackTrace();
					return;
				}
			}
		}
		
		//XXX Fully Grown Beetroot Crop
		if (event.getBlock().getType().equals(Material.WATER) || event.getBlock().getType().equals(Material.STATIONARY_WATER)
					&& event.getToBlock().getType().equals(Material.BEETROOT_BLOCK)) {
			if (event.getToBlock().getType().equals(Material.BEETROOT_BLOCK)) {
				Crops crops = (Crops) event.getToBlock().getState().getData();
				if (crops.getState() != CropState.RIPE) {
					return;
				}
				
				ItemManager.setTypeIdAndData(event.getToBlock(), CivData.AIR, (byte)0, true);
				try { //Drop seeds
					Random rand = new Random();
					int min = CivSettings.getInteger(CivSettings.dropsConfig, "beetrootcrop.water_broken.minbeetroot");
					int max = CivSettings.getInteger(CivSettings.dropsConfig, "beetrootcrop.water_broken.maxbeetroot");
					int randAmount = rand.nextInt(max - min) + 1;
					randAmount -= min;
					if (randAmount <= 0) {
						randAmount = 1;
					}
					
					for (int i = 0; i < randAmount; i++) {
						ItemStack stack = new ItemStack(Material.BEETROOT);
						event.getToBlock().getWorld().dropItem(event.getBlock().getLocation(), stack);
					}
				} catch (InvalidConfiguration e) {
					e.printStackTrace();
					return;
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onOreBlockBreakEvent(BlockBreakEvent event) {
		//XXX Lapis Ore
		if (event.getBlock().getType().equals(Material.LAPIS_ORE)) {
			try {
				boolean canSilk = CivSettings.getBoolean(CivSettings.dropsConfig, "lapisore.canSilk");
				if (event.getPlayer().getInventory().getItemInMainHand().containsEnchantment(Enchantment.SILK_TOUCH) && canSilk == true) {
					return;
				}
				
				event.getBlock().breakNaturally();
				ItemManager.setTypeIdAndData(event.getBlock(), CivData.AIR, (byte)0, true);
				Random rand = new Random();
				//Drop EXP
				int minXP = CivSettings.getInteger(CivSettings.dropsConfig, "lapisore.exp.min");
				int maxXP = CivSettings.getInteger(CivSettings.dropsConfig, "lapisore.exp.max");
				
				int randAmtXP = rand.nextInt(maxXP - minXP) + 1;
				randAmtXP -= minXP;
				if (randAmtXP <= 0) {
					randAmtXP = 1;
				}
				//Drop Tungsten
				int minOre = CivSettings.getInteger(CivSettings.dropsConfig, "lapisore.item.min");
				int maxOre;
				if (event.getPlayer().getInventory().getItemInMainHand().containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
					maxOre = CivSettings.getInteger(CivSettings.dropsConfig, "lapisore.item.max_fortune");
				} else {
					maxOre = CivSettings.getInteger(CivSettings.dropsConfig, "lapisore.item.max");
				}
				
				int randAmt = rand.nextInt(maxOre - minOre) + 1;
				randAmt -= minOre;
				if (randAmt <= 0) {
					randAmt = 1;
				}
				//Drop Lapis
				int minLap = CivSettings.getInteger(CivSettings.dropsConfig, "lapisore.item.min");
				int maxLap;
				if (event.getPlayer().getInventory().getItemInMainHand().containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
					maxLap = CivSettings.getInteger(CivSettings.dropsConfig, "lapisore.item.max_fortune");
				} else {
					maxLap = CivSettings.getInteger(CivSettings.dropsConfig, "lapisore.item.max");
				}
				
				int randAmtLap = rand.nextInt(maxLap - minLap) + 1;
				randAmtLap -= minLap;
				if (randAmtLap <= 0) {
					randAmtLap = 1;
				}
				
				for (int i = 0; i < randAmt; i++) {
					ItemStack stack1 = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:tungsten_ore"));
					event.getPlayer().getWorld().dropItem(event.getBlock().getLocation(), stack1);
				}
				for (int i = 0; i < randAmtLap; i++) {
					ItemStack stack2 = new ItemStack(Material.INK_SACK, randAmtLap, (short)4);
					event.getPlayer().getWorld().dropItem(event.getBlock().getLocation(), stack2);
				}
				ExperienceOrb exp = event.getPlayer().getWorld().spawn(event.getBlock().getLocation(), ExperienceOrb.class);
				exp.setExperience(randAmtXP);
			} catch (InvalidConfiguration e) {
				e.printStackTrace();
				return;
			}
		}
		
		//XXX Redstone Ore
		if (event.getBlock().getType().equals(Material.REDSTONE_ORE) || event.getBlock().getType().equals(Material.GLOWING_REDSTONE_ORE)) {
			try {
				boolean canSilk = CivSettings.getBoolean(CivSettings.dropsConfig, "redstoneore.canSilk");
				if (event.getPlayer().getInventory().getItemInMainHand().containsEnchantment(Enchantment.SILK_TOUCH) && canSilk == true) {
					return;
				}
				
				event.setCancelled(true);
				ItemManager.setTypeIdAndData(event.getBlock(), CivData.AIR, (byte)0, true);
				Random rand = new Random();
				//Drop EXP
				int minXP = CivSettings.getInteger(CivSettings.dropsConfig, "redstoneore.exp.min");
				int maxXP = CivSettings.getInteger(CivSettings.dropsConfig, "redstoneore.exp.max");
				
				int randAmtXP = rand.nextInt(maxXP - minXP) + 1;
				randAmtXP -= minXP;
				if (randAmtXP <= 0) {
					randAmtXP = 1;
				}
				//Drop Chromium
				int minOre = CivSettings.getInteger(CivSettings.dropsConfig, "redstoneore.dust.min");
				int maxOre;
				if (event.getPlayer().getInventory().getItemInMainHand().containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
					maxOre = CivSettings.getInteger(CivSettings.dropsConfig, "redstoneore.dust.max_fortune");
				} else {
					maxOre = CivSettings.getInteger(CivSettings.dropsConfig, "redstoneore.dust.max");
				}
				
				int randAmt = rand.nextInt(maxOre - minOre) + 1;
				randAmt -= minOre;
				if (randAmt <= 0) {
					randAmt = 1;
				}
				//Drop Redstone
				int minRed = CivSettings.getInteger(CivSettings.dropsConfig, "redstoneore.item.min");
				int maxRed;
				if (event.getPlayer().getInventory().getItemInMainHand().containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
					maxRed = CivSettings.getInteger(CivSettings.dropsConfig, "redstoneore.item.max_fortune");
				} else {
					maxRed = CivSettings.getInteger(CivSettings.dropsConfig, "redstoneore.item.max");
				}
				
				int randAmtRed = rand.nextInt(maxRed - minRed) + 1;
				randAmtRed -= minRed;
				if (randAmtRed <= 0) {
					randAmtRed = 1;
				}
				
				for (int i = 0; i < randAmt; i++) {
					ItemStack stack1 = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:chromium_ore"));
					event.getPlayer().getWorld().dropItem(event.getBlock().getLocation(), stack1);
				}
				for (int i = 0; i < randAmtRed; i++) {
					ItemStack stack2 = new ItemStack(Material.REDSTONE);
					event.getPlayer().getWorld().dropItem(event.getBlock().getLocation(), stack2);
				}
				ExperienceOrb exp = event.getPlayer().getWorld().spawn(event.getBlock().getLocation(), ExperienceOrb.class);
				exp.setExperience(randAmtXP);
			} catch (InvalidConfiguration e) {
				e.printStackTrace();
				return;
			}
		}
		
		//XXX Gold Ore
		if (event.getBlock().getType().equals(Material.GOLD_ORE)) {
			try {
				boolean canSilk = CivSettings.getBoolean(CivSettings.dropsConfig, "goldore.canSilk");
				if (event.getPlayer().getInventory().getItemInMainHand().containsEnchantment(Enchantment.SILK_TOUCH) && canSilk == true) {
					return;
				}
				
				event.setCancelled(true);
				ItemManager.setTypeIdAndData(event.getBlock(), CivData.AIR, (byte)0, true);
				Random rand = new Random();
				//Drop EXP
				int minXP = CivSettings.getInteger(CivSettings.dropsConfig, "goldore.exp.min");
				int maxXP = CivSettings.getInteger(CivSettings.dropsConfig, "goldore.exp.max");
				
				int randAmtXP = rand.nextInt(maxXP - minXP) + 1;
				randAmtXP -= minXP;
				if (randAmtXP <= 0) {
					randAmtXP = 1;
				}
				//Drop Chromium
				int minOre = CivSettings.getInteger(CivSettings.dropsConfig, "goldore.item.min");
				int maxOre;
				if (event.getPlayer().getInventory().getItemInMainHand().containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
					maxOre = CivSettings.getInteger(CivSettings.dropsConfig, "goldore.item.max_fortune");
				} else {
					maxOre = CivSettings.getInteger(CivSettings.dropsConfig, "goldore.item.max");
				}
				
				int randAmt = rand.nextInt(maxOre - minOre) + 1;
				randAmt -= minOre;
				if (randAmt <= 0) {
					randAmt = 1;
				}
				
				for (int i = 0; i < randAmt; i++) {
					ItemStack stack1 = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:titanium_ore"));
					event.getPlayer().getWorld().dropItem(event.getBlock().getLocation(), stack1);
				}
				ExperienceOrb exp = event.getPlayer().getWorld().spawn(event.getBlock().getLocation(), ExperienceOrb.class);
				exp.setExperience(randAmtXP);
			} catch (InvalidConfiguration e) {
				e.printStackTrace();
				return;
			}
		}
		
		//XXX Iron Ore
		if (event.getBlock().getType().equals(Material.IRON_ORE)) {
			try {
				boolean canSilk = CivSettings.getBoolean(CivSettings.dropsConfig, "ironore.canSilk");
				if (event.getPlayer().getInventory().getItemInMainHand().containsEnchantment(Enchantment.SILK_TOUCH) && canSilk == true) {
					return;
				}
				
				event.setCancelled(true);
				ItemManager.setTypeIdAndData(event.getBlock(), CivData.AIR, (byte)0, true);
				Random rand = new Random();
				//Drop EXP
				int minXP = CivSettings.getInteger(CivSettings.dropsConfig, "ironore.exp.min");
				int maxXP = CivSettings.getInteger(CivSettings.dropsConfig, "ironore.exp.max");
				
				int randAmtXP = rand.nextInt(maxXP - minXP) + 1;
				randAmtXP -= minXP;
				if (randAmtXP <= 0) {
					randAmtXP = 1;
				}
				//Drop Chromium
				int minOre = CivSettings.getInteger(CivSettings.dropsConfig, "ironore.item.min");
				int maxOre;
				if (event.getPlayer().getInventory().getItemInMainHand().containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
					maxOre = CivSettings.getInteger(CivSettings.dropsConfig, "ironore.item.max_fortune");
				} else {
					maxOre = CivSettings.getInteger(CivSettings.dropsConfig, "ironore.item.max");
				}
				
				int randAmt = rand.nextInt(maxOre - minOre) + 1;
				randAmt -= minOre;
				if (randAmt <= 0) {
					randAmt = 1;
				}
				
				for (int i = 0; i < randAmt; i++) {
					ItemStack stack1 = LoreMaterial.spawn(LoreMaterial.materialMap.get("civ:iron_ore"));
					event.getPlayer().getWorld().dropItem(event.getBlock().getLocation(), stack1);
				}
				ExperienceOrb exp = event.getPlayer().getWorld().spawn(event.getBlock().getLocation(), ExperienceOrb.class);
				exp.setExperience(randAmtXP);
			} catch (InvalidConfiguration e) {
				e.printStackTrace();
				return;
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST) 
	public void onBlockPlace(BlockPlaceEvent event) {
		ItemStack stack = event.getPlayer().getInventory().getItemInMainHand();
		if (stack == null || stack.getType().equals(Material.AIR)) {
			return;
		}
		
		LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(stack);
		if (craftMat == null) {
			return;
		}
		
		craftMat.onBlockPlaced(event);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteract(PlayerInteractEvent event) {
	
		ItemStack stack = event.getPlayer().getInventory().getItemInMainHand();
		if (stack == null) {
			return;
		}
		
		LoreMaterial material = LoreMaterial.getMaterial(stack);
		if (material != null) {
			material.onInteract(event);
		}
	}
	
	@EventHandler(priority = EventPriority.LOW) 
	public void onInteractEntity(PlayerInteractEntityEvent event) {
		if (event.isCancelled()) {
			return;
		}
		
		ItemStack stack = event.getPlayer().getInventory().getItemInMainHand();
		if (stack == null) {
			return;
		}

		LoreMaterial material = LoreMaterial.getMaterial(stack);
		if (material != null) {
			material.onInteractEntity(event);
		}
		
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onItemHeld(PlayerItemHeldEvent event) {
		if (event.isCancelled()) {
			return;
		}
		
		ItemStack stack = event.getPlayer().getInventory().getItemInMainHand();
		if (stack == null) {
			return;
		}

		LoreMaterial material = LoreMaterial.getMaterial(stack);
		if (material != null) {
			material.onHold(event);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void OnPlayerDropItem(PlayerDropItemEvent event) {
		if (event.isCancelled()) {
			return;
		}
		
		ItemStack stack = event.getItemDrop().getItemStack();
		if (LoreMaterial.isCustom(stack)) {
			LoreMaterial.getMaterial(stack).onItemDrop(event);
			return;
		}
		
		String custom = isCustomDrop(stack);
		if (custom != null) {
			event.setCancelled(true);
		}
	}	
	
	private static String isCustomDrop(ItemStack stack) {
		if (stack == null || ItemManager.getId(stack) != 166) {
			return null;
		}
		
		if(LoreGuiItem.isGUIItem(stack)) {
			return null;
		}
		return stack.getItemMeta().getDisplayName();
	}
	
	/* Prevent the player from using goodies in crafting recipies. */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void OnCraftItemEvent(CraftItemEvent event) {	
		for (ItemStack stack : event.getInventory().getMatrix()) {
			if (stack != null) {
				if (LoreMaterial.isCustom(stack)) {
					LoreMaterial.getMaterial(stack).onItemCraft(event);
//					String id = LoreMaterial.getMaterial(stack).getId();
//					CivLog.craft((Player) event.getWhoClicked(), id, stack.getAmount());
//				} else {
//					CivLog.craft((Player) event.getWhoClicked(), stack.getType(), stack.getAmount());
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void OnPlayerItemPickup(PlayerPickupItemEvent event) {
		ItemStack stack = event.getItem().getItemStack();
		if (LoreMaterial.isCustom(stack)) {
			LoreMaterial.getMaterial(stack).onItemPickup(event);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void OnItemSpawn(ItemSpawnEvent event) {
		ItemStack stack = event.getEntity().getItemStack();
		if (LoreMaterial.isCustom(stack)) {
			
			
/*			if (stack.getType() == Material.GOLD_SWORD ||
					stack.getType() == Material.WOOD_SWORD ||
					stack.getType() == Material.IRON_SWORD ||
					stack.getType() == Material.STONE_SWORD ||
					stack.getType() == Material.DIAMOND_SWORD) {
				
				net.minecraft.server.v1_10_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);
				NBTTagCompound compound = (nmsStack.hasTag()) ? nmsStack.getTag() : new NBTTagCompound();
				NBTTagList modifiers = new NBTTagList();
				NBTTagCompound damage = new NBTTagCompound();
				
				damage.set("AttributeName", new NBTTagString("generic.attackDamage"));
				damage.set("Name", new NBTTagString("generic.attackDamage"));
				damage.set("Operation", new NBTTagInt(0));
				damage.set("UUIDLeast", new NBTTagInt(894654));
				damage.set("UUIDMost", new NBTTagInt(2872));
				damage.set("Slot", new NBTTagString("mainhand"));
				
				modifiers.add(damage);
				compound.set("AttributeModifiers", modifiers);
				nmsStack.setTag(compound);
				stack = CraftItemStack.asBukkitCopy(nmsStack);
				LoreMaterial.getMaterial(stack).onItemSpawn(event);
			}*/
			
			
			LoreMaterial.getMaterial(stack).onItemSpawn(event);
			return;
		}
		
		String custom = isCustomDrop(stack);
		if (custom != null) {
			ItemStack newStack = LoreMaterial.spawn(LoreMaterial.materialMap.get(custom), stack.getAmount());
			event.getEntity().getWorld().dropItemNaturally(event.getLocation(), newStack);
			event.setCancelled(true);
			return;
		}
		
		if (isUnwantedVanillaItem(stack)) {
			if (!stack.getType().equals(Material.HOPPER) && 
					!stack.getType().equals(Material.HOPPER_MINECART)) {		
				event.setCancelled(true);
				event.getEntity().remove();
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerDefenseAndAttack(EntityDamageByEntityEvent event) {
		if (event.isCancelled()) {
			return;
		}
		
		Player defendingPlayer = null;
		if (event.getEntity() instanceof Player) {
			defendingPlayer = (Player)event.getEntity();
		}
		
		if (event.getDamager() instanceof Arrow) {
			LivingEntity shooter = (LivingEntity) ((Arrow)event.getDamager()).getShooter();
			
			if (shooter instanceof Player) {
				ItemStack inHand = ((Player)shooter).getInventory().getItemInMainHand();
				if (LoreMaterial.isCustom(inHand)) {
					LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(inHand);
					craftMat.onRangedAttack(event, inHand);
				}
			} else {
				ArrowFiredCache afc = CivCache.arrowsFired.get(event.getDamager().getUniqueId());
				if (afc != null) {
					/* Arrow was fired by a tower. */
					afc.setHit(true);
					afc.destroy(event.getDamager());
					
					Resident defenderResident = CivGlobal.getResident(defendingPlayer);
					if (defenderResident != null && defenderResident.hasTown() && 
							defenderResident.getTown().getCiv() == afc.getFromTower().getTown().getCiv()) {
						/* Prevent friendly fire from arrow towers. */
						event.setCancelled(true);
						return;
					}
					
					/* Return after arrow tower does damage, do not apply armor defense. */
					event.setDamage((double)afc.getFromTower().getDamage());
					return;
				}
			}
		} else if (event.getDamager() instanceof Player) {
			ItemStack inHand = ((Player)event.getDamager()).getInventory().getItemInMainHand();
			LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(inHand);
			if (craftMat != null) {
				craftMat.onAttack(event, inHand);
			} else {
				/* Non-civcraft items only do 0.5 damage. */
				event.setDamage(1.0);
			}
		}
		
		if (defendingPlayer == null) {
			if (event.getEntity() instanceof LivingEntity) {
			}
			return;
		} else {
			/* Search equipt items for defense event. */
			for (ItemStack stack : defendingPlayer.getEquipment().getArmorContents()) {
				if (LoreMaterial.isCustom(stack)) {
					LoreMaterial.getMaterial(stack).onDefense(event, stack);
				}
			}
		}
	}
		
	@EventHandler(priority = EventPriority.NORMAL)
	public void OnInventoryClose(InventoryCloseEvent event) {
		for (ItemStack stack : event.getInventory().getContents()) {
			if (stack == null) {
				continue;
			}

			if (LoreMaterial.isCustom(stack)) {
				LoreMaterial.getMaterial(stack).onInventoryClose(event);
			}
		}
		
		for (ItemStack stack : event.getPlayer().getInventory()) {
			if (stack == null) {
				continue;
			}

			if (LoreMaterial.isCustom(stack)) {
				LoreMaterial.getMaterial(stack).onInventoryClose(event);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void OnInventoryOpen(InventoryOpenEvent event) {
		for (ItemStack stack : event.getInventory().getContents()) {
			if (stack == null) {
				continue;
			}

			if (LoreMaterial.isCustom(stack)) {
				LoreCraftableMaterial.getMaterial(stack).onInventoryOpen(event, stack);
			}
		}
		
		for (ItemStack stack : event.getPlayer().getInventory()) {
			if (stack == null) {
				continue;
			}

			if (LoreMaterial.isCustom(stack)) {
				LoreMaterial.getMaterial(stack).onInventoryOpen(event, stack);
			}
		}
		
		for (ItemStack stack : event.getPlayer().getInventory().getArmorContents()) {
			if (stack == null) {
				continue;
			}

			if (LoreMaterial.isCustom(stack)) {
				LoreMaterial.getMaterial(stack).onInventoryOpen(event, stack);
			}
		}
	}
	
	/* 
	 * Returns false if item is destroyed.
	 */
	private boolean processDurabilityChanges(PlayerDeathEvent event, ItemStack stack, int i) {
		LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(stack);
		if (craftMat != null) {
			ItemChangeResult result = craftMat.onDurabilityDeath(event, stack);
			if (result != null) {
				if (!result.destroyItem) {
					event.getEntity().getInventory().setItem(i, result.stack);
				} else {
					event.getEntity().getInventory().setItem(i, new ItemStack(Material.AIR));
					event.getDrops().remove(stack);
					return false;
				}
			}
		}
		
		return true;
	}
	
	@EventHandler(priority = EventPriority.LOW) 
	public void onPlayerDeathEvent(PlayerDeathEvent event) {
		HashMap<Integer, ItemStack> noDrop = new HashMap<Integer, ItemStack>();
		ItemStack[] armorNoDrop = new ItemStack[4];
		
		/* Search and execute any enhancements */
		for (int i = 0; i < event.getEntity().getInventory().getSize(); i++) {
			ItemStack stack = event.getEntity().getInventory().getItem(i);
			if (stack == null) {
				continue;
			}
						
			if(!processDurabilityChanges(event, stack, i)) {
				/* Don't process anymore more enhancements on items after its been destroyed. */
				continue;
			}
			
			if (!LoreMaterial.hasEnhancements(stack)) {
				continue;
			}
			
			AttributeUtil attrs = new AttributeUtil(stack);
			for (LoreEnhancement enhance : attrs.getEnhancements()) {
				if (enhance.onDeath(event, stack)) {
					/* Stack is not going to be dropped on death. */
					noDrop.put(i, stack);
				}
			}
		}
		
		/* Search for armor, apparently it doesnt show up in the normal inventory. */
		ItemStack[] contents = event.getEntity().getInventory().getArmorContents();
		for (int i = 0; i < contents.length; i++) {
			ItemStack stack = contents[i];
			if (stack == null) {
				continue;
			}

			if(!processDurabilityChanges(event, stack, i)) {
				/* Don't process anymore more enhancements on items after its been destroyed. */
				continue;
			}

			if (!LoreMaterial.hasEnhancements(stack)) {
				continue;
			}
			
			AttributeUtil attrs = new AttributeUtil(stack);
			for (LoreEnhancement enhance : attrs.getEnhancements()) {
				if (enhance.onDeath(event, stack)) {
					/* Stack is not going to be dropped on death. */
					armorNoDrop[i] = stack;
				}
			}
		}

		
		//event.getEntity().getInventory().getArmorContents()	
		class SyncRestoreItemsTask implements Runnable {
			HashMap<Integer, ItemStack> restore;
			String playerName;
			ItemStack[] armorContents;
			
			public SyncRestoreItemsTask(HashMap<Integer, ItemStack> restore, 
					ItemStack[] armorContents, String playerName) {
				this.restore = restore;
				this.playerName = playerName;
				this.armorContents = armorContents;
			}
			
			@Override
			public void run() {
				try {
					Player player = CivGlobal.getPlayer(playerName);					
					PlayerInventory inv = player.getInventory();
					for (Integer slot : restore.keySet()) {
						ItemStack stack = restore.get(slot);
						inv.setItem(slot, stack);
					}	
					
					inv.setArmorContents(this.armorContents);
				} catch (CivException e) {
					e.printStackTrace();
					return;
				}
			}
			
		}
		TaskMaster.syncTask(new SyncRestoreItemsTask(noDrop, armorNoDrop, event.getEntity().getName()));
		
		
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void OnEntityDeath(EntityDeathEvent event) {
		if (event.getEntity() instanceof Player) {
			return;
		}
				
		/* Remove any vanilla item IDs that can't be crafted from vanilla drops. */
		LinkedList<ItemStack> removed = new LinkedList<ItemStack>();
		for (ItemStack stack : event.getDrops()) {
			Integer key = ItemManager.getId(stack);
			
			if (CivSettings.removedRecipies.containsKey(key)) {
				if (!LoreMaterial.isCustom(stack)) {
					removed.add(stack);
				}
			}
		}
		
		event.getDrops().removeAll(removed);
	}
	
	//XXX Controls minecraft-to-custom item converting
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.NORMAL)
	public void onItemPickup(PlayerPickupItemEvent event) {
		if (ItemManager.getId(event.getItem().getItemStack()) == ItemManager.getId(Material.IRON_INGOT)) {
			LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(event.getItem().getItemStack());
			if (craftMat == null) {
				/* Found a vanilla iron ingot. */
				LoreCraftableMaterial iron = LoreCraftableMaterial.getCraftMaterialFromId("civ:iron_ingot");
				ItemStack newStack = LoreCraftableMaterial.spawn(iron, event.getItem().getItemStack().getAmount());
				event.getPlayer().getInventory().addItem(newStack);
				event.getPlayer().updateInventory();
				event.getItem().remove();
				event.setCancelled(true);
				CivMessage.send(event.getPlayer(), CivColor.LightGreen+"You've picked up "+CivColor.LightPurple+event.getItem().getItemStack().getAmount()+CivColor.White+" Iron Ingot");
			}
		}
		
		
		if (ItemManager.getId(event.getItem().getItemStack()) == ItemManager.getId(Material.SLIME_BALL)) {
			LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(event.getItem().getItemStack());
			if (craftMat == null) {
				/* Found a vanilla slime ball. */
				LoreCraftableMaterial slime = LoreCraftableMaterial.getCraftMaterialFromId("mat_vanilla_slime");
				ItemStack newStack = LoreCraftableMaterial.spawn(slime);
				newStack.setAmount(event.getItem().getItemStack().getAmount());
				event.getPlayer().getInventory().addItem(newStack);
				event.getPlayer().updateInventory();
				event.getItem().remove();
				event.setCancelled(true);
			}
		}
		
		if (ItemManager.getId(event.getItem().getItemStack()) == ItemManager.getId(Material.RAW_FISH)
				&& ItemManager.getData(event.getItem().getItemStack()) == 
					ItemManager.getData(ItemManager.getMaterialData(CivData.FISH_RAW, CivData.CLOWNFISH))) {
			LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(event.getItem().getItemStack());
			if (craftMat == null) {
				/* Found a vanilla slime ball. */
				LoreCraftableMaterial clown = LoreCraftableMaterial.getCraftMaterialFromId("mat_vanilla_clownfish");
				ItemStack newStack = LoreCraftableMaterial.spawn(clown);
				newStack.setAmount(event.getItem().getItemStack().getAmount());
				event.getPlayer().getInventory().addItem(newStack);
				event.getPlayer().updateInventory();
				event.getItem().remove();
				event.setCancelled(true);
			}
		}
		
		if (ItemManager.getId(event.getItem().getItemStack()) == ItemManager.getId(Material.RAW_FISH)
				&& ItemManager.getData(event.getItem().getItemStack()) == 
					ItemManager.getData(ItemManager.getMaterialData(CivData.FISH_RAW, CivData.PUFFERFISH))) {
			LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(event.getItem().getItemStack());
			if (craftMat == null) {
				/* Found a vanilla slime ball. */
				LoreCraftableMaterial clown = LoreCraftableMaterial.getCraftMaterialFromId("mat_vanilla_pufferfish");
				ItemStack newStack = LoreCraftableMaterial.spawn(clown);
				newStack.setAmount(event.getItem().getItemStack().getAmount());
				event.getPlayer().getInventory().addItem(newStack);
				event.getPlayer().updateInventory();
				event.getItem().remove();
				event.setCancelled(true);			
			}
		}
	}
	
	/* XXX Called when we click on an object, used for conversion to fix up reverse compat problems. */
	public void convertLegacyItem(InventoryClickEvent event) {
		boolean currentEmpty = (event.getCurrentItem() == null) || (ItemManager.getId(event.getCurrentItem()) == CivData.AIR);

		if (currentEmpty) {
			return;
		}
		
		if (ItemManager.getId(event.getCurrentItem()) == ItemManager.getId(Material.IRON_INGOT)) {
			LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(event.getCurrentItem());
			if (craftMat == null) {
				/* Found a vanilla iron ingot. */
				LoreCraftableMaterial iron = LoreCraftableMaterial.getCraftMaterialFromId("civ:iron_ingot");
				ItemStack newStack = LoreCraftableMaterial.spawn(iron, event.getCurrentItem().getAmount());
				event.setCurrentItem(newStack);
			}
		}
		
		if (ItemManager.getId(event.getCurrentItem()) == ItemManager.getId(Material.SLIME_BALL)) {
			LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(event.getCurrentItem());
			if (craftMat == null) {
				/* Found a vanilla slime ball. */
				LoreCraftableMaterial slime = LoreCraftableMaterial.getCraftMaterialFromId("mat_vanilla_slime");
				ItemStack newStack = LoreCraftableMaterial.spawn(slime);
				newStack.setAmount(event.getCurrentItem().getAmount());
				event.setCurrentItem(newStack);
			}
		}
		
		if (ItemManager.getId(event.getCurrentItem()) == ItemManager.getId(Material.RAW_FISH)
				&& ItemManager.getData(event.getCurrentItem()) == 
					ItemManager.getData(ItemManager.getMaterialData(CivData.FISH_RAW, CivData.CLOWNFISH))) {
			LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(event.getCurrentItem());
			if (craftMat == null) {
				/* Found a vanilla slime ball. */
				LoreCraftableMaterial clown = LoreCraftableMaterial.getCraftMaterialFromId("mat_vanilla_clownfish");
				ItemStack newStack = LoreCraftableMaterial.spawn(clown);
				newStack.setAmount(event.getCurrentItem().getAmount());
				event.setCurrentItem(newStack);
			}
		}
		
		if (ItemManager.getId(event.getCurrentItem()) == ItemManager.getId(Material.RAW_FISH)
				&& ItemManager.getData(event.getCurrentItem()) == 
					ItemManager.getData(ItemManager.getMaterialData(CivData.FISH_RAW, CivData.PUFFERFISH))) {
			LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(event.getCurrentItem());
			if (craftMat == null) {
				/* Found a vanilla slime ball. */
				LoreCraftableMaterial clown = LoreCraftableMaterial.getCraftMaterialFromId("mat_vanilla_pufferfish");
				ItemStack newStack = LoreCraftableMaterial.spawn(clown);
				newStack.setAmount(event.getCurrentItem().getAmount());
				event.setCurrentItem(newStack);
			}
		}
	}
	
	/*
	 * Track the location of the goodie.
	 */
	@EventHandler(priority = EventPriority.HIGHEST) 
	public void OnInventoryClick(InventoryClickEvent event) {
		boolean currentEmpty = (event.getCurrentItem() == null) || (ItemManager.getId(event.getCurrentItem()) == CivData.AIR);
		boolean cursorEmpty = (event.getCursor() == null) || (ItemManager.getId(event.getCursor()) == CivData.AIR);
		
		if (currentEmpty && cursorEmpty) {
			return;
		}
		
		convertLegacyItem(event);
		
		if (event.getRawSlot() < 0) {
			//I guess this means "drop the item?"
			
			//CivLog.debug("GET RAW WAS NEGATIVE, cancel this event it should be invalid.");
			//event.setResult(Result.DENY);
			//event.setCancelled(true);
			
			//try {
			//	Player player = CivGlobal.getPlayer(event.getWhoClicked().getName());
			//	player.updateInventory();
			//} catch (CivException e) {
			//}
			
			return;
		}
		
		InventoryView view = event.getView();
		Inventory clickedInv;
		Inventory otherInv;
		
		if (view.getType().equals(InventoryType.CRAFTING)) {
			//This is the player's own inventory. For some reason it requires
			//special treatment. The 'top' inventory is the 2x2 crafting
			//area plus the output. During shift click, items do not go there
			//so the otherInv should always be the player's inventory aka the bottom.
			if (event.getRawSlot() <= 4) {
				clickedInv = view.getTopInventory();
				otherInv = view.getBottomInventory();
			} else {
				clickedInv = view.getBottomInventory();
				otherInv = view.getBottomInventory();
			}
		} else {
			if (event.getRawSlot() == view.convertSlot(event.getRawSlot())) {
				//Clicked in the top holder
				clickedInv = view.getTopInventory();
				otherInv = view.getBottomInventory();
			} else {
				clickedInv = view.getBottomInventory();
				otherInv = view.getTopInventory();
			}
		}
		
		LoreMaterial current = LoreMaterial.getMaterial(event.getCurrentItem());
		LoreMaterial cursor = LoreMaterial.getMaterial(event.getCursor());
		
		if (event.isShiftClick()) {
			// Shift click is _always_ current item.
		//	CustomItemStack is = new CustomItemStack(event.getCurrentItem());
			if (current != null) {
			//if (is.isCustomItem() && (is.getMaterial() instanceof CustomMaterialExtended)) {
				// Calling onInvShiftClick Event.
				//((CustomMaterialExtended)is.getMaterial()).onInvShiftClick(event, clickedInv, otherInv, is.getItem());
				current.onInvShiftClick(event, clickedInv, otherInv, event.getCurrentItem());
			//}
			}
			
		} else {
			
			if (!currentEmpty && !cursorEmpty) {
				//CustomItemStack currentIs = new CustomItemStack(event.getCurrentItem());
				//CustomItemStack cursorIs = new CustomItemStack(event.getCursor());
				
				if (current != null) {
					current.onInvItemSwap(event, clickedInv, event.getCursor(), event.getCurrentItem());
				}
				
				if (cursor != null) {
					cursor.onInvItemSwap(event, clickedInv, event.getCursor(), event.getCurrentItem());
				}
			} else if (!currentEmpty) {
				// This is a pickup event.
				//CustomItemStack is = new CustomItemStack(event.getCurrentItem());
				if (current != null) {
					// Calling onInvItemPickup Event.
					current.onInvItemPickup(event, clickedInv, event.getCurrentItem());
				}
			} else {
				// Implied !cursorEmpty
				if (cursor != null) {
					// Calling onInvItemDrop Event.
					cursor.onInvItemDrop(event, clickedInv, event.getCursor());
				}
				
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void OnPlayerInteractEntityEvent (PlayerInteractEntityEvent event) {
			
		LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(event.getPlayer().getInventory().getItemInMainHand());
		if (craftMat == null) {
			return;
		}
		
		craftMat.onPlayerInteractEntityEvent(event);
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void OnPlayerLeashEvent(PlayerLeashEntityEvent event) {
		LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(event.getPlayer().getInventory().getItemInMainHand());
		if (craftMat == null) {
			return;
		}
		
		craftMat.onPlayerLeashEvent(event);
	}
	
	
	@EventHandler(priority = EventPriority.LOW)
	public void onItemDurabilityChange(PlayerItemDamageEvent event) {
		ItemStack stack = event.getItem();
		
		LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(stack);
		if (craftMat == null) {
			return;
		}
		craftMat.onItemDurabilityChange(event);
	}
	
	private static boolean isUnwantedVanillaItem(ItemStack stack) {
		if (stack == null) {
			return false;
		}
		
		LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(stack);
		if (craftMat != null) {
			/* Assume that if it's custom, it's good to go. */			
			return false;
		}
		
		if(LoreGuiItem.isGUIItem(stack)) {
			return false;
		}
		
		ConfigRemovedRecipes removed = CivSettings.removedRecipies.get(ItemManager.getId(stack));
		if (removed == null && !stack.getType().equals(Material.ENCHANTED_BOOK)) {
			/* Check for badly enchanted tools */
			if (stack.containsEnchantment(Enchantment.DAMAGE_ALL) ||
				stack.containsEnchantment(Enchantment.DAMAGE_ARTHROPODS) ||
				stack.containsEnchantment(Enchantment.KNOCKBACK) ||
				stack.containsEnchantment(Enchantment.DAMAGE_UNDEAD) ||
				stack.containsEnchantment(Enchantment.DURABILITY)) {					
			} else if (stack.containsEnchantment(Enchantment.FIRE_ASPECT) && 
					   stack.getEnchantmentLevel(Enchantment.FIRE_ASPECT) > 2) {
				// Remove any fire aspect above this amount
			} else if (stack.containsEnchantment(Enchantment.LOOT_BONUS_MOBS) &&
					   stack.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS) > 1) {
				// Only allow looting 1
			} else if (stack.containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS) &&
				   stack.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS) > 1) {
				// Only allow fortune 1
			} else if (stack.containsEnchantment(Enchantment.DIG_SPEED) &&
					   stack.getEnchantmentLevel(Enchantment.DIG_SPEED) > 5) {
				// only allow efficiency 5
			} else {
				/* Not in removed list, so allow it. */
				return false;				
			}
		}
		return true;
	}
	
	@SuppressWarnings("deprecation")
	public static void removeUnwantedVanillaItems(Player player, Inventory inv) {
		if (player.isOp()) {
			/* Allow OP to carry vanilla stuff. */
			return;
		}
		boolean sentMessage = false;
		
		for (ItemStack stack : inv.getContents()) {
			if (!isUnwantedVanillaItem(stack)) {
				continue;
			}
			
			inv.remove(stack);
			if (player != null) {
				CivLog.info("Removed vanilla item:"+stack+" from "+player.getName());
			}
			if (!sentMessage) {
				if (player != null) {
					CivMessage.send(player, CivColor.LightGray+"Restricted vanilla items in your inventory have been removed.");
				}
				sentMessage = true;
			}
		}
		
		/* Also check the player's equipt. */
		if (player != null) {
			ItemStack[] contents = player.getEquipment().getArmorContents();
			boolean foundBad = false;
			for (int i = 0; i < contents.length; i++) {
				ItemStack stack = contents[i];
				if (stack == null) {
					continue;
				}
				
				LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(stack);
				if (craftMat != null) {
					/* Assume we are good if we are custom. */
					continue;
				}
				
				ConfigRemovedRecipes removed = CivSettings.removedRecipies.get(stack.getTypeId());
				if (removed == null && !stack.getType().equals(Material.ENCHANTED_BOOK)) {
					/* Not in removed list, so allow it. */
					continue;
				}
				
				CivLog.info("Removed vanilla item:"+stack+" from "+player.getName()+" from armor.");
				contents[i] = new ItemStack(Material.AIR);
				foundBad = true;
				if (!sentMessage) {
					CivMessage.send(player, CivColor.LightGray+"Restricted vanilla items in your inventory have been removed.");
					sentMessage = true;
				}
			}		
			if (foundBad) {
				player.getEquipment().setArmorContents(contents);
			}
		}
		
		if (sentMessage) {
			if (player != null) {
				player.updateInventory();
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void OnInventoryHold(PlayerItemHeldEvent event) {
		
		ItemStack stack = event.getPlayer().getInventory().getItem(event.getNewSlot());
		if (stack == null) {
			return;
		}
		
		LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(stack);
		if (craftMat == null) {
			return;
		}
		
		craftMat.onHold(event);
	}
	
//	/* Prevent books from being inside an inventory. */
	/* Prevent vanilla gear from being used. */
/*	@EventHandler(priority = EventPriority.LOWEST)
	public void OnInventoryOpenRemove(InventoryOpenEvent event) {
		//CivLog.debug("open event.");
		if (event.getPlayer() instanceof Player) {
			
			//for (ItemStack stack : event.getInventory()) {
			for (int i = 0; i < event.getInventory().getSize(); i++) {
				ItemStack stack = event.getInventory().getItem(i);
				//CivLog.debug("stack cleanup");
				
				AttributeUtil attrs = ItemCleanup(stack);
				if (attrs != null) {
					event.getInventory().setItem(i, attrs.getStack());
				}
			}
		}
	}*/
	
/*	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerLogin(PlayerLoginEvent event) {
		
		class SyncTask implements Runnable {
			String playerName;
			
			public SyncTask(String name) {
				playerName = name;
			}

			@Override
			public void run() {
				try {
					Player player = CivGlobal.getPlayer(playerName);
										
					for (int i = 0; i < player.getInventory().getSize(); i++) {
						ItemStack stack = player.getInventory().getItem(i);

						AttributeUtil attrs = ItemCleanup(stack);
						if (attrs != null) {
							player.getInventory().setItem(i, attrs.getStack());
						}
					}
					
					ItemStack[] contents = new ItemStack[player.getInventory().getArmorContents().length];
					for (int i = 0; i < player.getInventory().getArmorContents().length; i++) {
						ItemStack stack = player.getInventory().getArmorContents()[i];
						
						AttributeUtil attrs = ItemCleanup(stack);
						if (attrs != null) {
							contents[i] = attrs.getStack();
						} else {
							contents[i] = stack;
						}
					}
					
					player.getInventory().setArmorContents(contents);
					
				} catch (CivException e) {
					return;
				}
				
			}
		}
		
		TaskMaster.syncTask(new SyncTask(event.getPlayer().getName()));
	
	}*/
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void OnInventoryClickEvent(InventoryClickEvent event) {
		//if (event.getWhoClicked() instanceof Player) {
		//	removeUnwantedVanillaItems((Player)event.getWhoClicked(), event.getView().getBottomInventory());			
		//}
	}
		
	public LoreCraftableMaterial getCompatibleCatalyst(LoreCraftableMaterial craftMat) {
		/* Setup list of catalysts to refund. */
		LinkedList<LoreMaterial> cataList = new LinkedList<LoreMaterial>();
		cataList.add(LoreMaterial.materialMap.get("mat_common_attack_catalyst"));
		cataList.add(LoreMaterial.materialMap.get("mat_common_defense_catalyst"));
		cataList.add(LoreMaterial.materialMap.get("mat_uncommon_attack_catalyst"));
		cataList.add(LoreMaterial.materialMap.get("mat_uncommon_defense_catalyst"));
		cataList.add(LoreMaterial.materialMap.get("mat_rare_attack_catalyst"));
		cataList.add(LoreMaterial.materialMap.get("mat_rare_defense_catalyst"));
		cataList.add(LoreMaterial.materialMap.get("mat_legendary_attack_catalyst"));
		cataList.add(LoreMaterial.materialMap.get("mat_legendary_defense_catalyst"));
		
		for (LoreMaterial mat : cataList) {
			LoreCraftableMaterial cMat = (LoreCraftableMaterial)mat;
			
			Catalyst cat = (Catalyst)cMat.getComponent("Catalyst");
			String allowedMats = cat.getString("allowed_materials");
			String[] matSplit = allowedMats.split(",");
			
			for (String mid : matSplit) {
				if (mid.trim().equalsIgnoreCase(craftMat.getId())) {
					return cMat;
				}
			}
			
		}
		return null;
	}
	
	
//	/*
//	 * Checks a players inventory and inventories that are opened for items.
//	 *   - Currently looks for old catalyst enhancements and marks them so
//	 *     they can be refunded.
//	 *
//	 */
//	public AttributeUtil ItemCleanup(ItemStack stack) {
//		
//		LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(stack);
//		if (craftMat == null) {
//			return null;
//		}
//			
//		AttributeUtil attrs = new AttributeUtil(stack);
//		if (!attrs.hasLegacyEnhancements()) {
//			return null;
//		}
//		
//		/* Found a legacy catalysts. Repair it. */
//		ItemStack cleanItem = LoreCraftableMaterial.spawn(craftMat);
//		AttributeUtil attrsClean = new AttributeUtil(cleanItem);
//		
//		double level = 0;
//		for (LoreEnhancement enh : LoreCraftableMaterial.getLegacyEnhancements(stack)) {
//			if (enh instanceof LoreEnhancementDefense) {
//				level = Double.valueOf(attrs.getLegacyEnhancementData("LoreEnhancementDefense"));
//				LoreCraftableMaterial compatCatalyst = getCompatibleCatalyst(craftMat);
//				attrs.setCivCraftProperty("freeCatalyst", ""+level+":"+compatCatalyst.getId());
//				attrs.removeLegacyEnhancement("LoreEnhancementDefense");
//			} else if (enh instanceof LoreEnhancementAttack) {
//				level = Double.valueOf(attrs.getLegacyEnhancementData("LoreEnhancementAttack"));
//				LoreCraftableMaterial compatCatalyst = getCompatibleCatalyst(craftMat);
//				attrs.setCivCraftProperty("freeCatalyst", ""+level+":"+compatCatalyst.getId());
//				attrs.removeLegacyEnhancement("LoreEnhancementAttack");
//			} 
//		}
//		
//		attrs.setLore(attrsClean.getLore());
//		attrs.setName(attrsClean.getName());
//		attrs.add(Attribute.newBuilder().name("Attack").
//				type(AttributeType.GENERIC_ATTACK_DAMAGE).
//				amount(0).
//				build());
//		
//		if (level != 0) {
//			attrs.addLore(CivColor.LightBlue+level+" free enhancements! Redeem at blacksmith.");
//			CivLog.cleanupLog("Converted stack:"+stack+" with enhancement level:"+level);
//		
//		}
//		
//		for (LoreEnhancement enh : LoreCraftableMaterial.getLegacyEnhancements(stack)) {
//			if (enh instanceof LoreEnhancementSoulBound) {	
//				LoreEnhancementSoulBound soulbound = (LoreEnhancementSoulBound)LoreEnhancement.enhancements.get("LoreEnhancementSoulBound");
//				soulbound.add(attrs);
//			}
//		}
//		
//		
//
//		return attrs;
//	}
	
}
