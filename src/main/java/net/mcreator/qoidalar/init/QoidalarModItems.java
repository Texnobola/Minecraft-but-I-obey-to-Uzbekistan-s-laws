/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.qoidalar.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.common.ForgeSpawnEggItem;

import net.minecraft.world.item.Item;

import net.mcreator.qoidalar.item.LitsenziyaItem;
import net.mcreator.qoidalar.item.KadastrHujjatiItem;
import net.mcreator.qoidalar.item.DiplomItem;
import net.mcreator.qoidalar.QoidalarMod;

public class QoidalarModItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, QoidalarMod.MODID);
	public static final RegistryObject<Item> RAIS_SPAWN_EGG;
	public static final RegistryObject<Item> LITSENZIYA;
	public static final RegistryObject<Item> KADASTR_HUJJATI;
	public static final RegistryObject<Item> DIPLOM;
	static {
		RAIS_SPAWN_EGG = REGISTRY.register("rais_spawn_egg", () -> new ForgeSpawnEggItem(QoidalarModEntities.RAIS, -16777216, -1, new Item.Properties()));
		LITSENZIYA = REGISTRY.register("litsenziya", LitsenziyaItem::new);
		KADASTR_HUJJATI = REGISTRY.register("kadastr_hujjati", KadastrHujjatiItem::new);
		DIPLOM = REGISTRY.register("diplom", DiplomItem::new);
	}
	// Start of user code block custom items
	// End of user code block custom items
}