package com.avrgaming.civcraft.loregui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.avrgaming.civcraft.config.CivSettings;
import com.avrgaming.civcraft.config.ConfigTech;
import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.lorestorage.LoreGuiItem;
import com.avrgaming.civcraft.main.CivGlobal;
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
				throw new CivException("Current researching "+civ.getResearchTech().name+". " +
						"If you want to change your focus, use /civ research switch instead.");
			}
			
			if (!civ.getTreasury().hasEnough(sinfo.getAdjustedTechCost(civ))) {
				throw new CivException("Our Civilization's treasury does have the required "+sinfo.getAdjustedTechCost(civ)+" coins to start this research.");
			}
			
			if (civ.hasTech(sinfo.id)) {
				throw new CivException("You already have this technology.");
			}
			
			if (!sinfo.isAvailable(civ)) {
				throw new CivException("You do not have the required technology to research this technology.");
			}
			
			res.getCiv().startTechnologyResearch(sinfo);
			event.getWhoClicked().closeInventory();
			return;
		} catch (CivException e) {
			e.printStackTrace();
		}
	}
}
