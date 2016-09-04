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
import com.avrgaming.mob.MobBaseIronGolem;

public class BehemothOriginal extends CommonCustomMob implements ICustomMob {
	
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
		this.setKnockbackResistance(1.0);
		switch (this.getLevel()) {
		
		case LESSER:
			defense = new MobComponentDefense(3.5);
			setMaxHealth(75.0);
			modifySpeed(0.8);
//			setAttack(15.0);
			coinDrop(1, 25);
			
			this.addDrop("mat_ionic_crystal_fragment_1", 0.05);
			this.addDrop("mat_forged_clay", 0.1);
			this.addDrop("mat_crafted_reeds", 0.1);
			this.addDrop("mat_crafted_sticks", 0.1);
			break;
			
		case GREATER:
			defense = new MobComponentDefense(10);
			setMaxHealth(100.0);
			modifySpeed(0.8);
//			setAttack(25.0);
			coinDrop(10, 50);
			
			this.addDrop("mat_ionic_crystal_fragment_2", 0.05);
			this.addDrop("mat_steel_plate", 0.1);
			this.addDrop("mat_steel_ingot", 0.05);
			this.addDrop("mat_clay_molding", 0.05);
			this.addDrop("mat_varnish", 0.01);
			this.addDrop("mat_sticky_resin", 0.05);
			break;
			
		case ELITE:
			defense = new MobComponentDefense(15);
			setMaxHealth(125.0);
			modifySpeed(0.8);
//			setAttack(35.0);
			coinDrop(20, 75);
			
			this.addDrop("mat_ionic_crystal_fragment_3", 0.05);
			this.addDrop("mat_carbide_steel_plate", 0.1);
			this.addDrop("mat_carbide_steel_ingot", 0.05);
			this.addDrop("mat_clay_molding", 0.05);
			this.addDrop("mat_sticky_resin", 0.1);
			this.addDrop("mat_smithy_resin", 0.05);
			break;
			
		case BRUTAL:
			defense = new MobComponentDefense(20);
			setMaxHealth(150.0);
			modifySpeed(0.8);
//			setAttack(45.0);
			coinDrop(35, 125);
			
			this.addDrop("mat_ionic_crystal_fragment_4", 0.05);
			this.addDrop("mat_tungsten_plate", 0.1);
			this.addDrop("mat_tungsten_ingot", 0.05);
			this.addDrop("mat_clay_tungsten_casting", 0.05);
			this.addDrop("mat_sticky_resin", 0.1);
			this.addDrop("mat_smithy_resin", 0.05);
			break;
			
		default:
			defense = new MobComponentDefense(1);
			break;
		}
		this.addComponent(defense);
	}
	
	@Override
	public String getBaseEntity() {
		return MobBaseIronGolem.class.getName();
	}
	
	@Override
	public String getClassName() {
		return BehemothOriginal.class.getName();
	}
	
	public static void register() {
		setValidBiome(CustomMobType.BEHEMOTH, CustomMobLevel.LESSER, Biome.FROZEN_RIVER);
		setValidBiome(CustomMobType.BEHEMOTH, CustomMobLevel.LESSER, Biome.FROZEN_OCEAN);
		setValidBiome(CustomMobType.BEHEMOTH, CustomMobLevel.LESSER, Biome.COLD_BEACH);
		setValidBiome(CustomMobType.BEHEMOTH, CustomMobLevel.LESSER, Biome.COLD_TAIGA);
		
		setValidBiome(CustomMobType.BEHEMOTH, CustomMobLevel.GREATER, Biome.ICE_MOUNTAINS);
		setValidBiome(CustomMobType.BEHEMOTH, CustomMobLevel.GREATER, Biome.COLD_TAIGA_HILLS);
		setValidBiome(CustomMobType.BEHEMOTH, CustomMobLevel.GREATER, Biome.COLD_TAIGA_MOUNTAINS);
		
		setValidBiome(CustomMobType.BEHEMOTH, CustomMobLevel.ELITE, Biome.ICE_PLAINS);
		
		setValidBiome(CustomMobType.BEHEMOTH, CustomMobLevel.BRUTAL, Biome.ICE_PLAINS_SPIKES);
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
}
