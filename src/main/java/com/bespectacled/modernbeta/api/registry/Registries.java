package com.bespectacled.modernbeta.api.registry;

import java.util.function.Consumer;
import java.util.function.Function;

import com.bespectacled.modernbeta.api.client.gui.screen.WorldScreen;
import com.bespectacled.modernbeta.api.world.WorldProvider;
import com.bespectacled.modernbeta.api.world.WorldSettings;
import com.bespectacled.modernbeta.api.world.biome.BiomeProvider;
import com.bespectacled.modernbeta.api.world.gen.ChunkProvider;
import com.bespectacled.modernbeta.util.function.TriFunction;
import com.bespectacled.modernbeta.world.biome.OldBiomeSource;
import com.bespectacled.modernbeta.world.gen.OldChunkGenerator;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;

public final class Registries {
    public static final Registry<Function<OldBiomeSource, BiomeProvider>> BIOME;
    public static final Registry<Function<WorldScreen, Screen>> BIOME_SCREEN;
    public static final Registry<Function<OldChunkGenerator, ChunkProvider>> CHUNK;
    public static final Registry<WorldProvider> WORLD;
    public static final Registry<TriFunction<CreateWorldScreen, WorldSettings, Consumer<WorldSettings>, WorldScreen>> WORLD_SCREEN;
    
    static {
        BIOME = new Registry<>("BIOME");
        BIOME_SCREEN = new Registry<>("BIOME_SCREEN");
        CHUNK = new Registry<>("CHUNK");
        WORLD = new Registry<>("WORLD");
        WORLD_SCREEN = new Registry<>("WORLD_SCREEN");
    }
}
