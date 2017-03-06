package com.avrgaming.civcraft.items.components;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.SpectralArrow;
import org.bukkit.entity.TippedArrow;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

import com.avrgaming.civcraft.config.ConfigUnit;
import com.avrgaming.civcraft.items.units.Unit;
import com.avrgaming.civcraft.loreenhancements.LoreEnhancement;
import com.avrgaming.civcraft.loreenhancements.LoreEnhancementDefense;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.util.CivColor;

import gpl.AttributeUtil;
import gpl.AttributeUtil.Attribute;
import gpl.AttributeUtil.AttributeType;

public class Defense extends ItemComponent {
	
	@Override
	public void onPrepareCreate(AttributeUtil attrs) {
		attrs.add(Attribute.newBuilder().name("Defense").
				type(AttributeType.GENERIC_ARMOR).
				amount(this.getDouble("value")).build());
//		attrs.setHideFlag(63);
		attrs.addLore(CivColor.Blue+""+this.getDouble("value")+" Defense");
		return;
	}
	
	@Override
	public void onHold(PlayerItemHeldEvent event) {
		Resident resident = CivGlobal.getResident(event.getPlayer());
		if (!resident.hasTechForItem(event.getPlayer().getInventory().getItem(event.getNewSlot()))) {		
			CivMessage.send(resident, CivColor.Rose+"Warning - "+CivColor.LightGray+
					"You do not have the required technology to use this item. It's defense output will be reduced in half.");
		}
	}
	
	@Override
	public void onDefense(EntityDamageByEntityEvent event, ItemStack stack) {
		double defValue = this.getDouble("value");
		double extraDef = 0;
		AttributeUtil attrs = new AttributeUtil(stack);
		/* Try to get any extra defense enhancements from this item. */
		for (LoreEnhancement enh : attrs.getEnhancements()) {
			if (enh instanceof LoreEnhancementDefense) {
				extraDef += ((LoreEnhancementDefense)enh).getExtraDefense(attrs);
			}
		}
		
		defValue += extraDef;
		if (event.getEntity() instanceof Player) {
			Player p = (Player)event.getEntity();
			Resident res = CivGlobal.getResident(p);
			
			//Enable Projectile Protection
			if (event.getDamager() instanceof Arrow || event.getDamager() instanceof SpectralArrow || event.getDamager() instanceof TippedArrow || event.getCause() == DamageCause.PROJECTILE) {
				if (p.getInventory().getHelmet() != null && p.getInventory().getHelmet().getType() != Material.AIR) {
					Map<Enchantment, Integer> helm = p.getInventory().getHelmet().getEnchantments();
					if (helm.containsKey(Enchantment.PROTECTION_PROJECTILE)) {
						int level = helm.get(Enchantment.PROTECTION_PROJECTILE);
						defValue += level*2;
					}
				}
				
				if (p.getInventory().getChestplate() != null && p.getInventory().getChestplate().getType() != Material.AIR) {
					Map<Enchantment, Integer> chest = p.getInventory().getChestplate().getEnchantments();
					if (chest.containsKey(Enchantment.PROTECTION_PROJECTILE)) {
						int level = chest.get(Enchantment.PROTECTION_PROJECTILE);
						defValue += level*2;
					}
				}
				
				if (p.getInventory().getLeggings() != null && p.getInventory().getLeggings().getType() != Material.AIR) {
					Map<Enchantment, Integer> pants = p.getInventory().getLeggings().getEnchantments();
					if (pants.containsKey(Enchantment.PROTECTION_PROJECTILE)) {
						int level = pants.get(Enchantment.PROTECTION_PROJECTILE);
						defValue += level*2;
					}
				}
				
				if (p.getInventory().getBoots() != null && p.getInventory().getBoots().getType() != Material.AIR) {
					Map<Enchantment, Integer> boots = p.getInventory().getBoots().getEnchantments();
					if (boots.containsKey(Enchantment.PROTECTION_PROJECTILE)) {
						int level = boots.get(Enchantment.PROTECTION_PROJECTILE);
						defValue += level*2;
					}
				}
			}
			
			//Enable Blast Protection
			if (event.getDamager() instanceof Fireball || event.getCause() == DamageCause.BLOCK_EXPLOSION || event.getCause() == DamageCause.ENTITY_EXPLOSION || event.getCause() == DamageCause.LIGHTNING) {
				if (p.getInventory().getHelmet() != null && p.getInventory().getHelmet().getType() != Material.AIR) {
					Map<Enchantment, Integer> helm = p.getInventory().getHelmet().getEnchantments();
					if (helm.containsKey(Enchantment.PROTECTION_EXPLOSIONS)) {
						int level = helm.get(Enchantment.PROTECTION_EXPLOSIONS);
						defValue += level*2;
					}
				}
				
				if (p.getInventory().getChestplate() != null && p.getInventory().getChestplate().getType() != Material.AIR) {
					Map<Enchantment, Integer> chest = p.getInventory().getChestplate().getEnchantments();
					if (chest.containsKey(Enchantment.PROTECTION_EXPLOSIONS)) {
						int level = chest.get(Enchantment.PROTECTION_EXPLOSIONS);
						defValue += level*2;
					}
				}
				
				if (p.getInventory().getLeggings() != null && p.getInventory().getLeggings().getType() != Material.AIR) {
					Map<Enchantment, Integer> pants = p.getInventory().getLeggings().getEnchantments();
					if (pants.containsKey(Enchantment.PROTECTION_EXPLOSIONS)) {
						int level = pants.get(Enchantment.PROTECTION_EXPLOSIONS);
						defValue += level*2;
					}
				}
				
				if (p.getInventory().getBoots() != null && p.getInventory().getBoots().getType() != Material.AIR) {
					Map<Enchantment, Integer> boots = p.getInventory().getBoots().getEnchantments();
					if (boots.containsKey(Enchantment.PROTECTION_EXPLOSIONS)) {
						int level = boots.get(Enchantment.PROTECTION_EXPLOSIONS);
						defValue += level*2;
					}
				}
			}
			
			ConfigUnit unit = Unit.getPlayerUnit(p);
			if (unit != null && unit.id.equals("u_berserker")) {
				defValue *= 1.15;
			} else if (unit != null && unit.id.equals("u_spearman")) {
				defValue *= 1.30;
			}
			
			if (!res.hasTechForItem(stack)) {
				defValue = defValue / 2;
			}
		}
		
		double dmg = event.getDamage();
		dmg -= defValue;
//		if (dmg < 0.5) { /* Always do at least 0.5 damage. */
//			dmg = 0.5;
//		}
		event.setDamage(dmg);
	}
}