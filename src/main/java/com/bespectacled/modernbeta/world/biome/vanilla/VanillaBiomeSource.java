package com.bespectacled.modernbeta.world.biome.vanilla;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.bespectacled.modernbeta.mixin.MixinVanillaBiomeParametersInvoker;
import com.bespectacled.modernbeta.world.gen.OldChunkGeneratorSettings;
import com.bespectacled.modernbeta.world.gen.OldGeneratorConfig;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.biome.source.util.MultiNoiseUtil.ParameterRange;
import net.minecraft.world.biome.source.util.VanillaBiomeParameters;
import net.minecraft.world.gen.MultiNoiseParameters;
import net.minecraft.world.gen.NoiseColumnSampler;
import net.minecraft.world.gen.chunk.GenerationShapeConfig;
import net.minecraft.world.gen.random.ChunkRandom;

public class VanillaBiomeSource {
    private static final int HORIZONTAL_NOISE_RES = 4;
    private static final int VERTICAL_NOISE_RES = 8;
    private static final int WORLD_HEIGHT = 192; // (128 + 64)
    
    private static final GenerationShapeConfig SHAPE_CONFIG = OldGeneratorConfig.BETA_SHAPE_CONFIG;
    private static final MultiNoiseParameters MN_PARAMETERS = OldChunkGeneratorSettings.VANILLA_MN_PARAMS;
    
    private static final boolean GEN_NOISE_CAVES = false;
    private static final ChunkRandom.class_6675 RANDOM_TYPE = ChunkRandom.class_6675.XOROSHIRO;
    
    private final MultiNoiseUtil.Entries<Biome> biomeEntries;
    private final MultiNoiseBiomeSource biomeSource;
    private final NoiseColumnSampler columnSampler;
    
    private VanillaBiomeSource(ImmutableList<Pair<MultiNoiseUtil.NoiseHypercube, Supplier<Biome>>> biomeList, long seed) {
        this.biomeEntries = new MultiNoiseUtil.Entries<Biome>(biomeList);
        this.biomeSource = new MultiNoiseBiomeSource(this.biomeEntries, Optional.empty());
        this.columnSampler = new NoiseColumnSampler(
            HORIZONTAL_NOISE_RES,
            VERTICAL_NOISE_RES,
            MathHelper.floorDiv(WORLD_HEIGHT, VERTICAL_NOISE_RES),
            SHAPE_CONFIG,
            MN_PARAMETERS,
            GEN_NOISE_CAVES,
            seed,
            RANDOM_TYPE
        );
    }
    
    public Biome getBiome(int biomeX, int biomeY, int biomeZ) {
        return this.biomeSource.getBiome(biomeX, biomeY, biomeZ, this.columnSampler);
    }
    
    public MultiNoiseUtil.Entries<Biome> getBiomeEntries() {
        return this.biomeEntries;
    }
    
    public static class Builder {
        private final ImmutableList.Builder<Pair<MultiNoiseUtil.NoiseHypercube, Supplier<Biome>>> builder;
        private final VanillaBiomeParameters biomeParameters;
        private final Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters;
        private final long seed;
        
        public Builder(Registry<Biome> biomeRegistry, long seed) {
            this.builder = ImmutableList.builder(); 
            this.biomeParameters = new VanillaBiomeParameters();
            this.parameters = pair -> this.builder.add(pair.mapSecond(key -> () -> biomeRegistry.getOrThrow(key)));
            this.seed = seed;
        }
        
        public Builder writeMixedBiomes(ParameterRange range) {
            MixinVanillaBiomeParametersInvoker invoker = this.getInvoker();
            invoker.invokeWriteMixedBiomes(this.parameters, range);
            
            return this;
        }
        
        public Builder writePlainsBiomes(ParameterRange range) {
            MixinVanillaBiomeParametersInvoker invoker = this.getInvoker();
            invoker.invokeWritePlainsBiomes(this.parameters, range);
            
            return this;
        }
        
        public Builder writeMountainousBiomes(ParameterRange range) {
            MixinVanillaBiomeParametersInvoker invoker = this.getInvoker();
            invoker.invokeWriteMountainousBiomes(this.parameters, range);
            
            return this;
        }
        
        public Builder writeOceanBiomes() {
            MixinVanillaBiomeParametersInvoker invoker = this.getInvoker();
            invoker.invokeWriteOceanBiomes(this.parameters);
            
            return this;
        }
        
        public VanillaBiomeSource build() {
            return new VanillaBiomeSource(this.builder.build(), this.seed);
        }
        
        private MixinVanillaBiomeParametersInvoker getInvoker() {
            return ((MixinVanillaBiomeParametersInvoker)(Object)this.biomeParameters);
        }
    }
}
