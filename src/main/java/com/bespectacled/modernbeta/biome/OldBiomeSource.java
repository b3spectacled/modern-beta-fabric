package com.bespectacled.modernbeta.biome;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.biome.provider.*;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.dynamic.RegistryLookupCodec;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;

public class OldBiomeSource extends BiomeSource {
    
    public static final Codec<OldBiomeSource> CODEC = RecordCodecBuilder.create(instance -> instance
        .group(
            Codec.LONG.fieldOf("seed").stable().forGetter(biomeSource -> biomeSource.seed),
            RegistryLookupCodec.of(Registry.BIOME_KEY).forGetter(biomeSource -> biomeSource.biomeRegistry),
            CompoundTag.CODEC.fieldOf("settings").forGetter(biomeSource -> biomeSource.providerSettings)
        ).apply(instance, (instance).stable(OldBiomeSource::new)));
    
    private final long seed;
    private final Registry<Biome> biomeRegistry;
    private final CompoundTag providerSettings;
    
    private final BiomeType biomeType;
    private final AbstractBiomeProvider biomeProvider;
    
    public OldBiomeSource(long seed, Registry<Biome> biomeRegistry, CompoundTag settings) {
        super(BiomeType
            .getBiomeType(settings)
            .createBiomeProvider(seed, settings)
            .getBiomesForRegistry()
            .stream()
            .map((registryKey) -> () -> (Biome) biomeRegistry.get(registryKey))
        );
        
        this.seed = seed;
        this.biomeRegistry = biomeRegistry;
        this.providerSettings = settings;
        
        this.biomeType = BiomeType.getBiomeType(settings);
        this.biomeProvider = BiomeType.getBiomeType(settings).createBiomeProvider(seed, settings);
    }

    @Override
    public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        return this.biomeProvider.getBiomeForNoiseGen(this.biomeRegistry, biomeX, biomeY, biomeZ);
    }

    public Biome getOceanBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        return this.biomeProvider.getOceanBiomeForNoiseGen(this.biomeRegistry, biomeX, biomeY, biomeZ);
    }
    
    public Biome getBiomeForSurfaceGen(int x, int y, int z) {
        return this.biomeProvider.getBiomeForSurfaceGen(this.biomeRegistry, x, y, z);
    }

    public Registry<Biome> getBiomeRegistry() {
        return this.biomeRegistry;
    }

    public boolean isVanilla() {
        return this.biomeType == BiomeType.VANILLA;
    }
    
    public boolean isBeta() {
        return this.biomeType == BiomeType.BETA;
    }
    
    public boolean isSingle() {
        return this.biomeType == BiomeType.SINGLE;
    }
    
    public BiomeType getBiomeType() {
        return this.biomeType;
    }
    
    public AbstractBiomeProvider getBiomeProvider() {
        return this.biomeProvider;
    }
    
    public CompoundTag getProviderSettings() {
        return this.providerSettings;
    }
    
    @Override
    protected Codec<? extends BiomeSource> getCodec() {
        return OldBiomeSource.CODEC;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public BiomeSource withSeed(long seed) {
        return new OldBiomeSource(seed, this.biomeRegistry, this.providerSettings);
    }

    public static void register() {
        Registry.register(Registry.BIOME_SOURCE, ModernBeta.createId("old"), CODEC);
    }
}
