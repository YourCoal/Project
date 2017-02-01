package com.moblib.nms;

import java.lang.reflect.Field;

import com.google.common.collect.Sets;

import net.minecraft.server.v1_10_R1.PathfinderGoalSelector;

public class NMSUtil {
	
	public static void clearPathfinderGoals(PathfinderGoalSelector selector) {
		/*
		 * Use reflection to edit the default PathfinderGoalSelector fields.
		 * Use this to delete pre-made pathfinder goals.
		 */
		
		Field gsa;
		try {
			gsa = net.minecraft.server.v1_10_R1.PathfinderGoalSelector.class.getDeclaredField("b");
			gsa.setAccessible(true);
			
			/* Clear out the goals. */
			gsa.set(selector, Sets.newLinkedHashSet());
			
			/* Clear out the 'c' list as well. */
			gsa = net.minecraft.server.v1_10_R1.PathfinderGoalSelector.class.getDeclaredField("c");
			gsa.setAccessible(true);
			
			/* Clear out the goals. */
			gsa.set(selector, Sets.newLinkedHashSet());
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
