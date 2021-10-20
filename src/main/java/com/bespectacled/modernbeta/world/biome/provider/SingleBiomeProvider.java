package com.bespectacled.modernbeta.world.biome.provider;

import java.util.List;
import java.util.Optional;

import com.bespectacled.modernbeta.api.world.biome.ClimateBiomeProvider;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.NbtUtil;
import com.bespectacled.modernbeta.world.biome.provider.climate.SingleClimateSampler;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BuiltinBiomes;

public class SingleBiomeProvider extends ClimateBiomeProvider {
    private static final Identifier DEFAULT_BIOME_ID = new Identifier("plains");
    
    private final Identifier biomeId;
    
    public SingleBiomeProvider(long seed, NbtCompound settings, Registry<Biome> biomeRegistry) {
        super(seed, settings, biomeRegistry, new SingleClimateSampler(getBiome(settings, biomeRegistry)));
        
        this.biomeId = new Identifier(NbtUtil.readString(NbtTags.SINGLE_BIOME, settings, DEFAULT_BIOME_ID.toString()));
    }

    @Override
    public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        return this.biomeRegistry.get(this.biomeId);
    }
    
    @Override
    public List<Biome> getBiomesForRegistry() {
        return List.of(this.biomeRegistry.get(this.biomeId));
    }
    
    private static Biome getBiome(NbtCompound settings, Registry<Biome> biomeRegistry) {
        Identifier biomeId = new Identifier(NbtUtil.readString(NbtTags.SINGLE_BIOME, settings, DEFAULT_BIOME_ID.toString()));
        Optional<Biome> biome = biomeRegistry.getOrEmpty(biomeId);
        
        return biome.orElse(BuiltinBiomes.PLAINS);
    }
}
