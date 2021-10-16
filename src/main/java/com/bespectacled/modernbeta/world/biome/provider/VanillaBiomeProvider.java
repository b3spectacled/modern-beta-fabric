package com.bespectacled.modernbeta.world.biome.provider;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.bespectacled.modernbeta.api.world.biome.BiomeProvider;
import com.bespectacled.modernbeta.mixin.MixinVanillaBiomeParametersInvoker;
import com.bespectacled.modernbeta.world.gen.OldChunkGeneratorSettings;
import com.bespectacled.modernbeta.world.gen.OldGeneratorConfig;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.biome.source.util.VanillaBiomeParameters;
import net.minecraft.world.gen.NoiseColumnSampler;
import net.minecraft.world.gen.random.ChunkRandom;

public class VanillaBiomeProvider extends BiomeProvider {
    private MultiNoiseBiomeSource biomeSource;
    private MultiNoiseUtil.Entries<Biome> biomeEntries;
    
    private MultiNoiseUtil.MultiNoiseSampler noiseSampler;
    
    public VanillaBiomeProvider(long seed, NbtCompound settings, Registry<Biome> biomeRegistry) {
        super(seed, settings, biomeRegistry);
        
        ImmutableList.Builder<Pair<MultiNoiseUtil.NoiseHypercube, Supplier<Biome>>> builder = ImmutableList.builder();
        VanillaBiomeParameters biomeParameters = new VanillaBiomeParameters();
        
        Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters = 
            pair -> builder.add(pair.mapSecond(key -> () -> biomeRegistry.getOrThrow(key)));
            
        MixinVanillaBiomeParametersInvoker invoker = ((MixinVanillaBiomeParametersInvoker)(Object)biomeParameters);
        
        invoker.invokeWriteMixedBiomes(parameters, MultiNoiseUtil.ParameterRange.of(-1.0f, -0.93333334f));
        invoker.invokeWritePlainsBiomes(parameters, MultiNoiseUtil.ParameterRange.of(-0.93333334f, -0.7666667f));
        invoker.invokeWriteMountainousBiomes(parameters, MultiNoiseUtil.ParameterRange.of(-0.7666667f, -0.56666666f));
        invoker.invokeWritePlainsBiomes(parameters, MultiNoiseUtil.ParameterRange.of(-0.56666666f, -0.4f));
        invoker.invokeWriteMixedBiomes(parameters, MultiNoiseUtil.ParameterRange.of(-0.4f, -0.4f));
        invoker.invokeWritePlainsBiomes(parameters, MultiNoiseUtil.ParameterRange.of(0.4f, 0.56666666f));
        invoker.invokeWriteMountainousBiomes(parameters, MultiNoiseUtil.ParameterRange.of(0.56666666f, 0.7666667f));
        invoker.invokeWritePlainsBiomes(parameters, MultiNoiseUtil.ParameterRange.of(0.7666667f, 0.93333334f));
        invoker.invokeWriteMixedBiomes(parameters, MultiNoiseUtil.ParameterRange.of(0.93333334f, 1.0f));
        
        this.biomeEntries = new MultiNoiseUtil.Entries<Biome>(builder.build());
        this.biomeSource = new MultiNoiseBiomeSource(this.biomeEntries, Optional.empty());
        
        this.noiseSampler = new NoiseColumnSampler(
            4, 
            8, 
            MathHelper.floorDiv(192, 8), 
            OldGeneratorConfig.BETA_SHAPE_CONFIG, 
            OldChunkGeneratorSettings.VANILLA_MN_PARAMS, 
            false,
            seed,
            ChunkRandom.class_6675.XOROSHIRO
        );
    }

    @Override
    public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        return this.biomeSource.getBiome(biomeX, biomeY, biomeZ, this.noiseSampler);
    }
    
    @Override
    public List<RegistryKey<Biome>> getBiomesForRegistry() {
        return List.of();
    }
    
    private MultiNoiseBiomeSource init(Registry<Biome> biomeRegistry) {
        ImmutableList.Builder<Pair<MultiNoiseUtil.NoiseHypercube, Supplier<Biome>>> builder = ImmutableList.builder();
        VanillaBiomeParameters biomeParameters = new VanillaBiomeParameters();
        
        Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters = 
            pair -> builder.add(pair.mapSecond(key -> () -> biomeRegistry.getOrThrow(key)));
            
        MixinVanillaBiomeParametersInvoker invoker = ((MixinVanillaBiomeParametersInvoker)(Object)biomeParameters);
        
        invoker.invokeWriteMixedBiomes(parameters, MultiNoiseUtil.ParameterRange.of(-1.0f, -0.93333334f));
        invoker.invokeWritePlainsBiomes(parameters, MultiNoiseUtil.ParameterRange.of(-0.93333334f, -0.7666667f));
        invoker.invokeWriteMountainousBiomes(parameters, MultiNoiseUtil.ParameterRange.of(-0.7666667f, -0.56666666f));
        invoker.invokeWritePlainsBiomes(parameters, MultiNoiseUtil.ParameterRange.of(-0.56666666f, -0.4f));
        invoker.invokeWriteMixedBiomes(parameters, MultiNoiseUtil.ParameterRange.of(-0.4f, -0.4f));
        invoker.invokeWritePlainsBiomes(parameters, MultiNoiseUtil.ParameterRange.of(0.4f, 0.56666666f));
        invoker.invokeWriteMountainousBiomes(parameters, MultiNoiseUtil.ParameterRange.of(0.56666666f, 0.7666667f));
        invoker.invokeWritePlainsBiomes(parameters, MultiNoiseUtil.ParameterRange.of(0.7666667f, 0.93333334f));
        invoker.invokeWriteMixedBiomes(parameters, MultiNoiseUtil.ParameterRange.of(0.93333334f, 1.0f));
        
        return new MultiNoiseBiomeSource(new MultiNoiseUtil.Entries<Biome>(builder.build()), Optional.empty());
    }
}
