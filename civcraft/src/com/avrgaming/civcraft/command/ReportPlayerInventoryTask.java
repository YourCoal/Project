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
package com.avrgaming.civcraft.command;


import java.util.Queue;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import com.avrgaming.civcraft.main.CivMessage;

public class ReportPlayerInventoryTask implements Runnable {
	
	Queue<OfflinePlayer> offplayers;
	CommandSender sender;
	
	public ReportPlayerInventoryTask(CommandSender sender, Queue<OfflinePlayer> offplayers) {
		this.sender = sender;
		this.offplayers = offplayers;
	}
	
/*	private int countItem(ItemStack[] stacks, int id) {
		int total = 0;
		for (ItemStack stack : stacks) {
			if (stack == null) {
				continue;
			}
			
			if (ItemManager.getId(stack) == id) {
				total += stack.getAmount();
			}
		}
		return total;
	}*/
	
	@Override
	public void run() {
		CivMessage.sendError(sender, "Deprecated do not use anymore.. or fix it..");
		return;
/*		for (int i = 0; i < 20; i++) {
			OfflinePlayer off = offplayers.poll();
			if (off == null) {
				sender.sendMessage("Done... No players polled.");
				return;
			}
			
			try {
				OfflinePlayer offp = off;
				ItemStack[] pFileInv = offp.getPlayer().getInventory().getContents();
				ItemStack[] pFileEnd = offp.getPlayer().getEnderChest().getContents();
				
				int diamondBlocks = countItem(pFileEnd, ItemManager.getId(Material.DIAMOND_BLOCK));
				int diamonds = countItem(pFileEnd, ItemManager.getId(Material.DIAMOND));
				int goldBlocks = countItem(pFileEnd, ItemManager.getId(Material.GOLD_BLOCK));
				int gold = countItem(pFileEnd, ItemManager.getId(Material.GOLD_INGOT));
				int emeraldBlocks = countItem(pFileEnd, ItemManager.getId(Material.EMERALD_BLOCK));
				int emeralds = countItem(pFileEnd, ItemManager.getId(Material.EMERALD));
				int diamondOre = countItem(pFileEnd, ItemManager.getId(Material.DIAMOND_ORE));
				int goldOre = countItem(pFileEnd, ItemManager.getId(Material.GOLD_ORE));
				int emeraldOre = countItem(pFileEnd, ItemManager.getId(Material.EMERALD_ORE));
				
				String out =  off.getName()+": DB:"+diamondBlocks+" EB:"+emeraldBlocks+" GB:"+goldBlocks+" D:"+diamonds+" E:"+emeralds+" G:"+
						gold+" DO:"+diamondOre+" EO:"+emeraldOre+" GO:"+goldOre;
				if (diamondBlocks != 0 || diamonds != 0 || goldBlocks != 0 || gold != 0 || emeraldBlocks != 0 
						|| emeralds != 0 || diamondOre != 0 || goldOre != 0 || emeraldOre != 0) {
					CivMessage.send(sender, out);
					CivLog.info("REPORT:"+out);
				}
				
				diamondBlocks = countItem(pFileInv, ItemManager.getId(Material.DIAMOND_BLOCK));
				diamonds = countItem(pFileInv, ItemManager.getId(Material.DIAMOND));
				goldBlocks = countItem(pFileInv, ItemManager.getId(Material.GOLD_BLOCK));
				gold = countItem(pFileInv, ItemManager.getId(Material.GOLD_INGOT));
				emeraldBlocks = countItem(pFileInv, ItemManager.getId(Material.EMERALD_BLOCK));
				emeralds = countItem(pFileInv,ItemManager.getId( Material.EMERALD));
				diamondOre = countItem(pFileInv, ItemManager.getId(Material.DIAMOND_ORE));
				goldOre = countItem(pFileInv, ItemManager.getId(Material.GOLD_ORE));
				emeraldOre = countItem(pFileInv, ItemManager.getId(Material.EMERALD_ORE));
				
				String out2 =  off.getName()+": DB:"+diamondBlocks+" EB:"+emeraldBlocks+" GB:"+goldBlocks+" D:"+diamonds+" E:"+emeralds+" G:"+
						gold+" DO:"+diamondOre+" EO:"+emeraldOre+" GO:"+goldOre;
				if (diamondBlocks != 0 || diamonds != 0 || goldBlocks != 0 || gold != 0 || emeraldBlocks != 0 
						|| emeralds != 0 || diamondOre != 0 || goldOre != 0 || emeraldOre != 0) {
					CivMessage.send(sender, out2);
					CivLog.info("REPORT:"+out2);
				}
			} catch (Exception e) {
				CivLog.info("REPORT: "+off.getName()+" EXCEPTION:"+e.getMessage());
			}
		}
		TaskMaster.syncTask(new ReportPlayerInventoryTask(sender, offplayers));*/
	}
}
