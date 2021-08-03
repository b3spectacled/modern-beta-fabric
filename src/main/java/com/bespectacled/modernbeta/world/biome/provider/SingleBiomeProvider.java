package com.bespectacled.modernbeta.world.biome.provider;

import java.util.Arrays;
import java.util.List;

import com.bespectacled.modernbeta.api.world.biome.BiomeProvider;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.NbtUtil;
import com.bespectacled.modernbeta.world.biome.OldBiomeSource;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public class SingleBiomeProvider extends BiomeProvider {
    private static final Identifier DEFAULT_BIOME_ID = new Identifier("plains");
    
    private final Identifier biomeId;
    
    public SingleBiomeProvider(OldBiomeSource biomeSource) {
        super(biomeSource);
        
        this.biomeId = new Identifier(NbtUtil.readString(NbtTags.SINGLE_BIOME, settings, DEFAULT_BIOME_ID.toString()));
    }

    @Override
    public Biome getBiomeForNoiseGen(Registry<Biome> biomeRegistry, int biomeX, int biomeY, int biomeZ) {
        return biomeRegistry.get(this.biomeId);
    }
    
    @Override
    public List<RegistryKey<Biome>> getBiomesForRegistry() {
        return Arrays.asList(RegistryKey.of(Registry.BIOME_KEY, this.biomeId));
    }
}
