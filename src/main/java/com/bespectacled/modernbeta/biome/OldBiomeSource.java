package com.bespectacled.modernbeta.biome;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.biome.layer.VanillaBiomeLayer;
import com.bespectacled.modernbeta.biome.layer.VanillaOceanLayer;
import com.bespectacled.modernbeta.biome.provider.IOldBiomeProvider;
import com.bespectacled.modernbeta.biome.provider.OldBiomeProviderUtil;
import com.bespectacled.modernbeta.util.WorldEnum.BiomeType;
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
    private final BiomeLayerSampler biomeSampler;
    private final BiomeLayerSampler oceanSampler;
    
    private final WorldType worldType;
    private final BiomeType biomeType;
    private final IOldBiomeProvider biomeProvider;
    
    public OldBiomeSource(long seed, Registry<Biome> biomeRegistry, CompoundTag settings) {
        super(OldBiomeProviderUtil.getBiomeRegistryList(settings).stream().map((registryKey) -> () -> (Biome) biomeRegistry.get(registryKey)));
        
        this.seed = seed;
        this.biomeRegistry = biomeRegistry;
        this.settings = settings;
        
        this.worldType = OldBiomeProviderUtil.getWorldType(settings);
        this.biomeType = OldBiomeProviderUtil.getBiomeType(settings);
        
        System.out.println("World/Biome Type: " + this.worldType.getName() + ", " + this.biomeType.getName());
        
        this.biomeProvider = this.biomeType != BiomeType.VANILLA ? OldBiomeProviderUtil.getBiomeProvider(seed, worldType, biomeType) : null;
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
        return true;
    }

    @Override
    public boolean isVanilla() {
        return this.biomeType == BiomeType.VANILLA;
    }
    
    @Override
    public boolean isBeta() {
        return this.biomeType == BiomeType.BETA;
    }
    
    @Override
    public boolean isSkyDim() {
        return this.biomeType == BiomeType.SKY;
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
