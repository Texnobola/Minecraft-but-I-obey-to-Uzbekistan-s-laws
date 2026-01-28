package net.mcreator.qoidalar.item;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.network.chat.Component;

import java.util.List;

public class KadastrHujjatiItem extends Item {
    public KadastrHujjatiItem() {
        // UPGRADE 1: .fireResistant() qo'shildi - Endi bu hujjat olovda yonmaydi!
        super(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON).fireResistant());
    }

    @Override
    public boolean hasCraftingRemainingItem() {
        return true;
    }

    // UPGRADE 2: Craftingda ishlatilganda qog'ozga aylanmaydi, o'zini qaytaradi.
    // Bu degani hujjatni yo'qotmasdan qayta-qayta ishlatish mumkin.
    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemstack) {
        return itemstack.copy();
    }

    // UPGRADE 3: Item haqida ma'lumot (Tooltip)
    @Override
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        list.add(Component.literal("§7Uyingizni \"snos\"dan saqlaydigan yagona rasmiy hujjat."));
        list.add(Component.literal("§cOlovda yonmaydi."));
    }
}