package com.bespectacled.modernbeta.biome.beta;

import java.util.List;
import java.util.stream.Collectors;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.biome.OldBiomes;
import com.google.common.collect.ImmutableList;

import net.minecraft.util.Identifier;
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
        OldBiomes.register(FOREST_ID, Forest.BIOME);
        OldBiomes.register(SHRUBLAND_ID, Shrubland.BIOME);
        OldBiomes.register(DESERT_ID, Desert.BIOME);
        OldBiomes.register(SAVANNA_ID, Savanna.BIOME);
        OldBiomes.register(PLAINS_ID, Plains.BIOME);
        OldBiomes.register(SEASONAL_FOREST_ID, SeasonalForest.BIOME);
        OldBiomes.register(RAINFOREST_ID, Rainforest.BIOME);
        OldBiomes.register(SWAMPLAND_ID, Swampland.BIOME);
        OldBiomes.register(TAIGA_ID, Taiga.BIOME);
        OldBiomes.register(TUNDRA_ID, Tundra.BIOME);
        OldBiomes.register(ICE_DESERT_ID, IceDesert.BIOME);
        
        OldBiomes.register(OCEAN_ID, Ocean.BIOME);
        OldBiomes.register(LUKEWARM_OCEAN_ID, LukewarmOcean.BIOME);
        OldBiomes.register(WARM_OCEAN_ID, WarmOcean.BIOME);
        OldBiomes.register(COLD_OCEAN_ID, ColdOcean.BIOME);
        OldBiomes.register(FROZEN_OCEAN_ID, FrozenOcean.BIOME);
        
        OldBiomes.register(SKY_ID, Sky.BIOME);
    }
}
