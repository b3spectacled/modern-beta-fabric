package com.bespectacled.modernbeta.world.biome.provider;

import java.util.Arrays;
import java.util.List;

import com.bespectacled.modernbeta.api.AbstractBiomeProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public class SingleBiomeProvider extends AbstractBiomeProvider {
    private Identifier biomeId;
    
    public SingleBiomeProvider(long seed, CompoundTag settings) {
        super(seed, settings);
        
        this.biomeId = (settings.contains("singleBiome")) ?
            new Identifier(settings.getString("singleBiome")) :
            new Identifier("plains");
    }

    @Override
    public Biome getBiomeForNoiseGen(Registry<Biome> registry, int biomeX, int biomeY, int biomeZ) {
        return registry.get(biomeId); 
    }
    
    public Identifier getBiomeId() {
        return this.biomeId;
    }

    @Override
    public List<RegistryKey<Biome>> getBiomesForRegistry() {
        return Arrays.asList(RegistryKey.of(Registry.BIOME_KEY, this.biomeId));
    }
}
