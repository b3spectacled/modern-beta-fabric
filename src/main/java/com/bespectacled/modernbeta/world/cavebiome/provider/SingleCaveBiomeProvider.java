package com.bespectacled.modernbeta.world.cavebiome.provider;

import java.util.Arrays;
import java.util.List;

import com.bespectacled.modernbeta.api.world.cavebiome.CaveBiomeProvider;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.NbtUtil;
import com.bespectacled.modernbeta.world.biome.OldBiomeSource;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

public class SingleCaveBiomeProvider extends CaveBiomeProvider {
    private final Identifier biomeId;
    
    public SingleCaveBiomeProvider(long seed, NbtCompound settings) {
        super(seed, settings);
        
        this.biomeId = new Identifier(NbtUtil.readString(NbtTags.SINGLE_BIOME, settings, BiomeKeys.LUSH_CAVES.getValue().toString()));
    }

    @Override
    public Biome getBiome(Registry<Biome> biomeRegistry, int biomeX, int biomeY, int biomeZ) {
        return biomeRegistry.get(biomeId); 
    }
    
    @Override
    public List<RegistryKey<Biome>> getBiomesForRegistry() {
        return Arrays.asList(RegistryKey.of(Registry.BIOME_KEY, this.biomeId));
    }
}
