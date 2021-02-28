package com.bespectacled.modernbeta.biome.provider;

import java.util.List;

import com.bespectacled.modernbeta.biome.indev.IndevBiomes;
import com.bespectacled.modernbeta.biome.indev.IndevUtil.IndevTheme;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public class IndevBiomeProvider extends AbstractBiomeProvider {
    private final IndevTheme theme;
    
    public IndevBiomeProvider(long seed, CompoundTag settings) {
        this.theme = settings.contains("levelTheme") ? IndevTheme.fromName(settings.getString("levelTheme")) : IndevTheme.NORMAL;
    }

    @Override
    public Biome getBiomeForNoiseGen(Registry<Biome> registry, int biomeX, int biomeY, int biomeZ) {
        Biome biome;
        
        switch(theme) {
            case NORMAL:
                biome = registry.get(IndevBiomes.INDEV_NORMAL_ID);
                break;
            case HELL:
                biome = registry.get(IndevBiomes.INDEV_HELL_ID);
                break;
            case PARADISE:
                biome = registry.get(IndevBiomes.INDEV_PARADISE_ID);
                break;
            case WOODS:
                biome = registry.get(IndevBiomes.INDEV_WOODS_ID);
                break;  
            case SNOWY:
                biome = registry.get(IndevBiomes.INDEV_SNOWY_ID);
                break;
            default:
                biome = registry.get(IndevBiomes.INDEV_NORMAL_ID);
        }
        return biome;
    }

    @Override
    public List<RegistryKey<Biome>> getBiomesForRegistry() {
        return IndevBiomes.BIOME_KEYS;
    }

}
