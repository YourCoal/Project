package com.avrgaming.civcraft.loregui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.avrgaming.civcraft.lorestorage.LoreGuiItem;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.main.CivLog;
import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.global.perks.Perk;

public class ShowTemplateType implements GuiAction {
	
	@Override
	public void performAction(InventoryClickEvent event, ItemStack stack) {
		Resident resident = CivGlobal.getResident((Player)event.getWhoClicked());
		String perk_id = LoreGuiItem.getActionData(stack, "perk");
		Perk perk = resident.perks.get(perk_id);
		if (perk != null) {
			if (perk.getIdent().startsWith("template_arctic")) {
				resident.showTemplatePerks("arctic", "Arctic");
			} else if (perk.getIdent().startsWith("template_aztec")) {
				resident.showTemplatePerks("aztec", "Aztec");
			} else if (perk.getIdent().startsWith("template_cultist")) {
				resident.showTemplatePerks("cultist", "Cultist");
			} else if (perk.getIdent().startsWith("template_egyptian")) {
				resident.showTemplatePerks("egyptian", "Egyptian");
			} else if (perk.getIdent().startsWith("template_elven")) {
				resident.showTemplatePerks("elven", "Elven");
			} else if (perk.getIdent().startsWith("template_roman")) {
				resident.showTemplatePerks("roman", "Roman");
			} else if (perk.getIdent().startsWith("template_hell")) {
				resident.showTemplatePerks("hell", "Hell");
			}
		} else {
			CivLog.error("Couldn't activate perk:"+perk_id+" cause it wasn't found in perks hashmap.");
		}
	}
}