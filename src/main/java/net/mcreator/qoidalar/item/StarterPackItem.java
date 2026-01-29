package net.mcreator.qoidalar.item;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;

public class StarterPackItem extends Item {
	public StarterPackItem() {
		super(new Item.Properties().stacksTo(1).fireResistant().rarity(Rarity.EPIC));
	}
}