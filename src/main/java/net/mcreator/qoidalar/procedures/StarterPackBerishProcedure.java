package net.mcreator.qoidalar.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.CompoundTag;

import net.mcreator.qoidalar.init.QoidalarModItems;

@Mod.EventBusSubscriber
public class StarterPackBerishProcedure {
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Player _player = event.getEntity();
        CompoundTag data = _player.getPersistentData();

        // Agar "olgan_starter_pack" belgisi bo'lmasa -> Beramiz
        if (!data.getBoolean("olgan_starter_pack")) {
            
            // Starter Pack itemini berish
            _player.getInventory().add(new ItemStack(QoidalarModItems.STARTER_PACK.get()));

            _player.displayClientMessage(Component.literal("§e[Tizim]: §fSizga davlat tomonidan yordam xaltasi berildi. O'ng tugmani bosib oching!"), false);

            // Belgilab qo'yamiz (qaytib kirganda bermasligi uchun)
            data.putBoolean("olgan_starter_pack", true);
        }
    }
}