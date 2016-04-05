package com.avrgaming.civcraft.items.units;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.avrgaming.civcraft.config.ConfigUnit;
import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.lorestorage.LoreMaterial;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.Town;
import com.avrgaming.civcraft.util.CivColor;

public class Solider extends UnitMaterial {
	
	public Solider(String id, ConfigUnit configUnit) {
		super(id, configUnit);
	}
	
//	@Override
//	public void onHit(EntityDamageByEntityEvent event) {
//		double damage = event.getDamage();
//		event.setDamage(damage+4);
//	}
//	
//	public void onAttack(EntityDamageByEntityEvent event) {
//		double dmg = event.getDamage()*1.5;
//		if (event.getDamager() instanceof Player) {
//			event.setDamage(dmg);
//		}
//	}
	
	@Override
	public void onHold(PlayerItemHeldEvent event) {
//		event.getPlayer().getItemInHand();
		event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999999, 2));
		CivMessage.send(event.getPlayer(), CivColor.Gray+CivColor.ITALIC+"You were given strength from your Solider.");
	}
	
	@Override
	public void onItemPickup(PlayerPickupItemEvent event) {
		event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999999, 2));
		CivMessage.send(event.getPlayer(), CivColor.Gray+CivColor.ITALIC+"You were given strength from your Solider.");
	}
	
	@Override
	public void onItemToPlayer(Player player, ItemStack stack) {
		player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999999, 2));
		CivMessage.send(player, CivColor.Gray+CivColor.ITALIC+"You were given strength from your Solider.");
	}
	
	
	public void onItemFromPlayer(Player player, ItemStack stack) {
		player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
		CivMessage.send(player, CivColor.Gray+CivColor.ITALIC+"Your strength was removed since you no longer have a Solider.");
	}
	
	@Override
	public void onItemDrop(PlayerDropItemEvent event) {
		event.getPlayer().removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
		CivMessage.send(event.getPlayer(), CivColor.Gray+CivColor.ITALIC+"Your strength was removed since you no longer have a Solider.");
	}
	
	@Override
	public void onPlayerDeath(EntityDeathEvent event, ItemStack stack) {
		event.getEntity().removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
		CivMessage.send(event.getEntity(), CivColor.Gray+CivColor.ITALIC+"Your strength was removed since you no longer have a Solider.");
	}

	@Override
	public void onDrop(PlayerDropItemEvent event) {
		event.getPlayer().removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
		CivMessage.send(event.getPlayer(), CivColor.Gray+CivColor.ITALIC+"Your strength was removed since you no longer have a Solider.");
	}
	
	public static void spawn(Inventory inv, Town town) throws CivException {
		ItemStack is = LoreMaterial.spawn(Unit.SOLIDER_UNIT);
		UnitMaterial.setOwningTown(town, is);
		if (!Unit.addItemNoStack(inv, is)) {
			throw new CivException("Cannot make "+Unit.SOLIDER_UNIT.getUnit().name+". Barracks chest is full! Make Room!");
		}
	}
}
