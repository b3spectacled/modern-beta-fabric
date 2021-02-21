package com.bespectacled.modernbeta.biome.classic;

import java.util.HashMap;
import java.util.Map;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.biome.BiomeType;
import com.bespectacled.modernbeta.gen.WorldType;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;

public class ClassicBiomes {
    public static final Identifier ALPHA_ID = ModernBeta.createId("alpha");
    public static final Identifier ALPHA_WINTER_ID = ModernBeta.createId("alpha_winter");
    
    public static final Identifier INFDEV_ID = ModernBeta.createId("infdev");
    public static final Identifier INFDEV_WINTER_ID = ModernBeta.createId("infdev_winter");
    
    public static final Identifier INFDEV_OLD_ID = ModernBeta.createId("infdev_old");
    public static final Identifier INFDEV_OLD_WINTER_ID = ModernBeta.createId("infdev_old_winter");
    
    public static final Map<BiomeType, Identifier> INFDEV_OLD_BIOMES = new HashMap<BiomeType, Identifier>();
    public static final Map<BiomeType, Identifier> INFDEV_BIOMES = new HashMap<BiomeType, Identifier>();
    public static final Map<BiomeType, Identifier> ALPHA_BIOMES = new HashMap<BiomeType, Identifier>();
    
    static {
        INFDEV_OLD_BIOMES.put(BiomeType.CLASSIC, INFDEV_OLD_ID);
        INFDEV_OLD_BIOMES.put(BiomeType.WINTER, INFDEV_OLD_WINTER_ID);
        
        INFDEV_BIOMES.put(BiomeType.CLASSIC, INFDEV_ID);
        INFDEV_BIOMES.put(BiomeType.WINTER, INFDEV_WINTER_ID);
        
        ALPHA_BIOMES.put(BiomeType.CLASSIC, ALPHA_ID);
        ALPHA_BIOMES.put(BiomeType.WINTER, ALPHA_WINTER_ID);
    }
    
    public static void registerAlphaBiomes() {
        Registry.register(BuiltinRegistries.BIOME, ALPHA_ID, Alpha.BIOME);
        Registry.register(BuiltinRegistries.BIOME, ALPHA_WINTER_ID, AlphaWinter.BIOME);
    }
    
    public static void registerInfdevBiomes() {
        Registry.register(BuiltinRegistries.BIOME, INFDEV_ID, Infdev.BIOME);
        Registry.register(BuiltinRegistries.BIOME, INFDEV_WINTER_ID, InfdevWinter.BIOME);
    }
    
    public static void registerInfdevOldBiomes() {
        Registry.register(BuiltinRegistries.BIOME, INFDEV_OLD_ID, InfdevOld.BIOME);
        Registry.register(BuiltinRegistries.BIOME, INFDEV_OLD_WINTER_ID, InfdevOldWinter.BIOME);
    }
    
    public static Map<BiomeType, Identifier> getBiomeMap(WorldType worldType) {
        switch(worldType) {
            case ALPHA:
                return ALPHA_BIOMES;
            case INFDEV:
                return INFDEV_BIOMES;
            case INFDEV_OLD:
                return INFDEV_OLD_BIOMES;
            default:
                return ALPHA_BIOMES;
        }
    }
}
