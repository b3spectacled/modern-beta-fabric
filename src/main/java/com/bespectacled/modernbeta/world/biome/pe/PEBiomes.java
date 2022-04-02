package com.bespectacled.modernbeta.world.biome.pe;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.world.biome.OldBiomes;
import com.google.common.collect.ImmutableList;

import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public class PEBiomes {
    protected static final boolean ADD_LAKES = false;
    protected static final boolean ADD_SPRINGS = true;
    
    protected static final boolean ADD_ALTERNATE_STONES = false;
    protected static final boolean ADD_NEW_MINEABLES = true;
    
    public static final RegistryKey<Biome> PE_FOREST_KEY = OldBiomes.register(ModernBeta.createId("pe_forest"));
    public static final RegistryKey<Biome> PE_SHRUBLAND_KEY = OldBiomes.register(ModernBeta.createId("pe_shrubland"));
    public static final RegistryKey<Biome> PE_DESERT_KEY = OldBiomes.register(ModernBeta.createId("pe_desert"));
    public static final RegistryKey<Biome> PE_SAVANNA_KEY = OldBiomes.register(ModernBeta.createId("pe_savanna"));
    public static final RegistryKey<Biome> PE_PLAINS_KEY = OldBiomes.register(ModernBeta.createId("pe_plains"));
    public static final RegistryKey<Biome> PE_SEASONAL_FOREST_KEY = OldBiomes.register(ModernBeta.createId("pe_seasonal_forest"));
    public static final RegistryKey<Biome> PE_RAINFOREST_KEY = OldBiomes.register(ModernBeta.createId("pe_rainforest"));
    public static final RegistryKey<Biome> PE_SWAMPLAND_KEY = OldBiomes.register(ModernBeta.createId("pe_swampland"));
    public static final RegistryKey<Biome> PE_TAIGA_KEY = OldBiomes.register(ModernBeta.createId("pe_taiga"));
    public static final RegistryKey<Biome> PE_TUNDRA_KEY = OldBiomes.register(ModernBeta.createId("pe_tundra"));
    public static final RegistryKey<Biome> PE_ICE_DESERT_KEY = OldBiomes.register(ModernBeta.createId("pe_ice_desert"));
    
    public static final RegistryKey<Biome> PE_OCEAN_KEY = OldBiomes.register(ModernBeta.createId("pe_ocean"));
    public static final RegistryKey<Biome> PE_LUKEWARM_OCEAN_KEY = OldBiomes.register(ModernBeta.createId("pe_lukewarm_ocean"));
    public static final RegistryKey<Biome> PE_WARM_OCEAN_KEY = OldBiomes.register(ModernBeta.createId("pe_warm_ocean"));
    public static final RegistryKey<Biome> PE_COLD_OCEAN_KEY = OldBiomes.register(ModernBeta.createId("pe_cold_ocean"));
    public static final RegistryKey<Biome> PE_FROZEN_OCEAN_KEY = OldBiomes.register(ModernBeta.createId("pe_frozen_ocean"));
    
    public static final ImmutableList<RegistryKey<Biome>> BIOME_KEYS = ImmutableList.of(
        PE_FOREST_KEY,
        PE_SHRUBLAND_KEY,
        PE_DESERT_KEY,
        PE_SAVANNA_KEY, 
        PE_PLAINS_KEY,
        PE_SEASONAL_FOREST_KEY, 
        PE_RAINFOREST_KEY,
        PE_SWAMPLAND_KEY, 
        PE_TAIGA_KEY,
        PE_TUNDRA_KEY, 
        PE_ICE_DESERT_KEY,
        
        PE_OCEAN_KEY, 
        PE_LUKEWARM_OCEAN_KEY,
        PE_WARM_OCEAN_KEY, 
        PE_COLD_OCEAN_KEY,
        PE_FROZEN_OCEAN_KEY
    );
    
    public static void registerBiomes() {
        OldBiomes.register(PE_FOREST_KEY, PEForest.BIOME);
        OldBiomes.register(PE_SHRUBLAND_KEY, PEShrubland.BIOME);
        OldBiomes.register(PE_DESERT_KEY, PEDesert.BIOME);
        OldBiomes.register(PE_SAVANNA_KEY, PESavanna.BIOME);
        OldBiomes.register(PE_PLAINS_KEY, PEPlains.BIOME);
        OldBiomes.register(PE_SEASONAL_FOREST_KEY, PESeasonalForest.BIOME);
        OldBiomes.register(PE_RAINFOREST_KEY, PERainforest.BIOME);
        OldBiomes.register(PE_SWAMPLAND_KEY, PESwampland.BIOME);
        OldBiomes.register(PE_TAIGA_KEY, PETaiga.BIOME);
        OldBiomes.register(PE_TUNDRA_KEY, PETundra.BIOME);
        OldBiomes.register(PE_ICE_DESERT_KEY, PEIceDesert.BIOME);
        
        OldBiomes.register(PE_OCEAN_KEY, PEOcean.BIOME);
        OldBiomes.register(PE_LUKEWARM_OCEAN_KEY, PELukewarmOcean.BIOME);
        OldBiomes.register(PE_WARM_OCEAN_KEY, PEWarmOcean.BIOME);
        OldBiomes.register(PE_COLD_OCEAN_KEY, PEColdOcean.BIOME);
        OldBiomes.register(PE_FROZEN_OCEAN_KEY, PEFrozenOcean.BIOME);
    }
}   
