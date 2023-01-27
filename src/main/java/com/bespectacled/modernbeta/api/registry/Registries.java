package com.bespectacled.modernbeta.api.registry;

import java.util.function.Function;
import java.util.function.Supplier;

import com.bespectacled.modernbeta.api.world.biome.BiomeProvider;
import com.bespectacled.modernbeta.api.world.cavebiome.CaveBiomeProvider;
import com.bespectacled.modernbeta.api.world.gen.ChunkProvider;
import com.bespectacled.modernbeta.api.world.gen.noise.NoisePostProcessor;
import com.bespectacled.modernbeta.util.function.TriFunction;
import com.bespectacled.modernbeta.util.settings.Settings;
import com.bespectacled.modernbeta.world.gen.ModernBetaChunkGenerator;

import net.minecraft.world.biome.Biome;

public final class Registries {
    public static final Registry<TriFunction<Long, Settings, net.minecraft.util.registry.Registry<Biome>, BiomeProvider>> BIOME;
    public static final Registry<Supplier<Settings>> BIOME_SETTINGS;
    public static final Registry<TriFunction<Long, Settings, net.minecraft.util.registry.Registry<Biome>, CaveBiomeProvider>> CAVE_BIOME;
    public static final Registry<Supplier<Settings>> CAVE_BIOME_SETTINGS;
    public static final Registry<Function<ModernBetaChunkGenerator, ChunkProvider>> CHUNK;
    public static final Registry<Supplier<Settings>> CHUNK_SETTINGS;
    public static final Registry<NoisePostProcessor> NOISE_POST_PROCESSORS;
    
    static {
        BIOME = new Registry<>("BIOME");
        BIOME_SETTINGS = new Registry<>("BIOME_SETTINGS");
        CAVE_BIOME = new Registry<>("CAVE_BIOME");
        CAVE_BIOME_SETTINGS = new Registry<>("CAVE_BIOME_SETTINGS");
        CHUNK = new Registry<>("CHUNK");
        CHUNK_SETTINGS = new Registry<>("CHUNK_SETTINGS");
        NOISE_POST_PROCESSORS = new Registry<>("NOISE_POST_PROCESSOR");
    }
}
