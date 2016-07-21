/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.components;

public class AttributeRate extends AttributeBaseRate {

	@Override
	public double getGenerated() {
		if (this.getBuildable().isActive()) {
			return super.getDouble("value");
		} else {
			return 0.0;
		}	
	}

}
