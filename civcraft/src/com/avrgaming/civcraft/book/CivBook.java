package com.avrgaming.civcraft.book;

import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.avrgaming.civcraft.config.ConfigMaterial;
import com.avrgaming.civcraft.config.ConfigMaterialCategory;
import com.avrgaming.civcraft.lorestorage.LoreCraftableMaterial;
import com.avrgaming.civcraft.lorestorage.LoreGuiItem;
import com.avrgaming.civcraft.lorestorage.LoreGuiItemListener;
import com.avrgaming.civcraft.lorestorage.LoreMaterial;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.util.CivColor;
import com.avrgaming.civcraft.util.ItemManager;

import gpl.AttributeUtil;

public class CivBook {

	public static Inventory tutorialInventory = null;
	public static Inventory craftingHelpInventory = null;
	public static Inventory guiInventory = null;
	public static final int MAX_CHEST_SIZE = 6;
	
	public static void showTutorialInventory(Player player) {	
		if (tutorialInventory == null) {
			tutorialInventory = Bukkit.getServer().createInventory(player, 9*3, "CivCraft Tutorial");
			
			tutorialInventory.addItem(LoreGuiItem.build(CivColor.LightBlue+ChatColor.BOLD+"What is CivCraft?", ItemManager.getId(Material.WORKBENCH), 0, 
				ChatColor.RESET+"CivCraft is a game about building civilizations set in",
				ChatColor.RESET+"a large world filled with players. Players start",
				ChatColor.RESET+"out as nomads, gathering resources and making friends",
				ChatColor.RESET+"with people until they can build a camp.",
				ChatColor.RESET+CivColor.LightGreen+"Gather resources, allies, and found a civilization!",
				ChatColor.RESET+CivColor.LightGreen+"Research technology, build structures, and conquer the world!"
				));
		
			tutorialInventory.addItem(LoreGuiItem.build(CivColor.LightBlue+ChatColor.BOLD+"Explore", ItemManager.getId(Material.COMPASS), 0, 
					ChatColor.RESET+"Venture outward from spawn into the wild",
					ChatColor.RESET+"and find a spot to settle. You may encounter",
					ChatColor.RESET+"trade resources and different mobs in biomes.",
					ChatColor.RESET+"Different biomes generate different resources."
					));
			
			tutorialInventory.addItem(LoreGuiItem.build(CivColor.LightBlue+ChatColor.BOLD+"Custom Materials", ItemManager.getId(Material.DIAMOND_ORE), 0, 
					ChatColor.RESET+"CivCraft contains many custom items.",
					ChatColor.RESET+"These items are crafted using a crafting bench",
					ChatColor.RESET+"by combining normal Minecraft items into higher",
					ChatColor.RESET+"tier items. Certain items like iron, gold, diamonds,",
					ChatColor.RESET+"and emeralds can be exchanged for coins at a "+CivColor.Yellow+"Bank"+ChatColor.RESET+".",
					ChatColor.RESET+"Materials can be bought and sold at a "+CivColor.Yellow+"Market"+ChatColor.RESET+"."
					));
			
			tutorialInventory.addItem(LoreGuiItem.build(CivColor.LightBlue+ChatColor.BOLD+"Towns", ItemManager.getId(Material.FENCE), 0, 
					ChatColor.RESET+"Towns can be created by players to protect areas",
					ChatColor.RESET+"from outsiders. Inside a town, residents are",
					ChatColor.RESET+"free to build creatively withoutgriefers.",
					ChatColor.RESET+"Towns require a unit to create and coins to maintain.",
					ChatColor.RESET+"Towns can build structures, which allow its",
					ChatColor.RESET+"residents to access more features. Towns can only be",
					ChatColor.RESET+"built near your civilization's capital."
					));
			
			tutorialInventory.addItem(LoreGuiItem.build(CivColor.LightBlue+ChatColor.BOLD+"Civilizations", ItemManager.getId(Material.GOLD_HELMET), 0, 
					ChatColor.RESET+"A civilization is a collection of towns that share",
					ChatColor.RESET+"technology, which is researched by the civ. Many",
					ChatColor.RESET+"items and structures are only obtainable through the ",
					ChatColor.RESET+"use of technology. Founding your own civ is a lot of work,",
					ChatColor.RESET+"you must be an influential leader and bring people together",
					ChatColor.RESET+"in order for your civilization to survive and flourish."
					));
			
			if (CivGlobal.isCasualMode()) {
				tutorialInventory.setItem(8, LoreGuiItem.build(CivColor.LightBlue+ChatColor.BOLD+"Casual Mode!", ItemManager.getId(Material.FIREWORK), 0, 
						ChatColor.RESET+"War allows civilizations to settle their differences.",
						ChatColor.RESET+"In casual mode, Civs have to the option to request war from",
						ChatColor.RESET+"each other. The winner of a war is awarded a trophy which can be",
						ChatColor.RESET+"displayed in an item frame for bragging rights.",
						ChatColor.RESET+"After a civilization is defeated in war, war must be requested again."
						));
			} else {
				tutorialInventory.setItem(8, LoreGuiItem.build(CivColor.LightBlue+ChatColor.BOLD+"War Mode!", ItemManager.getId(Material.TNT), 0, 
						ChatColor.RESET+"War allows civilizations to settle their differences.",
						ChatColor.RESET+"Normally, all structures inside a civilization are protected",
						ChatColor.RESET+"from damage. However, civs have to the option to declare war",
						ChatColor.RESET+"on each other and do damage to each other's structures, and even",
						ChatColor.RESET+"capture towns. Each weekend, WarTime is enabled for two hours",
						ChatColor.RESET+"and players at war must defend their civ and conquer their enemies."
						));
			}
			
/*			tutorialInventory.setItem(8, LoreGuiItem.build(CivColor.LightBlue+ChatColor.BOLD+"More Info?", ItemManager.getId(Material.BOOK_AND_QUILL), 0, 
					ChatColor.RESET+"There is much more information you will require for your",
					ChatColor.RESET+"journey into CivCraft. Please visit the wiki at ",
					ChatColor.RESET+CivColor.LightGreen+ChatColor.BOLD+"http://civcraft.net/wiki",
					ChatColor.RESET+"For more detailed information about CivCraft and it's features."
					));*/
			
			tutorialInventory.setItem(9, LoreGuiItem.build(CivColor.LightBlue+ChatColor.BOLD+"QUEST: Build a Camp", ItemManager.getId(Material.BOOK_AND_QUILL), 0, 
					ChatColor.RESET+"First things first, in order to start your journey",
					ChatColor.RESET+"you must first build a camp. Camps allow you to store",
					ChatColor.RESET+"your materials safely, and allow you to obtain leadership",
					ChatColor.RESET+"tokens which can be crafted into a civ. The recipe for a camp is below."
					));
			
			tutorialInventory.setItem(10, LoreGuiItem.build(CivColor.LightBlue+ChatColor.BOLD+"QUEST: Found a Civ", ItemManager.getId(Material.BOOK_AND_QUILL), 0, 
					ChatColor.RESET+"Next, you'll want to start a civilization.",
					ChatColor.RESET+"To do this, you must first obtain leadership tokens",
					ChatColor.RESET+"by feeding bread to your camp's longhouse.",
					ChatColor.RESET+"Once you have enough leadership tokens.",
					ChatColor.RESET+"You can craft the founding flag item below."
					));
			
/*			tutorialInventory.setItem(11, LoreGuiItem.build(CivColor.LightBlue+ChatColor.BOLD+"Need to know a recipe?", ItemManager.getId(Material.WORKBENCH), 0, 
					ChatColor.RESET+"Type /res book to obtain the tutorial book",
					ChatColor.RESET+"and then click on 'Crafting Recipies'",
					ChatColor.RESET+"Every new item in CivCraft is listed here",
					ChatColor.RESET+"along with how to craft them.",
					ChatColor.RESET+"Good luck!"
					));*/
			
			for (ConfigMaterialCategory cat : ConfigMaterialCategory.getCategories()) {
				for (ConfigMaterial mat : cat.materials.values()) {
					if (mat.id.equals("civ_found_civ")) {
						ItemStack stack = getInfoBookForItem(mat.id);
						if (stack != null) {
							stack = LoreGuiItem.setAction(stack, "TutorialRecipe");
							tutorialInventory.setItem(19, LoreGuiItem.asGuiItem(stack));
						}
					} else if (mat.id.equals("civ_found_camp")) {
						ItemStack stack = getInfoBookForItem(mat.id);
						if (stack != null) {
							stack = LoreGuiItem.setAction(stack, "TutorialRecipe");
							tutorialInventory.setItem(18, LoreGuiItem.asGuiItem(stack));
						}
					}
				}
			}
			
			/* Add back buttons. */
			ItemStack backButton = LoreGuiItem.build("Back", ItemManager.getId(Material.MAP), 0, "Back to Categories");
			backButton = LoreGuiItem.setAction(backButton, "OpenInventory");
			backButton = LoreGuiItem.setActionData(backButton, "invType", "showGuiInv");
			backButton = LoreGuiItem.setActionData(backButton, "invName", guiInventory.getName());
			tutorialInventory.setItem((9*3)-1, backButton);
			
			LoreGuiItemListener.guiInventories.put(tutorialInventory.getName(), tutorialInventory);
		}
		
		if (player != null && player.isOnline() && player.isValid()) {
			player.openInventory(tutorialInventory);	
		}
	}
	
	public static ItemStack getInfoBookForItem(String matID) {
		LoreCraftableMaterial loreMat = LoreCraftableMaterial.getCraftMaterialFromId(matID);
		ItemStack stack = LoreMaterial.spawn(loreMat);
							
		if (!loreMat.isCraftable()) {
			return null;
		}
		
		AttributeUtil attrs = new AttributeUtil(stack);
		attrs.removeAll(); /* Remove all attribute modifiers to prevent them from displaying */
		LinkedList<String> lore = new LinkedList<String>();
		
		lore.add(""+ChatColor.RESET+ChatColor.BOLD+ChatColor.GOLD+"Click For Recipe");
		
		attrs.setLore(lore);				
		stack = attrs.getStack();
		return stack;
	}
	
	public static void showCraftingHelp(Player player) {
		if (craftingHelpInventory == null) {
			craftingHelpInventory = Bukkit.getServer().createInventory(player, 9*4, "Custom Item Recipes");

			/* Build the Category Inventory. */
			for (ConfigMaterialCategory cat : ConfigMaterialCategory.getCategories()) {
				if (cat.craftableCount == 0) {
					continue;
				}
				
				ItemStack infoRec = LoreGuiItem.build(cat.name, ItemManager.getId(Material.WRITTEN_BOOK), 0, 
						CivColor.LightBlue+cat.materials.size()+" Items", CivColor.Gold+"<Click To Open>");
						infoRec = LoreGuiItem.setAction(infoRec, "OpenInventory");
						infoRec = LoreGuiItem.setActionData(infoRec, "invType", "showGuiInv");
						infoRec = LoreGuiItem.setActionData(infoRec, "invName", cat.name+" Recipes");
						
						craftingHelpInventory.addItem(infoRec);
						
						
				Inventory inv = Bukkit.createInventory(player, 9*4, cat.name+" Recipes");
				for (ConfigMaterial mat : cat.materials.values()) {
					ItemStack stack = getInfoBookForItem(mat.id);
					if (stack != null) {
						stack = LoreGuiItem.setAction(stack, "ShowRecipe");
						inv.addItem(LoreGuiItem.asGuiItem(stack));
					}
				}
				
				/* Add Information Item */
				ItemStack info = LoreGuiItem.build("Information", ItemManager.getId(Material.SHIELD), 0,
						CivColor.White+"This GUI displays the materials",
						CivColor.White+"of which the category that you",
						CivColor.White+"previously selected. Clicking a.",
						CivColor.White+"material will show you a recipe."
						);
				inv.setItem((9*4)-2, info);
				
				/* Add back buttons. */
				ItemStack backButton = LoreGuiItem.build("Back", ItemManager.getId(Material.MAP), 0, "Back to Categories");
				backButton = LoreGuiItem.setAction(backButton, "OpenInventory");
				backButton = LoreGuiItem.setActionData(backButton, "invType", "showCraftingHelp");
				inv.setItem((9*4)-1, backButton);
				
				LoreGuiItemListener.guiInventories.put(inv.getName(), inv);
			}
			
			/* Add Information Item */
			ItemStack info = LoreGuiItem.build("Information", ItemManager.getId(Material.SHIELD), 0,
					CivColor.White+"This GUI displays all of the material",
					CivColor.White+"categories that a person can view",
					CivColor.White+"for crafting. Clicking a category",
					CivColor.White+"will show you items in the category."
					);
			craftingHelpInventory.setItem((9*4)-2, info);
			
			
			/* Add back buttons. */
			ItemStack backButton = LoreGuiItem.build("Back", ItemManager.getId(Material.MAP), 0, "Back to Categories");
			backButton = LoreGuiItem.setAction(backButton, "OpenInventory");
			backButton = LoreGuiItem.setActionData(backButton, "invType", "showGuiInv");
			backButton = LoreGuiItem.setActionData(backButton, "invName", guiInventory.getName());
			craftingHelpInventory.setItem((9*4)-1, backButton);
			
			LoreGuiItemListener.guiInventories.put(craftingHelpInventory.getName(), craftingHelpInventory);
		}
		
		player.openInventory(craftingHelpInventory);
	}
	
	public static void spawnGuiBook(Player player) {
		if (guiInventory == null) {
			guiInventory = Bukkit.getServer().createInventory(player, 9*3, "Knowledge of CivCraft");
			//00 01 02 03 04 05 06 07 08
			//09 10 11 12 13 14 15 16 17
			//18 19 20 21 22 23 24 25 26
			
			Resident res = CivGlobal.getResident(player);
			ItemStack playerInfo = LoreGuiItem.build("Player Info", ItemManager.getId(Material.SKULL_ITEM), 3,
					CivColor.Gold+"Coins: "+CivColor.LightGreen+res.getTreasury().getBalance(),
					CivColor.Gold+"Citizen XP: "+CivColor.RoseItalic+"Coming Soon");
			guiInventory.setItem(0, playerInfo);
			
			ItemStack civDip = LoreGuiItem.build("Diplomatic Relations", ItemManager.getId(Material.NAME_TAG), 0, CivColor.Gold+"<Click to View>", "civ_tag000_COMING_SOON");
//			civDip = LoreGuiItem.setAction(civDip, "BuildDiplomaticMenu");
			guiInventory.setItem(1, civDip);
			
			ItemStack craftRec = LoreGuiItem.build("Crafting Recipes", ItemManager.getId(Material.WORKBENCH), 0, CivColor.Gold+"<Click To View>");
			craftRec = LoreGuiItem.setAction(craftRec, "OpenInventory");
			craftRec = LoreGuiItem.setActionData(craftRec, "invType", "showCraftingHelp");
			guiInventory.setItem(3, craftRec);
			
			ItemStack newsInfo = LoreGuiItem.build("CivCraft Daily News", ItemManager.getId(Material.PAPER), 0, CivColor.Gold+"<Click To View>");
			newsInfo = LoreGuiItem.setAction(newsInfo, "NewspaperInventory");
			guiInventory.setItem(4, newsInfo);
			
			ItemStack dynmapInfo = LoreGuiItem.build("Useful Links", ItemManager.getId(Material.LADDER), 0, CivColor.Gold+"<Click to View>", "links_tag000_COMING_SOON");
//			dynmapInfo = LoreGuiItem.setAction(dynmapInfo, "SendDynmapLink");
			guiInventory.setItem(7, dynmapInfo);
			
	/*
			ItemStack dynmapInfo = LoreGuiItem.build("Dynmap", ItemManager.getId(Material.MAP), 0, CivColor.Gold+"<Click to View>", "map_tag000");
//			dynmapInfo = LoreGuiItem.setAction(dynmapInfo, "SendDynmapLink");
			guiInventory.setItem(6, dynmapInfo);
			
			ItemStack techTreeInfo = LoreGuiItem.build("Tech Tree", ItemManager.getId(Material.BOOK), 0, CivColor.Gold+"<Click to View>", "techtree_tag000");
//			techTreeInfo = LoreGuiItem.setAction(techTreeInfo, "SendDynmapLink");
			guiInventory.setItem(7, techTreeInfo);
		*/
			
			ItemStack gameInfo = LoreGuiItem.build("CivCraft Overview", ItemManager.getId(Material.WRITTEN_BOOK), 0, CivColor.Gold+"<Click To View>");
			gameInfo = LoreGuiItem.setAction(gameInfo, "OpenInventory");
			gameInfo = LoreGuiItem.setActionData(gameInfo, "invType", "showTutorialInventory");
			guiInventory.setItem(8, gameInfo);
			
			ItemStack buildMenu = LoreGuiItem.build("Building Menu", ItemManager.getId(Material.SLIME_BLOCK), 0, CivColor.Gold+"<Click to View>");
			buildMenu = LoreGuiItem.setAction(buildMenu, "_1BuildMenuList");
			guiInventory.setItem(9, buildMenu);
			//TODO Put this in BuildMenuList \/ \/ \/ \/ \/
//			ItemStack upgradeMenu = LoreGuiItem.build("Town Upgrades", ItemManager.getId(Material.ARMOR_STAND), 0, CivColor.Gold+"<Click to View>");
//			upgradeMenu = LoreGuiItem.setAction(upgradeMenu, "BuildUpgradeList");
//			guiInventory.setItem(20, upgradeMenu);
			
			ItemStack perkMenu = LoreGuiItem.build("Perk Menu", ItemManager.getId(Material.BOOK_AND_QUILL), 0, CivColor.Gold+"<Click to View>");
			perkMenu = LoreGuiItem.setAction(perkMenu, "ShowPerkPage");
			guiInventory.setItem(10, perkMenu);
			
			ItemStack techMenu = LoreGuiItem.build("Research Technology", ItemManager.getId(Material.POTION), 8267, CivColor.Gold+"<Click to View>");
			techMenu = LoreGuiItem.setAction(techMenu, "_2BuildTechnologyList");
			guiInventory.setItem(18, techMenu);
			
			ItemStack turorialMenu = LoreGuiItem.build("In-game Wiki", ItemManager.getId(Material.RED_ROSE), 1, CivColor.Gold+"<Click to View>", "wiki_tag000_COMING_SOON");
//			turorialMenu = LoreGuiItem.setAction(turorialMenu, "BuildTutorialMenu");
			guiInventory.setItem(26, turorialMenu);
			
	/*
			ItemStack civTutorial = LoreGuiItem.build("Civilizations", ItemManager.getId(Material.BOOK), 0, CivColor.Gold+"<Click to View>", "civ_tag001_COMING_SOON");
//			civTutorial = LoreGuiItem.setAction(civTutorial, "OpenInventory");
//			civTutorial = LoreGuiItem.setActionData(civTutorial, "invType", "showCivilizationTutorialInventory");
//			researchTutorial = LoreGuiItem.setActionData(researchTutorial, "invType", "showResearchTutorialInventory");
//			warTutorial = LoreGuiItem.setActionData(warTutorial, "invType", "showWarTutorialInventory");
			guiInventory.setItem(20, civTutorial);
			
			ItemStack townTutorial = LoreGuiItem.build("Towns", ItemManager.getId(Material.BOOK), 0, CivColor.Gold+"<Click to View>", "town_tag000_COMING_SOON");
//			townTutorial = LoreGuiItem.setAction(townTutorial, "OpenInventory");
//			townTutorial = LoreGuiItem.setActionData(townTutorial, "invType", "showTownTutorialInventory");
//			buildingTutorial = LoreGuiItem.setActionData(buildingTutorial, "invType", "showBuildingTutorialInventory");
//			randomEventTutorial = LoreGuiItem.setActionData(randomEventTutorial, "invType", "showBuildingTutorialInventory");
			guiInventory.setItem(21, townTutorial);
			
			ItemStack mobTutorial = LoreGuiItem.build("Custom Mobs", ItemManager.getId(Material.MONSTER_EGG), 0, CivColor.Gold+"<Click to View>", "entity_tag000_COMING_SOON");
//			mobTutorial = LoreGuiItem.setAction(mobTutorial, "OpenInventory");
//			mobTutorial = LoreGuiItem.setActionData(mobTutorial, "invType", "shoMobTutorialInventory");
			guiInventory.setItem(23, mobTutorial);
			
			ItemStack tradeResourcesTutorial = LoreGuiItem.build("Trade Resources", ItemManager.getId(Material.BOOK), 0, CivColor.Gold+"<Click to View>", "tr_tag000_COMING_SOON");
//			tradeResourcesTutorial = LoreGuiItem.setAction(tradeResourcesTutorial, "TradeResourceList");
			guiInventory.setItem(24, tradeResourcesTutorial);
	 */
			
/*			if (player.isOp()) {
				ItemStack adminMenu = LoreGuiItem.build("Admin Panel", ItemManager.getId(Material.COMMAND), 8194, CivColor.Gold+"<Click to View>", 
						CivColor.LightGray+CivColor.ITALIC+"You have this because you are",
						CivColor.LightGray+CivColor.ITALIC+"OP, or have admin permissions.");
				craftRec = LoreGuiItem.setActionData(craftRec, "invType", "AdminGUICommand.openAdminGUI(player)");
				guiInventory.setItem((9*1)-1, adminMenu);
			}*/
			
			/* Add Information Item */
//			ItemStack info = LoreGuiItem.build("Information", ItemManager.getId(Material.SHIELD), 0,
//					CivColor.White+"This GUI Allows for players to choose",
//					CivColor.White+"differnt values of the game to learn,",
//					CivColor.White+"how to play, or other functions for a",
//					CivColor.White+"civilization or town."
//					);
//			guiInventory.setItem((9*3)-2, info);
			
			LoreGuiItemListener.guiInventories.put(guiInventory.getName(), guiInventory);
		}
		player.openInventory(guiInventory);
	}
}
