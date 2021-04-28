package com.bespectacled.modernbeta.api.registry;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import com.bespectacled.modernbeta.api.gui.AbstractWorldScreenProvider;
import com.bespectacled.modernbeta.api.world.WorldProvider;
import com.bespectacled.modernbeta.api.world.biome.AbstractBiomeProvider;
import com.bespectacled.modernbeta.api.world.gen.AbstractChunkProvider;
import com.bespectacled.modernbeta.util.PentaFunction;
import com.bespectacled.modernbeta.util.QuadFunction;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public class ProviderRegistries {
    public static final ProviderRegistry<BiFunction<Long, CompoundTag, AbstractBiomeProvider>> BIOME;
    public static final ProviderRegistry<Function<AbstractWorldScreenProvider, Screen>> BIOME_SCREEN;
    public static final ProviderRegistry<QuadFunction<Long, ChunkGenerator, Supplier<ChunkGeneratorSettings>, CompoundTag, AbstractChunkProvider>> CHUNK;
    public static final ProviderRegistry<Supplier<CompoundTag>> CHUNK_SETTINGS;
    public static final ProviderRegistry<WorldProvider> WORLD;
    public static final ProviderRegistry<PentaFunction<CreateWorldScreen, DynamicRegistryManager, CompoundTag, CompoundTag, BiConsumer<CompoundTag, CompoundTag>, AbstractWorldScreenProvider>> WORLD_SCREEN;
    
    static {
        BIOME = new ProviderRegistry<>();
        BIOME_SCREEN = new ProviderRegistry<>();
        CHUNK = new ProviderRegistry<>();
        CHUNK_SETTINGS = new ProviderRegistry<>();
        WORLD = new ProviderRegistry<>();
        WORLD_SCREEN = new ProviderRegistry<>();
    }
}
