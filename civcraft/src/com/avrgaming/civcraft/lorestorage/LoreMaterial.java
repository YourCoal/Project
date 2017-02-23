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
package com.avrgaming.civcraft.lorestorage;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.avrgaming.civcraft.loreenhancements.LoreEnhancement;
import com.avrgaming.civcraft.main.CivData;
import com.avrgaming.civcraft.object.BuildableDamageBlock;
import com.avrgaming.civcraft.util.CivColor;
import com.avrgaming.civcraft.util.ItemManager;

import gpl.AttributeUtil;

public abstract class LoreMaterial {

	private String id;
	private int typeID;
	private short damage;

	private LinkedList<String> lore = new LinkedList<String>();
	private String name;
		
	public static Map<String, LoreMaterial> materialMap = new HashMap<String, LoreMaterial>();
	public static final String MID_TAG = CivColor.Black+"MID";
	
	public LoreMaterial(String id, int typeID, short damage) {
		this.id = id;
		this.typeID = typeID;
		this.damage = damage;
		/* Adding quotes around id since NBTString does it =\ */
		materialMap.put(id, this);
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	/* Sets MID in LORE, use for debugging. */
//	public static String getMID(ItemStack stack) {
//		AttributeUtil attrs = new AttributeUtil(stack);	
//		String[] lore = attrs.getLore();
//		
//		if (lore == null) {
//			return "";
//		}
//		
//		String[] split = lore[0].split(":");
//		if (!split[0].equals(MID_TAG)) {
//			return "";
//		}
//		
//		return split[1];
//	}
//	
//	public static void setMIDAndName(AttributeUtil attrs, String mid, String name) {
//		attrs.setLore(MID_TAG+":"+mid);
//		attrs.setName(name);
//	}
	
	/* Sets MID in NBT Data, use for production. */
	public static String getMID(ItemStack stack) {
		AttributeUtil attrs = new AttributeUtil(stack);
		
		String mid = attrs.getCivCraftProperty("mid");
		if (mid == null) {
			return "";
		}
		
		return mid;
	}
	
	public static void setMIDAndName(AttributeUtil attrs, String mid, String name) {
		attrs.setCivCraftProperty("mid", mid);
		attrs.setName(name);
	}
	
	public static boolean isCustom(ItemStack stack) {
		if (stack == null) {
			return false;
		}
		
		LoreMaterial material = getMaterial(stack);
		if (material == null) {
			return false;
		}
		return true;
	}
		
	public static LoreMaterial getMaterial(ItemStack stack) {
		if (stack == null) {
			return null;
		}
		return materialMap.get(getMID(stack));
	}
	
	/*
	 * Moves an item stack off of this slot by trying
	 * to re-add it to the inventory, if it fails, then
	 * we drop it on the ground.
	 */
	public void moveDropSet(Player player, Inventory inv, int slot, ItemStack newItem) {
		
		ItemStack stack = inv.getItem(slot);
		inv.setItem(slot, newItem);

		if (stack != null) {
			if (stack.equals(newItem)) {
				return;
			}
			
			HashMap<Integer, ItemStack> leftovers = inv.addItem(stack);
			
			for (ItemStack s : leftovers.values()) {
				player.getWorld().dropItem(player.getLocation(), s);
			}
		}
		
	}
	
	public Player getPlayer(InventoryClickEvent event) {
		if (event.getWhoClicked() instanceof Player) {
			return (Player)event.getWhoClicked();
		}
		return null;
	}

	public static ItemStack spawn(LoreMaterial material) {
		return spawn(material, 1);
	}
	
	public static ItemStack spawn(LoreMaterial material, int amount) {
		ItemStack stack = ItemManager.createItemStack(material.getTypeID(), amount, material.getDamage());
		AttributeUtil attrs = new AttributeUtil(stack);
		setMIDAndName(attrs, material.getId(), material.getName());
		
/*			ItemMeta im = stack.getItemMeta();
			im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			im.addItemFlags(ItemFlag.HIDE_DESTROYS);
			im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			im.addItemFlags(ItemFlag.HIDE_PLACED_ON);
			im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
			im.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);*/
		
		if (material instanceof LoreCraftableMaterial) {
			LoreCraftableMaterial craftMat = (LoreCraftableMaterial)material;
			attrs.addLore(CivColor.ITALIC+craftMat.getConfigMaterial().category);
			if (craftMat.getConfigMaterial().shiny) {
				attrs.setShiny();
			}
		}
		material.applyAttributes(attrs);
		return attrs.getStack();
	}
	
	public static boolean isSpamItem(ItemStack is) {
		switch (ItemManager.getId(is)) {
		case CivData.DIAMOND_HELMET:
		case CivData.DIAMOND_CHESTPLATE:
		case CivData.DIAMOND_LEGGINGS:
		case CivData.DIAMOND_BOOTS:
		case CivData.DIAMOND_SWORD:
		case CivData.DIAMOND_PICKAXE:
		case CivData.DIAMOND_SHOVEL:
		case CivData.DIAMOND_AXE:
		case CivData.DIAMOND_HOE:
			
		case CivData.IRON_HELMET:
		case CivData.IRON_CHESTPLATE:
		case CivData.IRON_LEGGINGS:
		case CivData.IRON_BOOTS:
		case CivData.IRON_SWORD:
		case CivData.IRON_PICKAXE:
		case CivData.IRON_SHOVEL:
		case CivData.IRON_AXE:
		case CivData.IRON_HOE:
			
		case CivData.GOLD_HELMET:
		case CivData.GOLD_CHESTPLATE:
		case CivData.GOLD_LEGGINGS:
		case CivData.GOLD_BOOTS:
		case CivData.GOLD_SWORD:
		case CivData.GOLD_PICKAXE:
		case CivData.GOLD_SHOVEL:
		case CivData.GOLD_AXE:
		case CivData.GOLD_HOE:
			
		case CivData.CHAIN_HELMET:
		case CivData.CHAIN_CHESTPLATE:
		case CivData.CHAIN_LEGGINGS:
		case CivData.CHAIN_BOOTS:
		case CivData.STONE_SWORD:
		case CivData.STONE_PICKAXE:
		case CivData.STONE_SHOVEL:
		case CivData.STONE_AXE:
		case CivData.STONE_HOE:
			
		case CivData.LEATHER_HELMET:
		case CivData.LEATHER_CHESTPLATE:
		case CivData.LEATHER_LEGGINGS:
		case CivData.LEATHER_BOOTS:
		case CivData.WOOD_SWORD:
		case CivData.WOOD_PICKAXE:
		case CivData.WOOD_SHOVEL:
		case CivData.WOOD_AXE:
		case CivData.WOOD_HOE:
			return true;
		default:
			return false;
		}
	}
	
	public int getTypeID() {
		return typeID;
	}

	public void setTypeID(int typeID) {
		this.typeID = typeID;
	}

	public short getDamage() {
		return damage;
	}

	public void setDamage(short damage) {
		this.damage = damage;
	}
	
	public void addLore(String lore) {
		this.lore.add(lore);
	}
	
	public void setLore(String lore) {
		this.lore.clear();
		this.lore.add(lore);
	}
	
	public void setLore(String[] lore) {
		this.lore.clear();
		for (String str : lore) {
			this.lore.add(str);
		}
	}
	
	public LinkedList<String> getLore() {
		return this.lore;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public static ItemStack addEnhancement(ItemStack stack, LoreEnhancement enhancement) {
		AttributeUtil attrs = new AttributeUtil(stack);
		attrs = enhancement.add(attrs);		
		return attrs.getStack();	
	}
	
	public static boolean hasEnhancement(ItemStack stack, String enhName) {
		AttributeUtil attrs = new AttributeUtil(stack);
		return attrs.hasEnhancement(enhName);
	}
	
	public static boolean hasEnhancements(ItemStack stack) {
		AttributeUtil attrs = new AttributeUtil(stack);
		return attrs.hasEnhancements();
	}
	
	public static LinkedList<LoreEnhancement> getEnhancements(ItemStack stack) {
		AttributeUtil attrs = new AttributeUtil(stack);
		return attrs.getEnhancements();
	}
	
	public void applyAttributes(AttributeUtil attrs) {
		/* 
		 * This is called when the item is created via the LoreMaterial.spawn() command.
		 * Can optionally be overriden by classes.
		 */
		return;
	}
	
	/* Events for this Material */
	public abstract void onHit(EntityDamageByEntityEvent event); /* Called when this is the item in-hand */
	public abstract void onInteract(PlayerInteractEvent event);
	public abstract void onInteractEntity(PlayerInteractEntityEvent event);
	public abstract void onBlockPlaced(BlockPlaceEvent event);
	public abstract void onBlockBreak(BlockBreakEvent event);
	public abstract void onBlockDamage(BlockDamageEvent event);
	public abstract void onBlockInteract(PlayerInteractEvent event);
	public abstract void onHold(PlayerItemHeldEvent event);
	public abstract void onDrop(PlayerDropItemEvent event);
	public abstract void onItemDrop(PlayerDropItemEvent event);
	public abstract void onItemCraft(CraftItemEvent event);
	public abstract void onItemPickup(PlayerPickupItemEvent event);
	public abstract void onItemSpawn(ItemSpawnEvent event);
	public abstract boolean onAttack(EntityDamageByEntityEvent event, ItemStack stack); /* Called when this item is in inventory. */
	public abstract void onInvItemPickup(InventoryClickEvent event, Inventory fromInv, ItemStack stack);
	public abstract void onInvItemDrop(InventoryClickEvent event, Inventory toInv, ItemStack stack);
	public abstract void onInvShiftClick(InventoryClickEvent event, Inventory fromInv, Inventory toInv, ItemStack stack);
	public abstract void onInvItemSwap(InventoryClickEvent event, Inventory toInv, ItemStack droppedStack, ItemStack pickedStack);	
	public abstract void onPlayerDeath(EntityDeathEvent event, ItemStack stack);
	public abstract void onInventoryClose(InventoryCloseEvent event);
	public void onDefense(EntityDamageByEntityEvent event, ItemStack stack) {}
	public int onStructureBlockBreak(BuildableDamageBlock dmgBlock, int damage) { return damage; }
	public void onInventoryOpen(InventoryOpenEvent event, ItemStack stack) {}
	
}