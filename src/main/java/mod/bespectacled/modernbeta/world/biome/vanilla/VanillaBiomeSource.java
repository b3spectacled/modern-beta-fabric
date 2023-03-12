package mod.bespectacled.modernbeta.world.biome.vanilla;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;

import mod.bespectacled.modernbeta.mixin.MixinDensityFunctionsAccessor;
import mod.bespectacled.modernbeta.mixin.MixinVanillaBiomeParametersAccessor;
import mod.bespectacled.modernbeta.world.gen.OldGeneratorConfig;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.biome.source.util.MultiNoiseUtil.NoiseHypercube;
import net.minecraft.world.biome.source.util.MultiNoiseUtil.ParameterRange;
import net.minecraft.world.gen.chunk.GenerationShapeConfig;
import net.minecraft.world.gen.densityfunction.DensityFunctions;
import net.minecraft.world.gen.noise.NoiseRouter;
import net.minecraft.world.gen.random.ChunkRandom;

public class VanillaBiomeSource {
    private static final GenerationShapeConfig NORMAL_BIOME_SHAPE_CONFIG = OldGeneratorConfig.BETA_SHAPE_CONFIG;
    private static final GenerationShapeConfig LARGE_BIOME_SHAPE_CONFIG = OldGeneratorConfig.BETA_SHAPE_CONFIG_LARGE_BIOMES;
    
    // Currently, large biomes option only works with Xoroshiro random type.
    private static final ChunkRandom.RandomProvider RANDOM_TYPE = ChunkRandom.RandomProvider.XOROSHIRO;
    
    private final MultiNoiseUtil.Entries<RegistryEntry<Biome>> biomeEntries;
    private final MultiNoiseBiomeSource biomeSource;
    private final MultiNoiseUtil.MultiNoiseSampler noiseSampler;
    private final long seed;
    
    private static MultiNoiseUtil.MultiNoiseSampler createNoiseSampler(
        long seed,
        boolean largeBiomes
    ) {
        GenerationShapeConfig shapeConfig = largeBiomes ? LARGE_BIOME_SHAPE_CONFIG : NORMAL_BIOME_SHAPE_CONFIG;
        
        NoiseRouter noiseRouter = DensityFunctions.method_40544(
            shapeConfig,
            seed,
            BuiltinRegistries.NOISE_PARAMETERS,
            RANDOM_TYPE,
            MixinDensityFunctionsAccessor.invokeMethod_41103(shapeConfig, largeBiomes)
        );
        
        return new MultiNoiseUtil.MultiNoiseSampler(
            noiseRouter.temperature(),
            noiseRouter.humidity(),
            noiseRouter.continents(),
            noiseRouter.erosion(),
            noiseRouter.depth(),
            noiseRouter.ridges(),
            noiseRouter.spawnTarget()
        );
    }
    
    private VanillaBiomeSource(
        List<Pair<NoiseHypercube, RegistryEntry<Biome>>> biomeList,
        long seed,
        boolean largeBiomes
    ) {
        this.biomeEntries = new MultiNoiseUtil.Entries<RegistryEntry<Biome>>(biomeList);
        this.biomeSource = new MultiNoiseBiomeSource(this.biomeEntries, Optional.empty());
        this.noiseSampler = createNoiseSampler(seed, largeBiomes);

        this.seed = seed;
    }
    
    public RegistryEntry<Biome> getBiome(int biomeX, int biomeY, int biomeZ) {
        return this.biomeSource.getBiome(biomeX, biomeY, biomeZ, this.noiseSampler);
    }
    
    public MultiNoiseUtil.Entries<RegistryEntry<Biome>> getBiomeEntries() {
        return this.biomeEntries;
    }
    
    public List<RegistryEntry<Biome>> getBiomes() {
        return this.biomeEntries.getEntries().stream().map(p -> p.getSecond()).toList();
    }
    
    public long getSeed() {
        return this.seed;
    }
    
    public static class Builder {
        private final ImmutableList.Builder<Pair<MultiNoiseUtil.NoiseHypercube, RegistryEntry<Biome>>> builder;
        private final ExtendedVanillaBiomeParameters biomeParameters;
        private final Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters;
        private final long seed;
        private boolean largeBiomes;
        
        public Builder(Registry<Biome> biomeRegistry, long seed) {
            this.builder = ImmutableList.builder(); 
            this.biomeParameters = new ExtendedVanillaBiomeParameters();
            this.parameters = pair -> this.builder.add(pair.mapSecond(biomeRegistry::getOrCreateEntry));
            this.seed = seed;
            this.largeBiomes = false;
        }
        
        public Builder writeMixedBiomes(ParameterRange range) {
            MixinVanillaBiomeParametersAccessor invoker = this.getInvoker();
            invoker.invokeWriteMixedBiomes(this.parameters, range);
            
            return this;
        }
        
        public Builder writePlainsBiomes(ParameterRange range) {
            MixinVanillaBiomeParametersAccessor invoker = this.getInvoker();
            invoker.invokeWritePlainsBiomes(this.parameters, range);
            
            return this;
        }
        
        public Builder writeMountainousBiomes(ParameterRange range) {
            MixinVanillaBiomeParametersAccessor invoker = this.getInvoker();
            invoker.invokeWriteMountainousBiomes(this.parameters, range);
            
            return this;
        }
        
        public Builder writeOceanBiomes() {
            MixinVanillaBiomeParametersAccessor invoker = this.getInvoker();
            invoker.invokeWriteOceanBiomes(this.parameters);
            
            return this;
        }
        
        public Builder writeDeepOceanBiomes() {
            this.biomeParameters.writeDeepOceanBiomes(this.parameters);
            
            return this;
        }
        
        public Builder largeBiomes(boolean largeBiomes) {
            this.largeBiomes = largeBiomes;
            
            return this;
        }
        
        public VanillaBiomeSource build() {
            return new VanillaBiomeSource(this.builder.build(), this.seed, this.largeBiomes);
        }
        
        private MixinVanillaBiomeParametersAccessor getInvoker() {
            return ((MixinVanillaBiomeParametersAccessor)(Object)this.biomeParameters);
        }
    }
}
