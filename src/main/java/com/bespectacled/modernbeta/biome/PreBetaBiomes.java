package com.bespectacled.modernbeta.biome;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.biome.BetaBiomes.BiomeType;
import com.bespectacled.modernbeta.util.BiomeUtil;
import com.bespectacled.modernbeta.util.WorldEnum;
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
    
    public static final Map<PreBetaBiomeType, Identifier> INFDEV_OLD_BIOMES = new HashMap<PreBetaBiomeType, Identifier>();
    public static final Map<PreBetaBiomeType, Identifier> INFDEV_BIOMES = new HashMap<PreBetaBiomeType, Identifier>();
    public static final Map<PreBetaBiomeType, Identifier> ALPHA_BIOMES = new HashMap<PreBetaBiomeType, Identifier>();
    
    static {
        INFDEV_OLD_BIOMES.put(PreBetaBiomeType.CLASSIC, INFDEV_OLD_ID);
        INFDEV_OLD_BIOMES.put(PreBetaBiomeType.WINTER, INFDEV_OLD_WINTER_ID);
        
        INFDEV_BIOMES.put(PreBetaBiomeType.CLASSIC, INFDEV_ID);
        INFDEV_BIOMES.put(PreBetaBiomeType.WINTER, INFDEV_WINTER_ID);
        
        ALPHA_BIOMES.put(PreBetaBiomeType.CLASSIC, ALPHA_ID);
        ALPHA_BIOMES.put(PreBetaBiomeType.WINTER, ALPHA_WINTER_ID);
    }
    
    public static void reserveBiomeIDs() {
        for (Identifier i : ALPHA_BIOMES.values()) {
            Registry.register(BuiltinRegistries.BIOME, i, DefaultBiomeCreator.createNormalOcean(false));
        }
        
        for (Identifier i : INFDEV_BIOMES.values()) {
            Registry.register(BuiltinRegistries.BIOME, i, DefaultBiomeCreator.createNormalOcean(false));
        }
        
        for (Identifier i : INFDEV_OLD_BIOMES.values()) {
            Registry.register(BuiltinRegistries.BIOME, i, DefaultBiomeCreator.createNormalOcean(false));
        }

        //ModernBeta.LOGGER.log(Level.INFO, "Reserved Alpha biome IDs.");
    }
    
    public static WorldType getWorldType(CompoundTag settings) {
        WorldType type = WorldType.BETA;
        
        if (settings.contains("worldType"))
            type = WorldType.values()[settings.getInt("worldType")];
        
        return type;
    }
    
    public static PreBetaBiomeType getBiomeType(CompoundTag settings, WorldType worldType) {
        int biomeTypeNdx = PreBetaBiomeType.CLASSIC.ordinal();
        
        if (settings.contains("preBetaBiomeType")) 
            biomeTypeNdx = settings.getInt("preBetaBiomeType");
        
        return PreBetaBiomeType.values()[biomeTypeNdx];
    }
    
    public static Map<PreBetaBiomeType, Identifier> getBiomeMap(WorldType worldType) {
        
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
            type = WorldType.values()[settings.getInt("worldType")];
        
        if (settings.contains("preBetaBiomeType")) 
            useVanillaBiomes = PreBetaBiomeType.values()[settings.getInt("preBetaBiomeType")] == PreBetaBiomeType.VANILLA;
        
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
