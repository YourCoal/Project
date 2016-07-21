/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package mobs.mob;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import mobs.nms.NMSUtil;

import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityGiantZombie;
import net.minecraft.server.v1_8_R3.Item;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.World;

public class MobBaseZombieGiant extends EntityGiantZombie implements ISpawnable {
	public ICustomMob customMob = null;

	public MobBaseZombieGiant(World world) {
		super(world);
	}
	
	public MobBaseZombieGiant(World world, ICustomMob custom) {
		super(world);
		NMSUtil.clearPathfinderGoals(this.goalSelector);
		NMSUtil.clearPathfinderGoals(this.targetSelector);
		customMob = custom;
	}
	
	/* Setting and loading custom NBT data. */
	@Override
	public void b(NBTTagCompound compound) {
		super.b(compound);
		compound.setString("customMobClass", this.customMob.getClassName());
		compound.setString("customMobData", this.customMob.getSaveString());
	}
	
	@Override
	public void a(NBTTagCompound compound) {
		super.a(compound);
		
		if (!compound.hasKey("customMobClass")) {
			System.out.println("NO CUSTOM CLASS FOUND REMOVING ENTITY.");
			this.world.removeEntity(this);
			return;
		}
		
		try {
			String className = compound.getString("customMobClass");
			Class<?> customClass = Class.forName(className);
			this.customMob = (ICustomMob)customClass.newInstance();
			this.customMob.loadSaveString(compound.getString("customMobData"));
			} catch (Exception e) {
			this.world.removeEntity(this);
			e.printStackTrace();
		}
	}
	
	@Override
	public ICustomMob getCustomMobInterface() {
		return null;
	}
	/* Do not drop items. */
	@Override
	protected Item getLoot() {
		return null;
	}
	
	@Override
	public void getRareDrop(int i) {
		return;
	}
	
	@Override
	public void die() {
		try {
		if (customMob != null) {
			customMob.onDeath(this);
	        CraftEventFactory.callEntityDeathEvent(this, new ArrayList<org.bukkit.inventory.ItemStack>());
		}
		super.die();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public int aE() {
		return super.aE();
	}

	@Override
	public boolean damageEntity(DamageSource damagesource, float f) {
		try {
		if (!super.damageEntity(damagesource, f)) {
			return false;
		}
		
		if (customMob != null) {
			customMob.onDamage(this, damagesource,
					this.goalSelector, this.targetSelector);
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	/* Try to prevent fire ticks.. */
	public void e() {
		try {
		super.aE();
		if (customMob != null) {
			customMob.onTick();	
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Entity spawnCustom(Location loc, ICustomMob iCustom) {
		CraftWorld world = (CraftWorld) loc.getWorld();
		World mcWorld = world.getHandle();
		MobBaseZombieGiant zombie = new MobBaseZombieGiant(mcWorld, iCustom);
		iCustom.setEntity(zombie);

		zombie.setPosition(loc.getX(), loc.getY(), loc.getZ());
		mcWorld.addEntity(zombie, SpawnReason.CUSTOM);
		
		return zombie;
	}
}
