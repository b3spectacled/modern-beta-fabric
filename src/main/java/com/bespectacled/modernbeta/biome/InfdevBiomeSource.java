package com.bespectacled.modernbeta.biome;

import java.util.List;
import java.util.Random;
import org.apache.logging.log4j.Level;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.gen.settings.AlphaGeneratorSettings;
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

public class InfdevBiomeSource extends BiomeSource {

    public static final Codec<InfdevBiomeSource> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(
                Codec.LONG.fieldOf("seed").stable().forGetter(infdevBiomeSource -> infdevBiomeSource.seed),
                RegistryLookupCodec.of(Registry.BIOME_KEY).forGetter(infdevBiomeSource -> infdevBiomeSource.biomeRegistry),
                CompoundTag.CODEC.fieldOf("settings").forGetter(infdevBiomeSource -> infdevBiomeSource.settings)
            ).apply(instance, (instance).stable(InfdevBiomeSource::new)));

    private final long seed;
    public final Registry<Biome> biomeRegistry;
    private final CompoundTag settings;
    
    public double temps[];
    public double humids[];
    public double noises[];

    private OldNoiseGeneratorOctaves2 tempNoiseOctaves;
    private OldNoiseGeneratorOctaves2 humidNoiseOctaves;
    private OldNoiseGeneratorOctaves2 noiseOctaves;

    private static Biome biomeLookupTable[] = new Biome[4096];
    private static Biome oceanBiomeLookupTable[] = new Biome[4096];

    public static Biome[] biomesInChunk = new Biome[256];
    public static Biome[] oceanBiomesInChunk = new Biome[256];
    
    private boolean infdevWinterMode = false;
    private boolean infdevPlus = false;
    
    private static final double[] TEMP_HUMID_POINT = new double[2];

    public InfdevBiomeSource(long seed, Registry<Biome> registry, CompoundTag settings) {
        super(InfdevBiomes.getBiomeList().stream().map((registryKey) -> () -> (Biome) registry.get(registryKey)));

        this.seed = seed;
        this.biomeRegistry = registry;
        this.settings = settings;
        
        this.infdevWinterMode = false;
        this.infdevPlus = false;
        
        if (settings.contains("infdevWinterMode")) this.infdevWinterMode = settings.getBoolean("infdevWinterMode");
        if (settings.contains("infdevPlus")) this.infdevPlus = settings.getBoolean("infdevPlus");

        tempNoiseOctaves = new OldNoiseGeneratorOctaves2(new Random(this.seed * 9871L), 4);
        humidNoiseOctaves = new OldNoiseGeneratorOctaves2(new Random(this.seed * 39811L), 4);
        noiseOctaves = new OldNoiseGeneratorOctaves2(new Random(this.seed * 543321L), 2);

        BiomeMath.setSeed(this.seed);
        generateBiomeLookup(registry);
    }

    @Override
    public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        Biome biome;
        int absX = biomeX << 2;
        int absZ = biomeZ << 2;

        if (this.infdevPlus) {
            // Sample biome at this one absolute coordinate.
            BiomeMath.fetchTempHumidAtPoint(TEMP_HUMID_POINT, absX, absZ);

            biome = fetchBiome(TEMP_HUMID_POINT[0], TEMP_HUMID_POINT[1]);
        } else if (infdevWinterMode) {
            biome = this.biomeRegistry.get(InfdevBiomes.INFDEV_WINTER_ID);
        } else {
            biome = this.biomeRegistry.get(InfdevBiomes.INFDEV_ID);
        }

        return biome;
    }

    public Biome getOceanBiomeForNoiseGen(int x, int y, int z) {
        Biome biome;

        if (this.infdevPlus) {
            // Sample biome at this one absolute coordinate.
            BiomeMath.fetchTempHumidAtPoint(TEMP_HUMID_POINT, x, z);

            biome = fetchBiome(TEMP_HUMID_POINT[0], TEMP_HUMID_POINT[1]);
        } else if (infdevWinterMode) {
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
            return registry.get(InfdevBiomes.INFDEV_WINTER_ID);
        }

        return registry.get(InfdevBiomes.INFDEV_ID);
    }

    public static Biome getOceanBiome(float temp, float humid, Registry<Biome> registry) {
        if (temp < 0.5F) {
            return registry.get(new Identifier(ModernBeta.ID, "frozen_ocean"));
        }

        return registry.get(new Identifier(ModernBeta.ID, "ocean"));

    }

    @Override
    protected Codec<? extends BiomeSource> getCodec() {
        return InfdevBiomeSource.CODEC;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public BiomeSource withSeed(long seed) {
        return new InfdevBiomeSource(seed, this.biomeRegistry, this.settings);
    }

    public static void register() {
        Registry.register(Registry.BIOME_SOURCE, new Identifier(ModernBeta.ID, "infdev_biome_source"), CODEC);
        //ModernBeta.LOGGER.log(Level.INFO, "Registered Infdev biome source.");
    }

}
