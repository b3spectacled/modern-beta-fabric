package com.bespectacled.modernbeta.biome;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.biome.layer.BetaBiomeLayer;
import com.bespectacled.modernbeta.biome.layer.BetaOceanLayer;
import com.bespectacled.modernbeta.util.BiomeUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryLookupCodec;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeLayerSampler;
import net.minecraft.world.biome.source.BiomeSource;

public class AlphaBiomeSource extends BiomeSource implements IOldBiomeSource {

    public static final Codec<AlphaBiomeSource> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(
                Codec.LONG.fieldOf("seed").stable().forGetter(alphaBiomeSource -> alphaBiomeSource.seed),
                RegistryLookupCodec.of(Registry.BIOME_KEY).forGetter(alphaBiomeSource -> alphaBiomeSource.biomeRegistry),
                CompoundTag.CODEC.fieldOf("settings").forGetter(settings -> settings.settings)
            ).apply(instance, (instance).stable(AlphaBiomeSource::new)));

    private final long seed;
    public final Registry<Biome> biomeRegistry;
    private final CompoundTag settings;
    private final BiomeLayerSampler biomeSampler;
    private final BiomeLayerSampler oceanSampler;
    
    private boolean alphaWinterMode = false;
    private boolean alphaPlus = false;
    private boolean generateVanillaBiomesAlpha = false;
    
    private static final double[] TEMP_HUMID_POINT = new double[2];

    public AlphaBiomeSource(long seed, Registry<Biome> registry, CompoundTag settings) {
        super(
            AlphaBiomes.getBiomeList(
                settings.contains("generateVanillaBiomesAlpha") ? 
                    settings.getBoolean("generateVanillaBiomesAlpha") : 
                    false
        ).stream().map((registryKey) -> () -> (Biome) registry.get(registryKey)));
                

        this.seed = seed;
        this.biomeRegistry = registry;
        this.settings = settings;
        
        this.alphaWinterMode = false;
        this.alphaPlus = false;
        
        if (settings.contains("alphaWinterMode")) this.alphaWinterMode = settings.getBoolean("alphaWinterMode");
        if (settings.contains("alphaPlus")) this.alphaPlus = settings.getBoolean("alphaPlus");
        if (settings.contains("generateVanillaBiomesAlpha")) this.generateVanillaBiomesAlpha = settings.getBoolean("generateVanillaBiomesAlpha");
        
        this.biomeSampler = this.generateVanillaBiomesAlpha ? BetaBiomeLayer.build(seed, false, 4, -1) : null;
        this.oceanSampler = this.generateVanillaBiomesAlpha ? BetaOceanLayer.build(seed, false, 6, -1) : null;
        
        BiomeUtil.setSeed(this.seed);

    }

    @Override
    public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        Biome biome;
        int absX = biomeX << 2;
        int absZ = biomeZ << 2;

        if (this.generateVanillaBiomesAlpha) {
            biome = biomeSampler.sample(this.biomeRegistry, biomeX, biomeZ);
        } else if (this.alphaPlus) {
            // Sample biome at this one absolute coordinate.
            BiomeUtil.fetchTempHumidAtPoint(TEMP_HUMID_POINT, absX, absZ);

            biome = getBiome((float)TEMP_HUMID_POINT[0], (float)TEMP_HUMID_POINT[1], this.biomeRegistry);
        } else if (alphaWinterMode) {
            biome = this.biomeRegistry.get(AlphaBiomes.ALPHA_WINTER_ID);
        } else {
            biome = this.biomeRegistry.get(AlphaBiomes.ALPHA_ID);
        }

        return biome;
    }
    
    @Override
    public Biome getOceanBiomeForNoiseGen(int biomeX, int biomeZ) {
        return this.generateVanillaBiomesAlpha ? this.oceanSampler.sample(this.biomeRegistry, biomeX, biomeZ) : this.getBiomeForNoiseGen(biomeX, 0, biomeZ);
    }

    public static Biome getBiome(float temp, float humid, Registry<Biome> registry) {
        if (temp < 0.5F) {
            return registry.get(AlphaBiomes.ALPHA_WINTER_ID);
        }

        return registry.get(AlphaBiomes.ALPHA_ID);
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

    @Override
    public boolean usesVanillaBiomes() {
        // TODO Auto-generated method stub
        return this.generateVanillaBiomesAlpha;
    }
}
