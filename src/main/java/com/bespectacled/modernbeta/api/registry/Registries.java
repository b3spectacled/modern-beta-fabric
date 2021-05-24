package com.bespectacled.modernbeta.api.registry;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.bespectacled.modernbeta.api.gui.WorldScreen;
import com.bespectacled.modernbeta.api.world.WorldProvider;
import com.bespectacled.modernbeta.api.world.WorldSettings;
import com.bespectacled.modernbeta.api.world.biome.BiomeProvider;
import com.bespectacled.modernbeta.api.world.cavebiome.CaveBiomeProvider;
import com.bespectacled.modernbeta.api.world.gen.ChunkProvider;
import com.bespectacled.modernbeta.util.function.QuadFunction;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public final class Registries {
    public static final Registry<BiFunction<Long, NbtCompound, BiomeProvider>> BIOME;
    public static final Registry<Function<WorldScreen, Screen>> BIOME_SCREEN;
    public static final Registry<BiFunction<Long, NbtCompound, CaveBiomeProvider>> CAVE_BIOME;
    public static final Registry<QuadFunction<Long, ChunkGenerator, Supplier<ChunkGeneratorSettings>, NbtCompound, ChunkProvider>> CHUNK;
    public static final Registry<WorldProvider> WORLD;
    public static final Registry<QuadFunction<CreateWorldScreen, DynamicRegistryManager, WorldSettings, Consumer<WorldSettings>, WorldScreen>> WORLD_SCREEN;
    
    static {
        BIOME = new Registry<>("BIOME");
        BIOME_SCREEN = new Registry<>("BIOME_SCREEN");
        CAVE_BIOME = new Registry<>("CAVE_BIOME");
        CHUNK = new Registry<>("CHUNK");
        WORLD = new Registry<>("WORLD");
        WORLD_SCREEN = new Registry<>("WORLD_SCREEN");
    }
}
