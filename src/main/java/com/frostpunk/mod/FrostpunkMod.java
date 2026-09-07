package com.frostpunk.mod;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafxmod.FXModLanguageProvider;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafxmod.FXModLanguageProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.IModLoadingState;
import net.minecraftforge.fml.ModLoadingPhase;
import net.minecraftforge.fml.javafxmod.FXModLanguageProvider;

@Mod(FrostpunkMod.MOD_ID)
public class FrostpunkMod {
    public static final String MOD_ID = "frostpunk_mod";
    private static final Logger LOGGER = LogUtils.getLogger();

    public FrostpunkMod(IEventBus modEventBus) {
        modEventBus.addListener(this::commonSetup);
        ModLoadingContext.getInstance().registerConfig(ModConfig.Type.COMMON, FrostpunkConfig.COMMON_SPEC);
        ModLoadingContext.getInstance().registerConfig(ModConfig.Type.CLIENT, FrostpunkConfig.CLIENT_SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Frostpunk Mod initialized! Preparing for eternal winter...");
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            LOGGER.info("Client setup for Frostpunk Mod");
        }
    }
}