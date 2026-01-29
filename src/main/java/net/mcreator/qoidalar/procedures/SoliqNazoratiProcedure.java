package net.mcreator.qoidalar.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;

import java.util.concurrent.atomic.AtomicInteger;

@Mod.EventBusSubscriber
public class SoliqNazoratiProcedure {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Player _player = event.player;
            Level world = _player.level();

            // Har kuni ertalab 06:00 da (Vaqt 0 bo'lganda)
            if (!world.isClientSide() && world.getDayTime() % 24000 == 0) {
                if (_player.isCreative() || _player.isSpectator()) return;

                // 1. BOYLIKNI HISOBLASH
                AtomicInteger wealthScore = new AtomicInteger();
                
                for (int i = 0; i < _player.getInventory().getContainerSize(); i++) {
                    ItemStack stack = _player.getInventory().getItem(i);
                    if (stack.getItem() == Items.EMERALD) wealthScore.addAndGet(stack.getCount() * 1);       // Zumrad = 1
                    if (stack.getItem() == Items.GOLD_INGOT) wealthScore.addAndGet(stack.getCount() * 2);    // Oltin = 2
                    if (stack.getItem() == Items.DIAMOND) wealthScore.addAndGet(stack.getCount() * 5);       // Olmos = 5
                    if (stack.getItem() == Items.NETHERITE_INGOT) wealthScore.addAndGet(stack.getCount() * 20); // Netherite = 20
                }

                // 2. Agar boylik 100 dan oshsa -> SOLIQ
                if (wealthScore.get() > 100) {
                    _player.displayClientMessage(Component.literal("§6[Soliq]: §fBoylik deklaratsiyasi: " + wealthScore.get() + " ball. (Limit: 100)"), false);
                    
                    boolean taxPaid = false;
                    
                    // Eng qimmatidan boshlab bitta dona olib qo'yamiz
                    if (removeOneItem(_player, Items.NETHERITE_INGOT)) {
                         _player.displayClientMessage(Component.literal("§c[Soliq]: 1 ta Netherite daromad solig'i sifatida olindi."), true);
                         taxPaid = true;
                    } 
                    else if (removeOneItem(_player, Items.DIAMOND)) {
                         _player.displayClientMessage(Component.literal("§c[Soliq]: 1 ta Olmos daromad solig'i sifatida olindi."), true);
                         taxPaid = true;
                    } 
                    else if (removeOneItem(_player, Items.GOLD_INGOT)) {
                         _player.displayClientMessage(Component.literal("§c[Soliq]: 1 ta Oltin daromad solig'i sifatida olindi."), true);
                         taxPaid = true;
                    }
                    else if (removeOneItem(_player, Items.EMERALD)) {
                         _player.displayClientMessage(Component.literal("§c[Soliq]: 1 ta Zumrad daromad solig'i sifatida olindi."), true);
                         taxPaid = true;
                    }

                    if (taxPaid) {
                        world.playSound(null, _player.blockPosition(), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.chest.close")), SoundSource.PLAYERS, 1.0F, 1.0F);
                    }
                }
            }
        }
    }

    // Yordamchi funksiya
    private static boolean removeOneItem(Player player, net.minecraft.world.item.Item item) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.getItem() == item) {
                stack.shrink(1);
                return true;
            }
        }
        return false;
    }
}