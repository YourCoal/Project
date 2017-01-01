package com.avrgaming.civcraft.structure;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.avrgaming.civcraft.components.NonMemberFeeComponent;
import com.avrgaming.civcraft.components.SignSelectionActionInterface;
import com.avrgaming.civcraft.components.SignSelectionComponent;
import com.avrgaming.civcraft.config.CivSettings;
import com.avrgaming.civcraft.config.ConfigStableHorse;
import com.avrgaming.civcraft.config.ConfigStableItem;
import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.main.CivLog;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.object.StructureSign;
import com.avrgaming.civcraft.object.Town;
import com.avrgaming.civcraft.util.BlockCoord;
import com.avrgaming.civcraft.util.CivColor;
import com.avrgaming.civcraft.util.ItemManager;
import com.avrgaming.civcraft.util.SimpleBlock;

import gpl.LlamaModifier;
import gpl.LlamaModifier.LlamaType;
import gpl.LlamaModifier.LlamaVariant;

public class LlamaPen extends Structure {
	
	private HashMap<Integer, SignSelectionComponent> signSelectors = new HashMap<Integer, SignSelectionComponent>();
	private BlockCoord llamaSpawnCoord;
	private BlockCoord muleSpawnCoord;
	private NonMemberFeeComponent nonMemberFeeComponent;
	
	public LlamaPen(ResultSet rs) throws SQLException, CivException {
		super(rs);
		nonMemberFeeComponent = new NonMemberFeeComponent(this);
		nonMemberFeeComponent.onLoad();
	}

	protected LlamaPen(Location center, String id, Town town) throws CivException {
		super(center, id, town);
		nonMemberFeeComponent = new NonMemberFeeComponent(this);
		nonMemberFeeComponent.onSave();
	}
	
	public void loadSettings() {
		super.loadSettings();

		SignSelectionComponent llamaVender = new SignSelectionComponent();
		SignSelectionComponent muleVender = new SignSelectionComponent();
		SignSelectionComponent itemVender = new SignSelectionComponent();
		
		signSelectors.put(0, llamaVender);
		signSelectors.put(1, muleVender);
		signSelectors.put(2, itemVender);

		class buyLlamaAction implements SignSelectionActionInterface {
			int llama_id;
			double cost;
			
			public buyLlamaAction(int llama_id, double cost) {
				this.llama_id = llama_id;
				this.cost = cost;
			}
			
			@Override
			public void process(Player player) {
				ConfigStableHorse llama = CivSettings.horses.get(llama_id);
				if (llama == null) {
					CivMessage.sendError(player, "Unknown llama ID.");
					return;
				}
				
				Resident resident = CivGlobal.getResident(player);
			
				double paid;
				if (resident.getTown() != getTown()) {
					if (!resident.getTreasury().hasEnough(getItemCost(cost))) {
						CivMessage.sendError(player, "You do not have the required "+getItemCost(cost)+" coins.");
						return;
					}
					
					
					resident.getTreasury().withdraw(getItemCost(cost));
					getTown().depositTaxed(getFeeToTown(cost));
					CivMessage.send(player, CivColor.Yellow+"Paid "+getFeeToTown(cost)+" in non-resident fees.");
					paid = getItemCost(cost);
				} else {
					if (!resident.getTreasury().hasEnough(cost)) {
						CivMessage.sendError(player, "You do not have the required "+cost+" coins.");
						return;
					}

					resident.getTreasury().withdraw(cost);
					paid = cost;
				}	

				LlamaModifier mod;
				if (!llama.mule) {
					mod = LlamaModifier.spawn(llamaSpawnCoord.getLocation());
					mod.setType(LlamaType.NORMAL);
				} else {
					mod = LlamaModifier.spawn(muleSpawnCoord.getLocation());
					mod.setType(LlamaType.OTHER);
				}
				
				mod.setVariant(LlamaVariant.valueOf(llama.variant));
				LlamaModifier.setLlamaSpeed(mod.getLlama(), llama.speed);
				((Llama)mod.getLlama()).setJumpStrength(llama.jump);
//				((Llama)mod.getLlama()).setMaxHealth(llama.health);
				((Llama)mod.getLlama()).setHealth(llama.health);
				((Llama)mod.getLlama()).setOwner(player);
				((Llama)mod.getLlama()).setBaby();
				
				CivMessage.send(player, CivColor.LightGreen+"Paid "+paid+" coins.");
			}
		}
		
		class buyItemAction implements SignSelectionActionInterface {

			int item_id;
			double cost;
			
			public buyItemAction(int item_id, double cost) {
				this.item_id = item_id;
				this.cost = cost;
			}
			
			@Override
			public void process(Player player) {

				Resident resident = CivGlobal.getResident(player);
				
				double paid;
				if (resident.getTown() != getTown()) {
					if (!resident.getTreasury().hasEnough(getItemCost(cost))) {
						CivMessage.sendError(player, "You do not have the required "+getItemCost(cost)+" coins.");
						return;
					}
					
					resident.getTreasury().withdraw(getItemCost(cost));
					CivMessage.send(player, CivColor.Yellow+"Paid "+getFeeToTown(cost)+" in non-resident fees.");
					paid = getItemCost(cost);
				} else {
					if (!resident.getTreasury().hasEnough(cost)) {
						CivMessage.sendError(player, "You do not have the required "+cost+" coins.");
						return;
					}
					
					resident.getTreasury().withdraw(cost);
					paid = cost;
				}

				HashMap<Integer, ItemStack> leftovers = player.getInventory().addItem(ItemManager.createItemStack(item_id, 1));
				if (leftovers.size() > 0) {
					for (ItemStack stack : leftovers.values()) {
						player.getWorld().dropItem(player.getLocation(), stack);
					}
				}
				
				CivMessage.send(player, CivColor.LightGreen+"Paid "+paid+" coins.");
			}
			
		}
		
		for (ConfigStableItem item : CivSettings.stableItems) {
			SignSelectionComponent comp = signSelectors.get(item.store_id);
			if (comp == null) {
				continue;
			}
			if (item.item_id == 0) {
				comp.addItem(new String[] {CivColor.LightGreen+item.name, "Buy For", ""+item.cost, "Fee:"+this.nonMemberFeeComponent.getFeeString()}, new buyLlamaAction(item.horse_id, item.cost));
			} else {
				comp.addItem(new String[] {CivColor.LightGreen+item.name, "Buy For", ""+item.cost, "Fee: "+this.nonMemberFeeComponent.getFeeString()}, new buyItemAction(item.item_id, item.cost));			
			}
		}
	}
	
	private double getItemCost(double cost) {
		return cost + getFeeToTown(cost);
	}
	
	private double getFeeToTown(double cost) {
		return cost*this.nonMemberFeeComponent.getFeeRate();
	}
	
	@Override
	public void processSignAction(Player player, StructureSign sign, PlayerInteractEvent event) {
		SignSelectionComponent signSelection = signSelectors.get(Integer.valueOf(sign.getAction()));
		if (signSelection == null) {
			CivLog.warning("No sign seletor component for with id:"+sign.getAction());
			return;
		}

		switch (sign.getType()) {
		case "prev":
			signSelection.processPrev();
			break;
		case "next":
			signSelection.processNext();
			break;
		case "item":
			signSelection.processAction(player);
			break;
		}
	}
	
	@Override
	public void updateSignText() {
		for (SignSelectionComponent comp : signSelectors.values()) {
			comp.setMessageAllItems(3, "Fee: "+this.nonMemberFeeComponent.getFeeString());
		}
	}
	
	@Override
	public void onPostBuild(BlockCoord absCoord, SimpleBlock sb) {
		StructureSign structSign;
		int selectorIndex;
		SignSelectionComponent signComp;
		
		switch (sb.command) {
		case "/prev":
			ItemManager.setTypeId(absCoord.getBlock(), sb.getType());
			ItemManager.setData(absCoord.getBlock(), sb.getData());
			structSign = new StructureSign(absCoord, this);
			structSign.setText("\n"+ChatColor.BOLD+ChatColor.UNDERLINE+"Prev");
			structSign.setDirection(sb.getData());
			structSign.setAction(sb.keyvalues.get("id"));
			structSign.setType("prev");
			structSign.update();
			this.addStructureSign(structSign);
			CivGlobal.addStructureSign(structSign);			
			break;
		case "/item":
			ItemManager.setTypeId(absCoord.getBlock(), sb.getType());
			ItemManager.setData(absCoord.getBlock(), sb.getData());

			structSign = new StructureSign(absCoord, this);
			structSign.setText("");
			structSign.setDirection(sb.getData());
			structSign.setAction(sb.keyvalues.get("id"));
			structSign.setType("item");
			structSign.update();
						
			this.addStructureSign(structSign);
			CivGlobal.addStructureSign(structSign);
						
			selectorIndex = Integer.valueOf(sb.keyvalues.get("id"));
			signComp = signSelectors.get(selectorIndex);
			if (signComp != null) {
				signComp.setActionSignCoord(absCoord);
				signComp.updateActionSign();
			} else {
				CivLog.warning("No sign selector found for id:"+selectorIndex);
			}
			
			break;
		case "/next":
			ItemManager.setTypeId(absCoord.getBlock(), sb.getType());
			ItemManager.setData(absCoord.getBlock(), sb.getData());

			structSign = new StructureSign(absCoord, this);
			structSign.setText("\n"+ChatColor.BOLD+ChatColor.UNDERLINE+"Next");
			structSign.setDirection(sb.getData());
			structSign.setType("next");
			structSign.setAction(sb.keyvalues.get("id"));
			structSign.update();
			this.addStructureSign(structSign);
			CivGlobal.addStructureSign(structSign);
			
			break;
		case "/horsespawn":
			this.llamaSpawnCoord = absCoord;
			break;
		case "/mulespawn":
			this.muleSpawnCoord = absCoord;
			break;
		}
	}

	public void setNonResidentFee(double d) {
		this.nonMemberFeeComponent.setFeeRate(d);
	}	

}
