package com.bespectacled.modernbeta.world.cavebiome.provider;

import java.util.List;

import com.bespectacled.modernbeta.api.world.cavebiome.CaveBiomeProvider;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.NbtUtil;
import com.bespectacled.modernbeta.util.settings.Settings;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

public class SingleCaveBiomeProvider extends CaveBiomeProvider {
    private static final Identifier DEFAULT_BIOME_ID = BiomeKeys.LUSH_CAVES.getValue();
    
    private final Identifier biomeId;
    
    public SingleCaveBiomeProvider(long seed, Settings settings, Registry<Biome> biomeRegistry) {
        super(seed, settings, biomeRegistry);

        this.biomeId = new Identifier(NbtUtil.toString(settings.get(NbtTags.SINGLE_BIOME), DEFAULT_BIOME_ID.toString()));
    }

    @Override
    public Biome getBiome(int biomeX, int biomeY, int biomeZ) {
        return this.biomeRegistry.get(this.biomeId);
    }
    
    @Override
    public List<Biome> getBiomesForRegistry() {
        return List.of(this.biomeRegistry.get(this.biomeId));
    }
}
