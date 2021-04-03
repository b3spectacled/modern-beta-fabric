package com.bespectacled.modernbeta.world.biome.provider;

import java.util.Arrays;
import java.util.List;

import com.bespectacled.modernbeta.api.AbstractBiomeProvider;
import com.bespectacled.modernbeta.api.registry.BiomeProviderRegistry.BuiltInBiomeType;
import com.bespectacled.modernbeta.world.biome.beta.BetaBiomes;
import com.bespectacled.modernbeta.world.biome.classic.ClassicBiomes;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public class SingleBiomeProvider extends AbstractBiomeProvider {
    private Identifier biomeId;
    
    public SingleBiomeProvider(long seed, NbtCompound settings) {
        super(seed, settings);
        
        this.biomeId = (settings.contains("singleBiome")) ?
            new Identifier(settings.getString("singleBiome")) :
            this.loadLegacySettings(settings);
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
    
    private Identifier loadLegacySettings(NbtCompound settings) {
        Identifier biomeId = ClassicBiomes.ALPHA_ID;
        
        String oldWorldType = settings.getString("worldType");
        String oldBiomeType = settings.getString("biomeType");
        
        if (oldWorldType.equals("alpha")) {
            if (oldBiomeType.equals(BuiltInBiomeType.CLASSIC.id) || oldBiomeType.equals(BuiltInBiomeType.PLUS.id)) 
                biomeId = ClassicBiomes.ALPHA_ID;
            if (oldBiomeType.equals(BuiltInBiomeType.WINTER.id)) 
                biomeId = ClassicBiomes.ALPHA_WINTER_ID;
        } else if (oldWorldType.equals("infdev")) {
            if (oldBiomeType.equals(BuiltInBiomeType.CLASSIC.id) || oldBiomeType.equals(BuiltInBiomeType.PLUS.id)) 
                biomeId = ClassicBiomes.INFDEV_415_ID;
            if (oldBiomeType.equals(BuiltInBiomeType.WINTER.id)) 
                biomeId = ClassicBiomes.INFDEV_415_WINTER_ID;
        } else if (oldWorldType.equals("alpha")) {
            if (oldBiomeType.equals(BuiltInBiomeType.CLASSIC.id) || oldBiomeType.equals(BuiltInBiomeType.PLUS.id)) 
                biomeId = ClassicBiomes.INFDEV_227_ID;
            if (oldBiomeType.equals(BuiltInBiomeType.WINTER.id)) 
                biomeId = ClassicBiomes.INFDEV_227_WINTER_ID;
        }
        
        if (oldBiomeType.equals(BuiltInBiomeType.SKY.id)) 
            biomeId = BetaBiomes.SKY_ID;
        
        return biomeId;
    }
}
