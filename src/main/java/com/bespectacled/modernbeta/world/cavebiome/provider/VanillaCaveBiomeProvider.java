package com.bespectacled.modernbeta.world.cavebiome.provider;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import com.bespectacled.modernbeta.api.world.cavebiome.CaveBiomeProvider;
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

public class VanillaCaveBiomeProvider extends CaveBiomeProvider {
    private MultiNoiseBiomeSource biomeSource;
    private MultiNoiseUtil.MultiNoiseSampler noiseSampler;
    
    public VanillaCaveBiomeProvider(long seed, NbtCompound settings, Registry<Biome> biomeRegistry) {
        super(seed, settings, biomeRegistry);
        
        this.biomeSource = null;
        this.noiseSampler = new NoiseColumnSampler(
            4, 
            8, 
            MathHelper.floorDiv(384, 8), 
            OldGeneratorConfig.BETA_SHAPE_CONFIG, 
            OldChunkGeneratorSettings.VANILLA_MN_PARAMS, 
            false, 
            seed,
            ChunkRandom.class_6675.XOROSHIRO
        );
    }

    @Override
    public Biome getBiome(Registry<Biome> biomeRegistry, int biomeX, int biomeY, int biomeZ) {
        if (this.biomeSource == null)
            this.biomeSource = this.init(biomeRegistry);
        
        return this.biomeSource.getBiome(biomeX, biomeY, biomeZ, this.noiseSampler);
    }
    
    @Override
    public List<RegistryKey<Biome>> getBiomesForRegistry() {
        return List.of();
    }
    
    private MultiNoiseBiomeSource init(Registry<Biome> biomeRegistry) {
        ImmutableList.Builder<Pair<MultiNoiseUtil.NoiseHypercube, Supplier<Biome>>> builder = ImmutableList.builder();
        VanillaBiomeParameters biomeParameters = new VanillaBiomeParameters();
        
        ((MixinVanillaBiomeParametersInvoker)(Object)biomeParameters).invokeWriteCaveBiomes(
            pair -> builder.add(pair.mapSecond(key -> () -> biomeRegistry.getOrThrow(key)))
        );
        
        return new MultiNoiseBiomeSource(new MultiNoiseUtil.Entries<Biome>(builder.build()), Optional.empty());
    }
}
