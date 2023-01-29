package mod.bespectacled.modernbeta.world.biome.beta;

import com.google.common.collect.ImmutableList;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomes;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public class BetaBiomes {
    protected static final boolean ADD_LAKES = true;
    protected static final boolean ADD_SPRINGS = true;
    
    protected static final boolean ADD_ALTERNATE_STONES = true;
    protected static final boolean ADD_NEW_MINEABLES = true;
    
    public static final RegistryKey<Biome> FOREST_KEY = ModernBetaBiomes.register(ModernBeta.createId("beta_forest"));
    public static final RegistryKey<Biome> SHRUBLAND_KEY = ModernBetaBiomes.register(ModernBeta.createId("beta_shrubland"));
    public static final RegistryKey<Biome> DESERT_KEY = ModernBetaBiomes.register(ModernBeta.createId("beta_desert"));
    public static final RegistryKey<Biome> SAVANNA_KEY = ModernBetaBiomes.register(ModernBeta.createId("beta_savanna"));
    public static final RegistryKey<Biome> PLAINS_KEY = ModernBetaBiomes.register(ModernBeta.createId("beta_plains"));
    public static final RegistryKey<Biome> SEASONAL_FOREST_KEY = ModernBetaBiomes.register(ModernBeta.createId("beta_seasonal_forest"));
    public static final RegistryKey<Biome> RAINFOREST_KEY = ModernBetaBiomes.register(ModernBeta.createId("beta_rainforest"));
    public static final RegistryKey<Biome> SWAMPLAND_KEY = ModernBetaBiomes.register(ModernBeta.createId("beta_swampland"));
    public static final RegistryKey<Biome> TAIGA_KEY = ModernBetaBiomes.register(ModernBeta.createId("beta_taiga"));
    public static final RegistryKey<Biome> TUNDRA_KEY = ModernBetaBiomes.register(ModernBeta.createId("beta_tundra"));
    public static final RegistryKey<Biome> ICE_DESERT_KEY = ModernBetaBiomes.register(ModernBeta.createId("beta_ice_desert"));

    public static final RegistryKey<Biome> OCEAN_KEY = ModernBetaBiomes.register(ModernBeta.createId("beta_ocean"));
    public static final RegistryKey<Biome> LUKEWARM_OCEAN_KEY = ModernBetaBiomes.register(ModernBeta.createId("beta_lukewarm_ocean"));
    public static final RegistryKey<Biome> WARM_OCEAN_KEY = ModernBetaBiomes.register(ModernBeta.createId("beta_warm_ocean"));
    public static final RegistryKey<Biome> COLD_OCEAN_KEY = ModernBetaBiomes.register(ModernBeta.createId("beta_cold_ocean"));
    public static final RegistryKey<Biome> FROZEN_OCEAN_KEY = ModernBetaBiomes.register(ModernBeta.createId("beta_frozen_ocean"));
    
    public static final RegistryKey<Biome> SKY_KEY = ModernBetaBiomes.register(ModernBeta.createId("beta_sky"));
    
    public static final ImmutableList<RegistryKey<Biome>> BIOME_KEYS = ImmutableList.of(
        FOREST_KEY,
        SHRUBLAND_KEY, 
        DESERT_KEY,
        SAVANNA_KEY, 
        PLAINS_KEY,
        SEASONAL_FOREST_KEY, 
        RAINFOREST_KEY,
        SWAMPLAND_KEY, 
        TAIGA_KEY,
        TUNDRA_KEY, 
        ICE_DESERT_KEY,

        OCEAN_KEY, 
        LUKEWARM_OCEAN_KEY,
        WARM_OCEAN_KEY, 
        COLD_OCEAN_KEY,
        FROZEN_OCEAN_KEY,

        SKY_KEY
    );
    
    public static void registerBiomes() {
        ModernBetaBiomes.register(FOREST_KEY, BetaForest.BIOME);
        ModernBetaBiomes.register(SHRUBLAND_KEY, BetaShrubland.BIOME);
        ModernBetaBiomes.register(DESERT_KEY, BetaDesert.BIOME);
        ModernBetaBiomes.register(SAVANNA_KEY, BetaSavanna.BIOME);
        ModernBetaBiomes.register(PLAINS_KEY, BetaPlains.BIOME);
        ModernBetaBiomes.register(SEASONAL_FOREST_KEY, BetaSeasonalForest.BIOME);
        ModernBetaBiomes.register(RAINFOREST_KEY, BetaRainforest.BIOME);
        ModernBetaBiomes.register(SWAMPLAND_KEY, BetaSwampland.BIOME);
        ModernBetaBiomes.register(TAIGA_KEY, BetaTaiga.BIOME);
        ModernBetaBiomes.register(TUNDRA_KEY, BetaTundra.BIOME);
        ModernBetaBiomes.register(ICE_DESERT_KEY, BetaIceDesert.BIOME);
        
        ModernBetaBiomes.register(OCEAN_KEY, BetaOcean.BIOME);
        ModernBetaBiomes.register(LUKEWARM_OCEAN_KEY, BetaLukewarmOcean.BIOME);
        ModernBetaBiomes.register(WARM_OCEAN_KEY, BetaWarmOcean.BIOME);
        ModernBetaBiomes.register(COLD_OCEAN_KEY, BetaColdOcean.BIOME);
        ModernBetaBiomes.register(FROZEN_OCEAN_KEY, BetaFrozenOcean.BIOME);
        
        ModernBetaBiomes.register(SKY_KEY, BetaSky.BIOME);
    }
}
