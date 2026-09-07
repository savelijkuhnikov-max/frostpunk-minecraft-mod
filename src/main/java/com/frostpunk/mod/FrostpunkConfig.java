package com.frostpunk.mod;

import net.minecraftforge.common.ForgeConfigSpec;

public class FrostpunkConfig {
    public static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec COMMON_SPEC;
    public static final ForgeConfigSpec CLIENT_SPEC;

    // Common Config
    public static ForgeConfigSpec.DoubleValue BLIZZARD_FREQUENCY;
    public static ForgeConfigSpec.DoubleValue TEMPERATURE_DROP_RATE;
    public static ForgeConfigSpec.IntValue MIN_TEMPERATURE;
    public static ForgeConfigSpec.BooleanValue ENABLE_FROSTBITE;
    public static ForgeConfigSpec.BooleanValue ENABLE_PERMAFROST;

    // Client Config
    public static ForgeConfigSpec.BooleanValue SHOW_TEMPERATURE_HUD;
    public static ForgeConfigSpec.BooleanValue SHOW_BLIZZARD_WARNING;

    static {
        COMMON_BUILDER.push("Blizzard Settings");
        BLIZZARD_FREQUENCY = COMMON_BUILDER
                .comment("How often blizzards occur (0.0-1.0)")
                .defineInRange("blizzard_frequency", 0.15, 0.0, 1.0);
        
        COMMON_BUILDER.pop();
        COMMON_BUILDER.push("Temperature Settings");
        TEMPERATURE_DROP_RATE = COMMON_BUILDER
                .comment("How fast temperature drops")
                .defineInRange("temperature_drop_rate", 0.5, 0.1, 2.0);
        
        MIN_TEMPERATURE = COMMON_BUILDER
                .comment("Minimum world temperature in Celsius")
                .defineInRange("min_temperature", -60, -100, 0);
        
        COMMON_BUILDER.pop();
        COMMON_BUILDER.push("Survival Mechanics");
        ENABLE_FROSTBITE = COMMON_BUILDER
                .comment("Enable frostbite damage to players")
                .define("enable_frostbite", true);
        
        ENABLE_PERMAFROST = COMMON_BUILDER
                .comment("Enable permafrost ground freezing")
                .define("enable_permafrost", true);
        
        COMMON_BUILDER.pop();

        CLIENT_BUILDER.push("HUD Settings");
        SHOW_TEMPERATURE_HUD = CLIENT_BUILDER
                .comment("Show temperature indicator on HUD")
                .define("show_temperature_hud", true);
        
        SHOW_BLIZZARD_WARNING = CLIENT_BUILDER
                .comment("Show blizzard warning on screen")
                .define("show_blizzard_warning", true);
        
        CLIENT_BUILDER.pop();

        COMMON_SPEC = COMMON_BUILDER.build();
        CLIENT_SPEC = CLIENT_BUILDER.build();
    }
}