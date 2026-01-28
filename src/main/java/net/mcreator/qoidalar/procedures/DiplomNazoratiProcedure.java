package net.mcreator.qoidalar.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;

import net.mcreator.qoidalar.init.QoidalarModItems;
import net.mcreator.qoidalar.init.QoidalarModEntities;
import net.mcreator.qoidalar.entity.RaisEntity;
import net.mcreator.qoidalar.QoidalarMod;

@Mod.EventBusSubscriber
public class DiplomNazoratiProcedure {
    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        // TUZATISH: instanceof tekshiruvi olib tashlandi, chunki event.getEntity() baribir Player qaytaradi
        Player _player = event.getEntity();
        
        if (_player != null) {
            LevelAccessor world = event.getLevel();
            BlockPos pos = event.getPos();
            Block clickedBlock = world.getBlockState(pos).getBlock();

            // 1. Diplom talab qilinadigan "Ilmiy Uskunalar" ro'yxati
            boolean isScientificBlock = 
                   clickedBlock == Blocks.ENCHANTING_TABLE 
                || clickedBlock == Blocks.ANVIL 
                || clickedBlock == Blocks.CHIPPED_ANVIL 
                || clickedBlock == Blocks.DAMAGED_ANVIL
                || clickedBlock == Blocks.BREWING_STAND
                || clickedBlock == Blocks.LECTERN
                || clickedBlock == Blocks.CARTOGRAPHY_TABLE
                || clickedBlock == Blocks.SMITHING_TABLE
                || clickedBlock == Blocks.GRINDSTONE;

            if (isScientificBlock) {
                // Creative rejimdagi o'yinchilarga tegmaymiz
                if (_player.isCreative() || _player.isSpectator()) return;

                // 2. Inventarda "Diplom" borligini tekshirish
                boolean hasDiploma = false;
                for (int i = 0; i < _player.getInventory().getContainerSize(); i++) {
                    ItemStack stack = _player.getInventory().getItem(i);
                    if (stack.getItem() == QoidalarModItems.DIPLOM.get()) { 
                        hasDiploma = true;
                        break;
                    }
                }

                // 3. Agar diplom yo'q bo'lsa - RUXSAT YO'Q!
                if (!hasDiploma) {
                    event.setCanceled(true); // Blokni ochishni bekor qilamiz

                    if (!world.isClientSide() && world instanceof ServerLevel _level) {
                        // A. Ovoz va Xabar
                        _level.playSound(null, pos, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.villager.no")), SoundSource.PLAYERS, 1.0F, 1.0F);
                        _player.displayClientMessage(Component.literal("§c[Ta'lim Vazirligi]: §fSizda Oliy Ma'lumot (Diplom) yo'q!"), true);
                        
                        // B. Title (Ekranning o'rtasiga yozuv)
                        _level.getServer().getCommands().performPrefixedCommand(_player.createCommandSourceStack(), "title @p actionbar {\"text\":\"Ilmiy ish qilish uchun DIPLOM kerak!\",\"color\":\"red\"}");

                        // C. Otam (Rais) paydo bo'lib jazolaydi
                        // Har safar emas, 50% ehtimol bilan chiqsin
                        if (Math.random() < 0.5 && _level.getEntitiesOfClass(RaisEntity.class, _player.getBoundingBox().inflate(15)).isEmpty()) {
                            RaisEntity _otam = (RaisEntity) QoidalarModEntities.RAIS.get().spawn(_level, pos.north(), MobSpawnType.MOB_SUMMONED);
                            if (_otam != null) {
                                _otam.setTarget(_player);
                                _player.displayClientMessage(Component.literal("§6[Otam]: §fO'qimagan - chiziq tortadi! Borib kontraktni to'lab, o'qib kel!"), false);
                                
                                // 7 soniyadan keyin yo'qoladi
                                QoidalarMod.queueServerWork(140, () -> { 
                                    if (_otam.isAlive()) _otam.discard(); 
                                });
                            }
                        }
                    }
                }
            }
        }
    }
}