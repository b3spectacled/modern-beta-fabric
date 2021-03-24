package com.bespectacled.modernbeta.biome.provider;

import java.util.Arrays;
import java.util.List;

import com.bespectacled.modernbeta.biome.BiomeType;
import com.bespectacled.modernbeta.biome.beta.BetaBiomes;
import com.bespectacled.modernbeta.biome.classic.ClassicBiomes;
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
        BiomeType biomeType = BiomeType.fromName(oldBiomeType);
        
        if (oldWorldType.equals("alpha")) {
            if (biomeType == BiomeType.CLASSIC || biomeType == BiomeType.PLUS) 
                biomeId = ClassicBiomes.ALPHA_ID;
            if (biomeType == BiomeType.WINTER) 
                biomeId = ClassicBiomes.ALPHA_WINTER_ID;
        } else if (oldWorldType.equals("infdev")) {
            if (biomeType == BiomeType.CLASSIC || biomeType == BiomeType.PLUS) 
                biomeId = ClassicBiomes.INFDEV_415_ID;
            if (biomeType == BiomeType.WINTER) 
                biomeId = ClassicBiomes.INFDEV_415_WINTER_ID;
        } else if (oldWorldType.equals("alpha")) {
            if (biomeType == BiomeType.CLASSIC || biomeType == BiomeType.PLUS) 
                biomeId = ClassicBiomes.INFDEV_227_ID;
            if (biomeType == BiomeType.WINTER) 
                biomeId = ClassicBiomes.INFDEV_227_WINTER_ID;
        }
        
        if (biomeType == BiomeType.SKY)
            biomeId = BetaBiomes.SKY_ID;
        
        return biomeId;
    }
}
