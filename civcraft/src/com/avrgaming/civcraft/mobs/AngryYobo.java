package com.avrgaming.civcraft.mobs;

import org.bukkit.Material;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

import com.avrgaming.civcraft.mobs.components.MobComponentDefense;
import com.avrgaming.civcraft.util.ItemManager;
import com.moblib.mob.ICustomMob;
import com.moblib.mob.MobBaseZombie;

import net.minecraft.server.v1_10_R1.EntityCreature;
import net.minecraft.server.v1_10_R1.EntityHuman;
import net.minecraft.server.v1_10_R1.EntityInsentient;
import net.minecraft.server.v1_10_R1.PathfinderGoalFloat;
import net.minecraft.server.v1_10_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_10_R1.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_10_R1.PathfinderGoalNearestAttackableTarget;

public class AngryYobo extends CommonCustomMob implements ICustomMob {

	public void onCreate() {
	    initLevelAndType();
	    
	    getGoalSelector().a(0, new PathfinderGoalFloat((EntityInsentient) entity));
	    getGoalSelector().a(2, new PathfinderGoalMeleeAttack((EntityCreature) entity, 1.0D, false));
	    getGoalSelector().a(8, new PathfinderGoalLookAtPlayer((EntityInsentient) entity, EntityHuman.class, 8.0F));
	    
	    getTargetSelector().a(2, new PathfinderGoalNearestAttackableTarget<EntityHuman>((EntityCreature) entity, EntityHuman.class, true));
	    this.setName(this.getLevel().getName()+" "+this.getType().getName());
	    MobBaseZombie zombie = ((MobBaseZombie)entity);
	    zombie.setBaby(true);
	}
	
	@Override
	public void onTick() {
		super.onTick();		
	}
	
	public void onCreateAttributes() {
		MobComponentDefense defense;
	    this.setKnockbackResistance(0.85);
		switch (this.getLevel()) {
		case SNOWY:
			defense = new MobComponentDefense(5);
			setMaxHealth(48.0);
			this.setAttack(24.0);
			this.addDrop("civ_crafted_reeds", 0.04);
			this.addDrop("civ_crafted_sticks", 0.04);
			this.addDrop("civ_refined_leather", 0.01);
			this.addDrop("civ_refined_string", 0.01);
			
		    this.addVanillaDrop(ItemManager.getId(Material.SNOW_BLOCK), (short)0, 0.10);
			
		    this.addVanillaDrop(ItemManager.getId(Material.IRON_ORE), (short)0, 0.01);
		    this.addVanillaDrop(ItemManager.getId(Material.DIAMOND_ORE), (short)0, 0.007);
			this.coinDrop(10, 40);
			break;
			
		case COLD:
			defense = new MobComponentDefense(5);
			setMaxHealth(48.0);
			this.setAttack(24.0);
			this.addDrop("civ_crafted_reeds", 0.04);
			this.addDrop("civ_crafted_sticks", 0.04);
			this.addDrop("civ_refined_leather", 0.01);
			this.addDrop("civ_refined_string", 0.01);
			
		    this.addVanillaDrop(ItemManager.getId(Material.ICE), (short)0, 0.10);
			
		    this.addVanillaDrop(ItemManager.getId(Material.GOLD_ORE), (short)0, 0.01);
		    this.addVanillaDrop(ItemManager.getId(Material.REDSTONE_ORE), (short)0, 0.007);
			this.coinDrop(10, 40);
			break;
			
		case LUSH:
			defense = new MobComponentDefense(5);
			setMaxHealth(48.0);
			this.setAttack(24.0);
			this.addDrop("civ_crafted_reeds", 0.04);
			this.addDrop("civ_crafted_sticks", 0.04);
			this.addDrop("civ_refined_leather", 0.01);
			this.addDrop("civ_refined_string", 0.01);
			
		    this.addVanillaDrop(ItemManager.getId(Material.CHORUS_FRUIT_POPPED), (short)0, 0.10);
			
		    this.addVanillaDrop(ItemManager.getId(Material.IRON_ORE), (short)0, 0.01);
		    this.addVanillaDrop(ItemManager.getId(Material.REDSTONE_ORE), (short)0, 0.007);
			this.coinDrop(10, 40);
			break;
			
		case WARM:
			defense = new MobComponentDefense(5);
			setMaxHealth(48.0);
			this.setAttack(24.0);
			this.addDrop("civ_crafted_reeds", 0.04);
			this.addDrop("civ_crafted_sticks", 0.04);
			this.addDrop("civ_refined_leather", 0.01);
			this.addDrop("civ_refined_string", 0.01);
			
		    this.addVanillaDrop(ItemManager.getId(Material.CACTUS), (short)0, 0.10);
			
		    this.addVanillaDrop(ItemManager.getId(Material.GOLD_ORE), (short)0, 0.01);
		    this.addVanillaDrop(ItemManager.getId(Material.DIAMOND_ORE), (short)0, 0.007);
			this.coinDrop(10, 40);
			break;
			
		case WATER:
			defense = new MobComponentDefense(5);
			setMaxHealth(48.0);
			modifySpeed(1.25);
			this.setAttack(24.0);
			this.addDrop("civ_crafted_reeds", 0.04);
			this.addDrop("civ_crafted_sticks", 0.04);
			this.addDrop("civ_refined_leather", 0.01);
			this.addDrop("civ_refined_string", 0.01);
			
		    this.addVanillaDrop(ItemManager.getId(Material.INK_SACK), (short)0, 0.10);
			
		    this.addVanillaDrop(ItemManager.getId(Material.COAL_ORE), (short)0, 0.01);
		    this.addVanillaDrop(ItemManager.getId(Material.LAPIS_ORE), (short)0, 0.007);
		    this.addVanillaDrop(ItemManager.getId(Material.REDSTONE_ORE), (short)0, 0.007);
		    this.addVanillaDrop(ItemManager.getId(Material.EMERALD_ORE), (short)0, 0.003);
			this.coinDrop(20, 60);
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
		return AngryYobo.class.getName();
	}
	
	@Override
	public void onTarget(EntityTargetEvent event) {
		super.onTarget(event);
		if (event.getReason().equals(TargetReason.FORGOT_TARGET) ||
		    event.getReason().equals(TargetReason.TARGET_DIED)) {
			event.getEntity().remove();
		}
	}
}
