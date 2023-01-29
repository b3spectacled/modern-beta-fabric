package mod.bespectacled.modernbeta.api.registry;

import java.util.function.Function;

import mod.bespectacled.modernbeta.api.world.biome.BiomeProvider;
import mod.bespectacled.modernbeta.api.world.cavebiome.CaveBiomeProvider;
import mod.bespectacled.modernbeta.api.world.chunk.ChunkProvider;
import mod.bespectacled.modernbeta.api.world.chunk.noise.NoisePostProcessor;
import mod.bespectacled.modernbeta.util.function.TriFunction;
import mod.bespectacled.modernbeta.world.chunk.ModernBetaChunkGenerator;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.biome.Biome;

public final class Registries {
    public static final Registry<Function<ModernBetaChunkGenerator, ChunkProvider>> CHUNK;
    public static final Registry<TriFunction<Long, NbtCompound, net.minecraft.util.registry.Registry<Biome>, BiomeProvider>> BIOME;
    public static final Registry<TriFunction<Long, NbtCompound, net.minecraft.util.registry.Registry<Biome>, CaveBiomeProvider>> CAVE_BIOME;
    public static final Registry<NoisePostProcessor> NOISE_POST_PROCESSORS;
    
    static {
        CHUNK = new Registry<>("CHUNK");
        BIOME = new Registry<>("BIOME");
        CAVE_BIOME = new Registry<>("CAVE_BIOME");
        NOISE_POST_PROCESSORS = new Registry<>("NOISE_POST_PROCESSOR");
    }
}
