package com.avrgaming.civcraft.mobs;

import java.util.LinkedList;

import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.main.CivLog;
import com.avrgaming.civcraft.mobs.MobSpawner.CustomMobLevel;
import com.avrgaming.civcraft.mobs.MobSpawner.CustomMobType;
import com.avrgaming.civcraft.mobs.components.MobComponentDefense;
import com.avrgaming.civcraft.util.ItemManager;
import com.moblib.mob.ICustomMob;
import com.moblib.mob.MobBaseZombie;

import net.minecraft.server.v1_10_R1.DamageSource;
import net.minecraft.server.v1_10_R1.Entity;
import net.minecraft.server.v1_10_R1.EntityCreature;
import net.minecraft.server.v1_10_R1.EntityDamageSource;
import net.minecraft.server.v1_10_R1.EntityHuman;
import net.minecraft.server.v1_10_R1.EntityInsentient;
import net.minecraft.server.v1_10_R1.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_10_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_10_R1.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_10_R1.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_10_R1.PathfinderGoalSelector;

public class Yobo extends CommonCustomMob implements ICustomMob {
	
	private boolean angry = false;
	LinkedList<Entity> minions = new LinkedList<Entity>();
	
	public void onCreate() {
		initLevelAndType();
		
		getGoalSelector().a(7, new PathfinderGoalRandomStroll((EntityCreature) entity, 1.0D));
		getGoalSelector().a(8, new PathfinderGoalLookAtPlayer((EntityInsentient) entity, EntityHuman.class, 8.0F));
		getTargetSelector().a(1, new PathfinderGoalHurtByTarget((EntityCreature) entity, true));
		
		this.setName(this.getLevel().getName()+" "+this.getType().getName());
	}
	
	@Override
	public void onTick() {
		super.onTick();		
	}
	
	public void onCreateAttributes() {
		MobComponentDefense defense;
		this.setKnockbackResistance(0.8);
		switch (this.getLevel()) {
		case SNOWY:
			defense = new MobComponentDefense(5);
			setMaxHealth(48.0);
			modifySpeed(1.25);
			this.setAttack(32.0);
			this.addDrop("civ_crafted_reeds", 0.04);
			this.addDrop("civ_crafted_sticks", 0.04);
			this.addDrop("civ_refined_leather", 0.01);
			this.addDrop("civ_refined_string", 0.01);
			
		    this.addVanillaDrop(ItemManager.getId(Material.SNOW_BLOCK), (short)0, 0.10);
			
		    this.addVanillaDrop(ItemManager.getId(Material.IRON_ORE), (short)0, 0.01);
		    this.addVanillaDrop(ItemManager.getId(Material.DIAMOND_ORE), (short)0, 0.007);
			this.coinDrop(15, 60);
			break;
			
		case COLD:
			defense = new MobComponentDefense(5);
			setMaxHealth(48.0);
			modifySpeed(1.25);
			this.setAttack(32.0);
			this.addDrop("civ_crafted_reeds", 0.04);
			this.addDrop("civ_crafted_sticks", 0.04);
			this.addDrop("civ_refined_leather", 0.01);
			this.addDrop("civ_refined_string", 0.01);
			
		    this.addVanillaDrop(ItemManager.getId(Material.ICE), (short)0, 0.10);
			
		    this.addVanillaDrop(ItemManager.getId(Material.GOLD_ORE), (short)0, 0.01);
		    this.addVanillaDrop(ItemManager.getId(Material.REDSTONE_ORE), (short)0, 0.007);
			this.coinDrop(15, 60);
			break;
			
		case LUSH:
			defense = new MobComponentDefense(5);
			setMaxHealth(48.0);
			modifySpeed(1.25);
			this.setAttack(32.0);
			this.addDrop("civ_crafted_reeds", 0.04);
			this.addDrop("civ_crafted_sticks", 0.04);
			this.addDrop("civ_refined_leather", 0.01);
			this.addDrop("civ_refined_string", 0.01);
			
		    this.addVanillaDrop(ItemManager.getId(Material.CHORUS_FRUIT_POPPED), (short)0, 0.10);
			
		    this.addVanillaDrop(ItemManager.getId(Material.IRON_ORE), (short)0, 0.01);
		    this.addVanillaDrop(ItemManager.getId(Material.REDSTONE_ORE), (short)0, 0.007);
			this.coinDrop(15, 60);
			break;
			
		case WARM:
			defense = new MobComponentDefense(5);
			setMaxHealth(48.0);
			modifySpeed(1.25);
			this.setAttack(32.0);
			this.addDrop("civ_crafted_reeds", 0.04);
			this.addDrop("civ_crafted_sticks", 0.04);
			this.addDrop("civ_refined_leather", 0.01);
			this.addDrop("civ_refined_string", 0.01);
			
		    this.addVanillaDrop(ItemManager.getId(Material.CACTUS), (short)0, 0.10);
			
		    this.addVanillaDrop(ItemManager.getId(Material.GOLD_ORE), (short)0, 0.01);
		    this.addVanillaDrop(ItemManager.getId(Material.DIAMOND_ORE), (short)0, 0.007);
			this.coinDrop(15, 60);
			break;
			
		case WATER:
			defense = new MobComponentDefense(5);
			setMaxHealth(48.0);
			modifySpeed(1.25);
			this.setAttack(32.0);
			this.addDrop("civ_crafted_reeds", 0.04);
			this.addDrop("civ_crafted_sticks", 0.04);
			this.addDrop("civ_refined_leather", 0.01);
			this.addDrop("civ_refined_string", 0.01);
			
		    this.addVanillaDrop(ItemManager.getId(Material.INK_SACK), (short)0, 0.10);
			
		    this.addVanillaDrop(ItemManager.getId(Material.COAL_ORE), (short)0, 0.01);
		    this.addVanillaDrop(ItemManager.getId(Material.LAPIS_ORE), (short)0, 0.007);
		    this.addVanillaDrop(ItemManager.getId(Material.REDSTONE_ORE), (short)0, 0.007);
		    this.addVanillaDrop(ItemManager.getId(Material.EMERALD_ORE), (short)0, 0.003);
			this.coinDrop(30, 75);
			break;
		default:
			defense = new MobComponentDefense(5);
			break;
		}
		this.addComponent(defense);
	}
	
	@Override
	public String getBaseEntity() {
		return MobBaseZombie.class.getName();
	}
	
	@Override
	public String getClassName() {
		return Yobo.class.getName();
	}
	
	@Override
	public void onDamage(EntityCreature e, DamageSource damagesource, PathfinderGoalSelector goalSelector, PathfinderGoalSelector targetSelector) {
		if (!(damagesource instanceof EntityDamageSource)) {
			return;
		}
		
		if (this.getLevel() == null) {
			this.setLevel(MobSpawner.CustomMobLevel.valueOf(getData("level")));
			if (this.getLevel() == null) {
				try {
					throw new CivException("Level was null after retry.");
				} catch (CivException e2) {
					CivLog.error("getData(level):"+getData("level"));
					e2.printStackTrace();
				}
			}
		}
		
		if (!angry) {
			angry = true;
			goalSelector.a(2, new PathfinderGoalMeleeAttack(e, 1.0D, false));
			for (int i = 0; i < 4; i++) {
				try {
					MobSpawner.spawnCustomMob(MobSpawner.CustomMobType.ANGRYYOBO, this.getLevel(), getLocation(e));
					this.minions.add(entity);
				} catch (CivException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	public static void register() {
		setValidBiome(CustomMobType.YOBO, CustomMobLevel.SNOWY, Biome.TAIGA_COLD);
		setValidBiome(CustomMobType.YOBO, CustomMobLevel.SNOWY, Biome.TAIGA_COLD_HILLS);
		setValidBiome(CustomMobType.YOBO, CustomMobLevel.SNOWY, Biome.MUTATED_TAIGA_COLD);
		
		setValidBiome(CustomMobType.YOBO, CustomMobLevel.COLD, Biome.TAIGA);
		setValidBiome(CustomMobType.YOBO, CustomMobLevel.COLD, Biome.TAIGA_HILLS);
		setValidBiome(CustomMobType.YOBO, CustomMobLevel.COLD, Biome.MUTATED_TAIGA);
		
		setValidBiome(CustomMobType.YOBO, CustomMobLevel.LUSH, Biome.PLAINS);
		setValidBiome(CustomMobType.YOBO, CustomMobLevel.LUSH, Biome.MUTATED_PLAINS);
		
		setValidBiome(CustomMobType.YOBO, CustomMobLevel.LUSH, Biome.FOREST);
		setValidBiome(CustomMobType.YOBO, CustomMobLevel.LUSH, Biome.FOREST_HILLS);
		setValidBiome(CustomMobType.YOBO, CustomMobLevel.LUSH, Biome.MUTATED_FOREST);
		
		setValidBiome(CustomMobType.YOBO, CustomMobLevel.WARM, Biome.DESERT);
		
		setValidBiome(CustomMobType.YOBO, CustomMobLevel.WATER, Biome.RIVER);
		setValidBiome(CustomMobType.YOBO, CustomMobLevel.WATER, Biome.FROZEN_RIVER);
	}
	
	@Override
	public void onTarget(EntityTargetEvent event) {
		super.onTarget(event);
		if (event.getReason().equals(TargetReason.FORGOT_TARGET) ||
			event.getReason().equals(TargetReason.TARGET_DIED)) {
			this.angry = false;
			for (Entity e : minions) {
				e.getBukkitEntity().remove();
			}
		}
	}
}
