package com.avrgaming.civcraft.mobs;

import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.EntityCreature;
import net.minecraft.server.v1_8_R3.EntityDamageSource;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_8_R3.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;

import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.main.CivLog;
import com.avrgaming.civcraft.mobs.MobSpawner.CustomMobLevel;
import com.avrgaming.civcraft.mobs.MobSpawner.CustomMobType;
import com.avrgaming.civcraft.mobs.components.MobComponentDefense;
import com.avrgaming.mob.ICustomMob;
import com.avrgaming.mob.MobBasePigZombie;

public class Savage extends CommonCustomMob implements ICustomMob {
	
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
		this.setKnockbackResistance(0.5);
		switch (this.getLevel()) {
		
		case LESSER:
			defense = new MobComponentDefense(3.5);
			setMaxHealth(15.0);
			modifySpeed(1.4);
			setAttack(5.0);
			coinDrop(5, 25);
			
			this.addDrop("mat_metallic_crystal_fragment_1", 0.05);
			this.addDrop("mat_forged_clay", 0.1);
			this.addDrop("mat_crafted_reeds", 0.1);
			this.addDrop("mat_crafted_sticks", 0.1);
			break;
			
		case GREATER:
			defense = new MobComponentDefense(10);
			setMaxHealth(30.0);
			modifySpeed(1.4);
			setAttack(10.0);
			coinDrop(10, 40);
			
			this.addDrop("mat_metallic_crystal_fragment_2", 0.05);
			this.addDrop("mat_aged_wood_stave", 0.1);
			this.addDrop("mat_crafted_string", 0.05);
			this.addDrop("mat_varnish", 0.01);
			this.addDrop("mat_sticky_resin", 0.01);
			break;
			
		case ELITE:
			defense = new MobComponentDefense(15);
			setMaxHealth(45.0);
			modifySpeed(1.4);
			setAttack(15.0);
			coinDrop(25,65);
			
			this.addDrop("mat_metallic_crystal_fragment_3", 0.05);
			this.addDrop("mat_aged_wood_stave", 0.1);
			this.addDrop("mat_varnish", 0.05);
			this.addDrop("mat_woven_threading", 0.1);
			this.addDrop("mat_sticky_resin", 0.1);
			this.addDrop("mat_smithy_resin", 0.01);
			break;
			
		case BRUTAL:
			defense = new MobComponentDefense(20);
			setMaxHealth(60.0);
			modifySpeed(1.4);
			setAttack(25.0);
			coinDrop(40, 100);
			
			this.addDrop("mat_metallic_crystal_fragment_4", 0.05);
			this.addDrop("mat_longbow_stave", 0.1);
			this.addDrop("mat_reinforced_braid", 0.15);
			this.addDrop("mat_leather_straps", 0.1);
			this.addDrop("mat_sticky_resin", 0.1);
			this.addDrop("mat_smithy_resin", 0.01);
			break;
			
		default:
			defense = new MobComponentDefense(1);
			break;
		}
		this.addComponent(defense);
	}

	@Override
	public String getBaseEntity() {
		return MobBasePigZombie.class.getName();
	}

	@Override
	public String getClassName() {
		return Savage.class.getName();
	}

	public static void register() {
		setValidBiome(CustomMobType.SAVAGE, CustomMobLevel.LESSER, Biome.DESERT);
		setValidBiome(CustomMobType.SAVAGE, CustomMobLevel.LESSER, Biome.DESERT_HILLS);
		setValidBiome(CustomMobType.SAVAGE, CustomMobLevel.LESSER, Biome.DESERT_MOUNTAINS);
		
		setValidBiome(CustomMobType.SAVAGE, CustomMobLevel.GREATER, Biome.SAVANNA);
		setValidBiome(CustomMobType.SAVAGE, CustomMobLevel.GREATER, Biome.SAVANNA_MOUNTAINS);
		setValidBiome(CustomMobType.SAVAGE, CustomMobLevel.GREATER, Biome.SAVANNA_PLATEAU);
		setValidBiome(CustomMobType.SAVAGE, CustomMobLevel.GREATER, Biome.SAVANNA_PLATEAU_MOUNTAINS);
		
		setValidBiome(CustomMobType.SAVAGE, CustomMobLevel.ELITE, Biome.MESA);
		setValidBiome(CustomMobType.SAVAGE, CustomMobLevel.ELITE, Biome.MESA_PLATEAU);
		setValidBiome(CustomMobType.SAVAGE, CustomMobLevel.ELITE, Biome.MEGA_TAIGA);
		setValidBiome(CustomMobType.SAVAGE, CustomMobLevel.ELITE, Biome.MEGA_SPRUCE_TAIGA);
		setValidBiome(CustomMobType.SAVAGE, CustomMobLevel.ELITE, Biome.MESA_PLATEAU_FOREST);
		setValidBiome(CustomMobType.SAVAGE, CustomMobLevel.ELITE, Biome.MEGA_SPRUCE_TAIGA_HILLS);
		setValidBiome(CustomMobType.SAVAGE, CustomMobLevel.ELITE, Biome.MEGA_TAIGA_HILLS);
		
		setValidBiome(CustomMobType.SAVAGE, CustomMobLevel.BRUTAL, Biome.MESA_BRYCE);
		setValidBiome(CustomMobType.SAVAGE, CustomMobLevel.BRUTAL, Biome.MESA_PLATEAU_MOUNTAINS);
		setValidBiome(CustomMobType.SAVAGE, CustomMobLevel.BRUTAL, Biome.MESA_PLATEAU_FOREST_MOUNTAINS);
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
				} catch (CivException e1) {
					CivLog.error("getData(level):"+getData("level"));
					e1.printStackTrace();
				}
			}
		}
		goalSelector.a(2, new PathfinderGoalMeleeAttack(e, EntityHuman.class, 1.0D, false));
	}
	
	@Override
	public void onTarget(EntityTargetEvent event) {
		super.onTarget(event);
		if (event.getReason().equals(TargetReason.FORGOT_TARGET) ||
			event.getReason().equals(TargetReason.TARGET_DIED)) {
			event.getEntity().remove();
			CommonCustomMob.customMobs.remove(this.entity.getUniqueID());
		}
		
		Location current = getLocation((EntityCreature) entity);
		Location targetLoc = event.getTarget().getLocation();
		if (current.distance(targetLoc) > this.getFollowRange()) {
			event.setCancelled(true);
		}
	}
	
//	public void onTarget(EntityTargetEvent event) {
//		Location current = getLocation((EntityCreature) entity);
//		Location targetLoc = event.getTarget().getLocation();
//		super.onTarget(event);
//		
//		if (current.distance(targetLoc) > TARGET_DISTANCE) {
//			event.setCancelled(true);
//		}
//	}
}
