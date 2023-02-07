package mod.bespectacled.modernbeta.world.structure;

import mod.bespectacled.modernbeta.ModernBeta;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.structure.StructureSet;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.placement.ConcentricRingsStructurePlacement;
import net.minecraft.world.gen.structure.Structure;

public class ModernBetaStructureSets {
    public static final RegistryKey<StructureSet> INDEV_STRONGHOLDS = keyOf(ModernBeta.createId("indev_strongholds"));
    
    public static void bootstrap(Registerable<StructureSet> structureSetRegisterable) {
        RegistryEntryLookup<Structure> registryStructure = structureSetRegisterable.getRegistryLookup(RegistryKeys.STRUCTURE);
        RegistryEntryLookup<Biome> registryBiome = structureSetRegisterable.getRegistryLookup(RegistryKeys.BIOME);
        
        structureSetRegisterable.register(
            INDEV_STRONGHOLDS,
            new StructureSet(
                registryStructure.getOrThrow(ModernBetaStructures.INDEV_STRONGHOLD),
                new ConcentricRingsStructurePlacement(0, 0, 1, registryBiome.getOrThrow(BiomeTags.STRONGHOLD_BIASED_TO))
            )
        );
    }
    
    private static RegistryKey<StructureSet> keyOf(Identifier id) {
        return RegistryKey.of(RegistryKeys.STRUCTURE_SET, id);
    }
}
