package com.bespectacled.modernbeta.biome.provider;

import java.util.ArrayList;
import java.util.List;

import com.bespectacled.modernbeta.biome.indev.IndevBiomes;
import com.bespectacled.modernbeta.biome.indev.IndevUtil;
import com.bespectacled.modernbeta.biome.indev.IndevUtil.IndevTheme;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public class IndevBiomeProvider extends AbstractBiomeProvider {
    private final IndevTheme theme;
   
    private final int width;
    private final int length;
    
    public IndevBiomeProvider(long seed, CompoundTag settings) {
        this.theme = settings.contains("levelTheme") ? IndevTheme.fromName(settings.getString("levelTheme")) : IndevTheme.NORMAL;

        this.width = settings.contains("levelWidth") ? settings.getInt("levelWidth") : 256;
        this.length = settings.contains("levelLength") ? settings.getInt("levelLength") : 256;
    }

    @Override
    public Biome getBiomeForNoiseGen(Registry<Biome> registry, int biomeX, int biomeY, int biomeZ) {
        int absX = biomeX << 2;
        int absZ = biomeZ << 2;
        
        Biome biome;
        
        if (IndevUtil.inIndevRegion(absX, absZ, this.width, this.length)) {
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
        } else {
            switch(theme) {
                case NORMAL:
                    biome = registry.get(IndevBiomes.INDEV_EDGE_ID);
                    break;
                case HELL:
                    biome = registry.get(IndevBiomes.INDEV_HELL_EDGE_ID);
                    break;
                case PARADISE:
                    biome = registry.get(IndevBiomes.INDEV_PARADISE_EDGE_ID);
                    break; 
                case WOODS:
                    biome = registry.get(IndevBiomes.INDEV_WOODS_EDGE_ID);
                    break;  
                case SNOWY:
                    biome = registry.get(IndevBiomes.INDEV_SNOWY_EDGE_ID);
                    break;
                default:
                    biome = registry.get(IndevBiomes.INDEV_EDGE_ID);
            }
        }
        
        return biome;
    }

    @Override
    public List<RegistryKey<Biome>> getBiomesForRegistry() {
        List<RegistryKey<Biome>> biomeList = new ArrayList<RegistryKey<Biome>>();

        for (Identifier i : IndevBiomes.BIOMES) {
            biomeList.add(RegistryKey.of(Registry.BIOME_KEY, i));
        }
        
        return biomeList;
    }

}
