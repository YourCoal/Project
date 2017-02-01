package com.avrgaming.civcraft.lorestorage;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import com.avrgaming.civcraft.config.CivSettings;
import com.avrgaming.civcraft.config.ConfigTech;
import com.avrgaming.civcraft.config.ConfigTechItem;
import com.avrgaming.civcraft.items.components.Tagged;
import com.avrgaming.civcraft.main.CivData;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.util.CivColor;
import com.avrgaming.civcraft.util.ItemManager;

import gpl.AttributeUtil;

public class LoreCraftableMaterialListener implements Listener {

	@EventHandler(priority = EventPriority.LOW)
	public void OnCraftItemEvent(CraftItemEvent event) {
		if (event.getWhoClicked() instanceof Player) {
			Player player = (Player)event.getWhoClicked();
						
			LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(event.getInventory().getResult());
			if (craftMat == null) {
				
				/* Disable notch apples */
				ItemStack resultStack = event.getInventory().getResult();
				if (resultStack == null) {
					return;
				}
				
				if (resultStack.getType().equals(Material.GOLDEN_APPLE)) {
					CivMessage.sendError((Player)event.getWhoClicked(), "You cannot craft golden apples. Sorry.");
					event.setCancelled(true);
					return;
				}
				
				ConfigTechItem restrictedTechItem = CivSettings.techItems.get(ItemManager.getId(resultStack));
				if (restrictedTechItem != null) {
					ConfigTech tech = CivSettings.techs.get(restrictedTechItem.require_tech);
					CivMessage.sendError(player, "Your civilization doesn't have the required technology ("+tech.name+") to craft this item.");
					event.setCancelled(true);
					return;
				}
				
				return;
			}
			
			if (!craftMat.getConfigMaterial().playerHasTechnology(player)) {
				CivMessage.sendError(player, "You do not have the required technology ("+craftMat.getConfigMaterial().getRequireString()+") to craft this item.");
				event.setCancelled(true);
				return;
			}
		}
	}
	
	private boolean matrixContainsCustom(ItemStack[] matrix) {
		for (ItemStack stack : matrix) {
			if (LoreMaterial.isCustom(stack)) {
				return true;
			}
		}
		return false;
	}
	

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.LOW)
	public void OnPrepareItemCraftEvent(PrepareItemCraftEvent event) {
		
		if (event.getRecipe() instanceof ShapedRecipe) {
			String key = LoreCraftableMaterial.getShapedRecipeKey(event.getInventory().getMatrix());
			LoreCraftableMaterial loreMat = LoreCraftableMaterial.shapedKeys.get(key);

			if (loreMat == null) {
				if(LoreCraftableMaterial.isCustom(event.getRecipe().getResult())) {
					/* Result is custom, but we have found no custom recipie. Set to blank. */
					event.getInventory().setResult(new ItemStack(CivData.AIR));
				}
				
				if (matrixContainsCustom(event.getInventory().getMatrix())) {
					event.getInventory().setResult(new ItemStack(CivData.AIR));
				}
				
				return;
			} else {
				if(!LoreCraftableMaterial.isCustom(event.getRecipe().getResult())) {
					/* Result is not custom, but recipie is. Set to blank. */
					if (!loreMat.isVanilla()) {
						event.getInventory().setResult(new ItemStack(CivData.AIR));
						return;
					}
				}
			}
			
			String matName =loreMat.getId(); 
			if (matName.contains("_alt")) {
				String id = matName.replaceAll("_alt(.*)", "");
				loreMat = LoreCraftableMaterial.getCraftMaterialFromId(id);
			}
			
			ItemStack newStack;
			if (!loreMat.isVanilla()) {
				newStack = LoreMaterial.spawn(loreMat);
				AttributeUtil attrs = new AttributeUtil(newStack);
				loreMat.applyAttributes(attrs);
				newStack.setAmount(loreMat.getCraftAmount());
			} else {
				newStack = ItemManager.createItemStack(loreMat.getTypeID(), loreMat.getCraftAmount());
			}
			
			event.getInventory().setResult(newStack);
			
		} else {
			String key = LoreCraftableMaterial.getShapelessRecipeKey(event.getInventory().getMatrix());
			LoreCraftableMaterial loreMat = LoreCraftableMaterial.shapelessKeys.get(key);
						
			if (loreMat == null) {
				if(LoreCraftableMaterial.isCustom(event.getRecipe().getResult())) {
					/* Result is custom, but we have found no custom recipie. Set to blank. */
					event.getInventory().setResult(new ItemStack(CivData.AIR));
				}
				
				if (matrixContainsCustom(event.getInventory().getMatrix())) {
					event.getInventory().setResult(new ItemStack(CivData.AIR));
				}
				
				return;
			} else {
				if(!LoreCraftableMaterial.isCustom(event.getRecipe().getResult())) {
					/* Result is not custom, but recipie is. Set to blank. */
					if (!loreMat.isVanilla()) {
						event.getInventory().setResult(new ItemStack(CivData.AIR));
						return;
					}
				}
			}
			
			String matName =loreMat.getId(); 
			if (matName.contains("_alt")) {
				String id = matName.replaceAll("_alt(.*)", "");
				loreMat = LoreCraftableMaterial.getCraftMaterialFromId(id);
			}
			
			ItemStack newStack;
			if (!loreMat.isVanilla()) {
				newStack = LoreMaterial.spawn(loreMat);
				AttributeUtil attrs = new AttributeUtil(newStack);
				loreMat.applyAttributes(attrs);
				newStack.setAmount(loreMat.getCraftAmount());
			} else {
				newStack = ItemManager.createItemStack(loreMat.getTypeID(), loreMat.getCraftAmount());
			}	
			
			event.getInventory().setResult(newStack);
		}
		
		ItemStack result = event.getInventory().getResult();
		LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(result);
		if (craftMat != null) {
			if (craftMat.hasComponent("Tagged")) {
				String tag = Tagged.matrixHasSameTag(event.getInventory().getMatrix());
				if (tag == null) {
					event.getInventory().setResult(ItemManager.createItemStack(CivData.AIR, 1));
					return;
				}
				
				Tagged tagged = (Tagged)craftMat.getComponent("Tagged");
				ItemStack stack = tagged.addTag(event.getInventory().getResult(), tag);
				AttributeUtil attrs = new AttributeUtil(stack);
				attrs.addLore(CivColor.LightGray+tag);
				stack = attrs.getStack();
				event.getInventory().setResult(stack);
			}
		}
		
	}	
}
