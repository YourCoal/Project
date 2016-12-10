package com.avrgaming.civcraft.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.avrgaming.civcraft.config.CivSettings;
import com.avrgaming.civcraft.main.CivMessage;

public class TestCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (!(sender instanceof Player))
		{
			CivMessage.sendError(sender, "Only a player can execute this command.");
			return false;
		}
		
		if (!sender.isOp() && !sender.hasPermission(CivSettings.ECON))
		{
			CivMessage.sendError(sender, "You do not have permission to execute this command.");
			return false;
		}
		
		Player player = (Player)sender;
		{
			@SuppressWarnings("deprecation")
			ItemStack item = player.getItemInHand();
			addGlow(item);
				return true;
		}			
	}
	
	public void addGlow(ItemStack stack)
	{
		ItemMeta meta = stack.getItemMeta();
		meta.addEnchant( Enchantment.LURE, 1, false );
		meta.addItemFlags( ItemFlag.HIDE_ENCHANTS );
		stack.setItemMeta( meta );
	}
	
}
