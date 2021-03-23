package com.bespectacled.modernbeta.biome;

import java.util.List;
import java.util.function.Supplier;

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
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.biome.GenerationSettings.Builder;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;

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
    private final Biome edgeBiome; // For Indev worlds
    
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
        this.edgeBiome = this.biomeType == BiomeType.SINGLE ? this.createEdgeBiome() : null;
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
        if (this.biomeType != BiomeType.SINGLE) return null;
        
        Biome mainBiome = this.biomeRegistry.get(((SingleBiomeProvider) this.biomeProvider).getBiomeId());
        GenerationSettings genSettings = mainBiome.getGenerationSettings();
        GenerationSettings.Builder genSettingsBuilder = new GenerationSettings.Builder().surfaceBuilder(genSettings.getSurfaceBuilder());
        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
        
        // Add features as necessary so that edge of Indev levels blend in
        List<List<Supplier<ConfiguredFeature<?, ?>>>> featureSteps = genSettings.getFeatures();
        for (int i = 0; i < featureSteps.size(); ++i) {
            if (i == GenerationStep.Feature.TOP_LAYER_MODIFICATION.ordinal()) { // Add freeze top layer feature
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
