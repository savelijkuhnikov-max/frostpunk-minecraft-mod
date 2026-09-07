package com.frostpunk.mod.survival;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.TickEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import com.frostpunk.mod.FrostpunkMod;
import com.frostpunk.mod.world.TemperatureManager;
import com.frostpunk.mod.world.BlizzardSystem;

/**
 * Система переохлаждения и обморожения
 */
@Mod.EventBusSubscriber(modid = FrostpunkMod.MOD_ID)
public class FrostbiteSystem {
    
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (event.player.level().isClientSide) return;

        Player player = event.player;
        double temperature = TemperatureManager.getWorldTemperature(player.level());
        double blizzardIntensity = BlizzardSystem.getBlizzardIntensity();

        // Эффективная температура с учётом метели
        double effectiveTemp = temperature - (blizzardIntensity * 20);

        applyFrostEffects(player, effectiveTemp, blizzardIntensity);
    }

    private static void applyFrostEffects(Player player, double temperature, double blizzardIntensity) {
        int warmthLevel = getWarmthLevel(player);
        
        // Настолько холодно, что даже броня не спасает
        if (temperature < -40 && warmthLevel < 2) {
            player.hurt(player.damageSources().freeze(), 2.0f);
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 1));
        }
        // Очень холодно
        else if (temperature < -20 && warmthLevel < 1) {
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 30, 0));
            
            if (player.getRandom().nextDouble() < 0.05) {
                player.hurt(player.damageSources().freeze(), 1.0f);
            }
        }
        // Холодно
        else if (temperature < 0 && warmthLevel < 1) {
            player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 100, 0));
        }

        // Эффекты метели
        if (blizzardIntensity > 0.5) {
            player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 20, 0));
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 0));
            
            // Слепота усиливается с интенсивностью метели
            if (blizzardIntensity > 0.8) {
                player.hurt(player.damageSources().freeze(), 0.5f);
            }
        }
    }

    /**
     * Определяет уровень теплоты из экипировки
     * 0 - нет защиты
     * 1 - базовая защита
     * 2 - полная защита
     */
    private static int getWarmthLevel(Player player) {
        int warmth = 0;

        // Проверяем надетую броню
        ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack chestplate = player.getItemBySlot(EquipmentSlot.CHEST);
        ItemStack leggings = player.getItemBySlot(EquipmentSlot.LEGS);
        ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);

        if (!helmet.isEmpty()) warmth++;
        if (!chestplate.isEmpty()) warmth++;
        if (!leggings.isEmpty()) warmth++;
        if (!boots.isEmpty()) warmth++;

        return Math.min(warmth, 2);
    }
}