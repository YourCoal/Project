package com.avrgaming.civcraft.loregui;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.avrgaming.civcraft.book.CivBook;
import com.avrgaming.civcraft.config.CivSettings;
import com.avrgaming.civcraft.config.ConfigBuildableInfo;
import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.loregui.GuiAction;
import com.avrgaming.civcraft.loregui.OpenInventoryTask;
import com.avrgaming.civcraft.lorestorage.LoreGuiItem;
import com.avrgaming.civcraft.main.CivData;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.structure.wonders.Wonder;
import com.avrgaming.civcraft.threading.TaskMaster;
import com.avrgaming.civcraft.util.CivColor;
import com.avrgaming.civcraft.util.ItemManager;
import com.avrgaming.global.perks.Perk;

public class _1BuildChooseWonderTemplate implements GuiAction {

	@Override
	public void performAction(InventoryClickEvent event, ItemStack stack) {
		Player player = (Player)event.getWhoClicked();
		Resident resident = CivGlobal.getResident(player);
		ConfigBuildableInfo sinfo = CivSettings.wonders.get(LoreGuiItem.getActionData(stack, "info"));
		Wonder w;
		try {
			w = Wonder.newWonder(player.getLocation(), sinfo.id, resident.getTown());
		} catch (CivException e) {
			e.printStackTrace();
			return;
		}
		
		/* Look for any custom template perks and ask the player if they want to use them. */
		ArrayList<Perk> perkList = w.getTown().getTemplatePerks(w, resident, w.info);		
		ArrayList<Perk> personalUnboundPerks = resident.getUnboundTemplatePerks(perkList, w.info);
		//if (perkList.size() != 0 || personalUnboundPerks.size() != 0) {
			/* Store the pending buildable. */
		resident.pendingBuildable = w;
		
		/* Build an inventory full of templates to select. */
		Inventory inv = Bukkit.getServer().createInventory(player, CivBook.MAX_CHEST_SIZE*9);
		ItemStack infoRec = LoreGuiItem.build("Default "+w.getDisplayName(), 
				ItemManager.getId(Material.WRITTEN_BOOK), 
				0, CivColor.Gold+"<Click To Build>");
		infoRec = LoreGuiItem.setAction(infoRec, "BuildWithTemplate");
		inv.addItem(infoRec);
		
		for (Perk perk : perkList) {
			if (!perk.getIdent().contains("template")) {
				infoRec = LoreGuiItem.build(perk.getDisplayName(), 
						perk.configPerk.type_id, 
						perk.configPerk.data, CivColor.Gold+"<Click To Build>",
						CivColor.Gray+"Provided by: "+CivColor.LightBlue+perk.provider);
				infoRec = LoreGuiItem.setAction(infoRec, "BuildWithTemplate");
				infoRec = LoreGuiItem.setActionData(infoRec, "perk", perk.getIdent());
				inv.addItem(infoRec);
			}
		}
		
		for (Perk perk : personalUnboundPerks) {
			if (!perk.getIdent().contains("template")) {
					infoRec = LoreGuiItem.build(perk.getDisplayName(), 
							CivData.BEDROCK, 
							perk.configPerk.data, CivColor.Gold+"<Click To Bind>",
							CivColor.Gray+"Unbound Temple",
						CivColor.Gray+"You own this template.",
						CivColor.Gray+"The town is missing it.",
						CivColor.Gray+"Click to bind to town first.",
						CivColor.Gray+"Then build again.");				
				infoRec = LoreGuiItem.setAction(infoRec, "ActivatePerk");
				infoRec = LoreGuiItem.setActionData(infoRec, "perk", perk.getIdent());
			}
		}
		
		TaskMaster.syncTask(new OpenInventoryTask(player, inv));
		return;		
	}
}
