package com.bespectacled.modernbeta.api.registry;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import com.bespectacled.modernbeta.api.gui.WorldScreen;
import com.bespectacled.modernbeta.api.world.WorldProvider;
import com.bespectacled.modernbeta.api.world.biome.BiomeProvider;
import com.bespectacled.modernbeta.api.world.cavebiome.CaveBiomeProvider;
import com.bespectacled.modernbeta.api.world.gen.ChunkProvider;
import com.bespectacled.modernbeta.util.PentaFunction;
import com.bespectacled.modernbeta.util.QuadFunction;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public final class ProviderRegistries {
    public static final ProviderRegistry<BiFunction<Long, NbtCompound, BiomeProvider>> BIOME;
    public static final ProviderRegistry<Function<String, NbtCompound>> BIOME_SETTINGS;
    public static final ProviderRegistry<Function<WorldScreen, Screen>> BIOME_SCREEN;
    public static final ProviderRegistry<BiFunction<Long, NbtCompound, CaveBiomeProvider>> CAVE_BIOME;
    public static final ProviderRegistry<QuadFunction<Long, ChunkGenerator, Supplier<ChunkGeneratorSettings>, NbtCompound, ChunkProvider>> CHUNK;
    public static final ProviderRegistry<Function<String, NbtCompound>> CHUNK_SETTINGS;
    public static final ProviderRegistry<WorldProvider> WORLD;
    public static final ProviderRegistry<PentaFunction<CreateWorldScreen, DynamicRegistryManager, NbtCompound, NbtCompound, BiConsumer<NbtCompound, NbtCompound>, WorldScreen>> WORLD_SCREEN;
    
    static {
        BIOME = new ProviderRegistry<>("BIOME");
        BIOME_SETTINGS = new ProviderRegistry<>("BIOME_SETTINGS");
        BIOME_SCREEN = new ProviderRegistry<>("BIOME_SCREEN");
        CAVE_BIOME = new ProviderRegistry<>("CAVE_BIOME");
        CHUNK = new ProviderRegistry<>("CHUNK");
        CHUNK_SETTINGS = new ProviderRegistry<>("CHUNK_SETTINGS");
        WORLD = new ProviderRegistry<>("WORLD");
        WORLD_SCREEN = new ProviderRegistry<>("WORLD_SCREEN");
    }
}
