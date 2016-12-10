package com.avrgaming.civcraft.items.components;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.util.CivColor;

import gpl.AttributeUtil;
import gpl.AttributeUtil.Attribute;
import gpl.AttributeUtil.AttributeType;

public class Attack extends ItemComponent {
	
	@Override
	public void onPrepareCreate(AttributeUtil attrs) {
		attrs.add(Attribute.newBuilder().name("Attack").
				type(AttributeType.GENERIC_ATTACK_DAMAGE).
				amount(this.getDouble("value")).build());
		attrs.addLore(CivColor.Rose+""+this.getDouble("value")+" Attack");
		return;
	}
	
/*	@Override
	public void onInteract(PlayerInteractEvent event) {
		ItemStack stack = event.getItem();
		
		net.minecraft.server.v1_10_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);
		NBTTagCompound compound = (nmsStack.hasTag()) ? nmsStack.getTag() : new NBTTagCompound();
		NBTTagList modifiers = new NBTTagList();
		NBTTagCompound damage = new NBTTagCompound();
		
		damage.set("AttributeName", new NBTTagString("generic.attackDamage"));
		damage.set("Name", new NBTTagString("generic.attackDamage"));
		damage.set("Operation", new NBTTagInt(0));
		damage.set("UUIDLeast", new NBTTagInt(894654));
		damage.set("UUIDMost", new NBTTagInt(2872));
		damage.set("Slot", new NBTTagString("mainhand"));
		
		modifiers.add(damage);
		compound.set("AttributeModifiers", modifiers);
		nmsStack.setTag(compound);
		stack = CraftItemStack.asBukkitCopy(nmsStack);
		LoreMaterial.getMaterial(stack).onInteract(event);
	}*/
	
	@Override
	public void onHold(PlayerItemHeldEvent event) {	
		Resident resident = CivGlobal.getResident(event.getPlayer());
		if (!resident.hasTechForItem(event.getPlayer().getInventory().getItem(event.getNewSlot()))) {		
			CivMessage.sendNoRepeat(resident, CivColor.Red+"WARNING - "+CivColor.LightGray+
					"You don't have the technology to use this item. It's attack output is reduced in half.");
		}
	}
	
	@Override
	public void onAttack(EntityDamageByEntityEvent event, ItemStack inHand) {
		double dmg = this.getDouble("value");
		if (event.getDamager() instanceof Player) {
			Resident resident = CivGlobal.getResident(((Player)event.getDamager()));
			if (!resident.hasTechForItem(inHand)) {
				dmg = dmg/2;
			}
		}
//		if (dmg < 0.5) {
//		/* Always do at least 0.5 damage. */
//		dmg = 0.5;
//	}
		event.setDamage(dmg);
	}
}
