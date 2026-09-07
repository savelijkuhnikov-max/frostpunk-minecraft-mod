package com.frostpunk.mod.world;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.util.Mth;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import com.frostpunk.mod.FrostpunkMod;

/**
 * Система метелей - основной механизм хардкорности мода
 */
@Mod.EventBusSubscriber(modid = FrostpunkMod.MOD_ID)
public class BlizzardSystem {
    
    private static double blizzardIntensity = 0.0; // 0-1
    private static long blizzardStartTime = 0;
    private static boolean isBlizzardActive = false;
    private static int blizzardDuration = 0;
    private static int timeSinceLastBlizzard = 0;

    private static final int MIN_BLIZZARD_INTERVAL = 400; // 20 сек
    private static final int MAX_BLIZZARD_INTERVAL = 1200; // 60 сек
    private static final int MIN_BLIZZARD_DURATION = 200; // 10 сек
    private static final int MAX_BLIZZARD_DURATION = 800; // 40 сек

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        for (var server : event.getServer().getAllLevels()) {
            updateBlizzard(server);
        }
    }

    private static void updateBlizzard(Level level) {
        timeSinceLastBlizzard++;

        if (!isBlizzardActive) {
            // Проверяем начало новой метели
            if (timeSinceLastBlizzard > MAX_BLIZZARD_INTERVAL) {
                if (level.random.nextDouble() < 0.1) { // 10% шанс начать метель
                    startBlizzard(level);
                }
            }
        } else {
            // Обновляем текущую метель
            blizzardIntensity = Math.min(1.0, blizzardIntensity + 0.02);
            
            if (timeSinceLastBlizzard > blizzardDuration) {
                endBlizzard(level);
            }
        }
    }

    private static void startBlizzard(Level level) {
        isBlizzardActive = true;
        blizzardIntensity = 0.0;
        blizzardStartTime = level.getGameTime();
        blizzardDuration = MIN_BLIZZARD_DURATION + 
                          level.random.nextInt(MAX_BLIZZARD_DURATION - MIN_BLIZZARD_DURATION);
        timeSinceLastBlizzard = 0;
        
        // Уведомляем игроков о приближении метели
        level.getPlayers(p -> true).forEach(player -> {
            player.displayClientMessage(
                net.minecraft.network.chat.Component.literal("§c§l⚠ МЕТЕЛЬ ПРИБЛИЖАЕТСЯ! ⚠"),
                false
            );
        });
    }

    private static void endBlizzard(Level level) {
        isBlizzardActive = false;
        blizzardIntensity = 0.0;
        timeSinceLastBlizzard = 0;
    }

    public static double getBlizzardIntensity() {
        return isBlizzardActive ? blizzardIntensity : 0.0;
    }

    public static boolean isBlizzardActive() {
        return isBlizzardActive;
    }

    public static int getBlizzardDuration() {
        return isBlizzardActive ? blizzardDuration : 0;
    }
}