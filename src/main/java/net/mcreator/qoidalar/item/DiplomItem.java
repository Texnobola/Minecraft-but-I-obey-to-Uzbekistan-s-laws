package net.mcreator.qoidalar.item;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.network.chat.Component;

import java.util.List;

public class DiplomItem extends Item {
    public DiplomItem() {
        // UPGRADE: .fireResistant() qo'shildi (olovda yonmaydi)
        // Rarity.EPIC - Qizil diplom bo'lgani uchun nomi binafsha rangda chiqadi
        super(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).fireResistant());
    }

    @Override
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        list.add(Component.literal("§7Oliy ma'lumot to'g'risidagi rasmiy hujjat."));
        list.add(Component.literal("§eKonstitutsiyaning 41-moddasi:"));
        list.add(Component.literal("§7\"Har kim bilim olish huquqiga ega.\""));
        list.add(Component.literal("§cBusiz ilmiy uskunalar ishlamaydi!"));
    }
}