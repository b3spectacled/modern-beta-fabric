package mod.bespectacled.modernbeta.compat;

import mod.bespectacled.modernbeta.world.biome.OldBiomes;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.GenerationStep;

public class CompatTechReborn {
    private static final String MOD_ID = "techreborn";
    private static final String FEATURE_PREFIX = "";
    private static final String FEATURE_SUFFIX = "";
    
    private static final String[] ORES = {
        /*
        "bauxite_ore",
        "galena_ore",
        "iridium_ore",
        "lead_ore",
        "ruby_ore",
        "sapphire_ore",
        "silver_ore",
        "tin_ore"
        */
    };
    
    public static void addCompat() {
        for (String o : ORES) {
            BiomeModifications.addFeature(
                BiomeSelectors.includeByKey(
                    OldBiomes.MODERN_BETA_BIOME_MAP
                    .keySet()
                    .stream()
                    .toList()
                ), 
                GenerationStep.Feature.UNDERGROUND_ORES,  
                RegistryKey.of(
                    Registry.PLACED_FEATURE_KEY, 
                    new Identifier(MOD_ID, FEATURE_PREFIX + o + FEATURE_SUFFIX)
                )
            );
        }
    }
}