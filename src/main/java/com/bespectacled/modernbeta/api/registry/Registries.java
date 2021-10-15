package com.bespectacled.modernbeta.api.registry;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.bespectacled.modernbeta.api.client.gui.screen.WorldScreen;
import com.bespectacled.modernbeta.api.world.WorldProvider;
import com.bespectacled.modernbeta.api.world.WorldSettings;
import com.bespectacled.modernbeta.api.world.biome.BiomeProvider;
import com.bespectacled.modernbeta.api.world.gen.ChunkProvider;
import com.bespectacled.modernbeta.util.function.TriFunction;
import com.bespectacled.modernbeta.world.gen.OldChunkGenerator;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.biome.Biome;

public final class Registries {
    public static final Registry<TriFunction<Long, NbtCompound, net.minecraft.util.registry.Registry<Biome>, BiomeProvider>> BIOME;
    public static final Registry<Function<WorldScreen, Screen>> BIOME_SCREEN;
    public static final Registry<Supplier<NbtCompound>> BIOME_SETTINGS;
    public static final Registry<Function<OldChunkGenerator, ChunkProvider>> CHUNK;
    public static final Registry<Supplier<NbtCompound>> CHUNK_SETTINGS;
    public static final Registry<WorldProvider> WORLD;
    public static final Registry<TriFunction<CreateWorldScreen, WorldSettings, Consumer<WorldSettings>, WorldScreen>> WORLD_SCREEN;
    
    static {
        BIOME = new Registry<>("BIOME");
        BIOME_SCREEN = new Registry<>("BIOME_SCREEN");
        BIOME_SETTINGS = new Registry<>("BIOME_SETTINGS");
        CHUNK = new Registry<>("CHUNK");
        CHUNK_SETTINGS = new Registry<>("CHUNK_SETTINGS");
        WORLD = new Registry<>("WORLD");
        WORLD_SCREEN = new Registry<>("WORLD_SCREEN");
    }
}
