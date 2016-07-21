/** CivCraft by AVRGAMING LLC -- Modified by - https://www.youtube.com/YourCoalMC**/
package com.civcraft.object;

import org.bukkit.enchantments.Enchantment;

import com.civcraft.exception.CivException;
import com.civcraft.loreenhancements.LoreEnhancement;
import com.civcraft.structure.Library;

public class LibraryEnchantment {
	public Enchantment enchant;
	public LoreEnhancement enhancement;
	public int level;
	public double price;
	public String name;
	public String displayName;

	public LibraryEnchantment(String name, int lvl, double p) throws CivException {
		enchant = Library.getEnchantFromString(name);
		if (enchant == null)  {
			enhancement = LoreEnhancement.enhancements.get(name);
			if (enhancement == null) {
				throw new CivException("Could not create CivEnchantment:"+name+". Couldn't find enchantment or enhancement");
			}
		}
		level = lvl;
		price = p;
		
		this.name = name;
		if (enchant != null) {
			displayName = name.replace("_", " ");
		} else {
			displayName = enhancement.getDisplayName();
		}
		
	}
}
