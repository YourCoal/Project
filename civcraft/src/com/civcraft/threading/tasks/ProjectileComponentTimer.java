/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.threading.tasks;

import java.util.ArrayList;

import com.civcraft.components.Component;
import com.civcraft.components.ProjectileComponent;
import com.civcraft.main.CivGlobal;

public class ProjectileComponentTimer implements Runnable {
	
	@Override
	public void run() {
		
		try {
			if (!CivGlobal.towersEnabled) {
				return;
			}
			
			Component.componentsLock.lock();
			try {
				ArrayList<Component> projectileComponents = Component.componentsByType.get(ProjectileComponent.class.getName());
				
				if (projectileComponents == null) {
					return;
				}
				
				for (Component c : projectileComponents) {
					ProjectileComponent projectileComponent = (ProjectileComponent)c;
					projectileComponent.process();
				}
			} finally {
				Component.componentsLock.unlock();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
