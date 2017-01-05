package gpl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import net.minecraft.server.v1_10_R1.AttributeInstance;
import net.minecraft.server.v1_10_R1.AttributeModifier;
import net.minecraft.server.v1_10_R1.EntityInsentient;
import net.minecraft.server.v1_10_R1.GenericAttributes;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
 
/**
* LlamaModifier v1.1
*
* You are free to use it, modify it and redistribute it under the condition to give credit to me
*
* @author DarkBlade12, modified by YourCoal
*/
public class LlamaModifier {
	private Object entityLlama;
	private Object nbtTagCompound;
	
	private static final UUID movementSpeedUID = UUID.fromString("206a89dc-ae78-4c4d-b42c-3b31db3f5a7c");
 
	/**
	* Creates a new instance of the LlamaModifier, which allows you to change/get values of llamas which aren't accessible with the bukkit api atm
	*/
	public LlamaModifier(LivingEntity llama) {
		if (!LlamaModifier.isLlama(llama)) {
			throw new IllegalArgumentException("Entity has to be a llama!");
		}
		try {
			this.entityLlama = ReflectionUtil.getMethod("getHandle", llama.getClass(), 0).invoke(llama);
			this.nbtTagCompound = NBTUtil.getNBTTagCompound(entityLlama);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 
	/**
	* Creates a new instance of the LlamaModifier; This constructor is only used for the static spawn method
	*/
	private LlamaModifier(Object entityLlama) {
		this.entityLlama = entityLlama;
		try {
			this.nbtTagCompound = NBTUtil.getNBTTagCompound(entityLlama);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 
	/**
	* Spawns a llama at a given location
	*/
	public static LlamaModifier spawn(Location loc) {
		World w = loc.getWorld();
		try {
			Object worldServer = ReflectionUtil.getMethod("getHandle", w.getClass(), 0).invoke(w);
			Object entityLlama = ReflectionUtil.getClass("EntityLlama", worldServer);
			ReflectionUtil.getMethod("setPosition", entityLlama.getClass(), 3).invoke(entityLlama, loc.getX(), loc.getY(), loc.getZ());
			ReflectionUtil.getMethod("addEntity", worldServer.getClass(), 1).invoke(worldServer, entityLlama);
			return new LlamaModifier(entityLlama);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
 
	/**
	* Checks if an entity is a llama
	*/
	public static boolean isLlama(LivingEntity le) {
		try {
			Object entityLiving = ReflectionUtil.getMethod("getHandle", le.getClass(), 0).invoke(le);
			Object nbtTagCompound = NBTUtil.getNBTTagCompound(entityLiving);
			return NBTUtil.hasKeys(nbtTagCompound, new String[] { "EatingHaystack", "ChestedHorse", "HasReproduced", "Bred", "Type", "Variant", "Temper", "Tame", "Strength" });
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
 
	
	public static void setLlamaSpeed(LivingEntity entity, double amount) {
		if (!isLlama(entity)) {
			return;
		}
		
		EntityInsentient nmsEntity = (EntityInsentient) ((CraftLivingEntity) entity).getHandle();
		AttributeInstance attributes = nmsEntity.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);
		AttributeModifier modifier = new AttributeModifier(movementSpeedUID, "civcraft llama movement speed", amount, 0);
		attributes.b(modifier); //remove the modifier, adding a duplicate causes errors
		attributes.a(modifier); //add the modifier
		//done??
	}
	
	public static boolean isCivCraftLlama(LivingEntity entity) {
		if (!isLlama(entity)) {
			return false;
		}
		
		EntityInsentient nmsEntity = (EntityInsentient) ((CraftLivingEntity) entity).getHandle();
		AttributeInstance attributes = nmsEntity.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);
		
		if (attributes.a(movementSpeedUID) == null) {
			return false;
		}
			
		return true;
		
	//	AttributeModifier modifier = new AttributeModifier(movementSpeedUID, "civcraft llama movement speed", amount, 1);
		//attributes.b(modifier); //remove the modifier, adding a duplicate causes errors
	//	attributes.a(modifier); //add the modifier
	}
	
	/**
	* Changes the type of the llama
	*/
	public void setType(LlamaType type) {
		setLlamaValue("Type", type.getId());
	}
 
	/**
	* Changes whether the llama is chested or not (only for donkeys and mules)
	*/
	public void setChested(boolean chested) {
		setLlamaValue("ChestedHorse", chested);
	}
 
	/**
	* Changes whether the llama is eating or not
	*/
	public void setEating(boolean eating) {
		setLlamaValue("EatingHaystack", eating);
	}
 
	/**
	* Changes whether the llama was bred or not
	*/
	public void setBred(boolean bred) {
		setLlamaValue("Bred", bred);
	}
	
	/**
	* Changes the llama chest size
	*/
	public void setStrength(int strength) {
		setLlamaValue("Strength", strength);
	}
 
	/**
	* Changes the color variant of the llama (only for normal llamas)
	*/
	public void setVariant(LlamaVariant variant) {
		setLlamaValue("Variant", variant.getId());
	}
 
	/**
	* Changes the temper of the llama
	*/
	public void setTemper(int temper) {
		setLlamaValue("Temper", temper);
	}
 
	/**
	* Changes whether the llama is tamed or not
	*/
	public void setTamed(boolean tamed) {
		setLlamaValue("Tame", tamed);
	}
 
	/**
	* Changes whether the llama is saddled or not
	*/
	public void setSaddled(boolean saddled) {
		setLlamaValue("Saddle", saddled);
	}
 
	/**
	* Sets the armor item of the llama (only for normal llamas)
	*/
	public void setDecorItem(ItemStack i) {
		if (i != null) {
			try {
				Object itemTag = ReflectionUtil.getClass("NBTTagCompound", "DecorItem");
				Object itemStack = ReflectionUtil.getMethod("asNMSCopy", Class.forName(Bukkit.getServer().getClass().getPackage().getName() + ".inventory.CraftItemStack"), 1).invoke(this, i);
				ReflectionUtil.getMethod("save", itemStack.getClass(), 1).invoke(itemStack, itemTag);
				setLlamaValue("DecorItem", itemTag);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			setLlamaValue("DecorItem", null);
		}
	}
 
	/**
	* Returns the type of the llama
	*/
	public LlamaType getType() {
		return LlamaType.fromId((int) NBTUtil.getValue(nbtTagCompound, Integer.class, "Type"));
	}
 
	/**
	* Returns whether the llama is chested or not
	*/
	public boolean isChested() {
		return (boolean) NBTUtil.getValue(nbtTagCompound, Boolean.class, "ChestedHorse");
	}
 
	/**
	* Returns whether the llama is eating or not
	*/
	public boolean isEating() {
		return (boolean) NBTUtil.getValue(nbtTagCompound, Boolean.class, "EatingHaystack");
	}
 
	/**
	* Returns whether the llama was bred or not
	*/
	public boolean isBred() {
		return (boolean) NBTUtil.getValue(nbtTagCompound, Boolean.class, "Bred");
	}
	
	/**
	* Returns the llama strength
	*/
	public int getStrength() {
		return (int) NBTUtil.getValue(nbtTagCompound, Integer.class, "Strength");
	}
 
	/**
	* Returns the variant of the llama
	*/
	public LlamaVariant getVariant() {
		return LlamaVariant.fromId((int) NBTUtil.getValue(nbtTagCompound, Integer.class, "Variant"));
	}
 
	/**
	* Returns the temper of the llama
	*/
	public int getTemper() {
		return (int) NBTUtil.getValue(nbtTagCompound, Integer.class, "Temper");
	}
 
	/**
	* Returns whether the llama is tamed or not
	*/
	public boolean isTamed() {
		return (boolean) NBTUtil.getValue(nbtTagCompound, Boolean.class, "Tame");
	}
 
	/**
	* Returns whether the llama is saddled or not
	*/
	public boolean isSaddled() {
		return (boolean) NBTUtil.getValue(nbtTagCompound, Boolean.class, "Saddle");
	}
 
	/**
	* Returns the armor item of the llama
	*/
	public ItemStack getDecorItem() {
		try {
			Object itemTag = NBTUtil.getValue(nbtTagCompound, nbtTagCompound.getClass(), "DecorItem");
			Object itemStack = ReflectionUtil.getMethod("createStack", Class.forName(ReflectionUtil.getPackageName() + ".ItemStack"), 1).invoke(this, itemTag);
			return (ItemStack) ReflectionUtil.getMethod("asCraftMirror", Class.forName(Bukkit.getServer().getClass().getPackage().getName() + ".inventory.CraftItemStack"), 1).invoke(this, itemStack);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
 
	/**
	* Opens the inventory of the llama for a player (only for tamed llamas)
	*/
	public void openInventory(Player p) {
		try {
			Object entityPlayer = ReflectionUtil.getMethod("getHandle", p.getClass(), 0).invoke(p);
			ReflectionUtil.getMethod("f", entityLlama.getClass(), 1).invoke(entityLlama, entityPlayer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 
	/**
	* Returns the llama entity
	*/
	public LivingEntity getLlama() {
		try {
			return (LivingEntity) ReflectionUtil.getMethod("getBukkitEntity", entityLlama.getClass(), 0).invoke(entityLlama);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
 
	/**
	* Changes a value in the NBTTagCompound and updates it to the llama
	*/
	private void setLlamaValue(String key, Object value) {
		NBTUtil.setValue(nbtTagCompound, key, value);
		NBTUtil.updateNBTTagCompound(entityLlama, nbtTagCompound);
	}
 
	public enum LlamaType {
 
		NORMAL("normal", 0), OTHER("normal", 1);
 
		private String name;
		private int id;
 
		LlamaType(String name, int id) {
			this.name = name;
			this.id = id;
		}
 
		public String getName() {
			return name;
		}
 
		public int getId() {
			return id;
		}
 
		private static final Map<String, LlamaType> NAME_MAP = new HashMap<String, LlamaType>();
		private static final Map<Integer, LlamaType> ID_MAP = new HashMap<Integer, LlamaType>();
		static {
			for (LlamaType effect : values()) {
				NAME_MAP.put(effect.name, effect);
				ID_MAP.put(effect.id, effect);
			}
		}
 
		public static LlamaType fromName(String name) {
			if (name == null) {
				return null;
			}
			for (Entry<String, LlamaType> e : NAME_MAP.entrySet()) {
				if (e.getKey().equalsIgnoreCase(name)) {
					return e.getValue();
				}
			}
			return null;
		}
 
		public static LlamaType fromId(int id) {
			return ID_MAP.get(id);
		}
	}
 
	public enum LlamaVariant {
		CREAMY("creamy", 0), WHITE("white", 1), BROWN("brown", 2), GRAY("gray", 3);
 
		private String name;
		private int id;
 
		LlamaVariant(String name, int id) {
			this.name = name;
			this.id = id;
		}
 
		public String getName() {
			return name;
		}
 
		public int getId() {
			return id;
		}
 
		private static final Map<String, LlamaVariant> NAME_MAP = new HashMap<String, LlamaVariant>();
		private static final Map<Integer, LlamaVariant> ID_MAP = new HashMap<Integer, LlamaVariant>();
		static {
			for (LlamaVariant effect : values()) {
				NAME_MAP.put(effect.name, effect);
				ID_MAP.put(effect.id, effect);
			}
		}
 
		public static LlamaVariant fromName(String name) {
			if (name == null) {
				return null;
			}
			for (Entry<String, LlamaVariant> e : NAME_MAP.entrySet()) {
				if (e.getKey().equalsIgnoreCase(name)) {
					return e.getValue();
				}
			}
			return null;
		}
 
		public static LlamaVariant fromId(int id) {
			return ID_MAP.get(id);
		}
	}
 
	private static class NBTUtil {
		public static Object getNBTTagCompound(Object entity) {
			try {
				Object nbtTagCompound = ReflectionUtil.getClass("NBTTagCompound");
				for (Method m : entity.getClass().getMethods()) {
					Class<?>[] pt = m.getParameterTypes();
					if (m.getName().equals("b") && pt.length == 1 && pt[0].getName().contains("NBTTagCompound")) {
						m.invoke(entity, nbtTagCompound);
					}
				}
				return nbtTagCompound;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
 
		public static void updateNBTTagCompound(Object entity, Object nbtTagCompound) {
			try {
				for (Method m : entity.getClass().getMethods()) {
					Class<?>[] pt = m.getParameterTypes();
					if (m.getName().equals("a") && pt.length == 1 && pt[0].getName().contains("NBTTagCompound")) {
						m.invoke(entity, nbtTagCompound);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
 
		public static void setValue(Object nbtTagCompound, String key, Object value) {
			try {
				if (value instanceof Integer) {
					ReflectionUtil.getMethod("setInt", nbtTagCompound.getClass(), 2).invoke(nbtTagCompound, key, (Integer) value);
					return;
				} else if (value instanceof Boolean) {
					ReflectionUtil.getMethod("setBoolean", nbtTagCompound.getClass(), 2).invoke(nbtTagCompound, key, (Boolean) value);
					return;
				} else {
					ReflectionUtil.getMethod("set", nbtTagCompound.getClass(), 2).invoke(nbtTagCompound, key, value);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
 
		public static Object getValue(Object nbtTagCompound, Class<?> c, String key) {
			try {
				if (c == Integer.class) {
					return ReflectionUtil.getMethod("getInt", nbtTagCompound.getClass(), 1).invoke(nbtTagCompound, key);
				} else if (c == Boolean.class) {
					return ReflectionUtil.getMethod("getBoolean", nbtTagCompound.getClass(), 1).invoke(nbtTagCompound, key);
				} else {
					return ReflectionUtil.getMethod("getCompound", nbtTagCompound.getClass(), 1).invoke(nbtTagCompound, key);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
 
		public static boolean hasKey(Object nbtTagCompound, String key) {
			try {
				return (boolean) ReflectionUtil.getMethod("hasKey", nbtTagCompound.getClass(), 1).invoke(nbtTagCompound, key);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
 
		public static boolean hasKeys(Object nbtTagCompound, String[] keys) {
			for (String key : keys) {
				if (!hasKey(nbtTagCompound, key)) {
					return false;
				}
			}
			return true;
		}
	}
 
	private static class ReflectionUtil {
		public static Object getClass(String name, Object... args) throws Exception {
			Class<?> c = Class.forName(ReflectionUtil.getPackageName() + "." + name);
			int params = 0;
			if (args != null) {
				params = args.length;
			}
			for (Constructor<?> co : c.getConstructors()) {
				if (co.getParameterTypes().length == params) {
					return co.newInstance(args);
				}
			}
			return null;
		}
 
		public static Method getMethod(String name, Class<?> c, int params) {
			for (Method m : c.getMethods()) {
				if (m.getName().equals(name) && m.getParameterTypes().length == params) {
					return m;
				}
			}
			return null;
		}
 
		public static String getPackageName() {
			return "net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
		}
	}
}