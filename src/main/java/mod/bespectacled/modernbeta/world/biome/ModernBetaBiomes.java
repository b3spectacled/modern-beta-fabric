package mod.bespectacled.modernbeta.world.biome;

import mod.bespectacled.modernbeta.ModernBeta;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.OverworldBiomeCreator;

public class ModernBetaBiomes {
    public static final Identifier BETA_FOREST_ID = ModernBeta.createId("beta_forest");
    public static final Identifier BETA_SHRUBLAND_ID = ModernBeta.createId("beta_shrubland");
    public static final Identifier BETA_DESERT_ID = ModernBeta.createId("beta_desert");
    public static final Identifier BETA_SAVANNA_ID = ModernBeta.createId("beta_savanna");
    public static final Identifier BETA_PLAINS_ID = ModernBeta.createId("beta_plains");
    public static final Identifier BETA_SEASONAL_FOREST_ID = ModernBeta.createId("beta_seasonal_forest");
    public static final Identifier BETA_RAINFOREST_ID = ModernBeta.createId("beta_rainforest");
    public static final Identifier BETA_SWAMPLAND_ID = ModernBeta.createId("beta_swampland");
    public static final Identifier BETA_TAIGA_ID = ModernBeta.createId("beta_taiga");
    public static final Identifier BETA_TUNDRA_ID = ModernBeta.createId("beta_tundra");
    public static final Identifier BETA_ICE_DESERT_ID = ModernBeta.createId("beta_ice_desert");

    public static final Identifier BETA_OCEAN_ID = ModernBeta.createId("beta_ocean");
    public static final Identifier BETA_LUKEWARM_OCEAN_ID = ModernBeta.createId("beta_lukewarm_ocean");
    public static final Identifier BETA_WARM_OCEAN_ID = ModernBeta.createId("beta_warm_ocean");
    public static final Identifier BETA_COLD_OCEAN_ID = ModernBeta.createId("beta_cold_ocean");
    public static final Identifier BETA_FROZEN_OCEAN_ID = ModernBeta.createId("beta_frozen_ocean");
    
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
    
    public static final Identifier ALPHA_ID = ModernBeta.createId("alpha");
    public static final Identifier ALPHA_WINTER_ID = ModernBeta.createId("alpha_winter");
    
    public static final Identifier INFDEV_611_ID = ModernBeta.createId("infdev_611");
    public static final Identifier INFDEV_420_ID = ModernBeta.createId("infdev_420");
    public static final Identifier INFDEV_415_ID = ModernBeta.createId("infdev_415");
    public static final Identifier INFDEV_227_ID = ModernBeta.createId("infdev_227");
    
    public static final Identifier INDEV_NORMAL_ID = ModernBeta.createId("indev_normal");
    public static final Identifier INDEV_HELL_ID = ModernBeta.createId("indev_hell");
    public static final Identifier INDEV_PARADISE_ID = ModernBeta.createId("indev_paradise");
    public static final Identifier INDEV_WOODS_ID = ModernBeta.createId("indev_woods");
    
    public static void register() {
        register(BETA_FOREST_ID);
        register(BETA_SHRUBLAND_ID);
        register(BETA_DESERT_ID);
        register(BETA_SAVANNA_ID);
        register(BETA_PLAINS_ID);
        register(BETA_SEASONAL_FOREST_ID);
        register(BETA_RAINFOREST_ID);
        register(BETA_SWAMPLAND_ID);
        register(BETA_TAIGA_ID);
        register(BETA_TUNDRA_ID);
        register(BETA_ICE_DESERT_ID);

        register(BETA_OCEAN_ID);
        register(BETA_LUKEWARM_OCEAN_ID);
        register(BETA_WARM_OCEAN_ID);
        register(BETA_COLD_OCEAN_ID);
        register(BETA_FROZEN_OCEAN_ID);

        register(PE_FOREST_ID);
        register(PE_SHRUBLAND_ID);
        register(PE_DESERT_ID);
        register(PE_SAVANNA_ID);
        register(PE_PLAINS_ID);
        register(PE_SEASONAL_FOREST_ID);
        register(PE_RAINFOREST_ID);
        register(PE_SWAMPLAND_ID);
        register(PE_TAIGA_ID);
        register(PE_TUNDRA_ID);
        register(PE_ICE_DESERT_ID);

        register(PE_OCEAN_ID);
        register(PE_LUKEWARM_OCEAN_ID);
        register(PE_WARM_OCEAN_ID);
        register(PE_COLD_OCEAN_ID);
        register(PE_FROZEN_OCEAN_ID);

        register(ALPHA_ID);
        register(ALPHA_WINTER_ID);

        register(INFDEV_611_ID);
        register(INFDEV_420_ID);
        register(INFDEV_415_ID);
        register(INFDEV_227_ID);

        register(INDEV_NORMAL_ID);
        register(INDEV_HELL_ID);
        register(INDEV_PARADISE_ID);
        register(INDEV_WOODS_ID);
    }
    
    private static void register(Identifier id) {
        Registry.register(BuiltinRegistries.BIOME, keyOf(id), OverworldBiomeCreator.createPlains(false, false, false));
    }
    
    private static RegistryKey<Biome> keyOf(Identifier id) {
        return RegistryKey.of(Registry.BIOME_KEY, id);
    }
}
