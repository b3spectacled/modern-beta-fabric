package com.bespectacled.modernbeta.api.registry;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import com.bespectacled.modernbeta.api.world.WorldProvider;
import com.bespectacled.modernbeta.api.world.biome.BiomeProvider;
import com.bespectacled.modernbeta.api.world.cavebiome.CaveBiomeProvider;
import com.bespectacled.modernbeta.api.world.gen.ChunkProvider;
import com.bespectacled.modernbeta.client.gui.screen.WorldScreen;
import com.bespectacled.modernbeta.util.function.TriFunction;
import com.bespectacled.modernbeta.util.noise.NoiseRules;
import com.bespectacled.modernbeta.util.settings.Settings;
import com.bespectacled.modernbeta.util.settings.WorldSettings.WorldSetting;
import com.bespectacled.modernbeta.world.gen.OldChunkGenerator;
import com.bespectacled.modernbeta.world.gen.sampler.OreVeinType;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.world.biome.Biome;

public final class Registries {
    public static final Registry<TriFunction<Long, Settings, net.minecraft.util.registry.Registry<Biome>, BiomeProvider>> BIOME;
    public static final Registry<BiFunction<WorldScreen, WorldSetting, Screen>> BIOME_SCREEN;
    public static final Registry<Supplier<Settings>> BIOME_SETTINGS;
    public static final Registry<TriFunction<Long, Settings, net.minecraft.util.registry.Registry<Biome>, CaveBiomeProvider>> CAVE_BIOME;
    public static final Registry<BiFunction<WorldScreen, WorldSetting, Screen>> CAVE_BIOME_SCREEN;
    public static final Registry<Supplier<Settings>> CAVE_BIOME_SETTINGS;
    public static final Registry<Function<OldChunkGenerator, ChunkProvider>> CHUNK;
    public static final Registry<Supplier<Settings>> CHUNK_SETTINGS;
    public static final Registry<WorldProvider> WORLD;
    public static final Registry<BiFunction<WorldScreen, WorldSetting, Screen>> WORLD_SCREEN;
    public static final Registry<NoiseRules<OreVeinType>> ORE_VEIN_RULES;
    
    static {
        BIOME = new Registry<>("BIOME");
        BIOME_SCREEN = new Registry<>("BIOME_SCREEN");
        BIOME_SETTINGS = new Registry<>("BIOME_SETTINGS");
        CAVE_BIOME = new Registry<>("CAVE_BIOME");
        CAVE_BIOME_SCREEN = new Registry<>("CAVE_BIOME_SCREEN");
        CAVE_BIOME_SETTINGS = new Registry<>("CAVE_BIOME_SETTINGS");
        CHUNK = new Registry<>("CHUNK");
        CHUNK_SETTINGS = new Registry<>("CHUNK_SETTINGS");
        WORLD = new Registry<>("WORLD");
        WORLD_SCREEN = new Registry<>("WORLD_SCREEN");
        ORE_VEIN_RULES = new Registry<>("ORE_VEIN_RULES");
    }
}
