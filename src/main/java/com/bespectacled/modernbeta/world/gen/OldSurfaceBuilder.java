package com.bespectacled.modernbeta.world.gen;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.compat.CompatBiomes;

import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler.NoiseParameters;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.NoiseColumnSampler;
import net.minecraft.world.gen.chunk.BlockColumn;
import net.minecraft.world.gen.random.AbstractRandom;
import net.minecraft.world.gen.random.ChunkRandom.RandomProvider;
import net.minecraft.world.gen.random.RandomDeriver;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;

public class OldSurfaceBuilder extends SurfaceBuilder {
    // Set for specifying which biomes should use their vanilla surface builders.
    // Done on per-biome basis for best mod compatibility.
    private static final Set<Identifier> BIOMES_WITH_CUSTOM_SURFACES = new HashSet<Identifier>(
        Stream.concat(
            CompatBiomes.BIOMES_WITH_CUSTOM_SURFACES.stream().map(b -> new Identifier(b)), 
            ModernBeta.COMPAT_CONFIG.biomesWithCustomSurfaces.stream().map(b -> new Identifier(b))
        ).toList()
    );
    
    private final RandomDeriver randomDeriver;
    
    public OldSurfaceBuilder(
        NoiseColumnSampler noiseColumnSampler, 
        Registry<NoiseParameters> noiseRegistry, 
        BlockState blockState, 
        int seaLevel, 
        long seed, 
        RandomProvider randomProvider
    ) {
        super(noiseColumnSampler, noiseRegistry, blockState, seaLevel, seed, randomProvider);
        
        this.randomDeriver = randomProvider.create(seed).createBlockPosRandomDeriver();
    }
    
    /*
     * Like buildSurface(), but for a single column
     */
    public boolean buildSurfaceColumn(Registry<Biome> biomeRegistry, BlockColumn column, Chunk chunk, Biome biome, int localX, int localZ) {
        RegistryKey<Biome> biomeKey = biomeRegistry.getKey(biome).orElseThrow(() -> new IllegalStateException("Unregistered biome: " + biome));
        
        if (!BIOMES_WITH_CUSTOM_SURFACES.contains(biomeKey.getValue())) {
            return false;
        }
        
        ChunkPos chunkPos = chunk.getPos();
        int startX = chunkPos.getStartX();
        int startZ = chunkPos.getStartZ();
        
        int x = startX + localX;
        int z = startZ + localZ;
        int y = chunk.sampleHeightmap(Heightmap.Type.WORLD_SURFACE_WG, localX, localZ);
        
        AbstractRandom random = this.randomDeriver.createRandom(x, 0, z);
        
        
        return true;
    }
    
}
