package net.mcreator.qoidalar.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.nbt.CompoundTag;

import net.mcreator.qoidalar.init.QoidalarModItems;

@Mod.EventBusSubscriber
public class StarterPackOchishProcedure {
    @SubscribeEvent
    public static void onItemRightClick(PlayerInteractEvent.RightClickItem event) {
        Player _player = event.getEntity();
        Level world = event.getLevel();
        ItemStack itemStack = event.getItemStack();

        // Agar qo'lidagi narsa STARTER PACK bo'lsa
        if (itemStack.getItem() == QoidalarModItems.STARTER_PACK.get()) {
            if (!world.isClientSide()) {
                // 1. Xaltani yo'qotish
                itemStack.shrink(1);

                // 2. Ichidagi narsalarni berish
                // A) Pasport (Muhim!)
                _player.getInventory().add(new ItemStack(QoidalarModItems.PASPORT.get()));
                
                // B) Yegulik (5 ta Non)
                _player.getInventory().add(new ItemStack(Items.BREAD, 5));
                
                // C) Himoya (Temir Qilich)
                _player.getInventory().add(new ItemStack(Items.IRON_SWORD));
                
                // D) Kiyim (Charm Kurtka - Oq rang)
                ItemStack kurtka = new ItemStack(Items.LEATHER_CHESTPLATE);
                CompoundTag nbt = kurtka.getOrCreateTagElement("display");
                nbt.putInt("color", 16777215); // Oq rang kodi
                _player.getInventory().add(kurtka);

                // 3. Xabar va Ovoz
                _player.displayClientMessage(Component.literal("§a[Hokimiyat]: §fXush kelibsiz! Sizga 'Ijtimoiy Yordam' to'plami berildi."), true);
                world.playSound(null, _player.blockPosition(), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("item.armor.equip_leather")), SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        }
    }
}