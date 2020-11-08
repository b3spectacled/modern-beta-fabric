package com.bespectacled.modernbeta.biome;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.util.BiomeMath;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryLookupCodec;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;

public class AlphaBiomeSource extends BiomeSource {

    public static final Codec<AlphaBiomeSource> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(
                Codec.LONG.fieldOf("seed").stable().forGetter(alphaBiomeSource -> alphaBiomeSource.seed),
                RegistryLookupCodec.of(Registry.BIOME_KEY).forGetter(alphaBiomeSource -> alphaBiomeSource.biomeRegistry),
                CompoundTag.CODEC.fieldOf("settings").forGetter(settings -> settings.settings)
            ).apply(instance, (instance).stable(AlphaBiomeSource::new)));

    private final long seed;
    public final Registry<Biome> biomeRegistry;
    private final CompoundTag settings;

    private static Biome biomeLookupTable[] = new Biome[4096];
    private static Biome oceanBiomeLookupTable[] = new Biome[4096];

    public static Biome[] biomesInChunk = new Biome[256];
    public static Biome[] oceanBiomesInChunk = new Biome[256];
    
    private boolean alphaWinterMode = false;
    private boolean alphaPlus = false;
    
    private static final double[] TEMP_HUMID_POINT = new double[2];

    public AlphaBiomeSource(long seed, Registry<Biome> registry, CompoundTag settings) {
        super(AlphaBiomes.getBiomeList().stream().map((registryKey) -> () -> (Biome) registry.get(registryKey)));

        this.seed = seed;
        this.biomeRegistry = registry;
        this.settings = settings;
        
        this.alphaWinterMode = false;
        this.alphaPlus = false;
        
        if (settings.contains("alphaWinterMode")) this.alphaWinterMode = settings.getBoolean("alphaWinterMode");
        if (settings.contains("alphaPlus")) this.alphaPlus = settings.getBoolean("alphaPlus");
        
        BiomeMath.setSeed(this.seed);
        generateBiomeLookup(registry);

    }

    @Override
    public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        Biome biome;
        int absX = biomeX << 2;
        int absZ = biomeZ << 2;

        if (this.alphaPlus) {
            // Sample biome at this one absolute coordinate.
            BiomeMath.fetchTempHumidAtPoint(TEMP_HUMID_POINT, absX, absZ);

            biome = fetchBiome(TEMP_HUMID_POINT[0], TEMP_HUMID_POINT[1]);
        } else if (alphaWinterMode) {
            biome = this.biomeRegistry.get(AlphaBiomes.ALPHA_WINTER_ID);
        } else {
            biome = this.biomeRegistry.get(AlphaBiomes.ALPHA_ID);
        }

        return biome;
    }

    public Biome getOceanBiomeForNoiseGen(int x, int y, int z) {
        Biome biome;

        if (this.alphaPlus) {
            // Sample biome at this one absolute coordinate.
            BiomeMath.fetchTempHumidAtPoint(TEMP_HUMID_POINT, x, z);

            biome = fetchBiome(TEMP_HUMID_POINT[0], TEMP_HUMID_POINT[1]);
        } else if (alphaWinterMode) {
            biome = this.biomeRegistry.get(new Identifier(ModernBeta.ID, "frozen_ocean"));
        } else {
            biome = this.biomeRegistry.get(new Identifier(ModernBeta.ID, "ocean"));
        }

        return biome;
    }
    
    private Biome fetchBiome(double temp, double humid) {
        return getBiomeFromLookup(temp, humid);
    }
    
    public void fetchBiomes(double[] temps, double[] humids, Biome[] biomes) {
        int sizeX = 16;
        int sizeZ = 16;
        
        for (int i = 0; i < sizeX * sizeZ; ++i) {
            biomes[i] = getBiomeFromLookup(temps[i], humids[i]);
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

    private Biome getBiomeFromLookup(double temp, double humid) {
        int i = (int) (temp * 63D);
        int j = (int) (humid * 63D);

        return biomeLookupTable[i + j * 64];
    }

    public Biome getOceanBiomeFromLookup(double temp, double humid) {
        int i = (int) (temp * 63D);
        int j = (int) (humid * 63D);

        return oceanBiomeLookupTable[i + j * 64];
    }

    public static Biome getBiome(float temp, float humid, Registry<Biome> registry) {
        if (temp < 0.5F) {
            return registry.get(AlphaBiomes.ALPHA_WINTER_ID);
        }

        return registry.get(AlphaBiomes.ALPHA_ID);
    }

    public static Biome getOceanBiome(float temp, float humid, Registry<Biome> registry) {
        if (temp < 0.5F) {
            return registry.get(new Identifier(ModernBeta.ID, "frozen_ocean"));
        }

        return registry.get(new Identifier(ModernBeta.ID, "ocean"));

    }

    @Override
    protected Codec<? extends BiomeSource> getCodec() {
        return AlphaBiomeSource.CODEC;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public BiomeSource withSeed(long seed) {
        return new AlphaBiomeSource(seed, this.biomeRegistry, this.settings);
    }

    public static void register() {
        Registry.register(Registry.BIOME_SOURCE, new Identifier(ModernBeta.ID, "alpha_biome_source"), CODEC);
        //ModernBeta.LOGGER.log(Level.INFO, "Registered Alpha biome source.");
    }

}
