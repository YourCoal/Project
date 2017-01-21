package com.avrgaming.civcraft.loregui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.avrgaming.civcraft.config.CivSettings;
import com.avrgaming.civcraft.config.ConfigTech;
import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.lorestorage.LoreGuiItem;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.object.Resident;

public class ResearchChooseTech implements GuiAction {
	
	@Override
	public void performAction(InventoryClickEvent event, ItemStack stack) {
		try {
			Player player = (Player)event.getWhoClicked();
			Resident res = CivGlobal.getResident(player);
			ConfigTech sinfo = CivSettings.techs.get(LoreGuiItem.getActionData(stack, "info"));
			res.getCiv().startTechnologyResearch(sinfo);
			event.getWhoClicked().closeInventory();
			return;
		} catch (CivException e) {
			e.printStackTrace();
		}
	}
}
