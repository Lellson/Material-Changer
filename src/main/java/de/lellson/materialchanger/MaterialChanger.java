package de.lellson.materialchanger;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

@Mod(modid = MaterialChanger.MODID, name = MaterialChanger.NAME, version = "1.0")
public class MaterialChanger {
	
	public static final String MODID = "materialchanger";
	public static final String NAME = "Material Changer";
	
	public static final int ARMOR_PROTECTION_INDEX = 5;
	public static final int ARMOR_TOUGHNESS_INDEX = 6;
	public static final int TOOL_EFFICIENCY_INDEX = 1;
	public static final int TOOL_DAMAGE_INDEX = 2;
	public static final int TOOL_ATTACK_SPEED_INDEX = 3;
	private static final int HOE_ATTACK_SPEED_INDEX = 0;
	public static final int SWORD_DAMAGE_INDEX = 0;
	public static final int MATERIAL_HARVEST_LEVEL_INDEX = 5;
	public static final int MATERIAL_ENCHANTABILITY_INDEX = 9;
	private static final int ARMOR_MATERIAL_ENCHANTABILITY_INDEX = 8;
	
	public Configuration config;
	private String[] configEntries;
	private List<Entry> entries;
	private ErrorMessenger error = new ErrorMessenger();
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		
		config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		configEntries = new ChangerConfig(this).load();
		config.save();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		
		entries = toList(configEntries);
		for (Entry entry : entries)
		{
			switch(entry.property)
			{
				case ARMOR_PROTECTION: changeArmor(entry, false); break;
				case ARMOR_TOUGHNESS: changeArmor(entry, true); break;
				case ATTACK_DAMAGE: changeAttackDamage(entry); break;
				case ATTACK_SPEED: changeAttackSpeed(entry); break;
				case DURABILITY: changeDurability(entry); break;
				case EFFICIENCY: changeEfficiency(entry); break;
				case ENCHANTABILITY: changeMaterial(entry, MATERIAL_ENCHANTABILITY_INDEX, ARMOR_MATERIAL_ENCHANTABILITY_INDEX); break;
				case HARVEST_LEVEL: changeMaterial(entry, MATERIAL_HARVEST_LEVEL_INDEX, -1); break;
				case STACKSIZE: changeStackSize(entry); break;
			}
		}
	}
	
	private void changeStackSize(Entry entry) {
		entry.item.setMaxStackSize((int)entry.value);
	}

	private void changeMaterial(Entry entry, int fieldIndexTool, int fieldIndexArmor) {
		
		ToolMaterial material = entry.getObjectFromClass(ItemTool.class, ToolMaterial.class);
		if (material == null)
		{
			material = entry.getObjectFromClass(ItemSword.class, ToolMaterial.class);
		}
			
		ArmorMaterial armorMaterial = entry.getObjectFromClass(ItemArmor.class, ArmorMaterial.class);
		boolean flag = false;
		
		if (material != null && fieldIndexTool >= 0)
		{
			ReflectionHelper.setPrivateValue(ToolMaterial.class, material, (int)entry.value, fieldIndexTool);
			flag = true;
		}
		
		if (armorMaterial != null && fieldIndexArmor >= 0)
		{
			ReflectionHelper.setPrivateValue(ArmorMaterial.class, armorMaterial, (int)entry.value, fieldIndexArmor);
			flag = true;
		}
		
		if (!flag)
			error.add(entry.line, "Couldn't find a matching tool material or armor material for the item: " + entry.item.getRegistryName() + " (Only tools, swords and armor can have a material)");
	}

	private void changeAttackSpeed(Entry entry) {
		
		if (entry.item instanceof ItemSword)
		{
			//TODO sword attack speed
			error.add(entry.line, "Sorry, but you can't change the attack speed of swords at the moment :(");
		}
		else if (entry.item instanceof ItemTool)
		{
			ReflectionHelper.setPrivateValue(ItemTool.class, (ItemTool)entry.item, (float)entry.value-4, TOOL_ATTACK_SPEED_INDEX);
		}
		else if (entry.item instanceof ItemHoe)
		{
			ReflectionHelper.setPrivateValue(ItemHoe.class, (ItemHoe)entry.item, (float)entry.value, HOE_ATTACK_SPEED_INDEX);
		}
		else
		{
			error.add(entry.line, "The item is not a valid tool: " + entry.item.getRegistryName() + " (Only tools can have an attack speed value)");
		}
	}

	private void changeEfficiency(Entry entry) {
		
		if (entry.item instanceof ItemTool)
		{
			ReflectionHelper.setPrivateValue(ItemTool.class, (ItemTool)entry.item, (float)entry.value, TOOL_EFFICIENCY_INDEX);
		}
	}

	private void changeAttackDamage(Entry entry) {
		
		if (entry.item instanceof ItemSword)
		{
			ReflectionHelper.setPrivateValue(ItemSword.class, (ItemSword)entry.item, (float)entry.value-1, SWORD_DAMAGE_INDEX);
		}
		else if (entry.item instanceof ItemTool)
		{
			ReflectionHelper.setPrivateValue(ItemTool.class, (ItemTool)entry.item, (float)entry.value-1, TOOL_DAMAGE_INDEX);
		}
		else
		{
			error.add(entry.line, "The item is not a valid tool or weapon: " + entry.item.getRegistryName() + " (Only tools and swords can have an attack damage value)");
		}
	}

	private void changeDurability(Entry entry) {
		entry.item.setMaxDamage((int)entry.value);
	}

	private void changeArmor(Entry entry, boolean toughness) {
		
		if (entry.item instanceof ItemArmor)
		{
			ReflectionHelper.setPrivateValue(ItemArmor.class, (ItemArmor)entry.item, (int)entry.value, toughness ? ARMOR_TOUGHNESS_INDEX : ARMOR_PROTECTION_INDEX);
		}
		else
		{
			error.add(entry.line, "The item is not a valid armor piece: " + entry.item.getRegistryName() + " (Only armor pieces can have a protection/toughness value)");
		}
	}

	private List<Entry> toList(String[] array) {
		
		List<Entry> list = new ArrayList<Entry>();
		for (String line : array)
		{
			line = validate(line);
			if (line.isEmpty()) 
				continue;
			final String[] values = line.split(";");
			if (values.length >= 3)
			{
				Entry entry = transformValues(line, values);
				if (entry != null) 
					list.add(entry);
			}
			else
			{
				error.add(line, "Invalid amount of arguments: " + values.length + " (3 arguments needed! Usage: property;item;value)");
			}
		}
		
		return list;
	}

	private Entry transformValues(String line, String[] values) {
		
		Property prop = null;
		for (Property p : Property.values())
		{
			if (p.equals(values[0]))
			{
				prop = p;
				break;
			}
		}
		
		if (prop == null)
		{
			error.add(line, "Invalid property: " + values[0] + " (Properties: ARMOR_PROTECTION, ARMOR_TOUGHNESS, ATTACK_DAMAGE, ATTACK_SPEED, DURABILITY, EFFICIENCY, ENCHANTABILITY, HARVEST_LEVEL, STACKSIZE)");
			return null;
		}
		
		double value;
		try 
		{
			value = Double.parseDouble(values[2]);
			value = Math.min(value, (double)Short.MAX_VALUE);
		}
		catch(NumberFormatException e)
		{
			error.add(line, "Invalid value: " + values[3]);
			return null;
		}
		
		ItemStack item = GameRegistry.makeItemStack(values[1], 0, 1, "");
		
		if (item == null)
		{
			error.add(line, "Invalid item: " + values[1] + " (Format: modid:name)");
			return null;
		}

		return new Entry(prop, item.getItem(), value, line);
	}

	private String validate(String line) {
		return line.replaceAll(" ", "");
	}

	public class Entry {
		
		public final Property property;
		public final Item item;
		public final double value;
		public final String line;
		
		public Entry(Property property, Item item, double value, String line) {
			this.property = property;
			this.item = item;
			this.value = value;
			this.line = line;
		}

		public <T> T getObjectFromClass(Class fromClazz, Class<T> clazz) {
			
			if (!fromClazz.isInstance(item)) return null;
			
			try
			{
				for (Field field : fromClazz.getDeclaredFields())
				{
					field.setAccessible(true);
					Object object = field.get(item);
					if (clazz.isInstance(object))
					{
						return (T) object;
					}
				}
			}
			catch(Exception e)
			{
				error.add(line, null);
				e.printStackTrace();
			}
			
			return null;
		}
	}
	
	public enum Property {
		ARMOR_PROTECTION, ARMOR_TOUGHNESS, ATTACK_DAMAGE, ATTACK_SPEED, DURABILITY, EFFICIENCY, ENCHANTABILITY, HARVEST_LEVEL, STACKSIZE;
		
		public boolean equals(String s) {
			return s.toLowerCase().equals(this.toString()); 
		}
		
		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}
	}
}
