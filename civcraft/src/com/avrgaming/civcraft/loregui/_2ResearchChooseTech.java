package com.avrgaming.civcraft.loregui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.avrgaming.civcraft.config.CivSettings;
import com.avrgaming.civcraft.config.ConfigTech;
import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.lorestorage.LoreGuiItem;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.Civilization;
import com.avrgaming.civcraft.object.Resident;

public class _2ResearchChooseTech implements GuiAction {
	
	@Override
	public void performAction(InventoryClickEvent event, ItemStack stack) {
		try {
			Player player = (Player)event.getWhoClicked();
			Resident res = CivGlobal.getResident(player);
			ConfigTech sinfo = CivSettings.techs.get(LoreGuiItem.getActionData(stack, "info"));
			Civilization civ = res.getCiv();
			
			if (civ.getResearchTech() != null) {
				CivMessage.sendError(res, "Current researching "+civ.getResearchTech().name+". " +
						"If you want to change your focus, use /civ research switch instead.");
				return;
			}
			
			if (!civ.getTreasury().hasEnough(sinfo.getAdjustedTechCost(civ))) {
				CivMessage.sendError(res, "Our Civilization's treasury does have the required "+sinfo.getAdjustedTechCost(civ)+" coins to start this research.");
				return;
			}
			
			if (civ.hasTech(sinfo.id)) {
				CivMessage.sendError(res, "You already have this technology.");
				return;
			}
			
			if (!sinfo.isAvailable(civ)) {
				CivMessage.sendError(res, "You do not have the required technology to research this technology.");
				return;
			}
			
			res.getCiv().startTechnologyResearch(sinfo);
			event.getWhoClicked().closeInventory();
			return;
		} catch (CivException e) {
			e.printStackTrace();
		}
	}
}
