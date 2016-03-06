package com.avrgaming.civcraft.util;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.server.v1_9_R1.AxisAlignedBB;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;

public class EntityProximity {
	
    public static double a;
    public static double b;
    public static double c;
    public static double d;
    public static double e;
    public static double f;
	
    public static AxisAlignedBB a(double d0, double d1, double d2, double g, double h, double i) {
        double d3 = a;
        double d4 = b;
        double d5 = c;
        double d6 = d;
        double d7 = e;
        double d8 = f;

        if (d0 < 0.0D) {
            d3 += d0;
        } else if (d0 > 0.0D) {
            d6 += d0;
        }

        if (d1 < 0.0D) {
            d4 += d1;
        } else if (d1 > 0.0D) {
            d7 += d1;
        }

        if (d2 < 0.0D) {
            d5 += d2;
        } else if (d2 > 0.0D) {
            d8 += d2;
        }

        return new AxisAlignedBB(d3, d4, d5, d6, d7, d8);
    }
    
	/* Use a NMS method to grab an axis aligned bounding box around an area to 
	 * determine which entities are within this radius. 
	 * Optionally provide an entity that is exempt from these checks.
	 * Also optionally provide a filter so we can only capture specific types of entities. */
	public static LinkedList<Entity> getNearbyEntities(Entity exempt, Location loc, double radius, Class<?> filter) {
		LinkedList<Entity> entities = new LinkedList<Entity>();
		
		double x = loc.getX()+0.5;
		double y = loc.getY()+0.5;
		double z = loc.getZ()+0.5;
		double r = radius;
		
		CraftWorld craftWorld = (CraftWorld)loc.getWorld();
		AxisAlignedBB bb = a(x-r, y-r, z-r, x+r, y+r, z+r);
		
		List<net.minecraft.server.v1_9_R1.Entity> eList;
		if (exempt != null) {
			 eList = craftWorld.getHandle().getEntities(((CraftEntity)exempt).getHandle(), bb);
		} else {
			 eList = craftWorld.getHandle().getEntities(null, bb);
		}
		
		for (net.minecraft.server.v1_9_R1.Entity e : eList) {
			
			
			if (filter == null || (filter.isInstance(e))) {
				entities.add(e.getBukkitEntity());
			}
		}
		
		return entities;
	}
	
}
