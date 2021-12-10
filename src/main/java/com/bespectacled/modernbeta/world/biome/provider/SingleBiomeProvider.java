package com.bespectacled.modernbeta.world.biome.provider;

import java.util.List;

import com.bespectacled.modernbeta.api.world.biome.BiomeProvider;
import com.bespectacled.modernbeta.api.world.biome.climate.ClimateSampler;
import com.bespectacled.modernbeta.api.world.biome.climate.Clime;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.NbtUtil;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public class SingleBiomeProvider extends BiomeProvider implements ClimateSampler {
    private static final Identifier DEFAULT_BIOME_ID = new Identifier("plains");
    
    private final Identifier biomeId;
    private final Clime biomeClime;
    
    public SingleBiomeProvider(long seed, NbtCompound settings, Registry<Biome> biomeRegistry) {
        super(seed, settings, biomeRegistry);
        
        this.biomeId = new Identifier(NbtUtil.readString(NbtTags.SINGLE_BIOME, settings, DEFAULT_BIOME_ID.toString()));
        this.biomeClime = new Clime(
            MathHelper.clamp(biomeRegistry.get(this.biomeId).getTemperature(), 0.0, 1.0),
            MathHelper.clamp(biomeRegistry.get(this.biomeId).getDownfall(), 0.0, 1.0)
        );
    }

    @Override
    public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        return this.biomeRegistry.get(this.biomeId);
    }
    
    @Override
    public List<Biome> getBiomesForRegistry() {
        return List.of(this.biomeRegistry.get(this.biomeId));
    }

    @Override
    public Clime sample(int x, int z) {
        return this.biomeClime;
    }
}
