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

public class Defensemen extends UnitMaterial {
	
	public Defensemen(String id, ConfigUnit configUnit) {
		super(id, configUnit);
	}
	
	@Override
	public void onHold(PlayerItemHeldEvent event) {
		event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 999999, 2));
		CivMessage.send(event.getPlayer(), CivColor.Gray+CivColor.ITALIC+"You were given resistance from your Solider.");
	}
	
	@Override
	public void onItemPickup(PlayerPickupItemEvent event) {
		event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 999999, 2));
		CivMessage.send(event.getPlayer(), CivColor.Gray+CivColor.ITALIC+"You were given resistance from your Solider.");
	}
	
	@Override
	public void onItemToPlayer(Player player, ItemStack stack) {
		player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 999999, 2));
		CivMessage.send(player, CivColor.Gray+CivColor.ITALIC+"You were given resistance from your Solider.");
	}
	
	
	public void onItemFromPlayer(Player player, ItemStack stack) {
		player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
		CivMessage.send(player, CivColor.Gray+CivColor.ITALIC+"Your resistance was removed since you no longer have a Solider.");
	}
	
	@Override
	public void onItemDrop(PlayerDropItemEvent event) {
		event.getPlayer().removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
		CivMessage.send(event.getPlayer(), CivColor.Gray+CivColor.ITALIC+"Your resistance was removed since you no longer have a Solider.");
	}
	
	@Override
	public void onPlayerDeath(EntityDeathEvent event, ItemStack stack) {
		event.getEntity().removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
		CivMessage.send(event.getEntity(), CivColor.Gray+CivColor.ITALIC+"Your resistance was removed since you no longer have a Solider.");
	}

	@Override
	public void onDrop(PlayerDropItemEvent event) {
		event.getPlayer().removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
		CivMessage.send(event.getPlayer(), CivColor.Gray+CivColor.ITALIC+"Your resistance was removed since you no longer have a Solider.");
	}
	
	public static void spawn(Inventory inv, Town town) throws CivException {
		ItemStack is = LoreMaterial.spawn(Unit.DEFENSEMEN_UNIT);
		UnitMaterial.setOwningTown(town, is);
		if (!Unit.addItemNoStack(inv, is)) {
			throw new CivException("Cannot make "+Unit.DEFENSEMEN_UNIT.getUnit().name+". Barracks chest is full! Make Room!");
		}
	}
}
