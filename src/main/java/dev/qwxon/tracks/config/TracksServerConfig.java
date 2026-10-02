/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.neoforged.neoforge.common.ModConfigSpec
 *  net.neoforged.neoforge.common.ModConfigSpec$BooleanValue
 *  net.neoforged.neoforge.common.ModConfigSpec$Builder
 */
package dev.qwxon.tracks.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class TracksServerConfig {
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.BooleanValue ENABLE_RENDER_TUNING_CHEATS;
    public static final ModConfigSpec.BooleanValue ENABLE_TERRAIN_DEBUG_VISUALIZATION;

    public static boolean renderTuningCheatsEnabled() {
        return (Boolean)ENABLE_RENDER_TUNING_CHEATS.get();
    }

    public static boolean terrainDebugVisualizationEnabled() {
        return (Boolean)ENABLE_TERRAIN_DEBUG_VISUALIZATION.get();
    }

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        builder.push("server");
        ENABLE_RENDER_TUNING_CHEATS = builder.comment("Allows operators to open the in-game tracks render tuning menu with J. Disabled by default.").define("enableRenderTuningCheats", false);
        ENABLE_TERRAIN_DEBUG_VISUALIZATION = builder.comment("Shows debug particles for drive wheel centers, terrain raycasts and terrain hit results. Disabled by default.").define("enableTerrainDebugVisualization", false);
        builder.pop();
        SPEC = builder.build();
    }
}

