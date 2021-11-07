package com.bespectacled.modernbeta.world.biome.provider.climate;

import com.bespectacled.modernbeta.api.world.biome.climate.ClimateBiomeRules;
import com.bespectacled.modernbeta.api.world.biome.climate.ClimateSampler;
import com.bespectacled.modernbeta.api.world.biome.climate.Clime;
import com.bespectacled.modernbeta.util.chunk.ChunkCache;
import com.bespectacled.modernbeta.util.chunk.ClimateChunk;
import com.bespectacled.modernbeta.util.chunk.FullResBiomeChunk;
import com.bespectacled.modernbeta.util.chunk.LowResBiomeChunk;
import com.bespectacled.modernbeta.world.biome.vanilla.VanillaBiomeSource;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.biome.source.BiomeAccess;

public class VanillaClimateSampler implements ClimateSampler, BiomeAccess.Storage {
    private static final int BLEND_DIST = 5;
    private static final double BLEND_AREA = Math.pow(BLEND_DIST * 2 + 1, 2);
    
    private final VanillaBiomeSource biomeSource;
    private final BiomeAccess biomeAccess;
    
    private final ChunkCache<LowResBiomeChunk> lowResBiomeCache;
    private final ChunkCache<FullResBiomeChunk> fullResBiomeCache;
    private final ChunkCache<ClimateChunk> climateCache;
    private final ClimateBiomeRules climateRules;
    
    public VanillaClimateSampler(VanillaBiomeSource biomeSource, Registry<Biome> biomeRegistry) {
        this.biomeSource = biomeSource;
        this.biomeAccess = new BiomeAccess(this, biomeSource.getSeed());
        
        this.lowResBiomeCache = new ChunkCache<>(
            "biome_low_res",
            1536,
            true,
            (chunkX, chunkZ) -> new LowResBiomeChunk(chunkX, chunkZ, this::sampleBiomeLowRes)
        );
        
        this.fullResBiomeCache = new ChunkCache<>(
            "biome_full_res",
            1536,
            true,
            (chunkX, chunkZ) -> new FullResBiomeChunk(chunkX, chunkZ, this::sampleBiomeFullRes)
        );
        
        this.climateCache = new ChunkCache<>(
            "climate",
            1536,
            true,
            (chunkX, chunkZ) -> new ClimateChunk(chunkX, chunkZ, this::blendBiomeClimate)
        );

        this.climateRules = new ClimateBiomeRules.Builder()
            .add(biome -> biome.getCategory() == Category.EXTREME_HILLS, () -> new Clime(1.0, 1.0))
            .add(biome -> biome.getCategory() == Category.MOUNTAIN, () -> new Clime(1.0, 1.0))
            .add(biome -> biome.getCategory() == Category.MESA, () -> new Clime(1.0, 1.0))
            .add(biome -> biome.getCategory() == Category.SWAMP, () -> new Clime(0.0, 0.0))
            .build();
    }
    
    @Override
    public Clime sampleClime(int x, int z) {
        int chunkX = x >> 4;
        int chunkZ = z >> 4;
        
        return this.climateCache.get(chunkX, chunkZ).sampleClime(x, z);
    }

    @Override
    public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        int chunkX = biomeX >> 2;
        int chunkZ = biomeZ >> 2;
        
        return this.lowResBiomeCache.get(chunkX, chunkZ).sampleBiome(biomeX, biomeZ);
    }
    
    public VanillaBiomeSource getBiomeSource() {
        return this.biomeSource;
    }
    
    private Biome sampleBiomeFullRes(int x, int z) {
        return this.biomeAccess.getBiome(new BlockPos(x, 0, z));
    }
    
    private Biome sampleBiomeLowRes(int biomeX, int biomeZ) {
        return this.biomeSource.getBiome(biomeX, 0, biomeZ);
    }
    
    private Clime blendBiomeClimate(int x, int z) {
        double temp = 0.0;
        double rain = 0.0;

        // Sample surrounding blocks
        for (int localX = -BLEND_DIST; localX <= BLEND_DIST; ++localX) {
            for (int localZ = -BLEND_DIST; localZ <= BLEND_DIST; ++localZ) {
                int curX = localX + x;
                int curZ = localZ + z;
                
                int chunkX = curX >> 4;
                int chunkZ = curZ >> 4;
                
                Biome biome = this.fullResBiomeCache.get(chunkX, chunkZ).sampleBiome(curX, curZ);
                Clime clime = this.climateRules.apply(biome);
                
                temp += clime.temp();
                rain += clime.rain();
            }
        }
        
        // Take simple average of temperature/rainfall values of surrounding blocks.
        temp /= BLEND_AREA;
        rain /= BLEND_AREA;
        
        temp = MathHelper.clamp(temp, 0.0, 1.0);
        rain = MathHelper.clamp(rain, 0.0, 1.0);
        
        return new Clime(temp, rain);
    }
}