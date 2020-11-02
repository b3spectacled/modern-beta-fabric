package com.bespectacled.modernbeta.biome;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.apache.logging.log4j.Level;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.gen.settings.BetaGeneratorSettings;
import com.bespectacled.modernbeta.noise.OldNoiseGeneratorOctaves2;
import com.bespectacled.modernbeta.util.BiomeMath;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.RegistryLookupCodec;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.feature.StructureFeature;

public class BetaBiomeSource extends BiomeSource {

    enum BiomeType {
        LAND, OCEAN
    }

    public static final Codec<BetaBiomeSource> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(
                Codec.LONG.fieldOf("seed").stable().forGetter(betaBiomeSource -> betaBiomeSource.seed),
                RegistryLookupCodec.of(Registry.BIOME_KEY).forGetter(betaBiomeSource -> betaBiomeSource.biomeRegistry),
                CompoundTag.CODEC.fieldOf("settings").forGetter(settings -> settings.settings)
            ).apply(instance, (instance).stable(BetaBiomeSource::new)));

    private final long seed;
    public final Registry<Biome> biomeRegistry;
    private final CompoundTag settings;

    private static Biome biomeLookupTable[] = new Biome[4096];
    private static Biome oceanBiomeLookupTable[] = new Biome[4096];

    public static Biome[] biomesInChunk = new Biome[256];
    public static Biome[] oceanBiomesInChunk = new Biome[256];

    private boolean generateOceans = false;
    private boolean generateBetaOceans = true;
    private boolean generateIceDesert = false;
    private boolean generateSkyDim = false;
    
    private static final double[] TEMP_HUMID_POINT = new double[2];
    
    private static final Identifier SKY_ID = new Identifier(ModernBeta.ID, "sky");
    
    public BetaBiomeSource(long seed, Registry<Biome> registry, CompoundTag settings) {
        super(BetaBiomes.getBiomeList().stream().map((registryKey) -> () -> (Biome) registry.get(registryKey)));

        this.seed = seed;
        this.biomeRegistry = registry;
        this.settings = settings;
        
        if (settings.contains("generateOceans")) this.generateOceans = settings.getBoolean("generateOceans");
        if (settings.contains("generateBetaOceans")) this.generateBetaOceans = settings.getBoolean("generateBetaOceans");
        if (settings.contains("generateIceDesert")) this.generateIceDesert = settings.getBoolean("generateIceDesert");
        if (settings.contains("generateSkyDim")) this.generateSkyDim = settings.getBoolean("generateSkyDim");
        
        BiomeMath.setSeed(this.seed);
        generateBiomeLookup(registry);
    }

    @Override
    public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        int absX = biomeX << 2;
        int absZ = biomeZ << 2;
        
        if (this.generateSkyDim) return biomeRegistry.get(SKY_ID);

        // Sample biome at this one absolute coordinate.
        BiomeMath.fetchTempHumidAtPoint(TEMP_HUMID_POINT, absX, absZ);
        
        return fetchBiome(TEMP_HUMID_POINT[0], TEMP_HUMID_POINT[1], BiomeType.LAND);
    }

    public Biome getOceanBiomeForNoiseGen(int x, int y, int z) {
        // Sample biome at this one absolute coordinate.
        BiomeMath.fetchTempHumidAtPoint(TEMP_HUMID_POINT, x, z);

        if (this.generateBetaOceans || this.generateOceans) // To maintain compat with old option
            return fetchBiome(TEMP_HUMID_POINT[0], TEMP_HUMID_POINT[1], BiomeType.OCEAN);
        else
            return fetchBiome(TEMP_HUMID_POINT[0], TEMP_HUMID_POINT[1], BiomeType.LAND);
    }

    private Biome fetchBiome(double temp, double humid, BiomeType type) {
        return getBiomeFromLookup(temp, humid, type);
    }
    
    public void fetchBiomes(double[] temps, double[] humids, Biome[] landBiomes, Biome[] oceanBiomes) {
        int sizeX = 16;
        int sizeZ = 16;
        
        for (int i = 0; i < sizeX * sizeZ; ++i) {
            landBiomes[i] = getBiomeFromLookup(temps[i], humids[i], BiomeType.LAND);
            oceanBiomes[i] = getBiomeFromLookup(temps[i], humids[i], BiomeType.OCEAN);
        }
    }

    private void generateBiomeLookup(Registry<Biome> registry) {
        for (int i = 0; i < 64; i++) {
            for (int j = 0; j < 64; j++) {
                biomeLookupTable[i + j * 64] = getBiome((float) i / 63F, (float) j / 63F, registry);
                oceanBiomeLookupTable[i + j * 64] = getOceanBiome((float) i / 63F, (float) j / 63F, registry);
            }
        }
    }

    private Biome getBiomeFromLookup(double temp, double humid, BiomeType type) {
        int i = (int) (temp * 63D);
        int j = (int) (humid * 63D);

        if (type == BiomeType.LAND)
            return biomeLookupTable[i + j * 64];
        if (type == BiomeType.OCEAN)
            return oceanBiomeLookupTable[i + j * 64];

        return biomeLookupTable[i + j * 64];
    }

    public Biome getBiome(float temp, float humid, Registry<Biome> registry) {
        humid *= temp;
        
        if (this.generateSkyDim) return registry.get(SKY_ID);

        if (temp < 0.1F) {
            if (this.generateIceDesert)
                return registry.get(BetaBiomes.ICE_DESERT_ID);
            else
                return registry.get(BetaBiomes.TUNDRA_ID);
        }

        if (humid < 0.2F) {
            if (temp < 0.5F) {
                return registry.get(BetaBiomes.TUNDRA_ID);
            }
            if (temp < 0.95F) {
                return registry.get(BetaBiomes.SAVANNA_ID);
            } else {
                return registry.get(BetaBiomes.DESERT_ID);
            }
        }

        if (humid > 0.5F && temp < 0.7F) {
            return registry.get(BetaBiomes.SWAMPLAND_ID);
        }

        if (temp < 0.5F) {
            return registry.get(BetaBiomes.TAIGA_ID);
        }

        if (temp < 0.97F) {
            if (humid < 0.35F) {
                return registry.get(BetaBiomes.SHRUBLAND_ID);
            } else {
                return registry.get(BetaBiomes.FOREST_ID);
            }
        }

        if (humid < 0.45F) {
            return registry.get(BetaBiomes.PLAINS_ID);
        }

        if (humid < 0.9F) {
            return registry.get(BetaBiomes.SEASONAL_FOREST_ID);
        } else {
            return registry.get(BetaBiomes.RAINFOREST_ID);
        }

    }

    public Biome getOceanBiome(float temp, float humid, Registry<Biome> registry) {
        humid *= temp;

        // == Vanilla Biome IDs ==
        // 0 = Ocean
        // 44 = Warm Ocean
        // 45 = Lukewarm Ocean
        // 46 = Cold Ocean
        // 10 = Frozen Ocean

        if (temp < 0.1F) {
            return registry.get(BetaBiomes.FROZEN_OCEAN_ID);
        }

        if (humid < 0.2F) {
            if (temp < 0.5F) {
                return registry.get(BetaBiomes.FROZEN_OCEAN_ID);
            }
            if (temp < 0.95F) {
                return registry.get(BetaBiomes.OCEAN_ID);
            } else {
                return registry.get(BetaBiomes.OCEAN_ID);
            }
        }

        if (humid > 0.5F && temp < 0.7F) {
            return registry.get(BetaBiomes.COLD_OCEAN_ID);
        }

        if (temp < 0.5F) {
            return registry.get(BetaBiomes.FROZEN_OCEAN_ID);
        }

        if (temp < 0.97F) {
            if (humid < 0.35F) {
                return registry.get(BetaBiomes.OCEAN_ID);
            } else {
                return registry.get(BetaBiomes.OCEAN_ID);
            }
        }

        if (humid < 0.45F) {
            return registry.get(BetaBiomes.OCEAN_ID);
        }

        if (humid < 0.9F) {
            return registry.get(BetaBiomes.LUKEWARM_OCEAN_ID);
        } else {
            return registry.get(BetaBiomes.WARM_OCEAN_ID);
        }

    }
    
    public boolean isSkyDim() {
        return this.generateSkyDim;
    }

    public boolean hasStructureFeature(StructureFeature<?> structureFeature) {
        return this.structureFeatures.computeIfAbsent(structureFeature,
                s -> this.biomes.stream().anyMatch(biome -> biome.getGenerationSettings().hasStructureFeature(s)));
    }

    @Override
    protected Codec<? extends BiomeSource> getCodec() {
        return BetaBiomeSource.CODEC;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public BiomeSource withSeed(long seed) {
        return new BetaBiomeSource(seed, this.biomeRegistry, this.settings);
    }

    public static void register() {
        Registry.register(Registry.BIOME_SOURCE, new Identifier(ModernBeta.ID, "beta_biome_source"), CODEC);
        ModernBeta.LOGGER.log(Level.INFO, "Registered Beta biome source.");
    }

}
