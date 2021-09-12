package com.bespectacled.modernbeta.world.biome.pe;

import java.util.List;
import java.util.stream.Collectors;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.world.biome.OldBiomes;
import com.google.common.collect.ImmutableList;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public class PEBiomes {
    protected static final boolean ADD_LAKES = false;
    protected static final boolean ADD_SPRINGS = true;
    
    protected static final boolean ADD_ALTERNATE_STONES = false;
    protected static final boolean ADD_NEW_MINEABLES = true;
    
    public static final Identifier PE_FOREST_ID = ModernBeta.createId("pe_forest");
    public static final Identifier PE_SHRUBLAND_ID = ModernBeta.createId("pe_shrubland");
    public static final Identifier PE_DESERT_ID = ModernBeta.createId("pe_desert");
    public static final Identifier PE_SAVANNA_ID = ModernBeta.createId("pe_savanna");
    public static final Identifier PE_PLAINS_ID = ModernBeta.createId("pe_plains");
    public static final Identifier PE_SEASONAL_FOREST_ID = ModernBeta.createId("pe_seasonal_forest");
    public static final Identifier PE_RAINFOREST_ID = ModernBeta.createId("pe_rainforest");
    public static final Identifier PE_SWAMPLAND_ID = ModernBeta.createId("pe_swampland");
    public static final Identifier PE_TAIGA_ID = ModernBeta.createId("pe_taiga");
    public static final Identifier PE_TUNDRA_ID = ModernBeta.createId("pe_tundra");
    public static final Identifier PE_ICE_DESERT_ID = ModernBeta.createId("pe_ice_desert");
    
    public static final Identifier PE_OCEAN_ID = ModernBeta.createId("pe_ocean");
    public static final Identifier PE_LUKEWARM_OCEAN_ID = ModernBeta.createId("pe_lukewarm_ocean");
    public static final Identifier PE_WARM_OCEAN_ID = ModernBeta.createId("pe_warm_ocean");
    public static final Identifier PE_COLD_OCEAN_ID = ModernBeta.createId("pe_cold_ocean");
    public static final Identifier PE_FROZEN_OCEAN_ID = ModernBeta.createId("pe_frozen_ocean");
    
    public static final ImmutableList<Identifier> BIOMES = ImmutableList.of(
        PE_FOREST_ID,
        PE_SHRUBLAND_ID,
        PE_DESERT_ID,
        PE_SAVANNA_ID, 
        PE_PLAINS_ID,
        PE_SEASONAL_FOREST_ID, 
        PE_RAINFOREST_ID,
        PE_SWAMPLAND_ID, 
        PE_TAIGA_ID,
        PE_TUNDRA_ID, 
        PE_ICE_DESERT_ID,
        
        PE_OCEAN_ID, 
        PE_LUKEWARM_OCEAN_ID,
        PE_WARM_OCEAN_ID, 
        PE_COLD_OCEAN_ID,
        PE_FROZEN_OCEAN_ID
    );
    
    public static final List<RegistryKey<Biome>> BIOME_KEYS = BIOMES
        .stream()
        .map(i -> RegistryKey.of(Registry.BIOME_KEY, i))
        .collect(Collectors.toList());
    
    public static void registerBiomes() {
        OldBiomes.register(PE_FOREST_ID, PEForest.BIOME);
        OldBiomes.register(PE_SHRUBLAND_ID, PEShrubland.BIOME);
        OldBiomes.register(PE_DESERT_ID, PEDesert.BIOME);
        OldBiomes.register(PE_SAVANNA_ID, PESavanna.BIOME);
        OldBiomes.register(PE_PLAINS_ID, PEPlains.BIOME);
        OldBiomes.register(PE_SEASONAL_FOREST_ID, PESeasonalForest.BIOME);
        OldBiomes.register(PE_RAINFOREST_ID, PERainforest.BIOME);
        OldBiomes.register(PE_SWAMPLAND_ID, PESwampland.BIOME);
        OldBiomes.register(PE_TAIGA_ID, PETaiga.BIOME);
        OldBiomes.register(PE_TUNDRA_ID, PETundra.BIOME);
        OldBiomes.register(PE_ICE_DESERT_ID, PEIceDesert.BIOME);
        
        OldBiomes.register(PE_OCEAN_ID, PEOcean.BIOME);
        OldBiomes.register(PE_LUKEWARM_OCEAN_ID, PELukewarmOcean.BIOME);
        OldBiomes.register(PE_WARM_OCEAN_ID, PEWarmOcean.BIOME);
        OldBiomes.register(PE_COLD_OCEAN_ID, PEColdOcean.BIOME);
        OldBiomes.register(PE_FROZEN_OCEAN_ID, PEFrozenOcean.BIOME);
    }
}   
