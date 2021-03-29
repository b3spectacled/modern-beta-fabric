package com.bespectacled.modernbeta.biome;

import java.util.List;
import java.util.function.Supplier;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.AbstractBiomeProvider;
import com.bespectacled.modernbeta.api.BiomeProviderType;
import com.bespectacled.modernbeta.biome.provider.*;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.dynamic.RegistryLookupCodec;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;

public class OldBiomeSource extends BiomeSource {
    
    public static final Codec<OldBiomeSource> CODEC = RecordCodecBuilder.create(instance -> instance
        .group(
            Codec.LONG.fieldOf("seed").stable().forGetter(biomeSource -> biomeSource.seed),
            RegistryLookupCodec.of(Registry.BIOME_KEY).forGetter(biomeSource -> biomeSource.biomeRegistry),
            NbtCompound.CODEC.fieldOf("settings").forGetter(biomeSource -> biomeSource.providerSettings)
        ).apply(instance, (instance).stable(OldBiomeSource::new)));
    
    private final long seed;
    private final Registry<Biome> biomeRegistry;
    private final NbtCompound providerSettings;
    
    //private final OldBiomeProviderType biomeType;
    private final String biomeProviderType;
    private final AbstractBiomeProvider biomeProvider;
    private final Biome edgeBiome; // For Indev worlds
    
    public OldBiomeSource(long seed, Registry<Biome> biomeRegistry, NbtCompound settings) {
        super(
            BiomeProviderType.getBiomeProvider(BiomeProviderType.getBiomeProviderType(settings))
            .apply(seed, settings)
            .getBiomesForRegistry()
            .stream()
            .map((registryKey) -> () -> (Biome) biomeRegistry.get(registryKey))
        );
        
        this.seed = seed;
        this.biomeRegistry = biomeRegistry;
        this.providerSettings = settings;
        
        this.biomeProviderType = BiomeProviderType.getBiomeProviderType(settings);
        this.biomeProvider = BiomeProviderType.getBiomeProvider(this.biomeProviderType).apply(seed, settings);
        this.edgeBiome = this.biomeProviderType == BiomeProviderType.SINGLE ? this.createEdgeBiome() : null;
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
    
    public Biome getEdgeBiome() {
        return this.edgeBiome;
    }

    public Registry<Biome> getBiomeRegistry() {
        return this.biomeRegistry;
    }

    public boolean isVanilla() {
        return this.biomeProviderType == BiomeProviderType.VANILLA;
    }
    
    public boolean isBeta() {
        return this.biomeProviderType == BiomeProviderType.BETA;
    }
    
    public boolean isSingle() {
        return this.biomeProviderType == BiomeProviderType.SINGLE;
    }
    
    public String getBiomeType() {
        return this.biomeProviderType;
    }
    
    public AbstractBiomeProvider getBiomeProvider() {
        return this.biomeProvider;
    }
    
    public NbtCompound getProviderSettings() {
        return this.providerSettings;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public BiomeSource withSeed(long seed) {
        return new OldBiomeSource(seed, this.biomeRegistry, this.providerSettings);
    }
    
    @Override
    protected Codec<? extends BiomeSource> getCodec() {
        return OldBiomeSource.CODEC;
    }
    
    private Biome createEdgeBiome() {
        if (this.biomeProviderType != BiomeProviderType.SINGLE) return null;
        
        Biome mainBiome = this.biomeRegistry.get(((SingleBiomeProvider) this.biomeProvider).getBiomeId());
        GenerationSettings genSettings = mainBiome.getGenerationSettings();
        GenerationSettings.Builder genSettingsBuilder = new GenerationSettings.Builder().surfaceBuilder(genSettings.getSurfaceBuilder());
        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
        
        // Add features as necessary so that edge of Indev levels blend in
        List<List<Supplier<ConfiguredFeature<?, ?>>>> featureSteps = genSettings.getFeatures();
        for (int i = 0; i < featureSteps.size(); ++i) {
            // Add top layer modifiers, plant features
            if (i == GenerationStep.Feature.TOP_LAYER_MODIFICATION.ordinal()) {
                List<Supplier<ConfiguredFeature<?, ?>>> configuredFeatures = featureSteps.get(i);
                for (Supplier<ConfiguredFeature<?, ?>> supplier : configuredFeatures) {
                    genSettingsBuilder.feature(i, supplier);
                }
            }
        }
        
        return new Biome.Builder()
            .precipitation(mainBiome.getPrecipitation())
            .category(mainBiome.getCategory())
            .depth(mainBiome.getDepth())
            .scale(mainBiome.getScale())
            .temperature(mainBiome.getTemperature())
            .downfall(mainBiome.getDownfall())
            .effects(mainBiome.getEffects())
            .generationSettings(genSettingsBuilder.build())
            .spawnSettings(spawnSettings.build())
            .build();
    }

    public static void register() {
        Registry.register(Registry.BIOME_SOURCE, ModernBeta.createId("old"), CODEC);
    }
}
