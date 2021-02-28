package com.bespectacled.modernbeta.compat;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.GenerationStep;

@SuppressWarnings("deprecation")
public class CompatTechReborn {
    private static final String MOD_ID = "techreborn";
    private static final String FEATURE_PREFIX = "techreborn/features/";
    private static final String FEATURE_SUFFIX = ".json";
    
    private static final String[] ORES = {
        "bauxite_ore",      
        "copper_ore",
        "galena_ore",
        "iridium_ore",
        "lead_ore",
        "ruby_ore",
        "sapphire_ore",
        "silver_ore",
        "tin_ore"
    };
    
    public static void addCompat() {
        for (String o : ORES) {
            BiomeModifications.addFeature(
                BiomeSelectors.includeByKey(Compat.MODERN_BETA_BIOMES), 
                GenerationStep.Feature.UNDERGROUND_ORES,  
                RegistryKey.of(
                    Registry.CONFIGURED_FEATURE_WORLDGEN, 
                    new Identifier(MOD_ID, FEATURE_PREFIX + o + FEATURE_SUFFIX)
                )
            );
        }
    }
    
}
