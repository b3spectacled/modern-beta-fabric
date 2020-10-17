package com.bespectacled.modernbeta.biome;

import java.util.List;
import java.util.Random;
import org.apache.logging.log4j.Level;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.gen.settings.AlphaGeneratorSettings;
import com.bespectacled.modernbeta.gen.settings.BetaGeneratorSettings;
import com.bespectacled.modernbeta.noise.BetaNoiseGeneratorOctaves2;
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

public class AlphaBiomeSource extends BiomeSource {

    public static final Codec<AlphaBiomeSource> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(
                Codec.LONG.fieldOf("seed").stable().forGetter(alphaBiomeSource -> alphaBiomeSource.seed),
                RegistryLookupCodec.of(Registry.BIOME_KEY).forGetter(alphaBiomeSource -> alphaBiomeSource.biomeRegistry),
                CompoundTag.CODEC.fieldOf("settings").forGetter(settings -> settings.settings)
            ).apply(instance, (instance).stable(AlphaBiomeSource::new)));

    private static final List<RegistryKey<Biome>> BIOMES = ImmutableList.of(
            RegistryKey.of(Registry.BIOME_KEY, new Identifier(ModernBeta.ID, "alpha")),
            RegistryKey.of(Registry.BIOME_KEY, new Identifier(ModernBeta.ID, "alpha_winter")));

    private final long seed;
    public final Registry<Biome> biomeRegistry;
    private final CompoundTag settings;
    
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
    
    private boolean alphaWinterMode = false;
    private boolean alphaPlus = false;

    public AlphaBiomeSource(long seed, Registry<Biome> registry, CompoundTag settings) {
        super(BIOMES.stream().map((registryKey) -> () -> (Biome) registry.get(registryKey)));

        this.seed = seed;
        this.biomeRegistry = registry;
        this.settings = settings;
        
        this.alphaWinterMode = false;
        this.alphaPlus = false;
        
        if (settings.contains("alphaWinterMode")) this.alphaWinterMode = settings.getBoolean("alphaWinterMode");
        if (settings.contains("alphaPlus")) this.alphaPlus = settings.getBoolean("alphaPlus");

        tempNoiseOctaves = new BetaNoiseGeneratorOctaves2(new Random(this.seed * 9871L), 4);
        humidNoiseOctaves = new BetaNoiseGeneratorOctaves2(new Random(this.seed * 39811L), 4);
        noiseOctaves = new BetaNoiseGeneratorOctaves2(new Random(this.seed * 543321L), 2);

        generateBiomeLookup(registry);

    }

    @Override
    public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        Biome biome;
        int absX = biomeX << 2;
        int absZ = biomeZ << 2;

        if (this.alphaPlus) {
            // Sample biome at this one absolute coordinate.
            fetchTempHumid(absX, absZ, 1, 1);

            biome = biomesInChunk[0];
        } else if (alphaWinterMode) {
            biome = this.biomeRegistry.get(new Identifier(ModernBeta.ID, "alpha_winter"));
        } else {
            biome = this.biomeRegistry.get(new Identifier(ModernBeta.ID, "alpha"));
        }

        return biome;
    }

    public Biome getOceanBiomeForNoiseGen(int x, int y, int z) {
        Biome biome;

        if (this.alphaPlus) {
            // Sample biome at this one absolute coordinate.
            fetchTempHumid(x, y, 1, 1);

            biome = oceanBiomesInChunk[0];
        } else if (alphaWinterMode) {
            biome = this.biomeRegistry.get(new Identifier(ModernBeta.ID, "frozen_ocean"));
        } else {
            biome = this.biomeRegistry.get(new Identifier(ModernBeta.ID, "ocean"));
        }

        return biome;
    }

    public Biome[] fetchTempHumid(int x, int z, int sizeX, int sizeZ) {
        temps = tempNoiseOctaves.func_4112_a(temps, x, z, sizeX, sizeX, 0.02500000037252903D, 0.02500000037252903D,
                0.25D);
        humids = humidNoiseOctaves.func_4112_a(humids, x, z, sizeX, sizeX, 0.05000000074505806D, 0.05000000074505806D,
                0.33333333333333331D);
        noises = noiseOctaves.func_4112_a(noises, x, z, sizeX, sizeX, 0.25D, 0.25D, 0.58823529411764708D);

        int i = 0;
        for (int j = 0; j < sizeX; j++) {
            for (int k = 0; k < sizeZ; k++) {
                double d = noises[i] * 1.1000000000000001D + 0.5D;
                double d1 = 0.01D;
                double d2 = 1.0D - d1;

                double temp = (temps[i] * 0.14999999999999999D + 0.69999999999999996D) * d2 + d * d1;

                d1 = 0.002D;
                d2 = 1.0D - d1;

                double humid = (humids[i] * 0.14999999999999999D + 0.5D) * d2 + d * d1;
                temp = 1.0D - (1.0D - temp) * (1.0D - temp);

                if (temp < 0.0D) {
                    temp = 0.0D;
                }
                if (humid < 0.0D) {
                    humid = 0.0D;
                }
                if (temp > 1.0D) {
                    temp = 1.0D;
                }
                if (humid > 1.0D) {
                    humid = 1.0D;
                }
                temps[i] = temp;
                humids[i] = humid;
                
                biomesInChunk[i] = getBiomeFromLookup(temp, humid);
                oceanBiomesInChunk[i] = getOceanBiomeFromLookup(temp, humid);
                
                ++i;
            }
        }

        return biomesInChunk;
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
            return registry.get(new Identifier(ModernBeta.ID, "alpha_winter"));
        }

        return registry.get(new Identifier(ModernBeta.ID, "alpha"));
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
        ModernBeta.LOGGER.log(Level.INFO, "Registered Alpha biome source.");
    }

}
