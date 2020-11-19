package com.bespectacled.modernbeta.biome;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.util.BiomeUtil;
import com.bespectacled.modernbeta.util.WorldEnum;
import com.bespectacled.modernbeta.util.WorldEnum.BiomeType;
import com.bespectacled.modernbeta.util.WorldEnum.PreBetaBiomeType;
import com.bespectacled.modernbeta.util.WorldEnum.WorldType;
import com.google.common.collect.ImmutableList;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeCreator;

public class PreBetaBiomes {
    public static final Identifier ALPHA_ID = new Identifier(ModernBeta.ID, "alpha");
    public static final Identifier ALPHA_WINTER_ID = new Identifier(ModernBeta.ID, "alpha_winter");
    
    public static final Identifier INFDEV_ID = new Identifier(ModernBeta.ID, "infdev");
    public static final Identifier INFDEV_WINTER_ID = new Identifier(ModernBeta.ID, "infdev_winter");
    
    public static final Identifier INFDEV_OLD_ID = new Identifier(ModernBeta.ID, "infdev_old");
    public static final Identifier INFDEV_OLD_WINTER_ID = new Identifier(ModernBeta.ID, "infdev_old_winter");
    
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
    
    public static void reserveAlphaBiomeIDs() {
        Registry.register(BuiltinRegistries.BIOME, ALPHA_ID, DefaultBiomeCreator.createNormalOcean(false));
        Registry.register(BuiltinRegistries.BIOME, ALPHA_WINTER_ID, DefaultBiomeCreator.createNormalOcean(false));
    }
    
    public static void reserveInfdevBiomeIds() {
        Registry.register(BuiltinRegistries.BIOME, INFDEV_ID, DefaultBiomeCreator.createNormalOcean(false));
        Registry.register(BuiltinRegistries.BIOME, INFDEV_WINTER_ID, DefaultBiomeCreator.createNormalOcean(false));
    }
    
    public static void reserveInfdevOldBiomeIds() {
        Registry.register(BuiltinRegistries.BIOME, INFDEV_OLD_ID, DefaultBiomeCreator.createNormalOcean(false));
        Registry.register(BuiltinRegistries.BIOME, INFDEV_OLD_WINTER_ID, DefaultBiomeCreator.createNormalOcean(false));
    }
    
    public static WorldType getWorldType(CompoundTag settings) {
        WorldType type = WorldType.ALPHA;
        
        if (settings.contains("worldType"))
            type = WorldType.fromName(settings.getString("worldType"));
        
        return type;
    }
    
    public static PreBetaBiomeType getBiomeType(CompoundTag settings) {
        PreBetaBiomeType type = PreBetaBiomeType.CLASSIC;
        
        if (settings.contains("preBetaBiomeType")) 
            type = PreBetaBiomeType.fromName(settings.getString("preBetaBiomeType"));
        
        return type;
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
    
    public static List<RegistryKey<Biome>> getBiomeRegistryList(CompoundTag settings) {
        WorldType type = WorldType.ALPHA;
        boolean useVanillaBiomes = false;
        
        if (settings.contains("worldType"))
            type = WorldType.fromName(settings.getString("worldType"));
        
        if (settings.contains("preBetaBiomeType")) 
            useVanillaBiomes = PreBetaBiomeType.fromName(settings.getString("preBetaBiomeType")) == PreBetaBiomeType.VANILLA;
        
        return getBiomeRegistryList(type, useVanillaBiomes);
    }
    
    private static List<RegistryKey<Biome>> getBiomeRegistryList(WorldType worldType, boolean useVanillaBiomes) {
        ArrayList<RegistryKey<Biome>> biomeList = new ArrayList<RegistryKey<Biome>>();
        List<Identifier> biomeIds;
        
        if (useVanillaBiomes) {
            return BiomeUtil.VANILLA_BIOMES;
        }
        
        switch(worldType) {
            case ALPHA:
                biomeIds = new ArrayList<Identifier>(ALPHA_BIOMES.values());
                break;
            case INFDEV:
                biomeIds = new ArrayList<Identifier>(INFDEV_BIOMES.values());
                break;
            case INFDEV_OLD:
                biomeIds = new ArrayList<Identifier>(INFDEV_OLD_BIOMES.values());
                break;
            default:
                biomeIds = new ArrayList<Identifier>(ALPHA_BIOMES.values());
                break;
        }
        
        for (Identifier i : biomeIds) {
            biomeList.add(RegistryKey.of(Registry.BIOME_KEY, i));
        }
        
        return Collections.unmodifiableList(biomeList);
    }
}
