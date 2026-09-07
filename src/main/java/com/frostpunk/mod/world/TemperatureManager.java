package com.frostpunk.mod.world;

import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.frostpunk.mod.FrostpunkMod;

/**
 * Управление глобальной температурой мира
 */
@Mod.EventBusSubscriber(modid = FrostpunkMod.MOD_ID)
public class TemperatureManager {
    
    private static double worldTemperature = 20.0;
    private static long lastTemperatureUpdate = 0;
    private static final double TEMPERATURE_UPDATE_INTERVAL = 200; // каждые 10 секунд

    public static double getWorldTemperature(Level level) {
        if (level == null) return worldTemperature;
        
        long gameTime = level.getGameTime();
        if (gameTime - lastTemperatureUpdate > TEMPERATURE_UPDATE_INTERVAL) {
            updateTemperature(level);
            lastTemperatureUpdate = gameTime;
        }
        
        return worldTemperature;
    }

    private static void updateTemperature(Level level) {
        // Максимальная температура зависит от времени суток
        double dayFactor = Math.sin((level.getDayTime() % 24000) / 3819.0);
        double maxTemp = 5.0 + (dayFactor * 10);
        
        // Температура медленно снижается к минимуму
        double minTemp = -40.0;
        double targetTemp = minTemp + (maxTemp - minTemp) * 0.3;
        
        // Постепенное изменение температуры
        double change = (targetTemp - worldTemperature) * 0.05;
        worldTemperature += change;
        
        // Ограничение диапазона
        worldTemperature = Math.max(minTemp, Math.min(maxTemp, worldTemperature));
    }

    public static boolean isFreezingTemperature() {
        return worldTemperature < 0;
    }

    public static boolean isBlizzardTemperature() {
        return worldTemperature < -15;
    }

    public static void setWorldTemperature(double temp) {
        worldTemperature = Math.max(-50, Math.min(30, temp));
    }
}