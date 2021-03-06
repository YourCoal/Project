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
package com.avrgaming.civcraft.items.units;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.avrgaming.civcraft.config.CivSettings;
import com.avrgaming.civcraft.config.ConfigMission;
import com.avrgaming.civcraft.config.ConfigUnit;
import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.lorestorage.LoreCraftableMaterial;
import com.avrgaming.civcraft.lorestorage.LoreMaterial;
import com.avrgaming.civcraft.main.CivData;
import com.avrgaming.civcraft.util.ItemManager;

public abstract class Unit {
	
	public static Archer ARCHER_UNIT;
	public static Warrior WARRIOR_UNIT;
	public static Berserker BERSERKER_UNIT;
	
	public static Crossbowman CROSSBOWMAN_UNIT;
	public static Swordsman SWORDSMAN_UNIT;
	public static Swordsman SPEARMAN_UNIT;
	
	public static Slinger SLINGER_UNIT;
	public static Musketman MUSKETMAN_UNIT;
	public static Knight KNIGHT_UNIT;
	
	public static Settler SETTLER_UNIT;
	public static Engineer ENGINEER_UNIT;
	
	public static Spy SPY_UNIT;
	public static ArrayList<MissionBook> SPY_MISSIONS = new ArrayList<MissionBook>();
	public static MissionBook SPY_INVESTIGATE_TOWN;
	public static MissionBook SPY_STEAL_TREASURY;
	public static MissionBook SPY_SUBVERT_GOVERNMENT;
	public static MissionBook SPY_POISON_GRANARY;
	public static MissionBook SPY_PIRATE;
	public static MissionBook SPY_SABOTAGE;
	
	public static void init() {
		SPY_UNIT = new Spy("u_spy", CivSettings.units.get("u_spy"));
		for (ConfigMission mission : CivSettings.missions.values()) {
			if (mission.slot > 0) {
				MissionBook book = new MissionBook(mission.id, Spy.BOOK_ID, (short)0);
				book.setName(mission.name);
				book.setupLore(book.getId());
				book.setParent(SPY_UNIT);
				book.setSocketSlot(mission.slot);
				SPY_UNIT.addMissionBook(book);
				SPY_MISSIONS.add(book);
			}
		}
		
		ARCHER_UNIT = new Archer("u_archer", CivSettings.units.get("u_archer"));
		WARRIOR_UNIT = new Warrior("u_warrior", CivSettings.units.get("u_warrior"));
		BERSERKER_UNIT = new Berserker("u_berserker", CivSettings.units.get("u_berserker"));
		
		CROSSBOWMAN_UNIT = new Crossbowman("u_crossbowman", CivSettings.units.get("u_crossbowman"));
		SWORDSMAN_UNIT = new Swordsman("u_swordsman", CivSettings.units.get("u_swordsman"));
		SPEARMAN_UNIT = new Swordsman("u_spearman", CivSettings.units.get("u_spearman"));
		
		SLINGER_UNIT = new Slinger("u_slinger", CivSettings.units.get("u_slinger"));
		MUSKETMAN_UNIT = new Musketman("u_musketman", CivSettings.units.get("u_musketman"));
		KNIGHT_UNIT = new Knight("u_knight", CivSettings.units.get("u_knight"));
		
		SETTLER_UNIT = new Settler("u_settler", CivSettings.units.get("u_settler"));
		ENGINEER_UNIT = new Engineer("u_engineer", CivSettings.units.get("u_engineer"));
	}
	
	public Unit() {
	}
	
	public Unit(Inventory inv) throws CivException {		
	}
	
	protected static boolean addItemNoStack(Inventory inv, ItemStack stack) {		
		ItemStack[] contents = inv.getContents();
		for (int i = 0; i < contents.length; i++) {
			if (contents[i] == null) {
				contents[i] = stack;
				inv.setContents(contents);
				return true;
			}
		}
		return false;
	}
	
	public static ConfigUnit getPlayerUnit(Player player) {
		for (ItemStack stack : player.getInventory().getContents()) {
			if (stack == null) {
				continue;
			}
			
			LoreMaterial material = LoreMaterial.getMaterial(stack);
			if (material != null && (material instanceof UnitMaterial)) {
				
				if(!UnitMaterial.validateUnitUse(player, stack)) {
					return null;
				}
				return ((UnitMaterial)material).getUnit();
			}
		}
		
		return null;
	}

	@SuppressWarnings("deprecation")
	public static void removeUnit(Player player) {
		
		for (ItemStack stack : player.getInventory().getContents()) {
			if (stack != null) {
				LoreMaterial material = LoreMaterial.getMaterial(stack);
				if (material != null) {
					if (material instanceof UnitMaterial) {
						player.getInventory().remove(stack);
						continue;
					}
					
					if (material instanceof UnitItemMaterial) {
						player.getInventory().remove(stack);
						continue;
					}
					
				}
			}
		}
		player.updateInventory();
	}

	public static boolean isWearingFullLeather(Player player) {
		
		try {
			if (ItemManager.getId(player.getEquipment().getBoots()) != CivData.LEATHER_BOOTS) {
				return false;
			}
			
			if (ItemManager.getId(player.getEquipment().getChestplate()) != CivData.LEATHER_CHESTPLATE) {
				return false;
			}
			
			if (ItemManager.getId(player.getEquipment().getHelmet()) != CivData.LEATHER_HELMET) {
				return false;
			}
			
			if (ItemManager.getId(player.getEquipment().getLeggings()) != CivData.LEATHER_LEGGINGS) {
				return false;
			}
		
		} catch (NullPointerException e) {
			return false;
		}
		return true;
	}
	
	public static boolean isWearingFullComposite(Player player) {
		
		for (ItemStack stack : player.getInventory().getArmorContents()) {
			
			LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(stack);
			if (craftMat == null) {
				return false;
			}
			
			if ((!craftMat.getConfigId().equals("mat_composite_leather_helmet"))) {
				return false;
			}
			
			if ((!craftMat.getConfigId().equals("mat_composite_leather_chestplate"))) {
				return false;
			}
			
			if ((!craftMat.getConfigId().equals("mat_composite_leather_leggings"))) {
				return false;
			}
			
			if ((!craftMat.getConfigId().equals("mat_composite_leather_boots"))) {
				return false;
			}
			
		}
		return true;	
	}
	
	public static boolean isWearingFullHardened(Player player) {
		
		for (ItemStack stack : player.getInventory().getArmorContents()) {
			
			LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(stack);
			if (craftMat == null) {
				return false;
			}
			
			if ((!craftMat.getConfigId().equals("mat_hardened_leather_helmet"))) {
				return false;
			}
			
			if ((!craftMat.getConfigId().equals("mat_hardened_leather_chestplate"))) {
				return false;
			}
			
			if ((!craftMat.getConfigId().equals("mat_hardened_leather_leggings"))) {
				return false;
			}
			
			if ((!craftMat.getConfigId().equals("mat_hardened_leather_boots"))) {
				return false;
			}
			
		}
		return true;	
	}
	
	public static boolean isWearingFullRefined(Player player) {
		
		for (ItemStack stack : player.getInventory().getArmorContents()) {
			
			LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(stack);
			if (craftMat == null) {
				return false;
			}
			
			if ((!craftMat.getConfigId().equals("mat_refined_leather_helmet"))) {
				return false;
			}
			
			if ((!craftMat.getConfigId().equals("mat_refined_leather_chestplate"))) {
				return false;
			}
			
			if ((!craftMat.getConfigId().equals("mat_refined_leather_leggings"))) {
				return false;
			}
			
			if ((!craftMat.getConfigId().equals("mat_refined_leather_boots"))) {
				return false;
			}
			
		}
		return true;	
	}
	
	public static boolean isWearingFullBasicLeather(Player player) {
		
		for (ItemStack stack : player.getInventory().getArmorContents()) {
			
			LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(stack);
			if (craftMat == null) {
				return false;
			}
			
			if ((!craftMat.getConfigId().equals("mat_leather_helmet"))) {
				return false;
			}
			
			if ((!craftMat.getConfigId().equals("mat_leather_chestplate"))) {
				return false;
			}
			
			if ((!craftMat.getConfigId().equals("mat_leather_leggings"))) {
				return false;
			}
			
			if ((!craftMat.getConfigId().equals("mat_leather_boots"))) {
				return false;
			}
			
		}
		return true;	
	}
	
	public static boolean isWearingAnyMetal(Player player) {
		return isWearingAnyChain(player) || isWearingAnyGold(player) || isWearingAnyIron(player) || isWearingAnyDiamond(player);
	}
	
	public static boolean isWearingAnyChain(Player player) {
		
		if (player.getEquipment().getBoots() != null) {
			if (player.getEquipment().getBoots().getType().equals(Material.CHAINMAIL_BOOTS)) {
				return true;
			}
		}
		
		if (player.getEquipment().getChestplate() != null) {
			if (player.getEquipment().getChestplate().getType().equals(Material.CHAINMAIL_CHESTPLATE)) {
				return true;
			}
		}
		
		if (player.getEquipment().getHelmet() != null) {
			if (player.getEquipment().getHelmet().getType().equals(Material.CHAINMAIL_HELMET)) {
				return true;
			}
		}
		
		if (player.getEquipment().getLeggings() != null) {
			if (player.getEquipment().getLeggings().getType().equals(Material.CHAINMAIL_LEGGINGS)) {
				return true;
			}
		}
		
		return false;
	}
	
	
	public static boolean isWearingAnyGold(Player player) {
		
		if (player.getEquipment().getBoots() != null) {
			if (player.getEquipment().getBoots().getType().equals(Material.GOLD_BOOTS)) {
				return true;
			}
		}
		
		if (player.getEquipment().getChestplate() != null) {
			if (player.getEquipment().getChestplate().getType().equals(Material.GOLD_CHESTPLATE)) {
				return true;
			}
		}
		
		if (player.getEquipment().getHelmet() != null) {
			if (player.getEquipment().getHelmet().getType().equals(Material.GOLD_HELMET)) {
				return true;
			}
		}
		
		if (player.getEquipment().getLeggings() != null) {
			if (player.getEquipment().getLeggings().getType().equals(Material.GOLD_LEGGINGS)) {
				return true;
			}
		}
		
		return false;
	}
	
	
	public static boolean isWearingAnyIron(Player player) {
		
		if (player.getEquipment().getBoots() != null) {
			if (ItemManager.getId(player.getEquipment().getBoots()) == CivData.IRON_BOOTS) {
				return true;
			}
		}
		
		if (player.getEquipment().getChestplate() != null) {
			if (ItemManager.getId(player.getEquipment().getChestplate()) == CivData.IRON_CHESTPLATE) {
				return true;
			}
		}
		
		if (player.getEquipment().getHelmet() != null) {
			if (ItemManager.getId(player.getEquipment().getHelmet()) == CivData.IRON_HELMET) {
				return true;
			}
		}
		
		if (player.getEquipment().getLeggings() != null) {
			if (ItemManager.getId(player.getEquipment().getLeggings()) == CivData.IRON_LEGGINGS) {
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean isWearingAnyDiamond(Player player) {
		
		if (player.getEquipment().getBoots() != null) {
			if (ItemManager.getId(player.getEquipment().getBoots()) == CivData.DIAMOND_BOOTS) {
				return true;
			}
		}
		
		if (player.getEquipment().getChestplate() != null) {
			if (ItemManager.getId(player.getEquipment().getChestplate()) == CivData.DIAMOND_CHESTPLATE) {
				return true;
			}
		}
		
		if (player.getEquipment().getHelmet() != null) {
			if (ItemManager.getId(player.getEquipment().getHelmet()) == CivData.DIAMOND_HELMET) {
				return true;
			}
		}
		
		if (player.getEquipment().getLeggings() != null) {
			if (ItemManager.getId(player.getEquipment().getLeggings()) == CivData.DIAMOND_LEGGINGS) {
				return true;
			}
		}
		
		return false;
	}
}
