package com.moblib.nms;

import net.minecraft.server.v1_9_R1.PathfinderGoalSelector;

public class NMSUtil {

	public static void clearPathfinderGoals(PathfinderGoalSelector selector) {
		/*
		 * Use reflection to edit the default PathfinderGoalSelector fields.
		 * Use this to delete pre-made pathfinder goals.
		 */

   //     Field gsa;
		try {
	//		gsa = net.minecraft.server.v1_9_R1.PathfinderGoalSelector.class.getDeclaredField("b");
	//		gsa.setAccessible(true);

		    /* Clear out the goals. */
	//		gsa.set(selector, new UnsafeList());
		    
		    /* Clear out the 'c' list as well. */
	//		gsa = net.minecraft.server.v1_9_R1.PathfinderGoalSelector.class.getDeclaredField("c");
	//		gsa.setAccessible(true);

		    /* Clear out the goals. */
	//		gsa.set(selector, new UnsafeList());
		    
//		} catch (NoSuchFieldException e) {
//			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
		}
		
	}
	
}
