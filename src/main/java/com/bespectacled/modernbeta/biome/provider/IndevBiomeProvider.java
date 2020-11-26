package com.bespectacled.modernbeta.biome.provider;

import com.bespectacled.modernbeta.biome.IndevBiomes;
import com.bespectacled.modernbeta.util.IndevUtil;
import com.bespectacled.modernbeta.util.IndevUtil.IndevTheme;
import com.bespectacled.modernbeta.util.IndevUtil.IndevType;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public class IndevBiomeProvider extends AbstractBiomeProvider {
    
    //private final IndevType type;
    private final IndevTheme theme;
   
    private final int width;
    private final int length;
    //private final int height;
    
    public IndevBiomeProvider(long seed, CompoundTag settings) {
        this.theme = settings.contains("levelTheme") ? IndevTheme.fromName(settings.getString("levelTheme")) : IndevTheme.NORMAL;
        //this.type = settings.contains("levelType") ? IndevType.fromName(settings.getString("levelType")) : IndevType.ISLAND;

        this.width = settings.contains("levelWidth") ? settings.getInt("levelWidth") : 256;
        this.length = settings.contains("levelLength") ? settings.getInt("levelLength") : 256;
        //this.height = settings.contains("levelHeight") ? settings.getInt("levelHeight") : 128;
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

}
