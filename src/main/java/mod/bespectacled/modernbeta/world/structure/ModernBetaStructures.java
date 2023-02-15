package mod.bespectacled.modernbeta.world.structure;

import java.util.Map;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.data.ModernBetaTagProviderBiome;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.world.StructureSpawns;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureTerrainAdaptation;
import net.minecraft.world.gen.structure.StrongholdStructure;
import net.minecraft.world.gen.structure.Structure;

public class ModernBetaStructures {
    public static final RegistryKey<Structure> INDEV_STRONGHOLD = RegistryKey.of(RegistryKeys.STRUCTURE, ModernBeta.createId("indev_stronghold"));
    
    public static void bootstrap(Registerable<Structure> structureRegisterable) {
        RegistryEntryLookup<Biome> registryBiome = structureRegisterable.getRegistryLookup(RegistryKeys.BIOME);
        
        structureRegisterable.register(
            INDEV_STRONGHOLD,
            new StrongholdStructure(createConfig(registryBiome.getOrThrow(ModernBetaTagProviderBiome.INDEV_STRONGHOLD_HAS_STRUCTURE), StructureTerrainAdaptation.BURY))
        );
    }
    
    private static Structure.Config createConfig(RegistryEntryList<Biome> biomes, StructureTerrainAdaptation terrainAdaptation) {
        return createConfig(biomes, Map.of(), GenerationStep.Feature.SURFACE_STRUCTURES, terrainAdaptation);
    }
    
    private static Structure.Config createConfig(RegistryEntryList<Biome> biomes, Map<SpawnGroup, StructureSpawns> spawns, GenerationStep.Feature featureStep, StructureTerrainAdaptation terrainAdaptation) {
        return new Structure.Config(biomes, spawns, featureStep, terrainAdaptation);
    }
}
