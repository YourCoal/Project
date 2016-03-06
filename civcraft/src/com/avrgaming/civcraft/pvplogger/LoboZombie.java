package com.avrgaming.civcraft.pvplogger;

import net.minecraft.server.v1_9_R1.DamageSource;
import net.minecraft.server.v1_9_R1.Entity;
import net.minecraft.server.v1_9_R1.EntityCreature;
import net.minecraft.server.v1_9_R1.EntityLiving;
import net.minecraft.server.v1_9_R1.PathfinderGoalSelector;

import java.util.HashMap;

import com.moblib.mob.ICustomMob;
import com.moblib.mob.MobBaseZombie;

public class LoboZombie implements ICustomMob {
	
	public static EntityLiving entity;
	public HashMap<String, String> dataMap = new HashMap<String, String>();
	
	public void onCreate() {
	}
	
	@Override
	public void onTick() {
	}
	
	@Override
	public String getBaseEntity() {
		return MobBaseZombie.class.getName();
	}
	
	@Override
	public void onDamage(EntityCreature e, DamageSource damagesource, PathfinderGoalSelector goalSelector, PathfinderGoalSelector targetSelector) {
	}
	
	@Override
	public void onDeath(EntityCreature e) {
	}
	
	public void onCreateAttributes() {
	}
	
	@Override
	public void onRangedAttack(Entity target) {
	}
	
	@Override
	public String getClassName() {
		return LoboZombie.class.getName();
	}
	
	public void setData(String key, String value) {
		dataMap.put(key, value);
	}
	
	public String getData(String key) {
		return dataMap.get(key);
	}
	
	@Override
	public void setEntity(EntityLiving e) {
		entity = e;
	}
	
	@Override
	public String getSaveString() {
		return this.getData("type")+":"+this.getData("level");
	}
	
	@Override
	public void loadSaveString(String str) {
		String[] split = str.split(":");
		this.setData("type", split[0]);
		this.setData("level", split[1]);
		if (entity == null) {
			return;
		}
		
		this.onCreate();
		this.onCreateAttributes();
	}
}
