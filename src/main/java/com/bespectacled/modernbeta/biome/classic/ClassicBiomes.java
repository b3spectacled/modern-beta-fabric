package com.bespectacled.modernbeta.biome.classic;

import java.util.HashMap;
import java.util.Map;
import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.biome.BiomeType;
import com.bespectacled.modernbeta.biome.OldBiomes;
import com.bespectacled.modernbeta.gen.WorldType;

import net.minecraft.util.Identifier;

public class ClassicBiomes {
    public static final Identifier ALPHA_ID = ModernBeta.createId("alpha");
    public static final Identifier ALPHA_WINTER_ID = ModernBeta.createId("alpha_winter");
    
    // TODO: Consider renaming to infdev_415, infdev_227, etc.
    public static final Identifier INFDEV_415_ID = ModernBeta.createId("infdev");
    public static final Identifier INFDEV_415_WINTER_ID = ModernBeta.createId("infdev_winter");
    
    public static final Identifier INFDEV_227_ID = ModernBeta.createId("infdev_old");
    public static final Identifier INFDEV_227_WINTER_ID = ModernBeta.createId("infdev_old_winter");
    
    public static final Map<BiomeType, Identifier> INFDEV_227_BIOMES = new HashMap<BiomeType, Identifier>();
    public static final Map<BiomeType, Identifier> INFDEV_415_BIOMES = new HashMap<BiomeType, Identifier>();
    public static final Map<BiomeType, Identifier> ALPHA_BIOMES = new HashMap<BiomeType, Identifier>();
    
    static {
        //INFDEV_227_BIOMES.put(BiomeType.CLASSIC, INFDEV_227_ID);
        //INFDEV_227_BIOMES.put(BiomeType.WINTER, INFDEV_227_WINTER_ID);
        
        //INFDEV_415_BIOMES.put(BiomeType.CLASSIC, INFDEV_415_ID);
        //INFDEV_415_BIOMES.put(BiomeType.WINTER, INFDEV_415_WINTER_ID);
        
        //ALPHA_BIOMES.put(BiomeType.CLASSIC, ALPHA_ID);
        //ALPHA_BIOMES.put(BiomeType.WINTER, ALPHA_WINTER_ID);
    }
    
    public static void registerAlphaBiomes() {
        OldBiomes.register(ALPHA_ID, Alpha.BIOME);
        OldBiomes.register(ALPHA_WINTER_ID, AlphaWinter.BIOME);
    }
    
    public static void registerInfdev415Biomes() {
        OldBiomes.register(INFDEV_415_ID, Infdev415.BIOME);
        OldBiomes.register(INFDEV_415_WINTER_ID, Infdev415Winter.BIOME);
    }
    
    public static void registerInfdev227Biomes() {
        OldBiomes.register(INFDEV_227_ID, Infdev227.BIOME);
        OldBiomes.register(INFDEV_227_WINTER_ID, Infdev227Winter.BIOME);
    }
    
    public static Map<BiomeType, Identifier> getBiomeMap(WorldType worldType) {
        switch(worldType) {
            case ALPHA:
                return ALPHA_BIOMES;
            case INFDEV_415:
                return INFDEV_415_BIOMES;
            case INFDEV_227:
                return INFDEV_227_BIOMES;
            default:
                return ALPHA_BIOMES;
        }
    }
}
