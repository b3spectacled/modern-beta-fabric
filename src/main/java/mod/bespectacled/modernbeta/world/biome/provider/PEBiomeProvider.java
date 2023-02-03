package mod.bespectacled.modernbeta.world.biome.provider;

import java.util.List;
import java.util.stream.Collectors;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.api.world.biome.BiomeBlockResolver;
import mod.bespectacled.modernbeta.api.world.biome.BiomeProvider;
import mod.bespectacled.modernbeta.api.world.biome.OceanBiomeResolver;
import mod.bespectacled.modernbeta.api.world.biome.climate.ClimateSampler;
import mod.bespectacled.modernbeta.api.world.biome.climate.Clime;
import mod.bespectacled.modernbeta.api.world.biome.climate.SkyClimateSampler;
import mod.bespectacled.modernbeta.util.chunk.ChunkCache;
import mod.bespectacled.modernbeta.util.chunk.ClimateChunk;
import mod.bespectacled.modernbeta.util.mersenne.MTRandom;
import mod.bespectacled.modernbeta.util.noise.PerlinOctaveNoise;
import mod.bespectacled.modernbeta.world.biome.provider.climate.BetaClimateMap;
import mod.bespectacled.modernbeta.world.biome.provider.climate.BetaClimateMapping.ClimateType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;

public class PEBiomeProvider extends BiomeProvider implements ClimateSampler, SkyClimateSampler, BiomeBlockResolver, OceanBiomeResolver {
    private BetaClimateMap climateMap;
    private PEClimateSampler climateSampler;
    
    public PEBiomeProvider(NbtCompound settings, RegistryEntryLookup<Biome> biomeRegistry) {
        super(settings, biomeRegistry);
    }
    
    @Override
    public boolean initProvider(long seed) {
        this.climateMap = new BetaClimateMap(this.settings);
        this.climateSampler = new PEClimateSampler(seed, this.settings.tempNoiseScale, this.settings.rainNoiseScale, this.settings.detailNoiseScale);
        
        return true;
    }

    @Override
    public RegistryEntry<Biome> getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        int x = biomeX << 2;
        int z = biomeZ << 2;
        
        Clime clime = this.climateSampler.sampleClime(x, z);
        double temp = clime.temp();
        double rain = clime.rain();
        
        return this.biomeRegistry.getOrThrow(this.climateMap.getBiome(temp, rain, ClimateType.LAND));
    }
 
    @Override
    public RegistryEntry<Biome> getOceanBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        int x = biomeX << 2;
        int z = biomeZ << 2;
        
        Clime clime = this.climateSampler.sampleClime(x, z);
        double temp = clime.temp();
        double rain = clime.rain();
        
        return this.biomeRegistry.getOrThrow(this.climateMap.getBiome(temp, rain, ClimateType.OCEAN));
    }
    
    @Override
    public RegistryEntry<Biome> getBiomeAtBlock(int x, int y, int z) {
        Clime clime = this.climateSampler.sampleClime(x, z);
        double temp = clime.temp();
        double rain = clime.rain();
        
        return this.biomeRegistry.getOrThrow(this.climateMap.getBiome(temp, rain, ClimateType.LAND));
    }

    @Override
    public List<RegistryEntry<Biome>> getBiomesForRegistry() {
        return this.climateMap.getBiomeKeys().stream().map(i -> this.biomeRegistry.getOrThrow(i)).collect(Collectors.toList());
    }

    @Override
    public double sampleSkyTemp(int x, int z) {
        return this.climateSampler.sampleSkyTemp(x, z);
    }

    @Override
    public Clime sample(int x, int z) {
        return this.climateSampler.sampleClime(x, z);
    }
    
    @Override
    public boolean sampleForBiomeColor() {
        return ModernBeta.RENDER_CONFIG.configBiomeColor.renderPEBetaBiomeColor;
    }
    
    @Override
    public boolean sampleSkyColor() {
        return ModernBeta.RENDER_CONFIG.configBiomeColor.renderPEBetaSkyColor;
    }
    
    private static class PEClimateSampler {
        private final PerlinOctaveNoise tempOctaveNoise;
        private final PerlinOctaveNoise rainOctaveNoise;
        private final PerlinOctaveNoise detailOctaveNoise;
        
        private final ChunkCache<ClimateChunk> climateCache;
        
        private final double tempNoiseScale;
        private final double rainNoiseScale;
        private final double detailNoiseScale;
        
        public PEClimateSampler(long seed, double tempNoiseScale, double rainNoiseScale, double detailNoiseScale) {
            this.tempOctaveNoise = new PerlinOctaveNoise(new MTRandom(seed * 9871L), 4, true);
            this.rainOctaveNoise = new PerlinOctaveNoise(new MTRandom(seed * 39811L), 4, true);
            this.detailOctaveNoise = new PerlinOctaveNoise(new MTRandom(seed * 543321L), 2, true);
            
            this.climateCache = new ChunkCache<>(
                "climate", 
                1536,
                true,
                (chunkX, chunkZ) -> new ClimateChunk(chunkX, chunkZ, this::sampleClimateNoise)
            );
            
            this.tempNoiseScale = 0.025 / tempNoiseScale;
            this.rainNoiseScale = 0.05 / rainNoiseScale;
            this.detailNoiseScale = 0.25 / detailNoiseScale;
        }
        
        public Clime sampleClime(int x, int z) {
            int chunkX = x >> 4;
            int chunkZ = z >> 4;
            
            return this.climateCache.get(chunkX, chunkZ).sampleClime(x, z);
        }
        
        public double sampleSkyTemp(int x, int z) {
            int chunkX = x >> 4;
            int chunkZ = z >> 4;
            
            return this.climateCache.get(chunkX, chunkZ).sampleClime(x, z).temp();
        }
        
        private Clime sampleClimateNoise(int x, int z) {
            double temp = this.tempOctaveNoise.sampleXZ(x, z, this.tempNoiseScale, this.tempNoiseScale);
            double rain = this.rainOctaveNoise.sampleXZ(x, z, this.rainNoiseScale, this.rainNoiseScale);
            double detail = this.detailOctaveNoise.sampleXZ(x, z, this.detailNoiseScale, this.detailNoiseScale);

            detail = detail * 1.1D + 0.5D;

            temp = (temp * 0.15D + 0.7D) * 0.99D + detail * 0.01D;
            rain = (rain * 0.15D + 0.5D) * 0.998D + detail * 0.002D;

            temp = 1.0D - (1.0D - temp) * (1.0D - temp);
            
            return new Clime(MathHelper.clamp(temp, 0.0, 1.0), MathHelper.clamp(rain, 0.0, 1.0));
        }
    }
}
