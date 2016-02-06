package com.avrgaming.civcraft.structure;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.inventory.ItemStack;

import com.avrgaming.civcraft.components.AttributeBiomeRadiusPerLevel;
import com.avrgaming.civcraft.components.TradeLevelComponent;
import com.avrgaming.civcraft.components.TradeLevelComponent.Result;
import com.avrgaming.civcraft.components.TradeShipResults;
import com.avrgaming.civcraft.config.CivSettings;
import com.avrgaming.civcraft.config.ConfigMineLevel;
import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.lorestorage.LoreMaterial;
import com.avrgaming.civcraft.main.CivData;
import com.avrgaming.civcraft.main.CivLog;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.Buff;
import com.avrgaming.civcraft.object.Town;
import com.avrgaming.civcraft.template.Template;
import com.avrgaming.civcraft.threading.CivAsyncTask;
import com.avrgaming.civcraft.threading.TaskMaster;
import com.avrgaming.civcraft.util.BlockCoord;
import com.avrgaming.civcraft.util.CivColor;
import com.avrgaming.civcraft.util.ItemManager;
import com.avrgaming.civcraft.util.MultiInventory;
import com.avrgaming.civcraft.util.SimpleBlock;
import com.avrgaming.civcraft.util.TimeTools;

public class TradeShip extends Structure {
	
	private int upgradeLevel = 1;
	private int tickLevel = 1;
	
	public HashSet<BlockCoord> goodsDepositPoints = new HashSet<BlockCoord>();
	public HashSet<BlockCoord> goodsWithdrawPoints = new HashSet<BlockCoord>();
	
	private TradeLevelComponent consumeComp = null;
	
	protected TradeShip(Location center, String id, Town town) throws CivException {
		super(center, id, town);
	}
	
	public TradeShip(ResultSet rs) throws SQLException, CivException {
		super(rs);
	}
	
	@Override
	public void loadSettings() {
		super.loadSettings();
	}
	
	public String getkey() {
		return getTown().getName()+"_"+this.getConfigId()+"_"+this.getCorner().toString(); 
	}
	
	@Override
	public String getDynmapDescription() {
		return null;
	}
	
	@Override
	public String getMarkerIconName() {
		return "anchor";
	}
	
	public TradeLevelComponent getConsumeComponent() {
		if (consumeComp == null) {
			consumeComp = (TradeLevelComponent) this.getComponent(TradeLevelComponent.class.getSimpleName());
		} return consumeComp;
	}
	
	@Override 
	public void updateSignText() {
		reprocessCommandSigns();
	}
	
	public void reprocessCommandSigns() {
		Template tpl;
		try {
			tpl = Template.getTemplate(this.getSavedTemplatePath(), null);
		} catch (IOException | CivException e) {
			e.printStackTrace();
			return;
		}
		class SyncTask implements Runnable {
			Template template;
			BlockCoord structCorner;
			
			public SyncTask(Template template, BlockCoord structCorner) {
				this.template = template;
				this.structCorner = structCorner;
			}
			
			@Override
			public void run() {
				processCommandSigns(template, structCorner);
			}
		}
		TaskMaster.syncTask(new SyncTask(tpl, corner), TimeTools.toTicks(3));
	}
	
	private void processCommandSigns(Template tpl, BlockCoord corner) {
		for (BlockCoord relativeCoord : tpl.commandBlockRelativeLocations) {
			SimpleBlock sb = tpl.blocks[relativeCoord.getX()][relativeCoord.getY()][relativeCoord.getZ()];
			BlockCoord absCoord = new BlockCoord(corner.getBlock().getRelative(relativeCoord.getX(), relativeCoord.getY(), relativeCoord.getZ()));
			switch (sb.command) {
			case "/incoming":
				Integer inID = Integer.valueOf(sb.keyvalues.get("id"));
				if (this.getUpgradeLvl() >= inID+1) {
					this.goodsWithdrawPoints.add(absCoord);
					ItemManager.setTypeId(absCoord.getBlock(), ItemManager.getId(Material.CHEST));
					byte data3 = CivData.convertSignDataToChestData((byte)sb.getData());
					ItemManager.setData(absCoord.getBlock(), data3);
				} else {
					ItemManager.setTypeId(absCoord.getBlock(), ItemManager.getId(Material.AIR));
					ItemManager.setData(absCoord.getBlock(), sb.getData());
				}
				this.addStructureBlock(absCoord, false);
				break;
			case "/outgoing":
				Integer outID = Integer.valueOf(sb.keyvalues.get("id"));
				if (this.getLevel() >= outID+1) {
					this.goodsDepositPoints.add(absCoord);
					ItemManager.setTypeId(absCoord.getBlock(), ItemManager.getId(Material.CHEST));
					byte data3 = CivData.convertSignDataToChestData((byte)sb.getData());
					ItemManager.setData(absCoord.getBlock(), data3);
				} else {
					ItemManager.setTypeId(absCoord.getBlock(), ItemManager.getId(Material.AIR));
					ItemManager.setData(absCoord.getBlock(), sb.getData());
				}
				this.addStructureBlock(absCoord, false);
				break;
			default:
				/* Unrecognized command... treat as a literal sign. */
				ItemManager.setTypeId(absCoord.getBlock(), ItemManager.getId(Material.WALL_SIGN));
				ItemManager.setData(absCoord.getBlock(), sb.getData());
				Sign sign = (Sign)absCoord.getBlock().getState();
				sign.setLine(0, sb.message[0]);
				sign.setLine(1, sb.message[1]);
				sign.setLine(2, sb.message[2]);
				sign.setLine(3, sb.message[3]);
				sign.update();
				break;
			}
		}
	}
	
	public TradeShipResults consume(CivAsyncTask task) throws InterruptedException {
		TradeShipResults tradeResult;
		if (this.goodsDepositPoints.size() == 0 || this.goodsWithdrawPoints.size() == 0) {
			tradeResult = new TradeShipResults();
			tradeResult.setResult(Result.STAGNATE);
			return tradeResult;
		}
		MultiInventory mInv = new MultiInventory();
		for (BlockCoord bcoord : this.goodsDepositPoints) {
			Block b = bcoord.getBlock();
			if (b.getState() instanceof Chest) {
				try {
				mInv.addInventory(((Chest)b.getState()).getInventory());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if (mInv.getInventoryCount() == 0) {
			tradeResult = new TradeShipResults();
			tradeResult.setResult(Result.STAGNATE);
			return tradeResult;
		}
		getConsumeComponent().setSource(mInv);
		getConsumeComponent().setConsumeRate(1.0);
		tradeResult = getConsumeComponent().processConsumption();
		getConsumeComponent().onSave();		
		return tradeResult;
	}
	
	public void process_trade_ship(CivAsyncTask task) throws InterruptedException {	
		TradeShipResults tradeResult = this.consume(task);
		CivLog.debug("moneyEarned: " + tradeResult.getMoney());
		CivLog.debug("countConsumed: " + tradeResult.getConsumed());
		CivLog.debug("cultureEarned: " + tradeResult.getCulture());
		Result result = tradeResult.getResult();
		switch (result) {
		case STAGNATE:
			CivMessage.sendTown(getTown(), CivColor.Rose+"A level "+getConsumeComponent().getLevel()+"Trade Ship's production stagnated. "+CivColor.LightGreen+getConsumeComponent().getCountString());
			break;
		case GROW:
			CivMessage.sendTown(getTown(), CivColor.Rose+"A level "+getConsumeComponent().getLevel()+" Trade Ship's production rose. "+getConsumeComponent().getCountString());
			break;
		case LEVELUP:
			CivMessage.sendTown(getTown(), CivColor.LightGreen+"A Trade Ship gained a level. It is now level "+getConsumeComponent().getLevel());
			this.reprocessCommandSigns();
			break;
		case MAXED:
			CivMessage.sendTown(getTown(), CivColor.Rose+"A level "+getConsumeComponent().getLevel()+" Trade Ship is maxed. "+CivColor.LightGreen+getConsumeComponent().getCountString());
			break;
		default:
			break;
		}
		if (tradeResult.getConsumed() >= 1) {
			CivMessage.sendTown(getTown(), CivColor.LightGreen+"Your Trade Ship generated "+tradeResult.getMoney()+" coins and "+tradeResult.getCulture()+" culture from selling "+
		tradeResult.getConsumed()+" units of cargo.");
		}
		if (tradeResult.getCulture() >= 1) {
			int total_culture = (int)Math.round(tradeResult.getCulture()*this.getTown().getCottageRate());
			this.getTown().addAccumulatedCulture(total_culture);
			this.getTown().save();
		}
		if (tradeResult.getReturnCargo().size() >= 1) {
			MultiInventory multiInv = new MultiInventory();
			for (BlockCoord bcoord : this.goodsWithdrawPoints) {
				Block b = bcoord.getBlock();
				if (b.getState() instanceof Chest) {
					try {
						multiInv.addInventory(((Chest)b.getState()).getInventory());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			for (HashMap<String, String> item :tradeResult.getReturnCargo()) {
				ItemStack newItem = LoreMaterial.spawn(LoreMaterial.materialMap.get(item.get("name")),Integer.parseInt(item.get("quantity")));
				multiInv.addItem(newItem);
			}
			CivMessage.sendTown(getTown(), CivColor.LightGreen+"It also brought back something special! Check your Trade Ship's incoming cargo hold.");
		}
	}
	
	public void onPostBuild(BlockCoord absCoord, SimpleBlock commandBlock) {
		this.upgradeLevel = getTown().saved_tradeship_upgrade_levels;
		this.reprocessCommandSigns();
	}
	
	public int getUpgradeLvl() {
		return upgradeLevel;
	}
	
	public void setUpgradeLvl(int level) {
		this.upgradeLevel = level;
		this.reprocessCommandSigns();
	}
	
	public int getLevel() {
		try {
			return this.getConsumeComponent().getLevel();
		} catch (Exception e) {
			return tickLevel;
		}
	}
	
	public double getHammersPerTile() {
		AttributeBiomeRadiusPerLevel attrBiome = (AttributeBiomeRadiusPerLevel)this.getComponent("AttributeBiomeRadiusPerLevel");
		double base = attrBiome.getBaseValue();
		double rate = 1;
		rate += this.getTown().getBuffManager().getEffectiveDouble(Buff.ADVANCED_TOOLING);
		return (rate*base);
	}
	
	public int getCount() {
		return this.getConsumeComponent().getCount();
	}
	
	public int getMaxCount() {
		int level = getLevel();
		ConfigMineLevel lvl = CivSettings.mineLevels.get(level);
		return lvl.count;	
	}
	
	public Result getLastResult() {
		return this.getConsumeComponent().getLastResult();
	}
	
	@Override
	public void delete() throws SQLException {
		super.delete();
		if (getConsumeComponent() != null) {
			getConsumeComponent().onDelete();
		}
	}
	
	public void onDestroy() {
		super.onDestroy();
		getConsumeComponent().setLevel(1);
		getConsumeComponent().setCount(0);
		getConsumeComponent().onSave();
	}
}