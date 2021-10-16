package com.bespectacled.modernbeta.world.biome.provider;

import java.util.List;
import java.util.stream.Collectors;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.world.biome.BiomeResolver;
import com.bespectacled.modernbeta.api.world.biome.ClimateBiomeProvider;
import com.bespectacled.modernbeta.api.world.biome.climate.ClimateType;
import com.bespectacled.modernbeta.api.world.biome.climate.Clime;
import com.bespectacled.modernbeta.world.biome.provider.climate.BetaClimateMap;
import com.bespectacled.modernbeta.world.biome.provider.climate.PEClimateSampler;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public class PEBiomeProvider extends ClimateBiomeProvider implements BiomeResolver {
    private final BetaClimateMap climateMap;
    
    public PEBiomeProvider(long seed, NbtCompound settings, Registry<Biome> biomeRegistry) {
        super(seed, settings, biomeRegistry, new PEClimateSampler(seed), new PEClimateSampler(seed));
        
        this.climateMap = new BetaClimateMap(settings);
    }

    @Override
    public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        int x = biomeX << 2;
        int z = biomeZ << 2;
        
        Clime clime = this.getClimateSampler().sampleClime(x, z);
        double temp = clime.temp();
        double rain = clime.rain();
        
        return this.biomeRegistry.get(this.climateMap.getBiome(temp, rain, ClimateType.LAND));
    }
 
    @Override
    public Biome getOceanBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        int x = biomeX << 2;
        int z = biomeZ << 2;
        
        Clime clime = this.getClimateSampler().sampleClime(x, z);
        double temp = clime.temp();
        double rain = clime.rain();
        
        return this.biomeRegistry.get(this.climateMap.getBiome(temp, rain, ClimateType.OCEAN));
    }
    
    @Override
    public Biome getBiome(int x, int y, int z) {
        Clime clime = this.getClimateSampler().sampleClime(x, z);
        double temp = clime.temp();
        double rain = clime.rain();
        
        return this.biomeRegistry.get(this.climateMap.getBiome(temp, rain, ClimateType.LAND));
    }

    @Override
    public List<RegistryKey<Biome>> getBiomesForRegistry() {
        return this.climateMap.getBiomeIds().stream().map(i -> RegistryKey.of(Registry.BIOME_KEY, i)).collect(Collectors.toList());
    }
    
    @Override
    public boolean sampleBiomeColor() {
        return ModernBeta.RENDER_CONFIG.biomeColorConfig.renderPEBetaBiomeColor;
    }
    
    @Override
    public boolean sampleSkyColor() {
        return ModernBeta.RENDER_CONFIG.biomeColorConfig.renderPEBetaSkyColor;
    }
}
