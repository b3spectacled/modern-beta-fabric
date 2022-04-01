package com.bespectacled.modernbeta.world.biome.provider;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.world.biome.BiomeBlockResolver;
import com.bespectacled.modernbeta.api.world.biome.BiomeProvider;
import com.bespectacled.modernbeta.api.world.biome.OceanBiomeResolver;
import com.bespectacled.modernbeta.api.world.biome.climate.ClimateSampler;
import com.bespectacled.modernbeta.api.world.biome.climate.Clime;
import com.bespectacled.modernbeta.api.world.biome.climate.SkyClimateSampler;
import com.bespectacled.modernbeta.util.chunk.ChunkCache;
import com.bespectacled.modernbeta.util.chunk.ClimateChunk;
import com.bespectacled.modernbeta.util.chunk.SkyClimateChunk;
import com.bespectacled.modernbeta.util.noise.SimplexOctaveNoise;
import com.bespectacled.modernbeta.util.settings.Settings;
import com.bespectacled.modernbeta.world.biome.provider.climate.BetaClimateMap;
import com.bespectacled.modernbeta.world.biome.provider.climate.ClimateMapping.ClimateType;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.biome.Biome;

public class BetaBiomeProvider extends BiomeProvider implements ClimateSampler, SkyClimateSampler, BiomeBlockResolver, OceanBiomeResolver {
    private final BetaClimateMap climateMap;
    private final BetaClimateSampler climateSampler;
    private final BetaSkyClimateSampler skyClimateSampler;
    
    public BetaBiomeProvider(long seed, Settings settings, Registry<Biome> biomeRegistry) {
        super(seed, settings, biomeRegistry);
        
        this.climateMap = new BetaClimateMap(settings);
        this.climateSampler = new BetaClimateSampler(seed);
        this.skyClimateSampler = new BetaSkyClimateSampler(seed);
    }

    @Override
    public RegistryEntry<Biome> getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        int x = biomeX << 2;
        int z = biomeZ << 2;
        
        Clime clime = this.climateSampler.sampleClime(x, z);
        double temp = clime.temp();
        double rain = clime.rain();
        
        return this.biomeRegistry.getOrCreateEntry(this.climateMap.getBiome(temp, rain, ClimateType.LAND));
    }
 
    @Override
    public RegistryEntry<Biome> getOceanBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        int x = biomeX << 2;
        int z = biomeZ << 2;
        
        Clime clime = this.climateSampler.sampleClime(x, z);
        double temp = clime.temp();
        double rain = clime.rain();
        
        return this.biomeRegistry.getOrCreateEntry(this.climateMap.getBiome(temp, rain, ClimateType.OCEAN));
    }
    
    @Override
    public RegistryEntry<Biome> getBiomeAtBlock(int x, int y, int z) {
        Clime clime = this.climateSampler.sampleClime(x, z);
        double temp = clime.temp();
        double rain = clime.rain();
        
        return this.biomeRegistry.getOrCreateEntry(this.climateMap.getBiome(temp, rain, ClimateType.LAND));
    }

    @Override
    public List<RegistryEntry<Biome>> getBiomesForRegistry() {
        return this.climateMap.getBiomeKeys().stream().map(k -> this.biomeRegistry.getOrCreateEntry(k)).collect(Collectors.toList());
    }

    @Override
    public double sampleSkyTemp(int x, int z) {
        return this.skyClimateSampler.sampleSkyTemp(x, z);
    }

    @Override
    public Clime sample(int x, int z) {
        return this.climateSampler.sampleClime(x, z);
    }
    
    @Override
    public boolean sampleForBiomeColor() {
        return ModernBeta.RENDER_CONFIG.configBiomeColor.renderBetaBiomeColor;
    }
    
    @Override
    public boolean sampleSkyColor() {
        return ModernBeta.RENDER_CONFIG.configBiomeColor.renderBetaSkyColor;
    }
    
    private static class BetaClimateSampler {
        private final SimplexOctaveNoise tempNoiseOctaves;
        private final SimplexOctaveNoise rainNoiseOctaves;
        private final SimplexOctaveNoise detailNoiseOctaves;
        
        private final ChunkCache<ClimateChunk> climateCache;
        
        private final double tempScale;
        private final double rainScale;
        private final double detailScale;
        
        public BetaClimateSampler(long seed) {
            this(seed, 1D);
        }
        
        public BetaClimateSampler(long seed, double climateScale) {
            this.tempNoiseOctaves = new SimplexOctaveNoise(new Random(seed * 9871L), 4);
            this.rainNoiseOctaves = new SimplexOctaveNoise(new Random(seed * 39811L), 4);
            this.detailNoiseOctaves = new SimplexOctaveNoise(new Random(seed * 543321L), 2);
            
            this.climateCache = new ChunkCache<>(
                "climate", 
                1536, 
                true, 
                (chunkX, chunkZ) -> new ClimateChunk(chunkX, chunkZ, this::sampleClimateNoise)
            );
            
            this.tempScale = 0.02500000037252903D / climateScale;
            this.rainScale = 0.05000000074505806D / climateScale;
            this.detailScale = 0.25D / climateScale;
        }

        public Clime sampleClime(int x, int z) {
            int chunkX = x >> 4;
            int chunkZ = z >> 4;
            
            return this.climateCache.get(chunkX, chunkZ).sampleClime(x, z);
        }
        
        public Clime sampleClimateNoise(int x, int z) {
            double temp = this.tempNoiseOctaves.sample(x, z, this.tempScale, this.tempScale, 0.25D);
            double rain = this.rainNoiseOctaves.sample(x, z, this.rainScale, this.rainScale, 0.33333333333333331D);
            double detail = this.detailNoiseOctaves.sample(x, z, this.detailScale, this.detailScale, 0.58823529411764708D);

            detail = detail * 1.1D + 0.5D;

            temp = (temp * 0.15D + 0.7D) * 0.99D + detail * 0.01D;
            rain = (rain * 0.15D + 0.5D) * 0.998D + detail * 0.002D;

            temp = 1.0D - (1.0D - temp) * (1.0D - temp);
            
            return new Clime(MathHelper.clamp(temp, 0.0, 1.0), MathHelper.clamp(rain, 0.0, 1.0));
        }
    }
    
    private static class BetaSkyClimateSampler {
        private final SimplexOctaveNoise tempNoiseOctaves;
        
        private final ChunkCache<SkyClimateChunk> skyClimateCache;
        
        private final double tempScale;
        
        public BetaSkyClimateSampler(long seed) {
            this(seed, 1D);
        }
        
        public BetaSkyClimateSampler(long seed, double climateScale) {
            this.tempNoiseOctaves = new SimplexOctaveNoise(new Random(seed * 9871L), 4);
            
            this.skyClimateCache = new ChunkCache<>(
                "sky", 
                256, 
                true, 
                (chunkX, chunkZ) -> new SkyClimateChunk(chunkX, chunkZ, this::sampleSkyTempNoise)
            );
            
            this.tempScale = 0.02500000037252903D / climateScale;
        }
        
        public double sampleSkyTemp(int x, int z) {
            int chunkX = x >> 4;
            int chunkZ = z >> 4;
            
            return this.skyClimateCache.get(chunkX, chunkZ).sampleTemp(x, z);
        }
        
        private double sampleSkyTempNoise(int x, int z) {
            return this.tempNoiseOctaves.sample(x, z, this.tempScale, this.tempScale, 0.5D);
        }
    }
}
