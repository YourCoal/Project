package com.avrgaming.civcraft.items.components;

import gpl.AttributeUtil;

import com.avrgaming.civcraft.util.CivColor;

public class Soulbound extends ItemComponent {

	@Override
	public void onPrepareCreate(AttributeUtil attrs) {
		attrs.addLore(CivColor.Gold+"Soulbound");
	}

}
