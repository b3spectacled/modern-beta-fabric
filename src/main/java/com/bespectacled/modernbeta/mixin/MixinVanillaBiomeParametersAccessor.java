package com.bespectacled.modernbeta.mixin;

import java.util.function.Consumer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import com.mojang.datafixers.util.Pair;

import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.biome.source.util.VanillaBiomeParameters;

@Mixin(VanillaBiomeParameters.class)
public interface MixinVanillaBiomeParametersAccessor {
    @Invoker("writeCaveBiomes")
    public void invokeWriteCaveBiomes(Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters);
    
    @Invoker("writeMixedBiomes")
    public void invokeWriteMixedBiomes(Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters, MultiNoiseUtil.ParameterRange weirdness);
    
    @Invoker("writePlainBiomes")
    public void invokeWritePlainsBiomes(Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters, MultiNoiseUtil.ParameterRange weirdness);
    
    @Invoker("writeMountainousBiomes")
    public void invokeWriteMountainousBiomes(Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters, MultiNoiseUtil.ParameterRange weirdness);
    
    @Invoker("writeBiomesNearRivers")
    public void invokeWriteBiomesNearRivers(Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters, MultiNoiseUtil.ParameterRange weirdness);
    
    @Invoker("writeOceanBiomes")
    public void invokeWriteOceanBiomes(Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters);
    
    @Invoker("writeBiomeParameters")
    public void invokeWriteBiomeParameters(
        Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters,
        MultiNoiseUtil.ParameterRange temperature,
        MultiNoiseUtil.ParameterRange humidity,
        MultiNoiseUtil.ParameterRange continentalness,
        MultiNoiseUtil.ParameterRange erosion,
        MultiNoiseUtil.ParameterRange weirdness,
        float offset,
        RegistryKey<Biome> biome
    );
    
    @Accessor
    public MultiNoiseUtil.ParameterRange[] getTEMPERATURE_PARAMETERS();
    
    @Accessor
    public MultiNoiseUtil.ParameterRange getDEFAULT_PARAMETER();
    
    @Accessor
    public RegistryKey<Biome>[][] getOCEAN_BIOMES();
}
