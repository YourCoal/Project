/*************************************************************************
 * 
 * AVRGAMING LLC
 * __________________
 * 
 *  [2013] AVRGAMING LLC
 *  All Rights Reserved.
 * 
 * NOTICE:  All information contained herein is, and remains
 * the property of AVRGAMING LLC and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to AVRGAMING LLC
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from AVRGAMING LLC.
 */
package com.avrgaming.civcraft.structure;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.avrgaming.civcraft.components.NonMemberFeeComponent;
import com.avrgaming.civcraft.config.CivSettings;
import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.lorestorage.LoreMaterial;
import com.avrgaming.civcraft.main.CivData;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.Buff;
import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.object.StructureSign;
import com.avrgaming.civcraft.object.Town;
import com.avrgaming.civcraft.util.BlockCoord;
import com.avrgaming.civcraft.util.CivColor;
import com.avrgaming.civcraft.util.SimpleBlock;

public class Bank extends Structure {
	
	private int level = 1;
	private double interestRate = 0;
	
	private NonMemberFeeComponent nonMemberFeeComponent;
	
	
	private static final int COAL_SIGN = 0;
	private static final int IRON_SIGN = 1;
	private static final int GOLD_SIGN = 2;
	private static final int DIAMOND_SIGN = 3;
	private static final int EMERALD_SIGN = 4;
	private static final int QUARTZ_SIGN = 5;
	//private static final int LAPIS_SIGN = 6;
	private static final int REDSTONE_SIGN = 7;
	//
	private static final int COAL_BLOCK_SIGN = 8;
	private static final int IRON_BLOCK_SIGN = 9;
	private static final int GOLD_BLOCK_SIGN = 10;
	private static final int DIAMOND_BLOCK_SIGN = 11;
	private static final int EMERALD_BLOCK_SIGN = 12;
	private static final int QUARTZ_BLOCK_SIGN = 13;
	private static final int LAPIS_BLOCK_SIGN = 14;
	private static final int REDSTONE_BLOCK_SIGN = 15;
	
	
	protected Bank(Location center, String id, Town town) throws CivException {
		super(center, id, town);
		nonMemberFeeComponent = new NonMemberFeeComponent(this);
		nonMemberFeeComponent.onSave();
		setLevel(town.saved_bank_level);
		setInterestRate(town.saved_bank_interest_amount);
	}
	
	public Bank(ResultSet rs) throws SQLException, CivException {
		super(rs);
		nonMemberFeeComponent = new NonMemberFeeComponent(this);
		nonMemberFeeComponent.onLoad();
	}

	public double getBankExchangeRate() {
		double exchange_rate = 0.0;
		switch (level) {
		case 1:
			exchange_rate = 0.4;
			break;
		case 2:
			exchange_rate = 0.5;
			break;
		case 3:
			exchange_rate = 0.6;
			break;
		case 4:
			exchange_rate = 0.7;
			break;
		case 5:
			exchange_rate = 0.8;
			break;
		case 6:
			exchange_rate = 0.9;
			break;
		case 7:
			exchange_rate = 1;
			break;
		case 8:
			exchange_rate = 1.25;
			break;
		case 9:
			exchange_rate = 1.5;
			break;
		case 10:
			exchange_rate = 2;
			break;
		}
		
		double rate = 1;
		double addtional = rate*this.getTown().getBuffManager().getEffectiveDouble(Buff.BARTER);
		rate += addtional;
		if (rate > 1) {
			exchange_rate *= rate;
		}
		return exchange_rate;
	}
	
	@Override
	public void onBonusGoodieUpdate() {
		this.updateSignText();
	}
	
	private String getExchangeRateString() {
		return ((int)(getBankExchangeRate()*100) + "%").toString();		
	}
	
	private String getNonResidentFeeString() {
		return CivSettings.localize.localizedString("bank_sign_fee")+" "+((int)(this.nonMemberFeeComponent.getFeeRate()*100) + "%").toString();		
	}
	
	private String getSignItemPrice(int signId) {
		double itemPrice;
		if (signId == COAL_SIGN) {
			itemPrice = CivSettings.coal_rate;
		} else if (signId == COAL_BLOCK_SIGN) {
			itemPrice = CivSettings.coal_rate*9;
		} else if (signId == IRON_SIGN) {
			itemPrice = CivSettings.iron_rate;
		} else if (signId == IRON_BLOCK_SIGN) {
			itemPrice = CivSettings.iron_rate*9;
		} else if (signId == GOLD_SIGN) {
			itemPrice = CivSettings.gold_rate;
		} else if (signId == GOLD_BLOCK_SIGN) {
			itemPrice = CivSettings.gold_rate*9;
		} else if (signId == DIAMOND_SIGN) {
			itemPrice = CivSettings.diamond_rate;
		} else if (signId == DIAMOND_BLOCK_SIGN) {
			itemPrice = CivSettings.diamond_rate*9;
		} else if (signId == EMERALD_SIGN) {
			itemPrice = CivSettings.emerald_rate;
		} else if (signId == EMERALD_BLOCK_SIGN) {
			itemPrice = CivSettings.emerald_rate*9;
		} else if (signId == QUARTZ_SIGN) {
			itemPrice = CivSettings.quartz_rate;
		} else if (signId == QUARTZ_BLOCK_SIGN) {
			itemPrice = CivSettings.quartz_rate*4; // <--- 4 quartz = block, not 9.
		//} else if (signId == LAPIS_SIGN) {
		//	itemPrice = CivSettings.lapis_rate;
		} else {
			itemPrice = CivSettings.redstone_rate*9;
		}
		
		String out = "1 = ";
		out += (int)(itemPrice*getBankExchangeRate());
		out += " Coins";
		return out;
	}
	
	public void exchange_for_coins(Resident resident, int itemId, int itemData, double coins) throws CivException {
		double exchange_rate = 0.0;
		String itemName;
		Player player = CivGlobal.getPlayer(resident);
		
		if (itemId == CivData.COAL) {
			itemName = "Coal";
		} else if (itemId == CivData.COAL_BLOCK) {
			itemName = "Coal Block";
		} else if (itemId == CivData.IRON_INGOT) {
			itemName = "Iron";
		} else if (itemId == CivData.IRON_BLOCK) {
			itemName = "Iron Block";
		} else if (itemId == CivData.GOLD_INGOT) {
			itemName = "Gold";
		} else if (itemId == CivData.GOLD_BLOCK) {
			itemName = "Gold Block";
		} else if (itemId == CivData.DIAMOND) {
			itemName = "Diamond";
		} else if (itemId == CivData.DIAMOND_BLOCK) {
			itemName = "Diamond Block";
		} else if (itemId == CivData.EMERALD) {
			itemName = "Emerald";
		} else if (itemId == CivData.EMERALD_BLOCK) {
			itemName = "Emerald Block";
		} else if (itemId == CivData.QUARTZ) {
			itemName = "Quartz";
		} else if (itemId == CivData.QUARTZ_BLOCK) {
			itemName = "Quartz Block";
		} else if (itemId == CivData.DYE && itemData == 4) {
			itemName = "Lapis";
		} else if (itemId == CivData.LAPIS_BLOCK) {
			itemName = "Lapis Block";
		} else if (itemId == CivData.REDSTONE_DUST) {
			itemName = "Redstone";
		} else if (itemId == CivData.REDSTONE_BLOCK) {
			itemName = "Redstone Block";
		} else {
			itemName = "UNKNOWN";
		}
		
		exchange_rate = getBankExchangeRate();
		int count = resident.takeItemsInHand(itemId, itemData);
		if (count == 0) {
			throw new CivException(CivSettings.localize.localizedString("var_bank_notEnoughInHand",itemName));
		}
		
		Town usersTown = resident.getTown();
		
		// Resident is in his own town.
		if (usersTown == this.getTown()) {		
			DecimalFormat df = new DecimalFormat();
			resident.getTreasury().deposit((double)((int)((coins*count)*exchange_rate)));
			CivMessage.send(player,
					CivColor.LightGreen + CivSettings.localize.localizedString("var_bank_exchanged",count,itemName,(df.format((coins*count)*exchange_rate)),CivSettings.CURRENCY_NAME));	
			return;
		}
		
		// non-resident must pay the town's non-resident tax
		double giveToPlayer = (double)((int)((coins*count)*exchange_rate));
		double giveToTown = (double)((int)giveToPlayer*this.getNonResidentFee());
		giveToPlayer -= giveToTown;
		
		giveToTown = Math.round(giveToTown);
		giveToPlayer = Math.round(giveToPlayer);
		
			this.getTown().depositDirect(giveToTown);
			resident.getTreasury().deposit(giveToPlayer);
		
		CivMessage.send(player, CivColor.LightGreen + CivSettings.localize.localizedString("var_bank_exchanged",count,itemName,giveToPlayer,CivSettings.CURRENCY_NAME));
		CivMessage.send(player,CivColor.Yellow+" "+CivSettings.localize.localizedString("var_taxes_paid",giveToTown,CivSettings.CURRENCY_NAME));
		return;
		
	}
	
	@Override
	public void processSignAction(Player player, StructureSign sign, PlayerInteractEvent event) {
		//int special_id = Integer.valueOf(sign.getAction());
		Resident resident = CivGlobal.getResident(player);
		
		if (resident == null) {
			return;
		}
		
		try {
			
			if (LoreMaterial.isCustom(player.getItemInHand())) {
				throw new CivException(CivSettings.localize.localizedString("bank_invalidItem"));
			}
			
			switch (sign.getAction()) {
			case "coal":
				exchange_for_coins(resident, CivData.COAL, 0, CivSettings.coal_rate);
				break;
			case "iron":
				exchange_for_coins(resident, CivData.IRON_INGOT, 0, CivSettings.iron_rate);
				break;
			case "gold":
				exchange_for_coins(resident, CivData.GOLD_INGOT, 0, CivSettings.gold_rate);
				break;
			case "diamond":
				exchange_for_coins(resident, CivData.DIAMOND, 0, CivSettings.diamond_rate);
				break;
			case "emerald":
				exchange_for_coins(resident, CivData.EMERALD, 0, CivSettings.emerald_rate);
				break;
			case "quartz":
				exchange_for_coins(resident, CivData.QUARTZ, 0, CivSettings.quartz_rate);
				break;
			//case "lapis":
			//	exchange_for_coins(resident, CivData.DYE, 4, CivSettings.lapis_rate);
			//	break;
			case "redstone":
				exchange_for_coins(resident, CivData.REDSTONE_DUST, 0, CivSettings.redstone_rate);
				break;
				
				
			case "coalb":
				exchange_for_coins(resident, CivData.COAL_BLOCK, 0, CivSettings.coal_rate*9);
				break;
			case "ironb":
				exchange_for_coins(resident, CivData.IRON_BLOCK, 0, CivSettings.iron_rate*9);
				break;
			case "goldb":
				exchange_for_coins(resident, CivData.GOLD_BLOCK, 0, CivSettings.gold_rate*9);
				break;
			case "diamondb":
				exchange_for_coins(resident, CivData.DIAMOND_BLOCK, 0, CivSettings.diamond_rate*9);
				break;
			case "emeraldb":
				exchange_for_coins(resident, CivData.EMERALD_BLOCK, 0, CivSettings.emerald_rate*9);
				break;
			case "quartzb":
				exchange_for_coins(resident, CivData.QUARTZ_BLOCK, 0, CivSettings.quartz_rate*9);
				break;
			case "lapisb":
				exchange_for_coins(resident, CivData.LAPIS_BLOCK, 0, CivSettings.lapis_rate*9);
				break;
			case "redstoneb":
				exchange_for_coins(resident, CivData.REDSTONE_BLOCK, 0, CivSettings.redstone_rate*9);
				break;
			}
		} catch (CivException e) {
			CivMessage.send(player, CivColor.Rose+e.getMessage());
		}
	}
	
	@Override
	public void updateSignText() {
		for (StructureSign sign : getSigns()) {
			switch (sign.getAction().toLowerCase()) {
			case "coal":
				sign.setText("Coal\n"+
					"At "+getExchangeRateString()+"\n"+
					getSignItemPrice(COAL_SIGN)+"\n"+
					getNonResidentFeeString());
				break;
			case "iron":
				sign.setText("Iron\n"+
					"At "+getExchangeRateString()+"\n"+
					getSignItemPrice(IRON_SIGN)+"\n"+
					getNonResidentFeeString());
				break;
			case "gold":
				sign.setText("Gold\n"+
					"At "+getExchangeRateString()+"\n"+
					getSignItemPrice(GOLD_SIGN)+"\n"+
					getNonResidentFeeString());
				break;
			case "diamond":
				sign.setText("Diamond\n"+
					"At "+getExchangeRateString()+"\n"+
					getSignItemPrice(DIAMOND_SIGN)+"\n"+
					getNonResidentFeeString());
				break;			
			case "emerald":
				sign.setText("Emerald\n"+
					"At "+getExchangeRateString()+"\n"+
					getSignItemPrice(EMERALD_SIGN)+"\n"+
					getNonResidentFeeString());
					break;
			case "quartz":
				sign.setText("Quartz\n"+
					"At "+getExchangeRateString()+"\n"+
					getSignItemPrice(QUARTZ_SIGN)+"\n"+
					getNonResidentFeeString());
					break;
/*			case "lapis":
				sign.setText("Lapis\n"+
					"At "+getExchangeRateString()+"\n"+
					getSignItemPrice(LAPIS_SIGN)+"\n"+
					getNonResidentFeeString());
					break;*/
			case "lapis":
				sign.setText("Lapis\n"+
					"At -0 Coins\n"+
					"Not Sellable\n"+
					"Due to Bug");
					break;
			case "redstone":
				sign.setText("Redstone\n"+
					"At "+getExchangeRateString()+"\n"+
					getSignItemPrice(REDSTONE_SIGN)+"\n"+
					getNonResidentFeeString());
					break;
			//
			case "coalb":
				sign.setText("Coal Block\n"+
					"At "+getExchangeRateString()+"\n"+
					getSignItemPrice(COAL_BLOCK_SIGN)+"\n"+
					getNonResidentFeeString());
				break;
			case "ironb":
				sign.setText("Iron Block\n"+
					"At "+getExchangeRateString()+"\n"+
					getSignItemPrice(IRON_BLOCK_SIGN)+"\n"+
					getNonResidentFeeString());
				break;
			case "goldb":
				sign.setText("Gold Block\n"+
					"At "+getExchangeRateString()+"\n"+
					getSignItemPrice(GOLD_BLOCK_SIGN)+"\n"+
					getNonResidentFeeString());
				break;
			case "diamondb":
				sign.setText("Diamond Block\n"+
					"At "+getExchangeRateString()+"\n"+
					getSignItemPrice(DIAMOND_BLOCK_SIGN)+"\n"+
					getNonResidentFeeString());
				break;			
			case "emeraldb":
				sign.setText("Emerald Block\n"+
					"At "+getExchangeRateString()+"\n"+
					getSignItemPrice(EMERALD_BLOCK_SIGN)+"\n"+
					getNonResidentFeeString());
				break;
			case "quartzb":
				sign.setText("Quartz Block\n"+
					"At "+getExchangeRateString()+"\n"+
					getSignItemPrice(QUARTZ_BLOCK_SIGN)+"\n"+
					getNonResidentFeeString());
				break;
			case "lapisb":
				sign.setText("Lapis Block\n"+
					"At "+getExchangeRateString()+"\n"+
					getSignItemPrice(LAPIS_BLOCK_SIGN)+"\n"+
					getNonResidentFeeString());
				break;
			case "redstoneb":
				sign.setText("Redstone Block\n"+
					"At "+getExchangeRateString()+"\n"+
					getSignItemPrice(REDSTONE_BLOCK_SIGN)+"\n"+
					getNonResidentFeeString());
				break;
			}
			sign.update();
		}
	}

	@Override
	public String getDynmapDescription() {
		String out = "<u><b>"+CivSettings.localize.localizedString("bank_dynmapName")+"</u></b><br/>";
		out += CivSettings.localize.localizedString("Level")+" "+this.level;
		return out;
	}
	
	@Override
	public String getMarkerIconName() {
		return "bank";
	}
	
	public int getLevel() {
		return level;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	public double getNonResidentFee() {
		return this.nonMemberFeeComponent.getFeeRate();
	}
	
	public void setNonResidentFee(double nonResidentFee) {
		this.nonMemberFeeComponent.setFeeRate(nonResidentFee);
	}
	
	public double getInterestRate() {
		return interestRate;
	}
	
	public void setInterestRate(double interestRate) {
		this.interestRate = interestRate;
	}
	
	@Override
	public void onLoad() {
		/* Process the interest rate. */
		if (interestRate == 0.0) {
			this.getTown().getTreasury().setPrincipalAmount(0);
			return;
		}
		/* Update the principal with the new value. */
		this.getTown().getTreasury().setPrincipalAmount(this.getTown().getTreasury().getBalance());
	}
	
	@Override
	public void onDailyEvent() {
		/* Process the interest rate. */
		double effectiveInterestRate = interestRate;
		if (effectiveInterestRate == 0.0) {
			this.getTown().getTreasury().setPrincipalAmount(0);
			return;
		}
		
		double principal = this.getTown().getTreasury().getPrincipalAmount();
		if (this.getTown().getBuffManager().hasBuff("buff_greed")) {
			double increase = this.getTown().getBuffManager().getEffectiveDouble("buff_greed");
			effectiveInterestRate += increase;
			CivMessage.sendTown(this.getTown(), CivColor.LightGray+CivSettings.localize.localizedString("bank_greed"));
		}
		
		double newCoins = principal*effectiveInterestRate;
		//Dont allow fractional coins.
		newCoins = Math.floor(newCoins);
		if (newCoins != 0) {
			CivMessage.sendTown(this.getTown(), CivColor.LightGreen+CivSettings.localize.localizedString("var_bank_interestMsg1",newCoins,CivSettings.CURRENCY_NAME,principal));
			this.getTown().getTreasury().deposit(newCoins);
		}
		/* Update the principal with the new value. */
		this.getTown().getTreasury().setPrincipalAmount(this.getTown().getTreasury().getBalance());
	}
	
	@Override
	public void onPostBuild(BlockCoord absCoord, SimpleBlock commandBlock) {
		this.level = getTown().saved_bank_level;
		this.interestRate = getTown().saved_bank_interest_amount;
	}
	
	public NonMemberFeeComponent getNonMemberFeeComponent() {
		return nonMemberFeeComponent;
	}
	
	public void setNonMemberFeeComponent(NonMemberFeeComponent nonMemberFeeComponent) {
		this.nonMemberFeeComponent = nonMemberFeeComponent;
	}
	
	public void onGoodieFromFrame() {
		this.updateSignText();
	}
	
	public void onGoodieToFrame() {
		this.updateSignText();
	}
}
