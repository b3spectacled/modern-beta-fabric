package com.bespectacled.modernbeta.biome.beta;

import java.util.List;
import java.util.stream.Collectors;

import com.bespectacled.modernbeta.ModernBeta;
import com.google.common.collect.ImmutableList;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public class BetaBiomes {
    public static final Identifier FOREST_ID = ModernBeta.createId("forest");
    public static final Identifier SHRUBLAND_ID = ModernBeta.createId("shrubland");
    public static final Identifier DESERT_ID = ModernBeta.createId("desert");
    public static final Identifier SAVANNA_ID = ModernBeta.createId("savanna");
    public static final Identifier PLAINS_ID = ModernBeta.createId("plains");
    public static final Identifier SEASONAL_FOREST_ID = ModernBeta.createId("seasonal_forest");
    public static final Identifier RAINFOREST_ID = ModernBeta.createId("rainforest");
    public static final Identifier SWAMPLAND_ID = ModernBeta.createId("swampland");
    public static final Identifier TAIGA_ID = ModernBeta.createId("taiga");
    public static final Identifier TUNDRA_ID = ModernBeta.createId("tundra");
    public static final Identifier ICE_DESERT_ID = ModernBeta.createId("ice_desert");

    public static final Identifier OCEAN_ID = ModernBeta.createId("ocean");
    public static final Identifier LUKEWARM_OCEAN_ID = ModernBeta.createId("lukewarm_ocean");
    public static final Identifier WARM_OCEAN_ID = ModernBeta.createId("warm_ocean");
    public static final Identifier COLD_OCEAN_ID = ModernBeta.createId("cold_ocean");
    public static final Identifier FROZEN_OCEAN_ID = ModernBeta.createId("frozen_ocean");
    
    public static final Identifier SKY_ID = ModernBeta.createId("sky");
    
    public static final ImmutableList<Identifier> BIOMES = ImmutableList.of(
        FOREST_ID,
        SHRUBLAND_ID, 
        DESERT_ID,
        SAVANNA_ID, 
        PLAINS_ID,
        SEASONAL_FOREST_ID, 
        RAINFOREST_ID,
        SWAMPLAND_ID, 
        TAIGA_ID,
        TUNDRA_ID, 
        ICE_DESERT_ID,

        OCEAN_ID, 
        LUKEWARM_OCEAN_ID,
        WARM_OCEAN_ID, 
        COLD_OCEAN_ID,
        FROZEN_OCEAN_ID,

        SKY_ID
    );
    
    public static final List<RegistryKey<Biome>> BIOME_KEYS = BIOMES.stream().map(i -> RegistryKey.of(Registry.BIOME_KEY, i)).collect(Collectors.toList());
    
    public static void registerBiomes() {
        Registry.register(BuiltinRegistries.BIOME, FOREST_ID, Forest.BIOME);
        Registry.register(BuiltinRegistries.BIOME, SHRUBLAND_ID, Shrubland.BIOME);
        Registry.register(BuiltinRegistries.BIOME, DESERT_ID, Desert.BIOME);
        Registry.register(BuiltinRegistries.BIOME, SAVANNA_ID, Savanna.BIOME);
        Registry.register(BuiltinRegistries.BIOME, PLAINS_ID, Plains.BIOME);
        Registry.register(BuiltinRegistries.BIOME, SEASONAL_FOREST_ID, SeasonalForest.BIOME);
        Registry.register(BuiltinRegistries.BIOME, RAINFOREST_ID, Rainforest.BIOME);
        Registry.register(BuiltinRegistries.BIOME, SWAMPLAND_ID, Swampland.BIOME);
        Registry.register(BuiltinRegistries.BIOME, TAIGA_ID, Taiga.BIOME);
        Registry.register(BuiltinRegistries.BIOME, TUNDRA_ID, Tundra.BIOME);
        Registry.register(BuiltinRegistries.BIOME, ICE_DESERT_ID, IceDesert.BIOME);
        
        Registry.register(BuiltinRegistries.BIOME, OCEAN_ID, Ocean.BIOME);
        Registry.register(BuiltinRegistries.BIOME, LUKEWARM_OCEAN_ID, LukewarmOcean.BIOME);
        Registry.register(BuiltinRegistries.BIOME, WARM_OCEAN_ID, WarmOcean.BIOME);
        Registry.register(BuiltinRegistries.BIOME, COLD_OCEAN_ID, ColdOcean.BIOME);
        Registry.register(BuiltinRegistries.BIOME, FROZEN_OCEAN_ID, FrozenOcean.BIOME);
        
        Registry.register(BuiltinRegistries.BIOME, SKY_ID, Sky.BIOME);
    }
}
