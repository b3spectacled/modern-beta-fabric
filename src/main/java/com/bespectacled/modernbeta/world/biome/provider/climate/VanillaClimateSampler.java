package com.bespectacled.modernbeta.world.biome.provider.climate;

import com.bespectacled.modernbeta.api.world.biome.climate.ClimateSampler;
import com.bespectacled.modernbeta.api.world.biome.climate.Clime;
import com.bespectacled.modernbeta.util.chunk.ChunkCache;
import com.bespectacled.modernbeta.util.chunk.ClimateChunk;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeLayerSampler;
import net.minecraft.world.biome.source.HorizontalVoronoiBiomeAccessType;

public class VanillaClimateSampler implements ClimateSampler, BiomeAccess.Storage {
    private static final int BLEND_DIST = 3;
    private static final double BLEND_AREA = Math.pow(BLEND_DIST * 2 + 1, 2);
    
    private final long seed;
    private final BiomeLayerSampler biomeSampler;
    private final Registry<Biome> biomeRegistry;
    
    private final ChunkCache<ClimateChunk> baseClimateCache;
    private final ChunkCache<ClimateChunk> climateCache;
    
    private final VanillaClimateRules climateRules;
    
    public VanillaClimateSampler(long seed, BiomeLayerSampler biomeSampler, Registry<Biome> biomeRegistry) {
        this.seed = seed;
        this.biomeSampler = biomeSampler;
        this.biomeRegistry = biomeRegistry;
        
        this.baseClimateCache = new ChunkCache<>(
            "climate", 
            512, 
            true, 
            (chunkX, chunkZ) -> new ClimateChunk(chunkX, chunkZ, this::sampleBiomeClimate)
        );
        
        this.climateCache = new ChunkCache<>(
            "climate", 
            1536, 
            true, 
            (chunkX, chunkZ) -> new ClimateChunk(chunkX, chunkZ, this::blendBiomeClimate)
        );
        
        this.climateRules = new VanillaClimateRules.Builder()
            .add(biome -> biome.getCategory() == Category.EXTREME_HILLS, () -> new Clime(1.0, 1.0))
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
        return this.biomeSampler.sample(this.biomeRegistry, biomeX, biomeZ);
    }
    
    public BiomeLayerSampler getBiomeSampler() {
        return this.biomeSampler;
    }
    
    private Clime sampleBiomeClimate(int x, int z) {
        Biome biome = HorizontalVoronoiBiomeAccessType.INSTANCE.getBiome(this.seed, x, 0, z, this);
        
        return this.climateRules.apply(biome);
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
                
                Clime clime = this.baseClimateCache.get(chunkX, chunkZ).sampleClime(curX, curZ);
                
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