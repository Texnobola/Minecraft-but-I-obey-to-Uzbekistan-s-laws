package net.mcreator.qoidalar.item;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.network.chat.Component;

import java.util.List;

public class PasportItem extends Item {
    public PasportItem() {
        // UPGRADE: .fireResistant() qo'shildi - Pasport olovda yonmaydi!
        super(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).fireResistant());
    }

    @Override
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        list.add(Component.literal("§7Shaxsni tasdiqlovchi biometrik hujjat."));
        list.add(Component.literal("§ePropiska tizimi (22-modda):"));
        list.add(Component.literal("§cQishloqqa kirish uchun talab qilinadi!"));
    }
}