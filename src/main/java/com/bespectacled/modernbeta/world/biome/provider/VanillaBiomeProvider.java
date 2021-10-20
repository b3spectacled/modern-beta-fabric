package com.bespectacled.modernbeta.world.biome.provider;

import java.util.List;

import com.bespectacled.modernbeta.api.world.biome.BiomeProvider;
import com.bespectacled.modernbeta.world.biome.vanilla.VanillaBiomeSource;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;

public class VanillaBiomeProvider extends BiomeProvider {
    private final VanillaBiomeSource vanillaBiomeSource;
    private final VanillaBiomeSource oceanBiomeSource;
    private final VanillaBiomeSource deepOceanBiomeSource;
    
    public VanillaBiomeProvider(long seed, NbtCompound settings, Registry<Biome> biomeRegistry) {
        super(seed, settings, biomeRegistry);
        
        this.vanillaBiomeSource = new VanillaBiomeSource.Builder(biomeRegistry, seed)
            .writeMixedBiomes(MultiNoiseUtil.ParameterRange.of(-1.0f, -0.93333334f))
            .writePlainsBiomes(MultiNoiseUtil.ParameterRange.of(-0.93333334f, -0.7666667f))
            .writeMountainousBiomes(MultiNoiseUtil.ParameterRange.of(-0.7666667f, -0.56666666f))
            .writePlainsBiomes(MultiNoiseUtil.ParameterRange.of(-0.56666666f, -0.4f))
            .writeMixedBiomes(MultiNoiseUtil.ParameterRange.of(-0.4f, -0.4f))
            .writePlainsBiomes(MultiNoiseUtil.ParameterRange.of(0.4f, 0.56666666f))
            .writeMountainousBiomes(MultiNoiseUtil.ParameterRange.of(0.56666666f, 0.7666667f))
            .writePlainsBiomes(MultiNoiseUtil.ParameterRange.of(0.7666667f, 0.93333334f))
            .writeMixedBiomes(MultiNoiseUtil.ParameterRange.of(0.93333334f, 1.0f))
            .build();
        
        this.oceanBiomeSource = new VanillaBiomeSource.Builder(biomeRegistry, seed)
            .writeOceanBiomes()
            .build();
        
        this.deepOceanBiomeSource = new VanillaBiomeSource.Builder(biomeRegistry, seed)
            .writeDeepOceanBiomes()
            .build();
    }

    @Override
    public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        return this.vanillaBiomeSource.getBiome(biomeX, biomeY, biomeZ);
    }
    
    @Override
    public Biome getOceanBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        return this.oceanBiomeSource.getBiome(biomeX, biomeY, biomeZ);
    }
    
    /*
     * Extra function to inject deep oceans
     */
    public Biome getDeepOceanBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        return this.deepOceanBiomeSource.getBiome(biomeX, biomeY, biomeZ);
    }
    
    @Override
    public List<Biome> getBiomesForRegistry() {
        return this.vanillaBiomeSource.getBiomeEntries().getEntries().stream().map(p -> p.getSecond().get()).toList();
    }
}
