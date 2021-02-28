package com.bespectacled.modernbeta.biome.classic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.biome.BiomeType;
import com.bespectacled.modernbeta.gen.WorldType;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public class ClassicBiomes {
    public static final Identifier ALPHA_ID = ModernBeta.createId("alpha");
    public static final Identifier ALPHA_WINTER_ID = ModernBeta.createId("alpha_winter");
    
    public static final Identifier INFDEV_415_ID = ModernBeta.createId("infdev_415");
    public static final Identifier INFDEV_415_WINTER_ID = ModernBeta.createId("infdev_415_winter");
    
    public static final Identifier INFDEV_227_ID = ModernBeta.createId("infdev_227");
    public static final Identifier INFDEV_227_WINTER_ID = ModernBeta.createId("infdev_227_winter");
    
    public static final Map<BiomeType, Identifier> INFDEV_OLD_BIOMES = new HashMap<BiomeType, Identifier>();
    public static final Map<BiomeType, Identifier> INFDEV_BIOMES = new HashMap<BiomeType, Identifier>();
    public static final Map<BiomeType, Identifier> ALPHA_BIOMES = new HashMap<BiomeType, Identifier>();
    
    public static final List<RegistryKey<Biome>> ALPHA_BIOME_KEYS;
    public static final List<RegistryKey<Biome>> INFDEV_BIOME_KEYS;
    public static final List<RegistryKey<Biome>> INFDEV_OLD_BIOME_KEYS;
    
    static {
        INFDEV_OLD_BIOMES.put(BiomeType.CLASSIC, INFDEV_227_ID);
        INFDEV_OLD_BIOMES.put(BiomeType.WINTER, INFDEV_227_WINTER_ID);
        
        INFDEV_BIOMES.put(BiomeType.CLASSIC, INFDEV_415_ID);
        INFDEV_BIOMES.put(BiomeType.WINTER, INFDEV_415_WINTER_ID);
        
        ALPHA_BIOMES.put(BiomeType.CLASSIC, ALPHA_ID);
        ALPHA_BIOMES.put(BiomeType.WINTER, ALPHA_WINTER_ID);
        
        ALPHA_BIOME_KEYS = ALPHA_BIOMES.values().stream().map(i -> RegistryKey.of(Registry.BIOME_KEY, i)).collect(Collectors.toList());
        INFDEV_BIOME_KEYS = INFDEV_BIOMES.values().stream().map(i -> RegistryKey.of(Registry.BIOME_KEY, i)).collect(Collectors.toList());
        INFDEV_OLD_BIOME_KEYS = INFDEV_OLD_BIOMES.values().stream().map(i -> RegistryKey.of(Registry.BIOME_KEY, i)).collect(Collectors.toList());
    }
    
    public static void registerAlphaBiomes() {
        Registry.register(BuiltinRegistries.BIOME, ALPHA_ID, Alpha.BIOME);
        Registry.register(BuiltinRegistries.BIOME, ALPHA_WINTER_ID, AlphaWinter.BIOME);
    }
    
    public static void registerInfdevBiomes() {
        Registry.register(BuiltinRegistries.BIOME, INFDEV_415_ID, Infdev415.BIOME);
        Registry.register(BuiltinRegistries.BIOME, INFDEV_415_WINTER_ID, Infdev415Winter.BIOME);
    }
    
    public static void registerInfdevOldBiomes() {
        Registry.register(BuiltinRegistries.BIOME, INFDEV_227_ID, Infdev227.BIOME);
        Registry.register(BuiltinRegistries.BIOME, INFDEV_227_WINTER_ID, Infdev227Winter.BIOME);
    }
    
    public static Map<BiomeType, Identifier> getBiomeMap(WorldType worldType) {
        switch(worldType) {
            case ALPHA:
                return ALPHA_BIOMES;
            case INFDEV_415:
                return INFDEV_BIOMES;
            case INFDEV_227:
                return INFDEV_OLD_BIOMES;
            default:
                return ALPHA_BIOMES;
        }
    }
}
