package com.frostpunk.mod.story;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.TickEvent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import com.frostpunk.mod.FrostpunkMod;
import com.frostpunk.mod.world.TemperatureManager;

/**
 * Система сюжета - отслеживание прогресса и события
 */
@Mod.EventBusSubscriber(modid = FrostpunkMod.MOD_ID)
public class StoryManager {

    private static final String STORY_TAG = "frostpunk_story";
    
    // Этапы сюжета
    private static final int STAGE_INTRO = 0;           // Начало - надвигающийся холод
    private static final int STAGE_FIRST_BLIZZARD = 1;  // Первая метель
    private static final int STAGE_SURVIVAL = 2;        // Борьба за выживание
    private static final int STAGE_ADVANCED = 3;        // Нужна продвинутая тактика

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (event.player.level().isClientSide) return;

        Player player = event.player;
        updateStory(player);
    }

    private static void updateStory(Player player) {
        CompoundTag playerData = player.getPersistentData();
        CompoundTag storyData = playerData.getCompound(STORY_TAG);
        
        int stage = storyData.getInt("stage");
        long firstJoinTime = storyData.getLong("first_join_time");
        
        if (firstJoinTime == 0) {
            // Первый раз заходит в мир
            firstJoinTime = player.level().getGameTime();
            storyData.putLong("first_join_time", firstJoinTime);
            storyData.putInt("stage", STAGE_INTRO);
            playerData.put(STORY_TAG, storyData);
            
            player.displayClientMessage(
                Component.literal("§6§l━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                    "§c§l🌨 ДОБРО ПОЖАЛОВАТЬ В ЛЕДЯНОЙ МИР 🌨\n" +
                    "§f§lЧувствуешь холод? Это только начало...\n" +
                    "§6§l━━━━━━━━━━━━━━━━━━━━━━━━━━━"),
                false
            );
            return;
        }

        long playTime = player.level().getGameTime() - firstJoinTime;
        double temperature = TemperatureManager.getWorldTemperature(player.level());

        // Прогрессия сюжета
        if (stage == STAGE_INTRO && playTime > 1000) {
            // ~50 сек игры - появляется первая метель
            storyData.putInt("stage", STAGE_FIRST_BLIZZARD);
            player.displayClientMessage(
                Component.literal("§c§l⚠ Небо начинает чернеть... Приближается метель! ⚠"),
                false
            );
        }
        
        if (stage == STAGE_FIRST_BLIZZARD && temperature < -10) {
            storyData.putInt("stage", STAGE_SURVIVAL);
            player.displayClientMessage(
                Component.literal("§4§l⚠⚠⚠ МЕТЕЛЬ НАЧАЛАСЬ! ⚠⚠⚠\n" +
                    "§cТемпература критична! Найди убежище немедленно!"),
                false
            );
        }

        if (stage == STAGE_SURVIVAL && temperature < -30) {
            storyData.putInt("stage", STAGE_ADVANCED);
            player.displayClientMessage(
                Component.literal("§1§lТемпература достигла критических значений...\n" +
                    "§9§lТреб хотя бы полная броня и огонь для выживания!"),
                false
            );
        }

        playerData.put(STORY_TAG, storyData);
    }

    public static void showObjective(Player player, String text) {
        player.displayClientMessage(Component.literal("§e§l[ЦЕЛЬ] " + text), false);
    }
}