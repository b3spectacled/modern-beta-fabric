package mod.bespectacled.modernbeta.api.registry;

import java.util.function.BiFunction;
import java.util.function.Function;

import mod.bespectacled.modernbeta.api.world.biome.BiomeProvider;
import mod.bespectacled.modernbeta.api.world.cavebiome.CaveBiomeProvider;
import mod.bespectacled.modernbeta.api.world.chunk.ChunkProvider;
import mod.bespectacled.modernbeta.api.world.chunk.SurfaceConfig;
import mod.bespectacled.modernbeta.api.world.chunk.noise.NoisePostProcessor;
import mod.bespectacled.modernbeta.world.chunk.ModernBetaChunkGenerator;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.world.biome.Biome;

public final class ModernBetaRegistries {
    public static final ModernBetaRegistry<Function<ModernBetaChunkGenerator, ChunkProvider>> CHUNK;
    public static final ModernBetaRegistry<BiFunction<NbtCompound, RegistryEntryLookup<Biome>, BiomeProvider>> BIOME;
    public static final ModernBetaRegistry<BiFunction<NbtCompound, RegistryEntryLookup<Biome>, CaveBiomeProvider>> CAVE_BIOME;
    public static final ModernBetaRegistry<NoisePostProcessor> NOISE_POST_PROCESSOR;
    public static final ModernBetaRegistry<SurfaceConfig> SURFACE_CONFIG;
    
    static {
        CHUNK = new ModernBetaRegistry<>("CHUNK");
        BIOME = new ModernBetaRegistry<>("BIOME");
        CAVE_BIOME = new ModernBetaRegistry<>("CAVE_BIOME");
        NOISE_POST_PROCESSOR = new ModernBetaRegistry<>("NOISE_POST_PROCESSOR");
        SURFACE_CONFIG = new ModernBetaRegistry<>("SURFACE_CONFIG");
    }
}
