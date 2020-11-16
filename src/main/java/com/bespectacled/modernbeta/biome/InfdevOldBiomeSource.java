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

public class InfdevOldBiomeSource extends BiomeSource implements IOldBiomeSource {

    public static final Codec<InfdevOldBiomeSource> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(
                Codec.LONG.fieldOf("seed").stable().forGetter(oldInfdevBiomeSource -> oldInfdevBiomeSource.seed),
                RegistryLookupCodec.of(Registry.BIOME_KEY).forGetter(oldInfdevBiomeSource -> oldInfdevBiomeSource.biomeRegistry),
                CompoundTag.CODEC.fieldOf("settings").forGetter(oldInfdevBiomeSource -> oldInfdevBiomeSource.settings)
            ).apply(instance, (instance).stable(InfdevOldBiomeSource::new)));

    private final long seed;
    public final Registry<Biome> biomeRegistry;
    private final CompoundTag settings;
    private final BiomeLayerSampler biomeSampler;
    private final BiomeLayerSampler oceanSampler;
    
    private boolean infdevOldWinterMode = false;
    private boolean infdevOldPlus = false;
    private boolean generateVanillaBiomesInfdevOld = false;
    
    private static final double[] TEMP_HUMID_POINT = new double[2];

    public InfdevOldBiomeSource(long seed, Registry<Biome> registry, CompoundTag settings) {
        super(
            InfdevOldBiomes.getBiomeList(
                settings.contains("generateVanillaBiomesInfdevOld") ? 
                    settings.getBoolean("generateVanillaBiomesInfdevOld") : 
                    false
        ).stream().map((registryKey) -> () -> (Biome) registry.get(registryKey)));

        this.seed = seed;
        this.biomeRegistry = registry;
        this.settings = settings;
        
        this.infdevOldWinterMode = false;
        this.infdevOldPlus = false;
        
        if (settings.contains("infdevOldWinterMode")) this.infdevOldWinterMode = settings.getBoolean("infdevOldWinterMode");
        if (settings.contains("infdevOldPlus")) this.infdevOldPlus = settings.getBoolean("infdevOldPlus");
        if (settings.contains("generateVanillaBiomesInfdevOld")) this.generateVanillaBiomesInfdevOld = settings.getBoolean("generateVanillaBiomesInfdevOld");
        
        this.biomeSampler = this.generateVanillaBiomesInfdevOld ? BetaBiomeLayer.build(seed, false, 4, -1) : null;
        this.oceanSampler = this.generateVanillaBiomesInfdevOld ? BetaOceanLayer.build(seed, false, 6, -1) : null;

        BiomeUtil.setSeed(this.seed);
    }

    @Override
    public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        Biome biome;
        int absX = biomeX << 2;
        int absZ = biomeZ << 2;

        if (this.generateVanillaBiomesInfdevOld) {
            biome = this.biomeSampler.sample(this.biomeRegistry, biomeX, biomeZ);
        } else if (this.infdevOldPlus) {
            // Sample biome at this one absolute coordinate.
            BiomeUtil.fetchTempHumidAtPoint(TEMP_HUMID_POINT, absX, absZ);

            biome = getBiome((float)TEMP_HUMID_POINT[0], (float)TEMP_HUMID_POINT[1], this.biomeRegistry);
        } else if (infdevOldWinterMode) {
            biome = this.biomeRegistry.get(InfdevOldBiomes.INFDEV_OLD_WINTER_ID);
        } else {
            biome = this.biomeRegistry.get(InfdevOldBiomes.INFDEV_OLD_ID);
        }

        return biome;
    }
    
    @Override
    public Biome getOceanBiomeForNoiseGen(int biomeX, int biomeZ) {
        return this.generateVanillaBiomesInfdevOld ? this.oceanSampler.sample(this.biomeRegistry, biomeX, biomeZ) : this.getBiomeForNoiseGen(biomeX, 0, biomeZ);
    }

    public static Biome getBiome(float temp, float humid, Registry<Biome> registry) {
        if (temp < 0.5F) {
            return registry.get(InfdevOldBiomes.INFDEV_OLD_WINTER_ID);
        }

        return registry.get(InfdevOldBiomes.INFDEV_OLD_ID);
    }

    @Override
    protected Codec<? extends BiomeSource> getCodec() {
        return InfdevOldBiomeSource.CODEC;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public BiomeSource withSeed(long seed) {
        return new InfdevOldBiomeSource(seed, this.biomeRegistry, this.settings);
    }

    public static void register() {
        Registry.register(Registry.BIOME_SOURCE, new Identifier(ModernBeta.ID, "infdev_old_biome_source"), CODEC);
        //ModernBeta.LOGGER.log(Level.INFO, "Registered Infdev biome source.");
    }

    @Override
    public boolean generateVanillaBiomes() {
        return this.generateVanillaBiomesInfdevOld;
    }

}
