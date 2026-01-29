package net.mcreator.qoidalar.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.level.Level; // MUHIM: LevelAccessor o'rniga Level
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.StructureTags;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;

import net.mcreator.qoidalar.init.QoidalarModItems;

@Mod.EventBusSubscriber
public class PropiskaNazoratiProcedure {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Player _player = event.player;
            // TUZATISH: LevelAccessor o'rniga Level ishlatamiz
            Level world = _player.level();

            // Har 100 tikda (5 soniyada) bir marta tekshiramiz
            // Endi world.getGameTime() xato bermaydi
            if (!world.isClientSide() && world.getGameTime() % 100 == 0) {
                // Creative rejimdagi o'yinchilarga tegmaymiz
                if (_player.isCreative() || _player.isSpectator()) return;

                BlockPos pos = _player.blockPosition();
                
                if (world instanceof ServerLevel _serverLevel) {
                    // 1. O'yinchi QISHLOQ (Village) ichidami?
                    if (_serverLevel.structureManager().getStructureWithPieceAt(pos, StructureTags.VILLAGE).isValid()) {
                        
                        // 2. Pasporti bormi?
                        boolean hasPasport = false;
                        for (int i = 0; i < _player.getInventory().getContainerSize(); i++) {
                            ItemStack stack = _player.getInventory().getItem(i);
                            if (stack.getItem() == QoidalarModItems.PASPORT.get()) { 
                                hasPasport = true;
                                break;
                            }
                        }

                        // 3. Agar pasporti bo'lmasa - REYD!
                        if (!hasPasport) {
                            _player.displayClientMessage(Component.literal("§c[IIB]: §fFuqaro! Qishloq hududida propiskangiz yo'q!"), true);
                            _player.displayClientMessage(Component.literal("§4[Tizim]: §fKonstitutsiya 22-modda buzildi! Pasportsiz yurmang!"), false);
                            
                            _player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0)); 
                            _player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1)); 

                            // 4. Jazo: Temir Golem
                            if (world.getEntitiesOfClass(IronGolem.class, _player.getBoundingBox().inflate(20)).isEmpty()) {
                                
                                world.playSound(null, pos, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.villager.no")), SoundSource.HOSTILE, 1.0F, 1.0F);

                                IronGolem politsiya = EntityType.IRON_GOLEM.create(_serverLevel);
                                if (politsiya != null) {
                                    politsiya.moveTo(pos.getX() + 3, pos.getY() + 1, pos.getZ() + 3, 0, 0);
                                    politsiya.setCustomName(Component.literal("§cIIB Xodimi"));
                                    politsiya.setCustomNameVisible(true);
                                    politsiya.setTarget(_player);
                                    _serverLevel.addFreshEntity(politsiya);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}