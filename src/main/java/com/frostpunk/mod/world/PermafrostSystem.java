package com.frostpunk.mod.world;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.TickEvent;
import com.frostpunk.mod.FrostpunkMod;
import net.minecraft.world.level.material.Material;

/**
 * Система вечной мерзлоты - замораживание блоков и воды
 */
@Mod.EventBusSubscriber(modid = FrostpunkMod.MOD_ID)
public class PermafrostSystem {

    private static int freezeCounter = 0;

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        for (var level : event.getServer().getAllLevels()) {
            if (TemperatureManager.getWorldTemperature(level) < -10) {
                freezeBlocks(level);
            }
        }
    }

    private static void freezeBlocks(Level level) {
        freezeCounter++;
        if (freezeCounter < 20) return; // Обновляем каждую секунду для оптимизации
        freezeCounter = 0;

        // Получаем позицию случайного игрока
        if (level.getPlayers(p -> true).isEmpty()) return;

        var player = level.getPlayers(p -> true).get(0);
        BlockPos playerPos = player.blockPosition();

        // Замораживаем блоки в радиусе 64 блока вокруг игрока
        for (int x = playerPos.getX() - 64; x <= playerPos.getX() + 64; x++) {
            for (int z = playerPos.getZ() - 64; z <= playerPos.getZ() + 64; z++) {
                for (int y = playerPos.getY() - 5; y <= playerPos.getY() + 5; y++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    BlockState state = level.getBlockState(pos);

                    // Замораживаем воду
                    if (state.getBlock() == Blocks.WATER) {
                        level.setBlock(pos, Blocks.ICE.defaultBlockState(), 3);
                    }
                    // Замораживаем текущую воду
                    else if (state.getBlock() == Blocks.FLOWING_WATER) {
                        level.setBlock(pos, Blocks.PACKED_ICE.defaultBlockState(), 3);
                    }
                    // Почва становится вечной мерзлотой (снег)
                    else if (state.getBlock() == Blocks.GRASS_BLOCK && level.random.nextDouble() < 0.1) {
                        level.setBlock(pos, Blocks.SNOW_BLOCK.defaultBlockState(), 3);
                    }
                }
            }
        }
    }
}