package com.bespectacled.modernbeta.world.biome.classic;

import java.util.HashMap;
import java.util.Map;
import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.world.biome.OldBiomes;

import net.minecraft.util.Identifier;

public class ClassicBiomes {
    public static final Identifier ALPHA_ID = ModernBeta.createId("alpha");
    public static final Identifier ALPHA_WINTER_ID = ModernBeta.createId("alpha_winter");
    
    // TODO: Consider renaming to infdev_415, infdev_227, etc.
    public static final Identifier INFDEV_415_ID = ModernBeta.createId("infdev");
    public static final Identifier INFDEV_415_WINTER_ID = ModernBeta.createId("infdev_winter");
    
    public static final Identifier INFDEV_227_ID = ModernBeta.createId("infdev_old");
    public static final Identifier INFDEV_227_WINTER_ID = ModernBeta.createId("infdev_old_winter");
    
    public static final Map<String, Identifier> INFDEV_227_BIOMES = new HashMap<String, Identifier>();
    public static final Map<String, Identifier> INFDEV_415_BIOMES = new HashMap<String, Identifier>();
    public static final Map<String, Identifier> ALPHA_BIOMES = new HashMap<String, Identifier>();
    
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
}
