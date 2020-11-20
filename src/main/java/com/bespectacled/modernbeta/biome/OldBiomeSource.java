package com.bespectacled.modernbeta.biome;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.biome.layer.VanillaBiomeLayer;
import com.bespectacled.modernbeta.biome.layer.VanillaOceanLayer;
import com.bespectacled.modernbeta.biome.provider.IOldBiomeProvider;
import com.bespectacled.modernbeta.biome.provider.InfBiomeProvider;
import com.bespectacled.modernbeta.util.WorldEnum;
import com.bespectacled.modernbeta.util.WorldEnum.BiomeType;
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

public class OldBiomeSource extends BiomeSource implements IOldBiomeSource {
    
    public static final Codec<OldBiomeSource> CODEC = RecordCodecBuilder.create(instance -> instance
        .group(
            Codec.LONG.fieldOf("seed").stable().forGetter(biomeSource -> biomeSource.seed),
            RegistryLookupCodec.of(Registry.BIOME_KEY).forGetter(biomeSource -> biomeSource.biomeRegistry),
            CompoundTag.CODEC.fieldOf("settings").forGetter(biomeSource -> biomeSource.settings)
        ).apply(instance, (instance).stable(OldBiomeSource::new)));
    
    private final long seed;
    private final Registry<Biome> biomeRegistry;
    private final CompoundTag settings;
    private final boolean generateOceans;
    
    private final WorldType worldType;
    private final BiomeType biomeType;
    
    private final IOldBiomeProvider biomeProvider;
    private final BiomeLayerSampler biomeSampler;
    private final BiomeLayerSampler oceanSampler;
    
    public OldBiomeSource(long seed, Registry<Biome> biomeRegistry, CompoundTag settings) {
        super(IOldBiomeProvider.getBiomeRegistryList(settings).stream().map((registryKey) -> () -> (Biome) biomeRegistry.get(registryKey)));
        
        this.seed = seed;
        this.biomeRegistry = biomeRegistry;
        this.settings = settings;
        this.generateOceans = settings.contains("generateOceans") ? settings.getBoolean("generateOceans") : true;
        
        this.worldType = WorldEnum.getWorldType(settings);
        this.biomeType = WorldEnum.getBiomeType(settings);
        
        this.biomeProvider = this.biomeType != BiomeType.VANILLA ? IOldBiomeProvider.getBiomeProvider(seed, settings) : null;
        this.biomeSampler = this.biomeType == BiomeType.VANILLA ? VanillaBiomeLayer.build(seed, false, 4, -1) : null;
        this.oceanSampler = this.biomeType == BiomeType.VANILLA ? VanillaOceanLayer.build(seed, false, 6, -1) : null;
    }

    @Override
    public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        return this.biomeType == BiomeType.VANILLA ? 
            this.biomeSampler.sample(biomeRegistry, biomeX, biomeZ) :
            this.biomeProvider.getBiomeForNoiseGen(biomeRegistry, biomeX, biomeY, biomeZ);
    }

    @Override
    public Biome getOceanBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        return this.biomeType == BiomeType.VANILLA ? 
            this.oceanSampler.sample(biomeRegistry, biomeX, biomeZ) :
            this.biomeProvider.getOceanBiomeForNoiseGen(biomeRegistry, biomeX, biomeY, biomeZ);
    }

    @Override
    public Registry<Biome> getBiomeRegistry() {
        return this.biomeRegistry;
    }
    
    @Override
    public boolean generateOceans() {
        return (this.isVanilla() || this.isBeta()) && this.generateOceans;
    }

    @Override
    public boolean isVanilla() {
        return this.worldType != WorldType.INDEV && this.biomeType == BiomeType.VANILLA;
    }
    
    @Override
    public boolean isBeta() {
        return this.worldType != WorldType.INDEV && this.biomeType == BiomeType.BETA;
        // || this.biomeType == BiomeType.ICE_DESERT;
    }
    
    @Override
    public boolean isSkyDim() {
        return this.worldType != WorldType.INDEV && this.biomeType == BiomeType.SKY;
    }
    
    @Override
    protected Codec<? extends BiomeSource> getCodec() {
        return OldBiomeSource.CODEC;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public BiomeSource withSeed(long seed) {
        return new OldBiomeSource(seed, this.biomeRegistry, this.settings);
    }

    public static void register() {
        Registry.register(Registry.BIOME_SOURCE, new Identifier(ModernBeta.ID, "old"), CODEC);
    }

    
    

}
