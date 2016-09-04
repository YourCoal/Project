package com.avrgaming.civcraft.mobs;

import net.minecraft.server.v1_8_R3.EntityCreature;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.PathfinderGoalFloat;
import net.minecraft.server.v1_8_R3.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_8_R3.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_8_R3.PathfinderGoalNearestAttackableTarget;

import org.bukkit.Location;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

import com.avrgaming.civcraft.mobs.components.MobComponentDefense;
import com.avrgaming.mob.ICustomMob;
import com.avrgaming.mob.MobBaseZombie;

public class AngryYobo  extends CommonCustomMob implements ICustomMob {
	
	public void onCreate() {
		initLevelAndType();
		getGoalSelector().a(0, new PathfinderGoalFloat((EntityInsentient) entity));
		getGoalSelector().a(8, new PathfinderGoalLookAtPlayer((EntityInsentient) entity, EntityHuman.class, 8.0F));
		getGoalSelector().a(2, new PathfinderGoalMeleeAttack((EntityCreature) entity, EntityHuman.class, 1.0D, false));
		getTargetSelector().a(2, new PathfinderGoalNearestAttackableTarget<EntityHuman>((EntityCreature) entity, EntityHuman.class, true));
		this.setName(this.getLevel().getName()+" "+this.getType().getName());
		MobBaseZombie zombie = ((MobBaseZombie)this.entity);
		zombie.setBaby(true);
		zombie.setVillager(true);
	}
	
	@Override
	public void onTick() {
		super.onTick();		
	}
	
	public void onCreateAttributes() {
		MobComponentDefense defense;
		this.setKnockbackResistance(0.85);
		switch (this.getLevel()) {
		
		case LESSER:
			defense = new MobComponentDefense(3.5);
			setMaxHealth(10.0);
			modifySpeed(0.7);
			setAttack(5.0);
			coinDrop(5, 25);
			
			this.addDrop("mat_metallic_crystal_fragment_1", 0.05);
			this.addDrop("mat_forged_clay", 0.1);
			this.addDrop("mat_crafted_reeds", 0.1);
			this.addDrop("mat_crafted_sticks", 0.1);
			break;
			
		case GREATER:
			defense = new MobComponentDefense(10);
			setMaxHealth(15.0);
			modifySpeed(0.7);
			setAttack(8.0);
			coinDrop(10, 40);
			
			this.addDrop("mat_metallic_crystal_fragment_2", 0.05);
			this.addDrop("mat_clay_steel_cast", 0.05);
			this.addDrop("mat_steel_ingot", 0.05);
			this.addDrop("mat_varnish", 0.01);
			this.addDrop("mat_sticky_resin", 0.01);
			break;
			
		case ELITE:
			defense = new MobComponentDefense(15);
			setMaxHealth(20.0);
			modifySpeed(0.7);
			setAttack(13.0);
			coinDrop(25,65);
			
			this.addDrop("mat_metallic_crystal_fragment_3", 0.05);
			this.addDrop("mat_clay_steel_cast", 0.05);
			this.addDrop("mat_carbide_steel_ingot", 0.05);
			this.addDrop("mat_sticky_resin", 0.1);
			this.addDrop("mat_smithy_resin", 0.01);
			break;
			
		case BRUTAL:
			defense = new MobComponentDefense(20);
			setMaxHealth(30.0);
			modifySpeed(0.7);
			setAttack(18.0);
			coinDrop(40, 100);
			
			this.addDrop("mat_metallic_crystal_fragment_4", 0.05);
			this.addDrop("mat_tungsten_ingot", 0.05);
			this.addDrop("mat_clay_tungsten_casting", 0.05);
			this.addDrop("mat_sticky_resin", 0.1);
			this.addDrop("mat_smithy_resin", 0.01);
			break;
			
		default:
			defense = new MobComponentDefense(2);
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
		return AngryYobo.class.getName();
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
