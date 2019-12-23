package de.lellson.materialchanger;

import net.minecraftforge.common.config.Configuration;

public class ChangerConfig {
	
	public static final String GENERAL = "general";
	public static final String CHANGER = "changer";
	
	public final MaterialChanger materialChanger;

	public ChangerConfig(MaterialChanger materialChanger) {
		this.materialChanger = materialChanger;
	}

	public String[] load() {
		
		materialChanger.config.addCustomCategoryComment(GENERAL, "General settings");
		materialChanger.config.addCustomCategoryComment(CHANGER, "This section allows you to change attributes of items. Each \"change\" takes 1 line with the following format:\n" +
												 "property;item;value\n\n" + 
				  								 "property: The property you want to change. Allowed properties: ARMOR_PROTECTION, ARMOR_TOUGHNESS, ATTACK_DAMAGE, ATTACK_SPEED, DURABILITY, EFFICIENCY, ENCHANTABILITY, HARVEST_LEVEL, STACKSIZE\n" +
												 "item: The id of the item. This is usually the mod id (or \"minecraft\" for vanilla) followed by a colon followed by the item name. e.g. minecraft:iron_sword\n" +
				  								 "value: The new value for the property.\n\n" +
												 "Keep in mind that changing the enchantability or harvest level affects the whole tool material, which means that you technically only need to change it for one tool.\n" +
				  								 "Unfortunately this also means that changing those attributes for one item also applies the change to every item with the same tool material (example below).\n" +
												 "Also, you can't change the attack speed of swords at the moment. Sorry!\n\n" +
				  								 "Examples:\n\n" + 
												 "ATTACK_DAMAGE;minecraft:iron_axe;12\n" +
				  								 "(Increases the attack damage of every iron axe to 12)\n\n" + 
												 "DURABILITY;minecraft:diamond_sword;500\n" +
												 "DURABILITY;minecraft:diamond_pickaxe;500\n" +
												 "DURABILITY;minecraft:diamond_axe;500\n" +
												 "DURABILITY;minecraft:diamond_shovel;500\n" +
												 "DURABILITY;minecraft:diamond_hoe;500\n" +
												 "(Decreases the durability of every vanilla diamond tool to 500)\n\n" +
												 "DURABILITY;minecraft:flint_and_steel;-1\n" +
												 "(Makes flint and steel unbreakable)\n\n" +
												 "STACKSIZE;minecraft:snowball;64\n" +
												 "(Increases the stacksize of snowballs to 64)\n\n" +
												 "HARVEST_LEVEL;minecraft:diamond_pickaxe;0\n" +
												 "(Decreases the harvest level of diamond pickaxes to 0. They can't even mine iron ore now)\n\n" +
												 "ARMOR_PROTECTION;minecraft:leather_helmet;3\n" +
												 "ARMOR_PROTECTION;minecraft:leather_chestplate;8\n" +
												 "ARMOR_PROTECTION;minecraft:leather_leggings;6\n" +
												 "ARMOR_PROTECTION;minecraft:leather_boots;3\n" +
												 "ARMOR_TOUGHNESS;minecraft:leather_helmet;2\n" +
												 "ARMOR_TOUGHNESS;minecraft:leather_chestplate;2\n" +
												 "ARMOR_TOUGHNESS;minecraft:leather_leggings;2\n" +
												 "ARMOR_TOUGHNESS;minecraft:leather_boots;2\n" +
												 "(Makes leather armor as protective as diamond armor)\n\n" +
												 "ARMOR_PROTECTION;minecraft:golden_helmet;2\n" +
												 "ARMOR_PROTECTION;minecraft:golden_chestplate;6\n" +
												 "ARMOR_PROTECTION;minecraft:golden_leggings;5\n" +
												 "ARMOR_PROTECTION;minecraft:golden_boots;2\n" +
												 "ARMOR_TOUGHNESS;minecraft:golden_helmet;3\n" +
												 "ARMOR_TOUGHNESS;minecraft:golden_chestplate;3\n" +
												 "ARMOR_TOUGHNESS;minecraft:golden_leggings;3\n" +
												 "ARMOR_TOUGHNESS;minecraft:golden_boots;3\n" +
												 "ATTACK_DAMAGE;minecraft:golden_sword;6.5\n" +
												 "ATTACK_DAMAGE;minecraft:golden_pickaxe;4.5\n" +
												 "ATTACK_DAMAGE;minecraft:golden_axe;9\n" +
												 "ATTACK_DAMAGE;minecraft:golden_shovel;5\n" +
												 "ATTACK_SPEED;minecraft:golden_axe;0.95\n" +
												 "ATTACK_SPEED;minecraft:golden_hoe;3.5\n" +
												 "DURABILITY;minecraft:golden_helmet;212\n" +
												 "DURABILITY;minecraft:golden_chestplate;308\n" +
												 "DURABILITY;minecraft:golden_leggings;289\n" +
												 "DURABILITY;minecraft:golden_boots;250\n" +
												 "DURABILITY;minecraft:golden_sword;420\n" +
												 "DURABILITY;minecraft:golden_pickaxe;420\n" +
												 "DURABILITY;minecraft:golden_axe;420\n" +
												 "DURABILITY;minecraft:golden_shovel;420\n" +
												 "DURABILITY;minecraft:golden_hoe;420\n" +
												 "EFFICIENCY;minecraft:golden_pickaxe;7\n" +
												 "EFFICIENCY;minecraft:golden_axe;7\n" +
												 "EFFICIENCY;minecraft:golden_shovel;7\n" +
												 "HARVEST_LEVEL;minecraft:golden_pickaxe;2\n" +
												 "ENCHANTABILITY;minecraft:golden_pickaxe;16\n" +
												 "ENCHANTABILITY;minecraft:golden_helmet;19\n" +
												 "(Better Gold: Changes golden tools, swords and armor properties completley. As you can see, I only changed the enchantability for one tool and one armor item since it affects the whole gold material)");
		
		return materialChanger.config.getStringList("changer", CHANGER, new String[]{}, "");
	}
}
