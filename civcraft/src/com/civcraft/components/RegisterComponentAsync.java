/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.components;

import java.util.ArrayList;

import com.civcraft.structure.Buildable;

public class RegisterComponentAsync implements Runnable {

	public Buildable buildable;
	public Component component;
	public String name;
	boolean register;
	
	public RegisterComponentAsync(Buildable buildable, Component component, String name, boolean register) {
		this.buildable = buildable;
		this.component = component;
		this.name = name;
		this.register = register;
	}
	
	
	@Override
	public void run() {
		
		if (register) {
		Component.componentsLock.lock();
			try {
				ArrayList<Component> components = Component.componentsByType.get(name);
				
				if (components == null) {
					components = new ArrayList<Component>();
				}
			
				components.add(component);
				Component.componentsByType.put(name, components);
				if (buildable != null) {
					buildable.attachedComponents.add(component);
				}
			} finally {
				Component.componentsLock.unlock();
			}		
		} else {
			Component.componentsLock.lock();
			try {
				ArrayList<Component> components = Component.componentsByType.get(name);
				
				if (components == null) {
					return;
				}
			
				components.remove(component);
				Component.componentsByType.put(name, components);
				if (buildable != null) {
					buildable.attachedComponents.remove(component);
				}
			} finally {
				Component.componentsLock.unlock();
			}
		}
		
	}

	
}
