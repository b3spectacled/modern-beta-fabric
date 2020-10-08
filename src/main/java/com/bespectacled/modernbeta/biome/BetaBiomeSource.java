package com.bespectacled.modernbeta.biome;

import java.util.List;
import java.util.Random;
import org.apache.logging.log4j.Level;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.config.ModernBetaConfig;
import com.bespectacled.modernbeta.noise.BetaNoiseGeneratorOctaves2;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.RegistryLookupCodec;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.feature.StructureFeature;

public class BetaBiomeSource extends BiomeSource {
    
    enum BiomeType {
        LAND,
        OCEAN
    }
	
	public static final Codec<BetaBiomeSource> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Codec.LONG.fieldOf("seed").stable().forGetter(betaBiomeSource -> betaBiomeSource.seed), 
		RegistryLookupCodec.of(Registry.BIOME_KEY).forGetter(betaBiomeSource -> betaBiomeSource.biomeRegistry)
	).apply(instance, (instance).stable(BetaBiomeSource::new)));
	
	private static final List<RegistryKey<Biome>> BIOMES = ImmutableList.of(
	        RegistryKey.of(Registry.BIOME_KEY, new Identifier(ModernBeta.ID, "forest")),
	        RegistryKey.of(Registry.BIOME_KEY, new Identifier(ModernBeta.ID, "desert")),
	        RegistryKey.of(Registry.BIOME_KEY, new Identifier(ModernBeta.ID, "plains")),
	        RegistryKey.of(Registry.BIOME_KEY, new Identifier(ModernBeta.ID, "savanna")),
	        RegistryKey.of(Registry.BIOME_KEY, new Identifier(ModernBeta.ID, "shrubland")),
	        RegistryKey.of(Registry.BIOME_KEY, new Identifier(ModernBeta.ID, "seasonal_forest")),
	        RegistryKey.of(Registry.BIOME_KEY, new Identifier(ModernBeta.ID, "rainforest")),
	        RegistryKey.of(Registry.BIOME_KEY, new Identifier(ModernBeta.ID, "swampland")),
            RegistryKey.of(Registry.BIOME_KEY, new Identifier(ModernBeta.ID, "taiga")),
            RegistryKey.of(Registry.BIOME_KEY, new Identifier(ModernBeta.ID, "tundra")),
            RegistryKey.of(Registry.BIOME_KEY, new Identifier(ModernBeta.ID, "ice_desert")),
            
            RegistryKey.of(Registry.BIOME_KEY, new Identifier(ModernBeta.ID, "ocean")),
            RegistryKey.of(Registry.BIOME_KEY, new Identifier(ModernBeta.ID, "lukewarm_ocean")),
            RegistryKey.of(Registry.BIOME_KEY, new Identifier(ModernBeta.ID, "warm_ocean")),
            RegistryKey.of(Registry.BIOME_KEY, new Identifier(ModernBeta.ID, "cold_ocean")),
            RegistryKey.of(Registry.BIOME_KEY, new Identifier(ModernBeta.ID, "frozen_ocean")),
            
            RegistryKey.of(Registry.BIOME_KEY, new Identifier(ModernBeta.ID, "sky"))
	);
			
	private final long seed;
	public final Registry<Biome> biomeRegistry;
	
	public double temps[];
	public double humids[];
	public double noises[];
	
	private BetaNoiseGeneratorOctaves2 tempNoiseOctaves;
    private BetaNoiseGeneratorOctaves2 humidNoiseOctaves;
    private BetaNoiseGeneratorOctaves2 noiseOctaves;
    
    private static Biome biomeLookupTable[] = new Biome[4096];
    private static Biome oceanBiomeLookupTable[] = new Biome[4096];
    
    public static Biome[] biomesInChunk = new Biome[256];
    public static Biome[] oceanBiomesInChunk = new Biome[256];
    
    private static final boolean GENERATE_OCEANS = ModernBetaConfig.loadConfig().generate_oceans;
    private static final boolean GENERATE_ICE_DESERT = ModernBetaConfig.loadConfig().generate_ice_desert;
    private static final boolean GENERATE_SKY = ModernBetaConfig.loadConfig().generate_sky;

	public BetaBiomeSource(long seed, Registry<Biome> registry) {
	    super(BIOMES.stream().map((registryKey) -> () -> (Biome)registry.get(registryKey)));
		
		this.seed = seed;
		this.biomeRegistry = registry;
		
		tempNoiseOctaves = new BetaNoiseGeneratorOctaves2(new Random(this.seed * 9871L), 4);
		humidNoiseOctaves = new BetaNoiseGeneratorOctaves2(new Random(this.seed * 39811L), 4);
		noiseOctaves = new BetaNoiseGeneratorOctaves2(new Random(this.seed * 543321L), 2);
		
		generateBiomeLookup(registry);
		
	}
	
	@Override
	public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
		int absX = biomeX << 2;
		int absZ = biomeZ << 2;
		
		// Sample biome at this one absolute coordinate.
		fetchTempHumid(absX, absZ, 1, 1);
		
		return biomesInChunk[0];
	}
	
	public Biome getOceanBiomeForNoiseGen(int x, int y, int z) {
		// Sample biome at this one absolute coordinate.
		fetchTempHumid(x, z, 1, 1);
		
		return oceanBiomesInChunk[0];
	}
	
	public Biome[] fetchTempHumid(int x, int z, int sizeX, int sizeZ) {
		temps = tempNoiseOctaves.func_4112_a(temps, x, z, sizeX, sizeX, 0.02500000037252903D, 0.02500000037252903D, 0.25D);
        humids = humidNoiseOctaves.func_4112_a(humids, x, z, sizeX, sizeX, 0.05000000074505806D, 0.05000000074505806D, 0.33333333333333331D);
        noises = noiseOctaves.func_4112_a(noises, x, z, sizeX, sizeX, 0.25D, 0.25D, 0.58823529411764708D);
        
        int i = 0;
        for(int j = 0; j < sizeX; j++) {
            for(int k = 0; k < sizeZ; k++) {
                double d = noises[i] * 1.1000000000000001D + 0.5D;
                double d1 = 0.01D;
                double d2 = 1.0D - d1;
                
                double temp = (temps[i] * 0.14999999999999999D + 0.69999999999999996D) * d2 + d * d1;
                
                d1 = 0.002D;
                d2 = 1.0D - d1;
                
                double humid = (humids[i] * 0.14999999999999999D + 0.5D) * d2 + d * d1;
                temp = 1.0D - (1.0D - temp) * (1.0D - temp);
                
                if(temp < 0.0D) {
                    temp = 0.0D;
                }
                if(humid < 0.0D) {
                    humid = 0.0D;
                }
                if(temp > 1.0D) {
                    temp = 1.0D;
                }
                if(humid > 1.0D) {
                    humid = 1.0D;
                }
                temps[i] = temp;
                humids[i] = humid;
                
                biomesInChunk[i] = getBiomeFromLookup(temp, humid, BiomeType.LAND);
                oceanBiomesInChunk[i] = getBiomeFromLookup(temp, humid, BiomeType.OCEAN);
                
                //biomesInChunk[k][j] = getBiomeFromLookup(temp, humid, BiomeType.LAND);
                //oceanBiomesInChunk[k][j] = getBiomeFromLookup(temp, humid, BiomeType.OCEAN);\
                i++;
            }
        }
        
        return biomesInChunk;
	}
	
	private void generateBiomeLookup(Registry<Biome> registry) {
		for(int i = 0; i < 64; i++) {
            for(int j = 0; j < 64; j++) {
                biomeLookupTable[i + j * 64] = getBiome((float)i / 63F, (float)j / 63F, registry);
                oceanBiomeLookupTable[i + j * 64] = getOceanBiome((float)i / 63F, (float)j / 63F, registry);
            }
        }
	}
	
	private Biome getBiomeFromLookup(double temp, double humid, BiomeType type) {
        int i = (int)(temp * 63D);
        int j = (int)(humid * 63D);
        
        if (type == BiomeType.LAND)
            return biomeLookupTable[i + j * 64];
        if (type == BiomeType.OCEAN)
            return oceanBiomeLookupTable[i + j * 64];
        
        return biomeLookupTable[i + j * 64];
    }
	
	
	public static Biome getBiome(float temp, float humid, Registry<Biome> registry) {
        humid *= temp;
        

        if(temp < 0.1F) {
            if (GENERATE_ICE_DESERT)
                return registry.get(new Identifier(ModernBeta.ID, "ice_desert"));
            else
                return registry.get(new Identifier(ModernBeta.ID, "tundra"));
        }
        
        if(humid < 0.2F) {
            if(temp < 0.5F) {
                return registry.get(new Identifier(ModernBeta.ID, "tundra"));
            }
            if(temp < 0.95F) {
                return registry.get(new Identifier(ModernBeta.ID, "savanna"));
            } else {
                return registry.get(new Identifier(ModernBeta.ID, "desert"));
            }
        }
        
        if(humid > 0.5F && temp < 0.7F) {
            return registry.get(new Identifier(ModernBeta.ID, "swampland"));
        }
        
        if(temp < 0.5F) {
            return registry.get(new Identifier(ModernBeta.ID, "taiga"));
        }
        
        if(temp < 0.97F) {
            if(humid < 0.35F) {
                return registry.get(new Identifier(ModernBeta.ID, "shrubland"));
            } else {
                return registry.get(new Identifier(ModernBeta.ID, "forest"));
            }
        }
        
        if(humid < 0.45F) {
            return registry.get(new Identifier(ModernBeta.ID, "plains"));
        }
        
        if(humid < 0.9F) {
            return registry.get(new Identifier(ModernBeta.ID, "seasonal_forest"));
        } else {
            return registry.get(new Identifier(ModernBeta.ID, "rainforest"));
        }
        
    }
	
	public static Biome getOceanBiome(float temp, float humid, Registry<Biome> registry) {
        humid *= temp;

        // == Vanilla Biome IDs ==
        // 0 = Ocean
        // 44 = Warm Ocean
        // 45 = Lukewarm Ocean
        // 46 = Cold Ocean
        // 10 = Frozen Ocean
        
        if (!GENERATE_OCEANS) {
            return getLiteOceanBiome(temp, humid, registry);
        }

        if(temp < 0.1F) {
            return registry.get(new Identifier(ModernBeta.ID, "frozen_ocean"));
        }
        
        if(humid < 0.2F) {
            if(temp < 0.5F) {
                return registry.get(new Identifier(ModernBeta.ID, "frozen_ocean"));
            }
            if(temp < 0.95F) {
                return registry.get(new Identifier(ModernBeta.ID, "ocean"));
            } else {
                return registry.get(new Identifier(ModernBeta.ID, "ocean"));
            }
        }
        
        if(humid > 0.5F && temp < 0.7F) {
            return registry.get(new Identifier(ModernBeta.ID, "cold_ocean"));
        }
        
        if(temp < 0.5F) {
            return registry.get(new Identifier(ModernBeta.ID, "frozen_ocean"));
        }
        
        if(temp < 0.97F) {
            if(humid < 0.35F) {
                return registry.get(new Identifier(ModernBeta.ID, "ocean"));
            } else {
                return registry.get(new Identifier(ModernBeta.ID, "ocean"));
            }
        }
        
        if(humid < 0.45F) {
            return registry.get(new Identifier(ModernBeta.ID, "ocean"));
        }
        
        if(humid < 0.9F) {
        	return registry.get(new Identifier(ModernBeta.ID, "lukewarm_ocean"));
        } else {
            return registry.get(new Identifier(ModernBeta.ID, "warm_ocean"));
        }
        
    }
	
	private static Biome getLiteOceanBiome(float temp, float humid, Registry<Biome> registry) {
        humid *= temp;
        
        if(temp < 0.1F) {
            if (GENERATE_ICE_DESERT)
                return registry.get(new Identifier(ModernBeta.ID, "frozen_ocean"));
            else
                return registry.get(new Identifier(ModernBeta.ID, "frozen_ocean"));
        }
        
        if(humid < 0.2F) {
            if(temp < 0.5F) {
                return registry.get(new Identifier(ModernBeta.ID, "frozen_ocean"));
            }
            if(temp < 0.95F) {
                return registry.get(new Identifier(ModernBeta.ID, "ocean"));
            } else {
                return registry.get(new Identifier(ModernBeta.ID, "ocean"));
            }
        }
        
        if(humid > 0.5F && temp < 0.7F) {
            return registry.get(new Identifier(ModernBeta.ID, "ocean"));
        }
        
        if(temp < 0.5F) {
            return registry.get(new Identifier(ModernBeta.ID, "frozen_ocean"));
        }
        
        return registry.get(new Identifier(ModernBeta.ID, "ocean"));
        
    }
	
	public boolean hasStructureFeature(StructureFeature<?> structureFeature) {
        return this.structureFeatures.computeIfAbsent(structureFeature, s -> this.biomes.stream().anyMatch(biome -> biome.getGenerationSettings().hasStructureFeature(s)));
    }

    @Override
    protected Codec<? extends BiomeSource> getCodec() {
        return BetaBiomeSource.CODEC;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public BiomeSource withSeed(long seed) {
        return new BetaBiomeSource(seed, this.biomeRegistry);
    }
	
	public static void register() {
		Registry.register(Registry.BIOME_SOURCE, new Identifier(ModernBeta.ID, "beta_biome_source"), CODEC);
		ModernBeta.LOGGER.log(Level.INFO, "Registered Beta biome source.");
	}
	
}
