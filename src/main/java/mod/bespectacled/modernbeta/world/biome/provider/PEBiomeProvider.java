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
import mod.bespectacled.modernbeta.world.biome.provider.climate.ClimateMapping.ClimateType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.biome.Biome;

public class PEBiomeProvider extends BiomeProvider implements ClimateSampler, SkyClimateSampler, BiomeBlockResolver, OceanBiomeResolver {
    private final BetaClimateMap climateMap;
    private final PEClimateSampler climateSampler;
    
    public PEBiomeProvider(long seed, NbtCompound settings, Registry<Biome> biomeRegistry) {
        super(seed, settings, biomeRegistry);
        
        this.climateMap = new BetaClimateMap(this.settings);
        this.climateSampler = new PEClimateSampler(seed);
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
        return this.climateMap.getBiomeKeys().stream().map(i -> this.biomeRegistry.getOrCreateEntry(i)).collect(Collectors.toList());
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
        private final PerlinOctaveNoise tempNoiseOctaves;
        private final PerlinOctaveNoise rainNoiseOctaves;
        private final PerlinOctaveNoise detailNoiseOctaves;
        
        private final ChunkCache<ClimateChunk> climateCache;
        
        private final double tempScale;
        private final double rainScale;
        private final double detailScale;
        
        public PEClimateSampler(long seed) {
            this(seed, 1D);
        }
        
        public PEClimateSampler(long seed, double climateScale) {
            this.tempNoiseOctaves = new PerlinOctaveNoise(new MTRandom(seed * 9871L), 4, true);
            this.rainNoiseOctaves = new PerlinOctaveNoise(new MTRandom(seed * 39811L), 4, true);
            this.detailNoiseOctaves = new PerlinOctaveNoise(new MTRandom(seed * 543321L), 2, true);
            
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
        
        public double sampleSkyTemp(int x, int z) {
            int chunkX = x >> 4;
            int chunkZ = z >> 4;
            
            return this.climateCache.get(chunkX, chunkZ).sampleClime(x, z).temp();
        }
        
        private Clime sampleClimateNoise(int x, int z) {
            double temp = this.tempNoiseOctaves.sampleXZ(x, z, this.tempScale, this.tempScale);
            double rain = this.rainNoiseOctaves.sampleXZ(x, z, this.rainScale, this.rainScale);
            double detail = this.detailNoiseOctaves.sampleXZ(x, z, this.detailScale, this.detailScale);

            detail = detail * 1.1D + 0.5D;

            temp = (temp * 0.15D + 0.7D) * 0.99D + detail * 0.01D;
            rain = (rain * 0.15D + 0.5D) * 0.998D + detail * 0.002D;

            temp = 1.0D - (1.0D - temp) * (1.0D - temp);
            
            return new Clime(MathHelper.clamp(temp, 0.0, 1.0), MathHelper.clamp(rain, 0.0, 1.0));
        }
    }
}
