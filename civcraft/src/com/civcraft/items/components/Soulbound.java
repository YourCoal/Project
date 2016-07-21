/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.items.components;

import gpl.AttributeUtil;

import com.civcraft.util.CivColor;

public class Soulbound extends ItemComponent {

	@Override
	public void onPrepareCreate(AttributeUtil attrs) {
		attrs.addLore(CivColor.Gold+"Soulbound");
	}

}
