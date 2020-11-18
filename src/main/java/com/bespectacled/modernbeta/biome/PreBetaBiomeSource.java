package com.bespectacled.modernbeta.biome;

import java.util.List;
import java.util.Map;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.biome.layer.VanillaBiomeLayer;
import com.bespectacled.modernbeta.biome.layer.VanillaOceanLayer;
import com.bespectacled.modernbeta.util.BiomeUtil;
import com.bespectacled.modernbeta.util.WorldEnum.PreBetaBiomeType;
import com.bespectacled.modernbeta.util.WorldEnum.WorldType;
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

public class PreBetaBiomeSource extends BiomeSource implements IOldBiomeSource {

    public static final Codec<PreBetaBiomeSource> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(
                Codec.LONG.fieldOf("seed").stable().forGetter(preBetaBiomeSource -> preBetaBiomeSource.seed),
                RegistryLookupCodec.of(Registry.BIOME_KEY).forGetter(preBetaBiomeSource -> preBetaBiomeSource.biomeRegistry),
                CompoundTag.CODEC.fieldOf("settings").forGetter(settings -> settings.settings)
            ).apply(instance, (instance).stable(PreBetaBiomeSource::new)));

    private final long seed;
    private final Registry<Biome> biomeRegistry;
    private final CompoundTag settings;
    private final BiomeLayerSampler biomeSampler;
    private final BiomeLayerSampler oceanSampler;
    //private final BetaBiomeSource betaBiomeSampler;
    
    private final WorldType worldType;
    private final PreBetaBiomeType biomeType;
    private final Map<PreBetaBiomeType, Identifier> biomeMapping;
    
    private static final double[] TEMP_HUMID_POINT = new double[2];

    public PreBetaBiomeSource(long seed, Registry<Biome> registry, CompoundTag settings) {
        super(PreBetaBiomes.getBiomeRegistryList(settings).stream().map((registryKey) -> () -> (Biome) registry.get(registryKey)));
                
        this.seed = seed;
        this.biomeRegistry = registry;
        this.settings = settings;
        
        this.worldType = PreBetaBiomes.getWorldType(settings);
        this.biomeType = PreBetaBiomes.getBiomeType(settings);
        this.biomeMapping = PreBetaBiomes.getBiomeMap(this.worldType);
        
        this.biomeSampler = this.biomeType == PreBetaBiomeType.VANILLA ? VanillaBiomeLayer.build(seed, false, 4, -1) : null;
        this.oceanSampler = this.biomeType == PreBetaBiomeType.VANILLA ? VanillaOceanLayer.build(seed, false, 6, -1) : null;
        //this.betaBiomeSampler = this.biomeType == PreBetaBiomeType.BETA ? new BetaBiomeSource(seed, registry, settings) : null;
        
        BiomeUtil.setSeed(this.seed);

    }

    @Override
    public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        Biome biome;
        int absX = biomeX << 2;
        int absZ = biomeZ << 2;
        
        switch(this.biomeType) {
            case CLASSIC:
                biome = this.biomeRegistry.get(this.biomeMapping.get(PreBetaBiomeType.CLASSIC));
                break;
            case WINTER:
                biome = this.biomeRegistry.get(this.biomeMapping.get(PreBetaBiomeType.WINTER));
                break;
            case PLUS:
                // Sample biome at this one absolute coordinate.
                BiomeUtil.fetchTempHumidAtPoint(TEMP_HUMID_POINT, absX, absZ);
    
                biome = getBiome((float)TEMP_HUMID_POINT[0], (float)TEMP_HUMID_POINT[1], this.biomeRegistry);
                break;
            case VANILLA: 
                biome = biomeSampler.sample(this.biomeRegistry, biomeX, biomeZ);
                break;
            default:
                biome = this.biomeRegistry.get(this.biomeMapping.get(PreBetaBiomeType.CLASSIC));
                break;      
        }
        

        return biome;
    }
    
    @Override
    public Biome getOceanBiomeForNoiseGen(int biomeX, int biomeZ) {
        return this.biomeType == PreBetaBiomeType.VANILLA ? 
            this.oceanSampler.sample(this.biomeRegistry, biomeX, biomeZ) : 
            this.getBiomeForNoiseGen(biomeX, 0, biomeZ);
    }

    public Biome getBiome(float temp, float humid, Registry<Biome> registry) {
        if (temp < 0.5F) {
            return registry.get(this.biomeMapping.get(PreBetaBiomeType.WINTER));
        }

        return registry.get(this.biomeMapping.get(PreBetaBiomeType.CLASSIC));
    }

    @Override
    protected Codec<? extends BiomeSource> getCodec() {
        return PreBetaBiomeSource.CODEC;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public BiomeSource withSeed(long seed) {
        return new PreBetaBiomeSource(seed, this.biomeRegistry, this.settings);
    }

    public static void register() {
        Registry.register(Registry.BIOME_SOURCE, new Identifier(ModernBeta.ID, "prebeta"), CODEC);
        //ModernBeta.LOGGER.log(Level.INFO, "Registered Alpha biome source.");
    }

    @Override
    public Registry<Biome> getBiomeRegistry() {
        return this.biomeRegistry;
    }
    
    @Override
    public boolean generateVanillaBiomes() {
        return this.biomeType == PreBetaBiomeType.VANILLA;
    }

    @Override
    public boolean isSkyDim() {
        return false;
    }

}
