package com.frostpunk.mod.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.frostpunk.mod.FrostpunkMod;
import com.frostpunk.mod.world.TemperatureManager;
import com.frostpunk.mod.world.BlizzardSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

/**
 * HUD система с отображением температуры и статуса метели
 */
@Mod.EventBusSubscriber(modid = FrostpunkMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class FrostpunkHUD {

    @SubscribeEvent
    public static void onRenderGuiLayer(ScreenEvent.Init.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) return;

        double temperature = TemperatureManager.getWorldTemperature(minecraft.level);
        double blizzardIntensity = BlizzardSystem.getBlizzardIntensity();

        // Отображение информации о температуре
        String tempText = String.format("§f🌡 Температура: §b%.1f°C", temperature);
        
        // Цвет зависит от температуры
        if (temperature < -40) {
            tempText = String.format("§4§l🌡 Температура: %.1f°C (КРИТИЧНО)", temperature);
        } else if (temperature < -20) {
            tempText = String.format("§c🌡 Температура: §6%.1f°C", temperature);
        } else if (temperature < 0) {
            tempText = String.format("§9🌡 Температура: §b%.1f°C", temperature);
        }

        // Отображение статуса метели
        if (BlizzardSystem.isBlizzardActive()) {
            String blizzardText = String.format("§c§l❄ МЕТЕЛЬ: %.0f%%", blizzardIntensity * 100);
            minecraft.gui.getChat().addMessage(Component.literal(blizzardText));
        }
    }
}