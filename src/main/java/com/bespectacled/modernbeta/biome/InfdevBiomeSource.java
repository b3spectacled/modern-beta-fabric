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

public class InfdevBiomeSource extends BiomeSource implements IOldBiomeSource {

    public static final Codec<InfdevBiomeSource> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(
                Codec.LONG.fieldOf("seed").stable().forGetter(infdevBiomeSource -> infdevBiomeSource.seed),
                RegistryLookupCodec.of(Registry.BIOME_KEY).forGetter(infdevBiomeSource -> infdevBiomeSource.biomeRegistry),
                CompoundTag.CODEC.fieldOf("settings").forGetter(infdevBiomeSource -> infdevBiomeSource.settings)
            ).apply(instance, (instance).stable(InfdevBiomeSource::new)));

    private final long seed;
    public final Registry<Biome> biomeRegistry;
    private final CompoundTag settings;
    private final BiomeLayerSampler biomeSampler;
    private final BiomeLayerSampler oceanSampler;
    
    private boolean infdevWinterMode = false;
    private boolean infdevPlus = false;
    private boolean generateVanillaBiomesInfdev = false;
    
    private static final double[] TEMP_HUMID_POINT = new double[2];

    public InfdevBiomeSource(long seed, Registry<Biome> registry, CompoundTag settings) {
        super(
            InfdevBiomes.getBiomeList(
                settings.contains("generateVanillaBiomesInfdev") ? 
                    settings.getBoolean("generateVanillaBiomesInfdev") : 
                    false
        ).stream().map((registryKey) -> () -> (Biome) registry.get(registryKey)));

        this.seed = seed;
        this.biomeRegistry = registry;
        this.settings = settings;
        
        this.infdevWinterMode = false;
        this.infdevPlus = false;
        
        if (settings.contains("infdevWinterMode")) this.infdevWinterMode = settings.getBoolean("infdevWinterMode");
        if (settings.contains("infdevPlus")) this.infdevPlus = settings.getBoolean("infdevPlus");
        if (settings.contains("generateVanillaBiomesInfdev")) this.generateVanillaBiomesInfdev = settings.getBoolean("generateVanillaBiomesInfdev");
        
        this.biomeSampler = this.generateVanillaBiomesInfdev ? BetaBiomeLayer.build(seed, false, 4, -1) : null;
        this.oceanSampler = this.generateVanillaBiomesInfdev ? BetaOceanLayer.build(seed, false, 6, -1) : null;

        BiomeUtil.setSeed(this.seed);
    }

    @Override
    public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        Biome biome;
        int absX = biomeX << 2;
        int absZ = biomeZ << 2;

        if (this.generateVanillaBiomesInfdev) {
            biome = this.biomeSampler.sample(this.biomeRegistry, biomeX, biomeZ);
        } else if (this.infdevPlus) {
            // Sample biome at this one absolute coordinate.
            BiomeUtil.fetchTempHumidAtPoint(TEMP_HUMID_POINT, absX, absZ);

            biome = getBiome((float)TEMP_HUMID_POINT[0], (float)TEMP_HUMID_POINT[1], this.biomeRegistry);
        } else if (infdevWinterMode) {
            biome = this.biomeRegistry.get(InfdevBiomes.INFDEV_WINTER_ID);
        } else {
            biome = this.biomeRegistry.get(InfdevBiomes.INFDEV_ID);
        }

        return biome;
    }
    
    @Override
    public Biome getOceanBiomeForNoiseGen(int biomeX, int biomeZ) {
        return this.generateVanillaBiomesInfdev ? this.oceanSampler.sample(this.biomeRegistry, biomeX, biomeZ) : this.getBiomeForNoiseGen(biomeX, 0, biomeZ);
    }

    public static Biome getBiome(float temp, float humid, Registry<Biome> registry) {
        if (temp < 0.5F) {
            return registry.get(InfdevBiomes.INFDEV_WINTER_ID);
        }

        return registry.get(InfdevBiomes.INFDEV_ID);
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

    @Override
    public boolean generateVanillaBiomes() {
        return this.generateVanillaBiomesInfdev;
    }

}
