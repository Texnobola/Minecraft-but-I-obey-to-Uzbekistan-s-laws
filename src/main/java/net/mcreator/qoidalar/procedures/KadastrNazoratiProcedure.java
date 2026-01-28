package net.mcreator.qoidalar.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;

import net.mcreator.qoidalar.init.QoidalarModItems;
import net.mcreator.qoidalar.init.QoidalarModEntities;
import net.mcreator.qoidalar.entity.RaisEntity;
import net.mcreator.qoidalar.QoidalarMod;

import java.util.Random;

@Mod.EventBusSubscriber
public class KadastrNazoratiProcedure {
    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        if (event.getEntity() instanceof Player _player) {
            LevelAccessor world = event.getLevel();
            BlockPos pos = event.getPos();
            Block placedBlock = event.getState().getBlock();

            // 1. Kadastr talab qilinadigan bloklar ro'yxati (Muhim mulklar)
            boolean isImportantStructure = 
                   placedBlock == Blocks.CHEST || placedBlock == Blocks.TRAPPED_CHEST || placedBlock == Blocks.ENDER_CHEST
                || placedBlock == Blocks.FURNACE || placedBlock == Blocks.BLAST_FURNACE || placedBlock == Blocks.SMOKER
                || placedBlock == Blocks.CRAFTING_TABLE || placedBlock == Blocks.ANVIL || placedBlock == Blocks.CHIPPED_ANVIL || placedBlock == Blocks.DAMAGED_ANVIL
                || placedBlock == Blocks.ENCHANTING_TABLE
                || event.getState().is(BlockTags.BEDS) 
                || event.getState().is(BlockTags.DOORS)
                || event.getState().is(BlockTags.FENCES)
                || event.getState().is(BlockTags.FENCE_GATES);

            if (isImportantStructure) {
                // 2. O'yinchida Kadastr Hujjati borligini tekshirish
                boolean hasKadastr = false;
                for (int i = 0; i < _player.getInventory().getContainerSize(); i++) {
                    ItemStack stack = _player.getInventory().getItem(i);
                    // QoidalarModItems.KADASTR_HUJJATI - Item nomini o'zingiznikiga moslang
                    if (stack.getItem() == QoidalarModItems.KADASTR_HUJJATI.get()) {
                        hasKadastr = true;
                        break;
                    }
                }

                // Agar hujjati bo'lmasa - SNOS!
                if (!hasKadastr) {
                    if (!world.isClientSide() && world instanceof ServerLevel _level) {
                        // A. Blokni bekor qilish (qo'ydirmaslik)
                        event.setCanceled(true);

                        // B. Ovoz va Effektlar (Qo'rquv uchun)
                        _level.playSound(null, pos, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.tnt.primed")), SoundSource.PLAYERS, 1.0F, 1.0F);
                        _level.sendParticles(ParticleTypes.EXPLOSION, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 3, 0.3, 0.3, 0.3, 0.1);

                        // C. Jarima tizimi (Avtomatik yechib olish)
                        boolean finePaid = false;
                        for (int i = 0; i < _player.getInventory().getContainerSize(); i++) {
                            ItemStack stack = _player.getInventory().getItem(i);
                            if (stack.getItem() == Items.EMERALD) {
                                stack.shrink(1); // 1 ta zumrad jarima
                                finePaid = true;
                                break;
                            }
                        }

                        // D. Xabar chiqarish
                        String jarimaText = finePaid ? "§c[JARIMA]: 1 ta zumrad yechildi." : "§c[JARIMA]: To'lashga pulingiz yo'q! Qarz yozildi!";
                        _player.displayClientMessage(Component.literal("§4[HOKIMIYAT]: §fNoqonuniy qurilish aniqlandi! Kadastr hujjatingiz yo'q! " + jarimaText), false);
                        
                        // E. Qurilishni sekinlashtirish (Jazo)
                        _player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 600, 2)); // 30 soniya Mining Fatigue III

                        // F. Otam (Rais) paydo bo'lishi
                        if (_level.getEntitiesOfClass(RaisEntity.class, _player.getBoundingBox().inflate(30)).isEmpty()) {
                            RaisEntity _otam = (RaisEntity) QoidalarModEntities.RAIS.get().spawn(_level, pos.north(), MobSpawnType.MOB_SUMMONED);
                            if (_otam != null) {
                                _otam.setTarget(_player);
                                
                                // Tasodifiy gaplar
                                String[] gaplar = {
                                    "Hujjat qani, uka? Buz buni!",
                                    "O'zboshimchalik bilan qurilgan imoratlar snosga tushadi!",
                                    "Kadastr qilmaguningcha g'isht qo'ydirmayman!",
                                    "Hokim buva ko'rsalar, hammamizni ishdan oladilar! Buz!",
                                    "Qonun hammaga barobar! Hujjatni olib kel keyin qurasan!"
                                };
                                String randomGap = gaplar[new Random().nextInt(gaplar.length)];
                                
                                _player.displayClientMessage(Component.literal("§6[Otam]: §f" + randomGap), true);
                                
                                // 15 soniyadan keyin yo'qolishi
                                QoidalarMod.queueServerWork(300, () -> { 
                                    if (_otam.isAlive()) _otam.discard(); 
                                });
                            }
                        }
                    }
                } else {
                    // Agar hujjati bo'lsa - Maqtov (kamdan-kam hollarda)
                    if (Math.random() < 0.05) { // 5% ehtimol
                         _player.displayClientMessage(Component.literal("§a[Hokimiyat]: §fQurilish qonuniy. Rahmat!"), true);
                    }
                }
            }
        }
    }
}