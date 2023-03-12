package mod.bespectacled.modernbeta.world.biome.beta;

import com.google.common.collect.ImmutableList;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.world.biome.OldBiomes;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public class BetaBiomes {
    protected static final boolean ADD_LAKES = true;
    protected static final boolean ADD_SPRINGS = true;
    
    protected static final boolean ADD_ALTERNATE_STONES = true;
    protected static final boolean ADD_NEW_MINEABLES = true;
    
    public static final RegistryKey<Biome> FOREST_KEY = OldBiomes.register(ModernBeta.createId("beta_forest"));
    public static final RegistryKey<Biome> SHRUBLAND_KEY = OldBiomes.register(ModernBeta.createId("beta_shrubland"));
    public static final RegistryKey<Biome> DESERT_KEY = OldBiomes.register(ModernBeta.createId("beta_desert"));
    public static final RegistryKey<Biome> SAVANNA_KEY = OldBiomes.register(ModernBeta.createId("beta_savanna"));
    public static final RegistryKey<Biome> PLAINS_KEY = OldBiomes.register(ModernBeta.createId("beta_plains"));
    public static final RegistryKey<Biome> SEASONAL_FOREST_KEY = OldBiomes.register(ModernBeta.createId("beta_seasonal_forest"));
    public static final RegistryKey<Biome> RAINFOREST_KEY = OldBiomes.register(ModernBeta.createId("beta_rainforest"));
    public static final RegistryKey<Biome> SWAMPLAND_KEY = OldBiomes.register(ModernBeta.createId("beta_swampland"));
    public static final RegistryKey<Biome> TAIGA_KEY = OldBiomes.register(ModernBeta.createId("beta_taiga"));
    public static final RegistryKey<Biome> TUNDRA_KEY = OldBiomes.register(ModernBeta.createId("beta_tundra"));
    public static final RegistryKey<Biome> ICE_DESERT_KEY = OldBiomes.register(ModernBeta.createId("beta_ice_desert"));

    public static final RegistryKey<Biome> OCEAN_KEY = OldBiomes.register(ModernBeta.createId("beta_ocean"));
    public static final RegistryKey<Biome> LUKEWARM_OCEAN_KEY = OldBiomes.register(ModernBeta.createId("beta_lukewarm_ocean"));
    public static final RegistryKey<Biome> WARM_OCEAN_KEY = OldBiomes.register(ModernBeta.createId("beta_warm_ocean"));
    public static final RegistryKey<Biome> COLD_OCEAN_KEY = OldBiomes.register(ModernBeta.createId("beta_cold_ocean"));
    public static final RegistryKey<Biome> FROZEN_OCEAN_KEY = OldBiomes.register(ModernBeta.createId("beta_frozen_ocean"));
    
    public static final RegistryKey<Biome> SKY_KEY = OldBiomes.register(ModernBeta.createId("beta_sky"));
    
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
        OldBiomes.register(FOREST_KEY, Forest.BIOME);
        OldBiomes.register(SHRUBLAND_KEY, Shrubland.BIOME);
        OldBiomes.register(DESERT_KEY, Desert.BIOME);
        OldBiomes.register(SAVANNA_KEY, Savanna.BIOME);
        OldBiomes.register(PLAINS_KEY, Plains.BIOME);
        OldBiomes.register(SEASONAL_FOREST_KEY, SeasonalForest.BIOME);
        OldBiomes.register(RAINFOREST_KEY, Rainforest.BIOME);
        OldBiomes.register(SWAMPLAND_KEY, Swampland.BIOME);
        OldBiomes.register(TAIGA_KEY, Taiga.BIOME);
        OldBiomes.register(TUNDRA_KEY, Tundra.BIOME);
        OldBiomes.register(ICE_DESERT_KEY, IceDesert.BIOME);
        
        OldBiomes.register(OCEAN_KEY, Ocean.BIOME);
        OldBiomes.register(LUKEWARM_OCEAN_KEY, LukewarmOcean.BIOME);
        OldBiomes.register(WARM_OCEAN_KEY, WarmOcean.BIOME);
        OldBiomes.register(COLD_OCEAN_KEY, ColdOcean.BIOME);
        OldBiomes.register(FROZEN_OCEAN_KEY, FrozenOcean.BIOME);
        
        OldBiomes.register(SKY_KEY, Sky.BIOME);
    }
}
